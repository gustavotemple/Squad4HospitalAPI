package com.acelera.squad.four.hospital.models;

import java.util.ArrayList;
import java.util.List;

public class Sphere {
	
	String type;
	List<Integer> coordenates = new ArrayList<Integer>();
	
	public Sphere() {
		this.type = "Point";
		this.coordenates.add(1);
		this.coordenates.add(2);
	}

}
