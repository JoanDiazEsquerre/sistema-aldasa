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
@Table(name = "asistencia")
public class Asistencia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="idempleado")
	private Empleado empleado;
	
	private String tipo;
	
	private Date hora;
	
	@ManyToOne
	@JoinColumn(name="idusercrea")
	private Usuario userCrea;
	
	@Column(name="fechacrea")
	private Date fechaCrea;

	@ManyToOne
	@JoinColumn(name="idusermodifica")
	private Usuario userModifica;
	
	@Column(name="fechamodifica")
	private Date fechaModifica;
	
	private boolean estado;

	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Date getHora() {
		return hora;
	}
	public void setHora(Date hora) {
		this.hora = hora;
	}
	public Usuario getUserCrea() {
		return userCrea;
	}
	public void setUserCrea(Usuario userCrea) {
		this.userCrea = userCrea;
	}
	public Date getFechaCrea() {
		return fechaCrea;
	}
	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}
	public Usuario getUserModifica() {
		return userModifica;
	}
	public void setUserModifica(Usuario userModifica) {
		this.userModifica = userModifica;
	}
	public Date getFechaModifica() {
		return fechaModifica;
	}
	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof Asistencia) && (id != null)
            ? id.equals(((Asistencia) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	
}
