package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.ConfiguracionComision;


public interface ConfiguracionComisionRepository  extends JpaRepository<ConfiguracionComision, Integer>{
	
	List<ConfiguracionComision> findByEstadoOrderByCodigoDesc(Boolean estado);
	ConfiguracionComision findByEstadoAndCodigo(Boolean estado, String codigo);
	ConfiguracionComision findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(Date fechaIni, Date fechaFin);
	
	@Query(nativeQuery = true,value = "SELECT * FROM configuracioncomision WHERE codigo=:codigo AND id<>:idComision ")
	ConfiguracionComision findByCodigoAndIdException(String codigo, int idComision);

	Page<ConfiguracionComision> findByEstadoAndFechaInicioBetween(Boolean estado,Date fechaIni, Date fechaFin, Pageable pageable);


}
