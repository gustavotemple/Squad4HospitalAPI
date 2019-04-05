package gestao.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import gestao.ApplicationTests;
import gestao.models.Hospital;
import gestao.repositories.HospitalRepository;
import gestao.service.HospitalService;

@RunWith(SpringRunner.class)
public class HospitalControllerTest extends ApplicationTests{

	@InjectMocks
	private HospitalController hospitalController;    

    @Mock
    private HospitalRepository hospitalRepository;
    
    @Mock 
    private HospitalService hospitalService;
    
    private MockMvc mockMvc;
    private Hospital hospital;

	@Before
	public void setup() {
        hospital = new Hospital( new ObjectId(), "Santa Luzia", "Rua do Java", 10);            
        mockMvc = MockMvcBuilders.standaloneSetup(hospitalController).build();  
        
	}
        

    @Test
    public void deveListarHospitais() throws Exception{
        String url = "/v1/hospitais/";

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
        Mockito.when(this.hospitalRepository.findBy_id( hospital.getObjectId() )).thenReturn(hospital);
       
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
        Mockito.when(this.hospitalRepository.findBy_id( hospital.getObjectId() )).thenReturn(hospital);
       
        // when        
        MockHttpServletResponse response = mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());       
        assertThat(response.getContentAsString()).isEqualTo("[]"); 
    }
}
