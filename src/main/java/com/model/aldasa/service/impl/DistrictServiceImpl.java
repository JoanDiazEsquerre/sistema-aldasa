package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Province;
import com.model.aldasa.repository.DistrictRepository;
import com.model.aldasa.service.DistrictService;

@Service("districtService")
public class DistrictServiceImpl implements DistrictService {
	
	@Autowired
	private DistrictRepository districtRepository;

	@Override
	public Optional<District> findById(Integer id) {
		// TODO Auto-generated method stub
		return districtRepository.findById(id);
	}

	@Override
	public District save(District entity) {
		// TODO Auto-generated method stub
		return districtRepository.save(entity);
	}

	@Override
	public void delete(District entity) {
		// TODO Auto-generated method stub
		districtRepository.delete(entity); 
	}

	@Override
	public List<District> findByProvince(Province province) {
		// TODO Auto-generated method stub
		return districtRepository.findByProvince(province);
	}

}
