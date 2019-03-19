package com.acelera.squad.four.hospital;

import java.util.Date;

public class Paciente {
	private String nome;
	private Date checkin;
	private Date checkout;
	private int cpf;
	private String sexo; //Melhor fazer uma enum
	
	public Paciente() {
	}
	
	public Paciente(String nome, Date checkin, Date checkout, int cpf, String sexo) {
		super();
		this.nome = nome;
		this.checkin = checkin;
		this.checkout = checkout;
		this.cpf = cpf;
		this.sexo = sexo;
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
	public int getCpf() {
		return cpf;
	}
	public void setCpf(int cpf) {
		this.cpf = cpf;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
}
