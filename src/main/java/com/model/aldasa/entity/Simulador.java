package com.model.aldasa.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Simulador {

	private String nroCuota;
	private BigDecimal inicial, cuotaSI, interes, cuotaTotal;
	private Date fechaPago;

	
	
	
	public String getNroCuota() {
		return nroCuota;
	}
	public void setNroCuota(String nroCuota) {
		this.nroCuota = nroCuota;
	}
	
	public BigDecimal getInicial() {
		return inicial;
	}
	public void setInicial(BigDecimal inicial) {
		this.inicial = inicial;
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
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}	
	
	
}
