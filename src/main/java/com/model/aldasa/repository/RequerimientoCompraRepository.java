package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.RequerimientoCompra;

public interface RequerimientoCompraRepository extends JpaRepository<RequerimientoCompra, Integer> {

	Page<RequerimientoCompra> findByEstado(String estado, Pageable pageable);

}
