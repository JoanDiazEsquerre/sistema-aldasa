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
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.RequerimientoSeparacionRepository;
import com.model.aldasa.service.RequerimientoSeparacionService;

@Service("requerimientoSeparacionService")
public class RequerimientoSeparacionServiceImpl implements RequerimientoSeparacionService {
	
	@Autowired
	private RequerimientoSeparacionRepository requerimientoSeparacionRepository;

	@Override
	public Optional<RequerimientoSeparacion> findById(Integer id) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findById(id);
	}

	@Override
	public RequerimientoSeparacion save(RequerimientoSeparacion entity) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.save(entity);
	}

	@Override
	public void delete(RequerimientoSeparacion entity) {
		// TODO Auto-generated method stub
		requerimientoSeparacionRepository.delete(entity);
	}

	@Override
	public List<RequerimientoSeparacion> findByProspection(Prospection prostection) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findByProspection(prostection);
	}

	

	@Override
	public RequerimientoSeparacion findAllByLoteAndEstado(Lote lote, String estado) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findAllByLoteAndEstado(lote, estado);
	}

	@Override
	public Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndGeneraDocumento(String estado,
			Sucursal sucursal, boolean generaDocuento, Pageable pageable) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findAllByEstadoAndLoteProjectSucursalAndGeneraDocumento(estado, sucursal, generaDocuento, pageable); 
	}

	@Override
	public Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(
			String estado, Sucursal sucursal, String manzana, String numLote, String person, String asesor,
			String supervisor, Pageable pageable) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findAllByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(estado, sucursal, manzana, numLote, person, asesor, supervisor, pageable);
	}

	@Override
	public Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(
			String estado, Sucursal sucursal, Project proyecto, String manzana, String numLote, String person,
			String asesor, String supervisor, Pageable pageable) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findAllByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(estado, sucursal, proyecto, manzana, numLote, person, asesor, supervisor, pageable);
	}

	@Override
	public List<RequerimientoSeparacion> findByEstado(String estado) {
		// TODO Auto-generated method stub 
		return requerimientoSeparacionRepository.findByEstado(estado);
	}

	@Override
	public Page<RequerimientoSeparacion> findAllByEstadoAndSucursalAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(
			String estado, Sucursal sucursal, String person, String asesor, String supervisor, Pageable pageable) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findAllByEstadoAndSucursalAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(estado, sucursal, person, asesor, supervisor, pageable); 
	}

	@Override
	public List<RequerimientoSeparacion> findByLote(Lote lote) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findByLote(lote);
	}




}
