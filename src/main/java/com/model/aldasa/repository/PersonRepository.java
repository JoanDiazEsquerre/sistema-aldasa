package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;

public interface PersonRepository extends PagingAndSortingRepository<Person, Integer>{

	List<Person> findByStatus(Boolean status);
	Page<Person> findAllByDniLikeAndSurnamesLikeAndStatus(String dni, String names, Boolean status, Pageable pageable);
	Person findByDni(String dni);
	
	@Query(nativeQuery = true,value = " SELECT * FROM person  WHERE dni =:dni and id<>:idUser")
    Person findByDniException(String dni, int idUser);
	
	@Query(nativeQuery = true,value = "SELECT DISTINCT p.* FROM lote l INNER JOIN person p on l.idPersonSupervisor=p.id WHERE l.status=:status AND l.fechaVendido BETWEEN :fechaIni AND :fechaFin")
	List<Person> getPersonSupervisor(String status, Date fechaIni, Date fechaFin);
	
//	esto es para obtener los supervisores con  excepcion de interno, externo y online.
	@Query(nativeQuery = true,value = "SELECT DISTINCT p.* FROM comisiones c INNER JOIN lote l on l.id=c.idLote INNER JOIN person p on p.id=l.idPersonSupervisor WHERE l.status='Vendido' AND l.fechaVendido BETWEEN :fechaIni AND :fechaFin AND c.tipoEmpleado is NULL AND c.estado=true;")
	List<Person> getPersonSupervisorCampo(Date fechaIni, Date fechaFin);
}
