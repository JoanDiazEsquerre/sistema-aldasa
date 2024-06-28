package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.AsistenciaRepository;
import com.model.aldasa.service.AsistenciaService;

@Service("asistenciaService")
public class AsistenciaServiceImpl  implements AsistenciaService  {


	@Autowired
	private AsistenciaRepository asistenciaRepository;
	
	@Override
	public Optional<Asistencia> findById(Integer id) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findById(id);
	}

	@Override
	public Asistencia save(Asistencia entity) {
		// TODO Auto-generated method stub
		return asistenciaRepository.save(entity);
	}

	@Override
	public void delete(Asistencia entity) {
		// TODO Auto-generated method stub
		asistenciaRepository.delete(entity);
	}

	@Override
	public List<Asistencia> findByEmpleadoAndHoraBetweenAndEstadoOrderByHoraAsc(Empleado empleado, Date horaIni, Date horaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoAndHoraBetweenAndEstadoOrderByHoraAsc(empleado, horaIni, horaFin, estado);
	}

	@Override
	public List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni,
			Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstado(dni, tipo, fechaIni, fechaFin, estado);
	}

	@Override
	public List<Asistencia> findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni,
			Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(dni, tipo, fechaIni, fechaFin, estado);
	}

	@Override
	public Asistencia findByEmpleadoPerson(Person empleado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPerson(empleado);
	}

	@Override
	public List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc(String dni, String tipo,
			Date fechaIni, Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc(dni, tipo, fechaIni, fechaFin, estado);
	}

	@Override
	public List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc(String dni, String tipo,
			Date fechaIni, Date fechaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc(dni, tipo, fechaIni, fechaFin, estado);
	}

	@Override
	public Page<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni,
			String tipo, Date fechaIni, Date fechaFin, boolean estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstado(dni, tipo, fechaIni, fechaFin, estado, pageable);
	}

	@Override
	public List<Asistencia> findByEmpleadoAndTipoAndHoraBetweenAndEstadoOrderByHoraAsc(Empleado empleado, String tipo,
			Date horaIni, Date horaFin, boolean estado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoAndTipoAndHoraBetweenAndEstadoOrderByHoraAsc(empleado, tipo, horaIni, horaFin, estado);
	}



}
