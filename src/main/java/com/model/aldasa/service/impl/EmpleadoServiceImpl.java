package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Team;
import com.model.aldasa.repository.EmpleadoRepository;
import com.model.aldasa.service.EmpleadoService;

@Service("empleadoService")
public class EmpleadoServiceImpl implements EmpleadoService {
	
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@Override
	public Optional<Empleado> findBy(Integer id) {
		return empleadoRepository.findById(id);
	}

	
	@Override
	public Empleado save(Empleado entity) {
		return empleadoRepository.save(entity);
	}
	
	@Override
	public void delete(Empleado entity) {
		empleadoRepository.delete(entity);
	}


	@Override
	public Page<Empleado> findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLike(String person, boolean status, String cargo, String area, Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLike(person, status, cargo, area, pageable);
	}


	@Override
	public Empleado findByPersonId(Integer id) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonId(id);
	}


	@Override
	public Empleado findByPersonIdException(int idPerson, int idEmpleado) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonIdException(idPerson, idEmpleado);
	}


	@Override
	public Empleado findByPersonDniAndEstado(String dniPerson, boolean estado) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonDniAndEstado(dniPerson, estado);
	}


	@Override
	public List<Empleado> findByEstadoAndExternoOrderByPersonSurnamesAsc(boolean estado, boolean externo) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndExternoOrderByPersonSurnamesAsc(estado, externo);
	}
	
	@Override
	public List<Empleado> findByEstadoOrderByPersonSurnamesAsc(boolean estado) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoOrderByPersonSurnamesAsc(estado);
	}

	@Override
	public List<Empleado> findByEstado(boolean status) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstado(status);
	}


	@Override
	public List<Empleado> findByPersonAndEstado(Person person, boolean status) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonAndEstado(person, status);
	}


	@Override
	public List<Empleado> findByEstadoAndArea(boolean estado, Area area) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndArea(estado, area);
	}


	@Override
	public List<Empleado> findByPersonAndEstadoAndArea(Person person, boolean status, Area area) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonAndEstadoAndArea(person, status, area);
	}


	@Override
	public Page<Empleado> findByEstadoAndSucursalEmpresa(boolean status, Empresa empresa, Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndSucursalEmpresa(status, empresa, pageable);
	}


	@Override
	public Page<Empleado> findByPersonAndEstadoAndSucursalEmpresa(Person person, boolean status, Empresa empresa,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonAndEstadoAndSucursalEmpresa(person, status, empresa, pageable);
	}


	@Override
	public Page<Empleado> findByEstadoAndAreaAndSucursalEmpresa(boolean estado, Area area, Empresa empresa,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndAreaAndSucursalEmpresa(estado, area, empresa, pageable);
	}


	@Override
	public Page<Empleado> findByPersonAndEstadoAndAreaAndSucursalEmpresa(Person person, boolean status, Area area,
			Empresa empresa, Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonAndEstadoAndAreaAndSucursalEmpresa(person, status, area, empresa, pageable);
	}


	@Override
	public List<Empleado> findByEstadoAndTeam(boolean status, Team team) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndTeam(status, team);
	}


	@Override
	public Page<Empleado> findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLikeAndTeam(
			String person, boolean status, String cargo, String area, Team team, Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLikeAndTeam(person, status, cargo, area, team, pageable); 
	}


	@Override
	public List<Empleado> findByEstadoAndPlanilla(boolean status, boolean planilla) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndPlanilla(status, planilla);
	}


	@Override
	public List<Empleado> findByEstadoAndPlanillaAndSucursal(boolean status, boolean planilla, Sucursal sucursal) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndPlanillaAndSucursal(status, planilla, sucursal);
	}


	@Override
	public List<Empleado> findByEstadoAndCargoDescripcion(boolean estado, String cargo) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndCargoDescripcion(estado, cargo); 
	}



}
