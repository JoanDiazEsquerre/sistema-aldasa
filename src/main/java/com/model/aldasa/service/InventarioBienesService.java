package com.model.aldasa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.InventarioBienes;

public interface InventarioBienesService {
	
	Optional<InventarioBienes> findById(Integer id);
	InventarioBienes save(InventarioBienes entity);
	void delete(InventarioBienes entity);
	
	Page<InventarioBienes> findByEstadoAndEmpresa(String estado, Empresa empresa, Pageable pageable);
	Page<InventarioBienes> findByEmpresa(Empresa empresa, Pageable pageable);

}
