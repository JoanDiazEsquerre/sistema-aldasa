package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.DetalleMovimientoBancario;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.MovimientoBancario;

public interface DetalleMovimientoBancarioService {

	Optional<DetalleMovimientoBancario> findById(Integer id);
	DetalleMovimientoBancario save(DetalleMovimientoBancario entity);
	void delete(DetalleMovimientoBancario entity);
	
	DetalleMovimientoBancario findByEstadoAndImagen(boolean estado, Imagen imagen);
	
	//TODOS
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancario(boolean estado, MovimientoBancario movimientoBancario, Pageable pageable);
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacion(boolean estado, MovimientoBancario movimientoBancario, Date fechaOperacion, Pageable pageable);
	
	//BUSQUEDA DE ABONOS IDENTIFICADOS
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, String observacion, BigDecimal importe,Pageable pageable);
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, Date fechaOperacion, String observacion, BigDecimal importe,Pageable pageable);
		
	//BUSQUEDA DE CARGOS IDENTIFICADOS
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, String observacion, BigDecimal importe,Pageable pageable);
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, Date fechaOperacion, String observacion, BigDecimal importe,Pageable pageable);
	
	//BUSQUEDA DE ABONOS Y CARGOS IDENTIFICADOS
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, String observacion,Pageable pageable);
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, Date fechaOperacion, String observacion,Pageable pageable);
	
	
	
	
	//BUSQUEDA DE ABONOS SIN IDENTIFICAR
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, Pageable pageable);
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, String fechaOperacion, Pageable pageable);
		
	//BUSQUEDA DE CARGOS SIN IDENTIFICAR
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, Pageable pageable);
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, String fechaOperacion, Pageable pageable);
	
	//BUSQUEDA DE ABONOS Y CARGOS SIN IDENTIFICAR
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, Pageable pageable);
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, String fechaOperacion, Pageable pageable);
		
	
	
	
	//DESCARGA REPORTE
	//TODOS
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancario(boolean estado, MovimientoBancario movimientoBancario);
	
	//BUSQUEDA DE ABONOS IDENTIFICADOS
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, String observacion, BigDecimal importe);
		
	//BUSQUEDA DE CARGOS IDENTIFICADOS
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, String observacion, BigDecimal importe);
	
	//BUSQUEDA DE ABONOS Y CARGOS IDENTIFICADOS
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionIsNotAndTipoMovimientoIsNotNull(boolean estado, MovimientoBancario movimientoBancario, String observacion);
	
	
	
	//BUSQUEDA DE ABONOS SIN IDENTIFICAR 
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(int idMovimientoBancario);
		
	//BUSQUEDA DE CARGOS SIN IDENTIFICAR
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(int idMovimientoBancario);
		
	//BUSQUEDA ABONOS Y CARGOS SIN IDENTIFICAR
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(int idMovimientoBancario);
		
		
	BigDecimal totalImporteMovimiento(int idMovimientoBancario);
	
	BigDecimal totalImporteAbonoIdentificado(int idMovimientoBancario);
	BigDecimal totalImporteCargoIdentificado(int idMovimientoBancario);
	
	BigDecimal totalImporteAbonoSinIdentificar(int idMovimientoBancario);
	BigDecimal totalImporteCargoSinIdentificar(int idMovimientoBancario);
	
	
}
