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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acelera.squad.four.hospital.configuration.ApplicationConfig;
import com.acelera.squad.four.hospital.exceptions.HospitalNotFoundException;
import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.Leito;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;
import com.acelera.squad.four.hospital.repositories.PacienteRepository;
import com.acelera.squad.four.hospital.repositories.ProdutoRepository;
import com.acelera.squad.four.hospital.service.HospitalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = ApplicationConfig.HOSPITAIS)
@RequestMapping(path = "/v1")
@ExposesResourceFor(Hospital.class)
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private HospitalRepository hospitalRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private PacienteRepository pacienteRepository;

	@PostMapping("/hospitais")
	@ApiOperation(value = "Adiciona um hospital")
	public ResponseEntity<Hospital> addHospital(@Valid @RequestBody Hospital hospital) {
		
		hospital.setLocalizacao(hospitalService.buscaCoordenadasPor(hospital.getEndereco()));
		hospital.set_id(ObjectId.get());

		if (hospital.getEstoque() != null) {
			hospital.getEstoque().forEach(p -> {
				p.setId(ObjectId.get().toHexString());
				produtoRepository.save(p);
			});
		}
		if (hospital.getPacientes() != null) {
			hospital.getPacientes().forEach(p -> {
				p.setId(ObjectId.get().toHexString());
				pacienteRepository.save(p);
			});
		}

		hospitalRepository.save(hospital);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais/near/{id}")
	@ApiOperation(value = "Retorna os hospitais proximos com leitos disponiveis")
	public ResponseEntity<Hospital> getHospitalsByLocation(@PathVariable ObjectId id) {
		
		Hospital hospital = hospitalRepository.findBy_id(id);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(id);
		
		String endereco;
		Float lat = null, lng = null;
		endereco = hospital.getEndereco();
		
		return ResponseEntity.ok().body(hospitalService.buscaHospitaisPor(endereco, lat, lng));
	}

	@GetMapping("/hospitais/{id}")
	@ApiOperation(value = "Retorna um hospital")
	public ResponseEntity<Hospital> getHospitalById(@PathVariable ObjectId id) {
		Hospital hospital = hospitalRepository.findBy_id(id);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(id);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais/{id}/leitos")
	@ApiOperation(value = "Retorna os leitos de um hospital")
	public ResponseEntity<Collection<Leito>> getLeitosById(@PathVariable ObjectId id) {
		Hospital hospital = hospitalRepository.findBy_id(id);
		Collection<Leito> leitos = hospital.getLeitos();

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(id);

		
		return ResponseEntity.ok().body(leitos);
	}

	@DeleteMapping("/hospitais/{id}")
	@ApiOperation(value = "Apaga um hospital")
	public ResponseEntity<String> deleteHospital(@PathVariable String id) {
		if (!hospitalRepository.exists(id))
			throw new HospitalNotFoundException(id);

		hospitalRepository.delete(id);
		return ResponseEntity.ok().body("Hospital " + id + " apagado.");
	}
	
	
	@GetMapping("/hospitais/todos")
	public ResponseEntity<Collection<Hospital>> listarProdutos() {
		return ResponseEntity.ok().body(hospitalRepository.findAll());
	}

}
