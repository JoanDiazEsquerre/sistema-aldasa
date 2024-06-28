package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.DetalleImagenLote;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Lote;

public interface DetalleImagenLoteRepository extends JpaRepository<DetalleImagenLote, Integer>{
	
	DetalleImagenLote findByEstadoAndImagenAndLote(boolean estado, Imagen imagen, Lote lote);
	DetalleImagenLote findByImagenAndLote(Imagen imagen, Lote lote);
	
	List<DetalleImagenLote> findByEstadoAndImagen(boolean estado, Imagen imagen);
	List<DetalleImagenLote> findByEstadoAndLoteAndImagenEstadoAndImagenResuelto(boolean estado, Lote lote, boolean estadoImagen, boolean resuelto); 
}
