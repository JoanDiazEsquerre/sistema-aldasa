package com.model.aldasa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tipomovimiento")
public class TipoMovimiento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre, tipo;
	
	private boolean estado;

	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
	
	@Override
    public boolean equals(Object other) {
        return (other instanceof TipoMovimiento) && (id != null)
            ? id.equals(((TipoMovimiento) other).id)
            : (other == this);
    }
	
	
   @Override
    public int hashCode() {
        return (id != null)
            ? (this.getClass().hashCode() + id.hashCode())
            : super.hashCode();
    }
	
}
