package com.acelera.squad.four.hospital.service;

import java.util.Collection;

import org.bson.types.ObjectId;

import com.acelera.squad.four.hospital.models.Produto;

public interface ProdutoService {

	public Produto addProduto(ObjectId hospitalId, Produto produto);

	public Produto getProduto(ObjectId hospitalId, ObjectId produtoId);

	public Produto updateProduto(ObjectId hospitalId, Produto produtoUpdate, ObjectId produtoId);

	public void deleteProduto(ObjectId hospitalId, ObjectId produtoId);

	public Collection<Produto> findAll(ObjectId hospitalId);

}
