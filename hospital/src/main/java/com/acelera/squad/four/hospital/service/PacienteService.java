package com.acelera.squad.four.hospital.service;

import java.util.Collection;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.exceptions.HospitalNotFoundException;
import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.Paciente;
import com.acelera.squad.four.hospital.repositories.HospitalRepository;
import com.acelera.squad.four.hospital.repositories.PacienteRepository;

@Service
public class PacienteService {
	@Autowired
	private PacienteRepository pacienteRepository;
	@Autowired
	private HospitalRepository hospitalRepository;

	public Paciente addPaciente(ObjectId hospitalId, Paciente novoPaciente) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		Paciente paciente = new Paciente();
		paciente.setNome(novoPaciente.getNome());
		paciente.setCheckin(novoPaciente.getCheckin());
		paciente.setCheckout(novoPaciente.getCheckout());
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

		paciente.setNome(pacienteUpdate.getNome());
		paciente.setCheckin(pacienteUpdate.getCheckin());
		paciente.setCheckout(pacienteUpdate.getCheckout());
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

		hospital.getPacientes().remove(paciente);

		hospitalRepository.save(hospital);
	}

	public Collection<Paciente> findAll(ObjectId hospitalId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		return hospital.getPacientes();
	}

}
