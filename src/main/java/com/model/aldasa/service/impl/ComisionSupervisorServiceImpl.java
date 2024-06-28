package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.repository.ComisionProyectoRepository;
import com.model.aldasa.repository.ComisionSupervisorRepository;
import com.model.aldasa.service.ComisionProyectoService;
import com.model.aldasa.service.ComisionSupervisorService;

@Service("comisionSupervisorService")
public class ComisionSupervisorServiceImpl  implements ComisionSupervisorService {
	
	@Autowired
	private ComisionSupervisorRepository comisionSupervisorRepository;

	@Override
	public Optional<ComisionSupervisor> findById(Integer id) {
		// TODO Auto-generated method stub
		return comisionSupervisorRepository.findById(id);
	}

	@Override
	public ComisionSupervisor save(ComisionSupervisor entity) {
		// TODO Auto-generated method stub
		return comisionSupervisorRepository.save(entity);
	}

	@Override
	public void delete(ComisionSupervisor entity) {
		// TODO Auto-generated method stub
		comisionSupervisorRepository.delete(entity);
	}

	@Override
	public Page<ComisionSupervisor> findByEstadoAndConfiguracionComision(Boolean estado, ConfiguracionComision conf,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return comisionSupervisorRepository.findByEstadoAndConfiguracionComision(estado, conf, pageable); 
	}

	@Override
	public ComisionSupervisor findByEstadoAndConfiguracionComisionAndPersonaSupervisor(boolean estado,
			ConfiguracionComision conf, Person supervisor) {
		// TODO Auto-generated method stub
		return comisionSupervisorRepository.findByEstadoAndConfiguracionComisionAndPersonaSupervisor(estado, conf, supervisor); 
	}

	@Override
	public List<ComisionSupervisor> findByEstadoAndConfiguracionComision(Boolean estado, ConfiguracionComision conf) {
		// TODO Auto-generated method stub
		return comisionSupervisorRepository.findByEstadoAndConfiguracionComision(estado, conf);
	}


	

	

}
