package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.repository.CuentaBancariaRepository;
import com.model.aldasa.repository.VoucherRepository;
import com.model.aldasa.service.VoucherService;


@Service("voucherService")
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	private VoucherRepository voucherRepository;

	@Override
	public Optional<Voucher> findById(Integer id) {
		// TODO Auto-generated method stub
		return voucherRepository.findById(id);
	}

	@Override
	public Voucher save(Voucher entity) {
		// TODO Auto-generated method stub
		return voucherRepository.save(entity);
	}

	@Override
	public void delete(Voucher entity) {
		// TODO Auto-generated method stub
		voucherRepository.delete(entity);
	}

	@Override
	public Voucher findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(
			CuentaBancaria cuentaBancaria, BigDecimal monto, String tipoTransaccion, String numeroTransaccion,
			Date fechaOperacion) {
		// TODO Auto-generated method stub
		return voucherRepository.findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(cuentaBancaria, monto, tipoTransaccion, numeroTransaccion, fechaOperacion);
	}

	@Override
	public List<Voucher> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return voucherRepository.findByEstado(estado);
	}



	@Override
	public Page<Voucher> findByEstadoAndGeneraDocumento(boolean estado, boolean generoDocumento, Pageable pageable) {
		// TODO Auto-generated method stub
		return voucherRepository.findByEstadoAndGeneraDocumento(estado, generoDocumento, pageable);
	}

	@Override
	public Voucher findByRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion) {
		// TODO Auto-generated method stub
		return voucherRepository.findByRequerimientoSeparacion(requerimientoSeparacion);
	}


	
}
