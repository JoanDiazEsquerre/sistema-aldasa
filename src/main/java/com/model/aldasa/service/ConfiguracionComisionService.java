package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.model.aldasa.entity.ConfiguracionComision;

public interface ConfiguracionComisionService {
	
	Optional<ConfiguracionComision> findById(Integer id);
	ConfiguracionComision save(ConfiguracionComision entity);
	void delete(ConfiguracionComision entity);
	
	List<ConfiguracionComision> findByEstadoOrderByCodigoDesc(Boolean estado);
	
	ConfiguracionComision findByEstadoAndCodigo(Boolean estado, String codigo);
	ConfiguracionComision findByCodigoAndIdException(String codigo, int idComision);
	ConfiguracionComision findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(Date fechaIni, Date fechaFin);


	Page<ConfiguracionComision> findByEstadoAndFechaInicioBetween(Boolean estado,Date fechaIni, Date fechaFin, Pageable pageable);

}
