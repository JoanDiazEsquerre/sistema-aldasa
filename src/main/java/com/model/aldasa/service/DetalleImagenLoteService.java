package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.DetalleImagenLote;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Lote;

public interface DetalleImagenLoteService {

	Optional<DetalleImagenLote> findById(Integer id);
	DetalleImagenLote save(DetalleImagenLote entity);
	void delete(DetalleImagenLote entity);
	
	
	DetalleImagenLote findByEstadoAndImagenAndLote(boolean estado, Imagen imagen, Lote lote);
	DetalleImagenLote findByImagenAndLote(Imagen imagen, Lote lote);
	
	List<DetalleImagenLote> findByEstadoAndImagen(boolean estado, Imagen imagen);
	List<DetalleImagenLote> findByEstadoAndLoteAndImagenEstadoAndImagenResuelto(boolean estado, Lote lote, boolean estadoImagen, boolean resuelto); 

}
