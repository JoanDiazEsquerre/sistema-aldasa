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
@Table(name = "user")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String username;
	private String password;
	boolean status;
	
	@ManyToOne
	@JoinColumn(name="idperson")
	private Person person;
	
	@ManyToOne
	@JoinColumn(name="idprofile")
	private Profile profile;
	
	@ManyToOne
	@JoinColumn(name="idteam")
	private Team team;
	
	@Column(name="modificarcronograma")
	private boolean modificarCronograma;
	
	@Column(name="aprobarplantillaventa")
	private boolean aprobarPlantillaVenta;
	
	@Column(name="descargarreporteventa")
	private boolean descargarReporteVenta;
	
	@Column(name="modificarvoucher")
	private boolean modificarVoucher;
	
	@Column(name="aprobarrequerimientoseparacion")
	private boolean aprobarRequerimientoSeparacion;
	
	@Column(name="aprobarordencompra")
	private boolean aprobarOrdenCompra;
	
	@Column(name="modolecturamantvoucher")
	private boolean modoLecturaMantVoucher;
	
	@Column(name="modificarmovimientocargo")
	private boolean modificarMovimientoCargo;
	
	@Column(name="verificardatosvoucher")
	private boolean verificarDatosVoucher;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public boolean isModificarCronograma() {
		return modificarCronograma;
	}
	public void setModificarCronograma(boolean modificarCronograma) {
		this.modificarCronograma = modificarCronograma;
	}
	public boolean isAprobarPlantillaVenta() {
		return aprobarPlantillaVenta;
	}
	public void setAprobarPlantillaVenta(boolean aprobarPlantillaVenta) {
		this.aprobarPlantillaVenta = aprobarPlantillaVenta;
	}
	public boolean isDescargarReporteVenta() {
		return descargarReporteVenta;
	}
	public void setDescargarReporteVenta(boolean descargarReporteVenta) {
		this.descargarReporteVenta = descargarReporteVenta;
	}
	public boolean isModificarVoucher() {
		return modificarVoucher;
	}
	public void setModificarVoucher(boolean modificarVoucher) {
		this.modificarVoucher = modificarVoucher;
	}
	public boolean isAprobarRequerimientoSeparacion() {
		return aprobarRequerimientoSeparacion;
	}
	public void setAprobarRequerimientoSeparacion(boolean aprobarRequerimientoSeparacion) {
		this.aprobarRequerimientoSeparacion = aprobarRequerimientoSeparacion;
	}
	public boolean isAprobarOrdenCompra() {
		return aprobarOrdenCompra;
	}
	public void setAprobarOrdenCompra(boolean aprobarOrdenCompra) {
		this.aprobarOrdenCompra = aprobarOrdenCompra;
	}
	public boolean isModoLecturaMantVoucher() {
		return modoLecturaMantVoucher;
	}
	public void setModoLecturaMantVoucher(boolean modoLecturaMantVoucher) {
		this.modoLecturaMantVoucher = modoLecturaMantVoucher;
	}
	public boolean isModificarMovimientoCargo() {
		return modificarMovimientoCargo;
	}
	public void setModificarMovimientoCargo(boolean modificarMovimientoCargo) {
		this.modificarMovimientoCargo = modificarMovimientoCargo;
	}
	public boolean isVerificarDatosVoucher() {
		return verificarDatosVoucher;
	}
	public void setVerificarDatosVoucher(boolean verificarDatosVoucher) {
		this.verificarDatosVoucher = verificarDatosVoucher;
	}

	
}
