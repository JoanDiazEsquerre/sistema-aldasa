package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.ProjectRepository; 
import com.model.aldasa.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public Optional<Project> findById(Integer id) {
		// TODO Auto-generated method stub
		return projectRepository.findById(id);
	}

	@Override
	public Project save(Project project) {
		// TODO Auto-generated method stub
		return projectRepository.save(project);
	}

	@Override
	public void delete(Project project) {
		// TODO Auto-generated method stub
		projectRepository.delete(project);
	}

	@Override
	public List<Project> findByStatusOrderByNameAsc(boolean status) {
		// TODO Auto-generated method stub
		return projectRepository.findByStatusOrderByNameAsc(status);
	}

	@Override
	public Project findByName(String name) {
		// TODO Auto-generated method stub
		return projectRepository.findByName(name);
	}

	@Override
	public Project findByNameException(String name, int idProject) {
		// TODO Auto-generated method stub
		return projectRepository.findByNameException(name, idProject); 
	}

	@Override
	public List<Project> findByStatusAndSucursalOrderByNameAsc(boolean status, Sucursal sucursal) {
		// TODO Auto-generated method stub
		return projectRepository.findByStatusAndSucursalOrderByNameAsc(status, sucursal);
	}
	

	
}
