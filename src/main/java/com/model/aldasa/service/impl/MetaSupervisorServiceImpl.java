package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.repository.ComisionSupervisorRepository;
import com.model.aldasa.repository.ConfiguracionComisionRepository;
import com.model.aldasa.repository.EmpleadoRepository;
import com.model.aldasa.repository.MetaSupervisorRepository;
import com.model.aldasa.service.ComisionSupervisorService;
import com.model.aldasa.service.MetaSupervisorService;

@Service("metaSupervisorService")
public class MetaSupervisorServiceImpl  implements MetaSupervisorService {
	
	@Autowired
	private MetaSupervisorRepository metaSupervisorRepository;
	
	@Autowired
	private ComisionSupervisorRepository comisionSupervisorRepository;
	
	@Autowired
	private ConfiguracionComisionRepository configuracionComisionRepository;
	
	
	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Override
	public Optional<MetaSupervisor> findById(Integer id) {
		// TODO Auto-generated method stub
		return metaSupervisorRepository.findById(id);
	}

	@Transactional
	@Override
	public MetaSupervisor save(MetaSupervisor entity) {
		// TODO Auto-generated method stub
		metaSupervisorRepository.save(entity);
		
		ComisionSupervisor comisionSupervisor = comisionSupervisorRepository.findByEstadoAndConfiguracionComisionAndPersonaSupervisor(true, entity.getConfiguracionComision(),entity.getPersonSupervisor());

		if(!entity.isEstado()) {
			comisionSupervisor.setEstado(false);
			comisionSupervisorRepository.save(comisionSupervisor);
		}else {
			if(comisionSupervisor==null) {
				comisionSupervisor = new ComisionSupervisor();
				comisionSupervisor.setPersonaSupervisor(entity.getPersonSupervisor());
				comisionSupervisor.setConfiguracionComision(entity.getConfiguracionComision());
				comisionSupervisor.setBono(BigDecimal.ZERO);
				comisionSupervisor.setMontoComision(BigDecimal.ZERO);
				comisionSupervisor.setNumVendido(0);
				comisionSupervisor.setComisionPorcentaje(entity.getConfiguracionComision().getComisionSupervisor());
				comisionSupervisor.setEstado(true);
				comisionSupervisor.setMeta(entity.getMeta());
				comisionSupervisorRepository.save(comisionSupervisor);
			}else {
				
			}
		}
		
		
		
		//Genera comision para el jefe de ventas
	
			
		int metaJefe =0;
		List<ComisionSupervisor> lstCS = comisionSupervisorRepository.findByEstadoAndConfiguracionComision(true, entity.getConfiguracionComision());
		for(ComisionSupervisor cs: lstCS) {
			metaJefe = metaJefe + cs.getMeta();
			
		}
		
		entity.getConfiguracionComision().setMetajv(metaJefe);
		configuracionComisionRepository.save(entity.getConfiguracionComision()); 
		
		
		
		
		return entity;
	}

	@Override
	public void delete(MetaSupervisor entity) {
		// TODO Auto-generated method stub
		metaSupervisorRepository.delete(entity);
	}

	@Override
	public List<MetaSupervisor> findByConfiguracionComisionAndEstado(ConfiguracionComision comision, boolean estado) {
		// TODO Auto-generated method stub
		return metaSupervisorRepository.findByConfiguracionComisionAndEstado(comision, estado);
	}

	@Override
	public MetaSupervisor findByConfiguracionComisionAndEstadoAndPersonSupervisor(ConfiguracionComision comision, boolean estado,
			Person supervisor) {
		// TODO Auto-generated method stub
		return metaSupervisorRepository.findByConfiguracionComisionAndEstadoAndPersonSupervisor(comision, estado, supervisor);
	}

}
