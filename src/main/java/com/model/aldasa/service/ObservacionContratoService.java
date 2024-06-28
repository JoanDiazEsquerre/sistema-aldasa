package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.ObservacionContrato;

public interface ObservacionContratoService {
	
	Optional<ObservacionContrato> findById(Integer id);
	ObservacionContrato save(ObservacionContrato entity);
	void delete(ObservacionContrato entity);

	List<ObservacionContrato> findByEstadoAndContratoOrderByFechaRegistroDesc(boolean estado, Contrato contrato);
	
	List<ObservacionContrato> findByEstadoAndFechaRegistroBetween(boolean estado, Date fechaIni, Date fechaFin);
}
