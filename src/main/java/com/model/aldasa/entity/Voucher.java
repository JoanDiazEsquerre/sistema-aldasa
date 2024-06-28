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
@Table(name = "voucher")
public class Voucher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idcuentabancaria")
	private CuentaBancaria cuentaBancaria;
	
	private BigDecimal monto;
	
	@Column(name="tipotransaccion")
	private String tipoTransaccion;
	
	@Column(name="numerotransaccion")
	private String numeroTransaccion;
	
	@Column(name="fechaoperacion")
	private Date fechaOperacion;

	@ManyToOne
	@JoinColumn(name="idrequerimientoseparacion")
	private RequerimientoSeparacion requerimientoSeparacion;
	
	private boolean estado;
	
	@Column(name="generadocumento")
	private boolean generaDocumento;
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public CuentaBancaria getCuentaBancaria() {
		return cuentaBancaria;
	}
	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}
	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public RequerimientoSeparacion getRequerimientoSeparacion() {
		return requerimientoSeparacion;
	}
	public void setRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion) {
		this.requerimientoSeparacion = requerimientoSeparacion;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public boolean isGeneraDocumento() {
		return generaDocumento;
	}
	public void setGeneraDocumento(boolean generaDocumento) {
		this.generaDocumento = generaDocumento;
	}
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Voucher) && (id != null)
            ? id.equals(((Voucher) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	

}
