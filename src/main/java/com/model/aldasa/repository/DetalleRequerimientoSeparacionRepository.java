package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.DetalleRequerimientoSeparacion;
import com.model.aldasa.entity.Sucursal;

public interface DetalleRequerimientoSeparacionRepository extends JpaRepository<DetalleRequerimientoSeparacion, Integer> {
	
	List<DetalleRequerimientoSeparacion> findByEstadoAndRequerimientoSeparacion(boolean estado, RequerimientoSeparacion requerimientoSeparacion);
	
	Page<DetalleRequerimientoSeparacion> findByEstadoAndRequerimientoSeparacionLoteProjectSucursalAndBoleteoTotal(boolean estado, Sucursal sucursal, String boleteoTotal, Pageable pageable);
	
}
