package com.model.aldasa.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuracioncomision")
public class ConfiguracionComision {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="fechainicio")
	private Date fechaInicio;
	
	@Column(name="fechafin")
	private Date fechaFin;
	
	@Column(name="codigo")
	private String codigo;
	
	@Column(name="comisioncontado")
	private BigDecimal comisionContado;
	
	@Column(name="comisioncredito")
	private BigDecimal comisionCredito;

	@Column(name="minimoventajunior")
	private Integer minimoVentaJunior ;
	
	@Column(name="maximoventajunior")
	private Integer maximoVentaJunior ;
	
	@Column(name="bonojunior")
	private BigDecimal bonoJunior ;
	
	@Column(name="minimoventasenior")
	private Integer minimoVentaSenior ;
	
	@Column(name="maximoventasenior")
	private Integer maximoVentaSenior ;
	
	@Column(name="bonosenior")
	private BigDecimal bonoSenior ;
	
	@Column(name="minimoventamaster")
	private Integer minimoVentaMaster ;
	
	@Column(name="maximoventamaster")
	private Integer maximoVentaMaster ;
	
	@Column(name="bonomaster")
	private BigDecimal bonoMaster ;
	
	@Column(name="ventasmetacontado")
	private Integer ventasMetaContado ;
	
	@Column(name="comisioncontadometa")
	private BigDecimal comisionContadoMeta ;
	
	@Column(name="comisioncontadoext")
	private BigDecimal comisionContadoExt ;
	
	@Column(name="comisioncreditoext")
	private BigDecimal comisionCreditoExt ;
	
	@Column(name="minimoventasext")
	private Integer minimoVentasExt ;
	
	@Column(name="bonoventaext")
	private BigDecimal bonoVentaExt ;
	
	@Column(name="ventasmetacontadoext")
	private Integer ventasMetaContadoExt ;
	
	@Column(name="comisioncontadometaext")
	private BigDecimal comisionContadoMetaExt ;
	
	@Column(name="comisioncontadoemp")
	private BigDecimal comisionContadoEmp ;
	
	@Column(name="comisioncreditoemp")
	private BigDecimal comisionCreditoEmp ;
	
	@Column(name="minimoventasemp")
	private Integer minimoVentasEmp ;
	
	@Column(name="bonoventaemp")
	private BigDecimal bonoVentaEmp ;
	
	@Column(name="ventasmetacontadoemp")
	private Integer ventasMetaContadoEmp ;
	
	@Column(name="comisioncontadometaemp")
	private BigDecimal comisionContadoMetaEmp ;
	
	@Column(name="comisionsupervisor")
	private BigDecimal comisionSupervisor ;
	
	
	@Column(name="bonosupervisor")
	private BigDecimal bonoSupervisor ;
	
	@Column(name="comisionjefeventa")
	private BigDecimal comisionJefeVenta ;
	
	@Column(name="porcentajebono")
	private BigDecimal porcentajeBono ;
	
	
	@Column(name="bonojefeventa")
	private BigDecimal bonoJefeVenta ;
	
	@Column(name="comisionjefeventameta")
	private BigDecimal comisionJefeVentaMeta ;
	
	@Column(name="bonocoordinador")
	private BigDecimal bonoCoordinador ;
	
	@Column(name="comisionsubgerente")
	private BigDecimal comisionSubgerente ;
	
	@Column(name="bonojefeventaobligatorio")
	private BigDecimal bonoJefeVentaObligatorio ;
	
	@Column(name="bonojv")
	private BigDecimal bonojv ;
	
	@Column(name="montocomisionjv")
	private BigDecimal montoComisionjv ;
	
	
	@Column(name="numvendidojv")
	private Integer numVendidojv ;
	
	@Column(name="comisionporcentajejv")
	private BigDecimal comisionPorcentajejv ;
	
	@Column(name="metajv")
	private Integer metajv ;
	
	@Column(name="montocomisionsubgerente")
	private BigDecimal montoComisionSubgerente ;
	
	
	
	
	
	private boolean estado;
		
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public BigDecimal getComisionContado() {
		return comisionContado;
	}
	public void setComisionContado(BigDecimal comisionContado) {
		this.comisionContado = comisionContado;
	}
	public BigDecimal getComisionCredito() {
		return comisionCredito;
	}
	public void setComisionCredito(BigDecimal comisionCredito) {
		this.comisionCredito = comisionCredito;
	}
	public Integer getMinimoVentaJunior() {
		return minimoVentaJunior;
	}
	public void setMinimoVentaJunior(Integer minimoVentaJunior) {
		this.minimoVentaJunior = minimoVentaJunior;
	}
	public Integer getMaximoVentaJunior() {
		return maximoVentaJunior;
	}
	public void setMaximoVentaJunior(Integer maximoVentaJunior) {
		this.maximoVentaJunior = maximoVentaJunior;
	}
	public BigDecimal getBonoJunior() {
		return bonoJunior;
	}
	public void setBonoJunior(BigDecimal bonoJunior) {
		this.bonoJunior = bonoJunior;
	}
	public Integer getMinimoVentaSenior() {
		return minimoVentaSenior;
	}
	public void setMinimoVentaSenior(Integer minimoVentaSenior) {
		this.minimoVentaSenior = minimoVentaSenior;
	}
	public Integer getMaximoVentaSenior() {
		return maximoVentaSenior;
	}
	public void setMaximoVentaSenior(Integer maximoVentaSenior) {
		this.maximoVentaSenior = maximoVentaSenior;
	}
	public BigDecimal getBonoSenior() {
		return bonoSenior;
	}
	public void setBonoSenior(BigDecimal bonoSenior) {
		this.bonoSenior = bonoSenior;
	}
	public Integer getMinimoVentaMaster() {
		return minimoVentaMaster;
	}
	public void setMinimoVentaMaster(Integer minimoVentaMaster) {
		this.minimoVentaMaster = minimoVentaMaster;
	}
	public Integer getMaximoVentaMaster() {
		return maximoVentaMaster;
	}
	public void setMaximoVentaMaster(Integer maximoVentaMaster) {
		this.maximoVentaMaster = maximoVentaMaster;
	}
	public BigDecimal getBonoMaster() {
		return bonoMaster;
	}
	public void setBonoMaster(BigDecimal bonoMaster) {
		this.bonoMaster = bonoMaster;
	}
	public Integer getVentasMetaContado() {
		return ventasMetaContado;
	}
	public void setVentasMetaContado(Integer ventasMetaContado) {
		this.ventasMetaContado = ventasMetaContado;
	}
	public BigDecimal getComisionContadoMeta() {
		return comisionContadoMeta;
	}
	public void setComisionContadoMeta(BigDecimal comisionContadoMeta) {
		this.comisionContadoMeta = comisionContadoMeta;
	}
	public BigDecimal getComisionContadoExt() {
		return comisionContadoExt;
	}
	public void setComisionContadoExt(BigDecimal comisionContadoExt) {
		this.comisionContadoExt = comisionContadoExt;
	}
	public BigDecimal getComisionCreditoExt() {
		return comisionCreditoExt;
	}
	public void setComisionCreditoExt(BigDecimal comisionCreditoExt) {
		this.comisionCreditoExt = comisionCreditoExt;
	}
	public Integer getMinimoVentasExt() {
		return minimoVentasExt;
	}
	public void setMinimoVentasExt(Integer minimoVentasExt) {
		this.minimoVentasExt = minimoVentasExt;
	}
	public BigDecimal getBonoVentaExt() {
		return bonoVentaExt;
	}
	public void setBonoVentaExt(BigDecimal bonoVentaExt) {
		this.bonoVentaExt = bonoVentaExt;
	}
	public Integer getVentasMetaContadoExt() {
		return ventasMetaContadoExt;
	}
	public void setVentasMetaContadoExt(Integer ventasMetaContadoExt) {
		this.ventasMetaContadoExt = ventasMetaContadoExt;
	}
	public BigDecimal getComisionContadoMetaExt() {
		return comisionContadoMetaExt;
	}
	public void setComisionContadoMetaExt(BigDecimal comisionContadoMetaExt) {
		this.comisionContadoMetaExt = comisionContadoMetaExt;
	}
	public BigDecimal getComisionContadoEmp() {
		return comisionContadoEmp;
	}
	public void setComisionContadoEmp(BigDecimal comisionContadoEmp) {
		this.comisionContadoEmp = comisionContadoEmp;
	}
	public BigDecimal getComisionCreditoEmp() {
		return comisionCreditoEmp;
	}
	public void setComisionCreditoEmp(BigDecimal comisionCreditoEmp) {
		this.comisionCreditoEmp = comisionCreditoEmp;
	}
	public Integer getMinimoVentasEmp() {
		return minimoVentasEmp;
	}
	public void setMinimoVentasEmp(Integer minimoVentasEmp) {
		this.minimoVentasEmp = minimoVentasEmp;
	}
	public BigDecimal getBonoVentaEmp() {
		return bonoVentaEmp;
	}
	public void setBonoVentaEmp(BigDecimal bonoVentaEmp) {
		this.bonoVentaEmp = bonoVentaEmp;
	}
	public Integer getVentasMetaContadoEmp() {
		return ventasMetaContadoEmp;
	}
	public void setVentasMetaContadoEmp(Integer ventasMetaContadoEmp) {
		this.ventasMetaContadoEmp = ventasMetaContadoEmp;
	}
	public BigDecimal getComisionContadoMetaEmp() {
		return comisionContadoMetaEmp;
	}
	public void setComisionContadoMetaEmp(BigDecimal comisionContadoMetaEmp) {
		this.comisionContadoMetaEmp = comisionContadoMetaEmp;
	}
	public BigDecimal getComisionSupervisor() {
		return comisionSupervisor;
	}
	public void setComisionSupervisor(BigDecimal comisionSupervisor) {
		this.comisionSupervisor = comisionSupervisor;
	}
	public BigDecimal getBonoSupervisor() {
		return bonoSupervisor;
	}
	public void setBonoSupervisor(BigDecimal bonoSupervisor) {
		this.bonoSupervisor = bonoSupervisor;
	}
	public BigDecimal getComisionJefeVenta() {
		return comisionJefeVenta;
	}
	public void setComisionJefeVenta(BigDecimal comisionJefeVenta) {
		this.comisionJefeVenta = comisionJefeVenta;
	}
	public BigDecimal getPorcentajeBono() {
		return porcentajeBono;
	}
	public void setPorcentajeBono(BigDecimal porcentajeBono) {
		this.porcentajeBono = porcentajeBono;
	}
	public BigDecimal getBonoJefeVenta() {
		return bonoJefeVenta;
	}
	public void setBonoJefeVenta(BigDecimal bonoJefeVenta) {
		this.bonoJefeVenta = bonoJefeVenta;
	}
	public BigDecimal getComisionJefeVentaMeta() {
		return comisionJefeVentaMeta;
	}
	public void setComisionJefeVentaMeta(BigDecimal comisionJefeVentaMeta) {
		this.comisionJefeVentaMeta = comisionJefeVentaMeta;
	}
	public BigDecimal getBonoCoordinador() {
		return bonoCoordinador;
	}
	public void setBonoCoordinador(BigDecimal bonoCoordinador) {
		this.bonoCoordinador = bonoCoordinador;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public BigDecimal getComisionSubgerente() {
		return comisionSubgerente;
	}
	public void setComisionSubgerente(BigDecimal comisionSubgerente) {
		this.comisionSubgerente = comisionSubgerente;
	}
	public BigDecimal getBonoJefeVentaObligatorio() {
		return bonoJefeVentaObligatorio;
	}
	public void setBonoJefeVentaObligatorio(BigDecimal bonoJefeVentaObligatorio) {
		this.bonoJefeVentaObligatorio = bonoJefeVentaObligatorio;
	}
	public BigDecimal getBonojv() {
		return bonojv;
	}
	public void setBonojv(BigDecimal bonojv) {
		this.bonojv = bonojv;
	}
	public BigDecimal getMontoComisionjv() {
		return montoComisionjv;
	}
	public void setMontoComisionjv(BigDecimal montoComisionjv) {
		this.montoComisionjv = montoComisionjv;
	}
	public BigDecimal getComisionPorcentajejv() {
		return comisionPorcentajejv;
	}
	public void setComisionPorcentajejv(BigDecimal comisionPorcentajejv) {
		this.comisionPorcentajejv = comisionPorcentajejv;
	}
	public BigDecimal getMontoComisionSubgerente() {
		return montoComisionSubgerente;
	}
	public void setMontoComisionSubgerente(BigDecimal montoComisionSubgerente) {
		this.montoComisionSubgerente = montoComisionSubgerente;
	}	
	public Integer getNumVendidojv() {
		return numVendidojv;
	}
	public void setNumVendidojv(Integer numVendidojv) {
		this.numVendidojv = numVendidojv;
	}
	public Integer getMetajv() {
		return metajv;
	}
	public void setMetajv(Integer metajv) {
		this.metajv = metajv;
	}
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof ConfiguracionComision) && (id != null)
            ? id.equals(((ConfiguracionComision) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
