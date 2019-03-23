package com.acelera.squad.four.hospital.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acelera.squad.four.hospital.configuration.ApplicationConfig;
import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;
import com.acelera.squad.four.hospital.service.HospitalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = ApplicationConfig.HOSPITALS)
@RequestMapping(path = "/v1")
@ExposesResourceFor(Hospital.class)
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private HospitalRepository hospitalRepository;

	@PostMapping("/hospitais")
	@ApiOperation(value = "Adiciona um hospital")
	public ResponseEntity<Hospital> addHospital(@RequestBody Hospital hospital) {
		hospital.setLocalizacao(hospitalService.buscaCoordenadasPor(hospital.getEndereco()));
		hospital.set_id(ObjectId.get());

		hospitalRepository.save(hospital);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais")
	@ApiOperation(value = "Retorna os hospitais proximos de um endereco com leitos disponiveis")
	public ResponseEntity<List<Hospital>> getHospitalsByAddress(
			@RequestParam(value = "endereco", required = true) String endereco) {
		return ResponseEntity.ok().body(hospitalService.buscaHospitaisPor(endereco));
	}

	@GetMapping("/hospitais/{id}")
	@ApiOperation(value = "Retorna um hospital")
	public ResponseEntity<Hospital> getHospitalById(@PathVariable ObjectId id) {
		return ResponseEntity.ok().body(hospitalRepository.findBy_id(id));
	}

	@DeleteMapping("/hospitais/{id}")
	@ApiOperation(value = "Apaga um hospital")
	public ResponseEntity<String> deleteHospital(@PathVariable ObjectId id) {
		Hospital hospital = hospitalRepository.findBy_id(id);

		hospitalRepository.delete(hospital);

		return ResponseEntity.ok().body("Hospital " + hospital.getNome() + " apagado.");
	}

}
