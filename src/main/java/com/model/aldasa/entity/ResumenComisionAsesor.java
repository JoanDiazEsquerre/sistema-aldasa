package com.model.aldasa.entity;

import java.math.BigDecimal;

public class ResumenComisionAsesor {
	
	private Person personAsesor;
	private BigDecimal comision, bono, comisionSupervisor;
	private int totalLotesVendidos;
	
	
	
	public Person getPersonAsesor() {
		return personAsesor;
	}
	public void setPersonAsesor(Person personAsesor) {
		this.personAsesor = personAsesor;
	}
	public BigDecimal getComision() {
		return comision;
	}
	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}
	public BigDecimal getBono() {
		return bono;
	}
	public void setBono(BigDecimal bono) {
		this.bono = bono;
	}
	public int getTotalLotesVendidos() {
		return totalLotesVendidos;
	}
	public void setTotalLotesVendidos(int totalLotesVendidos) {
		this.totalLotesVendidos = totalLotesVendidos;
	}
	public BigDecimal getComisionSupervisor() {
		return comisionSupervisor;
	}
	public void setComisionSupervisor(BigDecimal comisionSupervisor) {
		this.comisionSupervisor = comisionSupervisor;
	}
	
	

}
