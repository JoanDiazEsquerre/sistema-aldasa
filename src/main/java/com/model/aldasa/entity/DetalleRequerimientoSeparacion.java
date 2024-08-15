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
@Table(name = "detallerequerimientoseparacion")
public class DetalleRequerimientoSeparacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idrequerimientoseparacion")
	private RequerimientoSeparacion requerimientoSeparacion;
	
	private BigDecimal monto;
	
	@Column(name="pagototal")
	private String pagoTotal;
	
	private boolean estado;
	
	private Date fecha;

	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public RequerimientoSeparacion getRequerimientoSeparacion() {
		return requerimientoSeparacion;
	}
	public void setRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion) {
		this.requerimientoSeparacion = requerimientoSeparacion;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getPagoTotal() {
		return pagoTotal;
	}
	public void setPagoTotal(String pagoTotal) {
		this.pagoTotal = pagoTotal;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}
