package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DetalleImagenLote;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.repository.DetalleImagenLoteRepository;
import com.model.aldasa.repository.ImagenRepository;
import com.model.aldasa.service.ImagenService;

@Service("imagenService")
public class ImagenServiceImpl implements ImagenService{
	
	@Autowired
	private ImagenRepository imagenRepository;
	
	@Autowired
	private DetalleImagenLoteRepository detalleImagenLoteRepository;

	@Override
	public Optional<Imagen> findById(Integer id) {
		// TODO Auto-generated method stub
		return imagenRepository.findById(id);
	}

	@Override
	public Imagen save(Imagen entity) {
		// TODO Auto-generated method stub
		return imagenRepository.save(entity);
	}
	
	@Override
	public Imagen save(Imagen entity, List<Lote> lstLotes) {
		
		if(entity.getId()== null) {
			imagenRepository.save(entity);
			
			if(!lstLotes.isEmpty()) {
				for(Lote lt : lstLotes) {
					DetalleImagenLote detalle = new DetalleImagenLote();
					detalle.setImagen(entity);
					detalle.setLote(lt);
					detalle.setEstado(true);
					detalleImagenLoteRepository.save(detalle);
				}
			}
			
		}else {
			imagenRepository.save(entity);
			List<DetalleImagenLote> lstDetalleBusqueda = detalleImagenLoteRepository.findByEstadoAndImagen(true, entity);
			if(!lstDetalleBusqueda.isEmpty()) {
				for(DetalleImagenLote d :lstDetalleBusqueda ) {
					d.setEstado(false);
					detalleImagenLoteRepository.save(d);
				}
			}
			
			if(!lstLotes.isEmpty()) {
				for(Lote lt : lstLotes) {
					DetalleImagenLote busqueda = detalleImagenLoteRepository.findByImagenAndLote(entity, lt);
					if(busqueda!=null) {
						busqueda.setEstado(true);
						detalleImagenLoteRepository.save(busqueda);
					}else {
						DetalleImagenLote detalle = new DetalleImagenLote();
						detalle.setImagen(entity);
						detalle.setLote(lt);
						detalle.setEstado(true);
						detalleImagenLoteRepository.save(detalle);
					}
				}
			}
		}
		
		// TODO Auto-generated method stub
		return entity;
	}

	@Override
	public void delete(Imagen entity) {
		// TODO Auto-generated method stub
		imagenRepository.delete(entity);
	}

	@Override
	public List<Imagen> findByNombreLikeAndEstado(String nombre, boolean estado) {
		// TODO Auto-generated method stub
		return imagenRepository.findByNombreLikeAndEstado(nombre, estado);
	}

	@Override
	public Imagen findByNombre(String nombre) {
		// TODO Auto-generated method stub
		return imagenRepository.findByNombre(nombre);
	}

	@Override
	public List<Imagen> findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(boolean estado, Date fecha, BigDecimal monto, String numOperacion, CuentaBancaria cuentaBancaria) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(estado, fecha, monto, numOperacion, cuentaBancaria);
	}

	@Override
	public List<Imagen> findByDocumentoVentaAndEstadoAndResuelto(DocumentoVenta documentoVenta, boolean estado, boolean resuelto) {
		// TODO Auto-generated method stub
		return imagenRepository.findByDocumentoVentaAndEstadoAndResuelto(documentoVenta, estado, resuelto);
	} 

	@Override
	public List<Imagen> findByEstadoAndCuentaBancariaAndFechaBetweenOrderByFechaDesc(boolean estado,
			CuentaBancaria cuentaBancaria, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndCuentaBancariaAndFechaBetweenOrderByFechaDesc(estado, cuentaBancaria, fechaIni, fechaFin);
	}

	@Override
	public List<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndFechaBetweenOrderByFechaDesc(
			boolean estado, String sucursal, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndFechaBetweenOrderByFechaDesc(estado, sucursal, fechaIni, fechaFin); 
	}

	@Override
	public List<Imagen> findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(
			boolean estado, String fecha, BigDecimal monto, String numOperacion, int idCtaBanc, int id) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(estado, fecha, monto, numOperacion, idCtaBanc, id); 
	}

	@Override
	public List<Imagen> findByLoteAndEstado(Lote lote, boolean estado) {
		// TODO Auto-generated method stub
		return imagenRepository.findByLoteAndEstado(lote, estado); 
	}

	@Override
	public List<Imagen> findByEstadoAndMontoAndNumeroOperacionAndCuentaBancaria(boolean estado,
			BigDecimal monto, String numOperacion, CuentaBancaria cuentaBancaria) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndMontoAndNumeroOperacionAndCuentaBancaria(estado, monto, numOperacion, cuentaBancaria);
	}

	@Override
	public List<Imagen> findByEstadoAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(
			boolean estado, BigDecimal monto, String numOperacion, int idCtaBanc, int id) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(estado, monto, numOperacion, idCtaBanc, id); 
	}

	@Override
	public List<Imagen> findByDocumentoVentaAndEstado(DocumentoVenta documentoVenta, boolean estado) { 
		// TODO Auto-generated method stub
		return imagenRepository.findByDocumentoVentaAndEstado(documentoVenta, estado); 
	}

	@Override
	public Page<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeAndFechaIsNullOrderByIdDesc(
			boolean estado, String sucursal, String numCuenta,String porRegularizar,
			String numeroOperacion, String tipoTransaccion, String comentario, Pageable pageable) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeAndFechaIsNullOrderByIdDesc(estado, sucursal, numCuenta, porRegularizar, numeroOperacion, tipoTransaccion, comentario, pageable);
	}

	@Override
	public Page<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndFechaBetweenAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeOrderByIdDesc(
			boolean estado, String sucursal, String numCuenta, Date fechaIni, Date fechaFin, String porRegularizar,
			String numeroOperacion, String tipoTransaccion, String comentario, Pageable pageable) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndFechaBetweenAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeOrderByIdDesc(estado, sucursal, numCuenta, fechaIni, fechaFin, porRegularizar, numeroOperacion, tipoTransaccion, comentario, pageable);
	}

	@Override
	public Page<Imagen> findByEstadoAndMontoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeAndFechaIsNullOrderByIdDesc(
			boolean estado, BigDecimal monto, String sucursal, String numCuenta, String porRegularizar, String numeroOperacion, String tipoTransaccion, String comentario,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndMontoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeAndFechaIsNullOrderByIdDesc(estado, monto, sucursal, numCuenta, porRegularizar, numeroOperacion, tipoTransaccion, comentario, pageable);
	}

	@Override
	public Page<Imagen> findByEstadoAndMontoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndFechaBetweenAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeOrderByIdDesc(
			boolean estado, BigDecimal monto, String sucursal, String numCuenta, Date fechaIni, Date fechaFin,
			String porRegularizar, String numeroOperacion, String tipoTransaccion, String comentario,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndMontoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndFechaBetweenAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeOrderByIdDesc(estado, monto, sucursal, numCuenta, fechaIni, fechaFin, porRegularizar, numeroOperacion, tipoTransaccion, comentario, pageable);
	}


	
	
}
