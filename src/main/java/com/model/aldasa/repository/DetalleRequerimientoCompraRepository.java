package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DetalleRequerimientoCompra;
import com.model.aldasa.entity.RequerimientoCompra;

public interface DetalleRequerimientoCompraRepository extends JpaRepository<DetalleRequerimientoCompra, Integer> {
	
	List<DetalleRequerimientoCompra> findByRequerimientoCompraAndEstado(RequerimientoCompra requerimientoCompra, boolean estado);


}
