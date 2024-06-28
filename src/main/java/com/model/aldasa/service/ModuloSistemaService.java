package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.ModuloSistema;


public interface ModuloSistemaService {
	
	Optional<ModuloSistema> findById(Integer id);
	ModuloSistema save(ModuloSistema entity);
	void delete(ModuloSistema entity);
	List<ModuloSistema> findByEstadoOrderByNombreAsc(boolean estado);
	

}
