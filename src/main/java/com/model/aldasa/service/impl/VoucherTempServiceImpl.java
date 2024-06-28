package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.VoucherTemp;
import com.model.aldasa.repository.VoucherTempRepository;
import com.model.aldasa.service.VoucherTempService;

@Service("voucherTempService")
public class VoucherTempServiceImpl implements VoucherTempService {
	
	@Autowired
	private VoucherTempRepository voucherTempRepository;

	@Override
	public Optional<VoucherTemp> findById(Integer id) {
		// TODO Auto-generated method stub
		return voucherTempRepository.findById(id);
	}

	@Override
	public VoucherTemp save(VoucherTemp entity) {
		// TODO Auto-generated method stub
		return voucherTempRepository.save(entity);
	}

	@Override
	public void delete(VoucherTemp entity) {
		// TODO Auto-generated method stub
		voucherTempRepository.delete(entity);
	}

	@Override
	public List<VoucherTemp> findByRequerimientoSeparacionAndEstado(RequerimientoSeparacion req, boolean status) {
		// TODO Auto-generated method stub
		return voucherTempRepository.findByRequerimientoSeparacionAndEstado(req, status); 
	}

	@Override
	public List<VoucherTemp> findByPlantillaVentaAndEstado(PlantillaVenta plantilla, boolean status) {
		// TODO Auto-generated method stub
		return voucherTempRepository.findByPlantillaVentaAndEstado(plantilla, status);
	}

	@Override
	public List<VoucherTemp> findByPlantillaVentaEstadoAndPlantillaVentaLoteAndEstado(String estadoPlantilla, Lote lote,
			boolean status) {
		// TODO Auto-generated method stub
		return voucherTempRepository.findByPlantillaVentaEstadoAndPlantillaVentaLoteAndEstado(estadoPlantilla, lote, status);
	}

	@Override
	public List<VoucherTemp> findByRequerimientoSeparacionEstadoAndRequerimientoSeparacionLoteAndEstado(
			String estadoReq, Lote lote, boolean status) {
		// TODO Auto-generated method stub
		return voucherTempRepository.findByRequerimientoSeparacionEstadoAndRequerimientoSeparacionLoteAndEstado(estadoReq, lote, status);
	}

	@Override
	public VoucherTemp findByPlantillaVentaAndMontoAndTipoTransaccionAndNumeroOperacionAndFechaOperacionAndCuentaBancariaAndEstado(
			PlantillaVenta plantilla, BigDecimal monto, String tipoTransaccion, String numTransaccion, Date fecha,
			CuentaBancaria cuentaBancaria, boolean estado) {
		// TODO Auto-generated method stub
		return voucherTempRepository.findByPlantillaVentaAndMontoAndTipoTransaccionAndNumeroOperacionAndFechaOperacionAndCuentaBancariaAndEstado(plantilla, monto, tipoTransaccion, numTransaccion, fecha, cuentaBancaria, estado);
	}

	@Override
	public VoucherTemp findByRequerimientoSeparacionAndMontoAndTipoTransaccionAndNumeroOperacionAndFechaOperacionAndCuentaBancariaAndEstado(
			RequerimientoSeparacion req, BigDecimal monto, String tipoTransaccion, String numTransaccion, Date fecha,
			CuentaBancaria cuentaBancaria, boolean estado) {
		// TODO Auto-generated method stub
		return voucherTempRepository.findByRequerimientoSeparacionAndMontoAndTipoTransaccionAndNumeroOperacionAndFechaOperacionAndCuentaBancariaAndEstado(req, monto, tipoTransaccion, numTransaccion, fecha, cuentaBancaria, estado);
	}
	



}
