package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.CuotaRepository;
import com.model.aldasa.service.CuotaService;

@Service("cuotaService")
public class CuotaServiceImpl implements CuotaService{

	@Autowired
	private  CuotaRepository cuotaRepository;

	@Override
	public Optional<Cuota> findById(Integer id) {
		// TODO Auto-generated method stub
		return cuotaRepository.findById(id);
	}

	@Override
	public Cuota save(Cuota entity) {
		// TODO Auto-generated method stub
		return cuotaRepository.save(entity);
	}

	@Override
	public void delete(Cuota entity) {
		// TODO Auto-generated method stub
		cuotaRepository.delete(entity);
	}

	@Override
	public List<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstado(pagoTotal, estado);
	}

	@Override
	public List<Cuota> findByContratoAndEstado(Contrato contrato, boolean estado) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByContratoAndEstado(contrato, estado);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstado(pagoTotal, estado, pageable);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectNameAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLikeAndContratoEstado(
			String pagoTotal, boolean estado, String personSurnames, String personDni, String proyecto, Sucursal sucursal,String numLote, String manzana, String estadoContrato, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectNameAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLikeAndContratoEstado(pagoTotal, estado, personSurnames, personDni, proyecto, sucursal,numLote,manzana, estadoContrato, pageable);
	}
	
	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLikeAndContratoEstado(
			String pagoTotal, boolean estado, String personSurnames, String personDni, Sucursal sucursal, String numLote, String manzana,String estadoContrato, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLikeAndContratoEstado(pagoTotal, estado, personSurnames, personDni, sucursal,numLote,manzana, estadoContrato, pageable);
	}

	@Override
	public List<Cuota> findByContratoAndOriginal(Contrato contrato, boolean original) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByContratoAndOriginal(contrato, original);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVenta(String pagoTotal, boolean estado,
			Person contratoPersonVenta, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoPersonVenta(pagoTotal, estado, contratoPersonVenta, pageable);
	}

	@Override
	public List<Cuota> findByPagoTotalAndEstadoAndContratoOrderById(String pagoTotal, boolean estado,
			Contrato contrato) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoOrderById(pagoTotal, estado, contrato);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoBetween(String pagoTotal, boolean estado, Date fechaIni,
			Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndFechaPagoBetween(pagoTotal, estado, fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoLessThan(String pagoTotal, boolean estado, Date fechaIni,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndFechaPagoLessThan(pagoTotal, estado, fechaIni, pageable);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndPrepago(String pagoTotal, boolean estado, boolean prepago, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndPrepago(pagoTotal, estado, prepago, pageable);
	}

	@Override
	public List<Cuota> findByContrato(Contrato contrato) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByContrato(contrato);
	}
	
	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(String pagoTotal, boolean estado, String estadoContrato, String manzana, String numberLote, String personVenta, String dni,Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(pagoTotal, estado, estadoContrato, manzana, numberLote, personVenta, dni, pageable);
	}

	@Override
	public Page<Cuota> findByYear(String pagoTotal, boolean estado, String estadoContrato, int year, String manzana, String numberLote, String personVenta, String dni,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByYear(pagoTotal, estado, estadoContrato, year,manzana, numberLote, personVenta, dni, pageable);
	}

	@Override
	public Page<Cuota> findByMonth(String pagoTotal, boolean estado, String estadoContrato, int month, String manzana, String numberLote, String personVenta, String dni,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByMonth(pagoTotal, estado, estadoContrato, month, manzana, numberLote, personVenta, dni, pageable);
	}

	@Override
	public Page<Cuota> findByYearMonth(String pagoTotal, boolean estado, String estadoContrato, int year, int month, String manzana, String numberLote, String personVenta, String dni,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByYearMonth(pagoTotal, estado, estadoContrato, year, month, manzana, numberLote, personVenta, dni, pageable);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteProjectAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(String pagoTotal, boolean estado,
			String estadoContrato, Project proyecto, String manzana, String numberLote, String personVenta, String dni, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteProjectAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(pagoTotal, estado, estadoContrato, proyecto, manzana, numberLote, personVenta, dni, pageable);
	}

	@Override
	public Page<Cuota> findByYearProyecto(String pagoTotal, boolean estado, String estadoContrato, int year,
			int idProyecto, String manzana, String numberLote, String personVenta, String dni, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByYearProyecto(pagoTotal, estado, estadoContrato, year, idProyecto, manzana, numberLote, personVenta,dni, pageable);
	}

	@Override
	public Page<Cuota> findByMonthProyecto(String pagoTotal, boolean estado, String estadoContrato, int month,
			int idProyecto, String manzana, String numberLote, String personVenta, String dni, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByMonthProyecto(pagoTotal, estado, estadoContrato, month, idProyecto, manzana, numberLote, personVenta, dni, pageable);
	}

	@Override
	public Page<Cuota> findByYearMonthProyecto(String pagoTotal, boolean estado, String estadoContrato, int year,
			int month, int idProyecto, String manzana, String numberLote, String personVenta, String dni, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByYearMonthProyecto(pagoTotal, estado, estadoContrato, year, month, idProyecto, manzana, numberLote, personVenta, dni, pageable);
	}

	@Override
	public BigDecimal sumaTotal(String pagoTotal, boolean estado, String estadoContrato, String manzana,
			String numberLote, String personVenta, String dni) {
		// TODO Auto-generated method stub
		return cuotaRepository.sumaTotal(pagoTotal, estado, estadoContrato, manzana, numberLote, personVenta, dni); 
	}

	@Override
	public BigDecimal sumaTotalYear(String pagoTotal, boolean estado, String estadoContrato, int year, String manzana,
			String numberLote, String personVenta, String dni) {
		// TODO Auto-generated method stub
		return cuotaRepository.sumaTotalYear(pagoTotal, estado, estadoContrato, year, manzana, numberLote, personVenta, dni);
	}

	@Override
	public BigDecimal sumaTotalMonth(String pagoTotal, boolean estado, String estadoContrato, int month, String manzana,
			String numberLote, String personVenta, String dni) {
		// TODO Auto-generated method stub
		return cuotaRepository.sumaTotalMonth(pagoTotal, estado, estadoContrato, month, manzana, numberLote, personVenta, dni);
	}

	@Override
	public BigDecimal sumaTotalYearMonth(String pagoTotal, boolean estado, String estadoContrato, int year, int month,
			String manzana, String numberLote, String personVenta, String dni) {
		// TODO Auto-generated method stub
		return cuotaRepository.sumaTotalYearMonth(pagoTotal, estado, estadoContrato, year, month, manzana, numberLote, personVenta, dni);
	}

	@Override
	public BigDecimal sumaTotalProyecto(String pagoTotal, boolean estado, String estadoContrato, int idProyecto,
			String manzana, String numberLote, String personVenta, String dni) {
		// TODO Auto-generated method stub
		return cuotaRepository.sumaTotalProyecto(pagoTotal, estado, estadoContrato, idProyecto, manzana, numberLote, personVenta, dni);
	}

	@Override
	public BigDecimal sumaTotalYearProyecto(String pagoTotal, boolean estado, String estadoContrato, int year,
			int idProyecto, String manzana, String numberLote, String personVenta, String dni) {
		// TODO Auto-generated method stub
		return cuotaRepository.sumaTotalYearProyecto(pagoTotal, estado, estadoContrato, year, idProyecto, manzana, numberLote, personVenta, dni);
	}

	@Override
	public BigDecimal sumaTotalMonthProyecto(String pagoTotal, boolean estado, String estadoContrato, int month,
			int idProyecto, String manzana, String numberLote, String personVenta, String dni) {
		// TODO Auto-generated method stub
		return cuotaRepository.sumaTotalMonthProyecto(pagoTotal, estado, estadoContrato, month, idProyecto, manzana, numberLote, personVenta, dni);
	}

	@Override
	public BigDecimal sumaTotalYearMonthProyecto(String pagoTotal, boolean estado, String estadoContrato, int year,
			int month, int idProyecto, String manzana, String numberLote, String personVenta, String dni) {
		// TODO Auto-generated method stub
		return cuotaRepository.sumaTotalYearMonthProyecto(pagoTotal, estado, estadoContrato, year, month, idProyecto, manzana, numberLote, personVenta, dni);
	}

	@Override
	public int totalCuotasReporteGrafico_porProyecto(boolean estado, String pagoTotal, int year, int month,
			int idProject) {
		// TODO Auto-generated method stub
		return cuotaRepository.totalCuotasReporteGrafico_porProyecto(estado, pagoTotal, year, month, idProject);
	}

	@Override
	public int totalCuotasReporteGrafico_general(boolean estado, String pagoTotal, int year, int month) {
		// TODO Auto-generated method stub
		return cuotaRepository.totalCuotasReporteGrafico_general(estado, pagoTotal, year, month);
	}



	

	


	


}
