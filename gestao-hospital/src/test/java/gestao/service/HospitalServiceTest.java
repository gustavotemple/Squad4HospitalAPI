package gestao.service;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import gestao.models.Hospital;
import gestao.repositories.HospitalRepository;

@RunWith(MockitoJUnitRunner.class)
public class HospitalServiceTest {

	@InjectMocks
	private HospitalService hospitalService;

	@Mock
	private HospitalRepository hospitalRepository;

	@Mock
	private GeocodeClient geocodeClient;

	private Hospital hospital;

	@Before
	public void setUp() {

		hospital = new Hospital(new ObjectId(), "Hospital Israelita Albert Einstein",
				" Av. Albert Einstein, 627 - Jardim Leonor, São Paulo - SP", 10);

	}

}
