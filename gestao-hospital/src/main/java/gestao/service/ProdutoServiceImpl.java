package gestao.service;

import org.springframework.stereotype.Service;

import gestao.exceptions.HospitalNotFoundException;
import gestao.exceptions.ProdutoNotFoundException;
import gestao.models.Hospital;
import gestao.models.Produto;
import gestao.repositories.HospitalRepository;
import gestao.repositories.ProdutoRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	private ProdutoRepository produtoRepository;
	private HospitalRepository hospitalRepository;

	@Autowired
	public ProdutoServiceImpl(ProdutoRepository produtoRepository, HospitalRepository hospitalRepository) {
		this.produtoRepository = produtoRepository;
		this.hospitalRepository = hospitalRepository;
	}

	@Override
	public Produto addProduto(ObjectId hospitalId, Produto produto) {
		final Produto prod = new Produto();
		prod.setNome(produto.getNome());
		prod.setDescricao(produto.getDescricao());
		prod.setQuantidade(produto.getQuantidade());
		prod.setTipo(produto.getTipo());
		produtoRepository.save(prod);

		final Hospital hospital = findHospitalBy(hospitalId);
		hospital.getEstoque().add(prod);
		hospitalRepository.save(hospital);

		return prod;
	}

	@Override
	public Produto getProduto(ObjectId hospitalId, ObjectId produtoId) {
		final Produto produto = findProdutoBy(findHospitalBy(hospitalId), produtoId);

		return new Produto().build(produto);
	}

	@Override
	public Produto updateProduto(ObjectId hospitalId, Produto produtoUpdate, ObjectId produtoId) {
		final Produto produto = findProdutoBy(findHospitalBy(hospitalId), produtoId);

		produto.setNome(produtoUpdate.getNome());
		produto.setDescricao(produtoUpdate.getDescricao());
		produto.setQuantidade(produtoUpdate.getQuantidade());
		produto.setTipo(produtoUpdate.getTipo());
		produtoRepository.save(produto);

		return new Produto().build(produto);
	}

	@Override
	public void deleteProduto(ObjectId hospitalId, ObjectId produtoId) {
		final Hospital hospital = findHospitalBy(hospitalId);

		final Produto produto = findProdutoBy(hospital, produtoId);

		hospital.getEstoque().remove(produto);
		hospitalRepository.save(hospital);

		produtoRepository.delete(produtoId);
	}

	@Override
	public Collection<Produto> findAll(ObjectId hospitalId) {
		final Hospital hospital = findHospitalBy(hospitalId);

		return hospital.getEstoque();
	}

	private Produto findProdutoBy(Hospital hospital, ObjectId produtoId) {
		final Produto produto = hospital.getEstoque().stream().filter(p -> produtoId.equals(p.getObjectId()))
				.findFirst().orElse(null);
		if (Objects.isNull(produto))
			throw new ProdutoNotFoundException(produtoId);
		return produto;
	}

	private Hospital findHospitalBy(ObjectId hospitalId) {
		final Hospital hospital = hospitalRepository.findOne(hospitalId);
		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);
		return hospital;
	}

	@Override
	public Collection<Produto> findAllBolsas(ObjectId hospitalId) {
		final Hospital hospital = findHospitalBy(hospitalId);
		Collection<Produto> produtos =  hospital.getEstoque().stream()
				.filter(p -> p.getTipo().equals("SANGUE"))
				.collect(Collectors.toList());

		if (produtos.isEmpty())
			throw new ProdutoNotFoundException();

		return produtos;
	}

	@Override
	public Collection<Produto> findAllProdutos(ObjectId hospitalId) {
		final Hospital hospital = findHospitalBy(hospitalId);
		Collection<Produto> produtos =  hospital.getEstoque().stream()
				.filter(p -> p.getTipo().equals("COMUM"))
				.collect(Collectors.toList());

		if (produtos.isEmpty())
			throw new ProdutoNotFoundException();

		return produtos;
	}

}
