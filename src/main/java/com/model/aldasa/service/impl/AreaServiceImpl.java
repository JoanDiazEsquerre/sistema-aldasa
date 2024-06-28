package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Area;
import com.model.aldasa.repository.AreaRepository;
import com.model.aldasa.service.AreaService;

@Service("areaService")
public class AreaServiceImpl implements AreaService  {

	@Autowired
	private AreaRepository areaRepository;

	@Override
	public Optional<Area> findById(Integer id) {
		// TODO Auto-generated method stub
		return areaRepository.findById(id);
	}

	@Override
	public Area save(Area entity) {
		// TODO Auto-generated method stub
		return areaRepository.save(entity);
	}

	@Override
	public void delete(Area entity) {
		// TODO Auto-generated method stub
		areaRepository.delete(entity);
	}

	@Override
	public List<Area> findByEstadoOrderByNombreAsc(boolean estado) {
		// TODO Auto-generated method stub
		return areaRepository.findByEstadoOrderByNombreAsc(estado);
	}

	

}
