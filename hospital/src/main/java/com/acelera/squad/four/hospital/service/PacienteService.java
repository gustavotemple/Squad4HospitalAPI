package com.acelera.squad.four.hospital.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acelera.squad.four.hospital.models.Paciente;
import com.acelera.squad.four.hospital.repositories.PacienteRepository;


@Service
public class PacienteService {
	@Autowired
	private PacienteRepository pacienteRepository;

	public Paciente addPaciente(Paciente novoPaciente) {

		Paciente paciente = new Paciente();
		paciente.setId(novoPaciente.getId());
		paciente.setNome(novoPaciente.getNome());
		paciente.setCheckin(novoPaciente.getCheckin());
		paciente.setCheckout(novoPaciente.getCheckout());
		paciente.setCpf(novoPaciente.getCpf());
		paciente.setSexo(novoPaciente.getSexo());
		
		pacienteRepository.save(paciente);

		paciente.setId(novoPaciente.getId());

		return paciente;
	}

	public Paciente getPaciente(String id) {
		Paciente paciente = pacienteRepository.findOne(id);
		if (Objects.isNull(paciente)) {
			/* handle this exception using {@link RestExceptionHandler} */
			throw new NullPointerException();
		}
		return new Paciente().build(paciente);
	}

	public Paciente updatePaciente(Paciente pacienteUpdate, String id) {
		Paciente paciente = pacienteRepository.findOne(id);
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
		return new Paciente().build(paciente);
	}

	public void deletePaciente(String id) {
		pacienteRepository.delete(id);
	}

	public Object findAll() {
		return pacienteRepository.findAll();
	}

}
