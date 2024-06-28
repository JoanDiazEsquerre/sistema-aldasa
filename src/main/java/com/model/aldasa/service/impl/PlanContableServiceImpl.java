package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.PlanContable;
import com.model.aldasa.repository.PlanContableRepository;
import com.model.aldasa.service.PlanContableService;

@Service("planContableService")
public class PlanContableServiceImpl implements PlanContableService{

	@Autowired
	private  PlanContableRepository planContableRepository;

	@Override
	public Optional<PlanContable> findById(Integer id) {
		// TODO Auto-generated method stub
		return planContableRepository.findById(id);
	}

	@Override
	public PlanContable save(PlanContable entity) {
		// TODO Auto-generated method stub
		return planContableRepository.save(entity);
	}

	@Override
	public void delete(PlanContable entity) {
		// TODO Auto-generated method stub
		planContableRepository.delete(entity);
	}

}
