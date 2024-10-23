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
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(String status, Sucursal sucursal, Project project, String manzana, String numLote, String person, boolean cuotaEspecial, String compromisoPago, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(String status, Sucursal sucursal, Project project, String manzana, String numLote, String person, String compromisoPago, Pageable pageable);

	
	Page<Contrato> findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(String personVenta, String dni, String estado, boolean cancelacionTotal, Sucursal sucursal,Pageable pageable);

	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(String status, Sucursal sucursal, String manzana, String numLote, String personVenta, boolean cuotaEspecial, String compromisoPago, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(String status, Sucursal sucursal, String manzana, String numLote, String personVenta, String compromisoPago, Pageable pageable);

	int totalCuotasEspeciales(String estado);
	
	
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String personVenta, String manzana, String lote, Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String manzana, String lote,Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String manzana, String lote,Pageable pageable);
	
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(String estado, int cuotasAtrasadas, String personVenta, String manzana, String lote, Project proyecto, Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String manzana, String lote, Project proyecto,Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String manzana, String lote, Project proyecto, Pageable pageable);
	
	
	Page<Contrato> findByCliente(String estado, Sucursal sucursal, String manzana, String lote, Person person, Pageable pageable);

//	Page<Contrato> findByConMora(String fecha, String estadoContrato, int cuotasAtrasadas, Pageable pageable);
//	Page<Contrato> findByAlDia(String fecha, String estadoContrato, int cuotasAtrasadas, Pageable pageable);
	
//	List<Contrato> findByConMora(String fecha, String estadoContrato);
//	List<Contrato> findByAlDia(String fecha, String estadoContrato);
	
	long countByEstado(String estado);
	long countByEstadoAndCuotasAtrasadas(String estado, int cuotasAtrasadas);
	long countByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(String estado, int cuotasAtrasadas, String compromisoPago);
	
	long countByEstadoAndLoteProject(String estado, Project project);
	long countByEstadoAndCuotasAtrasadasAndLoteProject(String estado, int cuotasAtrasadas, Project project);
	long countByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndLoteProject(String estado, int cuotasAtrasadas, String compromisoPago, Project project);

}
