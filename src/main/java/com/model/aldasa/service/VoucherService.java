package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Voucher;

public interface VoucherService {

	Optional<Voucher> findById(Integer id);
	Voucher save(Voucher entity);
	void delete(Voucher entity);
	
	Voucher findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(CuentaBancaria cuentaBancaria, 
			BigDecimal monto, String tipoTransaccion, String numeroTransaccion, Date fechaOperacion);
	
	Voucher findByRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion);
	
	List<Voucher> findByEstado(boolean estado);
	
	Page<Voucher> findByEstadoAndGeneraDocumento(boolean estado, boolean generoDocumento, Pageable pageable);


}
