package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Sucursal;

public interface RequerimientoSeparacionRepository extends JpaRepository<RequerimientoSeparacion, Integer> {
	
	RequerimientoSeparacion findAllByLoteAndEstado(Lote lote, String estado);
	
	List<RequerimientoSeparacion> findByProspection(Prospection prostection);
	List<RequerimientoSeparacion> findByEstado(String estado);
	List<RequerimientoSeparacion> findByLote(Lote lote);
	
	Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(String estado, Sucursal sucursal, String manzana, String numLote, String person, String asesor, String supervisor,Pageable pageable);
	Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(String estado, Sucursal sucursal,Project proyecto, String manzana, String numLote, String person, String asesor, String supervisor,Pageable pageable);
	
	Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndGeneraDocumento(String estado, Sucursal sucursal, boolean generaDocuento,Pageable pageable);

	Page<RequerimientoSeparacion> findAllByEstadoAndSucursalAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(String estado, Sucursal sucursal, String person, String asesor, String supervisor,Pageable pageable);


}
