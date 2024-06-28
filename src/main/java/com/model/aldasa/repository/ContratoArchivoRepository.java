package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.ContratoArchivo;

public interface ContratoArchivoRepository extends PagingAndSortingRepository<ContratoArchivo, Integer> {
	
	ContratoArchivo findByEstadoAndNombre(boolean estado, String  nombre);
	
	List<ContratoArchivo> findByEstadoAndContrato(boolean estado, Contrato contrato);

}
