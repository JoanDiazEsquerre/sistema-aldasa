package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Sucursal;

public interface PlantillaVentaRepository extends JpaRepository<PlantillaVenta, Integer>{

	PlantillaVenta findByDocumentoVenta(DocumentoVenta documentoVenta);
	
	List<PlantillaVenta> findByEstadoAndLote(String estado, Lote lote);
	
	Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(String estado, String person, String project, String manzana, String lote, Sucursal sucursal, Pageable pageable);
	Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonSupervisor(String estado, String person, String project, String manzana, String lote, Sucursal sucursal, Person supervisor, Pageable pageable);
	Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonAsesor(String estado, String person, String project, String manzana, String lote, Sucursal sucursal, Person asesor, Pageable pageable);

}
