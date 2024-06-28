package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Action;
import com.model.aldasa.repository.ActionRepository;
import com.model.aldasa.service.ActionService;

@Service("actionService")
public class ActionServiceImpl implements ActionService {
	
	@Autowired
	private ActionRepository actionRepository;

	@Override
	public Optional<Action> findById(Integer id) {
		return actionRepository.findById(id);
	}

	@Override
	public Action save(Action entity) {
		return actionRepository.save(entity);
	}

	@Override
	public void delete(Action entity) {
		actionRepository.delete(entity);
		
	}

	@Override
	public List<Action> findByStatus(boolean status) {
		return actionRepository.findByStatus(status);
	}

}
