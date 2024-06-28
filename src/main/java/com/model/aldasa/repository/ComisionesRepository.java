package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Person;



public interface ComisionesRepository  extends JpaRepository<Comisiones, Integer>{
	
	Comisiones findByEstadoAndComisionSupervisorAndPersonaAsesor(boolean estado, ComisionSupervisor comisionSupervisor, Person personaAsesor);

	List<Comisiones> findByEstadoAndComisionSupervisor(boolean estado, ComisionSupervisor comisionSupervisor);
	
	List<Comisiones> findByEstadoAndComisionSupervisorConfiguracionComision(boolean estado, ConfiguracionComision configuracionComision);

	Page<Comisiones> findByEstadoAndComisionSupervisor(boolean estado, ComisionSupervisor comisionSupervisor, Pageable pageable);
	Page<Comisiones> findByEstadoAndComisionSupervisorConfiguracionComision(boolean estado, ConfiguracionComision conf, Pageable pageable);

}
