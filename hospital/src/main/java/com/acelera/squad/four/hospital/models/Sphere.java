package com.acelera.squad.four.hospital.models;

import java.util.ArrayList;
import java.util.List;

public class Sphere {
	
	private String type = "Point";
	private List<Float> coordenates = new ArrayList<Float>();
	
	public Sphere() {
	}

	public String getType() {
		return type;
	}

	public List<Float> getCoordenates() {
		return coordenates;
	}

}
