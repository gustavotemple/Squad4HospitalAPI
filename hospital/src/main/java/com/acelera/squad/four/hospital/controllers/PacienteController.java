package com.acelera.squad.four.hospital.controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.Paciente;
import com.acelera.squad.four.hospital.service.HospitalService;
import com.acelera.squad.four.hospital.service.PacienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "pacientes")
@RequestMapping(path = "/v1")
@ExposesResourceFor(Paciente.class)
public class PacienteController {
	
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private HospitalService hospitalService;

	@PostMapping("/hospitais/{id}/pacientes")
	public ResponseEntity<Paciente> addPaciente(@PathVariable ObjectId id, @Valid @RequestBody Paciente novoPaciente) {
		return ResponseEntity.ok(pacienteService.addPaciente(id, novoPaciente));
	}
	
	@GetMapping("/hospitais/pacientes/near")
	@ApiOperation(value = "Retorna o hospital mais proximo com leitos disponiveis")
	public ResponseEntity<Hospital> getHospitalsByLocation(
			@RequestParam(value = "endereco", required = true) String endereco) {
		return ResponseEntity.ok().body(hospitalService.hospitalMaisProximoPaciente(endereco));
	}

	@GetMapping("/hospitais/{id}/pacientes/{paciente}")
	@ApiOperation(value = "Retorna todas as informacoes do paciente cadastrado")
	public ResponseEntity<Paciente> getPaciente(@PathVariable ObjectId id, @PathVariable String paciente) {
		return ResponseEntity.ok().body(pacienteService.getPaciente(id, paciente));
	}

	@GetMapping("/hospitais/{id}/pacientes")
	@ApiOperation(value = "Retorna os pacientes dentro do hospital")
	public ResponseEntity<Collection<Paciente>> listarPacientes(@PathVariable ObjectId id) {
		return ResponseEntity.ok().body(pacienteService.findAll(id));
	}

	@PutMapping("/hospitais/{id}/pacientes/{paciente}")
	public ResponseEntity<Paciente> updatePaciente(@PathVariable ObjectId id, @Valid @RequestBody Paciente pacienteUpdate, @PathVariable String paciente) {
		return ResponseEntity.ok(pacienteService.updatePaciente(id, pacienteUpdate, paciente));
	}

	@DeleteMapping("/hospitais/{id}/pacientes/{paciente}")
	public ResponseEntity<String> deletePaciente(@PathVariable ObjectId id, @PathVariable String paciente) {
		pacienteService.deletePaciente(id, paciente);

		return ResponseEntity.ok().body("Paciente " + id + " apagado.");
	}

	@PostMapping("/hospitais/{id}/pacientes/{paciente}/checkin")
	public ResponseEntity<String> postMethodName(@PathVariable ObjectId id, @PathVariable String paciente) {
		pacienteService.checkin(id, paciente);
		
		return ResponseEntity.ok().body("Checkin feito com sucesso");
	}
	
}
