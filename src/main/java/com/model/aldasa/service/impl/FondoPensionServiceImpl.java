package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.FondoPension;
import com.model.aldasa.repository.FondoPensionRepository;
import com.model.aldasa.service.FondoPensionService;

@Service("fondoPensionService")
public class FondoPensionServiceImpl implements FondoPensionService {

	@Autowired
	private FondoPensionRepository fondoPensionRepository;

	@Override
	public Optional<FondoPension> findById(Integer id) {
		// TODO Auto-generated method stub
		return fondoPensionRepository.findById(id);
	}

	@Override
	public FondoPension save(FondoPension entity) {
		// TODO Auto-generated method stub
		return fondoPensionRepository.save(entity);
	}

	@Override
	public void delete(FondoPension entity) {
		// TODO Auto-generated method stub
		fondoPensionRepository.delete(entity);
	}

	@Override
	public List<FondoPension> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return fondoPensionRepository.findByEstado(estado);
	}
}
