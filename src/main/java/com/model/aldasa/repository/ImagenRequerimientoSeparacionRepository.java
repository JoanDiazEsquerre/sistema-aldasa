package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.ImagenRequerimientoSeparacion;
import com.model.aldasa.entity.RequerimientoSeparacion;

public interface ImagenRequerimientoSeparacionRepository extends PagingAndSortingRepository<ImagenRequerimientoSeparacion, Integer>{

	List<ImagenRequerimientoSeparacion> findByRequerimientoSeparacionAndEstado(RequerimientoSeparacion requerimiento, boolean estado);

}
