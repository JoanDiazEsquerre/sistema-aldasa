package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;

public interface MetaSupervisorService {
	
	Optional<MetaSupervisor> findById(Integer id);
	MetaSupervisor save(MetaSupervisor entity);
	void delete(MetaSupervisor entity);
	
	List<MetaSupervisor> findByConfiguracionComisionAndEstado(ConfiguracionComision comision ,boolean estado);
	MetaSupervisor findByConfiguracionComisionAndEstadoAndPersonSupervisor(ConfiguracionComision comision ,boolean estado, Person supervisor);

}
