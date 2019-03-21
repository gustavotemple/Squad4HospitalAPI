package com.acelera.squad.four.hospital.models;

import java.util.ArrayList;
import java.util.List;

public class Sphere {
	
	private String type = "Point";
	private List<Integer> coordenates = new ArrayList<Integer>();
	
	public Sphere() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Integer> getCoordenates() {
		return coordenates;
	}

	public void setCoordenates(List<Integer> coordenates) {
		this.coordenates = coordenates;
	}

}
