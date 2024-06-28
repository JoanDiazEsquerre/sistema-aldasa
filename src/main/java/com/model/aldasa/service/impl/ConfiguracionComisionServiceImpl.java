package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.repository.ConfiguracionComisionRepository;
import com.model.aldasa.service.ConfiguracionComisionService;

@Service("configuracionComisionService")
public class ConfiguracionComisionServiceImpl  implements ConfiguracionComisionService {
	
	@Autowired
	private ConfiguracionComisionRepository configuracionComisionRepository;

	@Override
	public Optional<ConfiguracionComision> findById(Integer id) {
		// TODO Auto-generated method stub
		return configuracionComisionRepository.findById(id);
	}

	@Override
	public ConfiguracionComision save(ConfiguracionComision entity) {
		// TODO Auto-generated method stub
		return configuracionComisionRepository.save(entity);
	}

	@Override
	public void delete(ConfiguracionComision entity) {
		// TODO Auto-generated method stub
		configuracionComisionRepository.delete(entity);
	}
	
	@Override
	public List<ConfiguracionComision> findByEstadoOrderByCodigoDesc(Boolean estado){
		// TODO Auto-generated method stub
		return configuracionComisionRepository.findByEstadoOrderByCodigoDesc(estado);
	}

	@Override
	public Page<ConfiguracionComision> findByEstadoAndFechaInicioBetween(Boolean estado,Date fechaIni, Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return configuracionComisionRepository.findByEstadoAndFechaInicioBetween(estado, fechaIni, fechaFin, pageable); 
	}

	@Override
	public ConfiguracionComision findByEstadoAndCodigo(Boolean estado, String codigo) {
		// TODO Auto-generated method stub
		return configuracionComisionRepository.findByEstadoAndCodigo(estado, codigo);
	}

	@Override
	public ConfiguracionComision findByCodigoAndIdException(String codigo, int idComision) {
		// TODO Auto-generated method stub
		return configuracionComisionRepository.findByCodigoAndIdException(codigo, idComision);
	}

	@Override
	public ConfiguracionComision findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(Date fechaIni, Date fechaCierre) {
		// TODO Auto-generated method stub
		return configuracionComisionRepository.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(fechaIni, fechaCierre); 
	}

	

	

}
