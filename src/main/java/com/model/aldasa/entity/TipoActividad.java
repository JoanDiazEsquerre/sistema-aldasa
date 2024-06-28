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
@Table(name = "tipoactividad")
public class TipoActividad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String descripcion;
	
	private boolean estado;

	@Column(name="codigoactivo")
	private String codigoActivo;	
	
	@Column(name="mostrarpagobeneficio")
	private String mostrarPagoBeneficio;
	
	
	
	
	
	
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
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getCodigoActivo() {
		return codigoActivo;
	}
	public void setCodigoActivo(String codigoActivo) {
		this.codigoActivo = codigoActivo;
	}
	public String getMostrarPagoBeneficio() {
		return mostrarPagoBeneficio;
	}
	public void setMostrarPagoBeneficio(String mostrarPagoBeneficio) {
		this.mostrarPagoBeneficio = mostrarPagoBeneficio;
	}	
	
	

}
