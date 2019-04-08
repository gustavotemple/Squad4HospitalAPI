package gestao.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import gestao.configuration.ApplicationConfig;
import gestao.models.Paciente;
import gestao.service.PacienteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "pacientes")
@RequestMapping(path = ApplicationConfig.BASE_URL + "/{id}/pacientes")
@ExposesResourceFor(Paciente.class)
public class PacienteController {

	private PacienteService pacienteService;

	@Autowired
	public PacienteController(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	@PostMapping
	@ApiOperation(value = "Adiciona um paciente em um hospital")
	public ResponseEntity<Paciente> addPaciente(@PathVariable("id") ObjectId hospitalId,
			@Valid @RequestBody Paciente novoPaciente) {
		final Paciente paciente = pacienteService.addPaciente(hospitalId, novoPaciente);

		paciente.add(linkTo(methodOn(PacienteController.class).getPaciente(hospitalId, paciente.getObjectId()))
				.withSelfRel());

		return ResponseEntity.ok(paciente);
	}

	@GetMapping("/{paciente}")
	@ApiOperation(value = "Retorna todas as informacoes do paciente cadastrado")
	public ResponseEntity<Paciente> getPaciente(@PathVariable("id") ObjectId hospitalId,
			@PathVariable("paciente") ObjectId pacienteId) {
		final Paciente paciente = pacienteService.getPaciente(hospitalId, pacienteId);

		paciente.add(linkTo(methodOn(PacienteController.class).getPaciente(hospitalId, paciente.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(paciente);
	}

	@GetMapping
	@ApiOperation(value = "Retorna os pacientes dentro do hospital")
	public ResponseEntity<Collection<Paciente>> listarPacientes(@PathVariable("id") ObjectId hospitalId) {
		return ResponseEntity.ok().body(pacienteService.findAll(hospitalId));
	}

	@PutMapping("/{paciente}")
	@ApiOperation(value = "Atualiza um paciente")
	public ResponseEntity<Paciente> updatePaciente(@PathVariable("id") ObjectId hospitalId,
			@Valid @RequestBody Paciente pacienteUpdate, @PathVariable("paciente") ObjectId pacienteId) {
		final Paciente paciente = pacienteService.updatePaciente(hospitalId, pacienteUpdate, pacienteId);

		paciente.add(linkTo(methodOn(PacienteController.class).getPaciente(hospitalId, paciente.getObjectId()))
				.withSelfRel());

		return ResponseEntity.ok().body(paciente);
	}

	@DeleteMapping("/{paciente}")
	@ApiOperation(value = "Exclui um paciente")
	public ResponseEntity<String> deletePaciente(@PathVariable("id") ObjectId hospitalId,
			@PathVariable("paciente") ObjectId pacienteId) {
		pacienteService.deletePaciente(hospitalId, pacienteId);

		return ResponseEntity.ok().body("Paciente " + pacienteId + " apagado.");
	}

	@PostMapping("/{paciente}/checkin")
	@ApiOperation(value = "Realiza o checkin de um paciente")
	public ResponseEntity<String> checkin(@PathVariable("id") ObjectId hospitalId,
			@PathVariable("paciente") ObjectId pacienteId) {
		pacienteService.checkin(hospitalId, pacienteId);

		return ResponseEntity.ok().body("Checkin feito com sucesso");
	}

	@PostMapping("/{paciente}/checkout")
	@ApiOperation(value = "Realiza o checkout de um paciente")
	public ResponseEntity<String> checkout(@PathVariable("id") ObjectId hospitalId,
			@PathVariable("paciente") ObjectId pacienteId) {
		pacienteService.checkout(hospitalId, pacienteId);

		return ResponseEntity.ok().body("Checkout feito com sucesso");
	}
}
