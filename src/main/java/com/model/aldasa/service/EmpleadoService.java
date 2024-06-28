package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Team;

public interface EmpleadoService {
	
	Optional<Empleado> findBy(Integer id);
	Empleado save(Empleado empleado);
	void delete(Empleado empleado);
	
	Empleado findByPersonId (Integer id);
	Empleado findByPersonIdException(int idPerson, int idEmpleado);
	Empleado findByPersonDniAndEstado(String dniPerson, boolean estado);
	List<Empleado> findByEstadoAndExternoOrderByPersonSurnamesAsc(boolean estado, boolean externo);
	List<Empleado> findByEstadoOrderByPersonSurnamesAsc(boolean estado);

	Page<Empleado> findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLike(String person, boolean status, String cargo, String area, Pageable pageable);
	Page<Empleado> findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLikeAndTeam(String person, boolean status, String cargo, String area, Team team, Pageable pageable);
	
	
	Page<Empleado> findByEstadoAndSucursalEmpresa(boolean status, Empresa empresa, Pageable pageable);
	Page<Empleado> findByPersonAndEstadoAndSucursalEmpresa(Person person, boolean status, Empresa empresa, Pageable pageable);
	Page<Empleado> findByEstadoAndAreaAndSucursalEmpresa(boolean estado, Area area, Empresa empresa, Pageable pageable);
	Page<Empleado> findByPersonAndEstadoAndAreaAndSucursalEmpresa(Person person, boolean status, Area area, Empresa empresa, Pageable pageable);
	
	List<Empleado> findByEstado(boolean status);
	List<Empleado> findByPersonAndEstado(Person person, boolean status);
	List<Empleado> findByEstadoAndArea(boolean estado, Area area);
	List<Empleado> findByPersonAndEstadoAndArea(Person person, boolean status, Area area);
	List<Empleado> findByEstadoAndTeam(boolean status, Team team);
	
	List<Empleado> findByEstadoAndPlanilla(boolean status, boolean planilla);
	List<Empleado> findByEstadoAndPlanillaAndSucursal(boolean status, boolean planilla, Sucursal sucursal);

	List<Empleado> findByEstadoAndCargoDescripcion(boolean estado, String cargo);

}
