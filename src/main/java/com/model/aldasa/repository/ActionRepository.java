package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Action;


public interface ActionRepository extends PagingAndSortingRepository<Action, Integer> {
	
	List<Action> findByStatus(boolean status);

}
