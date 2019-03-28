package com.acelera.squad.four.hospital.service;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.geocode.Geocode;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;

@Service
@EnableFeignClients
public class HospitalService {

	private static final String KEY = "AIzaSyAczcT1dO-iPGi273Mu3fr9uxJoUgArfyI";

	private static int MIN_LEITOS_DISPONIVEIS = 0;
	private int QTD_MIN_PRODUTOS = 4;

	@Autowired
	private GeocodeClient geocodeClient;
	@Autowired
	private HospitalRepository hospitalRepository;

	public GeoJsonPoint buscaCoordenadasPor(final String endereco) {
		final Geocode geocode = geocodeClient.buscaCoordenadasPor(endereco, KEY);
		
		if(geocode.getResults().isEmpty()) {
			throw new IllegalArgumentException("Endereço nao existente");
		}		

		final GeoJsonPoint point = new GeoJsonPoint(geocode.getResults().get(0).getGeometry().getLocation().getLng(),
				geocode.getResults().get(0).getGeometry().getLocation().getLat());

		return point;
	}

	public Hospital buscaHospitaisPor(final String endereco, final Float lat, final Float lng) {
		Point point;

		if (!Objects.isNull(endereco))
			point = buscaCoordenadasPor(endereco);
		else if (!Objects.isNull(lat) && !Objects.isNull(lng))
			point = new Point(lng, lat);
		else
			throw new IllegalArgumentException("Pametro nao preenchido");

		List<Hospital> hospitals = hospitalRepository.findByLocalizacaoNear(point);
			
		return hospitals.stream()
				.filter(hospital -> hospital.getLeitos().size() > MINIMUM)
				.skip(1).findFirst().get();

	
	public Hospital hospitalMaisProximoPaciente(final String endereco) {
		Point point;

		if (!Objects.isNull(endereco))
			point = buscaCoordenadasPor(endereco);		
		else
			throw new IllegalArgumentException("Pametro nao preenchido");

		List<Hospital> hospitals = hospitalRepository.findByLocalizacaoNear(point);

		return hospitals.stream().filter(leitosEProdutosDisponiveis())
				.findFirst().get();			
	}
	
	private Predicate<Hospital> leitosEProdutosDisponiveis(){
		return hospital -> hospital.getLeitosDisponiveis() > MIN_LEITOS_DISPONIVEIS && 
				hospital.getEstoque().stream().allMatch(prod -> prod.getQuantidade() > QTD_MIN_PRODUTOS) &&
				!hospital.getEstoque().isEmpty();

	}

}
