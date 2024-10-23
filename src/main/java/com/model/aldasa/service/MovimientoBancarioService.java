package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.MovimientoBancario;
import com.model.aldasa.entity.Sucursal;

public interface MovimientoBancarioService {

	Optional<MovimientoBancario> findById(Integer id);
	MovimientoBancario save(MovimientoBancario entity);
	void delete(MovimientoBancario entity);
	
	MovimientoBancario findByEstadoAndMesAndAnioAndCuentaBancaria(boolean estado, String mes, String anio, CuentaBancaria ctaBancBusquedaMov);
	
	Page<MovimientoBancario> findByEstadoAndMesLikeAndAnioLikeAndCuentaBancariaNumeroLikeAndCuentaBancariaSucursal(boolean estado, String mes, String anio, String cuentaBanc,Sucursal sucursal, Pageable pageable); 
}
