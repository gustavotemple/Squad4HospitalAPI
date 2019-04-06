package com.acelera.squad.four.hospital.service;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.acelera.squad.four.hospital.models.Hospital;

public interface HospitalService {

	public GeoJsonPoint buscaCoordenadasPor(final String endereco);

	public Hospital hospitalMaisProximoHospital(final Hospital hospital);

	public Hospital hospitalMaisProximoPaciente(final String endereco);

}
