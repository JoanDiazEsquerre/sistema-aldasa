package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.Project;

public interface ComisionProyectoRepository  extends JpaRepository<ComisionProyecto, Integer>{
	
	ComisionProyecto findByConfiguracionComisionAndProyectoAndEstado(ConfiguracionComision comision, Project proyecto, boolean estado);
	List<ComisionProyecto> findByConfiguracionComisionAndEstado(ConfiguracionComision comision, boolean estado);


}
