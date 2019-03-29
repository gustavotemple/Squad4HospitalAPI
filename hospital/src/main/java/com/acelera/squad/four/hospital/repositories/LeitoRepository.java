package com.acelera.squad.four.hospital.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.acelera.squad.four.hospital.models.Leito;

public interface LeitoRepository extends MongoRepository<Leito, String> {

}
