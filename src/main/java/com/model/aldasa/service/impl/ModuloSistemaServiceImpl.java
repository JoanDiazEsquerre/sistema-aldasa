package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ModuloSistema;
import com.model.aldasa.repository.ModuloSistemaRepository;
import com.model.aldasa.service.ModuloSistemaService;



@Service("moduloSistemaService")
public class ModuloSistemaServiceImpl implements ModuloSistemaService {
	
	@Autowired
	private ModuloSistemaRepository moduloSistemaRepository;

	@Override
	public Optional<ModuloSistema> findById(Integer id) {
		// TODO Auto-generated method stub
		return moduloSistemaRepository.findById(id);
	}

	@Override
	public ModuloSistema save(ModuloSistema entity) {
		// TODO Auto-generated method stub
		return moduloSistemaRepository.save(entity);
	}

	@Override
	public void delete(ModuloSistema entity) {
		// TODO Auto-generated method stub
		moduloSistemaRepository.delete(entity);
	}

	@Override
	public List<ModuloSistema> findByEstadoOrderByNombreAsc(boolean estado) {
		// TODO Auto-generated method stub
		return moduloSistemaRepository.findByEstadoOrderByNombreAsc(estado); 
	}

	

}
