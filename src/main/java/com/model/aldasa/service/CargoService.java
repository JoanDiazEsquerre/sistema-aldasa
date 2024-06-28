package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Cargo;


public interface CargoService {

	Optional<Cargo> findById(Integer id);
	Cargo save(Cargo entity);
	void delete(Cargo entity);
	
	List<Cargo> findByEstadoOrderByDescripcionAsc(boolean estado);
}
