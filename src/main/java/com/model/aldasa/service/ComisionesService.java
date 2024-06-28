package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Person;


public interface ComisionesService {
	
	Optional<Comisiones> findById(Integer id);
	Comisiones save(Comisiones entity);
	void delete(Comisiones entity);
	
	Comisiones findByEstadoAndComisionSupervisorAndPersonaAsesor(boolean estado, ComisionSupervisor comisionSupervisor, Person personaAsesor);
	
	List<Comisiones> findByEstadoAndComisionSupervisor(boolean estado, ComisionSupervisor comisionSupervisor);

	Page<Comisiones> findByEstadoAndComisionSupervisor(boolean estado, ComisionSupervisor comisionSupervisor, Pageable pageable);
	Page<Comisiones> findByEstadoAndComisionSupervisorConfiguracionComision(boolean estado, ConfiguracionComision conf, Pageable pageable);

}
