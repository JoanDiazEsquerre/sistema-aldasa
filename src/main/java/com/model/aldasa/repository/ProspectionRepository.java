package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.Sucursal;

public interface ProspectionRepository extends PagingAndSortingRepository<Prospection, Integer>{
	
	List<Prospection> findByStatus(String status);
	Page<Prospection> findAllByPersonSupervisorSurnamesLikeAndPersonAssessorSurnamesLikeAndProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndStatusAndProjectSucursal(String surnamesPersonSupervisor, String surnamesPersonAssessor, String surnamesProspect, String dniProspect, String originContact, String assessorSurname, String status, Sucursal sucursal, Pageable pageable);
	Page<Prospection> findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatusAndProjectSucursal(String surnamesProspect, String dniProspect, String originContact, String assessorSurname,Person personAssessor, String status, Sucursal sucursal, Pageable pageable);
	Page<Prospection> findAllByProspectPersonSurnamesLikeAndProspectPersonDniLikeAndOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatusAndProjectSucursal(String surnamesProspect, String dniProspecto, String originContact, String assessorSurname,Person personSupervisor, String status, Sucursal sucursal, Pageable pageable);
	
	Prospection findByProspectAndStatus(Prospect prospect,String status);
	List<Prospection> findByProspect(Prospect prospect);

}
