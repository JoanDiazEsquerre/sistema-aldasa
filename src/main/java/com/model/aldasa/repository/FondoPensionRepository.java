package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.FondoPension;

public interface FondoPensionRepository extends JpaRepository<FondoPension, Integer> {

	List<FondoPension> findByEstado(boolean estado);

}
