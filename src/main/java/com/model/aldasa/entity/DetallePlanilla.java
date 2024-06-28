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
@Table(name = "detalleplanilla")
public class DetallePlanilla {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idplanilla")
	private Planilla planilla;
	
	@ManyToOne
	@JoinColumn(name="idempleado")
	private Empleado empleado;
	
	private BigDecimal tardanza, vacaciones, comisiones, bono, total, onp, adelanto, prestamo;
	
	@Column(name="aporteobligatorio")
	private BigDecimal aporteObligatorio;
	
	@Column(name="primaseguros")
	private BigDecimal primaSeguros;
	
	@Column(name="comisionvariable")
	private BigDecimal comisionVariable;
	
	@Column(name="rentaquinta")
	private BigDecimal rentaQuinta;
	
	@Column(name="descmesanterior")
	private BigDecimal descMesAnterior;
	
	@Column(name="fesalud")
	private BigDecimal feSalud;
	
	@Column(name="pagovactrunca")
	private BigDecimal pagoVacTrunca;
	
	@Column(name="totaldescuento")
	private BigDecimal totalDescuento;
	
	@Column(name="netopagar")
	private BigDecimal netoPagar;
	
	@Column(name="essalud")
	private BigDecimal esSalud;
	
	@Column(name="abonado30")
	private BigDecimal abonado30;
	
	@Column(name="abonado4")
	private BigDecimal abonado4;
	
	private boolean estado;
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Planilla getPlanilla() {
		return planilla;
	}
	public void setPlanilla(Planilla planilla) {
		this.planilla = planilla;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public BigDecimal getTardanza() {
		return tardanza;
	}
	public void setTardanza(BigDecimal tardanza) {
		this.tardanza = tardanza;
	}
	public BigDecimal getVacaciones() {
		return vacaciones;
	}
	public void setVacaciones(BigDecimal vacaciones) {
		this.vacaciones = vacaciones;
	}
	public BigDecimal getComisiones() {
		return comisiones;
	}
	public void setComisiones(BigDecimal comisiones) {
		this.comisiones = comisiones;
	}
	public BigDecimal getBono() {
		return bono;
	}
	public void setBono(BigDecimal bono) {
		this.bono = bono;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getOnp() {
		return onp;
	}
	public void setOnp(BigDecimal onp) {
		this.onp = onp;
	}
	public BigDecimal getAdelanto() {
		return adelanto;
	}
	public void setAdelanto(BigDecimal adelanto) {
		this.adelanto = adelanto;
	}
	public BigDecimal getPrestamo() {
		return prestamo;
	}
	public void setPrestamo(BigDecimal prestamo) {
		this.prestamo = prestamo;
	}
	public BigDecimal getAbonado30() {
		return abonado30;
	}
	public void setAbonado30(BigDecimal abonado30) {
		this.abonado30 = abonado30;
	}
	public BigDecimal getAbonado4() {
		return abonado4;
	}
	public void setAbonado4(BigDecimal abonado4) {
		this.abonado4 = abonado4;
	}
	public BigDecimal getAporteObligatorio() {
		return aporteObligatorio;
	}
	public void setAporteObligatorio(BigDecimal aporteObligatorio) {
		this.aporteObligatorio = aporteObligatorio;
	}
	public BigDecimal getPrimaSeguros() {
		return primaSeguros;
	}
	public void setPrimaSeguros(BigDecimal primaSeguros) {
		this.primaSeguros = primaSeguros;
	}
	public BigDecimal getComisionVariable() {
		return comisionVariable;
	}
	public void setComisionVariable(BigDecimal comisionVariable) {
		this.comisionVariable = comisionVariable;
	}
	public BigDecimal getRentaQuinta() {
		return rentaQuinta;
	}
	public void setRentaQuinta(BigDecimal rentaQuinta) {
		this.rentaQuinta = rentaQuinta;
	}
	public BigDecimal getDescMesAnterior() {
		return descMesAnterior;
	}
	public void setDescMesAnterior(BigDecimal descMesAnterior) {
		this.descMesAnterior = descMesAnterior;
	}
	public BigDecimal getPagoVacTrunca() {
		return pagoVacTrunca;
	}
	public void setPagoVacTrunca(BigDecimal pagoVacTrunca) {
		this.pagoVacTrunca = pagoVacTrunca;
	}
	public BigDecimal getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(BigDecimal totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	public BigDecimal getNetoPagar() {
		return netoPagar;
	}
	public void setNetoPagar(BigDecimal netoPagar) {
		this.netoPagar = netoPagar;
	}
	public BigDecimal getEsSalud() {
		return esSalud;
	}
	public void setEsSalud(BigDecimal esSalud) {
		this.esSalud = esSalud;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public BigDecimal getFeSalud() {
		return feSalud;
	}
	public void setFeSalud(BigDecimal feSalud) {
		this.feSalud = feSalud;
	}
	
	
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof DetallePlanilla) && (id != null)
            ? id.equals(((DetallePlanilla) other).id)
            : (other == this);
    }

   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	

}
