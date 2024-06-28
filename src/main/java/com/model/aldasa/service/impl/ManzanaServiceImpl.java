package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Manzana;
import com.model.aldasa.repository.ManzanaRepository; 
import com.model.aldasa.service.ManzanaService;

@Service("manzanaService")
public class ManzanaServiceImpl implements ManzanaService {

	@Autowired
	private ManzanaRepository manzanaRepository;
	
	@Override
	public Manzana save(Manzana entity) {
		return manzanaRepository.save(entity);
	}
	
	@Override
	public void delete(Manzana entity) {
		manzanaRepository.delete(entity);
	}
	
	@Override
	public List<Manzana> findByStatusOrderByNameAsc(boolean status) {
		return manzanaRepository.findByStatusOrderByNameAsc(status);
	}
	
	@Override
	public Manzana findByName(String name) {
		return manzanaRepository.findByName(name);
	}
	
	@Override
	public  Manzana findByNameException(String name, int idManzana) {
		return manzanaRepository.findByNameException(name, idManzana);
	}


	@Override
	public Optional<Manzana> findById(Integer id) {
		return manzanaRepository.findById(id);
	}

	@Override
	public List<Manzana> findByProject(int idProject) {
		return manzanaRepository.findByProject(idProject);
	}
	
}
