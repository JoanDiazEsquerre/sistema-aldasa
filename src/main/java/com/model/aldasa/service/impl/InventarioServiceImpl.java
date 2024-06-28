package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Inventario;
import com.model.aldasa.repository.InventarioRepository;
import com.model.aldasa.service.InventarioService;

@Service("inventarioService")
public class InventarioServiceImpl implements InventarioService{
	
	@Autowired
	private InventarioRepository inventarioRepository;

	@Override
	public Optional<Inventario> findById(Integer id) {
		// TODO Auto-generated method stub
		return inventarioRepository.findById(id);
	}

	@Override
	public Inventario save(Inventario entity) {
		// TODO Auto-generated method stub
		return inventarioRepository.save(entity);
	}

	@Override
	public void delete(Inventario entity) {
		// TODO Auto-generated method stub
		inventarioRepository.delete(entity);
	}

	@Override
	public Page<Inventario> findByEstadoAndSucursalEmpresa(boolean estado, Empresa empresa, Pageable pageable) {
		// TODO Auto-generated method stub
		return inventarioRepository.findByEstadoAndSucursalEmpresa(estado, empresa, pageable);
	}

	

}
