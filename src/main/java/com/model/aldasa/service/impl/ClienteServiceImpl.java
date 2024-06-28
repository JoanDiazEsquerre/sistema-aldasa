package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Person;
import com.model.aldasa.repository.ClienteRepository;
import com.model.aldasa.service.ClienteService;

@Service("clienteService")
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public Optional<Cliente> findById(Integer id) {
		// TODO Auto-generated method stub
		return clienteRepository.findById(id);
	}

	@Override
	public Cliente save(Cliente entity) {
		// TODO Auto-generated method stub
		return clienteRepository.save(entity);
	}

	@Override
	public void delete(Cliente entity) {
		// TODO Auto-generated method stub
		clienteRepository.delete(entity);
	}

	@Override
	public Cliente findByPersonAndEstado(Person person, boolean estado) {
		// TODO Auto-generated method stub
		return clienteRepository.findByPersonAndEstado(person, estado);
	}

	@Override
	public Cliente findByPersonAndEstadoAndPersonaNatural(Person person, boolean estado, boolean personaNatural) {
		// TODO Auto-generated method stub
		return clienteRepository.findByPersonAndEstadoAndPersonaNatural(person, estado, personaNatural);
	}

	@Override
	public List<Cliente> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return clienteRepository.findByEstado(estado);
	}

	@Override
	public Page<Cliente> findByRazonSocialLikeAndNombreComercialLikeAndRucLikeAndDniLikeAndEstado(String razonSocial,
			String noombreComercial, String ruc, String dni, boolean status, Pageable pageable) {
		// TODO Auto-generated method stub
		return clienteRepository.findByRazonSocialLikeAndNombreComercialLikeAndRucLikeAndDniLikeAndEstado(razonSocial, noombreComercial, ruc, dni, status, pageable);
	}

	@Override
	public Cliente findByRucAndEstado(String ruc, boolean estado) {
		// TODO Auto-generated method stub
		return clienteRepository.findByRucAndEstado(ruc, estado);
	}

	@Override
	public List<Cliente> findByEstadoAndPersonaNatural(boolean estado, boolean personaNatural) {
		// TODO Auto-generated method stub
		return clienteRepository.findByEstadoAndPersonaNatural(estado, personaNatural);
	}

	@Override
	public Cliente findByPersonIdAndEstadoAndPersonaNaturalExcepcion(int idPerson, boolean estado,
			boolean personaNatural, int idCliente) {
		// TODO Auto-generated method stub
		return clienteRepository.findByPersonIdAndEstadoAndPersonaNaturalExcepcion(idPerson, estado, personaNatural, idCliente);
	}

	@Override
	public Cliente findByRucAndEstadoExcepcion(String ruc, boolean estado, int idCliente) {
		// TODO Auto-generated method stub
		return clienteRepository.findByRucAndEstadoExcepcion(ruc, estado, idCliente);
	}




	
}
