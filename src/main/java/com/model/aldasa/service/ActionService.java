package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Action;

public interface ActionService {
	
	Optional<Action> findById(Integer id);
	Action save(Action entity);
	void delete(Action entity);
	
	List<Action> findByStatus(boolean status);

}
