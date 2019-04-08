package gestao.service;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import gestao.models.Hospital;
import gestao.repositories.HospitalRepository;
import gestao.service.GeocodeClient;
import gestao.service.HospitalService;
import gestao.service.HospitalServiceImpl;

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