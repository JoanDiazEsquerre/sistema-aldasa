package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.Province;

public interface ProvinceRepository extends PagingAndSortingRepository<Province, Integer> {

	List<Province> findByDepartment(Department department);
}
