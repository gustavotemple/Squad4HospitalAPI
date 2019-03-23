package com.acelera.squad.four.hospital.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "paciente")
public class Paciente {

	@Id
	private String id;
	private String nome;
	private Date checkin;
	private Date checkout;
	private String cpf;
	private String sexo; // Melhor fazer uma enum

	public Paciente() {
	}

	public Paciente(String id, String nome, Date checkin, Date checkout, String cpf, String sexo) {
		super();
		this.id = id;
		this.nome = nome;
		this.checkin = checkin;
		this.checkout = checkout;
		this.cpf = cpf;
		this.sexo = sexo;
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

	public Date getCheckin() {
		return checkin;
	}

	public void setCheckin(Date checkin) {
		this.checkin = checkin;
	}

	public Date getCheckout() {
		return checkout;
	}

	public void setCheckout(Date checkout) {
		this.checkout = checkout;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Paciente build(Paciente novoPaciente) {
		this.id = novoPaciente.getId();
		this.nome = novoPaciente.getNome();
		this.checkin = novoPaciente.getCheckin();
		this.checkout = novoPaciente.getCheckout();
		this.cpf = novoPaciente.getCpf();
		this.sexo = novoPaciente.getSexo();
		return this;
	}

}
