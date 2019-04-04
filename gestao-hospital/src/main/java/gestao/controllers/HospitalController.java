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
import gestao.models.Hospital;
import gestao.models.Leito;
import gestao.service.HospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = ApplicationConfig.HOSPITAIS)
@RequestMapping(path = "/v1")
@ExposesResourceFor(Hospital.class)
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	@PostMapping("/hospitais")
	@ApiOperation(value = "Adiciona um hospital")
	public ResponseEntity<Hospital> addHospital(@Valid @RequestBody Hospital hospital) {

		hospitalService.addHospital(hospital);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais/near/{id}")
	@ApiOperation(value = "Retorna o hospital mais proximo com estoque disponivel")
	public ResponseEntity<Hospital> getHospitalByLocation(@PathVariable ObjectId id) {

		final Hospital hospitalMaisProximo = hospitalService.hospitalMaisProximoHospital(id);

		hospitalMaisProximo
				.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospitalMaisProximo.getObjectId()))
						.withSelfRel());

		return ResponseEntity.ok().body(hospitalMaisProximo);
	}

	@GetMapping("/hospitais/{id}")
	@ApiOperation(value = "Retorna um hospital")
	public ResponseEntity<Hospital> getHospitalById(@PathVariable ObjectId id) {

		Hospital hospital = hospitalService.getHospital(id);

		return ResponseEntity.ok().body(hospital);
	}

	@PutMapping("/hospitais/{id}")
	@ApiOperation(value = "Atualiza um hospital")
	public ResponseEntity<Hospital> updateHospital(@PathVariable ObjectId id,
			@Valid @RequestBody Hospital hospitalUpdate) {

		Hospital hospital = hospitalService.updateHospital(id, hospitalUpdate);
		
		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());
		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais/{id}/leitos")
	@ApiOperation(value = "Retorna os leitos de um hospital")
	public ResponseEntity<Collection<Leito>> getLeitosById(@PathVariable ObjectId id) {

		Collection<Leito> leitos = hospitalService.getLeitosHospital(id);

		return ResponseEntity.ok().body(leitos);
	}

	@DeleteMapping("/hospitais/{id}")
	@ApiOperation(value = "Exclui um hospital")
	public ResponseEntity<String> deleteHospital(@PathVariable String id) {
		hospitalService.deleteHospital(id);
		return ResponseEntity.ok().body("Hospital " + id + " apagado.");
	}

	@GetMapping("/hospitais")
	@ApiOperation(value = "Retorna todos hospitais")
	public ResponseEntity<Collection<Hospital>> listarHospitais() {
		return ResponseEntity.ok().body(hospitalService.getHospitais());
	}

}
