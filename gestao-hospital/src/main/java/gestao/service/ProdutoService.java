package gestao.service;

import java.util.Collection;

import org.bson.types.ObjectId;

import gestao.models.Produto;

public interface ProdutoService {

	public Produto addProduto(ObjectId hospitalId, Produto produto);

	public Produto getProduto(ObjectId hospitalId, ObjectId produtoId);

	public Produto updateProduto(ObjectId hospitalId, Produto produtoUpdate, ObjectId produtoId);

	public void deleteProduto(ObjectId hospitalId, ObjectId produtoId);

	public Collection<Produto> findAll(ObjectId hospitalId);

	public Collection<Produto> findAllBolsas(ObjectId hospitalId);

	public Collection<Produto> findAllProdutos(ObjectId hospitalId);

}
