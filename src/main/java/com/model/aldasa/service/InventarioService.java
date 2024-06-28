package com.model.aldasa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Inventario;

public interface InventarioService {
	
	Optional<Inventario> findById(Integer id);
	Inventario save(Inventario entity);
	void delete(Inventario entity);
	
	Page<Inventario> findByEstadoAndSucursalEmpresa(boolean estado, Empresa empresa, Pageable pageable);


}
