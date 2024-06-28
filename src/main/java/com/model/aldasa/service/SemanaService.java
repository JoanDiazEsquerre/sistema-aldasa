package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Semana;

public interface SemanaService {
	
	Optional<Semana> findById(Integer id);
	Semana save(Semana entity);
	void delete(Semana entity);
	
	List<Semana> findByFechaIniLessThanEqualOrderByFechaIniDesc(Date fechaIni);
}
