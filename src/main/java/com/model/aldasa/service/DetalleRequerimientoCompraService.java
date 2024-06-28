package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.DetalleRequerimientoCompra;
import com.model.aldasa.entity.RequerimientoCompra;
import com.model.aldasa.entity.Unidad;

public interface DetalleRequerimientoCompraService {

	Optional<DetalleRequerimientoCompra> findById(Integer id);
	DetalleRequerimientoCompra save(DetalleRequerimientoCompra entity);
	void delete(DetalleRequerimientoCompra entity);
	
	List<DetalleRequerimientoCompra> findByRequerimientoCompraAndEstado(RequerimientoCompra requerimientoCompra, boolean estado);

}
