package com.acelera.squad.four.hospital.models;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@TypeAlias("Paciente")
@Document(collection = "pacientes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Paciente {
	
	public enum Type {
		M, F
	}

	@Id
	@JsonProperty(access = Access.READ_ONLY)
	private String id;
	@NotEmpty(message = "Nome do paciente nao preenchido")
	private String nome;
	private Date checkin;
	private Date checkout;
	@Indexed(name="cpfPaciente", unique=true)
	@NotEmpty(message = "CPF do paciente nao preenchido")	
	private String cpf;
	private Paciente.Type sexo;

	public Paciente() {
	}

	public Paciente(String id, String nome, Date checkin, Date checkout, String cpf, Paciente.Type sexo) {
		super();
		this.id = id;
		this.nome = nome;
		this.checkin = checkin;
		this.checkout = checkout;
		this.cpf = cpf;
		this.sexo = sexo;
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

	public Paciente.Type getSexo() {
		return sexo;
	}

	public void setSexo(Paciente.Type sexo) {
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
