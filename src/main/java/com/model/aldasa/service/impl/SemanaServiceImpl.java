package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Semana;
import com.model.aldasa.repository.SemanaRepository;
import com.model.aldasa.service.SemanaService;

@Service("semanaService")
public class SemanaServiceImpl implements SemanaService{

	@Autowired
	private SemanaRepository semanaRepository;

	@Override
	public Optional<Semana> findById(Integer id) {
		// TODO Auto-generated method stub
		return semanaRepository.findById(id);
	}

	@Override
	public Semana save(Semana entity) {
		// TODO Auto-generated method stub
		return semanaRepository.save(entity);
	}

	@Override
	public void delete(Semana entity) {
		// TODO Auto-generated method stub
		semanaRepository.delete(entity);
	}

	@Override
	public List<Semana> findByFechaIniLessThanEqualOrderByFechaIniDesc(Date fechaIni) {
		// TODO Auto-generated method stub
		return semanaRepository.findByFechaIniLessThanEqualOrderByFechaIniDesc(fechaIni);
	}
}
