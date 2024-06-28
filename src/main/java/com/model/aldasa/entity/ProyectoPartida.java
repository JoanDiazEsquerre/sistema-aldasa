package com.model.aldasa.entity;

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
@Table(name = "proyectopartida")
public class ProyectoPartida {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idmanzana")
	private Manzana manzana;
	
	@ManyToOne
	@JoinColumn(name="idproyecto")
	private Project proyecto;
	
	@Column(name="areahectarea")
	private String areaHectarea;
	
	@Column(name="unidadcatastral")
	private String unidadCatastral;

	@Column(name="numpartidaelectronica")
	private String numPartidaElectronica;
	
	@Column(name="codigopredio")
	private String codigoPredio;
	
	@Column(name="estado")
	private boolean estado;
	
	
	
	

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Manzana getManzana() {
		return manzana;
	}

	public void setManzana(Manzana manzana) {
		this.manzana = manzana;
	}

	public Project getProyecto() {
		return proyecto;
	}

	public void setProyecto(Project proyecto) {
		this.proyecto = proyecto;
	}

	public String getAreaHectarea() {
		return areaHectarea;
	}

	public void setAreaHectarea(String areaHectarea) {
		this.areaHectarea = areaHectarea;
	}

	public String getUnidadCatastral() {
		return unidadCatastral;
	}

	public void setUnidadCatastral(String unidadCatastral) {
		this.unidadCatastral = unidadCatastral;
	}

	public String getNumPartidaElectronica() {
		return numPartidaElectronica;
	}

	public void setNumPartidaElectronica(String numPartidaElectronica) {
		this.numPartidaElectronica = numPartidaElectronica;
	}

	public String getCodigoPredio() {
		return codigoPredio;
	}

	public void setCodigoPredio(String codigoPredio) {
		this.codigoPredio = codigoPredio;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	
	

}
