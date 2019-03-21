package com.acelera.squad.four.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.service.HospitalService;

@RestController
@RequestMapping(path = "/v1")
public class HospitalController {
	
	private final HospitalService hospitalService;
	
	@Autowired
	public HospitalController(HospitalService hospitalService) {
		this.hospitalService = hospitalService;
	}
	
	@PostMapping(path = "/hospitais")
	public void addHospital(@RequestBody Hospital hospital) {
		hospitalService.buscaCoordenadasPor(hospital.getEndereco());
	}

}
