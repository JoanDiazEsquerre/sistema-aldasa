package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.DetalleCaja;

public interface DetalleCajaRepository extends JpaRepository<DetalleCaja, Integer> {

	List<DetalleCaja> findByCajaAndEstadoOrderByFechaDesc(Caja caja, boolean estado);
	List<DetalleCaja> findByCajaAndEstadoOrderByFechaAsc(Caja caja, boolean estado);

	Page<DetalleCaja> findByEstado(boolean estado, Pageable pageable);

}
