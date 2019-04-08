package gestao.controllers;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import gestao.controllers.HospitalController;
import gestao.repositories.HospitalRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HospitalControllerTests {

	private MockMvc mockMvc;

	@Autowired
	private HospitalController hospitalController;
	@Autowired
	private HospitalRepository hospitalRepository;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(hospitalController).build();
	}

	@Test
	public void deveInserirHospital() throws Exception {
		hospitalRepository.deleteAll(); // apaga todos os hospitais p/ testes ***

		ResultMatcher ok = MockMvcResultMatchers.status().isOk();

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/v1/hospitais").contentType(MediaType.APPLICATION_JSON)
								.content(createUserInJson("10", "SaoFrancisco2",
										"Praca Francisco Matarazzo, 60, Americana, SP", "10", "10")))
				.andExpect(ok).andReturn();
	}

	private static String createUserInJson(String id, String nome, String endereco, String leitosTotais,
			String leitosDisponiveis) {
		return "{ \"id\": \"" + id + "\", " + "\"nome\":\"" + nome + "\"," + "\"endereco\":\"" + endereco + "\","
				+ "\"leitosDisponiveis\":\"" + leitosDisponiveis + "\"," + "\"leitosTotais\":\"" + leitosTotais + "\"}";
	}

}
