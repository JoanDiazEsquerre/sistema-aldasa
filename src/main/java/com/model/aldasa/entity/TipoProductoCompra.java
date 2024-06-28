package com.model.aldasa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tipoproductocompra")
public class TipoProductoCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String descripcion;
	
	@ManyToOne
	@JoinColumn(name="idplancontable")
	private PlanContable planContable;
	
	private boolean estado;

	@Column(name="tipomoneda")
	private String tipoMoneda;	

	@ManyToOne
	@JoinColumn(name="idactividadoperacion")
	private ActividadOperacion actividadOperacion;
	
	@ManyToOne
	@JoinColumn(name="idplancontableanterior")
	private PlanContable planContableAnterior;
	
	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public PlanContable getPlanContable() {
		return planContable;
	}
	public void setPlanContable(PlanContable planContable) {
		this.planContable = planContable;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public ActividadOperacion getActividadOperacion() {
		return actividadOperacion;
	}
	public void setActividadOperacion(ActividadOperacion actividadOperacion) {
		this.actividadOperacion = actividadOperacion;
	}
	public PlanContable getPlanContableAnterior() {
		return planContableAnterior;
	}
	public void setPlanContableAnterior(PlanContable planContableAnterior) {
		this.planContableAnterior = planContableAnterior;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	
}
