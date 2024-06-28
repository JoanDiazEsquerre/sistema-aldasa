package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.ProyectoPartida;



public interface ProyectoPartidaService {

	Optional<ProyectoPartida> findById(Integer id);
	ProyectoPartida save(ProyectoPartida entity);
	void delete(ProyectoPartida entity);
	
	ProyectoPartida findByEstadoAndProyectoAndManzana(boolean status, Project proyecto, Manzana manzana);
	
	List<ProyectoPartida> findByEstadoAndProyecto(boolean status, Project proyecto);



}
