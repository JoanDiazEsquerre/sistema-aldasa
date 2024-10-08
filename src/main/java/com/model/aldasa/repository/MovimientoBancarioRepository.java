package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.MovimientoBancario;
import com.model.aldasa.entity.Sucursal;


public interface MovimientoBancarioRepository extends JpaRepository<MovimientoBancario, Integer> {
	
	Page<MovimientoBancario> findByEstadoAndMesLikeAndAnioLikeAndCuentaBancariaNumeroLikeAndCuentaBancariaSucursal(boolean estado, String mes, String anio, String cuentaBanc,Sucursal sucursal, Pageable pageable);

}
