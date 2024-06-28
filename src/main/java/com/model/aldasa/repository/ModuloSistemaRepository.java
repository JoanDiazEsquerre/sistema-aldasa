package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.ModuloSistema;

public interface ModuloSistemaRepository extends PagingAndSortingRepository<ModuloSistema, Integer> {
	
	List<ModuloSistema> findByEstadoOrderByNombreAsc(boolean estado);

}
