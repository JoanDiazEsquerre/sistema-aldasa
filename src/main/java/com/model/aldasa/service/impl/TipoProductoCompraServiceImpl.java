package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.TipoProductoCompra;
import com.model.aldasa.repository.TipoProductoCompraRepository;
import com.model.aldasa.service.TipoProductoCompraService;

@Service("tipoProductoCompraService")
public class TipoProductoCompraServiceImpl implements TipoProductoCompraService{

	@Autowired
	private TipoProductoCompraRepository tipoProductoCompraRepository;

	@Override
	public Optional<TipoProductoCompra> findById(Integer id) {
		// TODO Auto-generated method stub
		return tipoProductoCompraRepository.findById(id);
	}

	@Override
	public TipoProductoCompra save(TipoProductoCompra entity) {
		// TODO Auto-generated method stub
		return tipoProductoCompraRepository.save(entity);
	}

	@Override
	public void delete(TipoProductoCompra entity) {
		// TODO Auto-generated method stub
		tipoProductoCompraRepository.delete(entity);
	}
}
