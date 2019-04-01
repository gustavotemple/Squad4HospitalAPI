package com.acelera.squad.four.hospital.repositories;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.acelera.squad.four.hospital.models.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, ObjectId> {

}
