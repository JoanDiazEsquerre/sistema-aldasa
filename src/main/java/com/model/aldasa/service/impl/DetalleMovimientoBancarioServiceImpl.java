package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DetalleMovimientoBancario;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.MovimientoBancario;
import com.model.aldasa.repository.DetalleMovimientoBancarioRepository;
import com.model.aldasa.service.DetalleMovimientoBancarioService;

@Service("detalleMovimientoBancarioService")
public class DetalleMovimientoBancarioServiceImpl implements DetalleMovimientoBancarioService {
	
	@Autowired
	private DetalleMovimientoBancarioRepository detalleMovimientoBancarioRepository;

	@Override
	public Optional<DetalleMovimientoBancario> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findById(id);
	}

	@Override
	public DetalleMovimientoBancario save(DetalleMovimientoBancario entity) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.save(entity);
	}

	@Override
	public void delete(DetalleMovimientoBancario entity) {
		// TODO Auto-generated method stub
		detalleMovimientoBancarioRepository.delete(entity);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancario(boolean estado,
			MovimientoBancario movimientoBancario, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancario(estado, movimientoBancario, pageable); 
	}

	@Override
	public BigDecimal totalImporteMovimiento(int idMovimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.totalImporteMovimiento(idMovimientoBancario);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacion(boolean estado,
			MovimientoBancario movimientoBancario, Date fechaOperacion, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndFechaOperacion(estado, movimientoBancario, fechaOperacion, pageable); 
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, String observacion, BigDecimal importe,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(estado, movimientoBancario, observacion, importe, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, Date fechaOperacion, String observacion,
			BigDecimal importe, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(estado, movimientoBancario, fechaOperacion, observacion, importe, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, String observacion, BigDecimal importe,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(estado, movimientoBancario, observacion, importe, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, Date fechaOperacion, String observacion,
			BigDecimal importe, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(estado, movimientoBancario, fechaOperacion, observacion, importe, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, String observacion, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndTipoMovimientoIsNotNull(estado, movimientoBancario, observacion, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, Date fechaOperacion, String observacion,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndTipoMovimientoIsNotNull(estado, movimientoBancario, fechaOperacion, observacion, pageable);
	}

	@Override
	public List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancario(boolean estado,
			MovimientoBancario movimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancario(estado, movimientoBancario);
	}

	@Override
	public List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, String observacion, BigDecimal importe) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(estado, movimientoBancario, observacion, importe);
	}

	@Override
	public List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, String observacion, BigDecimal importe) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(estado, movimientoBancario, observacion, importe);
	}

	@Override
	public List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndTipoMovimientoIsNotNull(
			boolean estado, MovimientoBancario movimientoBancario, String observacion) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndTipoMovimientoIsNotNull(estado, movimientoBancario, observacion);
	}

	@Override
	public BigDecimal totalImporteAbonoIdentificado(int idMovimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.totalImporteAbonoIdentificado(idMovimientoBancario); 
	}

	@Override
	public BigDecimal totalImporteCargoIdentificado(int idMovimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.totalImporteCargoIdentificado(idMovimientoBancario);
	}

	@Override
	public List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(int idMovimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(idMovimientoBancario); 
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(estado, idMovimientoBancario, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, String fechaOperacion, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionOrTipoMovimientoIsNull(estado, idMovimientoBancario, fechaOperacion, pageable);
	}

	@Override
	public BigDecimal totalImporteAbonoSinIdentificar(int idMovimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.totalImporteAbonoSinIdentificar(idMovimientoBancario);
	}

	@Override
	public BigDecimal totalImporteCargoSinIdentificar(int idMovimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.totalImporteCargoSinIdentificar(idMovimientoBancario);
	}

	@Override
	public List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(int idMovimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(idMovimientoBancario);
	}

	@Override
	public List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(int movimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(movimientoBancario);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(estado, idMovimientoBancario, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, String fechaOperacion, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(estado, idMovimientoBancario, fechaOperacion, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, 	Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(estado, idMovimientoBancario, pageable);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, String fechaOperacion, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(estado, idMovimientoBancario, fechaOperacion, pageable);
	}

	@Override
	public DetalleMovimientoBancario findByEstadoAndImagen(boolean estado, Imagen imagen) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndImagen(estado, imagen); 
	}



	


}
