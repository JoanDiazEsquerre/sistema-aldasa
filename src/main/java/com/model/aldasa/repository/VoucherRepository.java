package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Voucher;


public interface VoucherRepository extends JpaRepository<Voucher, Integer>{
	
	Voucher findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(CuentaBancaria cuentaBancaria, 
			BigDecimal monto, String tipoTransaccion, String numeroTransaccion, Date fechaOperacion);
	
	Voucher findByRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion);

	
	List<Voucher> findByEstado(boolean estado);
	
	Page<Voucher> findByEstadoAndGeneraDocumento(boolean estado, boolean generoDocumento, Pageable pageable);




}
