package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DetalleMovimientoBancario;
import com.model.aldasa.entity.MovimientoBancario;
import com.model.aldasa.repository.DetalleMovimientoBancarioRepository;
import com.model.aldasa.service.DetalleMovimientoBancarioService;

@Service("detalleMovimientoBancarioService")
public class DetalleMovimientoBancarioServiceImpl implements DetalleMovimientoBancarioService {
	
	@Autowired
	private DetalleMovimientoBancarioRepository detalleMovimientoBancarioRepository;

	@Override
	public Optional<DetalleMovimientoBancario> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findById(id);
	}

	@Override
	public DetalleMovimientoBancario save(DetalleMovimientoBancario entity) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.save(entity);
	}

	@Override
	public void delete(DetalleMovimientoBancario entity) {
		// TODO Auto-generated method stub
		detalleMovimientoBancarioRepository.delete(entity);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancario(boolean estado,
			MovimientoBancario movimientoBancario, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancario(estado, movimientoBancario, pageable); 
	}

	@Override
	public BigDecimal totalImporteMovimiento(int idMovimientoBancario) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.totalImporteMovimiento(idMovimientoBancario);
	}

	@Override
	public Page<DetalleMovimientoBancario> findByEstadoAndMovimientoBancarioAndFechaOperacion(boolean estado,
			MovimientoBancario movimientoBancario, Date fechaOperacion, Pageable pageable) {
		// TODO Auto-generated method stub
		return detalleMovimientoBancarioRepository.findByEstadoAndMovimientoBancarioAndFechaOperacion(estado, movimientoBancario, fechaOperacion, pageable); 
	}

	

	


}
