package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.ProyectoPartida;



public interface ProyectoPartidaRepository extends PagingAndSortingRepository<ProyectoPartida, Integer> {
	
	ProyectoPartida findByEstadoAndProyectoAndManzana(boolean status, Project proyecto, Manzana manzana);
	
	List<ProyectoPartida> findByEstadoAndProyecto(boolean status, Project proyecto);
}
