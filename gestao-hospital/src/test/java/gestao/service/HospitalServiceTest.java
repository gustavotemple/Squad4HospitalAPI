package gestao.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import gestao.exceptions.BadEnderecoException;
import gestao.exceptions.HospitalNotFoundException;
import gestao.models.Hospital;
import gestao.models.HospitalDTO;
import gestao.models.geocode.Geocode;
import gestao.models.geocode.Geometry;
import gestao.models.geocode.Location;
import gestao.models.geocode.Result;
import gestao.repositories.HospitalRepository;

@RunWith(MockitoJUnitRunner.class)
public class HospitalServiceTest {

	private HospitalService hospitalService;

	@Mock
	private HospitalRepository hospitalRepository;
	@Mock
	private GeocodeClient geocodeClient;

	private HospitalDTO newHospital;
	private Hospital hospital;

	private final String ENDERECO = "Av. Albert Einstein, 627 - Jardim Leonor, São Paulo - SP";
	private static final String KEY = "AIzaSyAczcT1dO-iPGi273Mu3fr9uxJoUgArfyI";

	private Location location = new Location(-23.6000507F, 46.7152458F);
	private Geometry geometry = new Geometry(location);
	private List<Result> result = Arrays.asList(new Result(geometry));
	private Geocode geocode = new Geocode(result);

	@Before
	public void setUp() {
		hospitalService = new HospitalServiceImpl(geocodeClient, hospitalRepository);		
		hospital = new Hospital("Hospital Israelita Albert Einstein", ENDERECO, 10);
		hospital.set_id(ObjectId.get());
	}

	@Test
	public void deveInserirHospital() {
		Mockito.when(geocodeClient.buscaCoordenadasPor(ENDERECO, KEY)).thenReturn(geocode);
		Mockito.when(hospitalRepository.save(hospital)).thenReturn(hospital);
		newHospital = new HospitalDTO("Hospital Israelita Albert Einstein", ENDERECO, 10);
		Hospital hospitalAdicioado = hospitalService.addHospital(newHospital);
	
		assertEquals(hospitalAdicioado, hospital);
	}
	
	@Test(expected = HospitalNotFoundException.class)
	public void naoDeveListarSeHospitalNaoExistir() {
		hospitalService.getHospitalNearById(new ObjectId());
	}
	
	@Test(expected = BadEnderecoException.class)
	public void naoDeveListarHospitalSeEnderecoForNulo() {
		hospitalService.getHospitalNearByAddress(null);
	}
	
	@Test(expected = HospitalNotFoundException.class)
	public void naoDeveDelearHospitalSeNaoExistir() {
		hospitalService.deleteHospital(new ObjectId());
	}	
	
	@Test(expected = HospitalNotFoundException.class)
	public void naoDeveListarSeHospitalProximoNaoExistir() {
		hospitalService.getHospitalNearById(new ObjectId());
	}


}