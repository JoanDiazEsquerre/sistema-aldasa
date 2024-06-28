package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Unidad;
import com.model.aldasa.repository.UnidadRepository;
import com.model.aldasa.service.UnidadService;

@Service("unidadService")
public class UnidadServiceImpl implements UnidadService {

	@Autowired
	private UnidadRepository unidadRepository;

	@Override
	public Optional<Unidad> findById(Integer id) {
		// TODO Auto-generated method stub
		return unidadRepository.findById(id);
	}

	@Override
	public Unidad save(Unidad entity) {
		// TODO Auto-generated method stub
		return unidadRepository.save(entity);
	}

	@Override
	public void delete(Unidad entity) {
		// TODO Auto-generated method stub
		unidadRepository.delete(entity);
	}

	@Override
	public List<Unidad> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return unidadRepository.findByEstado(estado);
	}
}
