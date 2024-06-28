package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.ContratoArchivo;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.ComisionProyectoRepository;
import com.model.aldasa.repository.ContratoArchivoRepository;
//import com.model.aldasa.repository.DetalleComisionesRepository;
import com.model.aldasa.repository.ContratoRepository;
import com.model.aldasa.repository.PlantillaVentaRepository;
import com.model.aldasa.service.ContratoArchivoService;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.util.EstadoLote;

@Service("contratoArchivoService")
public class ContratoArchivoServiceImpl implements ContratoArchivoService{
	
	@Autowired
	private ContratoArchivoRepository contratoArchivoRepository;

	@Override
	public Optional<ContratoArchivo> findById(Integer id) {
		// TODO Auto-generated method stub
		return contratoArchivoRepository.findById(id);
	}

	@Override
	public ContratoArchivo save(ContratoArchivo entity) {
		// TODO Auto-generated method stub
		return contratoArchivoRepository.save(entity);
	}

	@Override
	public void delete(ContratoArchivo entity) {
		// TODO Auto-generated method stub
		contratoArchivoRepository.delete(entity);
	}

	@Override
	public List<ContratoArchivo> findByEstadoAndContrato(boolean estado, Contrato contrato) {
		// TODO Auto-generated method stub
		return contratoArchivoRepository.findByEstadoAndContrato(estado, contrato); 
	}

	@Override
	public ContratoArchivo findByEstadoAndNombre(boolean estado, String nombre) {
		// TODO Auto-generated method stub
		return contratoArchivoRepository.findByEstadoAndNombre(estado, nombre); 
	}
	



	


}
