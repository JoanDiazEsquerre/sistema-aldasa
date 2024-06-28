package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Province;

public interface DistrictService {

	Optional<District> findById(Integer id);
	District save(District entity);
	void delete(District entity);
	List<District> findByProvince(Province province);
}
