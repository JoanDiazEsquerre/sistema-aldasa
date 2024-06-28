package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.MovimientoBancario;


public interface MovimientoBancarioRepository extends JpaRepository<MovimientoBancario, Integer> {
	
	Page<MovimientoBancario> findByEstadoAndMesLikeAndAnioLike(boolean estado, String mes, String anio, Pageable pageable);

}
