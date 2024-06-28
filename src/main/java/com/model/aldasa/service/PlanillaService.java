package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetallePlanilla;
import com.model.aldasa.entity.Planilla;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Voucher;

public interface PlanillaService {

	Optional<Planilla> findById(Integer id);
	Planilla save(Planilla entity);
	Planilla save(Planilla entity, List<DetallePlanilla> lstDetalle);
	void delete(Planilla entity);
	
	Planilla findByEstadoAndTemporal(boolean estado, boolean temporal);
	
	Page<Planilla> findByEstadoAndTemporal(boolean estado, boolean temporal, Pageable pageable);


}
