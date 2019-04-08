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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestao.configuration.ApplicationConfig;
import gestao.models.Hospital;
import gestao.models.HospitalDTO;
import gestao.models.Leito;
import gestao.service.HospitalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = ApplicationConfig.HOSPITAIS)
@RequestMapping(path = ApplicationConfig.BASE_URL)
@ExposesResourceFor(Hospital.class)
public class HospitalController {

	private HospitalService hospitalService;

	@Autowired
	public HospitalController(HospitalService hospitalService) {
		this.hospitalService = hospitalService;
	}

	@PostMapping
	@ApiOperation(value = "Adiciona um hospital")
	public ResponseEntity<Hospital> addHospital(@Valid @RequestBody HospitalDTO hospitalDTO) {
		final Hospital hospital = hospitalService.addHospital(hospitalDTO);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/near/{hospital}")
	@ApiOperation(value = "Retorna o hospital mais proximo com estoque disponivel")
	public ResponseEntity<Hospital> getHospitalNearById(@PathVariable("hospital") ObjectId hospitalId) {
		final Hospital hospital = hospitalService.getHospitalNearById(hospitalId);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/near/paciente")
	@ApiOperation(value = "Retorna o hospital mais proximo com leitos disponiveis")
	public ResponseEntity<Hospital> getHospitalNearByAddress(
			@RequestParam(value = "endereco", required = true) String endereco) {
		final Hospital hospital = hospitalService.getHospitalNearByAddress(endereco);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Retorna um hospital")
	public ResponseEntity<Hospital> getHospitalById(@PathVariable("id") ObjectId hospitalId) {
		final Hospital hospital = hospitalService.getHospitalById(hospitalId);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@PutMapping("/{id}")
	@ApiOperation(value = "Atualiza um hospital")
	public ResponseEntity<Hospital> updateHospital(@PathVariable("id") ObjectId hospitalId,
			@Valid @RequestBody HospitalDTO hospitalDTO) {
		final Hospital hospital = hospitalService.updateHospital(hospitalId, hospitalDTO);

		hospital.add(linkTo(methodOn(HospitalController.class).getHospitalById(hospital.getObjectId())).withSelfRel());

		return ResponseEntity.ok().body(hospital);
	}

	@GetMapping("/{id}/leitos")
	@ApiOperation(value = "Retorna os leitos de um hospital")
	public ResponseEntity<Collection<Leito>> getLeitosById(@PathVariable("id") ObjectId hospitalId) {
		return ResponseEntity.ok().body(hospitalService.getLeitosById(hospitalId));
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Exclui um hospital")
	public ResponseEntity<String> deleteHospital(@PathVariable("id") ObjectId hospitalId) {
		hospitalService.deleteHospital(hospitalId);

		return ResponseEntity.ok().body("Hospital " + hospitalId + " apagado.");
	}

	@GetMapping
	@ApiOperation(value = "Retorna todos hospitais")
	public ResponseEntity<Collection<Hospital>> listarHospitais() {
		return ResponseEntity.ok().body(hospitalService.findAll());
	}

}
