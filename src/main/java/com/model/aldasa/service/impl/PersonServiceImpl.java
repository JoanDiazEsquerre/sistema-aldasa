package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.repository.PersonRepository;
import com.model.aldasa.service.PersonService;

@Service("personService")
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Override
	public Optional<Person> findById(Integer id) {
		return personRepository.findById(id);
	}

	@Override
	public Person save(Person entity) {
		return personRepository.save(entity);
	}

	@Override
	public void delete(Person entity) {
		personRepository.delete(entity);
	}

	@Override
	public List<Person> findByStatus(Boolean status) {
		return personRepository.findByStatus(status);
	}
	
	@Override
	public Page<Person> findAllByDniLikeAndSurnamesLikeAndStatus(String dni, String names, Boolean status, Pageable pageable) {
		return personRepository.findAllByDniLikeAndSurnamesLikeAndStatus(dni, names, status, pageable);
	}
	
	@Override
	public Person findByDni(String dni) {
		return personRepository.findByDni(dni);
	}
	
	@Override
	public Person findByDniException(String dni, int idUser) {
		return personRepository.findByDniException(dni, idUser);
	}

	@Override
	public List<Person> getPersonSupervisor(String status, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return personRepository.getPersonSupervisor(status, fechaIni, fechaFin);
	}

	@Override
	public List<Person> getPersonSupervisorCampo(Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return personRepository.getPersonSupervisorCampo(fechaIni, fechaFin);
	}

}
