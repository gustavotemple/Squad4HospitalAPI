package com.acelera.squad.four.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.models.Sphere;
import com.acelera.squad.four.hospital.models.geocode.Geocode;

@Service
@EnableFeignClients
public class HospitalService {
	
	private static final String KEY = "AIzaSyBa6MxMRKgE8wSfWutmzp3hNajttONXMuo";
	
	private final GeocodeClient geocodeClient;
	
	@Autowired
	public HospitalService(GeocodeClient geocodeClient) {
		this.geocodeClient = geocodeClient;
	}
	
	public Sphere buscaCoordenadasPor(String endereco) {
		Sphere sphere = new Sphere();

		Geocode geocode = geocodeClient.buscaCoordenadasPor(endereco, KEY);

		System.out.println(geocode.getStatus());

		return sphere;
	}
	
}
