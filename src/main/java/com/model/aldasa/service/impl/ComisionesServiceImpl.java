package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Person;
import com.model.aldasa.repository.ComisionesRepository;
import com.model.aldasa.service.ComisionesService;



@Service("comisionesService")
public class ComisionesServiceImpl  implements ComisionesService {
	
	@Autowired
	private ComisionesRepository comisionesRepository;

	@Override
	public Optional<Comisiones> findById(Integer id) {
		// TODO Auto-generated method stub
		return comisionesRepository.findById(id);
	}

	@Override
	public Comisiones save(Comisiones entity) {
		// TODO Auto-generated method stub
		return comisionesRepository.save(entity);
	}

	@Override
	public void delete(Comisiones entity) {
		// TODO Auto-generated method stub
		comisionesRepository.delete(entity);
	}

	@Override
	public Comisiones findByEstadoAndComisionSupervisorAndPersonaAsesor(boolean estado,
			ComisionSupervisor comisionSupervisor, Person personaAsesor) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByEstadoAndComisionSupervisorAndPersonaAsesor(estado, comisionSupervisor, personaAsesor);
	}

	@Override
	public List<Comisiones> findByEstadoAndComisionSupervisor(boolean estado, ComisionSupervisor comisionSupervisor) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByEstadoAndComisionSupervisor(estado, comisionSupervisor); 
	}

	@Override
	public Page<Comisiones> findByEstadoAndComisionSupervisor(boolean estado, ComisionSupervisor comisionSupervisor,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByEstadoAndComisionSupervisor(estado, comisionSupervisor, pageable); 
	}

	@Override
	public Page<Comisiones> findByEstadoAndComisionSupervisorConfiguracionComision(boolean estado,
			ConfiguracionComision conf, Pageable pageable) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByEstadoAndComisionSupervisorConfiguracionComision(estado, conf, pageable); 
	}

}
