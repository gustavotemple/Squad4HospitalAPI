package gestao.service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import gestao.exceptions.BadEnderecoException;
import gestao.exceptions.HospitalNotFoundException;
import gestao.models.Hospital;
import gestao.models.HospitalDTO;
import gestao.models.Leito;
import gestao.models.geocode.Geocode;
import gestao.repositories.HospitalRepository;

@Service
@EnableFeignClients
public class HospitalServiceImpl implements HospitalService {

	private static final String KEY = "AIzaSyAczcT1dO-iPGi273Mu3fr9uxJoUgArfyI";

	private static int MIN_LEITOS_DISPONIVEIS = 0;
	private static int QTD_MIN_PRODUTOS = 4;

	private GeocodeClient geocodeClient;
	private HospitalRepository hospitalRepository;

	@Autowired
	public HospitalServiceImpl(GeocodeClient geocodeClient, HospitalRepository hospitalRepository) {
		this.geocodeClient = geocodeClient;
		this.hospitalRepository = hospitalRepository;
	}

	/**
	 * buscaCoordenadasPor
	 * 
	 * @param endereco String
	 * @return GeoJsonPoint
	 */
	private GeoJsonPoint buscaCoordenadasPor(final String endereco) {
		final Geocode geocode = geocodeClient.buscaCoordenadasPor(endereco, KEY);

		if (geocode.getResults().isEmpty())
			throw new BadEnderecoException();

		final GeoJsonPoint point = new GeoJsonPoint(geocode.getResults().get(0).getGeometry().getLocation().getLng(),
				geocode.getResults().get(0).getGeometry().getLocation().getLat());

		return point;
	}

	@Override
	public Hospital addHospital(HospitalDTO hospitalDTO) {
		final Hospital hospital = hospitalDTO.toHospital();

		hospital.setLocalizacao(buscaCoordenadasPor(hospital.getEndereco()));

		return hospitalRepository.save(hospital);
	}

	/**
	 * Caso o hospital precise de um produto, por exemplo, um banco de sangue, eh
	 * importante fazer o envio do hospital mais proximo ao local.
	 * 
	 * @param id do hospital solicitante
	 * @return Hospital
	 */
	@Override
	public Hospital getHospitalNearById(ObjectId id) {
		Hospital hospital = getHospitalById(id);

		final Point point = buscaCoordenadasPor(hospital.getEndereco());

		final List<Hospital> hospitals = hospitalRepository.findByLocalizacaoNear(point).stream()
				.filter(hosp -> !hosp.getObjectId().equals(id)).collect(Collectors.toList());		

		Hospital hospitalMaisProximo = hospitals.stream().filter(produtosDisponiveis()).findFirst().orElse(null);

		if (hospitalMaisProximo == null) {
			throw new HospitalNotFoundException();
		}

		return hospitalMaisProximo;
	}

	private Predicate<Hospital> produtosDisponiveis() {
		return hospital -> hospital.getEstoque().stream().allMatch(prod -> prod.getQuantidade() > QTD_MIN_PRODUTOS)
				&& !hospital.getEstoque().isEmpty();
	}

	/**
	 * Ao encontrar um paciente eh importante o recomendar para um hospital mais
	 * proximo e que tenha vaga disponivel.
	 * 
	 * @param endereco do paciente
	 * @return Hospital
	 */
	@Override
	public Hospital getHospitalNearByAddress(String endereco) {
		Point point;

		if (!Objects.isNull(endereco))
			point = buscaCoordenadasPor(endereco);
		else
			throw new BadEnderecoException();

		List<Hospital> hospitals = hospitalRepository.findByLocalizacaoNear(point);

		if (hospitals.isEmpty())
			throw new HospitalNotFoundException();

		Hospital hospital;
		try {
			hospital = hospitals.stream().filter(leitosEProdutosDisponiveis()).findFirst().get();
		} catch (NoSuchElementException e) {
			throw new HospitalNotFoundException();
		}

		return hospital;
	}

	private Predicate<Hospital> leitosEProdutosDisponiveis() {
		return hospital -> hospital.getLeitosDisponiveis() > MIN_LEITOS_DISPONIVEIS
				&& hospital.getEstoque().stream().allMatch(prod -> prod.getQuantidade() > QTD_MIN_PRODUTOS)
				&& !hospital.getEstoque().isEmpty();
	}

	@Override
	public Hospital getHospitalById(ObjectId hospitalId) {
		final Hospital hospital = hospitalRepository.findOne(hospitalId);
		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);
		return hospital;
	}

	@Override
	public Hospital updateHospital(ObjectId hospitalId, HospitalDTO hospitalDTO) {
		final Hospital hospital = getHospitalById(hospitalId);

		hospital.setEndereco(hospitalDTO.getEndereco());
		hospital.setNome(hospitalDTO.getNome());
		hospital.setLeitosTotais(hospitalDTO.getLeitosTotais());

		return hospitalRepository.save(hospital);
	}

	@Override
	public Collection<Leito> getLeitosById(ObjectId hospitalId) {
		final Hospital hospital = getHospitalById(hospitalId);

		return hospital.getLeitos();
	}

	@Override
	public void deleteHospital(ObjectId hospitalId) {
		if (!hospitalRepository.exists(hospitalId))
			throw new HospitalNotFoundException(hospitalId);

		hospitalRepository.delete(hospitalId);
	}

	@Override
	public Collection<Hospital> findAll() {
		
		return hospitalRepository.findAll();
	}

}
