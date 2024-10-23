package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;

public interface ContratoRepository extends PagingAndSortingRepository<Contrato, Integer> {
	
	Contrato findByLoteAndEstado(Lote lote, String estado);
	
	List<Contrato> findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(String status, Sucursal sucursal, String tipoPago, boolean cancelacionTotal);
	List<Contrato> findByEstado(String estado);
	
	Page<Contrato> findByEstado(String status, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(String status, Sucursal sucursal, Project project, String manzana, String numLote, String person, boolean cuotaEspecial, String compromisoPago, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(String status, Sucursal sucursal, Project project, String manzana, String numLote, String person, String compromisoPago, Pageable pageable);

	
	Page<Contrato> findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(String personVenta, String dni, String estado, boolean cancelacionTotal, Sucursal sucursal,Pageable pageable);

	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(String status, Sucursal sucursal, String manzana, String numLote, String personaVenta, boolean cuotaEspecial, String compromisoPago ,Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(String status, Sucursal sucursal, String manzana, String numLote, String personVenta, String compromisoPago, Pageable pageable);

	
	
	@Query(nativeQuery = true,value = "SELECT COUNT(*) from contrato c where c.estado = :estado and c.cuotaEspecial = 1")
    int totalCuotasEspeciales(String estado);
		
	
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String personVenta, String manzana, String lote, Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String manzana, String lote,Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String manzana, String lote,Pageable pageable);
	
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(String estado, int cuotasAtrasadas, String personVenta, String manzana, String lote, Project proyecto, Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String manzana, String lote, Project proyecto,Pageable pageable);
	Page<Contrato> findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProject(String estado, int cuotasAtrasadas, String compromisoPago,String personVenta, String manzana, String lote, Project proyecto, Pageable pageable);
	
	
	long countByEstadoAndCuotasAtrasadas(String estado, int cuotasAtrasadas);
	long countByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(String estado, int cuotasAtrasadas, String compromisoPago);
	
	
	@Query("SELECT c FROM Contrato c WHERE c.estado = :status AND c.lote.project.sucursal = :sucursal AND c.lote.manzana.name LIKE :manzana AND c.lote.numberLote LIKE :numLote AND (c.personVenta = :personVenta OR c.personVenta2 = :personVenta OR c.personVenta3 = :personVenta OR c.personVenta4 = :personVenta OR c.personVenta5 = :personVenta)")
	Page<Contrato> findContratosByMultipleCriteria(
		@Param("status") String status,
		@Param("sucursal") Sucursal sucursal,
	    @Param("manzana") String manzana,
	    @Param("numLote") String numLote,
	    @Param("personVenta") Person personVenta,
	    Pageable pageable);
	
//	@Query(value = "SELECT DISTINCT cc.* FROM cuota c " +
//	        "LEFT JOIN contrato cc ON cc.id = c.idContrato " +
//	        "WHERE c.estado = 1 " +
//	        "AND c.fechaPago < :fecha " +
//	        "AND c.pagoTotal = 'N' " +
//	        "AND cc.estado = :estadoContrato",
//	countQuery = "SELECT COUNT(DISTINCT cc.id) FROM cuota c " +
//	        "LEFT JOIN contrato cc ON cc.id = c.idContrato " +
//	        "WHERE c.estado = 1 " +
//	        "AND c.fechaPago < :fecha " +
//	        "AND c.pagoTotal = 'N' " +
//	        "AND cc.estado = :estadoContrato",
//	nativeQuery = true)
//	Page<Contrato> findByConMora(@Param("fecha") String fecha, @Param("estadoContrato") String estadoContrato, Pageable pageable);
	
//	@Query(nativeQuery = true, value = 
//		    "SELECT con.* " +
//		    "FROM contrato con " +
//		    "WHERE con.id NOT IN ( " +
//		    "    SELECT DISTINCT cc.id " +
//		    "    FROM cuota c " +
//		    "    LEFT JOIN contrato cc ON cc.id = c.idContrato " +
//		    "    WHERE c.estado = 1 " +
//		    "      AND c.fechaPago < :fecha " +
//		    "      AND c.pagoTotal = 'N' " +
//		    "      AND cc.estado = :estadoContrato " +
//		    ") " +
//		    "AND con.estado = :estadoContrato")
//		Page<Contrato> findByAlDia(@Param("fecha") String fecha, 
//		                           @Param("estadoContrato") String estadoContrato, 
//		                           Pageable pageable);
	
	
	
//	@Query(nativeQuery = true,value = "SELECT DISTINCT cc.* from cuota c " + 
//	"LEFT JOIN contrato cc on cc.id = c.idContrato " + 
//	"where c.estado = 1 and c.fechaPago < :fecha and c.pagoTotal = 'N' and cc.estado = :estadoContrato")
//List<Contrato> findByConMora(String fecha, String estadoContrato);
//
//@Query(nativeQuery = true,value = "SELECT * from contrato where id not in(SELECT DISTINCT cc.id from cuota c " + 
//	"LEFT JOIN contrato cc on cc.id = c.idContrato " + 
//	"where c.estado = 1 and c.fechaPago < :fecha and c.pagoTotal = 'N' and cc.estado = :estadoContrato) and estado = :estadoContrato")
//List<Contrato> findByAlDia(String fecha, String estadoContrato);
	
	@Query(nativeQuery = true,value = "SELECT c.* FROM contrato c WHERE c.estado = :estado AND YEAR(c.fechaVenta) = :year")
	List<Contrato> findByEstadoAndFechaVentaYear(String estado, int year);
	
	@Query(nativeQuery = true,value = "SELECT c.* FROM contrato c WHERE c.estado = :estado AND MONTH(c.fechaVenta) = :month")
	List<Contrato> findByEstadoAndFechaVentaMonth(String estado, int month);
	
	@Query(nativeQuery = true,value = "SELECT c.* FROM contrato c WHERE c.estado = :estado AND YEAR(c.fechaVenta) = :year AND MONTH(c.fechaVenta) = :month")
	List<Contrato> findByEstadoAndFechaVentaYearAndFechaVentaMonth(String estado, int year, int month);
	
	long countByEstado(String estado);
	
	long countByEstadoAndLoteProject(String estado, Project project);
	long countByEstadoAndCuotasAtrasadasAndLoteProject(String estado, int cuotasAtrasadas, Project project);
	long countByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndLoteProject(String estado, int cuotasAtrasadas, String compromisoPago, Project project);
}
