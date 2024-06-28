package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Distribucion;
import com.model.aldasa.entity.Inventario;

public interface DistribucionService {
	
	Optional<Distribucion> findById(Integer id);
	Distribucion save( Distribucion entity);
	void delete( Distribucion entity);
	
	List<Distribucion> findByInventarioAndEstado(Inventario inventario, boolean estado);


}
