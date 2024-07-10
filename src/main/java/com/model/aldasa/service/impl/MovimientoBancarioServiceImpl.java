package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.MovimientoBancario;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.MovimientoBancarioRepository;
import com.model.aldasa.service.MovimientoBancarioService;

@Service("movimientoBancarioService")
public class MovimientoBancarioServiceImpl implements MovimientoBancarioService {
	
	@Autowired
	private MovimientoBancarioRepository movimientoBancarioRepository;

	@Override
	public Optional<MovimientoBancario> findById(Integer id) {
		// TODO Auto-generated method stub
		return movimientoBancarioRepository.findById(id);
	}

	@Override
	public MovimientoBancario save(MovimientoBancario entity) {
		// TODO Auto-generated method stub
		return movimientoBancarioRepository.save(entity);
	}

	@Override
	public void delete(MovimientoBancario entity) {
		// TODO Auto-generated method stub
		movimientoBancarioRepository.delete(entity);
	}

	@Override
	public Page<MovimientoBancario> findByEstadoAndMesLikeAndAnioLikeAndCuentaBancariaNumeroLikeAndCuentaBancariaSucursal(
			boolean estado, String mes, String anio, String cuentaBanc, Sucursal sucursal, Pageable pageable) {
		// TODO Auto-generated method stub
		return movimientoBancarioRepository.findByEstadoAndMesLikeAndAnioLikeAndCuentaBancariaNumeroLikeAndCuentaBancariaSucursal(estado, mes, anio, cuentaBanc, sucursal, pageable);
	}


	


}
