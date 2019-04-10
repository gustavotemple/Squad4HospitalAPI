package gestao.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;


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

	
	  private Location location = new Location();	   
	  private Geometry geometry = new Geometry(); 
	  private Result result = new Result();	 
	  private Geocode geocode = new Geocode();
	

	@Before
	public void setUp() {
		hospitalService = new HospitalServiceImpl(geocodeClient, hospitalRepository);		
		hospital = new Hospital("Hospital Israelita Albert Einstein", ENDERECO, 10);
		hospital.set_id(ObjectId.get());
		
		location.setLat(-23.6000507F);
		location.setLng(46.7152458F);
		geometry.setLocation(location);
		result.setGeometry(geometry);		
		geocode.setResults(Arrays.asList(result));
	
		
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