package com.acelera.squad.four.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.models.Sphere;
import com.acelera.squad.four.hospital.models.geocode.Geocode;

@Service
@EnableFeignClients
public class HospitalService {
	
	private static final String KEY = "AIzaSyBa6MxMRKgE8wSfWutmzp3hNajttONXMuo";
	
	@Autowired
	private GeocodeClient geocodeClient;
		
	public Sphere buscaCoordenadasPor(String endereco) {
		Sphere sphere = new Sphere();

		Geocode geocode = geocodeClient.buscaCoordenadasPor(endereco, KEY);
		
		sphere.getCoordenates().add(geocode.getResults().get(0).getGeometry().getLocation().getLat());
		sphere.getCoordenates().add(geocode.getResults().get(0).getGeometry().getLocation().getLng());

		return sphere;
	}
	
}
