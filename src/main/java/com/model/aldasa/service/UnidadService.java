package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Unidad;

public interface UnidadService {

	Optional<Unidad> findById(Integer id);
	Unidad save(Unidad entity);
	void delete(Unidad entity);
	
	List<Unidad> findByEstado(boolean estado);
}
