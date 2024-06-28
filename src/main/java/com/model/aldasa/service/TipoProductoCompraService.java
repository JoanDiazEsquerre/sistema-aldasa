package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.TipoProductoCompra;

public interface TipoProductoCompraService {
	
	Optional<TipoProductoCompra> findById(Integer id);
	TipoProductoCompra save(TipoProductoCompra entity);
	void delete(TipoProductoCompra entity);

}
