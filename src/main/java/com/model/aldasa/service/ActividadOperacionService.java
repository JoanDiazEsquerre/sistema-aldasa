package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.ActividadOperacion;

public interface ActividadOperacionService {
	
	Optional<ActividadOperacion> findById(Integer id);
	ActividadOperacion save(ActividadOperacion entity);
	void delete(ActividadOperacion entity);

}
