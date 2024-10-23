package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;

public interface DocumentoVentaService {

	Optional<DocumentoVenta> findById(Integer id);
	DocumentoVenta save(DocumentoVenta entity);
	DocumentoVenta saveNota(DocumentoVenta entity);
	DocumentoVenta save(DocumentoVenta entity, List<DetalleDocumentoVenta> lstDetalleDocumentoVenta, SerieDocumento serieDocumento);
	DocumentoVenta anular(DocumentoVenta entity);
	DocumentoVenta findByDocumentoVentaRefAndEstado(DocumentoVenta documentoVenta, boolean estado);
	
	void delete(DocumentoVenta entity);
	
	
	List<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa, Date fechaIni, Date fechaFin);
	List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin);
	List<DocumentoVenta> findByDocumentoVentaRefAndTipoDocumentoAndEstado(DocumentoVenta documentoVenta, TipoDocumento tipoDocumento, boolean estado);
	List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionAndEnvioSunat(boolean estado, Sucursal sucursal, Date fechaEmision, boolean envioSunat);
	
	Page<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin, Pageable pageable);
	
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndUsuarioRegistroUsernameLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, String username ,Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumentoAndUsuarioRegistroUsernameLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat, TipoDocumento tipoDocumento,  String username ,Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndUsuarioRegistroUsernameLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc,String username , Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndUsuarioRegistroUsernameLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat, String username , Pageable pageable);

	//PARA EL REPORTE DE DOCUMENTOS DE VENTAS
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user, Pageable pageable);

	Page<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user, Pageable pageable);
	Page<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user, Pageable pageable);

	// PARA DESCARGA DE CABECERA
	List<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user);
	List<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user);

	List<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user);
	List<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user);


	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionAndUsuarioRegistroUsernameLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaEmision, String username ,Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionAndUsuarioRegistroUsernameLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento,Date fechaEmision, String username , Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndFechaEmisionAndUsuarioRegistroUsernameLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat, Date fechaEmision, String username , Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumentoAndFechaEmisionAndUsuarioRegistroUsernameLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat, TipoDocumento tipoDocumento, Date fechaEmision, String username , Pageable pageable);

}
