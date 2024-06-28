package com.model.aldasa.entity;

import java.math.BigDecimal;

public class Meta {

	private String supervisor;
	private int lotesVendidos, porcentajeMeta;
	private BigDecimal montoContado, montoInicial, saldoPendiente;
	
	
	

	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public int getLotesVendidos() {
		return lotesVendidos;
	}
	public void setLotesVendidos(int lotesVendidos) {
		this.lotesVendidos = lotesVendidos;
	}
	public int getPorcentajeMeta() {
		return porcentajeMeta;
	}
	public void setPorcentajeMeta(int porcentajeMeta) {
		this.porcentajeMeta = porcentajeMeta;
	}
	public BigDecimal getMontoContado() {
		return montoContado;
	}
	public void setMontoContado(BigDecimal montoContado) {
		this.montoContado = montoContado;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public BigDecimal getSaldoPendiente() {
		return saldoPendiente;
	}
	public void setSaldoPendiente(BigDecimal saldoPendiente) {
		this.saldoPendiente = saldoPendiente;
	}

	
	
	
	
}
