package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.DetallePlanilla;
import com.model.aldasa.entity.Planilla;

public interface DetallePlanillaService {

	Optional<DetallePlanilla> findById(Integer id);
	DetallePlanilla save(DetallePlanilla entity);
	void delete(DetallePlanilla entity);
	
	List<DetallePlanilla> findByEstadoAndPlanilla(boolean estado, Planilla planilla);
	


}
