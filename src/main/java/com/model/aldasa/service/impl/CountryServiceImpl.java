package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Country;
import com.model.aldasa.repository.CountryRepository;
import com.model.aldasa.service.CountryService;

@Service("countryService")
public class CountryServiceImpl implements CountryService{
	
	@Autowired
	private CountryRepository countryRepository;

	@Override
	public Optional<Country> findById(Integer id) {
		return countryRepository.findById(id);
	}

	@Override
	public Country save(Country entity) {
		return countryRepository.save(entity);
	}

	@Override
	public void delete(Country entity) {
		countryRepository.delete(entity); 
	}

	@Override
	public List<Country> findAll() {
		// TODO Auto-generated method stub
		return (List<Country>) countryRepository.findAll();
	}



}
