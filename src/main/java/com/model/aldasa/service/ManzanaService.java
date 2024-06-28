package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Manzana;

public interface ManzanaService {
 
	Optional<Manzana> findById(Integer id);
	Manzana save(Manzana manzana);
	void delete(Manzana manzana);
	List<Manzana> findByStatusOrderByNameAsc(boolean status);
	Manzana findByName (String name);
	Manzana findByNameException(String name, int idManzana);
	
	List<Manzana> findByProject(int idProject);
}
