package gestao.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import gestao.controllers.HospitalController;
import gestao.exceptions.BadEnderecoException;
import gestao.exceptions.HospitalNotFoundException;
import gestao.models.Hospital;
import gestao.models.Leito;
import gestao.models.geocode.Geocode;
import gestao.repositories.HospitalRepository;

@Service
@EnableFeignClients
public class HospitalService {

	private static final String KEY = "AIzaSyAczcT1dO-iPGi273Mu3fr9uxJoUgArfyI";

	private static int MIN_LEITOS_DISPONIVEIS = 0;
	private static int QTD_MIN_PRODUTOS = 4;

	@Autowired
	private GeocodeClient geocodeClient;
	@Autowired
	private HospitalRepository hospitalRepository;

	public Hospital addHospital(Hospital hospital) {
		hospital.setLocalizacao(buscaCoordenadasPor(hospital.getEndereco()));

		hospitalRepository.save(hospital);

		return hospital;

	}

	public Hospital getHospital(ObjectId hospitalId) {
		final Hospital hospital = findHospitalBy(hospitalId);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return hospital;

	}

	public Hospital updateHospital(ObjectId hospitalId, Hospital hospitalUpdate) {

		final Hospital hospital = findHospitalBy(hospitalId);

		hospital.setEndereco(hospitalUpdate.getEndereco());
		hospital.setNome(hospitalUpdate.getNome());
		hospital.setLeitosTotais(hospitalUpdate.getLeitosTotais());

		hospitalRepository.save(hospital);		

		return hospital;

	}

	public Collection<Leito> getLeitosHospital(ObjectId hospitalId) {

		final Hospital hospital = findHospitalBy(hospitalId);

		return hospital.getLeitos();

	}

	public void deleteHospital(String hospitalId) {
		if (!hospitalRepository.exists(hospitalId))
			throw new HospitalNotFoundException(hospitalId);

		hospitalRepository.delete(hospitalId);
	}

	public List<Hospital> getHospitais() {
		return hospitalRepository.findAll();
	}

	public GeoJsonPoint buscaCoordenadasPor(final String endereco) {
		final Geocode geocode = geocodeClient.buscaCoordenadasPor(endereco, KEY);

		if (geocode.getResults().isEmpty())
			throw new BadEnderecoException();

		final GeoJsonPoint point = new GeoJsonPoint(geocode.getResults().get(0).getGeometry().getLocation().getLng(),
				geocode.getResults().get(0).getGeometry().getLocation().getLat());

		return point;
	}

	/**
	 * Caso o hospital precise de um produto, por exemplo, um banco de sangue, eh
	 * importante fazer o envio do hospital mais proximo ao local.
	 * 
	 * @param endereco do hospital
	 * @return Hospital
	 */
	public Hospital hospitalMaisProximoHospital(final ObjectId hospitalId) {

		final Hospital hospital = findHospitalBy(hospitalId);
		final Point point = buscaCoordenadasPor(hospital.getEndereco());

		final List<Hospital> hospitals = hospitalRepository.findByLocalizacaoNear(point).stream()
				.filter(hosp -> !hosp.getObjectId().equals(hospitalId)).collect(Collectors.toList());

		Hospital hospitalMaisProximo = hospitals.stream().filter(produtosDisponiveis()).findFirst().orElse(null);

		if (hospitalMaisProximo == null) {
			throw new HospitalNotFoundException();
		}

		return hospitalMaisProximo;
	}

	/**
	 * Ao encontrar um paciente eh importante o recomendar para um hospital mais
	 * proximo e que tenha vaga disponivel.
	 * 
	 * @param endereco do paciente
	 * @return Hospital
	 */
	public Hospital hospitalMaisProximoPaciente(final String endereco) {
		Point point;

		if (!Objects.isNull(endereco))
			point = buscaCoordenadasPor(endereco);
		else
			throw new BadEnderecoException();

		List<Hospital> hospitals = hospitalRepository.findByLocalizacaoNear(point);

		Hospital hospitalMaisProximo = hospitals.stream().filter(leitosEProdutosDisponiveis()).findFirst().orElse(null);

		if (hospitalMaisProximo == null) {
			throw new HospitalNotFoundException();
		}

		return hospitalMaisProximo;

	}

	private Predicate<Hospital> leitosEProdutosDisponiveis() {
		return hospital -> hospital.getLeitosDisponiveis() > MIN_LEITOS_DISPONIVEIS
				&& hospital.getEstoque().stream().allMatch(prod -> prod.getQuantidade() > QTD_MIN_PRODUTOS)
				&& !hospital.getEstoque().isEmpty();
	}

	private Predicate<Hospital> produtosDisponiveis() {
		return hospital -> hospital.getEstoque().stream().allMatch(prod -> prod.getQuantidade() > QTD_MIN_PRODUTOS)
				&& !hospital.getEstoque().isEmpty();
	}

	private Hospital findHospitalBy(ObjectId hospitalId) {
		final Hospital hospital = hospitalRepository.findBy_id(hospitalId);
		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);
		return hospital;
	}

}
