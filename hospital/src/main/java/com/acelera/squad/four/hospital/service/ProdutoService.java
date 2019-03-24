package com.acelera.squad.four.hospital.service;

import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.Produto;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;
import com.acelera.squad.four.hospital.repositories.ProdutoRepository;

import java.util.Collection;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository userMongoDbRepository;
	@Autowired
	private HospitalRepository hospitalRepository;

	public Produto addProduto(ObjectId hospitalId, Produto produto) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		Produto prod = new Produto();
		prod.setNome(produto.getNome());
		prod.setDescricao(produto.getDescricao());
		prod.setQuantidade(produto.getQuantidade());
		prod.setTipo(produto.getTipo());

		userMongoDbRepository.save(prod);

		hospital.getEstoque().add(prod);

		hospitalRepository.save(hospital);

		return prod;
	}

	public Produto getProduto(ObjectId hospitalId, String produtoId) {		
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		Produto produto = hospital.getEstoque().stream().filter(p -> produtoId.equals(p.getId())).findFirst().orElse(null);

		if (Objects.isNull(produto)) {
			/* handle this exception using {@link RestExceptionHandler} */
			throw new NullPointerException();
		}
		return new Produto().build(produto);
	}

	public Produto updateProduto(ObjectId hospitalId, Produto produtoUpdate, String produtoId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		Produto prod = hospital.getEstoque().stream().filter(p -> produtoId.equals(p.getId())).findFirst().orElse(null);

		if (Objects.isNull(prod)) {
			/* handle this exception using {@link RestExceptionHandler} */
			throw new NullPointerException();
		}

		prod.setNome(produtoUpdate.getNome());
		prod.setDescricao(produtoUpdate.getDescricao());
		prod.setQuantidade(produtoUpdate.getQuantidade());
		prod.setTipo(produtoUpdate.getTipo());

		userMongoDbRepository.save(prod);

		hospital.getEstoque().add(prod);

		hospitalRepository.save(hospital);
		return new Produto().build(prod);
	}

	public void deleteProduto(ObjectId hospitalId, String produtoId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		Produto produto = hospital.getEstoque().stream().filter(p -> produtoId.equals(p.getId())).findFirst().orElse(null);

		hospital.getEstoque().remove(produto);

		hospitalRepository.save(hospital);
	}

	public Collection<Produto> findAll(ObjectId hospitalId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);
		return hospital.getEstoque();
	}

}
