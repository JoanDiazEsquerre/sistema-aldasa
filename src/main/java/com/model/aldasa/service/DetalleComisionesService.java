package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;


public interface DetalleComisionesService {
	
	Optional<DetalleComisiones> findById(Integer id);
	DetalleComisiones save(DetalleComisiones entity);
	void delete(DetalleComisiones entity);
	

	List<DetalleComisiones> findByEstadoAndComisiones(boolean estado, Comisiones comisiones);
	List<DetalleComisiones> findByEstadoAndComisionesAndPlantillaVentaTipoPago(boolean estado, Comisiones comisiones, String tipoPago); 
	List<DetalleComisiones> findByEstadoAndPlantillaVenta(boolean estado, PlantillaVenta plantillaVenta);

}
