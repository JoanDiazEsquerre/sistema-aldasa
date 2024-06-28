package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Person;

public interface ComisionSupervisorRepository  extends JpaRepository<ComisionSupervisor, Integer>{
	
	ComisionSupervisor findByEstadoAndConfiguracionComisionAndPersonaSupervisor(boolean estado, ConfiguracionComision conf, Person supervisor);
	
	Page<ComisionSupervisor> findByEstadoAndConfiguracionComision(Boolean estado, ConfiguracionComision conf, Pageable pageable);

	List<ComisionSupervisor> findByEstadoAndConfiguracionComision(Boolean estado, ConfiguracionComision conf);
}
