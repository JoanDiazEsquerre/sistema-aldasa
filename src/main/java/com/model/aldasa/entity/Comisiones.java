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
@Table(name = "comisiones")
public class Comisiones {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idcomisionsupervisor")
	private ComisionSupervisor comisionSupervisor;
	
	@ManyToOne
	@JoinColumn(name="idpersonaasesor")
	private Person personaAsesor;
	
	@Column(name="bono")
	private BigDecimal bono;
	
	@Column(name="montocomision")
	private BigDecimal montoComision;

	@Column(name="numvendido")
	private Integer numVendido;
	
	private boolean estado;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	
	
	public ComisionSupervisor getComisionSupervisor() {
		return comisionSupervisor;
	}
	public void setComisionSupervisor(ComisionSupervisor comisionSupervisor) {
		this.comisionSupervisor = comisionSupervisor;
	}
	public Person getPersonaAsesor() {
		return personaAsesor;
	}
	public void setPersonaAsesor(Person personaAsesor) {
		this.personaAsesor = personaAsesor;
	}
	@Override
    public boolean equals(Object other) {
        return (other instanceof Comisiones) && (id != null)
            ? id.equals(((Comisiones) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
