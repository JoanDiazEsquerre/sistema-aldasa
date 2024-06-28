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
@Table(name = "detallemovimientobancario")
public class DetalleMovimientoBancario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
		
	@ManyToOne
	@JoinColumn(name="idmovimientobancario")
	private MovimientoBancario movimientoBancario;
	
	@Column(name="fechaoperacion")
	private Date fechaOperacion;
	
	@Column(name="fechaproceso")
	private Date fechaProceso;
	
	private String hora;
	
	@Column(name="numerooperacion")
	private String numeroOperacion;
	
	private String descripcion, observacion;
	
	private BigDecimal importe;
	
	@ManyToOne
	@JoinColumn(name="idlote")
	private Lote lote;

	@ManyToOne
	@JoinColumn(name="idtipomovimiento")
	private TipoMovimiento tipoMovimiento;
	
	private boolean estado;
	
	public DetalleMovimientoBancario() {
		
	}
	
	public DetalleMovimientoBancario(DetalleMovimientoBancario det) {
		super();
		this.id = det.id;
		this.movimientoBancario = det.movimientoBancario;
		this.fechaOperacion = det.fechaOperacion;
		this.fechaProceso = det.fechaProceso;
		this.hora = det.hora;
		this.numeroOperacion = det.numeroOperacion;
		this.descripcion = det.descripcion;
		this.observacion = det.observacion;
		this.importe = det.importe;
		this.lote = det.lote;
		this.tipoMovimiento = det.tipoMovimiento;
		this.estado = det.estado;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public MovimientoBancario getMovimientoBancario() {
		return movimientoBancario;
	}
	public void setMovimientoBancario(MovimientoBancario movimientoBancario) {
		this.movimientoBancario = movimientoBancario;
	}
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getNumeroOperacion() {
		return numeroOperacion;
	}
	public void setNumeroOperacion(String numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
	public Lote getLote() {
		return lote;
	}
	public void setLote(Lote lote) {
		this.lote = lote;
	}
	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}
	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof DetalleMovimientoBancario) && (id != null)
            ? id.equals(((DetalleMovimientoBancario) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
