package com.acelera.squad.four.hospital.models;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Hospital e seus atributos")
public class HospitalDTO {

	@ApiModelProperty(notes = "Nome do hospital")
	@NotEmpty(message = "Nome do hospital nao preenchido")
	private String nome;
	@ApiModelProperty(notes = "Endereco do hospital")
	@NotEmpty(message = "Endereco do hospital nao preenchido")
	private String endereco;
	@DecimalMin(value = "1")
	@ApiModelProperty(notes = "Leitos totais do hospital")
	@NotNull(message = "Leitos totais do hospital nao preenchido")
	private Integer leitosTotais;

	public HospitalDTO(String nome, String endereco, Integer leitosTotais) {
		this.nome = nome;
		this.endereco = endereco;
		this.leitosTotais = leitosTotais;
	}

	public HospitalDTO() {
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

	public Hospital toHospital() {
		return new Hospital(this.nome, this.endereco, this.leitosTotais);
	}

}
