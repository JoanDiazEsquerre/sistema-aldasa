package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.DetalleMovimientoBancario;
import com.model.aldasa.entity.MovimientoBancario;


public interface DetalleMovimientoBancarioRepository extends JpaRepository<DetalleMovimientoBancario, Integer> {
	
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
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = :estado AND idMovimientoBancario = :idMovimientoBancario and importe > 0 AND (observacion = '' OR idTipoMovimiento is null)")
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, Pageable pageable);
	
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = :estado AND idMovimientoBancario = :idMovimientoBancario and fechaOperacion = :fechaOperacion and importe > 0 AND (observacion = '' OR idTipoMovimiento is null)")
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, String fechaOperacion,Pageable pageable);
		
	//BUSQUEDA DE CARGOS SIN IDENTIFICAR
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = :estado AND idMovimientoBancario = :idMovimientoBancario and importe < 0 AND (observacion = '' OR idTipoMovimiento is null)")
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, Pageable pageable);
	
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = :estado AND idMovimientoBancario = :idMovimientoBancario and fechaOperacion = :fechaOperacion and importe < 0 AND (observacion = '' OR idTipoMovimiento is null)")
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario, String fechaOperacion, Pageable pageable);	
	
	//BUSQUEDA DE ABONOS Y CARGOS SIN IDENTIFICAR
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = :estado AND idMovimientoBancario = :idMovimientoBancario AND (observacion = '' OR idTipoMovimiento is null)")
	Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(boolean estado, int idMovimientoBancario,Pageable pageable);
	
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = :estado AND idMovimientoBancario = :idMovimientoBancario and fechaOperacion = :fechaOperacion AND (observacion = '' OR idTipoMovimiento is null)")
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
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = 1 AND idMovimientoBancario = :idMovimientoBancario and importe > 0 AND (observacion = '' OR idTipoMovimiento is null)")
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(int idMovimientoBancario);
		
	//BUSQUEDA DE CARGOS SIN IDENTIFICAR
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = 1 AND idMovimientoBancario = :idMovimientoBancario and importe < 0 AND (observacion = '' OR idTipoMovimiento is null)")
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(int idMovimientoBancario);
		
	//BUSQUEDA SIN IDENTIFICAR
	@Query(nativeQuery = true,value = "SELECT * from detallemovimientobancario where estado = 1 AND idMovimientoBancario = :idMovimientoBancario and (observacion = '' OR idTipoMovimiento is null)")
	List<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(int idMovimientoBancario);
		
		
		
		
	
	@Query(nativeQuery = true,value = "SELECT if(sum(importe) is null, 0,sum(importe)) from detallemovimientobancario where idMovimientoBancario = :idMovimientoBancario and estado = 1")
	BigDecimal totalImporteMovimiento(int idMovimientoBancario);
	
	
	@Query(nativeQuery = true,value = "SELECT if(sum(importe) is null, 0,sum(importe)) from detallemovimientobancario where idMovimientoBancario = :idMovimientoBancario and estado = 1 AND importe > 0 AND observacion <> '' and idTipoMovimiento is not null")
	BigDecimal totalImporteAbonoIdentificado(int idMovimientoBancario);
	
	@Query(nativeQuery = true,value = "SELECT if(sum(importe) is null, 0,sum(importe)) from detallemovimientobancario where idMovimientoBancario = :idMovimientoBancario and estado = 1 AND importe < 0 AND observacion <> '' and idTipoMovimiento is not null")
	BigDecimal totalImporteCargoIdentificado(int idMovimientoBancario);
	
	
	@Query(nativeQuery = true,value = "SELECT if(sum(importe) is null, 0,sum(importe)) from detallemovimientobancario where idMovimientoBancario = :idMovimientoBancario and estado = 1 AND importe > 0 AND (idTipoMovimiento is null or observacion = '')")
	BigDecimal totalImporteAbonoSinIdentificar(int idMovimientoBancario);
	
	@Query(nativeQuery = true,value = "SELECT if(sum(importe) is null, 0,sum(importe)) from detallemovimientobancario where idMovimientoBancario = :idMovimientoBancario and estado = 1 AND importe < 0 AND (idTipoMovimiento is null or observacion = '')")

	BigDecimal totalImporteCargoSinIdentificar(int idMovimientoBancario);
}
