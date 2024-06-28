package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;

public interface DepartmentService {
	
	Optional<Department> findById(Integer id);
	Department save(Department entity);
	void delete(Department entity);
	List<Department> findByCountry(Country country);

}
