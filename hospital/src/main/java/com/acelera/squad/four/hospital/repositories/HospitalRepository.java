package com.acelera.squad.four.hospital.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.acelera.squad.four.hospital.models.Hospital;

public interface HospitalRepository extends MongoRepository<Hospital, ObjectId> {
	Hospital findBy_id(ObjectId _id);
	List<Hospital> findByLocalizacaoNear(Point p);
}
