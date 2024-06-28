package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.repository.DetalleComisionesRepository;
import com.model.aldasa.service.DetalleComisionesService;



@Service("detalleComisionesService")
public class DetalleComisionesServiceImpl  implements DetalleComisionesService {
	
	@Autowired
	private DetalleComisionesRepository detalleComisionesRepository;

	@Override
	public Optional<DetalleComisiones> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleComisionesRepository.findById(id);
	}

	@Override
	public DetalleComisiones save(DetalleComisiones entity) {
		// TODO Auto-generated method stub
		return detalleComisionesRepository.save(entity);
	}

	@Override
	public void delete(DetalleComisiones entity) {
		// TODO Auto-generated method stub
		detalleComisionesRepository.delete(entity);
	}

	@Override
	public List<DetalleComisiones> findByEstadoAndComisiones(boolean estado, Comisiones comisiones) {
		// TODO Auto-generated method stub
		return detalleComisionesRepository.findByEstadoAndComisiones(estado, comisiones); 
	}

	@Override
	public List<DetalleComisiones> findByEstadoAndComisionesAndPlantillaVentaTipoPago(boolean estado,
			Comisiones comisiones, String tipoPago) {
		// TODO Auto-generated method stub
		return detalleComisionesRepository.findByEstadoAndComisionesAndPlantillaVentaTipoPago(estado, comisiones, tipoPago); 
	}

	@Override
	public List<DetalleComisiones> findByEstadoAndPlantillaVenta(boolean estado, PlantillaVenta plantillaVenta) {
		// TODO Auto-generated method stub
		return detalleComisionesRepository.findByEstadoAndPlantillaVenta(estado, plantillaVenta);
	} 

}
