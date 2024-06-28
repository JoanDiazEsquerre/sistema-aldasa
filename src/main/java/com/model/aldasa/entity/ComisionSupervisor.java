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
@Table(name = "comisionsupervisor")
public class ComisionSupervisor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idpersonasupervisor")
	private Person personaSupervisor;
	
	@ManyToOne
	@JoinColumn(name="idconfiguracioncomision")
	private ConfiguracionComision configuracionComision;
	
	@Column(name="bono")
	private BigDecimal bono;
	
	@Column(name="montocomision")
	private BigDecimal montoComision;

	@Column(name="numvendido")
	private Integer numVendido;
	
	@Column(name="comisionporcentaje")
	private BigDecimal comisionPorcentaje;

	private boolean estado;
	
	@Column(name="meta")
	private Integer meta;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ConfiguracionComision getConfiguracionComision() {
		return configuracionComision;
	}
	public void setConfiguracionComision(ConfiguracionComision configuracionComision) {
		this.configuracionComision = configuracionComision;
	}
	public Person getPersonaSupervisor() {
		return personaSupervisor;
	}
	public void setPersonaSupervisor(Person personaSupervisor) {
		this.personaSupervisor = personaSupervisor;
	}
	public BigDecimal getBono() {
		return bono;
	}
	public void setBono(BigDecimal bono) {
		this.bono = bono;
	}
	public BigDecimal getMontoComision() {
		return montoComision;
	}
	public void setMontoComision(BigDecimal montoComision) {
		this.montoComision = montoComision;
	}
	public Integer getNumVendido() {
		return numVendido;
	}
	public void setNumVendido(Integer numVendido) {
		this.numVendido = numVendido;
	}
	public BigDecimal getComisionPorcentaje() {
		return comisionPorcentaje;
	}
	public void setComisionPorcentaje(BigDecimal comisionPorcentaje) {
		this.comisionPorcentaje = comisionPorcentaje;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Integer getMeta() {
		return meta;
	}
	public void setMeta(Integer meta) {
		this.meta = meta;
	}
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof ComisionSupervisor) && (id != null)
            ? id.equals(((ComisionSupervisor) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
