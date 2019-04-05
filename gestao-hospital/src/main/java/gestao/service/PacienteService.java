package gestao.service;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exceptions.HospitalNotFoundException;
import gestao.exceptions.LeitoNotFoundExcpetion;
import gestao.exceptions.PacienteNotFoundException;
import gestao.models.Hospital;
import gestao.models.Leito;
import gestao.models.Paciente;
import gestao.repositories.HospitalRepository;
import gestao.repositories.LeitoRepository;
import gestao.repositories.PacienteRepository;

@Service
public class PacienteService {
	@Autowired
	private PacienteRepository pacienteRepository;
	@Autowired
	private HospitalRepository hospitalRepository;
	@Autowired
	private LeitoRepository leitoreRepository;

	public Paciente addPaciente(ObjectId hospitalId, Paciente novoPaciente) {
		final Paciente paciente = new Paciente();
		paciente.setNome(novoPaciente.getNome());
		paciente.setCpf(novoPaciente.getCpf());
		paciente.setSexo(novoPaciente.getSexo());
		pacienteRepository.save(paciente);

		final Hospital hospital = findHospitalBy(hospitalId);
		hospital.getPacientes().add(paciente);
		hospitalRepository.save(hospital);

		return paciente;
	}

	public Paciente getPaciente(ObjectId hospitalId, ObjectId pacienteId) {
		final Paciente paciente = findPacienteBy(findHospitalBy(hospitalId), pacienteId);
		
		return new Paciente().build(paciente);
	}

	public Paciente updatePaciente(ObjectId hospitalId, Paciente pacienteUpdate, ObjectId pacienteId) {
		final Paciente paciente = findPacienteBy(findHospitalBy(hospitalId), pacienteId);

		paciente.setNome(pacienteUpdate.getNome());
		paciente.setCpf(pacienteUpdate.getCpf());
		paciente.setSexo(pacienteUpdate.getSexo());
		pacienteRepository.save(paciente);

		return new Paciente().build(paciente);
	}

	public void deletePaciente(ObjectId hospitalId, ObjectId pacienteId) {
		final Hospital hospital = findHospitalBy(hospitalId);

		final Paciente paciente = findPacienteBy(hospital, pacienteId);
		
		hospital.getPacientes().remove(paciente);
		hospitalRepository.save(hospital);
		
		pacienteRepository.delete(pacienteId);
	}

	public Collection<Paciente> findAll(ObjectId hospitalId) {
		final Hospital hospital = findHospitalBy(hospitalId);

		return hospital.getPacientes();
	}

	public void checkin(ObjectId hospitalId, ObjectId pacienteId) {
		final Hospital hospital = findHospitalBy(hospitalId);

		Date checkin = new Date();
		Leito leito = new Leito(pacienteId, checkin, null);
		hospital.addLeito(leito);
		
		leitoreRepository.save(leito);
		hospitalRepository.save(hospital);
	}

	private Paciente findPacienteBy(Hospital hospital, ObjectId pacienteId) {
		final Paciente paciente = hospital.getPacientes().stream().filter(p -> pacienteId.equals(p.getObjectId())).findFirst()
				.orElse(null);
		if (Objects.isNull(paciente))
			throw new PacienteNotFoundException(pacienteId);
		return paciente;
	}

	public void checkout(ObjectId hospitalId, ObjectId pacienteId) {
		Hospital hospital = hospitalRepository.findBy_id(hospitalId);

		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);

		Date checkout = new Date();

		Collection<Leito> leitos = hospital.getLeitos();
		Leito leito = leitos.stream().filter(o -> o.getPacienteId().equals(pacienteId)).findFirst().orElse(null);
		
		if(Objects.isNull(leito))
			throw new LeitoNotFoundExcpetion(hospitalId);
			
		leito.setCheckout(checkout);
		
		hospital.removeLeito(leito);		
		leitoreRepository.save(leito);
		hospitalRepository.save(hospital);
	}

	private Hospital findHospitalBy(ObjectId hospitalId) {
		final Hospital hospital = hospitalRepository.findBy_id(hospitalId);
		if (Objects.isNull(hospital))
			throw new HospitalNotFoundException(hospitalId);
		return hospital;
	}

}
