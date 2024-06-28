package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ImagenRequerimientoSeparacion;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.repository.ImagenRequerimientoSeparacionRepository;
import com.model.aldasa.service.ImagenPlantillaVentaService;
import com.model.aldasa.service.ImagenRequerimientoSeparacionService;

@Service("imagenRequerimientoSeparacionService")
public class ImagenRequerimientoSeparacionServiceImpl implements ImagenRequerimientoSeparacionService{

	@Autowired
	private ImagenRequerimientoSeparacionRepository imagenRequerimientoSeparacionRepository;

	@Override
	public Optional<ImagenRequerimientoSeparacion> findById(Integer id) {
		// TODO Auto-generated method stub
		return imagenRequerimientoSeparacionRepository.findById(id);
	}

	@Override
	public ImagenRequerimientoSeparacion save(ImagenRequerimientoSeparacion entity) {
		// TODO Auto-generated method stub
		return imagenRequerimientoSeparacionRepository.save(entity);
	}

	@Override
	public void delete(ImagenRequerimientoSeparacion entity) {
		// TODO Auto-generated method stub
		imagenRequerimientoSeparacionRepository.delete(entity);
	}

	@Override
	public List<ImagenRequerimientoSeparacion> findByRequerimientoSeparacionAndEstado(RequerimientoSeparacion requerimiento, boolean estado) {
		// TODO Auto-generated method stub
		return imagenRequerimientoSeparacionRepository.findByRequerimientoSeparacionAndEstado(requerimiento, estado);
	}
}
