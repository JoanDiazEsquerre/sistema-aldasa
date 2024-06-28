package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Unidad;

public interface UnidadRepository extends JpaRepository<Unidad, Integer> {

	List<Unidad> findByEstado(boolean estado);

}
