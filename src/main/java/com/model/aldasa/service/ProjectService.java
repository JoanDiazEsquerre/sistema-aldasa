package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;

public interface ProjectService {
	
	Optional<Project> findById(Integer id);
	Project save(Project project) ;
	void delete(Project project);
	List<Project> findByStatusOrderByNameAsc(boolean status);
	List<Project> findByStatusAndSucursalOrderByNameAsc(boolean status, Sucursal sucursal);

	Project findByName (String name);
	Project findByNameException(String name, int idProject);

}
