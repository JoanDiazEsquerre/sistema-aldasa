package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Person;

public interface LoteRepository extends JpaRepository<Lote, Integer> {

	Lote findById(int id);
	List<Lote> findByStatus(String status);
	List<Lote> findByStatusAndProjectSucursal(String status, Sucursal sucursal);

	Lote findByNumberLoteAndManzanaAndProject (String name, Manzana manzana, Project project);
	
	List<Lote> findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(Project project, Manzana manzana, String status);
	List<Lote> findByProjectAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(Project project, String Status);
	
	@Query(nativeQuery = true,value = "SELECT * FROM lote WHERE numberLote=:name AND idManzana=:manzana AND idProject=:project AND id <> :idLote ")
	Lote findByNumberLoteAndManzanaAndProjectException(String name, int manzana, int project, int idLote);
	
	Page<Lote> findByNumberLoteLikeAndStatusLikeAndProjectSucursal(String numberLote, String status,Sucursal sucursal,Pageable pageable);
	Page<Lote> findByNumberLoteLikeAndStatusLikeAndProjectSucursalAndManzana(String numberLote, String status,Sucursal sucursal, Manzana manzana,Pageable pageable);
	Page<Lote> findByNumberLoteLikeAndStatusLikeAndProjectSucursalAndProject(String numberLote, String status,Sucursal sucursal, Project project, Pageable pageable);
	Page<Lote> findByNumberLoteLikeAndStatusLikeAndProjectSucursalAndManzanaAndProject(String numberLote, String status,Sucursal sucursal, Manzana manzana, Project project,Pageable pageable);
	
	Page<Lote> findByStatusAndProjectSucursalAndRealizoContratoAndNumberLoteLikeAndManzanaNameLikeAndProjectNameLike(String status, Sucursal sucursal, String realizoContrato,String numberLote,  String nameManzana, String proyecto, Pageable pageable);
	Page<Lote> findByStatusAndProjectSucursalAndRealizoContratoAndNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndProject(String status, Sucursal sucursal, String realizoContrato,String numberLote,  String nameManzana, String proyecto, Project project, Pageable pageable);

	//Para mod. comisiones
	Page<Lote> findAllByStatusLikeAndPersonSupervisorDniLikeAndPersonAssessorDniLikeAndFechaVendidoBetween(String status, String dniPersonSupervisor,String dniPersonAsesor, Date fechaIni, Date fechaFin ,Pageable pageable);

	
	List<Lote> findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(String Status, Person personSupervisor, String dniAsesor, Date fechaIni, Date fechaFin);
	List<Lote> findByStatusAndPersonAssessorDniAndFechaVendidoBetween(String Status,String dniAsesor, Date fechaIni, Date fechaFin);
	List<Lote> findByStatusAndPersonAssessorDniAndTipoPagoAndFechaVendidoBetween(String Status,String dniAsesor,String tipoPago ,Date fechaIni, Date fechaFin);



}
