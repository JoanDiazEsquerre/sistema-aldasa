package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {
	
	List<Cargo> findByEstadoOrderByDescripcionAsc(boolean estado);

}
