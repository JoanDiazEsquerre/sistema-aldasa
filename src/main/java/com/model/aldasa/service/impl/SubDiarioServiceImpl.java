package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.SubDiario;
import com.model.aldasa.repository.SubDiarioRepository;
import com.model.aldasa.service.SubDiarioService;

@Service("subDiarioService")
public class SubDiarioServiceImpl implements SubDiarioService {

	@Autowired
	private SubDiarioRepository subDiarioRepository;

	@Override
	public Optional<SubDiario> findById(Integer id) {
		// TODO Auto-generated method stub
		return subDiarioRepository.findById(id);
	}

	@Override
	public SubDiario save(SubDiario entity) {
		// TODO Auto-generated method stub
		return subDiarioRepository.save(entity);
	}

	@Override
	public void delete(SubDiario entity) {
		// TODO Auto-generated method stub
		subDiarioRepository.delete(entity);
	}

}
