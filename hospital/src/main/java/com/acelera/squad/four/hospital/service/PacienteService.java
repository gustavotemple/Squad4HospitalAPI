package com.acelera.squad.four.hospital.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.exceptions.HospitalNotFoundException;
import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.Leito;
import com.acelera.squad.four.hospital.models.Paciente;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;
import com.acelera.squad.four.hospital.repositories.LeitoRepository;
import com.acelera.squad.four.hospital.repositories.PacienteRepository;

@Service
public class PacienteService {
	@Autowired
	private PacienteRepository pacienteRepository;
	@Autowired
	private HospitalRepository hospitalRepository;
	@Autowired
	private LeitoRepository leitoreRepository;

	public Paciente addPaciente(ObjectId hospitalId, Paciente novoPaciente) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		Paciente paciente = new Paciente();
		paciente.setNome(novoPaciente.getNome());

		paciente.setCpf(novoPaciente.getCpf());
		paciente.setSexo(novoPaciente.getSexo());
		
		pacienteRepository.save(paciente);

		hospital.getPacientes().add(paciente);

		hospitalRepository.save(hospital);

		return paciente;
	}

	public Paciente getPaciente(ObjectId hospitalId, String pacienteId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		Paciente paciente = hospital.getPacientes().stream().filter(p -> pacienteId.equals(p.getId())).findFirst().orElse(null);
		if (Objects.isNull(paciente)) {
			/* handle this exception using {@link RestExceptionHandler} */
			throw new NullPointerException();
		}
		return new Paciente().build(paciente);
	}

	public Paciente updatePaciente(ObjectId hospitalId, Paciente pacienteUpdate, String pacienteId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		Paciente paciente = hospital.getPacientes().stream().filter(p -> pacienteId.equals(p.getId())).findFirst().orElse(null);
		if (Objects.isNull(paciente)) {
			/* handle this exception using {@link RestExceptionHandler} */
			throw new NullPointerException();
		}
		hospital.getPacientes().remove(paciente);

		paciente.setNome(pacienteUpdate.getNome());

		paciente.setCpf(pacienteUpdate.getCpf());
		paciente.setSexo(pacienteUpdate.getSexo());

		pacienteRepository.save(paciente);

		hospital.getPacientes().add(paciente);

		hospitalRepository.save(hospital);
		return new Paciente().build(paciente);
	}

	public void deletePaciente(ObjectId hospitalId, String pacienteId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		Paciente paciente = hospital.getPacientes().stream().filter(p -> pacienteId.equals(p.getId())).findFirst().orElse(null);
		if (Objects.isNull(paciente)) {
			/* handle this exception using {@link RestExceptionHandler} */
			throw new NullPointerException();
		}
		hospital.getPacientes().remove(paciente);

		hospitalRepository.save(hospital);
	}

	public Collection<Paciente> findAll(ObjectId hospitalId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		return hospital.getPacientes();
	}

	public void checkin(ObjectId hospitalId, String pacienteId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);


		Date checkin = new Date();
		Leito leito = new Leito(pacienteId, checkin, null);
		hospital.addLeito(leito);
		
		leitoreRepository.save(leito);
		hospitalRepository.save(hospital);

	}

	public void checkout(ObjectId hospitalId, String pacienteId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		Date checkout = new Date();

		Collection<Leito> leitos = hospital.getLeitos();
		Leito leito = leitos.stream().filter(o -> o.getPacienteId().equals(pacienteId)).findFirst().get();
		leito.setCheckout(checkout);
		
		hospital.removeLeito(leito);		
		leitoreRepository.save(leito);
		hospitalRepository.save(hospital);

	}

}
