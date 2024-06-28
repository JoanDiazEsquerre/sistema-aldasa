package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Person;

public interface PersonService {
	
	Optional<Person> findById(Integer id);
	Person save(Person entity);
	void delete(Person entity);
	List<Person> findByStatus(Boolean status);
	Person findByDni(String dni);
	Person findByDniException(String dni, int idUser);
	Page<Person> findAllByDniLikeAndSurnamesLikeAndStatus(String dni, String names, Boolean status, Pageable pageable);
	
	List<Person> getPersonSupervisor(String status, Date fechaIni, Date fechaFin);
	List<Person> getPersonSupervisorCampo(Date fechaIni, Date fechaFin);


}
