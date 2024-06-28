package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Banco;
import com.model.aldasa.repository.BancoRepository;
import com.model.aldasa.service.BancoService;

@Service("bancoService")
public class BancoServiceImpl implements BancoService {
	
	@Autowired
	private BancoRepository bancoRepository;

	@Override
	public Optional<Banco> findById(Integer id) {
		// TODO Auto-generated method stub
		return bancoRepository.findById(id);
	}

	@Override
	public Banco save(Banco entity) {
		// TODO Auto-generated method stub
		return bancoRepository.save(entity);
	}

	@Override
	public void delete(Banco entity) {
		// TODO Auto-generated method stub
		bancoRepository.delete(entity);
	}

	@Override
	public List<Banco> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return bancoRepository.findByEstado(estado);
	}


}
