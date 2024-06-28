package com.model.aldasa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detallecomisiones")
public class DetalleComisiones {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name="idcomisiones")
    private Comisiones comisiones;
	
	@ManyToOne
    @JoinColumn(name="idplantillaventa")
    private PlantillaVenta plantillaVenta;
	
	@Column(name="montocomision")
	private BigDecimal montoComision;
	
	@Column(name="montocomisionsupervisor")
	private BigDecimal montoComisionSupervisor;
	
	@Column(name="montocomisionjefeventa")
	private BigDecimal montoComisionJefeVenta;
	
	@Column(name="montocomisionsubgerente")
	private BigDecimal montoComisionSubgerente;
	
	private boolean estado;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Comisiones getComisiones() {
		return comisiones;
	}
	public void setComisiones(Comisiones comisiones) {
		this.comisiones = comisiones;
	}
	public PlantillaVenta getPlantillaVenta() {
		return plantillaVenta;
	}
	public void setPlantillaVenta(PlantillaVenta plantillaVenta) {
		this.plantillaVenta = plantillaVenta;
	}
	public BigDecimal getMontoComision() {
		return montoComision;
	}
	public void setMontoComision(BigDecimal montoComision) {
		this.montoComision = montoComision;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public BigDecimal getMontoComisionSupervisor() {
		return montoComisionSupervisor;
	}
	public void setMontoComisionSupervisor(BigDecimal montoComisionSupervisor) {
		this.montoComisionSupervisor = montoComisionSupervisor;
	}
	public BigDecimal getMontoComisionJefeVenta() {
		return montoComisionJefeVenta;
	}
	public void setMontoComisionJefeVenta(BigDecimal montoComisionJefeVenta) {
		this.montoComisionJefeVenta = montoComisionJefeVenta;
	}
	public BigDecimal getMontoComisionSubgerente() {
		return montoComisionSubgerente;
	}
	public void setMontoComisionSubgerente(BigDecimal montoComisionSubgerente) {
		this.montoComisionSubgerente = montoComisionSubgerente;
	}
	
	
}
