package com.model.aldasa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "project")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	private boolean status;
	
	private String ubicacion;
	
	private String sector;

	private String predio;
	
	@Column(name="codigopredio")
	private String codigoPredio;
	
	@Column(name="areahectarea")
	private String areaHectarea;
	
	@Column(name="unidadcatastral")
	private String unidadCatastral;
	
	@ManyToOne
    @JoinColumn(name="iddistrict")
    private District district;
	
	@Column(name="numpartidaelectronica")
	private String numPartidaElectronica;
	
	@Column(name="zonaregistral")
	private String zonaRegistral;
	
	@ManyToOne
    @JoinColumn(name="idsucursal")
    private Sucursal sucursal;
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getPredio() {
		return predio;
	}
	public void setPredio(String predio) {
		this.predio = predio;
	}
	public String getCodigoPredio() {
		return codigoPredio;
	}
	public void setCodigoPredio(String codigoPredio) {
		this.codigoPredio = codigoPredio;
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
	public District getDistrict() {
		return district;
	}
	public void setDistrict(District district) {
		this.district = district;
	}
	public String getNumPartidaElectronica() {
		return numPartidaElectronica;
	}
	public void setNumPartidaElectronica(String numPartidaElectronica) {
		this.numPartidaElectronica = numPartidaElectronica;
	}
	public String getZonaRegistral() {
		return zonaRegistral;
	}
	public void setZonaRegistral(String zonaRegistral) {
		this.zonaRegistral = zonaRegistral;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	@Override
    public boolean equals(Object other) {
        return (other instanceof Project) && (id != null)
            ? id.equals(((Project) other).id)
            : (other == this);
    }



   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	
	

}
