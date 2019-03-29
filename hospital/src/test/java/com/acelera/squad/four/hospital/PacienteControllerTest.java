package com.acelera.squad.four.hospital;

import com.acelera.squad.four.hospital.controllers.PacienteController;
import com.acelera.squad.four.hospital.models.Paciente;
import com.acelera.squad.four.hospital.repositories.PacienteRepository;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

import com.acelera.squad.four.hospital.controllers.HospitalController;
import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.Paciente;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;

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
import org.bson.types.ObjectId;
import org.junit.Before;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
public class PacienteControllerTest extends HospitalApplicationTests{

	@InjectMocks
	private PacienteController pacienteController;    

    @Mock
    private PacienteRepository pacienteRepository;

    private MockMvc mockMvc;    
    private Paciente paciente;
    private Hospital hospital;

	@Before
	public void setup() {        
        hospital = new Hospital(new ObjectId(), "Santa Tereza", "Rua do Java", 5);
        paciente = new Paciente("2222", "Roberto Carlos", "11111111111111", Paciente.Type.M );
        mockMvc = MockMvcBuilders.standaloneSetup(pacienteController).build();  
        
    }
 
    @Test
    public void deveFazerCheckin() throws Exception{
        String url = "/v1/hospitais/" + hospital.getId() + "pacientes/" + paciente.getId() + "/checkin" ;

        MockHttpServletResponse response = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());       
        assertThat(response.getContentAsString()).isEqualTo("Checkin feito com sucesso");         
    }

}