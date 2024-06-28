package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Cargo;
import com.model.aldasa.repository.CargoRepository;
import com.model.aldasa.service.CargoService;


@Service("cargoService")
public class CargoServiceImpl implements CargoService {
	
	@Autowired
	private CargoRepository cargoRepository;

	@Override
	public Optional<Cargo> findById(Integer id) {
		// TODO Auto-generated method stub
		return cargoRepository.findById(id);
	}

	@Override
	public Cargo save(Cargo entity) {
		// TODO Auto-generated method stub
		return cargoRepository.save(entity);
	}

	@Override
	public void delete(Cargo entity) {
		// TODO Auto-generated method stub
		cargoRepository.delete(entity);
	}

	@Override
	public List<Cargo> findByEstadoOrderByDescripcionAsc(boolean estado) {
		// TODO Auto-generated method stub
		return cargoRepository.findByEstadoOrderByDescripcionAsc(estado);
	}


}
