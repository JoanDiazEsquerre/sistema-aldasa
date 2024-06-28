package com.model.aldasa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "metasupervisor")
public class MetaSupervisor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idpersonsupervisor")
	Person personSupervisor;
	
	@ManyToOne
    @JoinColumn(name="idconfiguracioncomision")
    private ConfiguracionComision configuracionComision;
	
	private int meta;
	
	private boolean estado;

	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Person getPersonSupervisor() {
		return personSupervisor;
	}
	public void setPersonSupervisor(Person personSupervisor) {
		this.personSupervisor = personSupervisor;
	}
	public ConfiguracionComision getConfiguracionComision() {
		return configuracionComision;
	}
	public void setConfiguracionComision(ConfiguracionComision configuracionComision) {
		this.configuracionComision = configuracionComision;
	}
	public int getMeta() {
		return meta;
	}
	public void setMeta(int meta) {
		this.meta = meta;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}


}
