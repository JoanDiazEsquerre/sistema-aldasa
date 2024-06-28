package com.model.aldasa.entity;

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
@Table(name = "plancontable")
public class PlanContable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String cuenta;

	private String descripcion;
	
	private String anexo;
	
	private Integer nivel;
	
	private boolean estado;

	@ManyToOne
    @JoinColumn(name="idusuario")
    private Usuario usuario;
	
	@Column(name="fecharegistro")
	private Date fechaRegistro;
	
	private String periodo;

	private String indicador;

	private String presentacion;

	private String comprobante;
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getAnexo() {
		return anexo;
	}
	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}
	public Integer getNivel() {
		return nivel;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getIndicador() {
		return indicador;
	}
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}
	public String getPresentacion() {
		return presentacion;
	}
	public void setPresentacion(String presentacion) {
		this.presentacion = presentacion;
	}
	public String getComprobante() {
		return comprobante;
	}
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}
	
	
	

	
	
}
