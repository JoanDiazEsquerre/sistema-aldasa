package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;

public interface CuotaRepository  extends JpaRepository<Cuota, Integer> {
	
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

	@Query("SELECT c FROM Cuota c WHERE c.pagoTotal = :pagoTotal AND c.estado = :estado AND c.contrato.estado = :estadoContrato AND YEAR(c.fechaPago) = :year AND c.contrato.lote.manzana.name like :manzana AND c.contrato.lote.numberLote like :numberLote AND c.contrato.personVenta.surnames like :personVenta AND c.contrato.personVenta.dni like :dni")
    Page<Cuota> findByYear(String pagoTotal, boolean estado, String estadoContrato ,int year, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);
	
	@Query("SELECT c FROM Cuota c WHERE c.pagoTotal = :pagoTotal AND c.estado = :estado AND c.contrato.estado = :estadoContrato AND MONTH(c.fechaPago) = :month AND c.contrato.lote.manzana.name like :manzana AND c.contrato.lote.numberLote like :numberLote AND c.contrato.personVenta.surnames like :personVenta AND c.contrato.personVenta.dni like :dni")
    Page<Cuota> findByMonth(String pagoTotal, boolean estado, String estadoContrato ,int month, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);
	
	@Query("SELECT c FROM Cuota c WHERE c.pagoTotal = :pagoTotal AND c.estado = :estado AND c.contrato.estado = :estadoContrato  AND YEAR(c.fechaPago) = :year AND MONTH(c.fechaPago) = :month AND c.contrato.lote.manzana.name like :manzana AND c.contrato.lote.numberLote like :numberLote AND c.contrato.personVenta.surnames like :personVenta AND c.contrato.personVenta.dni like :dni")
    Page<Cuota> findByYearMonth(String pagoTotal, boolean estado, String estadoContrato ,int year , int month, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);
	
	
	
	//PARA REPORTE DE DEUDAS CON FILTRO DE PROYECTO
	Page<Cuota> findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteProjectAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(String pagoTotal, boolean estado, String estadoContrato, Project proyecto, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);	
	
	@Query("SELECT c FROM Cuota c WHERE c.pagoTotal = :pagoTotal AND c.estado = :estado AND c.contrato.estado = :estadoContrato AND YEAR(c.fechaPago) = :year AND c.contrato.lote.project.id = :idProyecto AND c.contrato.lote.manzana.name like :manzana AND c.contrato.lote.numberLote like :numberLote AND c.contrato.personVenta.surnames like :personVenta AND c.contrato.personVenta.dni like :dni")
    Page<Cuota> findByYearProyecto(String pagoTotal, boolean estado, String estadoContrato ,int year, int idProyecto, String manzana, String numberLote, String personVenta, String dni,Pageable pageable);
	
	@Query("SELECT c FROM Cuota c WHERE c.pagoTotal = :pagoTotal AND c.estado = :estado AND c.contrato.estado = :estadoContrato AND MONTH(c.fechaPago) = :month AND c.contrato.lote.project.id = :idProyecto AND c.contrato.lote.manzana.name like :manzana AND c.contrato.lote.numberLote like :numberLote AND c.contrato.personVenta.surnames like :personVenta AND c.contrato.personVenta.dni like :dni")
    Page<Cuota> findByMonthProyecto(String pagoTotal, boolean estado, String estadoContrato ,int month, int idProyecto, String manzana, String numberLote, String personVenta, String dni,Pageable pageable);
	
	@Query("SELECT c FROM Cuota c WHERE c.pagoTotal = :pagoTotal AND c.estado = :estado AND c.contrato.estado = :estadoContrato  AND YEAR(c.fechaPago) = :year AND MONTH(c.fechaPago) = :month AND c.contrato.lote.project.id = :idProyecto AND c.contrato.lote.manzana.name like :manzana AND c.contrato.lote.numberLote like :numberLote AND c.contrato.personVenta.surnames like :personVenta AND c.contrato.personVenta.dni like :dni")
    Page<Cuota> findByYearMonthProyecto(String pagoTotal, boolean estado, String estadoContrato ,int year , int month, int idProyecto, String manzana, String numberLote, String personVenta, String dni, Pageable pageable);


	
	
//	--------------------------------CALCULAR TOTALES EN REPORTE DE DEUDAS
	@Query(nativeQuery = true,value = "SELECT SUM(IF(c.pagoTotal = 'N', c.cuotaTotal-c.adelanto, c.cuotaTotal)) as suma from cuota c LEFT JOIN contrato cc on cc.id=c.idContrato LEFT JOIN lote l on l.id=cc.idLote LEFT JOIN manzana m on m.id = l.idManzana LEFT JOIN person p on p.id = cc.idPersonVenta "
			+ "where c.estado = :estado and c.pagoTotal = :pagoTotal AND cc.estado = :estadoContrato AND m.name like :manzana AND l.numberLote like :numberLote AND p.surnames like :personVenta AND p.dni like :dni")
	BigDecimal sumaTotal(String pagoTotal, boolean estado, String estadoContrato, String manzana, String numberLote, String personVenta, String dni);
	
	@Query(nativeQuery = true,value = "SELECT SUM(IF(c.pagoTotal = 'N', c.cuotaTotal-c.adelanto, c.cuotaTotal)) as suma from cuota c "
			+ "LEFT JOIN contrato cc on cc.id=c.idContrato "
			+ "LEFT JOIN lote l on l.id=cc.idLote "
			+ "LEFT JOIN manzana m on m.id = l.idManzana "
			+ "LEFT JOIN person p on p.id = cc.idPersonVenta "
			+ "where c.pagoTotal = :pagoTotal AND c.estado = :estado AND cc.estado = :estadoContrato AND YEAR(c.fechaPago) = :year AND m.name like :manzana AND l.numberLote like :numberLote AND p.surnames like :personVenta AND p.dni like :dni")
	BigDecimal sumaTotalYear(String pagoTotal, boolean estado, String estadoContrato ,int year, String manzana, String numberLote, String personVenta, String dni);
	
	@Query(nativeQuery = true,value = "SELECT SUM(IF(c.pagoTotal = 'N', c.cuotaTotal-c.adelanto, c.cuotaTotal)) as suma from cuota c LEFT JOIN contrato cc on cc.id=c.idContrato LEFT JOIN lote l on l.id=cc.idLote LEFT JOIN manzana m on m.id = l.idManzana LEFT JOIN person p on p.id = cc.idPersonVenta "
			+ "where c.pagoTotal = :pagoTotal AND c.estado = :estado AND cc.estado = :estadoContrato AND MONTH(c.fechaPago) = :month AND m.name like :manzana AND l.numberLote like :numberLote AND p.surnames like :personVenta AND p.dni like :dni")
	BigDecimal sumaTotalMonth(String pagoTotal, boolean estado, String estadoContrato ,int month, String manzana, String numberLote, String personVenta, String dni);
	
	@Query(nativeQuery = true,value = "SELECT SUM(IF(c.pagoTotal = 'N', c.cuotaTotal-c.adelanto, c.cuotaTotal)) as suma from cuota c LEFT JOIN contrato cc on cc.id=c.idContrato LEFT JOIN lote l on l.id=cc.idLote LEFT JOIN manzana m on m.id = l.idManzana LEFT JOIN person p on p.id = cc.idPersonVenta "
			+ "where c.pagoTotal = :pagoTotal AND c.estado = :estado AND cc.estado = :estadoContrato AND YEAR(c.fechaPago) = :year AND MONTH(c.fechaPago) = :month AND m.name like :manzana AND l.numberLote like :numberLote AND p.surnames like :personVenta AND p.dni like :dni")
	BigDecimal sumaTotalYearMonth(String pagoTotal, boolean estado, String estadoContrato ,int year , int month, String manzana, String numberLote, String personVenta, String dni);
	
	
	//	--------------------------CALCULAR TOTALES EN REPORTE DE DEUDAS CON FILTRO PROYECTO
	@Query(nativeQuery = true,value = "SELECT SUM(IF(c.pagoTotal = 'N', c.cuotaTotal-c.adelanto, c.cuotaTotal)) as suma from cuota c LEFT JOIN contrato cc on cc.id=c.idContrato LEFT JOIN lote l on l.id=cc.idLote LEFT JOIN manzana m on m.id = l.idManzana LEFT JOIN person p on p.id = cc.idPersonVenta "
			+ "where c.estado = :estado and c.pagoTotal = :pagoTotal AND l.idProject = :idProyecto AND cc.estado = :estadoContrato AND m.name like :manzana AND l.numberLote like :numberLote AND p.surnames like :personVenta AND p.dni like :dni ")
	BigDecimal sumaTotalProyecto(String pagoTotal, boolean estado, String estadoContrato, int idProyecto, String manzana, String numberLote, String personVenta, String dni);
	
	@Query(nativeQuery = true,value = "SELECT SUM(IF(c.pagoTotal = 'N', c.cuotaTotal-c.adelanto, c.cuotaTotal)) as suma from cuota c LEFT JOIN contrato cc on cc.id=c.idContrato LEFT JOIN lote l on l.id=cc.idLote LEFT JOIN manzana m on m.id = l.idManzana LEFT JOIN person p on p.id = cc.idPersonVenta "
			+ "where c.estado = :estado and c.pagoTotal = :pagoTotal AND YEAR(c.fechaPago) = :year AND idProject = :idProyecto AND cc.estado = :estadoContrato AND m.name like :manzana AND l.numberLote like :numberLote AND p.surnames like :personVenta AND p.dni like :dni ")
	BigDecimal sumaTotalYearProyecto(String pagoTotal, boolean estado, String estadoContrato ,int year, int idProyecto, String manzana, String numberLote, String personVenta,String dni);
	
	@Query(nativeQuery = true,value = "SELECT SUM(IF(c.pagoTotal = 'N', c.cuotaTotal-c.adelanto, c.cuotaTotal)) as suma from cuota c LEFT JOIN contrato cc on cc.id=c.idContrato LEFT JOIN lote l on l.id=cc.idLote LEFT JOIN manzana m on m.id = l.idManzana LEFT JOIN person p on p.id = cc.idPersonVenta  "
			+ "where c.estado = :estado and c.pagoTotal = :pagoTotal AND MONTH(c.fechaPago) = :month AND l.idProject = :idProyecto AND cc.estado = :estadoContrato AND m.name like :manzana AND l.numberLote like :numberLote AND p.surnames like :personVenta AND p.dni like :dni ")
	BigDecimal sumaTotalMonthProyecto(String pagoTotal, boolean estado, String estadoContrato ,int month, int idProyecto, String manzana, String numberLote, String personVenta,String dni);
	
	@Query(nativeQuery = true,value = "SELECT SUM(IF(c.pagoTotal = 'N', c.cuotaTotal-c.adelanto, c.cuotaTotal)) as suma from cuota c LEFT JOIN contrato cc on cc.id=c.idContrato LEFT JOIN lote l on l.id=cc.idLote LEFT JOIN manzana m on m.id = l.idManzana LEFT JOIN person p on p.id = cc.idPersonVenta  "
			+ "where c.estado = :estado and c.pagoTotal = :pagoTotal AND YEAR(c.fechaPago) = :year AND MONTH(c.fechaPago) = :month AND l.idProject = :idProyecto AND cc.estado = :estadoContrato AND m.name like :manzana AND l.numberLote like :numberLote AND p.surnames like :personVenta AND p.dni like :dni ")
	BigDecimal sumaTotalYearMonthProyecto(String pagoTotal, boolean estado, String estadoContrato ,int year , int month, int idProyecto, String manzana, String numberLote, String personVenta, String dni);


	// consulta para reporte de grafico DEUDAS
	@Query(nativeQuery = true,value = "SELECT COUNT(*) from cuota c " + 
			"LEFT JOIN contrato ct on ct.id = c.idContrato " + 
			"LEFT JOIN lote l on l.id = ct.idLote " + 
			"where c.estado = :estado and c.pagoTotal = :pagoTotal and YEAR(c.fechaPago)=:year AND MONTH(c.fechaPago) = :month AND l.idProject = :idProject ")
	int totalCuotasReporteGrafico_porProyecto(boolean estado, String pagoTotal, int year, int month, int idProject);
	
	@Query(nativeQuery = true,value = "SELECT COUNT(*) from cuota c where c.estado = :estado and c.pagoTotal = :pagoTotal and YEAR(c.fechaPago)=:year and MONTH(c.fechaPago) = :month")
    int totalCuotasReporteGrafico_general(boolean estado, String pagoTotal, int year, int month);
}
