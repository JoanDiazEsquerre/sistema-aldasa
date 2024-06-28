package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.Province;

public interface ProvinceService {

	Optional<Province> findById(Integer id);
	Province save(Province entity);
	void delete(Province entity);
	List<Province> findByDepartment(Department department);
}
