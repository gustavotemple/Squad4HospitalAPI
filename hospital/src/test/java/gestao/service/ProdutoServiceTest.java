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
import gestao.service.ProdutoService;
import gestao.service.ProdutoServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ProdutoServiceTest {

	private ProdutoService produtoService;

	@Mock
	private ProdutoRepository produtoRepository;

	@Mock
	private HospitalRepository hospitalRepository;

	HospitalRepository test = mock(HospitalRepository.class);

	private final int ESTOQUE_DEPOIS_INSERIR = 4;
	
	private Hospital hospital;
	private Produto prodOne;
	private Produto prodTwo;
	private Produto prodThree;
	Collection<Produto> estoque = new ArrayList<Produto>();

	@Before
	public void setUp() {
		produtoService = new ProdutoServiceImpl(produtoRepository, hospitalRepository);

		hospital = new Hospital("Hospital Israelita Albert Einstein",
				" Av. Albert Einstein, 627 - Jardim Leonor, São Paulo - SP", 10);
		hospital.set_id(ObjectId.get());

		prodOne = new Produto("Sangue", "O positivo", 10, null);
		prodOne.set_id(ObjectId.get());
		prodTwo = new Produto("Sangue", "O negativo", 10, null);
		prodTwo.set_id(ObjectId.get());
		prodThree = new Produto("Soro", "fisiologico", 10, null);
		prodThree.set_id(ObjectId.get());

		estoque.add(prodOne);
		estoque.add(prodTwo);
		estoque.add(prodThree);

		hospital.setEstoque(estoque);
	}

	@Test
	public void deveInserirProduto() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);
		Produto produtoFour = new Produto("Esparadrapo", "branco", 15, null);
		produtoService.addProduto(hospital.getObjectId(), produtoFour);
		assertEquals(produtoService.findAll(hospital.getObjectId()).size(), ESTOQUE_DEPOIS_INSERIR);

	}

	@Test
	public void deveListarTodosProdutos() {

		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		assertEquals(produtoService.findAll(hospital.getObjectId()), estoque);
		verify(hospitalRepository, atLeastOnce()).findOne(hospital.getObjectId());

	}

	@Test
	public void deveListarProduto() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		assertEquals("Sangue", produtoService.getProduto(hospital.getObjectId(), prodOne.getObjectId()).getNome());

		verify(hospitalRepository, atLeastOnce()).findOne(hospital.getObjectId());
	}
	
	@Test(expected = ProdutoNotFoundException.class)
	public void naoDeveListarProdutoSeNaoHouver() {
		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		produtoService.getProduto(hospital.getObjectId(), new ObjectId());

	}

	@Test
	public void deveAtualizarProduto() {

		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		Produto newProduto = new Produto("Seringa", null, 10, null);
		produtoService.updateProduto(hospital.getObjectId(), newProduto, prodOne.getObjectId());

		assertEquals("Seringa", produtoService.getProduto(hospital.getObjectId(), prodOne.getObjectId()).getNome());
	}
	
	@Test(expected = ProdutoNotFoundException.class)
	public void naoDeveAtualizarProdutoSeNaoHouver() {

		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		Produto newProduto = new Produto("Seringa", null, 10, null);
		produtoService.updateProduto(hospital.getObjectId(), newProduto, new ObjectId());
		
	}

	@Test(expected = ProdutoNotFoundException.class)
	public void deveDeletarProduto() {

		Mockito.when(hospitalRepository.findOne(hospital.getObjectId())).thenReturn(hospital);

		produtoService.deleteProduto(hospital.getObjectId(), prodOne.getObjectId());

		produtoService.getProduto(hospital.getObjectId(), prodOne.getObjectId());

	}

}