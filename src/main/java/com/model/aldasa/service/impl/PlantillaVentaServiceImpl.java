package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.ComisionSupervisorRepository;
import com.model.aldasa.repository.ComisionesRepository;
import com.model.aldasa.repository.ConfiguracionComisionRepository;
import com.model.aldasa.repository.DetalleComisionesRepository;
import com.model.aldasa.repository.EmpleadoRepository;
import com.model.aldasa.repository.LoteRepository;
import com.model.aldasa.repository.PlantillaVentaRepository;
import com.model.aldasa.repository.RequerimientoSeparacionRepository;
import com.model.aldasa.service.PlantillaVentaService;
import com.model.aldasa.util.EstadoRequerimientoSeparacionType;

@Service("plantillaVentaService")
public class PlantillaVentaServiceImpl implements PlantillaVentaService{

	@Autowired
	private PlantillaVentaRepository plantillaVentaRepository;
	
	@Autowired
	private LoteRepository loteRepository;
	
	@Autowired
	private ComisionesRepository comisionesRepository;
	
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@Autowired
	private DetalleComisionesRepository detalleComisionesRepository;
	
	@Autowired
	private ComisionSupervisorRepository comisionSupervisorRepository;
	
	@Autowired
	private ConfiguracionComisionRepository configuracionComisionRepository;
	
	@Autowired
	private RequerimientoSeparacionRepository requerimientoSeparacionRepository;
	

	@Override
	public Optional<PlantillaVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findById(id);
	}

	@Override
	public PlantillaVenta save(PlantillaVenta entity) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.save(entity);
	}

	@Override
	public void delete(PlantillaVenta entity) {
		// TODO Auto-generated method stub
		plantillaVentaRepository.delete(entity);
	}

	@Override
	public Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(
			String estado, String person, String project, String manzana, String lote, Sucursal sucursal,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(estado, person, project, manzana, lote, sucursal, pageable);
	}

	@Override
	public Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonSupervisor(
			String estado, String person, String project, String manzana, String lote, Sucursal sucursal,
			Person supervisor, Pageable pageable) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonSupervisor(estado, person, project, manzana, lote, sucursal, supervisor, pageable);
	}

	@Override
	public Page<PlantillaVenta> findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonAsesor(
			String estado, String person, String project, String manzana, String lote, Sucursal sucursal,
			Person asesor, Pageable pageable) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonAsesor(estado, person, project, manzana, lote, sucursal, asesor, pageable);
	}

	@Override
	public List<PlantillaVenta> findByEstadoAndLote(String estado, Lote lote) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findByEstadoAndLote(estado, lote); 
	}

	@Transactional
	@Override
	public PlantillaVenta saveAprobarPlantilla(PlantillaVenta plantilla, ConfiguracionComision configuracionComision, ComisionSupervisor comisionSupervisor) {
		if(plantilla.getRequerimientoSeparacion() != null) {
			plantilla.getRequerimientoSeparacion().setEstado(EstadoRequerimientoSeparacionType.TERMINADO.getDescripcion());
			requerimientoSeparacionRepository.save(plantilla.getRequerimientoSeparacion());
		}
		
		
		plantilla.getLote().setStatus("Vendido");
		plantilla.getLote().setPersonVenta(plantilla.getPerson()); 
		plantilla.getLote().setMontoVenta(plantilla.getMontoVenta());
		plantilla.getLote().setTipoPago(plantilla.getTipoPago());
		plantilla.getLote().setPersonAssessor(plantilla.getPersonAsesor());
		plantilla.getLote().setPersonSupervisor(plantilla.getPersonSupervisor());
		plantilla.setFechaVenta(plantilla.getFechaVenta());
		
		if(plantilla.getTipoPago().equals("Crédito")) {
			plantilla.getLote().setNumeroCuota(plantilla.getNumeroCuota());
			plantilla.getLote().setMontoInicial(plantilla.getMontoInicial());
			plantilla.getLote().setInteres(plantilla.getInteres());
		}
		loteRepository.save(plantilla.getLote());
		
		
		
		
		
		Comisiones comAsesor = comisionesRepository.findByEstadoAndComisionSupervisorAndPersonaAsesor(true, comisionSupervisor, plantilla.getPersonAsesor()); 
		
		if(comAsesor == null) {
			comAsesor = new Comisiones();
			comAsesor.setComisionSupervisor(comisionSupervisor);
			comAsesor.setPersonaAsesor(plantilla.getPersonAsesor());
			comAsesor.setBono(BigDecimal.ZERO);
			comAsesor.setMontoComision(BigDecimal.ZERO);
			comAsesor.setNumVendido(0);
			comAsesor.setEstado(true);
			comisionesRepository.save(comAsesor);
		}
		
		// PARA DETALLE COMISIONES
		DetalleComisiones detalleComisionNew = new DetalleComisiones();
		detalleComisionNew.setComisiones(comAsesor);
		detalleComisionNew.setPlantillaVenta(plantilla);
		detalleComisionNew.setMontoComision(BigDecimal.ZERO);
		detalleComisionNew.setEstado(true);
		detalleComisionNew.setMontoComisionJefeVenta(BigDecimal.ZERO);
		
		BigDecimal porcentajeSup = configuracionComision.getComisionSupervisor().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
		BigDecimal montoComisionSup = plantilla.getMontoVenta().multiply(porcentajeSup);
		detalleComisionNew.setMontoComisionSupervisor(montoComisionSup);
		
		BigDecimal porcentajeSub = configuracionComision.getComisionSubgerente().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
		BigDecimal montoComisionSub = plantilla.getMontoVenta().multiply(porcentajeSub);
		detalleComisionNew.setMontoComisionSubgerente(montoComisionSub);
		
		Empleado empleadoAsesor =  empleadoRepository.findByPersonId(plantilla.getPersonAsesor().getId());
		if(empleadoAsesor.isPlanilla()) {
			if(empleadoAsesor.getCargo().getDescripcion().equals("ASESOR DE VENTA")) {
				if(plantilla.getTipoPago().equals("Contado")) {
					BigDecimal porcentaje = configuracionComision.getComisionContado().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					BigDecimal montoComision = plantilla.getMontoVenta().multiply(porcentaje);
					detalleComisionNew.setMontoComision(montoComision);
				}else {
					BigDecimal porcentaje = configuracionComision.getComisionCredito().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					BigDecimal montoComision = plantilla.getMontoVenta().multiply(porcentaje);
					detalleComisionNew.setMontoComision(montoComision);
				}
				detalleComisionesRepository.save(detalleComisionNew);
				
				
				//****************VERIFICA SI LLEGO A LA META CONTADO Y CAMBIA A UN NUEVO PORCENTAJE AL CONTADO
				List<DetalleComisiones> lstComisionesAsesorContado = detalleComisionesRepository.findByEstadoAndComisionesAndPlantillaVentaTipoPago(true, comAsesor, "Contado");
				if(lstComisionesAsesorContado.size() >= configuracionComision.getVentasMetaContado()) {
					for(DetalleComisiones dContado : lstComisionesAsesorContado) {
						BigDecimal porcentaje = configuracionComision.getComisionContadoMeta().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
						BigDecimal montoComision = dContado.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
						dContado.setMontoComision(montoComision);
						detalleComisionesRepository.save(dContado);
					}
				}
				
				
				//*****************ACTUALIZA INFO DEL ASESOR TABLA COMISIONES
				List<DetalleComisiones> lstComisionesAsesor = detalleComisionesRepository.findByEstadoAndComisiones(true, comAsesor);
				comAsesor.setBono(BigDecimal.ZERO);
				comAsesor.setMontoComision(BigDecimal.ZERO); 
				comAsesor.setNumVendido(lstComisionesAsesor.size());
				
				if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentaJunior() && lstComisionesAsesor.size() <= configuracionComision.getMaximoVentaJunior() ) {
					comAsesor.setBono(configuracionComision.getBonoJunior());
				}
				if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentaSenior() && lstComisionesAsesor.size() <= configuracionComision.getMaximoVentaSenior() ) {
					comAsesor.setBono(configuracionComision.getBonoSenior());
				}
				if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentaMaster() && lstComisionesAsesor.size() <= configuracionComision.getMaximoVentaMaster() ) {
					comAsesor.setBono(configuracionComision.getBonoMaster());
				}
				
				BigDecimal montototal= BigDecimal.ZERO;
				for(DetalleComisiones dc:lstComisionesAsesor) {
					montototal = montototal.add(dc.getMontoComision());
				}
				comAsesor.setMontoComision(montototal); 
				comisionesRepository.save(comAsesor);
				
				
			}else {
				if(plantilla.getTipoPago().equals("Contado")) {
					BigDecimal porcentaje = configuracionComision.getComisionContadoEmp().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					BigDecimal montoComision = plantilla.getMontoVenta().multiply(porcentaje);
					detalleComisionNew.setMontoComision(montoComision);
				}else {
					BigDecimal porcentaje = configuracionComision.getComisionCreditoEmp().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					BigDecimal montoComision = plantilla.getMontoVenta().multiply(porcentaje);
					detalleComisionNew.setMontoComision(montoComision);
				}
				detalleComisionesRepository.save(detalleComisionNew);
				
				
				//****************VERIFICA SI LLEGO A LA META CONTADO Y CAMBIA A UN NUEVO PORCENTAJE AL CONTADO
				List<DetalleComisiones> lstComisionesAsesorContado = detalleComisionesRepository.findByEstadoAndComisionesAndPlantillaVentaTipoPago(true, comAsesor, "Contado");
				if(lstComisionesAsesorContado.size() >= configuracionComision.getVentasMetaContadoEmp()) {
					for(DetalleComisiones dContado : lstComisionesAsesorContado) {
						BigDecimal porcentaje = configuracionComision.getComisionContadoMetaEmp().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
						BigDecimal montoComision = dContado.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
						dContado.setMontoComision(montoComision);
						detalleComisionesRepository.save(dContado);
					}
				}
				
				
				//*****************ACTUALIZA INFO DEL ASESOR TABLA COMISIONES
				List<DetalleComisiones> lstComisionesAsesor = detalleComisionesRepository.findByEstadoAndComisiones(true, comAsesor);
				comAsesor.setBono(BigDecimal.ZERO);
				comAsesor.setMontoComision(BigDecimal.ZERO); 
				comAsesor.setNumVendido(lstComisionesAsesor.size());
				
				if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentasEmp() ) {
					comAsesor.setBono(configuracionComision.getBonoVentaEmp());
				}
				
				BigDecimal montototal= BigDecimal.ZERO;
				for(DetalleComisiones dc:lstComisionesAsesor) {
					montototal = montototal.add(dc.getMontoComision());
				}
				comAsesor.setMontoComision(montototal); 
				comisionesRepository.save(comAsesor);
				
			}
			
		}else {
			
			if(plantilla.getTipoPago().equals("Contado")) {
				BigDecimal porcentaje = configuracionComision.getComisionContadoExt().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
				BigDecimal montoComision = plantilla.getMontoVenta().multiply(porcentaje);
				detalleComisionNew.setMontoComision(montoComision);
			}else {
				BigDecimal porcentaje = configuracionComision.getComisionCreditoExt().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
				BigDecimal montoComision = plantilla.getMontoVenta().multiply(porcentaje);
				detalleComisionNew.setMontoComision(montoComision);
			}
			detalleComisionesRepository.save(detalleComisionNew); 
			
			
			
			//****************VERIFICA SI LLEGO A LA META CONTADO Y CAMBIA A UN NUEVO PORCENTAJE AL CONTADO
			List<DetalleComisiones> lstComisionesAsesorContado = detalleComisionesRepository.findByEstadoAndComisionesAndPlantillaVentaTipoPago(true, comAsesor, "Contado");
			if(lstComisionesAsesorContado.size() >= configuracionComision.getVentasMetaContadoExt()) {
				for(DetalleComisiones dContado : lstComisionesAsesorContado) {
					BigDecimal porcentaje = configuracionComision.getComisionContadoMetaExt().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					BigDecimal montoComision = dContado.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
					dContado.setMontoComision(montoComision);
					detalleComisionesRepository.save(dContado);
				}
			}
			
			
			//*****************ACTUALIZA INFO DEL ASESOR TABLA COMISIONES
			List<DetalleComisiones> lstComisionesAsesor = detalleComisionesRepository.findByEstadoAndComisiones(true, comAsesor);
			comAsesor.setBono(BigDecimal.ZERO);
			comAsesor.setMontoComision(BigDecimal.ZERO); 
			comAsesor.setNumVendido(lstComisionesAsesor.size());
			
			if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentasExt() ) {
				comAsesor.setBono(configuracionComision.getBonoVentaExt());
			}
			
			BigDecimal montototal= BigDecimal.ZERO;
			for(DetalleComisiones dc:lstComisionesAsesor) {
				montototal = montototal.add(dc.getMontoComision());
			}
			comAsesor.setMontoComision(montototal); 
			comisionesRepository.save(comAsesor);
		}
		
		
		//Actualiza comisionSupervisor
		List<DetalleComisiones> lstDetalleSupervisor = detalleComisionesRepository.findByEstadoAndComisionesComisionSupervisor(true, comisionSupervisor);
		comisionSupervisor.setNumVendido(lstDetalleSupervisor.size());
		
		if(comisionSupervisor.getNumVendido() >= comisionSupervisor.getMeta()) {
			comisionSupervisor.setBono(configuracionComision.getBonoSupervisor());
 		}
		
		BigDecimal montocomisionsuper = BigDecimal.ZERO;
		for(DetalleComisiones dcs : lstDetalleSupervisor) {
			BigDecimal porcentaje = dcs.getComisiones().getComisionSupervisor().getComisionPorcentaje() .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
			BigDecimal montoComision = dcs.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
			dcs.setMontoComisionSupervisor(montoComision);
			montocomisionsuper = montocomisionsuper.add(montoComision);
		}
		comisionSupervisor.setMontoComision(montocomisionsuper);
		
		comisionSupervisorRepository.save(comisionSupervisor);
		
		
		//actualiza configuracion jefe venta
		List<ComisionSupervisor> lstComisionSupervisor = comisionSupervisorRepository.findByEstadoAndConfiguracionComision(true, configuracionComision);		
		
		int totalVendido = 0;
		for(ComisionSupervisor cs : lstComisionSupervisor) {
			totalVendido = totalVendido+cs.getNumVendido();
		}
		configuracionComision.setNumVendidojv(totalVendido);
		
		//verifica si llega al porcentaje configurado para 	qque el jefe de ventas gane un bono extra siempre y cuando no llegue a la meta del 100%
		BigDecimal calculo = BigDecimal.ZERO;
		int num = configuracionComision.getNumVendidojv();
		
		if(num !=0) {
			BigDecimal multiplicado = new BigDecimal(num).multiply(new BigDecimal(100));    
			calculo = multiplicado.divide(new BigDecimal(configuracionComision.getMetajv()), 2, RoundingMode.HALF_UP);
		}
		
		if(calculo.compareTo(new BigDecimal(100))>=0) {
			configuracionComision.setBonojv(BigDecimal.ZERO);
			configuracionComision.setComisionPorcentajejv(configuracionComision.getComisionJefeVentaMeta()); 
		}else if(calculo.compareTo(configuracionComision.getPorcentajeBono())>=0) {
			configuracionComision.setBonojv(configuracionComision.getBonojv().add(configuracionComision.getBonoJefeVenta()));  
		}
		
		BigDecimal montoSubGerente = BigDecimal.ZERO;
		BigDecimal montoJefeVenta = BigDecimal.ZERO;
		List<DetalleComisiones> detalleComisionesGeneral = detalleComisionesRepository.findByEstadoAndComisionesComisionSupervisorConfiguracionComision(true, configuracionComision);
		configuracionComision.setNumVendidojv(detalleComisionesGeneral.size()); 
		for(DetalleComisiones dc : detalleComisionesGeneral) {
			BigDecimal porcentaje = configuracionComision.getComisionPorcentajejv().divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);
			BigDecimal montoComision = dc.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
			dc.setMontoComisionJefeVenta(montoComision);
			
			montoJefeVenta = montoJefeVenta.add(dc.getMontoComisionJefeVenta());
			detalleComisionesRepository.save(dc);
			
			
			montoSubGerente = montoSubGerente.add(dc.getMontoComisionSubgerente());
		}
		configuracionComision.setMontoComisionjv(montoJefeVenta); 
		configuracionComision.setMontoComisionSubgerente(montoSubGerente);
		
		
		configuracionComisionRepository.save(configuracionComision);
		
		return plantillaVentaRepository.save(plantilla);
	}
	
	public BigDecimal calcularPorcentaje(ComisionSupervisor comisionSupervisor) {
		BigDecimal calculo = BigDecimal.ZERO;
		int num = comisionSupervisor.getNumVendido();
		
		if(num !=0) {
			BigDecimal multiplicado = new BigDecimal(num).multiply(new BigDecimal(100));    
			calculo = multiplicado.divide(new BigDecimal(comisionSupervisor.getMeta()), 2, RoundingMode.HALF_UP);
		}
		return calculo;
	}

	@Override
	public PlantillaVenta anularPlantilla(PlantillaVenta plantilla) {
		List<DetalleComisiones> lstbusquedaComision = detalleComisionesRepository.findByEstadoAndPlantillaVenta(true, plantilla);
		if(!lstbusquedaComision.isEmpty()) {
			for(DetalleComisiones dc: lstbusquedaComision) {
				
				ConfiguracionComision configuracionComision = dc.getComisiones().getComisionSupervisor().getConfiguracionComision();
				Comisiones comAsesor = dc.getComisiones();
				
				Empleado empleadoAsesor =  empleadoRepository.findByPersonId(plantilla.getPersonAsesor().getId());
				if(empleadoAsesor.isPlanilla()) {
					if(empleadoAsesor.getCargo().getDescripcion().equals("ASESOR DE VENTA")) {
						
						dc.setEstado(false);
						detalleComisionesRepository.save(dc);
						
						
						//****************VERIFICA SI LLEGO A LA META CONTADO Y CAMBIA A UN NUEVO PORCENTAJE AL CONTADO
						List<DetalleComisiones> lstComisionesAsesorContado = detalleComisionesRepository.findByEstadoAndComisionesAndPlantillaVentaTipoPago(true, comAsesor, "Contado");
						if(lstComisionesAsesorContado.size() >= configuracionComision.getVentasMetaContado()) {
							for(DetalleComisiones dContado : lstComisionesAsesorContado) {
								BigDecimal porcentaje = configuracionComision.getComisionContadoMeta().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
								BigDecimal montoComision = dContado.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
								dContado.setMontoComision(montoComision);
								detalleComisionesRepository.save(dContado);
							}
						}
						
						
						//*****************ACTUALIZA INFO DEL ASESOR TABLA COMISIONES
						List<DetalleComisiones> lstComisionesAsesor = detalleComisionesRepository.findByEstadoAndComisiones(true, comAsesor);
						comAsesor.setBono(BigDecimal.ZERO);
						comAsesor.setMontoComision(BigDecimal.ZERO); 
						comAsesor.setNumVendido(lstComisionesAsesor.size());
						
						if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentaJunior() && lstComisionesAsesor.size() <= configuracionComision.getMaximoVentaJunior() ) {
							comAsesor.setBono(configuracionComision.getBonoJunior());
						}
						if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentaSenior() && lstComisionesAsesor.size() <= configuracionComision.getMaximoVentaSenior() ) {
							comAsesor.setBono(configuracionComision.getBonoSenior());
						}
						if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentaMaster() && lstComisionesAsesor.size() <= configuracionComision.getMaximoVentaMaster() ) {
							comAsesor.setBono(configuracionComision.getBonoMaster());
						}
						
						BigDecimal montototal= BigDecimal.ZERO;
						for(DetalleComisiones dc2:lstComisionesAsesor) {
							montototal = montototal.add(dc2.getMontoComision());
						}
						comAsesor.setMontoComision(montototal); 
						comisionesRepository.save(comAsesor);
						
						
					}else {
						dc.setEstado(false);
						detalleComisionesRepository.save(dc);
						
						
						//****************VERIFICA SI LLEGO A LA META CONTADO Y CAMBIA A UN NUEVO PORCENTAJE AL CONTADO
						List<DetalleComisiones> lstComisionesAsesorContado = detalleComisionesRepository.findByEstadoAndComisionesAndPlantillaVentaTipoPago(true, comAsesor, "Contado");
						if(lstComisionesAsesorContado.size() >= configuracionComision.getVentasMetaContadoEmp()) {
							for(DetalleComisiones dContado : lstComisionesAsesorContado) {
								BigDecimal porcentaje = configuracionComision.getComisionContadoMetaEmp().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
								BigDecimal montoComision = dContado.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
								dContado.setMontoComision(montoComision);
								detalleComisionesRepository.save(dContado);
							}
						}
						
						
						//*****************ACTUALIZA INFO DEL ASESOR TABLA COMISIONES
						List<DetalleComisiones> lstComisionesAsesor = detalleComisionesRepository.findByEstadoAndComisiones(true, comAsesor);
						comAsesor.setBono(BigDecimal.ZERO);
						comAsesor.setMontoComision(BigDecimal.ZERO); 
						comAsesor.setNumVendido(lstComisionesAsesor.size());
						
						if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentasEmp() ) {
							comAsesor.setBono(configuracionComision.getBonoVentaEmp());
						}
						
						BigDecimal montototal= BigDecimal.ZERO;
						for(DetalleComisiones dc2:lstComisionesAsesor) {
							montototal = montototal.add(dc2.getMontoComision());
						}
						comAsesor.setMontoComision(montototal); 
						comisionesRepository.save(comAsesor);
						
					}
					
				}else {
					
					dc.setEstado(false);
					detalleComisionesRepository.save(dc);
					
					
					
					//****************VERIFICA SI LLEGO A LA META CONTADO Y CAMBIA A UN NUEVO PORCENTAJE AL CONTADO
					List<DetalleComisiones> lstComisionesAsesorContado = detalleComisionesRepository.findByEstadoAndComisionesAndPlantillaVentaTipoPago(true, comAsesor, "Contado");
					if(lstComisionesAsesorContado.size() >= configuracionComision.getVentasMetaContadoExt()) {
						for(DetalleComisiones dContado : lstComisionesAsesorContado) {
							BigDecimal porcentaje = configuracionComision.getComisionContadoMetaExt().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
							BigDecimal montoComision = dContado.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
							dContado.setMontoComision(montoComision);
							detalleComisionesRepository.save(dContado);
						}
					}
					
					
					//*****************ACTUALIZA INFO DEL ASESOR TABLA COMISIONES
					List<DetalleComisiones> lstComisionesAsesor = detalleComisionesRepository.findByEstadoAndComisiones(true, comAsesor);
					comAsesor.setBono(BigDecimal.ZERO);
					comAsesor.setMontoComision(BigDecimal.ZERO); 
					comAsesor.setNumVendido(lstComisionesAsesor.size());
					
					if(lstComisionesAsesor.size() >= configuracionComision.getMinimoVentasExt() ) {
						comAsesor.setBono(configuracionComision.getBonoVentaExt());
					}
					
					BigDecimal montototal= BigDecimal.ZERO;
					for(DetalleComisiones dc2:lstComisionesAsesor) {
						montototal = montototal.add(dc2.getMontoComision());
					}
					comAsesor.setMontoComision(montototal); 
					comisionesRepository.save(comAsesor);
				}
				
				
				
				
				
				
				
				ComisionSupervisor comisionSupervisor = dc.getComisiones().getComisionSupervisor();
				//Actualiza comisionSupervisor
				List<DetalleComisiones> lstDetalleSupervisor = detalleComisionesRepository.findByEstadoAndComisionesComisionSupervisor(true, comisionSupervisor);
				comisionSupervisor.setNumVendido(lstDetalleSupervisor.size());
				
				if(comisionSupervisor.getNumVendido() >= comisionSupervisor.getMeta()) {
					comisionSupervisor.setBono(configuracionComision.getBonoSupervisor());
		 		}
				
				BigDecimal montocomisionsuper = BigDecimal.ZERO;
				for(DetalleComisiones dcs : lstDetalleSupervisor) {
					BigDecimal porcentaje = dcs.getComisiones().getComisionSupervisor().getComisionPorcentaje() .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
					BigDecimal montoComision = dcs.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
					dcs.setMontoComisionSupervisor(montoComision);
					montocomisionsuper = montocomisionsuper.add(montoComision);
				}
				comisionSupervisor.setMontoComision(montocomisionsuper);
				
				comisionSupervisorRepository.save(comisionSupervisor);
				
				
				//actualiza configuracion jefe venta
				List<ComisionSupervisor> lstComisionSupervisor = comisionSupervisorRepository.findByEstadoAndConfiguracionComision(true, configuracionComision);		
				
				int totalVendido = 0;
				for(ComisionSupervisor cs : lstComisionSupervisor) {
					totalVendido = totalVendido+cs.getNumVendido();
				}
				configuracionComision.setNumVendidojv(totalVendido);
				
				//verifica si llega al porcentaje configurado para 	qque el jefe de ventas gane un bono extra siempre y cuando no llegue a la meta del 100%
				BigDecimal calculo = BigDecimal.ZERO;
				int num = configuracionComision.getNumVendidojv();
				
				if(num !=0) {
					BigDecimal multiplicado = new BigDecimal(num).multiply(new BigDecimal(100));    
					calculo = multiplicado.divide(new BigDecimal(configuracionComision.getMetajv()), 2, RoundingMode.HALF_UP);
				}
				
				if(calculo.compareTo(new BigDecimal(100))>=0) {
					configuracionComision.setBonojv(BigDecimal.ZERO);
					configuracionComision.setComisionPorcentajejv(configuracionComision.getComisionJefeVentaMeta()); 
				}else if(calculo.compareTo(configuracionComision.getPorcentajeBono())>=0) {
					configuracionComision.setBonojv(configuracionComision.getBonojv().add(configuracionComision.getBonoJefeVenta()));  
				}
				
				BigDecimal montoSubGerente = BigDecimal.ZERO;
				BigDecimal montoJefeVenta = BigDecimal.ZERO;
				List<DetalleComisiones> detalleComisionesGeneral = detalleComisionesRepository.findByEstadoAndComisionesComisionSupervisorConfiguracionComision(true, configuracionComision);
				configuracionComision.setNumVendidojv(detalleComisionesGeneral.size()); 
				for(DetalleComisiones dc2 : detalleComisionesGeneral) {
					BigDecimal porcentaje = configuracionComision.getComisionPorcentajejv().divide(new BigDecimal(100), 3, RoundingMode.HALF_UP);
					BigDecimal montoComision = dc2.getPlantillaVenta().getMontoVenta().multiply(porcentaje);
					dc2.setMontoComisionJefeVenta(montoComision);
					
					montoJefeVenta = montoJefeVenta.add(dc2.getMontoComisionJefeVenta());
					detalleComisionesRepository.save(dc2);
					
					
					montoSubGerente = montoSubGerente.add(dc2.getMontoComisionSubgerente());
				}
				configuracionComision.setMontoComisionjv(montoJefeVenta); 
				configuracionComision.setMontoComisionSubgerente(montoSubGerente);
				
				
				configuracionComisionRepository.save(configuracionComision);
				
			}
		}
		
		
		
		
		
		
		
		
			
		plantilla.setEstado("Anulado");
		return plantillaVentaRepository.save(plantilla); 
	}

	
}
