package com.acelera.squad.four.hospital.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
		final Paciente paciente = pacienteService.addPaciente(id, novoPaciente);
		
		paciente.add(linkTo(methodOn(PacienteController.class).getPaciente(id, paciente.getObjectId())).withSelfRel());
		
		return ResponseEntity.ok(paciente);
	}
	
	@GetMapping("/hospitais/pacientes/near")
	@ApiOperation(value = "Retorna o hospital mais proximo com leitos disponiveis")
	public ResponseEntity<Hospital> getHospitalsByLocation(
			@RequestParam(value = "endereco", required = true) String endereco) {
		final Hospital hospital = hospitalService.hospitalMaisProximoPaciente(endereco);
		
		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());
		
		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais/{id}/pacientes/{paciente}")
	@ApiOperation(value = "Retorna todas as informacoes do paciente cadastrado")
	public ResponseEntity<Paciente> getPaciente(@PathVariable ObjectId id, @PathVariable ObjectId paciente) {
		final Paciente p = pacienteService.getPaciente(id, paciente);
		
		p.add(linkTo(methodOn(PacienteController.class).getPaciente(id, p.getObjectId())).withSelfRel());
		
		return ResponseEntity.ok().body(p);
	}

	@GetMapping("/hospitais/{id}/pacientes")
	@ApiOperation(value = "Retorna os pacientes dentro do hospital")
	public ResponseEntity<Collection<Paciente>> listarPacientes(@PathVariable ObjectId id) {
		return ResponseEntity.ok().body(pacienteService.findAll(id));
	}

	@PutMapping("/hospitais/{id}/pacientes/{paciente}")
	@ApiOperation(value = "Atualiza um paciente")
	public ResponseEntity<Paciente> updatePaciente(@PathVariable ObjectId id, @Valid @RequestBody Paciente pacienteUpdate, @PathVariable ObjectId paciente) {
		final Paciente p = pacienteService.updatePaciente(id, pacienteUpdate, paciente);
		
		p.add(linkTo(methodOn(PacienteController.class).getPaciente(id, p.getObjectId())).withSelfRel());
		
		return ResponseEntity.ok().body(p);
	}

	@DeleteMapping("/hospitais/{id}/pacientes/{paciente}")
	@ApiOperation(value = "Exclui um paciente")
	public ResponseEntity<String> deletePaciente(@PathVariable ObjectId id, @PathVariable ObjectId paciente) {
		pacienteService.deletePaciente(id, paciente);

		return ResponseEntity.ok().body("Paciente " + id + " apagado.");
	}

	@PostMapping("/hospitais/{id}/pacientes/{paciente}/checkin")
	public ResponseEntity<String> checkin(@PathVariable ObjectId id, @PathVariable ObjectId paciente) {
		pacienteService.checkin(id, paciente);
		
		return ResponseEntity.ok().body("Checkin feito com sucesso");
	}
	
}
