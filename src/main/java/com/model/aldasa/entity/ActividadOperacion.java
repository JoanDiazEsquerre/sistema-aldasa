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
@Table(name = "actividadoperacion")
public class ActividadOperacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String codigo;

	private String descripcion;
	
	@ManyToOne
	@JoinColumn(name="idtipoactividad")
	private TipoActividad tipoActividad;
	
	private boolean estado;

	@Column(name="mostrarpagobeneficio")
	private String mostrarPagoBeneficio;
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public TipoActividad getTipoActividad() {
		return tipoActividad;
	}
	public void setTipoActividad(TipoActividad tipoActividad) {
		this.tipoActividad = tipoActividad;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getMostrarPagoBeneficio() {
		return mostrarPagoBeneficio;
	}
	public void setMostrarPagoBeneficio(String mostrarPagoBeneficio) {
		this.mostrarPagoBeneficio = mostrarPagoBeneficio;
	}	

	
}
