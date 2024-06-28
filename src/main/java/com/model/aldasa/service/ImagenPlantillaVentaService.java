package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.ImagenPlantillaVenta;
import com.model.aldasa.entity.PlantillaVenta;

public interface ImagenPlantillaVentaService {

	Optional<ImagenPlantillaVenta> findById(Integer id);
	ImagenPlantillaVenta save(ImagenPlantillaVenta entity);
	void delete(ImagenPlantillaVenta entity);
	
	List<ImagenPlantillaVenta> findByPlantillaVentaAndEstado(PlantillaVenta nombre, boolean estado);

}
