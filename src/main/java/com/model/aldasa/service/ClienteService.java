package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;

public interface ClienteService {
	
	Optional<Cliente> findById(Integer id);
	Cliente save(Cliente entity);
	void delete(Cliente entity);
	
	Cliente findByPersonAndEstado (Person person, boolean estado);
	Cliente findByPersonAndEstadoAndPersonaNatural (Person person, boolean estado, boolean personaNatural);
	Cliente findByRucAndEstado (String ruc, boolean estado);
	
	Cliente findByPersonIdAndEstadoAndPersonaNaturalExcepcion (int idPerson, boolean estado, boolean personaNatural, int idCliente);
	Cliente findByRucAndEstadoExcepcion (String ruc, boolean estado, int idCliente);

	
	List<Cliente> findByEstado (boolean estado);
	List<Cliente> findByEstadoAndPersonaNatural (boolean estado, boolean personaNatural);
	
	Page<Cliente> findByRazonSocialLikeAndNombreComercialLikeAndRucLikeAndDniLikeAndEstado(String razonSocial,String noombreComercial,String ruc,String dni, boolean status, Pageable pageable);



}
