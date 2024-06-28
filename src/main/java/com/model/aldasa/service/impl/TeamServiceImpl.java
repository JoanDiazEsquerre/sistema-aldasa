package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Team;
import com.model.aldasa.repository.TeamRepository;
import com.model.aldasa.service.TeamService;

@Service("teamService")
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public Optional<Team> findById(Integer id) {
		return teamRepository.findById(id);
	}

	@Override
	public Team save(Team team) {
		return teamRepository.save(team);
	}

	@Override
	public void delete(Team team) {
		teamRepository.delete(team);
	}

	@Override
	public List<Team> findByStatus(boolean status) {
		return teamRepository.findByStatus(status);
	}
	
	@Override
	public Team findByName(String name) {
		return teamRepository.findByName(name);
	}

	@Override
	public Team findByNameException(String name, int idTeam) {
		return teamRepository.findByNameException(name, idTeam);
	}
	
	@Override
	public Team findByPersonSupervisor(Person personSupervisor) {
		return teamRepository.findByPersonSupervisor(personSupervisor);
	}

	@Override
	public Page<Team> findByNameLikeAndStatus(String name, boolean status, Pageable pageable) {
		// TODO Auto-generated method stub
		return teamRepository.findByNameLikeAndStatus(name, status, pageable); 
	}

	@Override
	public Page<Team> findByStatus(boolean status, Pageable pageable) {
		// TODO Auto-generated method stub
		return teamRepository.findByStatus(status, pageable);
	}
	
}
