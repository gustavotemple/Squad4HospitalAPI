package com.acelera.squad.four.hospital.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;
import java.util.Objects;

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

import com.acelera.squad.four.hospital.configuration.ApplicationConfig;
import com.acelera.squad.four.hospital.exceptions.HospitalNotFoundException;
import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.Leito;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;
import com.acelera.squad.four.hospital.service.HospitalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = ApplicationConfig.HOSPITAIS)
@RequestMapping(path = "/v1")
@ExposesResourceFor(Hospital.class)
public class HospitalController {

	private HospitalService hospitalService;
	private HospitalRepository hospitalRepository;

	@Autowired
	public HospitalController(HospitalService hospitalService, HospitalRepository hospitalRepository) {
		this.hospitalService = hospitalService;
		this.hospitalRepository = hospitalRepository;
	}

	@PostMapping("/hospitais")
	@ApiOperation(value = "Adiciona um hospital")
	public ResponseEntity<Hospital> addHospital(@Valid @RequestBody Hospital hospital) {	
		hospital.setLocalizacao(hospitalService.buscaCoordenadasPor(hospital.getEndereco()));

		hospitalRepository.save(hospital);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais/near/{id}")
	@ApiOperation(value = "Retorna o hospital mais proximo com estoque disponivel")
	public ResponseEntity<Hospital> getHospitalByLocation(@PathVariable ObjectId id) {		
		final Hospital hospital = findHospitalBy(id);
		
		final Hospital hospitalMaisProximo = hospitalService.hospitalMaisProximoHospital(hospital);
		
		hospitalMaisProximo.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());
		
		return ResponseEntity.ok().body(hospitalMaisProximo);
	}

	@GetMapping("/hospitais/{id}")
	@ApiOperation(value = "Retorna um hospital")
	public ResponseEntity<Hospital> getHospitalById(@PathVariable ObjectId id) {
		final Hospital hospital = findHospitalBy(id);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}
	
	@PutMapping("/hospitais/{id}")
	@ApiOperation(value = "Atualiza um hospital")
	public ResponseEntity<Hospital> updateHospital(@PathVariable ObjectId id, @Valid @RequestBody Hospital h) {
		final Hospital hospital = findHospitalBy(id);
		
		hospital.setEndereco(h.getEndereco());
		hospital.setNome(h.getNome());
		hospital.setLeitosTotais(h.getLeitosTotais());
		
		hospitalRepository.save(hospital);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais/{id}/leitos")
	@ApiOperation(value = "Retorna os leitos de um hospital")
	public ResponseEntity<Collection<Leito>> getLeitosById(@PathVariable ObjectId id) {
		final Hospital hospital = findHospitalBy(id);
		
		Collection<Leito> leitos = hospital.getLeitos();

		return ResponseEntity.ok().body(leitos);
	}

	@DeleteMapping("/hospitais/{id}")
	@ApiOperation(value = "Exclui um hospital")
	public ResponseEntity<String> deleteHospital(@PathVariable ObjectId id) {
		if (!hospitalRepository.exists(id))
			throw new HospitalNotFoundException(id);

		hospitalRepository.delete(id);
		return ResponseEntity.ok().body("Hospital " + id + " apagado.");
	}
	
	@GetMapping("/hospitais")
	@ApiOperation(value = "Retorna todos hospitais")
	public ResponseEntity<Collection<Hospital>> listarHospitais() {
		return ResponseEntity.ok().body(hospitalRepository.findAll());
	}

	private Hospital findHospitalBy(ObjectId hospitalId) {
		final Hospital hospital = hospitalRepository.findBy_id(hospitalId);
		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);
		return hospital;
	}

}
