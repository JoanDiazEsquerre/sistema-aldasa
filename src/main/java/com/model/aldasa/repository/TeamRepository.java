package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;

public interface TeamRepository extends PagingAndSortingRepository<Team, Integer>{

	List<Team> findByStatus(Boolean status);
	Team findByName(String name);
	
	@Query(nativeQuery = true,value = "SELECT * FROM team WHERE name=:namee AND id <> :idTeam ")
    Team findByNameException(String namee, int idTeam);
	
	Team findByPersonSupervisor(Person personSupervisor);
	
	Page<Team> findByNameLikeAndStatus(String name, boolean status, Pageable pageable);
	Page<Team> findByStatus(boolean status, Pageable pageable);


}
