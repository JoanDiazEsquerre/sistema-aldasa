package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.TipoActividad;

public interface TipoActividadService {
	
	Optional<TipoActividad> findById(Integer id);
	TipoActividad save(TipoActividad entity);
	void delete(TipoActividad entity);

}
