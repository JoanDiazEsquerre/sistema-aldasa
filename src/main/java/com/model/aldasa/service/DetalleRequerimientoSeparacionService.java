package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.DetalleRequerimientoSeparacion;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Sucursal;

public interface DetalleRequerimientoSeparacionService {
	
	Optional<DetalleRequerimientoSeparacion> findById(Integer id);
	DetalleRequerimientoSeparacion save(DetalleRequerimientoSeparacion entity);
	void delete(DetalleRequerimientoSeparacion entity);
	
	List<DetalleRequerimientoSeparacion> findByEstadoAndRequerimientoSeparacion(boolean estado, RequerimientoSeparacion requerimientoSeparacion);
	
	Page<DetalleRequerimientoSeparacion> findByEstadoAndRequerimientoSeparacionLoteProjectSucursalAndBoleteoTotal(boolean estado, Sucursal sucursal, String boleteoTotal, Pageable pageable);
	
}
