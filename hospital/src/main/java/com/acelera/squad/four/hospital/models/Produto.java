package com.acelera.squad.four.hospital.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "produto")
public class Produto {

	@Id
	private String id;
	@Field("nome")
	private String nome;
	@Field("descricao")
	private String descricao;
	@Field("quantidade")
	private int quantidade;

	public Produto() {
	}

	public Produto(String id, String nome, String descricao, int quantidade) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.quantidade = quantidade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Produto build(Produto prod) {
		this.id = prod.getId();
		this.nome = prod.getNome();
		this.descricao = prod.getDescricao();
		this.quantidade = prod.getQuantidade();
		return this;
	}
}
