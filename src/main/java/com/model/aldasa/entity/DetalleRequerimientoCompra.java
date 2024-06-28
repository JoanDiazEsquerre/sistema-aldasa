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
@Table(name = "detallerequerimientocompra")
public class DetalleRequerimientoCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idrequerimientocompra")
	private RequerimientoCompra requerimientoCompra;
	
	private BigDecimal cantidad;
	
	@Column(name="descripcionproducto")
	private String DescripcionProducto;
	
	@ManyToOne
	@JoinColumn(name="idunidad")
	private Unidad unidad;
	
	private boolean estado;
	
	private BigDecimal precio;

	private BigDecimal total;
	
	
	

	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public RequerimientoCompra getRequerimientoCompra() {
		return requerimientoCompra;
	}
	public void setRequerimientoCompra(RequerimientoCompra requerimientoCompra) {
		this.requerimientoCompra = requerimientoCompra;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public String getDescripcionProducto() {
		return DescripcionProducto;
	}
	public void setDescripcionProducto(String descripcionProducto) {
		DescripcionProducto = descripcionProducto;
	}
	public Unidad getUnidad() {
		return unidad;
	}
	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}


	
	
}
