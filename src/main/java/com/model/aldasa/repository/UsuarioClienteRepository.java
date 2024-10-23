package com.model.aldasa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.UsuarioCliente;

public interface UsuarioClienteRepository extends JpaRepository<UsuarioCliente, Integer>{
	
	UsuarioCliente findByUsernameAndPasswordAndEstado(String username, String password, boolean estado);
	
}
