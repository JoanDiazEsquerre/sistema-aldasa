package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.ObservacionContrato;
import com.model.aldasa.repository.ObservacionContratoRepository;
import com.model.aldasa.service.ObservacionContratoService;

@Service("observacionContratoService")
public class ObservacionContratoServiceImpl implements ObservacionContratoService{

	@Autowired
	private ObservacionContratoRepository observacionContratoRepository;

	@Override
	public Optional<ObservacionContrato> findById(Integer id) {
		// TODO Auto-generated method stub
		return observacionContratoRepository.findById(id);
	}

	@Override
	public ObservacionContrato save(ObservacionContrato entity) {
		// TODO Auto-generated method stub
		return observacionContratoRepository.save(entity);
	}

	@Override
	public void delete(ObservacionContrato entity) {
		// TODO Auto-generated method stub
		observacionContratoRepository.delete(entity);
	}

	@Override
	public List<ObservacionContrato> findByEstadoAndContratoOrderByFechaRegistroDesc(boolean estado, Contrato contrato) {
		// TODO Auto-generated method stub
		return observacionContratoRepository.findByEstadoAndContratoOrderByFechaRegistroDesc(estado, contrato);
	}

	@Override
	public List<ObservacionContrato> findByEstadoAndFechaRegistroBetween(boolean estado, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return observacionContratoRepository.findByEstadoAndFechaRegistroBetween(estado, fechaIni, fechaFin);
	}
}
