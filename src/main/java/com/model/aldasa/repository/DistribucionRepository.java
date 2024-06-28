package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Distribucion;
import com.model.aldasa.entity.Inventario;

public interface DistribucionRepository extends JpaRepository<Distribucion, Integer>{

	List<Distribucion> findByInventarioAndEstado(Inventario inventario, boolean estado);

}
