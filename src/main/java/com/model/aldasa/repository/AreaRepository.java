package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Area;

public interface AreaRepository extends JpaRepository<Area, Integer>  {
	
	List<Area> findByEstadoOrderByNombreAsc(boolean estado);


}
