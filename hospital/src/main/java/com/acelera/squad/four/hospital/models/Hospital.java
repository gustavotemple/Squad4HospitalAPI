package com.acelera.squad.four.hospital.models;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonIgnoreProperties(ignoreUnknown = true)

@Document(collection = "hospitals")
public class Hospital extends ResourceSupport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty(access = Access.READ_ONLY)
	private ObjectId _id;
	private String nome = "";
	private String endereco = "";
	private int leitos;
	@JsonProperty(access = Access.READ_ONLY)
	private Sphere localizacao;
	
	public Hospital() {
	}

	@JsonProperty
	public String get_id() {
		return _id.toHexString();
	}
	
	@JsonIgnore
	public ObjectId getObjectId() {
		return _id;
	}

	@JsonIgnore
	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public int getLeitos() {
		return leitos;
	}

	public void setLeitos(int leitos) {
		this.leitos = leitos;
	}

	@JsonProperty
	public Sphere getLocalizacao() {
		return localizacao;
	}

	@JsonIgnore
	public void setLocalizacao(Sphere localizacao) {
		this.localizacao = localizacao;
	}
	
}
