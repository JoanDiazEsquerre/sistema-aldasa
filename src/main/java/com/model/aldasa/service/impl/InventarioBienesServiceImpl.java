package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.InventarioBienes;
import com.model.aldasa.repository.InventarioBienesRepository;
import com.model.aldasa.service.InventarioBienesService;

@Service("inventarioBienesService")
public class InventarioBienesServiceImpl implements InventarioBienesService{

	@Autowired
	private InventarioBienesRepository inventarioBienesRepository;

	@Override
	public Optional<InventarioBienes> findById(Integer id) {
		// TODO Auto-generated method stub
		return inventarioBienesRepository.findById(id);
	}

	@Override
	public InventarioBienes save(InventarioBienes entity) {
		// TODO Auto-generated method stub
		return inventarioBienesRepository.save(entity);
	}

	@Override
	public void delete(InventarioBienes entity) {
		// TODO Auto-generated method stub
		inventarioBienesRepository.delete(entity);
	}

	@Override
	public Page<InventarioBienes> findByEstadoAndEmpresa(String estado, Empresa empresa, Pageable pageable) {
		// TODO Auto-generated method stub
		return inventarioBienesRepository.findByEstadoAndEmpresa(estado, empresa, pageable);
	}

	@Override
	public Page<InventarioBienes> findByEmpresa(Empresa empresa, Pageable pageable) {
		// TODO Auto-generated method stub
		return inventarioBienesRepository.findByEmpresa(empresa, pageable);
	}
}
