package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;

public interface DepartmentRepository extends PagingAndSortingRepository<Department, Integer>{

	List<Department> findByCountry(Country country);
}
