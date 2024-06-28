package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.TipoMovimiento;

public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Integer>  {
	
	List<TipoMovimiento> findByEstadoAndTipoOrderByNombreAsc(boolean estado, String tipo);


}
