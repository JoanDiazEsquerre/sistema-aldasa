package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.TipoActividad;
import com.model.aldasa.repository.TipoActividadRepository;
import com.model.aldasa.service.TipoActividadService;

@Service("tipoActividadService")
public class TipoActividadServiceImpl implements TipoActividadService{

	@Autowired
	private TipoActividadRepository tipoActividadRepository;

	@Override
	public Optional<TipoActividad> findById(Integer id) {
		// TODO Auto-generated method stub
		return tipoActividadRepository.findById(id);
	}

	@Override
	public TipoActividad save(TipoActividad entity) {
		// TODO Auto-generated method stub
		return tipoActividadRepository.save(entity);
	}

	@Override
	public void delete(TipoActividad entity) {
		// TODO Auto-generated method stub
		tipoActividadRepository.delete(entity);
	}
}
