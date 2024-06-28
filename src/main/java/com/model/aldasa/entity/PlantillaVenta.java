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
@Table(name = "plantillaventa")
public class PlantillaVenta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name="idlote")
    private Lote lote;
	
	@ManyToOne
    @JoinColumn(name="idperson")
    private Person person;
	
	@ManyToOne
    @JoinColumn(name="idpersonasesor")
    private Person personAsesor;
	
	@ManyToOne
    @JoinColumn(name="idpersonsupervisor")
    private Person personSupervisor;
	
	private String estado;
	
	@Column(name="fechaventa")
	private Date fechaVenta;
	
	@Column(name="montoventa")
	private BigDecimal montoVenta;
	
	@Column(name="tipopago")
	private String tipoPago;
	
	@Column(name="montoinicial")
	private BigDecimal montoInicial;
	
	@Column(name="numerocuota")
	private Integer numeroCuota;

	private BigDecimal interes;

	@ManyToOne
    @JoinColumn(name="idusuario")
    private Usuario usuario;
	
	private Date fecha;
	
	@Column(name="realizocontrato")
	private boolean realizoContrato;
	
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
	
	@Column(name="realizoboletainicial")
	private boolean realizoBoletaInicial;
	
	@ManyToOne
    @JoinColumn(name="iddocumentoventa")
    private DocumentoVenta documentoVenta;
	
	@ManyToOne
    @JoinColumn(name="idrequerimientoseparacion")
    private RequerimientoSeparacion requerimientoSeparacion;
	
	private String observacion;
	
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
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public BigDecimal getMontoVenta() {
		return montoVenta;
	}
	public void setMontoVenta(BigDecimal montoVenta) {
		this.montoVenta = montoVenta;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public Integer getNumeroCuota() {
		return numeroCuota;
	}
	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}
	public BigDecimal getInteres() {
		return interes;
	}
	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Person getPersonAsesor() {
		return personAsesor;
	}
	public void setPersonAsesor(Person personAsesor) {
		this.personAsesor = personAsesor;
	}
	public Person getPersonSupervisor() {
		return personSupervisor;
	}
	public void setPersonSupervisor(Person personSupervisor) {
		this.personSupervisor = personSupervisor;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public boolean isRealizoContrato() {
		return realizoContrato;
	}
	public void setRealizoContrato(boolean realizoContrato) {
		this.realizoContrato = realizoContrato;
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
	public boolean isRealizoBoletaInicial() {
		return realizoBoletaInicial;
	}
	public void setRealizoBoletaInicial(boolean realizoBoletaInicial) {
		this.realizoBoletaInicial = realizoBoletaInicial;
	}
	public DocumentoVenta getDocumentoVenta() {
		return documentoVenta;
	}
	public void setDocumentoVenta(DocumentoVenta documentoVenta) {
		this.documentoVenta = documentoVenta;
	}
	public RequerimientoSeparacion getRequerimientoSeparacion() {
		return requerimientoSeparacion;
	}
	public void setRequerimientoSeparacion(RequerimientoSeparacion requerimientoSeparacion) {
		this.requerimientoSeparacion = requerimientoSeparacion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Date getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	
	
	
}
