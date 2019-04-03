package gestao.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;

import gestao.models.Leito;

public interface LeitoRepository extends MongoRepository<Leito, String> {

}
