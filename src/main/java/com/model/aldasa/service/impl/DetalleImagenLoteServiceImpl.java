package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DetalleImagenLote;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.repository.DetalleImagenLoteRepository;
import com.model.aldasa.service.DetalleImagenLoteService;

@Service("detalleImagenLoteService")
public class DetalleImagenLoteServiceImpl implements DetalleImagenLoteService{

	@Autowired
	private  DetalleImagenLoteRepository  detalleImagenLoteRepository;

	@Override
	public Optional<DetalleImagenLote> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleImagenLoteRepository.findById(id);
	}

	@Override
	public DetalleImagenLote save(DetalleImagenLote entity) {
		// TODO Auto-generated method stub
		return detalleImagenLoteRepository.save(entity);
	}

	@Override
	public void delete(DetalleImagenLote entity) {
		// TODO Auto-generated method stub
		detalleImagenLoteRepository.delete(entity);
	}

	@Override
	public DetalleImagenLote findByEstadoAndImagenAndLote(boolean estado, Imagen imagen, Lote lote) {
		// TODO Auto-generated method stub
		return detalleImagenLoteRepository.findByEstadoAndImagenAndLote(estado, imagen, lote);
	}

	@Override
	public List<DetalleImagenLote> findByEstadoAndImagen(boolean estado, Imagen imagen) {
		// TODO Auto-generated method stub
		return detalleImagenLoteRepository.findByEstadoAndImagen(estado, imagen); 
	}

	@Override
	public DetalleImagenLote findByImagenAndLote(Imagen imagen, Lote lote) {
		// TODO Auto-generated method stub
		return detalleImagenLoteRepository.findByImagenAndLote(imagen, lote);
	}

	@Override
	public List<DetalleImagenLote> findByEstadoAndLoteAndImagenEstadoAndImagenResuelto(boolean estado, Lote lote, boolean estadoImagen, boolean resuelto) {
		// TODO Auto-generated method stub
		return detalleImagenLoteRepository.findByEstadoAndLoteAndImagenEstadoAndImagenResuelto(estado, lote, estadoImagen, resuelto);
	}


	
}
