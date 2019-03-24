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
import com.acelera.squad.four.hospital.models.HospitalDTO;
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
	public ResponseEntity<Hospital> addHospital(@RequestBody Hospital hospital) {
		hospital.setLocalizacao(hospitalService.buscaCoordenadasPor(hospital.getEndereco()));
		hospital.set_id(ObjectId.get());

		hospital.getEstoque().forEach(p -> {
			p.setId(ObjectId.get().toHexString());
			produtoRepository.save(p);
		});
		hospital.getPacientes().forEach(p -> {
			p.setId(ObjectId.get().toHexString());
			pacienteRepository.save(p);
		});

		hospitalRepository.save(hospital);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais")
	@ApiOperation(value = "Retorna os hospitais proximos com leitos disponiveis")
	public ResponseEntity<List<Hospital>> getHospitalsByLocation(
			@RequestParam(value = "endereco", required = false) String endereco,
			@RequestParam(value = "latitude", required = false) Float lat,
			@RequestParam(value = "longitude", required = false) Float lng) {
		return ResponseEntity.ok().body(hospitalService.buscaHospitaisPor(endereco, lat, lng));
	}

	@GetMapping("/hospitais/{id}")
	@ApiOperation(value = "Retorna um hospital")
	public ResponseEntity<Hospital> getHospitalById(@PathVariable ObjectId id) {
		Hospital hospital = hospitalRepository.findBy_id(id);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/hospitais/{id}/leitos")
	@ApiOperation(value = "Retorna os leitos de um hospital")
	public ResponseEntity<HospitalDTO> getLeitosById(@PathVariable ObjectId id) {
		Hospital hospital = hospitalRepository.findBy_id(id);

		HospitalDTO hospitalDTO = new HospitalDTO(hospital);

		return ResponseEntity.ok().body(hospitalDTO);
	}

	@DeleteMapping("/hospitais/{id}")
	@ApiOperation(value = "Apaga um hospital")
	public ResponseEntity<String> deleteHospital(@PathVariable String id) {
		hospitalRepository.delete(id);
		return ResponseEntity.ok().body("Hospital " + id + " apagado.");
	}

}
