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
@Table(name = "movimientobancario")
public class MovimientoBancario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String mes, anio;
	
	@ManyToOne
	@JoinColumn(name="idcuentabancaria")
	private CuentaBancaria cuentaBancaria;
	
	@Column(name="fecharegistro")
	private Date fechaRegistro;
	
	@ManyToOne
	@JoinColumn(name="idusuario")
	private Usuario usuario;
	
	private boolean estado;
	
	@Column(name="saldoinicial")
	private BigDecimal saldoInicial;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public CuentaBancaria getCuentaBancaria() {
		return cuentaBancaria;
	}
	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}
	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof MovimientoBancario) && (id != null)
            ? id.equals(((MovimientoBancario) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
