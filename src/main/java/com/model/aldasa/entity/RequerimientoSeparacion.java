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
@Table(name = "requerimientoseparacion")
public class RequerimientoSeparacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idlote")
	private Lote lote;
	
	private Date fecha;
	
	@Column(name="fechavencimiento")
	private Date fechaVencimiento;
	
	private String estado;
	
	@ManyToOne
	@JoinColumn(name="idprospection")
	private Prospection prospection;
	
	@ManyToOne
	@JoinColumn(name="idperson")
	private Person person;
	
	@ManyToOne
	@JoinColumn(name="idpersonsupervisor")
	private Person personSupervisor;

	@ManyToOne
	@JoinColumn(name="idpersonasesor")
	private Person personAsesor;
	
	@ManyToOne
	@JoinColumn(name="iddocumentoventa")
	private DocumentoVenta documentoVenta;
	
	private BigDecimal monto;
	
	@Column(name="montoadicional")
	private BigDecimal montoAdicional;
	
	@Column(name="pagototal")
	private String pagoTotal;
	
	@ManyToOne
	@JoinColumn(name="idcontrato")
	private Contrato idContrato;
	
	@Column(name="generadocumento")
	private boolean generaDocumento;
	
	@ManyToOne
    @JoinColumn(name="idusuarioaprueba")
    private Usuario usuarioAprueba;
	
	@Column(name="fechaaprueba")
	private Date fechaAprueba;
	
	@ManyToOne
    @JoinColumn(name="idusuariorechaza")
    private Usuario usuarioRechaza;
	
	@Column(name="fecharechaza")
	private Date fechaRechaza;
	
	private String observacion;
	
	@ManyToOne
	@JoinColumn(name="idsucursal")
	private Sucursal sucursal;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Lote getLote() {
		return lote;
	}
	public void setLote(Lote lote) {
		this.lote = lote;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Prospection getProspection() {
		return prospection;
	}
	public void setProspection(Prospection prospection) {
		this.prospection = prospection;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Person getPersonSupervisor() {
		return personSupervisor;
	}
	public void setPersonSupervisor(Person personSupervisor) {
		this.personSupervisor = personSupervisor;
	}
	public Person getPersonAsesor() {
		return personAsesor;
	}
	public void setPersonAsesor(Person personAsesor) {
		this.personAsesor = personAsesor;
	}
	public DocumentoVenta getDocumentoVenta() {
		return documentoVenta;
	}
	public void setDocumentoVenta(DocumentoVenta documentoVenta) {
		this.documentoVenta = documentoVenta;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public boolean isGeneraDocumento() {
		return generaDocumento;
	}
	public void setGeneraDocumento(boolean generaDocumento) {
		this.generaDocumento = generaDocumento;
	}
	public Usuario getUsuarioAprueba() {
		return usuarioAprueba;
	}
	public void setUsuarioAprueba(Usuario usuarioAprueba) {
		this.usuarioAprueba = usuarioAprueba;
	}
	public Date getFechaAprueba() {
		return fechaAprueba;
	}
	public void setFechaAprueba(Date fechaAprueba) {
		this.fechaAprueba = fechaAprueba;
	}
	public Usuario getUsuarioRechaza() {
		return usuarioRechaza;
	}
	public void setUsuarioRechaza(Usuario usuarioRechaza) {
		this.usuarioRechaza = usuarioRechaza;
	}
	public Date getFechaRechaza() {
		return fechaRechaza;
	}
	public void setFechaRechaza(Date fechaRechaza) {
		this.fechaRechaza = fechaRechaza;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public BigDecimal getMontoAdicional() {
		return montoAdicional;
	}
	public void setMontoAdicional(BigDecimal montoAdicional) {
		this.montoAdicional = montoAdicional;
	}
	public String getPagoTotal() {
		return pagoTotal;
	}
	public void setPagoTotal(String pagoTotal) {
		this.pagoTotal = pagoTotal;
	}
	public Contrato getIdContrato() {
		return idContrato;
	}
	public void setIdContrato(Contrato idContrato) {
		this.idContrato = idContrato;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	
	
	
}
