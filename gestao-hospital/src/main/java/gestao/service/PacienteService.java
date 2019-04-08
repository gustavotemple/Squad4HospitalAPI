package gestao.service;

import java.util.Collection;

import org.bson.types.ObjectId;

import gestao.models.Paciente;

public interface PacienteService {

	public Paciente addPaciente(ObjectId hospitalId, Paciente novoPaciente);

	public Paciente getPaciente(ObjectId hospitalId, ObjectId pacienteId);

	public Paciente updatePaciente(ObjectId hospitalId, Paciente pacienteUpdate, ObjectId pacienteId);

	public void deletePaciente(ObjectId hospitalId, ObjectId pacienteId);

	public Collection<Paciente> findAll(ObjectId hospitalId);

	public void checkin(ObjectId hospitalId, ObjectId pacienteId);

	public void checkout(ObjectId hospitalId, ObjectId pacienteId);

}
