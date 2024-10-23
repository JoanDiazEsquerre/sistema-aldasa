package com.model.aldasa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detalledocumentoventa")
public class DetalleDocumentoVenta{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="iddocumentoventa")
	private DocumentoVenta documentoVenta;
	
	@ManyToOne
	@JoinColumn(name="idproducto")
	private Producto producto;
	
	private String descripcion;
	
	private BigDecimal amortizacion;
	
	private BigDecimal interes;
	
	private BigDecimal adelanto;
	
	@Column(name="importeventa")
	private BigDecimal importeVenta;

	@ManyToOne
	@JoinColumn(name="idcuota")
	private Cuota cuota;
	
	@ManyToOne
	@JoinColumn(name="idrequerimientoseparacion")
	private RequerimientoSeparacion requerimientoSeparacion;
	
	@ManyToOne
	@JoinColumn(name="iddetallerequerimientoseparacion")
	private DetalleRequerimientoSeparacion detalleRequerimientoSeparacion;
	
	@ManyToOne
	@JoinColumn(name="idcuotaprepago")
	private Cuota cuotaPrepago;
	
	private boolean estado;
	
	@Column(name="importeventasinigv")
	private BigDecimal importeVentaSinIgv;
	
	@Column(name="preciosinigv")
	private BigDecimal precioSinIgv;
	
	private transient BigDecimal totalTemp;
		
	
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public DocumentoVenta getDocumentoVenta() {
		return documentoVenta;
	}
	public void setDocumentoVenta(DocumentoVenta documentoVenta) {
		this.documentoVenta = documentoVenta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getAmortizacion() {
		return amortizacion;
	}
	public void setAmortizacion(BigDecimal amortizacion) {
		this.amortizacion = amortizacion;
	}
	public BigDecimal getInteres() {
		return interes;
	}
	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}
	public BigDecimal getImporteVenta() {
		return importeVenta;
	}
	public void setImporteVenta(BigDecimal importeVenta) {
		this.importeVenta = importeVenta;
	}
	public Cuota getCuota() {
		return cuota;
	}
	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public RequerimientoSeparacion getRequerimientoSeparacion() {
		return requerimientoSeparacion;
	}
	public void setRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion) {
		this.requerimientoSeparacion = requerimientoSeparacion;
	}
	public BigDecimal getAdelanto() {
		return adelanto;
	}
	public void setAdelanto(BigDecimal adelanto) {
		this.adelanto = adelanto;
	}
	public BigDecimal getTotalTemp() {
		return totalTemp;
	}
	public void setTotalTemp(BigDecimal totalTemp) {
		this.totalTemp = totalTemp;
	}
	public Cuota getCuotaPrepago() {
		return cuotaPrepago;
	}
	public void setCuotaPrepago(Cuota cuotaPrepago) {
		this.cuotaPrepago = cuotaPrepago;
	}
	public BigDecimal getImporteVentaSinIgv() {
		return importeVentaSinIgv;
	}
	public void setImporteVentaSinIgv(BigDecimal importeVentaSinIgv) {
		this.importeVentaSinIgv = importeVentaSinIgv;
	}
	public BigDecimal getPrecioSinIgv() {
		return precioSinIgv;
	}
	public void setPrecioSinIgv(BigDecimal precioSinIgv) {
		this.precioSinIgv = precioSinIgv;
	}
	public DetalleRequerimientoSeparacion getDetalleRequerimientoSeparacion() {
		return detalleRequerimientoSeparacion;
	}
	public void setDetalleRequerimientoSeparacion(DetalleRequerimientoSeparacion detalleRequerimientoSeparacion) {
		this.detalleRequerimientoSeparacion = detalleRequerimientoSeparacion;
	}
	
	
	
}
