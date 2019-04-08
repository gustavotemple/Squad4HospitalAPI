package gestao.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import gestao.ApplicationTest;
import gestao.configuration.ApplicationConfig;
import gestao.controllers.HospitalController;
import gestao.models.Hospital;
import gestao.repositories.HospitalRepository;
import gestao.service.GeocodeClient;
import gestao.service.HospitalService;
import gestao.service.HospitalServiceImpl;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.bson.types.ObjectId;

@RunWith(SpringRunner.class)
public class HospitalControllerTest extends ApplicationTest{

	private HospitalController hospitalController;    
    private HospitalService hospitalService;
    
    @Mock
	private GeocodeClient geocodeClient;
    @Mock
	private HospitalRepository hospitalRepository;
    

    private MockMvc mockMvc;
    private Hospital hospital;

	@Before
	public void setup() {
        hospitalService = new HospitalServiceImpl(geocodeClient, hospitalRepository);
        hospitalController = new HospitalController(hospitalService);

        hospital = new Hospital("Santa Luzia", "Rua do Java", 10);
        hospital.set_id(ObjectId.get());
        mockMvc = MockMvcBuilders.standaloneSetup(hospitalController).build();  
        
	}
        

    @Test
    public void deveListarHospitais() throws Exception{
        String url = ApplicationConfig.BASE_URL;

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
        Mockito.when(this.hospitalRepository.findOne( hospital.getObjectId() )).thenReturn(hospital);
       
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
        Mockito.when(this.hospitalRepository.findOne( hospital.getObjectId() )).thenReturn(hospital);
       
        // when        
        MockHttpServletResponse response = mockMvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());       
        assertThat(response.getContentAsString()).isEqualTo("[]"); 
    }
}
