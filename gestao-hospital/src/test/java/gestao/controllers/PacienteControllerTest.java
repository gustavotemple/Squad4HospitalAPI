package gestao.controllers;

import gestao.ApplicationTest;
import gestao.controllers.HospitalController;
import gestao.controllers.PacienteController;
import gestao.models.Hospital;
import gestao.models.Leito;
import gestao.models.Paciente;
import gestao.repositories.HospitalRepository;
import gestao.repositories.LeitoRepository;
import gestao.repositories.PacienteRepository;
import gestao.service.PacienteService;
import gestao.service.PacienteServiceImpl;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.junit.Before;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.bson.types.ObjectId;

@RunWith(SpringRunner.class)
public class PacienteControllerTest extends ApplicationTest{

    private PacienteController pacienteController;
    private PacienteService pacienteService;

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
	private HospitalRepository hospitalRepository;
    @Mock
	private LeitoRepository leitorRepository;

    private MockMvc mockMvc;    
    private Paciente paciente;
    private Hospital hospital;
    private Leito leito;
    
	Collection<Paciente> pacientes = new ArrayList<Paciente>();

	Collection<Leito> leitos = new ArrayList<Leito>();

	@Before
	public void setup() {
		pacienteService = new PacienteServiceImpl(pacienteRepository, hospitalRepository, leitorRepository);
		pacienteController = new PacienteController(pacienteService);
		
        hospital = new Hospital("Santa Tereza", "Rua do Java", 5);
        hospital.set_id(ObjectId.get());
        paciente = new Paciente("Roberto Carlos", "11111111111111", Paciente.Type.M );
        paciente.set_id(ObjectId.get());

		pacientes.add(paciente);

		leito = new Leito(paciente.getObjectId(), new Date(03 / 03 / 2019), null);
		leitos.add(leito);

		hospital.setPacientes(pacientes);
		hospital.setLeitos(leitos);
        
        mockMvc = MockMvcBuilders.standaloneSetup(pacienteController).build();  
        
    }
 
    @Test
    public void deveFazerCheckin() throws Exception{
    	Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

        String url = "/v1/hospitais/" + hospital.get_id() + "/pacientes/" + paciente.get_id() + "/checkin" ;

        MockHttpServletResponse response = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());       
        assertThat(response.getContentAsString()).isEqualTo("Checkin feito com sucesso");         
    }

    @Test
    public void deveFazerCheckout() throws Exception{
    	Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

        String url = "/v1/hospitais/" + this.hospital.get_id() + "/pacientes/" + paciente.get_id() + "/checkout" ;

        MockHttpServletResponse response = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());       
        assertThat(response.getContentAsString()).isEqualTo("Checkout feito com sucesso");            
    }

}