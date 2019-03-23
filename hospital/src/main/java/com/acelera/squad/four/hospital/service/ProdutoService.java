package com.acelera.squad.four.hospital.service;

import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.models.Produto;
import com.acelera.squad.four.hospital.repositories.ProdutoRepository;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository userMongoDbRepository;

	public Produto addProduto(Produto produto) {

		Produto prod = new Produto();
		prod.setId(produto.getId());
		prod.setNome(produto.getNome());
		prod.setDescricao(produto.getDescricao());
		prod.setQuantidade(produto.getQuantidade());

		userMongoDbRepository.save(prod);

		produto.setId(prod.getId());

		return produto;
	}

	public Produto getProduto(String id) {
		Produto produto = userMongoDbRepository.findOne(id);
		if (Objects.isNull(produto)) {
			/* handle this exception using {@link RestExceptionHandler} */
			throw new NullPointerException();
		}
		return new Produto().build(produto);
	}

	public Produto updateProduto(Produto produtoUpdate, String id) {
		Produto prod = userMongoDbRepository.findOne(id);
		if (Objects.isNull(prod)) {
			/* handle this exception using {@link RestExceptionHandler} */
			throw new NullPointerException();
		}

		prod.setNome(produtoUpdate.getNome());
		prod.setDescricao(produtoUpdate.getDescricao());
		prod.setQuantidade(produtoUpdate.getQuantidade());

		userMongoDbRepository.save(prod);
		return new Produto().build(prod);
	}

	public void deleteProduto(String id) {
		userMongoDbRepository.delete(id);
	}

	public Object findAll() {
		return userMongoDbRepository.findAll();
	}

}
