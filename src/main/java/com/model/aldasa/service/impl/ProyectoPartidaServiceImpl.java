package com.model.aldasa.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.ProyectoPartida;
import com.model.aldasa.repository.ProyectoPartidaRepository;
//import com.model.aldasa.repository.DetalleComisionesRepository;
import com.model.aldasa.service.ProyectoPartidaService;

@Service("proyectoPartidaService")
public class ProyectoPartidaServiceImpl implements ProyectoPartidaService{
	
	@Autowired
	private ProyectoPartidaRepository proyectoContratoRepository;

	@Override
	public Optional<ProyectoPartida> findById(Integer id) {
		// TODO Auto-generated method stub
		return proyectoContratoRepository.findById(id);
	}

	@Override
	public ProyectoPartida save(ProyectoPartida entity) {
		// TODO Auto-generated method stub
		return proyectoContratoRepository.save(entity);
	}

	@Override
	public void delete(ProyectoPartida entity) {
		// TODO Auto-generated method stub
		proyectoContratoRepository.delete(entity); 
	}

	@Override
	public List<ProyectoPartida> findByEstadoAndProyecto(boolean status, Project proyecto) {
		// TODO Auto-generated method stub
		return proyectoContratoRepository.findByEstadoAndProyecto(status, proyecto);
	}

	@Override
	public ProyectoPartida findByEstadoAndProyectoAndManzana(boolean status, Project proyecto, Manzana manzana) {
		// TODO Auto-generated method stub
		return proyectoContratoRepository.findByEstadoAndProyectoAndManzana(status, proyecto, manzana);
	}
	



	


}
