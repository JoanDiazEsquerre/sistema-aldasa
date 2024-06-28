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
@Table(name = "vouchertemp")
public class VoucherTemp {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idplantillaventa")
	private PlantillaVenta plantillaVenta;
	
	@ManyToOne
	@JoinColumn(name="idrequerimientoseparacion")
	private RequerimientoSeparacion requerimientoSeparacion;
	
	private BigDecimal monto;
	
	@Column(name="tipotransaccion") 
	private String tipoTransaccion;
	
	@Column(name="numerooperacion") 
	private String numeroOperacion;
	
	@Column(name="fechaoperacion") 
	private Date fechaOperacion;
	
	@ManyToOne
	@JoinColumn(name="idcuentabancaria")
	private CuentaBancaria cuentaBancaria;
	

	private boolean estado;


	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public PlantillaVenta getPlantillaVenta() {
		return plantillaVenta;
	}
	public void setPlantillaVenta(PlantillaVenta plantillaVenta) {
		this.plantillaVenta = plantillaVenta;
	}
	public RequerimientoSeparacion getRequerimientoSeparacion() {
		return requerimientoSeparacion;
	}
	public void setRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion) {
		this.requerimientoSeparacion = requerimientoSeparacion;
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
	public String getNumeroOperacion() {
		return numeroOperacion;
	}
	public void setNumeroOperacion(String numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}
	
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public CuentaBancaria getCuentaBancaria() {
		return cuentaBancaria;
	}
	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}


	@Override
    public boolean equals(Object other) {
        return (other instanceof VoucherTemp) && (id != null)
            ? id.equals(((VoucherTemp) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	
}
