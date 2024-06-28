package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.PlanContable;

public interface PlanContableService {

	Optional<PlanContable> findById(Integer id);
	PlanContable save(PlanContable entity);
	void delete(PlanContable entity);
}
