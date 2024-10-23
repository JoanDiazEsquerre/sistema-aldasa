package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DetalleRequerimientoSeparacion;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.VoucherTemp;


public interface VoucherTempService {
	
	Optional<VoucherTemp> findById(Integer id);
	VoucherTemp save(VoucherTemp entity);
	void delete(VoucherTemp entity);
	

	VoucherTemp findByPlantillaVentaAndMontoAndTipoTransaccionAndNumeroOperacionAndFechaOperacionAndCuentaBancariaAndEstado(PlantillaVenta plantilla, BigDecimal monto, String tipoTransaccion, String numTransaccion, Date fecha, CuentaBancaria cuentaBancaria, boolean estado);
	VoucherTemp findByRequerimientoSeparacionAndMontoAndTipoTransaccionAndNumeroOperacionAndFechaOperacionAndCuentaBancariaAndEstado(RequerimientoSeparacion req, BigDecimal monto, String tipoTransaccion, String numTransaccion, Date fecha, CuentaBancaria cuentaBancaria, boolean estado);

	
	List<VoucherTemp> findByDetalleRequerimientoSeparacionRequerimientoSeparacionAndEstado(RequerimientoSeparacion req, boolean status);
	List<VoucherTemp> findByDetalleRequerimientoSeparacionAndEstado(DetalleRequerimientoSeparacion detReq, boolean status);
	List<VoucherTemp> findByPlantillaVentaAndEstado(PlantillaVenta plantilla, boolean status);
	List<VoucherTemp> findByPlantillaVentaEstadoAndPlantillaVentaLoteAndEstado(String estadoPlantilla, Lote lote, boolean status);
	List<VoucherTemp> findByRequerimientoSeparacionEstadoAndRequerimientoSeparacionLoteAndEstado(String estadoReq, Lote lote, boolean status);

	



}
