package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Semana;

public interface SemanaRepository extends PagingAndSortingRepository<Semana, Integer>{
	
	List<Semana> findByFechaIniLessThanEqualOrderByFechaIniDesc(Date fechaIni);


}
