package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.MovimientoBancario;
import com.model.aldasa.entity.Sucursal;


public interface MovimientoBancarioRepository extends JpaRepository<MovimientoBancario, Integer> {
	
	MovimientoBancario findByEstadoAndMesAndAnioAndCuentaBancaria(boolean estado, String mes, String anio, CuentaBancaria cuentaBanc);
	
	Page<MovimientoBancario> findByEstadoAndMesLikeAndAnioLikeAndCuentaBancariaNumeroLikeAndCuentaBancariaSucursal(boolean estado, String mes, String anio, String cuentaBanc,Sucursal sucursal, Pageable pageable);

}
