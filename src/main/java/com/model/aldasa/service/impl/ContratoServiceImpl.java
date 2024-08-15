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


	@Override
	public List<Contrato> findByEstado(String estado) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstado(estado); 
	}

	@Override
	public long countByEstadoAndCuotasAtrasadas(String estado, int cuotasAtrasadas) {
		// TODO Auto-generated method stub
		return contratoRepository.countByEstadoAndCuotasAtrasadas(estado, cuotasAtrasadas);
	}

	@Override
	public long countByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(String estado, int cuotasAtrasadas,
			String compromisoPago) {
		// TODO Auto-generated method stub
		return contratoRepository.countByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(estado, cuotasAtrasadas, compromisoPago); 
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(
			String estado, int cuotasAtrasadas, String personVenta, String manzana, String lote,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estado, cuotasAtrasadas, personVenta, manzana, lote, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(
			String estado, int cuotasAtrasadas, String compromisoPago, String personVenta, String manzana, String lote, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estado, cuotasAtrasadas, compromisoPago, personVenta, manzana, lote, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(
			String estado, int cuotasAtrasadas, String compromisoPago, String personVenta, String manzana, String lote, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estado, cuotasAtrasadas, compromisoPago, personVenta, manzana, lote, pageable);
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

	@Override
	public long countByEstado(String estado) {
		// TODO Auto-generated method stub
		return contratoRepository.countByEstado(estado);
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(
			String estado, int cuotasAtrasadas, String personVenta, String manzana, String lote, Project proyecto,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(estado, cuotasAtrasadas, personVenta, manzana, lote, proyecto, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(
			String estado, int cuotasAtrasadas, String compromisoPago, String personVenta, String manzana, String lote,
			Project proyecto, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(estado, cuotasAtrasadas, compromisoPago, personVenta, manzana, lote, proyecto, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(
			String estado, int cuotasAtrasadas, String compromisoPago, String personVenta, String manzana, String lote,
			Project proyecto, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(estado, cuotasAtrasadas, compromisoPago, personVenta, manzana, lote, proyecto, pageable);
	}

	@Override
	public long countByEstadoAndLoteProject(String estado, Project project) {
		// TODO Auto-generated method stub
		return contratoRepository.countByEstadoAndLoteProject(estado, project); 
	}

	@Override
	public long countByEstadoAndCuotasAtrasadasAndLoteProject(String estado, int cuotasAtrasadas, Project project) {
		// TODO Auto-generated method stub
		return contratoRepository.countByEstadoAndCuotasAtrasadasAndLoteProject(estado, cuotasAtrasadas, project);
	}

	@Override
	public long countByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndLoteProject(String estado,
			int cuotasAtrasadas, String compromisoPago, Project project) {
		// TODO Auto-generated method stub
		return contratoRepository.countByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndLoteProject(estado, cuotasAtrasadas, compromisoPago, project); 
	}


}
