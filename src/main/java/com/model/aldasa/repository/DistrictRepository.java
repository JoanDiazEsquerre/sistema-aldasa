package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Province;

public interface DistrictRepository extends PagingAndSortingRepository<District, Integer> {

	List<District> findByProvince(Province province);
}
