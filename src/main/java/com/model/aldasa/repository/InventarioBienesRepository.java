package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.InventarioBienes;

public interface InventarioBienesRepository extends JpaRepository<InventarioBienes, Integer>{

	Page<InventarioBienes> findByEstadoAndEmpresa(String estado, Empresa empresa, Pageable pageable);
	Page<InventarioBienes> findByEmpresa(Empresa empresa, Pageable pageable);

}
