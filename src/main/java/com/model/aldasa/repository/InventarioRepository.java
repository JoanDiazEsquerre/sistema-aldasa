package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer>{
	
	Page<Inventario> findByEstadoAndSucursalEmpresa(boolean estado, Empresa empresa, Pageable pageable);


}
