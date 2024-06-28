package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;
import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Project;

public interface ComisionProyectoService {
	
	Optional<ComisionProyecto> findById(Integer id);
	ComisionProyecto save(ComisionProyecto entity);
	void delete(ComisionProyecto entity);
	
	ComisionProyecto findByConfiguracionComisionAndProyectoAndEstado(ConfiguracionComision comision, Project proyecto, boolean estado);
	List<ComisionProyecto> findByConfiguracionComisionAndEstado(ConfiguracionComision comision, boolean estado);

	


}
