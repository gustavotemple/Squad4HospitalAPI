package gestao.service;

import java.util.Collection;

import org.bson.types.ObjectId;

import gestao.models.Hospital;
import gestao.models.HospitalDTO;
import gestao.models.Leito;

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
