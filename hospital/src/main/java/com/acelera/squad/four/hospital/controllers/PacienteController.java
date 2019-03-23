package com.acelera.squad.four.hospital.controllers;

import javax.validation.Valid;

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

import com.acelera.squad.four.hospital.models.Paciente;
import com.acelera.squad.four.hospital.service.PacienteService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "paciente")
@RequestMapping(path = "/v1")
@ExposesResourceFor(Paciente.class)
public class PacienteController {
	
	@Autowired
	private PacienteService pacienteService;

	@PostMapping("/pacientes")
	public ResponseEntity<Paciente> addPaciente(@Valid @RequestBody Paciente novoPaciente) {
		return ResponseEntity.ok(pacienteService.addPaciente(novoPaciente));
	}

	@GetMapping("/pacientes/{id}")
	public Paciente getPaciente(@PathVariable String id) {
		return pacienteService.getPaciente(id);
	}

	@GetMapping("/pacientes/todos")
	public ResponseEntity<Object> listarPacientes() {
		return ResponseEntity.ok().body(pacienteService.findAll());
	}

	@PutMapping("/pacientes/{id}")
	public ResponseEntity<Paciente> updatePaciente(@Valid @RequestBody Paciente pacienteUpdate, @PathVariable String id) {
		return ResponseEntity.ok(pacienteService.updatePaciente(pacienteUpdate, id));
	}

	@DeleteMapping("/pacientes/{id}")
	public ResponseEntity<String> deletePaciente(@PathVariable String id) {
		pacienteService.deletePaciente(id);

		return ResponseEntity.ok().body("Produto " + id + " apagado.");
	}

}
