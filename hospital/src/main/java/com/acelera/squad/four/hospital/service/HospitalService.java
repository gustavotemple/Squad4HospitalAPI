package com.acelera.squad.four.hospital.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.geocode.Geocode;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;

@Service
@EnableFeignClients
public class HospitalService {

	private static final String KEY = "AIzaSyBa6MxMRKgE8wSfWutmzp3hNajttONXMuo";

	private static Distance DISTANCE = new Distance(1, Metrics.KILOMETERS);

	private static int MINIMUM = 0;

	@Autowired
	private GeocodeClient geocodeClient;
	@Autowired
	private HospitalRepository hospitalRepository;

	public GeoJsonPoint buscaCoordenadasPor(final String endereco) {
		final Geocode geocode = geocodeClient.buscaCoordenadasPor(endereco, KEY);

		final GeoJsonPoint point = new GeoJsonPoint(geocode.getResults().get(0).getGeometry().getLocation().getLng(),
				geocode.getResults().get(0).getGeometry().getLocation().getLat());

		return point;
	}

	public List<Hospital> buscaHospitaisPor(final String endereco, final Float lat, final Float lng) {
		Point point;

		if (!Objects.isNull(endereco))
			point = buscaCoordenadasPor(endereco);
		else if (!Objects.isNull(lat) && !Objects.isNull(lng))
			point = new Point(lng, lat);
		else
			return Collections.emptyList();

		List<Hospital> hospitals = hospitalRepository.findByLocalizacaoNear(point, DISTANCE);

		return hospitals.stream().filter(hospital -> hospital.getLeitosDisponiveis() > MINIMUM)
				.collect(Collectors.toList());
	}

}
