package com.model.aldasa.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cuota")
public class Cuota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="nrocuota")
	private Integer nroCuota;
	
	@Column(name="fechapago")
	private Date fechaPago;
	
	@Column(name="cuotasi")
	private BigDecimal cuotaSI;
	
	private BigDecimal interes;
	
	@Column(name="cuotatotal")
	private BigDecimal cuotaTotal;

	private BigDecimal adelanto;
	
	@Column(name="pagototal")
	private String pagoTotal;
	
	@ManyToOne
	@JoinColumn(name="idcontrato")
	private Contrato contrato;
	
	private boolean estado;
	
	private boolean original;
	
	private boolean prepago;

	@ManyToOne
	@JoinColumn(name="idcuotaref")
	private Cuota cuotaRef;
	
	public Cuota() {
		
	}
	
	public Cuota(Cuota entity) {
		super();
		this.nroCuota =entity.getNroCuota();
		this.fechaPago = entity.getFechaPago();
		this.cuotaSI = entity.getCuotaSI();
		this.interes = entity.getInteres();
		this.cuotaTotal = entity.getCuotaTotal();
		this.adelanto = entity.getAdelanto();
		this.pagoTotal = entity.getPagoTotal();
		this.contrato = entity.getContrato();
		this.estado = entity.isEstado();
		this.original = false;
		this.prepago = false;
	}
	
	
	
	
	
	
	public Integer getNroCuota() {
		return nroCuota;
	}
	public void setNroCuota(Integer nroCuota) {
		this.nroCuota = nroCuota;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public BigDecimal getCuotaSI() {
		return cuotaSI;
	}
	public void setCuotaSI(BigDecimal cuotaSI) {
		this.cuotaSI = cuotaSI;
	}
	public BigDecimal getInteres() {
		return interes;
	}
	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}
	public BigDecimal getCuotaTotal() {
		return cuotaTotal;
	}
	public void setCuotaTotal(BigDecimal cuotaTotal) {
		this.cuotaTotal = cuotaTotal;
	}
	public BigDecimal getAdelanto() {
		return adelanto;
	}
	public void setAdelanto(BigDecimal adelanto) {
		this.adelanto = adelanto;
	}
	public String getPagoTotal() {
		return pagoTotal;
	}
	public void setPagoTotal(String pagoTotal) {
		this.pagoTotal = pagoTotal;
	}
	public Contrato getContrato() {
		return contrato;
	}
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public boolean isOriginal() {
		return original;
	}
	public void setOriginal(boolean original) {
		this.original = original;
	}
	public boolean isPrepago() {
		return prepago;
	}
	public void setPrepago(boolean prepago) {
		this.prepago = prepago;
	}
	public Cuota getCuotaRef() {
		return cuotaRef;
	}
	public void setCuotaRef(Cuota cuotaRef) {
		this.cuotaRef = cuotaRef;
	}

}
