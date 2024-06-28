package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.PlantillaVenta;



public interface DetalleComisionesRepository  extends JpaRepository<DetalleComisiones, Integer>{
	
	List<DetalleComisiones> findByEstadoAndComisiones(boolean estado, Comisiones comisiones);
	List<DetalleComisiones> findByEstadoAndComisionesAndPlantillaVentaTipoPago(boolean estado, Comisiones comisiones, String tipoPago);
	
	
	List<DetalleComisiones> findByEstadoAndComisionesComisionSupervisor(boolean estado, ComisionSupervisor comisionSupervisor);
	List<DetalleComisiones> findByEstadoAndComisionesComisionSupervisorConfiguracionComision(boolean estado, ConfiguracionComision configuracioncomision);
	List<DetalleComisiones> findByEstadoAndPlantillaVenta(boolean estado, PlantillaVenta plantillaVenta);

}
