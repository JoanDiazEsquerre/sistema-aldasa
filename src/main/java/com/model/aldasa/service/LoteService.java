package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;

public interface LoteService {
 
	Lote findById(int id);
	Lote save(Lote lote);
	void delete(Lote lote);
	List<Lote> findByStatus(String status);
	List<Lote> findByStatusAndProjectSucursal(String status, Sucursal sucursal);

	List<Lote> findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(Project project, Manzana manzana, String Status);
	List<Lote> findByProjectAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(Project project, String Status);
	Lote findByNumberLoteAndManzanaAndProject (String name, Manzana manzana, Project project);
	Lote findByNumberLoteAndManzanaAndProjectException(String name, int manzana, int project, int idLote);
	
	Page<Lote> findByNumberLoteLikeAndStatusLikeAndProjectSucursal(String numberLote, String status,Sucursal sucursal,Pageable pageable);
	Page<Lote> findByNumberLoteLikeAndStatusLikeAndProjectSucursalAndManzana(String numberLote, String status,Sucursal sucursal, Manzana manzana,Pageable pageable);
	Page<Lote> findByNumberLoteLikeAndStatusLikeAndProjectSucursalAndProject(String numberLote, String status,Sucursal sucursal, Project project, Pageable pageable);
	Page<Lote> findByNumberLoteLikeAndStatusLikeAndProjectSucursalAndManzanaAndProject(String numberLote, String status,Sucursal sucursal, Manzana manzana, Project project,Pageable pageable);
	
	Page<Lote> findByStatusAndProjectSucursalAndRealizoContratoAndNumberLoteLikeAndManzanaNameLikeAndProjectNameLike(String status, Sucursal sucursal, String realizoContrato,String numberLote,  String nameManzana, String proyecto, Pageable pageable);
	Page<Lote> findByStatusAndProjectSucursalAndRealizoContratoAndNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndProject(String status, Sucursal sucursal, String realizoContrato,String numberLote,  String nameManzana, String proyecto, Project project, Pageable pageable);

	//Para mod. comisiones
	Page<Lote> findAllByStatusLikeAndPersonSupervisorDniLikeAndPersonAssessorDniLikeAndFechaVendidoBetween(String status, String dniPersonSupervisor,String dniPersonAsesor, Date fechaIni, Date fechaFin ,Pageable pageable);

	
	//para mod de comisiones
	List<Lote> findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(String Status, Person personSupervisor,String dniAsesor, Date fechaIni, Date fechaFin);
	List<Lote> findByStatusAndPersonAssessorDniAndFechaVendidoBetween(String Status,String dniAsesor, Date fechaIni, Date fechaFin);
	List<Lote> findByStatusAndPersonAssessorDniAndTipoPagoAndFechaVendidoBetween(String Status,String dniAsesor,String tipoPago ,Date fechaIni, Date fechaFin);
	
}
