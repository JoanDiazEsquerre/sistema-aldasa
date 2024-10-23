package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.UsuarioCliente;
import com.model.aldasa.repository.UsuarioClienteRepository;
import com.model.aldasa.service.UsuarioClienteService;

@Service("usuarioClienteService")
public class UsuarioClienteServiceImpl implements UsuarioClienteService {
	
	@Autowired
	private UsuarioClienteRepository usuarioClienteRepository;

	@Override
	public Optional<UsuarioCliente> findById(Integer id) {
		// TODO Auto-generated method stub
		return usuarioClienteRepository.findById(id);
	}

	@Override
	public UsuarioCliente save(UsuarioCliente user) {
		// TODO Auto-generated method stub
		return usuarioClienteRepository.save(user);
	}

	@Override
	public void delete(UsuarioCliente user) {
		// TODO Auto-generated method stub
		usuarioClienteRepository.delete(user);
	}

	@Override
	public UsuarioCliente findByUsernameAndPasswordAndEstado(String username, String password, boolean estado) {
		// TODO Auto-generated method stub
		return usuarioClienteRepository.findByUsernameAndPasswordAndEstado(username, password, estado); 
	}
	
	
}
