package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.ObservacionContrato;

public interface ObservacionContratoRepository extends JpaRepository<ObservacionContrato, Integer> {

	List<ObservacionContrato> findByEstadoAndContratoOrderByFechaRegistroDesc(boolean estado, Contrato contrato);
	List<ObservacionContrato> findByEstadoAndFechaRegistroBetween(boolean estado, Date fechaIni, Date fechaFin);
}
