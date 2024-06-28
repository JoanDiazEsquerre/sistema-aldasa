package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.ProspectRepository;
import com.model.aldasa.service.ProspectService;

@Service("prospectService")
public class ProspectServiceImpl implements ProspectService{
	
	@Autowired
	private ProspectRepository prospectRepository;

	@Override
	public Optional<Prospect> findById(Integer id) {
		return prospectRepository.findById(id);
	}

	@Override
	public Prospect save(Prospect entity) {
		return prospectRepository.save(entity);
	}

	@Override
	public void delete(Prospect entity) {
		prospectRepository.delete(entity);
	}

	@Override
	public Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessorAndSucursal(String dniPerson,String surnamesPerson,Person assessor,Sucursal sucursal,Pageable pageable) {
		return prospectRepository.findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessorAndSucursal( dniPerson, surnamesPerson, assessor, sucursal, pageable); 
	}
	
	@Override
	public Page<Prospect> findByPersonSurnamesLikeAndPersonAssessorAndSucursal(String surnamesPerson,Person assessor,Sucursal sucursal,Pageable pageable) {
		return prospectRepository.findByPersonSurnamesLikeAndPersonAssessorAndSucursal( surnamesPerson, assessor, sucursal, pageable); 
	}
	
	@Override
	public Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisorAndSucursal(String dni, String surnamesPerson,Person supervisor,Sucursal sucursal,Pageable pageable) {
		return prospectRepository.findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisorAndSucursal(dni, surnamesPerson, supervisor, sucursal, pageable); 
	}
	
	@Override
	public Page<Prospect> findByPersonSurnamesLikeAndPersonSupervisorAndSucursal(String surnamesPerson,Person supervisor,Sucursal sucursal,Pageable pageable) {
		return prospectRepository.findByPersonSurnamesLikeAndPersonSupervisorAndSucursal(surnamesPerson, supervisor, sucursal, pageable); 
	}

	@Override
	public Page<Prospect> findByPersonSurnamesLikeAndPersonDniLikeAndSucursal(String surnamesPerson, String dni , Sucursal sucursal, Pageable pageable) {
		return prospectRepository.findByPersonSurnamesLikeAndPersonDniLikeAndSucursal(surnamesPerson, dni, sucursal, pageable);
	}
	
	@Override
	public Page<Prospect> findByPersonSurnamesLikeAndSucursal(String surnamesPerson, Sucursal sucursal, Pageable pageable) {
		return prospectRepository.findByPersonSurnamesLikeAndSucursal(surnamesPerson, sucursal, pageable);
	}

	@Override
	public Prospect findByPersonAndSucursal(Person entity, Sucursal sucursal) {
		return prospectRepository.findByPersonAndSucursal(entity,sucursal);
	}

	@Override
	public List<Prospect> findByPersonAssessor(Person assessor) {
		return prospectRepository.findByPersonAssessor(assessor);
	}

	@Override
	public List<Prospect> findByPersonSupervisor(Person supervisor) {
		return prospectRepository.findByPersonSupervisor(supervisor);
	}

	@Override
	public List<Prospect> findAll() {
		return prospectRepository.findAll();
	}

}
