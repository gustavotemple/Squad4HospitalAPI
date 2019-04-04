package gestao.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

import gestao.exceptions.ProdutoNotFoundException;
import gestao.models.Hospital;
import gestao.models.Produto;
import gestao.repositories.HospitalRepository;
import gestao.repositories.ProdutoRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProdutoServiceTest {

	@InjectMocks
	private ProdutoService produtoService;

	@Mock
	private ProdutoRepository produtoRepository;

	@Mock
	private HospitalRepository hospitalRepository;

	HospitalRepository test = mock(HospitalRepository.class);

	private Hospital hospital;
	private Produto prodOne;
	private Produto prodTwo;
	private Produto prodThree;
	Collection<Produto> list = new ArrayList<Produto>();

	@Before
	public void setUp() {

		hospital = new Hospital(new ObjectId(), "Hospital Israelita Albert Einstein",
				" Av. Albert Einstein, 627 - Jardim Leonor, São Paulo - SP", 10);

		prodOne = new Produto(new ObjectId(), "Sangue", "O positivo", 10, null);
		prodTwo = new Produto(new ObjectId(), "Sangue", "O negativo", 10, null);
		prodThree = new Produto(new ObjectId(), "Soro", "fisiologico", 10, null);

		list.add(prodOne);
		list.add(prodTwo);
		list.add(prodThree);

		hospital.setEstoque(list);
	}

	@Test
	public void deveListarTodosProdutos() {

		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		assertEquals(produtoService.findAll(hospital.getObjectId()), list);
		verify(hospitalRepository, atLeastOnce()).findBy_id(hospital.getObjectId());

	}

	@Test
	public void deveListarProduto() {
		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		assertEquals("Sangue", produtoService.getProduto(hospital.getObjectId(), prodOne.getObjectId()).getNome());

		verify(hospitalRepository, atLeastOnce()).findBy_id(hospital.getObjectId());
	}

	@Test
	public void deveAtualizarProduto() {

		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		Produto newProduto = new Produto(new ObjectId(), "Seringa", null, 10, null);
		produtoService.updateProduto(hospital.getObjectId(), newProduto, prodOne.getObjectId());

		assertEquals("Seringa", produtoService.getProduto(hospital.getObjectId(), prodOne.getObjectId()).getNome());
	}

	@Test(expected = ProdutoNotFoundException.class)
	public void deveDeletarProduto() {

		Mockito.when(hospitalRepository.findBy_id(hospital.getObjectId())).thenReturn(hospital);

		produtoService.deleteProduto(hospital.getObjectId(), prodOne.getObjectId());

		produtoService.getProduto(hospital.getObjectId(), prodOne.getObjectId());

	}

}
