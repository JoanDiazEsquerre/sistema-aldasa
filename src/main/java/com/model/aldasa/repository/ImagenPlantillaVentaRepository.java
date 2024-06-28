package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.ImagenPlantillaVenta;
import com.model.aldasa.entity.PlantillaVenta;

public interface ImagenPlantillaVentaRepository extends PagingAndSortingRepository<ImagenPlantillaVenta, Integer>{

	List<ImagenPlantillaVenta> findByPlantillaVentaAndEstado(PlantillaVenta plantillaVenta, boolean estado);

}
