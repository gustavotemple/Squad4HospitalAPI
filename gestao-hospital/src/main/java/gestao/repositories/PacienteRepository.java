package gestao.repositories;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import gestao.models.Paciente;

public interface PacienteRepository extends MongoRepository<Paciente, ObjectId>{

}
