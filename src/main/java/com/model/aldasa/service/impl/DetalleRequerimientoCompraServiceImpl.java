package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DetalleRequerimientoCompra;
import com.model.aldasa.entity.RequerimientoCompra;
import com.model.aldasa.repository.ContratoRepository;
import com.model.aldasa.repository.DetalleRequerimientoCompraRepository;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.service.DetalleRequerimientoCompraService;

@Service("detalleRequerimientoCompraService")
public class DetalleRequerimientoCompraServiceImpl implements DetalleRequerimientoCompraService {

	@Autowired
	private DetalleRequerimientoCompraRepository detalleRequerimientoCompraRepository;

	@Override
	public Optional<DetalleRequerimientoCompra> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleRequerimientoCompraRepository.findById(id);
	}

	@Override
	public DetalleRequerimientoCompra save(DetalleRequerimientoCompra entity) {
		// TODO Auto-generated method stub
		return detalleRequerimientoCompraRepository.save(entity);
	}

	@Override
	public void delete(DetalleRequerimientoCompra entity) {
		// TODO Auto-generated method stub
		detalleRequerimientoCompraRepository.delete(entity);
	}

	@Override
	public List<DetalleRequerimientoCompra> findByRequerimientoCompraAndEstado(RequerimientoCompra requerimientoCompra, boolean estado) {
		// TODO Auto-generated method stub
		return detalleRequerimientoCompraRepository.findByRequerimientoCompraAndEstado(requerimientoCompra, estado);
	}
}
