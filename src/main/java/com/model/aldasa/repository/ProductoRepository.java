package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	
	Producto findByEstadoAndTipoProducto (boolean estado, String tipoProducto);
	List<Producto> findByEstado(boolean estado);

}
