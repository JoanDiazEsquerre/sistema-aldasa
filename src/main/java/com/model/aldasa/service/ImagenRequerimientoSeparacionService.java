package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.ImagenRequerimientoSeparacion;
import com.model.aldasa.entity.RequerimientoSeparacion;

public interface ImagenRequerimientoSeparacionService {

	Optional<ImagenRequerimientoSeparacion> findById(Integer id);
	ImagenRequerimientoSeparacion save(ImagenRequerimientoSeparacion entity);
	void delete(ImagenRequerimientoSeparacion entity);
	
	List<ImagenRequerimientoSeparacion> findByRequerimientoSeparacionAndEstado(RequerimientoSeparacion requerimientoSeparacion, boolean estado);

}
