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
@Table(name = "requerimientocompra")
public class RequerimientoCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="fechaemision")
	private Date fechaEmision;
	
	private String estado, numero;
	
	@ManyToOne
	@JoinColumn(name="idusuario")
	private Usuario usuario;
	
	@Column(name="fecharegistro")
	private Date fechaRegistro;
	
	@Column(name="formapago")
	private String formaPago;
	
	private String observacion;
	
	private BigDecimal total;
	
	@ManyToOne
	@JoinColumn(name="idusuarioaprueba")
	private Usuario usuarioAprueba;
	
	@ManyToOne
	@JoinColumn(name="idusuariorechaza")
	private Usuario usuarioRechaza;
	
	@Column(name="fechaaprueba")
	private Date fechaAprueba;
	
	@Column(name="fecharechaza")
	private Date fechaRechaza;

	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
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
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Usuario getUsuarioRechaza() {
		return usuarioRechaza;
	}
	public void setUsuarioRechaza(Usuario usuarioRechaza) {
		this.usuarioRechaza = usuarioRechaza;
	}
	public Usuario getUsuarioAprueba() {
		return usuarioAprueba;
	}
	public void setUsuarioAprueba(Usuario usuarioAprueba) {
		this.usuarioAprueba = usuarioAprueba;
	}
	public Date getFechaAprueba() {
		return fechaAprueba;
	}
	public void setFechaAprueba(Date fechaAprueba) {
		this.fechaAprueba = fechaAprueba;
	}
	public Date getFechaRechaza() {
		return fechaRechaza;
	}
	public void setFechaRechaza(Date fechaRechaza) {
		this.fechaRechaza = fechaRechaza;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
}
