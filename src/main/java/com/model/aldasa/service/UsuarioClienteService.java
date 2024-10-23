package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.UsuarioCliente;

public interface UsuarioClienteService {
	
	Optional<UsuarioCliente> findById(Integer id);
	UsuarioCliente save(UsuarioCliente user);
	void delete(UsuarioCliente user);

	UsuarioCliente findByUsernameAndPasswordAndEstado(String username, String password, boolean estado);
	

}
