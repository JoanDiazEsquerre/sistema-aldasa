package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.entity.Voucher;

public interface DetalleDocumentoVentaRepository extends JpaRepository<DetalleDocumentoVenta, Integer>{
	
	List<DetalleDocumentoVenta> findByRequerimientoSeparacionAndDocumentoVentaEstado(RequerimientoSeparacion requerimientoSeparacion, boolean estado);

	List<DetalleDocumentoVenta> findByDocumentoVentaAndEstado(DocumentoVenta documentoVenta, boolean estado);
	
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndCuota(boolean estado, Cuota cuota);
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndCuotaPrepago(boolean estado, Cuota cuotaPrepago);
	
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndCuotaContratoOrderByCuotaContratoIdAsc(boolean estadoDocVenta, Contrato contrato);
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndCuotaPrepagoContratoOrderByCuotaContratoIdAsc(boolean estadoDocVenta, Contrato contrato);


	//PARA EL REPORTE DE DOCUMENTOS DE VENTAS
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin);
	List<DetalleDocumentoVenta> findByDocumentoVentaEstadoAndDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Boolean estado, Sucursal sucursal, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin);

	List<DetalleDocumentoVenta> findByDocumentoVentaSucursalAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Sucursal sucursal, Date fechaIni, Date fechaFin);
	List<DetalleDocumentoVenta> findByDocumentoVentaSucursalAndDocumentoVentaTipoDocumentoAndDocumentoVentaFechaEmisionBetweenOrderByDocumentoVentaNumeroAsc(Sucursal sucursal, TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin);

}
