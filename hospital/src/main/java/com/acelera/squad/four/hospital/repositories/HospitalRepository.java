package com.acelera.squad.four.hospital.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.acelera.squad.four.hospital.models.Hospital;

public interface HospitalRepository extends MongoRepository<Hospital, String> {
	Hospital findBy_id(ObjectId _id);
}
