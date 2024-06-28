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
import com.model.aldasa.entity.DetallePlanilla;
import com.model.aldasa.entity.Planilla;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.repository.CuentaBancariaRepository;
import com.model.aldasa.repository.DetallePlanillaRepository;
import com.model.aldasa.repository.PlanillaRepository;
import com.model.aldasa.repository.VoucherRepository;
import com.model.aldasa.service.DetallePlanillaService;
import com.model.aldasa.service.PlanillaService;
import com.model.aldasa.service.VoucherService;


@Service("detallePlanillaService")
public class DetallePlanillaServiceImpl implements DetallePlanillaService {

	@Autowired
	private DetallePlanillaRepository detallePlanillaRepository;

	@Override
	public Optional<DetallePlanilla> findById(Integer id) {
		// TODO Auto-generated method stub
		return detallePlanillaRepository.findById(id);
	}

	@Override
	public DetallePlanilla save(DetallePlanilla entity) {
		// TODO Auto-generated method stub
		return detallePlanillaRepository.save(entity); 
	}

	@Override
	public void delete(DetallePlanilla entity) {
		// TODO Auto-generated method stub
		detallePlanillaRepository.delete(entity);
	}

	@Override
	public List<DetallePlanilla> findByEstadoAndPlanilla(boolean estado, Planilla planilla) {
		// TODO Auto-generated method stub
		return detallePlanillaRepository.findByEstadoAndPlanilla(estado, planilla);
	}




	
	
}
