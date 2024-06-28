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
@Table(name = "planilla")
public class Planilla {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	private String periodo, mes;
	
	@Column(name="tipoplanilla")
	private String tipoPlanilla;
	
	private boolean estado, temporal;
	
	
	@Column(name="fecharegistro")
	private Date fechaRegistro;

	@ManyToOne
	@JoinColumn(name="idusuario")
	private Usuario usuario;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getTipoPlanilla() {
		return tipoPlanilla;
	}
	public void setTipoPlanilla(String tipoPlanilla) {
		this.tipoPlanilla = tipoPlanilla;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
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
	public boolean isTemporal() {
		return temporal;
	}
	public void setTemporal(boolean temporal) {
		this.temporal = temporal;
	}
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Planilla) && (id != null)
            ? id.equals(((Planilla) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	

}
