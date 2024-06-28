package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.DetalleRequerimientoCompra;
import com.model.aldasa.entity.RequerimientoCompra;
import com.model.aldasa.entity.SerieDocumento;

public interface RequerimientoCompraService {

	Optional<RequerimientoCompra> findById(Integer id);
	RequerimientoCompra save(RequerimientoCompra entity);
	RequerimientoCompra save(RequerimientoCompra entity, List<DetalleRequerimientoCompra> lstDetalle, SerieDocumento serieDocumento);
	void delete(RequerimientoCompra entity);
	
	Page<RequerimientoCompra> findByEstado(String estado, Pageable pageable);

}
