package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;

public interface AsistenciaRepository  extends JpaRepository<Asistencia, Integer> {
	
	List<Asistencia> findByEmpleadoAndHoraBetweenAndEstadoOrderByHoraAsc(Empleado empleado, Date horaIni, Date horaFin, boolean estado);
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	List<Asistencia> findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);

	List<Asistencia> findByEmpleadoAndTipoAndHoraBetweenAndEstadoOrderByHoraAsc(Empleado empleado, String tipo, Date horaIni, Date horaFin, boolean estado);
	
	Asistencia findByEmpleadoPerson (Person empleado);

	Page<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado, Pageable pageable);

	
}
