package com.acelera.squad.four.hospital.repositories;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.acelera.squad.four.hospital.models.Paciente;

public interface PacienteRepository extends MongoRepository<Paciente, ObjectId>{

}
