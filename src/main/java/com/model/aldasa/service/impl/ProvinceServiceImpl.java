package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.Province;
import com.model.aldasa.repository.ProvinceRepository;
import com.model.aldasa.service.ProvinceService;

@Service("provinceService")
public class ProvinceServiceImpl implements ProvinceService {
	
	@Autowired
	private ProvinceRepository provinceRepository;

	@Override
	public Optional<Province> findById(Integer id) {
		// TODO Auto-generated method stub
		return provinceRepository.findById(id);
	}

	@Override
	public Province save(Province entity) {
		// TODO Auto-generated method stub
		return provinceRepository.save(entity);
	}

	@Override
	public void delete(Province entity) {
		// TODO Auto-generated method stub
		provinceRepository.delete(entity);
	}

	@Override
	public List<Province> findByDepartment(Department department) {
		// TODO Auto-generated method stub
		return provinceRepository.findByDepartment(department);
	}

}
