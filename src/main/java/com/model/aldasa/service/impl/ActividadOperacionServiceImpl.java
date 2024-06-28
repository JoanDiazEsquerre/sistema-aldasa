package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ActividadOperacion;
import com.model.aldasa.repository.ActividadOperacionRepository;
import com.model.aldasa.service.ActividadOperacionService;

@Service("actividadOperacionService")
public class ActividadOperacionServiceImpl implements ActividadOperacionService{

	@Autowired
	private ActividadOperacionRepository actividadOperacionRepository;

	@Override
	public Optional<ActividadOperacion> findById(Integer id) {
		// TODO Auto-generated method stub
		return actividadOperacionRepository.findById(id);
	}

	@Override
	public ActividadOperacion save(ActividadOperacion entity) {
		// TODO Auto-generated method stub
		return actividadOperacionRepository.save(entity);
	}

	@Override
	public void delete(ActividadOperacion entity) {
		// TODO Auto-generated method stub
		actividadOperacionRepository.delete(entity);
	}
}
