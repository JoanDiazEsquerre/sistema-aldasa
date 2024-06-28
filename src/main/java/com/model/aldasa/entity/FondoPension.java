package com.model.aldasa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fondopension")
public class FondoPension {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	String nombre;
	
	@Column(name="aporteobligatorio")
	private BigDecimal aporteObligatorio;
	
	@Column(name="primaseguro")
	private BigDecimal primaSeguro;
	
	@Column(name="comisionsobreflujo1")
	private BigDecimal comisionSobreFlujo1;
	
	@Column(name="comisionsobreflujo2")
	private BigDecimal comisionSobreFlujo2;
	
	@Column(name="comisionsobreflujo3")
	private BigDecimal comisionSobreFlujo3;
	
	
	@Column(name="comisionanualsobresaldo")
	private BigDecimal comisionAnualSobreSaldo;
	
	@Column(name="remuneracionmaxima")
	private BigDecimal remuneracionMaxima;
	
	boolean estado;
	
	
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
	public BigDecimal getAporteObligatorio() {
		return aporteObligatorio;
	}
	public void setAporteObligatorio(BigDecimal aporteObligatorio) {
		this.aporteObligatorio = aporteObligatorio;
	}
	public BigDecimal getPrimaSeguro() {
		return primaSeguro;
	}
	public void setPrimaSeguro(BigDecimal primaSeguro) {
		this.primaSeguro = primaSeguro;
	}
	public BigDecimal getComisionSobreFlujo1() {
		return comisionSobreFlujo1;
	}
	public void setComisionSobreFlujo1(BigDecimal comisionSobreFlujo1) {
		this.comisionSobreFlujo1 = comisionSobreFlujo1;
	}
	public BigDecimal getComisionSobreFlujo2() {
		return comisionSobreFlujo2;
	}
	public void setComisionSobreFlujo2(BigDecimal comisionSobreFlujo2) {
		this.comisionSobreFlujo2 = comisionSobreFlujo2;
	}
	public BigDecimal getComisionSobreFlujo3() {
		return comisionSobreFlujo3;
	}
	public void setComisionSobreFlujo3(BigDecimal comisionSobreFlujo3) {
		this.comisionSobreFlujo3 = comisionSobreFlujo3;
	}
	public BigDecimal getComisionAnualSobreSaldo() {
		return comisionAnualSobreSaldo;
	}
	public void setComisionAnualSobreSaldo(BigDecimal comisionAnualSobreSaldo) {
		this.comisionAnualSobreSaldo = comisionAnualSobreSaldo;
	}
	public BigDecimal getRemuneracionMaxima() {
		return remuneracionMaxima;
	}
	public void setRemuneracionMaxima(BigDecimal remuneracionMaxima) {
		this.remuneracionMaxima = remuneracionMaxima;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof FondoPension) && (id != null)
            ? id.equals(((FondoPension) other).id)
            : (other == this);
    }
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
