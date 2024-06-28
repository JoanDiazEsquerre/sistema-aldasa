package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.repository.ProspectionDetailRepository;
import com.model.aldasa.service.ProspectionDetailService;

@Service("prospectionDetailService")
public class ProspectionDetailServiceImpl implements ProspectionDetailService{
	
	@Autowired
	private ProspectionDetailRepository prospectionDetailRepository;

	@Override
	public Optional<ProspectionDetail> findById(Integer id) {
		return prospectionDetailRepository.findById(id);
	}

	@Override
	public ProspectionDetail save(ProspectionDetail entity) {
		return prospectionDetailRepository.save(entity);
	}

	@Override
	public void delete(ProspectionDetail prospectionDetail) {
		prospectionDetailRepository.delete(prospectionDetail);
		
	}

	@Override
	public List<ProspectionDetail> findByProspectionAndScheduled(Prospection prospection,boolean scheduled) {
		return prospectionDetailRepository.findByProspectionAndScheduled(prospection,scheduled); 
	}

	@Override
	public List<ProspectionDetail> findByProspectionStatusAndScheduledAndDateBetween(String prospectionStatus, boolean scheduled,Date  dateStart,Date dateFinish) {
		return prospectionDetailRepository.findByProspectionStatusAndScheduledAndDateBetween(prospectionStatus, scheduled,dateStart,dateFinish);
	}

	@Override
	public List<ProspectionDetail> findByProspectionPersonAssessorAndProspectionStatusAndScheduledAndDateBetween(Person personAssessor, String prospectionStatus, boolean scheduled,Date dateStart,Date dateFinish) {
		return prospectionDetailRepository.findByProspectionPersonAssessorAndProspectionStatusAndScheduledAndDateBetween(personAssessor, prospectionStatus, scheduled,dateStart,dateFinish);
	}

	@Override
	public List<ProspectionDetail> findByProspectionPersonSupervisorAndProspectionStatusAndScheduledAndDateBetween(Person personSupervisor, String prospectionStatus, boolean scheduled,Date dateStart,Date dateFinish) {
		return prospectionDetailRepository.findByProspectionPersonSupervisorAndProspectionStatusAndScheduledAndDateBetween(personSupervisor, prospectionStatus, scheduled,dateStart,dateFinish);
	}

//	@Override
//	public List<ProspectionDetail> findByScheduledAndProspectionProspectPersonIdLikeAndProspectionPersonAssessorIdLikeAndActionIdLikeAndProspectionOriginContactLikeAndProspectionProjectIdLikeAndDateBetween(
//			boolean scheduled, String idPerson, String idPersonAssessor, String idAction, String originContact,String idProject,Date fechaIni, Date fechaFin) {
//		return prospectionDetailRepository.findByScheduledAndProspectionProspectPersonIdLikeAndProspectionPersonAssessorIdLikeAndActionIdLikeAndProspectionOriginContactLikeAndProspectionProjectIdLikeAndDateBetween(scheduled, idPerson, idPersonAssessor, idAction, originContact, idProject, fechaIni, fechaFin);
//	}
	
	@Override
	public List<ProspectionDetail> findByScheduledAndProspectionProspectPerson(boolean scheduled,Person person) {
		return prospectionDetailRepository.findByScheduledAndProspectionProspectPerson(scheduled, person);
	}

	@Override
	public List<ProspectionDetail> findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(
			boolean scheduled,Date  dateStart,Date dateFinish,String personSurnames,String assessorDni,String supervisorDni,String action,String originContact,String project){
		return prospectionDetailRepository.findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(scheduled, dateStart, dateFinish, personSurnames,assessorDni,supervisorDni,action,originContact,project);
	}


}
