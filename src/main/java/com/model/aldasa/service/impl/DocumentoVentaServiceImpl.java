package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.repository.ContratoRepository;
import com.model.aldasa.repository.CuotaRepository;
import com.model.aldasa.repository.DetalleDocumentoVentaRepository;
import com.model.aldasa.repository.DocumentoVentaRepository;
import com.model.aldasa.repository.ImagenRepository;
import com.model.aldasa.repository.PlantillaVentaRepository;
import com.model.aldasa.repository.RequerimientoSeparacionRepository;
import com.model.aldasa.repository.SerieDocumentoRepository;
import com.model.aldasa.repository.VoucherRepository;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.util.TipoProductoType;

@Service("documentoVentaService")
public class DocumentoVentaServiceImpl implements DocumentoVentaService{
	
	@Autowired
	private DocumentoVentaRepository documentoVentaRepository;

	@Autowired
	private CuotaRepository cuotaRepository;

	@Autowired
	private DetalleDocumentoVentaRepository detalleDocumentoVentaRepository;

	@Autowired
	private SerieDocumentoRepository serieDocumentoRepository;

	@Autowired
	private ContratoRepository contratoRepository;

	@Autowired
	private RequerimientoSeparacionRepository requerimientoSeparacionRepository;
	
	@Autowired
	private PlantillaVentaRepository plantillaVentaRepository;
	
	@Autowired
	private ImagenRepository imagenRepository;
	
	@Override
	public Optional<DocumentoVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findById(id);
	} 

	@Transactional
	@Override
	public DocumentoVenta save(DocumentoVenta entity, List<DetalleDocumentoVenta> lstDetalleDocumentoVenta, SerieDocumento serieDocumento) {
		SerieDocumento serie = serieDocumentoRepository.findById(serieDocumento.getId()).get();
		String numeroActual = String.format("%0" + serie.getTamanioNumero() + "d", Integer.valueOf(serie.getNumero()));
		Integer aumento = Integer.parseInt(serie.getNumero())+1;
	  			
		
		entity.setNumero(numeroActual); 
		documentoVentaRepository.save(entity);
		for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
			d.setDocumentoVenta(entity);
			d.setEstado(true);
			detalleDocumentoVentaRepository.save(d);
			
			if(d.getCuota()!=null) {
				// ESTO ES PARA ACTUALIZAR PLANTILLA DE VENTA-------
				if(d.getProducto().getTipoProducto().equals(TipoProductoType.INICIAL.getTipo())) {
					List<PlantillaVenta> lstPlantilla = plantillaVentaRepository.findByEstadoAndLote("Aprobado", d.getCuota().getContrato().getLote());
					if(!lstPlantilla.isEmpty()) {
						for(PlantillaVenta p: lstPlantilla) {
							p.setRealizoBoletaInicial(true);
							p.setDocumentoVenta(entity); 
							plantillaVentaRepository.save(p);
						}
					}
				}
				//-----------------------------------FIN
				
				if(!d.getProducto().getTipoProducto().equals(TipoProductoType.INTERES.getTipo())) {
					BigDecimal cuota = d.getCuota().getCuotaTotal().subtract(d.getCuota().getAdelanto());
					BigDecimal cuota2 = BigDecimal.ZERO;
					
					Cuota cuotaselected = d.getCuota();
					for(DetalleDocumentoVenta d2:lstDetalleDocumentoVenta) {
						if(d2.getCuota()!=null) {
							if(cuotaselected.getId().equals(d2.getCuota().getId())) {
								if(d2.getProducto().getTipoProducto().equals(TipoProductoType.INICIAL.getTipo())) {
									cuota2 = cuota2.add(d2.getAmortizacion()).subtract(d2.getAdelanto());
								}else {
									cuota2 = cuota2.add(d2.getAmortizacion()).add(d2.getInteres());
								}
								
								
							}
						}
					}
					
					if(cuota.compareTo(cuota2)==0 ) {
						d.getCuota().setPagoTotal("S");
					}else {
						d.getCuota().setAdelanto(cuota2);
					}
					
					cuotaRepository.save(d.getCuota()); 
				}
				if(d.getCuota().getNroCuota()==0 && d.getCuota().getContrato().getTipoPago().equals("Contado") && d.getCuota().getPagoTotal().equals("S")) {
					d.getCuota().getContrato().setCancelacionTotal(true);							
					contratoRepository.save(d.getCuota().getContrato());
				}
				
			}
			
			if(d.getRequerimientoSeparacion()!=null) {
				d.getRequerimientoSeparacion().setGeneraDocumento(true); 
				d.getRequerimientoSeparacion().setDocumentoVenta(entity);
				requerimientoSeparacionRepository.save(d.getRequerimientoSeparacion());
			}
			if(d.getCuotaPrepago()!=null) {
				d.getCuotaPrepago().setPagoTotal("S");
				cuotaRepository.save(d.getCuotaPrepago());
			}
				
		}  
		
		serie.setNumero(aumento+"");
		serieDocumentoRepository.save(serie);
		
		

		return entity;

		
	}

	@Override
	public void delete(DocumentoVenta entity) {
		// TODO Auto-generated method stub
		documentoVentaRepository.delete(entity);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(estado, empresa, fechaIni, fechaFin);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndFechaEmisionBetween(estado, sucursal, fechaIni, fechaFin);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa,
			Date fechaIni, Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(estado, empresa, fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni,
			Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndFechaEmisionBetween(estado, sucursal, fechaIni, fechaFin, pageable);
	}

	@Override
	public List<DocumentoVenta> findByDocumentoVentaRefAndTipoDocumentoAndEstado(DocumentoVenta documentoVenta, TipoDocumento tipoDocumento, boolean estado) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByDocumentoVentaRefAndTipoDocumentoAndEstado(documentoVenta, tipoDocumento, estado);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionAndEnvioSunat(boolean estado, Sucursal sucursal, Date fechaEmision, boolean envioSunat) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndFechaEmisionAndEnvioSunat(estado, sucursal, fechaEmision, envioSunat);
	}

	@Override
	public DocumentoVenta save(DocumentoVenta entity) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.save(entity);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumento(
			boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat,
			TipoDocumento tipoDocumento, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumento(estado, sucursal, razonSocial, numero, ruc, envioSunat, tipoDocumento, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumento(
			boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc,
			TipoDocumento tipoDocumento, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumento(estado, sucursal, razonSocial, numero, ruc, tipoDocumento, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(boolean estado,
			Sucursal sucursal, String razonSocial, String numero, String ruc, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(estado, sucursal, razonSocial, numero, ruc, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunat(
			boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, boolean envioSunat,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunat(estado, sucursal, razonSocial, numero, ruc, envioSunat, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni,
			Date fechaFin, String user,Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, sucursal, razonSocial, numero, ruc, fechaIni, fechaFin, user,pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc,
			TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin,String user, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, sucursal, razonSocial, numero, ruc, tipoDocumento, fechaIni, fechaFin, user,pageable);
	}

	@Override
	public Page<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin, String user,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(sucursal, razonSocial, numero, ruc, fechaIni, fechaFin, user, pageable);
	}

	@Override
	public Page<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento,
			Date fechaIni, Date fechaFin, String user, Pageable pageable) {
		// TODO Auto-generated method stub 
		return documentoVentaRepository.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(sucursal, razonSocial, numero, ruc, tipoDocumento, fechaIni, fechaFin, user, pageable);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni,
			Date fechaFin, String user) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, sucursal, razonSocial, numero, ruc, fechaIni, fechaFin, user);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc,
			TipoDocumento tipoDocumento, Date fechaIni, Date fechaFin, String user) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(estado, sucursal, razonSocial, numero, ruc, tipoDocumento, fechaIni, fechaFin, user);
	}

	@Override
	public List<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Sucursal sucursal, String razonSocial, String numero, String ruc, Date fechaIni, Date fechaFin,
			String user) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(sucursal, razonSocial, numero, ruc, fechaIni, fechaFin, user);
	}

	@Override
	public List<DocumentoVenta> findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(
			Sucursal sucursal, String razonSocial, String numero, String ruc, TipoDocumento tipoDocumento,
			Date fechaIni, Date fechaFin, String user) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findBySucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumentoAndFechaEmisionBetweenAndUsuarioRegistroUsernameLike(sucursal, razonSocial, numero, ruc, tipoDocumento, fechaIni, fechaFin, user);
	}

	@Override
	public DocumentoVenta anular(DocumentoVenta entity) {
		if(entity.getDocumentoVentaRef()!=null) {
			if(entity.getDocumentoVentaRef().getTipoDocumento().getAbreviatura().equals("C")) {
				entity.getDocumentoVentaRef().setNotacredito(false);
				entity.getDocumentoVentaRef().setNumeroNotaCredito(null);
				
				List<DetalleDocumentoVenta> lstDetalleDocumentoVentaRef = detalleDocumentoVentaRepository.findByDocumentoVentaAndEstado(entity.getDocumentoVentaRef(), true);
				
				for(DetalleDocumentoVenta d:lstDetalleDocumentoVentaRef) {
					// estte recorrido	AQUI HACERLO CON CONSULTA NATIVA
//					d.setEstado(false); 
//					detalleDocumentoVentaService.save(d);
					
					if(d.getRequerimientoSeparacion()!=null) {
						d.getRequerimientoSeparacion().setGeneraDocumento(true);
						d.getRequerimientoSeparacion().setDocumentoVenta(entity.getDocumentoVentaRef());
						requerimientoSeparacionRepository.save(d.getRequerimientoSeparacion());
					}
					if(d.getCuota()!=null) {
						d.getCuota().setPagoTotal("S");
						cuotaRepository.save(d.getCuota());
					}
					
					if(d.getCuotaPrepago()!=null) {
						d.getCuotaPrepago().setPagoTotal("S");
						cuotaRepository.save(d.getCuotaPrepago());
					}
					
					PlantillaVenta plantillaVenta = plantillaVentaRepository.findByDocumentoVenta(entity.getDocumentoVentaRef());
					if(plantillaVenta!=null) {
						plantillaVenta.setDocumentoVenta(entity.getDocumentoVentaRef());
						plantillaVenta.setRealizoBoletaInicial(true); 
						plantillaVentaRepository.save(plantillaVenta);
						
					}
					
					// aqui anulamos las imagenes
					String nombreBusqueda = "%"+entity.getDocumentoVentaRef().getId() +"_%";
					List<Imagen> lstImagen = imagenRepository.findByNombreLikeAndEstado(nombreBusqueda, false);
					for(Imagen i:lstImagen) {
						i.setEstado(true);
						imagenRepository.save(i);
					}
					
					
				}

			}
			
			if(entity.getDocumentoVentaRef().getTipoDocumento().getAbreviatura().equals("D")) {
				entity.getDocumentoVentaRef().setNotaDebito(false);
				entity.getDocumentoVentaRef().setNumeroNotaDebito(null);
			}
			documentoVentaRepository.save(entity.getDocumentoVentaRef());
			
			entity.setEstado(false);
			documentoVentaRepository.save(entity);
			return entity;
		}
		
		entity.setEstado(false);
		documentoVentaRepository.save(entity);
		
		
		List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected = detalleDocumentoVentaRepository.findByDocumentoVentaAndEstado(entity, true);
	
		for(DetalleDocumentoVenta d:lstDetalleDocumentoVentaSelected) {
			// estte recorrido	AQUI HACERLO CON CONSULTA NATIVA
//			d.setEstado(false); 
//			detalleDocumentoVentaService.save(d);
			
			if(d.getRequerimientoSeparacion()!=null) {
				d.getRequerimientoSeparacion().setGeneraDocumento(false);
				d.getRequerimientoSeparacion().setDocumentoVenta(null);
				requerimientoSeparacionRepository.save(d.getRequerimientoSeparacion());
			}
			if(d.getCuota()!=null) {
				d.getCuota().setPagoTotal("N");
				cuotaRepository.save(d.getCuota());
			}
			
			if(d.getCuotaPrepago()!=null) {
				d.getCuotaPrepago().setPagoTotal("N");
				cuotaRepository.save(d.getCuotaPrepago());
			}
			
			PlantillaVenta plantillaVenta = plantillaVentaRepository.findByDocumentoVenta(entity);
			if(plantillaVenta!=null) {
				plantillaVenta.setDocumentoVenta(null);
				plantillaVenta.setRealizoBoletaInicial(false);
				plantillaVentaRepository.save(plantillaVenta);
				
			}
			
			
		}
		
		// aqui anulamos las imagenes
		String nombreBusqueda = "%"+entity.getId() +"_%";
		List<Imagen> lstImagen = imagenRepository.findByNombreLikeAndEstado(nombreBusqueda, true);
		for(Imagen i:lstImagen) {
			i.setEstado(false);
			imagenRepository.save(i);
		}
		
		
		//AQUI ANULAMOS LOS DOCUMENTO DE VENTA ASOSOCIADOS A UNA SEPARACION O INCIAL
		
		return entity;
	}

	@Override
	public DocumentoVenta findByDocumentoVentaRefAndEstado(DocumentoVenta documentoVenta, boolean estado) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByDocumentoVentaRefAndEstado(documentoVenta, estado);
	}

	@Override
	public DocumentoVenta saveNota(DocumentoVenta entity) {
		documentoVentaRepository.save(entity);
		
		List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected = detalleDocumentoVentaRepository.findByDocumentoVentaAndEstado(entity.getDocumentoVentaRef(), true);
		
		for(DetalleDocumentoVenta d:lstDetalleDocumentoVentaSelected) {
			// estte recorrido	AQUI HACERLO CON CONSULTA NATIVA
//			d.setEstado(false); 
//			detalleDocumentoVentaService.save(d);
			
			if(d.getRequerimientoSeparacion()!=null) {
				d.getRequerimientoSeparacion().setGeneraDocumento(false);
				d.getRequerimientoSeparacion().setDocumentoVenta(null);
				requerimientoSeparacionRepository.save(d.getRequerimientoSeparacion());
			}
			if(d.getCuota()!=null) {
				d.getCuota().setPagoTotal("N");
				cuotaRepository.save(d.getCuota());
			}
			
			if(d.getCuotaPrepago()!=null) {
				d.getCuotaPrepago().setPagoTotal("N");
				cuotaRepository.save(d.getCuotaPrepago());
			}
			
			PlantillaVenta plantillaVenta = plantillaVentaRepository.findByDocumentoVenta(entity);
			if(plantillaVenta!=null) {
				plantillaVenta.setDocumentoVenta(null);
				plantillaVenta.setRealizoBoletaInicial(false);
				plantillaVentaRepository.save(plantillaVenta);
				
			}
			
			
		}
		
		
		// TODO Auto-generated method stub
		return entity;
	}

	

 
	

	
}
