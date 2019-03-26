package com.acelera.squad.four.hospital.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;

import com.acelera.squad.four.hospital.configuration.ApplicationConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@TypeAlias("Hospital")
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = ApplicationConfig.HOSPITAIS)
public class Hospital extends ResourceSupport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty(access = Access.READ_ONLY)
	private ObjectId _id;
	@Indexed(name="nomeHospital", unique=true)
	@NotEmpty(message = "Nome do hospital nao preenchido")
	private String nome;
	@NotEmpty(message = "Endereco do hospital nao preenchido")	
	private String endereco;
	@NotNull(message = "Leitos totais do hospital nao preenchido")
	@DecimalMin(value = "1")
	private Integer leitosTotais;
	private Integer leitosDisponiveis;
	@JsonProperty(access = Access.READ_ONLY)
	private GeoJsonPoint localizacao;
	@DBRef
	private Collection<Paciente> pacientes = new ArrayList<Paciente>();
	@DBRef
	private Collection<Produto> estoque = new ArrayList<Produto>();
	
	public Hospital() {
	}

	@JsonProperty("id")
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

	public int getLeitosTotais() {
		return leitosTotais;
	}

	public void setLeitosTotais(int leitosTotais) {
		this.leitosTotais = leitosTotais;
	}

	public int getLeitosDisponiveis() {
		return leitosDisponiveis;
	}

	public void setLeitosDisponiveis(int leitosDisponiveis) {
		this.leitosDisponiveis = leitosDisponiveis;
	}

	@JsonProperty
	public GeoJsonPoint getLocalizacao() {
		return localizacao;
	}

	@JsonIgnore
	public void setLocalizacao(GeoJsonPoint localizacao) {
		this.localizacao = localizacao;
	}

	public Collection<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(Collection<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public Collection<Produto> getEstoque() {
		return estoque;
	}

	public void setEstoque(Collection<Produto> estoque) {
		this.estoque = estoque;
	}

}
