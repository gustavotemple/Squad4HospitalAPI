package gestao.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import gestao.exceptions.PacienteNotFoundException;
import gestao.models.Hospital;
import gestao.models.Paciente;
import gestao.repositories.HospitalRepository;
import gestao.repositories.PacienteRepository;

@RunWith(MockitoJUnitRunner.class)
public class PacienteServiceTest {

	@InjectMocks
	private PacienteService pacienteService;

	@Mock
	private PacienteRepository pacienteRepository;
	@Mock
	private HospitalRepository hospitalRepository;
	
	private final int PACIENTES_DEPOIS_INSERIR = 4;

	private Hospital hospital;
	private Paciente pacienteOne;
	private Paciente pacienteTwo;
	private Paciente pacienteThree;

	Collection<Paciente> pacientes = new ArrayList<Paciente>();

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

		hospital.setPacientes(pacientes);

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
}
