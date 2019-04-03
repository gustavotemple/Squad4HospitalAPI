package gestao.service;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import gestao.exceptions.BadEnderecoException;
import gestao.models.Hospital;
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

	public GeoJsonPoint buscaCoordenadasPor(final String endereco) {
		final Geocode geocode = geocodeClient.buscaCoordenadasPor(endereco, KEY);
		
		if(geocode.getResults().isEmpty())
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
	public Hospital hospitalMaisProximoHospital(final Hospital hospital) {
		final Point point = buscaCoordenadasPor(hospital.getEndereco());

		final List<Hospital> hospitals = hospitalRepository.findByLocalizacaoNear(point);
			
		return hospitals.stream()
				.filter(produtosDisponiveis())
				.skip(1).findFirst().get();
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

		return hospitals.stream().filter(leitosEProdutosDisponiveis())
				.findFirst().get();			
	}
	
	private Predicate<Hospital> leitosEProdutosDisponiveis(){
		return hospital -> hospital.getLeitosDisponiveis() > MIN_LEITOS_DISPONIVEIS && 
				hospital.getEstoque().stream().allMatch(prod -> prod.getQuantidade() > QTD_MIN_PRODUTOS) &&
				!hospital.getEstoque().isEmpty();
	}
	
	private Predicate<Hospital> produtosDisponiveis(){
		return hospital -> hospital.getEstoque().stream().allMatch(prod -> prod.getQuantidade() > QTD_MIN_PRODUTOS) &&
				!hospital.getEstoque().isEmpty();
	}

}
