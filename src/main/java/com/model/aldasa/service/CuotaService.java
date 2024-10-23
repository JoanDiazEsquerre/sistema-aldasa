package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;

public interface CuotaService {

	Optional< Cuota> findById(Integer id);
	Cuota save( Cuota entity);
	void delete( Cuota entity);
	
	List<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado);
	List<Cuota> findByContratoAndEstado(Contrato contrato, boolean estado);
	List<Cuota> findByContratoAndOriginal(Contrato contrato, boolean original);
	List<Cuota> findByPagoTotalAndEstadoAndContratoOrderById(String pagoTotal, boolean estado, Contrato contrato);
	List<Cuota> findByContrato(Contrato contrato);

	Page<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVenta(String pagoTotal, boolean estado,Person contratoPersonVenta, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectNameAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLikeAndContratoEstado(String pagoTotal, boolean estado, String personSurnames, String personDni, String proyecto, Sucursal sucursal, String numLote, String manzana, String estadoContrato,Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLikeAndContratoEstado(String pagoTotal, boolean estado, String personSurnames, String personDni, Sucursal sucursal,String numLote, String manzana, String estadoContrato, Pageable pageable);

	Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoBetween(String pagoTotal, boolean estado, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoLessThan(String pagoTotal, boolean estado, Date fechaIni, Pageable pageable);

	Page<Cuota> findByPagoTotalAndEstadoAndPrepago(String pagoTotal, boolean estado, boolean prepago, Pageable pageable);


	//PARA REPORTE DE DEUDAS
	Page<Cuota> findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(String pagoTotal, boolean estado, String estadoContrato, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);	
    Page<Cuota> findByYear(String pagoTotal, boolean estado, String estadoContrato ,int year, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);
    Page<Cuota> findByMonth(String pagoTotal, boolean estado, String estadoContrato ,int month, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);
    Page<Cuota> findByYearMonth(String pagoTotal, boolean estado, String estadoContrato ,int year , int month, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);
    
    Page<Cuota> findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteProjectAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(String pagoTotal, boolean estado, String estadoContrato, Project proyecto, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);	
    Page<Cuota> findByYearProyecto(String pagoTotal, boolean estado, String estadoContrato ,int year, int idProyecto, String manzana, String numberLote, String personVenta,String dni, Pageable pageable);
    Page<Cuota> findByMonthProyecto(String pagoTotal, boolean estado, String estadoContrato ,int month, int idProyecto, String manzana, String numberLote, String personVenta,String dni, Pageable pageable);
    Page<Cuota> findByYearMonthProyecto(String pagoTotal, boolean estado, String estadoContrato ,int year , int month, int idProyecto, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);

    
    BigDecimal sumaTotal(String pagoTotal, boolean estado, String estadoContrato, String manzana, String numberLote, String personVenta, String dni);
    BigDecimal sumaTotalYear(String pagoTotal, boolean estado, String estadoContrato ,int year, String manzana, String numberLote, String personVenta, String dni);
    BigDecimal sumaTotalMonth(String pagoTotal, boolean estado, String estadoContrato ,int month, String manzana, String numberLote, String personVenta, String dni);
    BigDecimal sumaTotalYearMonth(String pagoTotal, boolean estado, String estadoContrato ,int year , int month, String manzana, String numberLote, String personVenta, String dni);
    
    BigDecimal sumaTotalProyecto(String pagoTotal, boolean estado, String estadoContrato, int idProyecto, String manzana, String numberLote, String personVenta, String dni);
    BigDecimal sumaTotalYearProyecto(String pagoTotal, boolean estado, String estadoContrato ,int year, int idProyecto, String manzana, String numberLote, String personVenta,String dni);
    BigDecimal sumaTotalMonthProyecto(String pagoTotal, boolean estado, String estadoContrato ,int month, int idProyecto, String manzana, String numberLote, String personVenta,String dni);
    BigDecimal sumaTotalYearMonthProyecto(String pagoTotal, boolean estado, String estadoContrato ,int year , int month, int idProyecto, String manzana, String numberLote, String personVenta, String dni);

    int totalCuotasReporteGrafico_porProyecto(boolean estado, String pagoTotal, int year, int month, int idProject);
    int totalCuotasReporteGrafico_general(boolean estado, String pagoTotal, int year, int month);
}
