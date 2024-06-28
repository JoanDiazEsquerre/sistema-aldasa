package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.MovimientoBancario;

public interface MovimientoBancarioService {

	Optional<MovimientoBancario> findById(Integer id);
	MovimientoBancario save(MovimientoBancario entity);
	void delete(MovimientoBancario entity);
	
	Page<MovimientoBancario> findByEstadoAndMesLikeAndAnioLike(boolean estado, String mes, String anio, Pageable pageable);
}
