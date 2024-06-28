package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.ComisionProyectoRepository;
//import com.model.aldasa.repository.DetalleComisionesRepository;
import com.model.aldasa.repository.ContratoRepository;
import com.model.aldasa.repository.CuotaRepository;
import com.model.aldasa.repository.PlantillaVentaRepository;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.util.EstadoLote;

@Service("contratoService")
public class ContratoServiceImpl implements ContratoService{
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	@Autowired
	private CuotaRepository cuotaRepository;
	
//	@Autowired
//	private DetalleComisionesRepository comisionesRepository;
	
	@Autowired
	private PlantillaVentaRepository plantillaVentaRepository;
	
	@Autowired
	private ComisionProyectoRepository comisionProyectoRepository;

	@Override
	public Optional<Contrato> findById(Integer id) {
		return contratoRepository.findById(id);
	}

	@Override
	public Contrato saveGeneraComision(Contrato entity) {
//		if(entity.isFirma()) {
//			Comision comision = comisionRepository.findByFechaIniLessThanEqualAndFechaCierreGreaterThanEqual(entity.getFechaVenta(), entity.getFechaVenta());
//			if(comision != null){
//				ComisionProyecto cp = comisionProyectoRepository.findByComisionAndProyectoAndEstado(comision, entity.getLote().getProject(), true);
//				BigDecimal interesContado=new BigDecimal(comision.getComisionContado()); BigDecimal interesCredito=new BigDecimal(comision.getComisionCredito());
//				if(cp!=null) {
//					interesContado = cp.getInteresContado();
//					interesCredito = cp.getInteresCredito();
//				}
//			
//				
//				List<PlantillaVenta> lstPlantilla = plantillaVentaRepository.findByEstadoAndLote("Aprobado", entity.getLote());
//				if(!lstPlantilla.isEmpty()) {
//					PlantillaVenta plantilla = lstPlantilla.get(0);
//					Comisiones comisiones = new Comisiones();
//					comisiones.setLote(entity.getLote());
//					comisiones.setPersonAsesor(plantilla.getPersonAsesor());
//					comisiones.setPersonSupervisor(plantilla.getPersonSupervisor());
//					
//					if (entity.getTipoPago().equals("Contado")) {
//						BigDecimal multiplica = entity.getMontoVenta().multiply(interesContado);
//						comisiones.setComisionAsesor(multiplica.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));	
//					}else {
//						BigDecimal multiplica = entity.getMontoVenta().multiply(interesCredito);
//						comisiones.setComisionAsesor(multiplica.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//					}
//					
//					BigDecimal multiplicaSup = entity.getMontoVenta().multiply(new BigDecimal(comision.getComisionSupervisor()));
//					comisiones.setComisionSupervisor(multiplicaSup.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//					
//					BigDecimal multiplicaSub = entity.getMontoVenta().multiply(new BigDecimal(comision.getSubgerente()));
//					comisiones.setComisionSubgerente(multiplicaSub.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//					comisiones.setEstado(true);
//					comisiones.setComision(comision); 
//					comisiones.setContrato(entity);
//					comisionesRepository.save(comisiones);
//					entity.setComisiones(comisiones);
//				}
//			}
//		}else {
//			if(entity.getComisiones() != null) {
//				entity.getComisiones().setEstado(false);
//				comisionesRepository.save(entity.getComisiones());
//			}
//			
//			entity.setComisiones(null);
//		}
		
//		return contratoRepository.save(entity);
		return null;
	}

	@Override
	public void delete(Contrato entity) {
		contratoRepository.delete(entity); 
	}

	@Override
	public List<Contrato> findAll() {
		// TODO Auto-generated method stub
		return (List<Contrato>) contratoRepository.findAll();
	}

	@Override
	public Page<Contrato> findByEstado(String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstado(status, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(String status, Sucursal sucursal,Project project, String manzana, String numeroLote, String personVenta, boolean cuotaEspecial, String compromisoPago,Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(status, sucursal, project, manzana, numeroLote, personVenta, cuotaEspecial, compromisoPago,pageable);
	}
	
	@Override
	public Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(String status, Sucursal sucursal,Project project, String manzana, String numeroLote, String personVenta,String compromisoPago, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(status, sucursal, project, manzana, numeroLote, personVenta, compromisoPago, pageable);
	}

	@Override
	public Page<Contrato> findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(
			String personVenta, String dni, String estado, boolean cancelacionTotal, Sucursal sucursal,Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(personVenta, dni, estado, cancelacionTotal, sucursal, pageable);
	}

	@Override
	public List<Contrato> findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(String status, Sucursal sucursal, String tipoPago, boolean cancelacionTotal) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(status, sucursal, tipoPago, cancelacionTotal); 
	}

	@Override
	public Contrato save(Contrato entity) {
		// TODO Auto-generated method stub
		return contratoRepository.save(entity);
	}

	@Override
	public Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(String status,
			Sucursal sucursal, String manzana, String numLote,String personVenta, boolean cuotaEspecial, String compromisoPago,Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(status, sucursal, manzana, numLote, personVenta,cuotaEspecial, compromisoPago,pageable);
	}
	
	@Override
	public Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(String status,
			Sucursal sucursal, String manzana, String numLote,String personVenta, String compromisoPago, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(status, sucursal, manzana, numLote, personVenta, compromisoPago,pageable);
	}

	@Override
	public Contrato findByLoteAndEstado(Lote lote, String estado) {
		// TODO Auto-generated method stub
		return contratoRepository.findByLoteAndEstado(lote, estado);
	}

	@Override
	public int totalCuotasEspeciales(String estado) {
		// TODO Auto-generated method stub
		return contratoRepository.totalCuotasEspeciales(estado); 
	}

//	@Override
//	public List<Contrato> findByConMora(String fecha, String estadoContrato) {
//		// TODO Auto-generated method stub
//		List<Contrato> lstContrato = contratoRepository.findByConMora(fecha, estadoContrato);
//	
//	
//	
//		for (Contrato c : lstContrato) {
//	    	
//	    	int numCuotasAtrasadas=0;
//			
//			List<Cuota> lstcuotas = cuotaRepository.findByContratoAndEstado(c, true);
//			if(!lstcuotas.isEmpty()) {
//				for(Cuota cuota : lstcuotas) {
//	//				if(cuota.getNroCuota()!=0) { 
//						if(cuota.getPagoTotal().equals("N")) {	
//							if(cuota.getFechaPago().before(new Date())) {
//								numCuotasAtrasadas++;
//								
//							}
//						}
//	//				}
//				
//				}
//			}
//			
//			c.setCuotasAtrasadas(numCuotasAtrasadas);
//	    }
//		
//		
//		return lstContrato; 
//	}
//
//	@Override
//	public List<Contrato> findByAlDia(String fecha, String estadoContrato) {
//		// TODO Auto-generated method stub
//		List<Contrato> lstContrato = contratoRepository.findByAlDia(fecha, estadoContrato);
//		
//		for (Contrato c : lstContrato) {
//        	
//        	int numCuotasAtrasadas=0;
//			
//    		List<Cuota> lstcuotas = cuotaRepository.findByContratoAndEstado(c, true);
//    		if(!lstcuotas.isEmpty()) {
//    			for(Cuota cuota : lstcuotas) {
////    				if(cuota.getNroCuota()!=0) { 
//    					if(cuota.getPagoTotal().equals("N")) {	
//    						if(cuota.getFechaPago().before(new Date())) {
//    							numCuotasAtrasadas++;
//    							
//    						}
//    					}
////    				}
//    			
//    			}
//    		}
//    		
//    		c.setCuotasAtrasadas(numCuotasAtrasadas);
//        }
//		
//		
//		return lstContrato; 
//	}

//	@Override
//	public Page<Contrato> findByConMora(String fecha, String estadoContrato, int cuotasAtrasadas, Pageable pageable) {
//		// TODO Auto-generated method stub
//		Page<Contrato> contratosPage = contratoRepository.findByConMora(fecha, estadoContrato, pageable);
//	    
//	    List<Contrato> lstContratos = new ArrayList<>(contratosPage.getContent());
//	    
//	    List<Contrato> lstNuevaLista = new ArrayList<>();
//	    
//	    // Asignar valor a la variable transient
//	    for (Contrato contrato : lstContratos) {
//	        int numCuotasAtrasadas = 0;
//	        
//	        List<Cuota> lstCuotas = cuotaRepository.findByContratoAndEstado(contrato, true);
//	        if (!lstCuotas.isEmpty()) {
//	            for (Cuota cuota : lstCuotas) {
//	                if ("N".equals(cuota.getPagoTotal()) && cuota.getFechaPago().before(new Date())) {
//	                    numCuotasAtrasadas++;
//	                }
//	            }
//	        }
//	        
//	        contrato.setCuotasAtrasadas(numCuotasAtrasadas);
//	        
//	        if (cuotasAtrasadas == 0 || cuotasAtrasadas == numCuotasAtrasadas) {
//	            lstNuevaLista.add(contrato);
//	        }
//	    }
//	    
//	    Page<Contrato> newContratosPage = new PageImpl<>(lstNuevaLista, pageable, contratosPage.getTotalElements());
//	    
//	    return newContratosPage;
//	}

//	@Override
//	public Page<Contrato> findByAlDia(String fecha, String estadoContrato, int cuotasAtrasadas, Pageable pageable) {
//		// TODO Auto-generated method stub
//		Page<Contrato> contratosPage = contratoRepository.findByAlDia(fecha, estadoContrato, pageable); 
//		
//		List<Contrato> lstNuevaLista = new ArrayList<>();
//		 // Asignar valor a la variable transient
//        for (Contrato c : contratosPage.getContent()) {
//        	
//        	int numCuotasAtrasadas=0;
//			
//    		List<Cuota> lstcuotas = cuotaRepository.findByContratoAndEstado(c, true);
//    		if(!lstcuotas.isEmpty()) {
//    			for(Cuota cuota : lstcuotas) {
////    				if(cuota.getNroCuota()!=0) { 
//    					if(cuota.getPagoTotal().equals("N")) {	
//    						if(cuota.getFechaPago().before(new Date())) {
//    							numCuotasAtrasadas++;
//    							
//    						}
//    					}
////    				}
//    			
//    			}
//    		}
//        	
//        	
//    		c.setCuotasAtrasadas(numCuotasAtrasadas);
//    		
//    		if(cuotasAtrasadas == 0) {
//    			lstNuevaLista.add(c);
//    		}else {
//    			if(cuotasAtrasadas== numCuotasAtrasadas) {
//    				lstNuevaLista.add(c);
//    			}
//    		}
//        }
//        
//        Page<Contrato> newContratosPage = new PageImpl<>(lstNuevaLista, pageable, contratosPage.getTotalElements());
//		
//		return newContratosPage;
//	}

	@Override
	public List<Contrato> findByEstado(String estado) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstado(estado); 
	}

	@Override
	public List<Contrato> findByEstadoAndCuotasAtrasadas(String estado, int cuotasAtrasadas) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadas(estado, cuotasAtrasadas);
	}

	@Override
	public List<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(String estado, int cuotasAtrasadas,
			String compromisoPago) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(estado, cuotasAtrasadas, compromisoPago); 
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(
			String estado, int cuotasAtrasadas, String personVenta, String proyecto, String manzana, String lote,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estado, cuotasAtrasadas, personVenta, proyecto, manzana, lote, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(
			String estado, int cuotasAtrasadas, String compromisoPago, String personVenta, String proyecto,
			String manzana, String lote, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estado, cuotasAtrasadas, compromisoPago, personVenta, proyecto, manzana, lote, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(
			String estado, int cuotasAtrasadas, String compromisoPago, String personVenta, String proyecto,
			String manzana, String lote, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estado, cuotasAtrasadas, compromisoPago, personVenta, proyecto, manzana, lote, pageable);
	}

	@Override
	public List<Contrato> findByEstadoAndFechaVentaYear(String estado, int year) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndFechaVentaYear(estado,year);
	}

	@Override
	public List<Contrato> findByEstadoAndFechaVentaMonth(String estado, int month) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndFechaVentaMonth(estado, month);
	}

	@Override
	public List<Contrato> findByEstadoAndFechaVentaYearAndFechaVentaMonth(String estado, int year, int month) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndFechaVentaYearAndFechaVentaMonth(estado, year, month); 
	}


}
