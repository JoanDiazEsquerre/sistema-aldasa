package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Person;

public interface ComisionSupervisorService {
	
	Optional<ComisionSupervisor> findById(Integer id);
	ComisionSupervisor save(ComisionSupervisor entity);
	void delete(ComisionSupervisor entity);
	
	ComisionSupervisor findByEstadoAndConfiguracionComisionAndPersonaSupervisor(boolean estado, ConfiguracionComision conf, Person supervisor);

	Page<ComisionSupervisor> findByEstadoAndConfiguracionComision(Boolean estado, ConfiguracionComision conf, Pageable pageable);

	List<ComisionSupervisor> findByEstadoAndConfiguracionComision(Boolean estado, ConfiguracionComision conf);

}
