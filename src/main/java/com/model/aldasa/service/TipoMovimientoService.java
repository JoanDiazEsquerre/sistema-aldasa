package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.TipoMovimiento;

public interface TipoMovimientoService {
	
	Optional<TipoMovimiento> findById(Integer id);
	TipoMovimiento save(TipoMovimiento entity);
	void delete(TipoMovimiento entity);
	
	List<TipoMovimiento> findByEstadoAndTipoOrderByNombreAsc(boolean estado, String tipo);

}
