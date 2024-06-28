package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Planilla;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Voucher;


public interface PlanillaRepository extends JpaRepository<Planilla, Integer>{
	
	Planilla findByEstadoAndTemporal(boolean estado, boolean temporal);
	
	Page<Planilla> findByEstadoAndTemporal(boolean estado, boolean temporal, Pageable pageable);
	
	

}
