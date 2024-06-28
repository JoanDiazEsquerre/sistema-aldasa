package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.ProspectionRepository;
import com.model.aldasa.service.ProspectionService;

@Service("prospectionService")
public class ProspectionServiceImpl implements ProspectionService {
	
	@Autowired
	private ProspectionRepository prospectionRepository;

	@Override
	public Optional<Prospection> findById(Integer id) {
		return prospectionRepository.findById(id);
	}

	@Override
	public Prospection save(Prospection prospection) {
		return prospectionRepository.save(prospection);
	}

	@Override
	public void delete(Prospection prospection) {
		prospectionRepository.delete(prospection);
	}

	@Override
	public List<Prospection> findByStatus(String status) {
		return prospectionRepository.findByStatus(status);
	}

	@Override
	public Page<Prospection> findAllByPersonSupervisorSurnamesLikeAndPersonAssessorSurnamesLikeAndProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndStatusAndProjectSucursal(String surnamesPersonSupervisor, String surnamesPersonAssessor, String surnamesProspect, String dniProspect, String originContact, String assessorSurname, String status, Sucursal sucursal, Pageable pageable) {
		return prospectionRepository.findAllByPersonSupervisorSurnamesLikeAndPersonAssessorSurnamesLikeAndProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndStatusAndProjectSucursal(surnamesPersonSupervisor, surnamesPersonAssessor, surnamesProspect, dniProspect, originContact,assessorSurname, status, sucursal,  pageable);
	}
	
	@Override
	public Page<Prospection> findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatusAndProjectSucursal(String surnamesProspect, String dniProspecto, String originContact, String assessorSurname, Person personAssessor, String status, Sucursal sucursal,  Pageable pageable) {
		// TODO Auto-generated method stub
		return prospectionRepository.findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatusAndProjectSucursal(surnamesProspect, dniProspecto, originContact, assessorSurname, personAssessor, status, sucursal, pageable);
	}

	@Override
	public Page<Prospection> findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatusAndProjectSucursal(
			String surnamesProspecto, String dniProspecto, String originContact, String assessorSurname, Person personSupervisor, String status, Sucursal sucursal, Pageable pageable) {
		// TODO Auto-generated method stub
		return prospectionRepository.findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatusAndProjectSucursal(surnamesProspecto, dniProspecto, originContact, assessorSurname, personSupervisor, status, sucursal, pageable);
	}
	
	@Override
	public Prospection findByProspectAndStatus(Prospect prospect,String status) {
		return prospectionRepository.findByProspectAndStatus(prospect, status);
	}

	@Override
	public List<Prospection> findByProspect(Prospect prospect) {
		// TODO Auto-generated method stub
		return prospectionRepository.findByProspect(prospect);
	}

	

}
