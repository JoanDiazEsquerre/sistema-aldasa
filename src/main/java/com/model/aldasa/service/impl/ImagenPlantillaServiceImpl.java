package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ImagenPlantillaVenta;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.repository.ImagenPlantillaVentaRepository;
import com.model.aldasa.service.ImagenPlantillaVentaService;

@Service("imagenPlantillaVentaService")
public class ImagenPlantillaServiceImpl implements ImagenPlantillaVentaService{

	@Autowired
	private ImagenPlantillaVentaRepository imagenPlantillaVentaRepository;

	@Override
	public Optional<ImagenPlantillaVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return imagenPlantillaVentaRepository.findById(id);
	}

	@Override
	public ImagenPlantillaVenta save(ImagenPlantillaVenta entity) {
		// TODO Auto-generated method stub
		return imagenPlantillaVentaRepository.save(entity);
	}

	@Override
	public void delete(ImagenPlantillaVenta entity) {
		// TODO Auto-generated method stub
		imagenPlantillaVentaRepository.delete(entity);
	}

	@Override
	public List<ImagenPlantillaVenta> findByPlantillaVentaAndEstado(PlantillaVenta plantillaVenta, boolean estado) {
		// TODO Auto-generated method stub
		return imagenPlantillaVentaRepository.findByPlantillaVentaAndEstado(plantillaVenta, estado);
	}
}
