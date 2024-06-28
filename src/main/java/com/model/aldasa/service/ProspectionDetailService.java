package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;

public interface ProspectionDetailService {
	
	Optional<ProspectionDetail> findById(Integer id);
	ProspectionDetail save(ProspectionDetail entity);
	void delete(ProspectionDetail entity);
	List<ProspectionDetail> findByProspectionAndScheduled(Prospection prospection,boolean scheduled);
	
	List<ProspectionDetail> findByProspectionStatusAndScheduledAndDateBetween(String prospectionStatus,boolean scheduled,Date  dateStart,Date dateFinish);
	List<ProspectionDetail> findByProspectionPersonAssessorAndProspectionStatusAndScheduledAndDateBetween(Person personAssessor,String prospectionStatus,boolean scheduled,Date dateStart,Date dateFinish);
	List<ProspectionDetail> findByProspectionPersonSupervisorAndProspectionStatusAndScheduledAndDateBetween(Person personSupervisor,String prospectionStatus,boolean scheduled,Date dateStart,Date dateFinish);
	List<ProspectionDetail> findByScheduledAndProspectionProspectPerson(boolean scheduled,Person person);
	
	//ConsultaS para reporte
	List<ProspectionDetail> findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(
			boolean scheduled,Date  dateStart,Date dateFinish,String personSurnames,String assessorDni,String supervisorDni,String action,String originContact,String project);
}
