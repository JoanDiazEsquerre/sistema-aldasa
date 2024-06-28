package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Lote;

public interface ImagenRepository extends PagingAndSortingRepository<Imagen, Integer>{
	
	List<Imagen> findByNombreLikeAndEstado(String nombre, boolean estado);
	List<Imagen> findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(boolean estado, Date fecha, BigDecimal monto, String numOperacion, CuentaBancaria cuentaBancaria);
	List<Imagen> findByEstadoAndMontoAndNumeroOperacionAndCuentaBancaria(boolean estado, BigDecimal monto, String numOperacion, CuentaBancaria cuentaBancaria);
	
	List<Imagen> findByDocumentoVentaAndEstadoAndResuelto(DocumentoVenta documentoVenta, boolean estado, boolean resuelto);
	List<Imagen> findByDocumentoVentaAndEstado(DocumentoVenta documentoVenta, boolean estado);
	List<Imagen> findByLoteAndEstado(Lote lote, boolean estado);
	List<Imagen> findByEstadoAndCuentaBancariaAndFechaBetweenOrderByFechaDesc(boolean estado, CuentaBancaria cuentaBancaria, Date fechaIni, Date fechaFin);
	List<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndFechaBetweenOrderByFechaDesc(boolean estado, String sucursal, Date fechaIni, Date fechaFin);
	
	
	Page<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeAndFechaIsNullOrderByIdDesc
	(boolean estado, String sucursal, String numCuenta, String porRegularizar, String numeroOperacion, String tipoTransaccion, String comentario,Pageable pageable);
	
	Page<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndFechaBetweenAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeOrderByIdDesc
	(boolean estado, String sucursal, String numCuenta, Date fechaIni, Date fechaFin, String porRegularizar, String numeroOperacion, String tipoTransaccion, String comentario,Pageable pageable);
	
	Page<Imagen> findByEstadoAndMontoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeAndFechaIsNullOrderByIdDesc
	(boolean estado, BigDecimal monto, String sucursal, String numCuenta, String porRegularizar, String numeroOperacion, String tipoTransaccion, String comentario,Pageable pageable);

	Page<Imagen> findByEstadoAndMontoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndFechaBetweenAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeOrderByIdDesc
	(boolean estado, BigDecimal monto, String sucursal, String numCuenta, Date fechaIni, Date fechaFin, String porRegularizar, String numeroOperacion, String tipoTransaccion, String comentario,Pageable pageable);


	

	Imagen findByNombre(String nombre);
	
	
	@Query(nativeQuery = true,value = "SELECT * FROM imagen WHERE estado = :estado AND fecha = :fecha and monto = :monto AND numeroOperacion = :numOperacion AND idCuentaBancaria = :idCtaBanc AND id<>:id ")
	List<Imagen> findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(boolean estado, String fecha, BigDecimal monto, String numOperacion, int idCtaBanc, int id);

	@Query(nativeQuery = true,value = "SELECT * FROM imagen WHERE estado = :estado and monto = :monto AND numeroOperacion = :numOperacion AND idCuentaBancaria = :idCtaBanc AND id<>:id ")
	List<Imagen> findByEstadoAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(boolean estado, BigDecimal monto, String numOperacion, int idCtaBanc, int id);



}
