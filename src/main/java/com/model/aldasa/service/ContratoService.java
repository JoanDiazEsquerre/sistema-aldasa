package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;


public interface ContratoService {

	Optional<Contrato> findById(Integer id);
	Contrato saveGeneraComision(Contrato entity);
	Contrato save(Contrato entity);
	void delete(Contrato entity);
	
	Contrato findByLoteAndEstado(Lote lote, String estado);
	
	List<Contrato> findAll();
	List<Contrato> findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(String status, Sucursal sucursal, String tipoPago, boolean cancelacionTotal);
	List<Contrato> findByEstado(String estado);
	
	List<Contrato> findByEstadoAndFechaVentaYear(String estado, int year);
	List<Contrato> findByEstadoAndFechaVentaMonth(String estado, int month);
	List<Contrato> findByEstadoAndFechaVentaYearAndFechaVentaMonth(String estado, int year, int month);
	
	Page<Contrato> findByEstado(String status, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(String status, Sucursal sucursal, Project project, String manzana, String numLote, String personVenta, boolean cuotaEspecial, String compromisoPago, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(String status, Sucursal sucursal, Project project, String manzana, String numLote, String personVenta, String compromisoPago, Pageable pageable);

	
	Page<Contrato> findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(String personVenta, String dni, String estado, boolean cancelacionTotal, Sucursal sucursal,Pageable pageable);

	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(String status, Sucursal sucursal, String manzana, String numLote, String personVenta, boolean cuotaEspecial, String compromisoPago, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(String status, Sucursal sucursal, String manzana, String numLote, String personVenta, String compromisoPago, Pageable pageable);

	int totalCuotasEspeciales(String estado);
	
	
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String personVenta, String proyecto, String manzana, String lote, Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String proyecto, String manzana, String lote,Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String proyecto, String manzana, String lote,Pageable pageable);
	
//	Page<Contrato> findByConMora(String fecha, String estadoContrato, int cuotasAtrasadas, Pageable pageable);
//	Page<Contrato> findByAlDia(String fecha, String estadoContrato, int cuotasAtrasadas, Pageable pageable);
	
//	List<Contrato> findByConMora(String fecha, String estadoContrato);
//	List<Contrato> findByAlDia(String fecha, String estadoContrato);
	
	
	List<Contrato> findByEstadoAndCuotasAtrasadas(String estado, int cuotasAtrasadas);
	List<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(String estado, int cuotasAtrasadas, String compromisoPago);

}
