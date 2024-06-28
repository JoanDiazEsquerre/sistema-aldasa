package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;

public interface AreaService {
	
	Optional<Area> findById(Integer id);
	Area save(Area entity);
	void delete(Area entity);
	
	List<Area> findByEstadoOrderByNombreAsc(boolean estado);


}
