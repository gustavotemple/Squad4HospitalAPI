package gestao.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import gestao.models.Hospital;

public interface HospitalRepository extends MongoRepository<Hospital, ObjectId> {
	List<Hospital> findByLocalizacaoNear(Point p);
}
