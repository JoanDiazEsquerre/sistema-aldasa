package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Sucursal;

public interface RequerimientoSeparacionService {
	
	Optional<RequerimientoSeparacion> findById(Integer id);
	RequerimientoSeparacion save(RequerimientoSeparacion entity);
	void delete(RequerimientoSeparacion entity);
	
	RequerimientoSeparacion findAllByLoteAndEstado(Lote lote, String estado);
	
	List<RequerimientoSeparacion> findByProspection(Prospection prostection);
	
	Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(String estado, Sucursal sucursal, String manzana, String numLote, String person, String asesor, String supervisor,Pageable pageable);
	Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(String estado, Sucursal sucursal,Project proyecto, String manzana, String numLote, String person, String asesor, String supervisor,Pageable pageable);
	
	Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndGeneraDocumento(String estado, Sucursal sucursal, boolean generaDocuento,Pageable pageable);



	
	
}
