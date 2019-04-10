package gestao.repositories;

import java.util.Collection;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import gestao.models.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, ObjectId> {
}
