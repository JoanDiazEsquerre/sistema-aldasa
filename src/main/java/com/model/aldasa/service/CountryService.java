package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Country;

public interface CountryService {

	Optional<Country> findById(Integer id);
	Country save(Country entity);
	void delete(Country entity);
	List<Country> findAll();
}
