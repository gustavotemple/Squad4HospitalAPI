package gestao.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import gestao.exceptions.HospitalNotFoundException;
import gestao.exceptions.LeitoNotFoundExcpetion;
import gestao.exceptions.PacienteNotFoundException;
import gestao.models.Hospital;
import gestao.models.Leito;
import gestao.models.Paciente;
import gestao.repositories.HospitalRepository;
import gestao.repositories.LeitoRepository;
import gestao.repositories.PacienteRepository;

@RunWith(MockitoJUnitRunner.class)
public class PacienteServiceTest {

	@InjectMocks
	private PacienteService pacienteService;

	@Mock
	private PacienteRepository pacienteRepository;
	@Mock
	private HospitalRepository hospitalRepository;
	@Mock
	private LeitoRepository leitoreRepository;

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

		hospital = new Hospital(new ObjectId(), "Hospital Israelita Albert Einstein",
				" Av. Albert Einstein, 627 - Jardim Leonor, São Paulo - SP", 10);

		pacienteOne = new Paciente(new ObjectId(), "Joao", "123.123.123-48", Paciente.Type.M);
		pacienteTwo = new Paciente(new ObjectId(), "Maria", "123.123.123-27", Paciente.Type.F);
		pacienteThree = new Paciente(new ObjectId(), "Jose", "123.123.123-55", Paciente.Type.M);

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
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		Paciente pacienteFour = new Paciente(new ObjectId(), "Ana", "123.123.123-55", Paciente.Type.F);
		pacienteService.addPaciente(hospital.getObjectId(), pacienteFour);

		assertEquals(pacienteService.findAll(hospital.getObjectId()).size(), PACIENTES_DEPOIS_INSERIR);

	}

	@Test
	public void deveListarPaciente() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		assertEquals(pacienteService.getPaciente(hospital.getObjectId(), pacienteOne.getObjectId()), pacienteOne);
	}

	@Test(expected = PacienteNotFoundException.class)
	public void naoDeveListarPacienteSeNaoHouver() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.getPaciente(hospital.getObjectId(), new ObjectId());
	}

	@Test
	public void deveListarTodosPacientes() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		assertEquals(pacienteService.findAll(hospital.getObjectId()), pacientes);
	}

	@Test(expected = HospitalNotFoundException.class)
	public void naoDeveListarPacientesCasoNaoHouverHospital() {
		pacienteService.findAll(new ObjectId());
	}

	@Test
	public void deveAtualizarPaciente() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		Paciente newPaciente = new Paciente(new ObjectId(), "Carlos", "123.123.123-55", Paciente.Type.F);
		pacienteService.updatePaciente(hospital.getObjectId(), newPaciente, pacienteOne.getObjectId());

		assertEquals(pacienteService.getPaciente(hospital.getObjectId(), pacienteOne.getObjectId()).getNome(),
				"Carlos");
	}

	@Test(expected = PacienteNotFoundException.class)
	public void deveExcluirPaciente() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.deletePaciente(hospital.getObjectId(), pacienteOne.getObjectId());
		pacienteService.getPaciente(hospital.getObjectId(), pacienteOne.getObjectId());
	}

	@Test
	public void deveFazerCheckin() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.checkin(hospital.getObjectId(), pacienteTwo.getObjectId());

		assertEquals(
				hospital.getLeitos().stream().filter(leito -> leito.getPacienteId().equals(pacienteTwo.getObjectId()))
						.findFirst().get().getPacienteId(),
				pacienteTwo.getObjectId());
	}

	@Test(expected = NoSuchElementException.class)
	public void deveFazerCheckout() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.checkout(hospital.getObjectId(), pacienteOne.getObjectId());

		assertEquals(
				hospital.getLeitos().stream().filter(leito -> leito.getPacienteId().equals(pacienteOne.getObjectId()))
						.findFirst().get().getPacienteId(),
				pacienteOne.getObjectId());

	}

	@Test(expected = LeitoNotFoundExcpetion.class)
	public void naoDeveFazerCheckoutCasoNaoHouverPaciente() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		pacienteService.checkout(hospital.getObjectId(), pacienteTwo.getObjectId());

	}

}
