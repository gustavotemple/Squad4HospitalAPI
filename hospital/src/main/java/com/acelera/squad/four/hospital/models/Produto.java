package com.acelera.squad.four.hospital.models;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@TypeAlias("Produto")
@Document(collection = "estoque")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Produto {

	public enum Type {
		COMUM, SANGUE
	}

	@Id
	@JsonProperty(access = Access.READ_ONLY)
	private String id;
	@NotEmpty(message = "Nome do item de estoque nao preenchido")
	private String nome;
	//@Field("descricao")
	private String descricao = "";
	//@Field("quantidade")
	private int quantidade;
	//@Field("tipo")
	private Produto.Type tipo;

	public Produto() {
	}

	public Produto(String id, String nome, String descricao, int quantidade, Produto.Type tipo) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.tipo = tipo;
	}

	@JsonProperty
	public String getId() {
		return id;
	}

	@JsonIgnore
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

	public Produto.Type getTipo() {
		return tipo;
	}

	public void setTipo(Produto.Type tipo) {
		this.tipo = tipo;
	}

	public Produto build(Produto prod) {
		this.id = prod.getId();
		this.nome = prod.getNome();
		this.descricao = prod.getDescricao();
		this.quantidade = prod.getQuantidade();
		this.tipo = prod.getTipo();
		return this;
	}
}
