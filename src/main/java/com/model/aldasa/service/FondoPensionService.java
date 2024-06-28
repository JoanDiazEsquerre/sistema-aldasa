package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.FondoPension;

public interface FondoPensionService {

	Optional<FondoPension> findById(Integer id);
	FondoPension save(FondoPension entity);
	void delete(FondoPension entity);
	
	List<FondoPension> findByEstado(boolean estado);
}
