package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.TipoMovimiento;
import com.model.aldasa.repository.TipoMovimientoRepository;
import com.model.aldasa.service.TipoMovimientoService;

@Service("tipoMovimientoService")
public class TipoMovimientoServiceImpl implements TipoMovimientoService  {

	@Autowired
	private TipoMovimientoRepository tipoMovimientoRepository;

	@Override
	public Optional<TipoMovimiento> findById(Integer id) {
		// TODO Auto-generated method stub
		return tipoMovimientoRepository.findById(id);
	}

	@Override
	public TipoMovimiento save(TipoMovimiento entity) {
		// TODO Auto-generated method stub
		return tipoMovimientoRepository.save(entity);
	}

	@Override
	public void delete(TipoMovimiento entity) {
		// TODO Auto-generated method stub
		tipoMovimientoRepository.delete(entity);
	}

	@Override
	public List<TipoMovimiento> findByEstadoAndTipoOrderByNombreAsc(boolean estado, String tipo) {
		// TODO Auto-generated method stub
		return tipoMovimientoRepository.findByEstadoAndTipoOrderByNombreAsc(estado, tipo); 
	}



}
