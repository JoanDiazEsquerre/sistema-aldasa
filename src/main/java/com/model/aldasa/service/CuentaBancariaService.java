package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Sucursal;

public interface CuentaBancariaService {
	
	Optional<CuentaBancaria> findById(Integer id);
	CuentaBancaria save(CuentaBancaria entity);
	void delete(CuentaBancaria entity);
	
	List<CuentaBancaria> findByEstado(boolean estado);
	List<CuentaBancaria> findByEstadoAndSucursal(boolean estado, Sucursal sucursal);


}
