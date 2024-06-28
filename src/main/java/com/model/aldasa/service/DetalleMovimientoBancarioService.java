package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.DetalleMovimientoBancario;
import com.model.aldasa.entity.MovimientoBancario;

public interface DetalleMovimientoBancarioService {

	Optional<DetalleMovimientoBancario> findById(Integer id);
	DetalleMovimientoBancario save(DetalleMovimientoBancario entity);
	void delete(DetalleMovimientoBancario entity);
	
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancario(boolean estado, MovimientoBancario movimientoBancario, Pageable pageable);
	
	BigDecimal totalImporteMovimiento(int idMovimientoBancario);
}
