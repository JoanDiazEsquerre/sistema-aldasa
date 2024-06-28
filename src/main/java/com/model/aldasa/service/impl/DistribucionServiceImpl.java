package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Distribucion;
import com.model.aldasa.entity.Inventario;
import com.model.aldasa.repository.DistribucionRepository;
import com.model.aldasa.service.DistribucionService;

@Service("distribucionService")
public class DistribucionServiceImpl  implements DistribucionService{

	
	@Autowired
	private DistribucionRepository distribucionRepository;

	@Override
	public Optional<Distribucion> findById(Integer id) {
		// TODO Auto-generated method stub
		return distribucionRepository.findById(id);
	}

	@Override
	public Distribucion save(Distribucion entity) {
		// TODO Auto-generated method stub
		return distribucionRepository.save(entity);
	}

	@Override
	public void delete(Distribucion entity) {
		// TODO Auto-generated method stub
		distribucionRepository.delete(entity);
	}

	@Override
	public List<Distribucion> findByInventarioAndEstado(Inventario inventario, boolean estado) {
		// TODO Auto-generated method stub
		return distribucionRepository.findByInventarioAndEstado(inventario, estado);
	}

}
