package com.model.aldasa.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Country;

public interface CountryRepository extends PagingAndSortingRepository<Country, Integer> {

}
