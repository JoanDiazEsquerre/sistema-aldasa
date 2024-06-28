package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DetalleRequerimientoCompra;
import com.model.aldasa.entity.RequerimientoCompra;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.repository.DetalleRequerimientoCompraRepository;
import com.model.aldasa.repository.RequerimientoCompraRepository;
import com.model.aldasa.repository.SerieDocumentoRepository;
import com.model.aldasa.service.RequerimientoCompraService;

@Service("requerimientoCompraService")
public class RequerimientoCompraServiceImpl implements RequerimientoCompraService {

	@Autowired
	private RequerimientoCompraRepository requerimientoCompraRepository;
	
	@Autowired
	private DetalleRequerimientoCompraRepository detalleRequerimientoCompraRepository;
	
	@Autowired
	private SerieDocumentoRepository serieDocumentoRepository;

	@Override
	public Optional<RequerimientoCompra> findById(Integer id) {
		// TODO Auto-generated method stub
		return requerimientoCompraRepository.findById(id);
	}

	@Override
	public RequerimientoCompra save(RequerimientoCompra entity) {
		// TODO Auto-generated method stub
		return requerimientoCompraRepository.save(entity);
	}
	
	@Transactional
	@Override
	public RequerimientoCompra save(RequerimientoCompra entity, List<DetalleRequerimientoCompra> lstDetalle, SerieDocumento serieDocumento) {
		SerieDocumento serie = serieDocumentoRepository.findById(serieDocumento.getId()).get();
		String numeroActual = String.format("%0" + serie.getTamanioNumero() + "d", Integer.valueOf(serie.getNumero()));
		Integer aumento = Integer.parseInt(serie.getNumero())+1;
		
		entity.setNumero(serie.getSerie()+"-"+numeroActual); 
		requerimientoCompraRepository.save(entity);
		
		for(DetalleRequerimientoCompra d:lstDetalle) {
			d.setRequerimientoCompra(entity);
			d.setEstado(true);
			detalleRequerimientoCompraRepository.save(d);
		}
		
		serie.setNumero(aumento+"");
		serieDocumentoRepository.save(serie);
		
		return entity;
	}

	@Override
	public void delete(RequerimientoCompra entity) {
		// TODO Auto-generated method stub
		requerimientoCompraRepository.delete(entity);
	}

	@Override
	public Page<RequerimientoCompra> findByEstado(String estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return requerimientoCompraRepository.findByEstado(estado, pageable);
	}

	
}
