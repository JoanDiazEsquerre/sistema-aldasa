package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.Sucursal;

public interface ProspectionService {
	
	Optional<Prospection> findById(Integer id);
	Prospection save(Prospection prospection);
	void delete(Prospection prospection);
	List<Prospection> findByStatus(String status);
	
	Page<Prospection> findAllByPersonSupervisorSurnamesLikeAndPersonAssessorSurnamesLikeAndProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndStatusAndProjectSucursal(String surnamesPersonSupervisor, String surnamesPersonAssessor, String surnamesProspect, String dniProspect, String originContact, String assessorSurname, String status, Sucursal sucursal, Pageable pageable);
	Page<Prospection> findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatusAndProjectSucursal(String surnamesProspect, String dniProspect, String originContact, String assessorSurname,Person personAssessor, String status, Sucursal sucursal, Pageable pageable);
	Page<Prospection> findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatusAndProjectSucursal(String surnamesProspect, String dniProspecto, String originContact, String assessorSurname,Person personSupervisor, String status, Sucursal sucursal, Pageable pageable);
	
	Prospection findByProspectAndStatus(Prospect prospect,String status);
	
	List<Prospection> findByProspect(Prospect prospect);

}
