package gestao.repositories;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import gestao.models.Leito;

public interface LeitoRepository extends MongoRepository<Leito, ObjectId> {

}
