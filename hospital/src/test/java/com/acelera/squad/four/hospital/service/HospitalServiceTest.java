package com.acelera.squad.four.hospital.service;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;

@RunWith(MockitoJUnitRunner.class)
public class HospitalServiceTest {

	private HospitalService hospitalService;

	@Mock
	private HospitalRepository hospitalRepository;
	@Mock
	private GeocodeClient geocodeClient;

	private Hospital hospital;

	@Before
	public void setUp() {
		hospitalService = new HospitalServiceImpl(geocodeClient, hospitalRepository);

		hospital = new Hospital("Hospital Israelita Albert Einstein",
				" Av. Albert Einstein, 627 - Jardim Leonor, São Paulo - SP", 10);
		hospital.set_id(ObjectId.get());

	}

}