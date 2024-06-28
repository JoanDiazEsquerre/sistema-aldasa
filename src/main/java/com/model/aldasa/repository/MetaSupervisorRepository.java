package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;

public interface MetaSupervisorRepository  extends JpaRepository<MetaSupervisor, Integer> {
	
	List<MetaSupervisor> findByConfiguracionComisionAndEstado(ConfiguracionComision comision ,boolean estado);
	MetaSupervisor findByConfiguracionComisionAndEstadoAndPersonSupervisor(ConfiguracionComision comision ,boolean estado, Person supervisor);

}
