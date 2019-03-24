package com.acelera.squad.four.hospital.models;

public class HospitalDTO {

	private int leitosTotais;
	private int leitosDisponiveis;

	public HospitalDTO() {
	}

	public HospitalDTO(Hospital hospital) {
		this.leitosTotais = hospital.getLeitosTotais();
		this.leitosDisponiveis = hospital.getLeitosDisponiveis();
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

}
