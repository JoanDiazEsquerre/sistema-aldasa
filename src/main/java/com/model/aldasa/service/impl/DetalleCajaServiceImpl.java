package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.DetalleCaja;
import com.model.aldasa.repository.DetalleCajaRepository;
import com.model.aldasa.service.DetalleCajaService;

@Service("detalleCajaService")
public class DetalleCajaServiceImpl implements DetalleCajaService {

	@Autowired
	private  DetalleCajaRepository detalleCajaRepository;

	@Override
	public Optional<DetalleCaja> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.findById(id);
	}

	@Override
	public DetalleCaja save(DetalleCaja entity) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.save(entity);
	}

	@Override
	public void delete(DetalleCaja entity) {
		// TODO Auto-generated method stub
		detalleCajaRepository.delete(entity);
	}

	@Override
	public Page<DetalleCaja> findByEstado(boolean estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.findByEstado(estado, pageable);
	}

	@Override
	public List<DetalleCaja> findByCajaAndEstadoOrderByFechaDesc(Caja caja, boolean estado) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.findByCajaAndEstadoOrderByFechaDesc(caja, estado);
	}

	@Override
	public List<DetalleCaja> findByCajaAndEstadoOrderByFechaAsc(Caja caja, boolean estado) {
		// TODO Auto-generated method stub
		return detalleCajaRepository.findByCajaAndEstadoOrderByFechaAsc(caja, estado);
	}
}
