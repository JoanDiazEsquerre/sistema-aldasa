package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Sucursal;

public interface PlantillaVentaService {

	Optional<PlantillaVenta> findById(Integer id);
	PlantillaVenta save(PlantillaVenta entity);
	PlantillaVenta saveAprobarPlantilla(PlantillaVenta entity, ConfiguracionComision configuracionComision, ComisionSupervisor comisionSupervisor);
	PlantillaVenta anularPlantilla(PlantillaVenta entity); 
	void delete(PlantillaVenta entity);
	
	List<PlantillaVenta> findByEstadoAndLote(String estado, Lote lote);
	
	//para administrador
	Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(String estado, String person, String project, String manzana, String lote, Sucursal sucursal, Pageable pageable);

	//para supervisor
	Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonSupervisor(String estado, String person, String project, String manzana, String lote, Sucursal sucursal, Person supervisor, Pageable pageable);

	
	//para asesor
	Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonAsesor(String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal, Person asesor, Pageable pageable);

}
