package gestao.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import gestao.exceptions.HospitalNotFoundException;
import gestao.exceptions.PacienteNotFoundException;
import gestao.models.Hospital;
import gestao.models.Leito;
import gestao.models.Paciente;
import gestao.repositories.HospitalRepository;
import gestao.repositories.LeitoRepository;
import gestao.repositories.PacienteRepository;
import gestao.service.PacienteService;
import gestao.service.PacienteServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PacienteServiceTest {

	private PacienteService pacienteService;

	@Mock
	private PacienteRepository pacienteRepository;
	@Mock
	private HospitalRepository hospitalRepository;
	@Mock
	private LeitoRepository leitorRepository;

	private final int PACIENTES_DEPOIS_INSERIR = 4;

	private Hospital hospital;
	private Paciente pacienteOne;
	private Paciente pacienteTwo;
	private Paciente pacienteThree;
	private Leito leitoOne;

	Collection<Paciente> pacientes = new ArrayList<Paciente>();

	Collection<Leito> leitos = new ArrayList<Leito>();

	@Before
	public void setUp() {
		pacienteService = new PacienteServiceImpl(pacienteRepository, hospitalRepository, leitorRepository);

		hospital = new Hospital("Hospital Israelita Albert Einstein",
				" Av. Albert Einstein, 627 - Jardim Leonor, São Paulo - SP", 10);
		hospital.set_id(ObjectId.get());

		pacienteOne = new Paciente("Joao", "123.123.123-48", Paciente.Type.M);
		pacienteOne.set_id(ObjectId.get());
		pacienteTwo = new Paciente("Maria", "123.123.123-27", Paciente.Type.F);
		pacienteTwo.set_id(ObjectId.get());
		pacienteThree = new Paciente("Jose", "123.123.123-55", Paciente.Type.M);
		pacienteThree.set_id(ObjectId.get());

		pacientes.add(pacienteOne);
		pacientes.add(pacienteTwo);
		pacientes.add(pacienteThree);

		leitoOne = new Leito(pacienteOne.getObjectId(), new Date(03 / 03 / 2019), null);
		leitos.add(leitoOne);

		hospital.setPacientes(pacientes);
		hospital.setLeitos(leitos);

	}

	@Test
	public void deveInserirPaciente() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		Paciente pacienteFour = new Paciente("Ana", "123.123.123-55", Paciente.Type.F);
		pacienteService.addPaciente(hospital.getObjectId(), pacienteFour);

		assertEquals(pacienteService.findAll(hospital.getObjectId()).size(), PACIENTES_DEPOIS_INSERIR);

	}

	@Test
	public void deveListarPaciente() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		assertEquals(pacienteService.getPaciente(hospital.getObjectId(), pacienteOne.getObjectId()), pacienteOne);
	}

	@Test(expected = PacienteNotFoundException.class)
	public void naoDeveListarPacienteSeNaoHouver() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.getPaciente(hospital.getObjectId(), new ObjectId());
	}

	@Test
	public void deveListarTodosPacientes() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		assertEquals(pacienteService.findAll(hospital.getObjectId()), pacientes);
	}

	@Test(expected = HospitalNotFoundException.class)
	public void naoDeveListarPacientesCasoNaoHouverHospital() {
		pacienteService.findAll(new ObjectId());
	}

	@Test
	public void deveAtualizarPaciente() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		Paciente newPaciente = new Paciente("Carlos", "123.123.123-55", Paciente.Type.F);
		pacienteService.updatePaciente(hospital.getObjectId(), newPaciente, pacienteOne.getObjectId());

		assertEquals(pacienteService.getPaciente(hospital.getObjectId(), pacienteOne.getObjectId()).getNome(),
				"Carlos");
	}

	@Test
	public void deveFazerCheckin() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.checkin(hospital.getObjectId(), pacienteTwo.getObjectId());

		assertEquals(
				hospital.getLeitos().stream().filter(leito -> leito.getPacienteObjectId().equals(pacienteTwo.getObjectId()))
						.findFirst().get().getPacienteObjectId(),
				pacienteTwo.getObjectId());
	}

	@Test
	public void deveFazerCheckout() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);
		
		pacienteService.checkout(hospital.getObjectId(), pacienteOne.getObjectId());

		assertTrue(!hospital.getLeitos().contains(leitoOne));
	}

	@Test(expected = PacienteNotFoundException.class)
	public void naoDeveFazerCheckoutCasoNaoHouverPaciente() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.checkout(hospital.getObjectId(), pacienteTwo.getObjectId());

	}
	
	@Test(expected = PacienteNotFoundException.class)
	public void deveExcluirPaciente() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.deletePaciente(hospital.getObjectId(), pacienteOne.getObjectId());
		pacienteService.getPaciente(hospital.getObjectId(), pacienteOne.getObjectId());
	}

}