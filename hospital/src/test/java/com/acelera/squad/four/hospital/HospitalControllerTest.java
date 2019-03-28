package com.acelera.squad.four.hospital;

import static org.assertj.core.api.Assertions.assertThat;

import com.acelera.squad.four.hospital.controllers.HospitalController;
import com.acelera.squad.four.hospital.models.Hospital;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
public class HospitalControllerTest extends HospitalApplicationTests{

	@InjectMocks
	private HospitalController hospitalController;    

    @Mock
    private HospitalRepository hospialRepository;

    private MockMvc mockMvc;
    private Hospital hospital;

	@Before
	public void setup() {
        hospital = new Hospital( new ObjectId(), "Santa Luzia", "Rua do Java", 10);            
        mockMvc = MockMvcBuilders.standaloneSetup(hospitalController).build();  
        
	}
        

    @Test
    public void deveListarHospitais() throws Exception{
        String url = "/v1/hospitais/todos";

        // when        
        MockHttpServletResponse response = mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());        
    }

    @Test
    public void deveBuscarHospial() throws Exception {
        String url = "/v1/hospitais/" + hospital.getObjectId() ;
        Mockito.when(this.hospialRepository.findBy_id( hospital.getObjectId() )).thenReturn(hospital);
       
        // when        
        MockHttpServletResponse response = mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());    
    }


    @Test
    public void deveListarLeitos() throws Exception{
        String url = "/v1/hospitais/" + hospital.getObjectId() + "/leitos" ;
        Mockito.when(this.hospialRepository.findBy_id( hospital.getObjectId() )).thenReturn(hospital);
       
        // when        
        MockHttpServletResponse response = mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());       
        assertThat(response.getContentAsString()).isEqualTo("[]"); 
    }
}