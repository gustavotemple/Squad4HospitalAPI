package com.acelera.squad.four.hospital.service;

import java.util.Collection;

import org.bson.types.ObjectId;

import com.acelera.squad.four.hospital.models.Hospital;
import com.acelera.squad.four.hospital.models.HospitalDTO;
import com.acelera.squad.four.hospital.models.Leito;

public interface HospitalService {

	public Hospital addHospital(HospitalDTO hospitalDTO);

	public Hospital getHospitalNearById(ObjectId hospitalId);

	public Hospital getHospitalNearByAddress(String endereco);

	public Hospital getHospitalById(ObjectId hospitalId);

	public Hospital updateHospital(ObjectId hospitalId, HospitalDTO hospitalDTO);

	public Collection<Leito> getLeitosById(ObjectId hospitalId);

	public void deleteHospital(ObjectId hospitalId);

	public Collection<Hospital> findAll();

}
