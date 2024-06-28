package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.repository.DepartmentRepository;
import com.model.aldasa.service.DepartmentService;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService{

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Override
	public Optional<Department> findById(Integer id) {
		// TODO Auto-generated method stub
		return departmentRepository.findById(id);
	}

	@Override
	public Department save(Department entity) {
		// TODO Auto-generated method stub
		return departmentRepository.save(entity);
	}

	@Override
	public void delete(Department entity) {
		// TODO Auto-generated method stub
		departmentRepository.delete(entity); 
	}

	@Override
	public List<Department> findByCountry(Country country) {
		// TODO Auto-generated method stub
		return departmentRepository.findByCountry(country);
	}

	

}
