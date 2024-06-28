package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.DetallePlanilla;
import com.model.aldasa.entity.Planilla;

public interface DetallePlanillaRepository extends JpaRepository<DetallePlanilla, Integer>{
	
	List<DetallePlanilla> findByEstadoAndPlanilla(boolean estado, Planilla planilla);
	
	

}
