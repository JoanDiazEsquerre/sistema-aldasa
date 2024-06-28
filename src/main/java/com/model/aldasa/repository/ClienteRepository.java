package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Person;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	Cliente findByPersonAndEstado (Person person, boolean estado);
	Cliente findByPersonAndEstadoAndPersonaNatural (Person person, boolean estado, boolean personaNatural);
	Cliente findByRucAndEstado (String ruc, boolean estado);
	
	@Query(nativeQuery = true,value = "SELECT * FROM cliente WHERE idPerson=:idPerson AND estado=:estado AND personaNatural=:personaNatural AND id <> :idCliente")
	Cliente findByPersonIdAndEstadoAndPersonaNaturalExcepcion (int idPerson, boolean estado, boolean personaNatural, int idCliente);
	
	@Query(nativeQuery = true,value = "SELECT * FROM cliente WHERE ruc=:ruc AND estado=:estado AND id <> :idCliente ")
	Cliente findByRucAndEstadoExcepcion (String ruc, boolean estado, int idCliente);

	List<Cliente> findByEstado (boolean estado);
	List<Cliente> findByEstadoAndPersonaNatural (boolean estado, boolean personaNatural);
	
	Page<Cliente> findByRazonSocialLikeAndNombreComercialLikeAndRucLikeAndDniLikeAndEstado(String razonSocial,String noombreComercial,String ruc, String dni, boolean status, Pageable pageable);




}
