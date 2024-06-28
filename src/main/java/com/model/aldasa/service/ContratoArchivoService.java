package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.ContratoArchivo;




public interface ContratoArchivoService {

	Optional<ContratoArchivo> findById(Integer id);
	ContratoArchivo save(ContratoArchivo entity);
	void delete(ContratoArchivo entity);
	
	ContratoArchivo findByEstadoAndNombre(boolean estado, String  nombre);
		
	List<ContratoArchivo> findByEstadoAndContrato(boolean estado, Contrato contrato);

}
