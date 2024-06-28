package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Team;

public interface TeamService {
	
	Optional<Team> findById(Integer id);
	Team save(Team team) ;
	void delete(Team team);
	List<Team> findByStatus(boolean status);
	Team findByName (String name);
	Team findByNameException(String name, int idTeam);
	Team findByPersonSupervisor(Person personSupervisor);
	
	Page<Team> findByNameLikeAndStatus(String name, boolean status, Pageable pageable);
	Page<Team> findByStatus(boolean status, Pageable pageable);



}
