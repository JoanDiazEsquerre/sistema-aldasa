package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.SubDiario;

public interface SubDiarioService {
	
	Optional<SubDiario> findById(Integer id);
	SubDiario save(SubDiario entity);
	void delete(SubDiario entity);

}
