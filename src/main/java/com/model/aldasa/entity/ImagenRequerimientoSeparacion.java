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
@Table(name = "imagenrequerimientoseparacion")
public class ImagenRequerimientoSeparacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;
	private String carpeta;
	private boolean estado;

	@ManyToOne
	@JoinColumn(name="idrequerimientoseparacion")
	private RequerimientoSeparacion requerimientoSeparacion;
	
	@ManyToOne
	@JoinColumn(name="iddetallerequerimientoseparacion")
	private DetalleRequerimientoSeparacion detalleRequerimientoSeparacion;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCarpeta() {
		return carpeta;
	}
	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public RequerimientoSeparacion getRequerimientoSeparacion() {
		return requerimientoSeparacion;
	}
	public void setRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion) {
		this.requerimientoSeparacion = requerimientoSeparacion;
	}
	public DetalleRequerimientoSeparacion getDetalleRequerimientoSeparacion() {
		return detalleRequerimientoSeparacion;
	}
	public void setDetalleRequerimientoSeparacion(DetalleRequerimientoSeparacion detalleRequerimientoSeparacion) {
		this.detalleRequerimientoSeparacion = detalleRequerimientoSeparacion;
	}

	
	
}
