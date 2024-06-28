package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Banco;


public interface BancoRepository extends JpaRepository<Banco, Integer> {
	
	List<Banco> findByEstado(boolean estado);

}
