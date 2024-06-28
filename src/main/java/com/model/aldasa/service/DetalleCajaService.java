package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleCaja;
import com.model.aldasa.entity.RequerimientoCompra;

public interface DetalleCajaService {

	Optional<DetalleCaja> findById(Integer id);
	DetalleCaja save(DetalleCaja entity);
	void delete(DetalleCaja entity);
	
	List<DetalleCaja> findByCajaAndEstadoOrderByFechaDesc(Caja caja, boolean estado);
	List<DetalleCaja> findByCajaAndEstadoOrderByFechaAsc(Caja caja, boolean estado);

	Page<DetalleCaja> findByEstado(boolean estado, Pageable pageable);

}
