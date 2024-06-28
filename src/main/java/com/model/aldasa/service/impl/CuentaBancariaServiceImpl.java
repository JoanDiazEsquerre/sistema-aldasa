package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.CuentaBancariaRepository;
import com.model.aldasa.service.CuentaBancariaService;

@Service("cuentaBancariaService")
public class CuentaBancariaServiceImpl  implements CuentaBancariaService {

	@Autowired
	private CuentaBancariaRepository cuentaBancariaRepository;

	@Override
	public Optional<CuentaBancaria> findById(Integer id) {
		// TODO Auto-generated method stub
		return cuentaBancariaRepository.findById(id);
	}

	@Override
	public CuentaBancaria save(CuentaBancaria entity) {
		// TODO Auto-generated method stub
		return cuentaBancariaRepository.save(entity);
	}

	@Override
	public void delete(CuentaBancaria entity) {
		// TODO Auto-generated method stub
		cuentaBancariaRepository.delete(entity);
	}

	@Override
	public List<CuentaBancaria> findByEstado(boolean estado){
		// TODO Auto-generated method stub
		return cuentaBancariaRepository.findByEstado(estado);
	}

	@Override
	public List<CuentaBancaria> findByEstadoAndSucursal(boolean estado, Sucursal sucursal) {
		// TODO Auto-generated method stub
		return cuentaBancariaRepository.findByEstadoAndSucursal(estado, sucursal);
	}


}
