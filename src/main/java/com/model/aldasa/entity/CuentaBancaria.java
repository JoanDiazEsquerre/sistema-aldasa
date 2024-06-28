package com.model.aldasa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cuentabancaria")
public class CuentaBancaria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idbanco")
	private Banco banco;
	
	private String numero;
	
	private boolean estado;
	
	private String moneda;

	private String cci;

	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Banco getBanco() {
		return banco;
	}
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getCci() {
		return cci;
	}
	public void setCci(String cci) {
		this.cci = cci;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	@Override
    public boolean equals(Object other) {
        return (other instanceof CuentaBancaria) && (id != null)
            ? id.equals(((CuentaBancaria) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }


}
