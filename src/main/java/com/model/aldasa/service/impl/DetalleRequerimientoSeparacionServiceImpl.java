package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.DetalleRequerimientoSeparacion;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.DetalleRequerimientoSeparacionRepository;
import com.model.aldasa.repository.RequerimientoSeparacionRepository;
import com.model.aldasa.service.DetalleRequerimientoSeparacionService;
import com.model.aldasa.service.RequerimientoSeparacionService;

@Service("detalleRequerimientoSeparacionService")
public class DetalleRequerimientoSeparacionServiceImpl implements DetalleRequerimientoSeparacionService {
	
	@Autowired
	private DetalleRequerimientoSeparacionRepository detalleRequerimientoSeparacionRepository;

	@Override
	public Optional<DetalleRequerimientoSeparacion> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleRequerimientoSeparacionRepository.findById(id);
	}

	@Override
	public DetalleRequerimientoSeparacion save(DetalleRequerimientoSeparacion entity) {
		// TODO Auto-generated method stub
		return detalleRequerimientoSeparacionRepository.save(entity);
	}

	@Override
	public void delete(DetalleRequerimientoSeparacion entity) {
		// TODO Auto-generated method stub
		detalleRequerimientoSeparacionRepository.delete(entity);
	}

	@Override
	public List<DetalleRequerimientoSeparacion> findByEstadoAndRequerimientoSeparacion(boolean estado,
			RequerimientoSeparacion requerimientoSeparacion) {
		// TODO Auto-generated method stub
		return detalleRequerimientoSeparacionRepository.findByEstadoAndRequerimientoSeparacion(estado, requerimientoSeparacion); 
	}

	@Override
	public Page<DetalleRequerimientoSeparacion> findByEstadoAndRequerimientoSeparacionLoteProjectSucursalAndBoleteoTotal(boolean estado, Sucursal sucursal, String boleteoTotal, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleRequerimientoSeparacionRepository.findByEstadoAndRequerimientoSeparacionLoteProjectSucursalAndBoleteoTotal(estado, sucursal,boleteoTotal, pageable); 
	}




}
