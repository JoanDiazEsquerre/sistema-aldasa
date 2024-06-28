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
@Table(name = "comisionproyecto")
public class ComisionProyecto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idconfiguracioncomision")
	private ConfiguracionComision configuracionComision;
	
	@ManyToOne
	@JoinColumn(name="idproyecto")
	private Project proyecto;
	
	@Column(name="interescontado")
	private BigDecimal interesContado;
	
	@Column(name="interescredito")
	private BigDecimal interesCredito;

	private boolean estado;
	
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
	public Project getProyecto() {
		return proyecto;
	}
	public void setProyecto(Project proyecto) {
		this.proyecto = proyecto;
	}
	public BigDecimal getInteresContado() {
		return interesContado;
	}
	public void setInteresContado(BigDecimal interesContado) {
		this.interesContado = interesContado;
	}
	public BigDecimal getInteresCredito() {
		return interesCredito;
	}
	public void setInteresCredito(BigDecimal interesCredito) {
		this.interesCredito = interesCredito;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof ComisionProyecto) && (id != null)
            ? id.equals(((ComisionProyecto) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
}
