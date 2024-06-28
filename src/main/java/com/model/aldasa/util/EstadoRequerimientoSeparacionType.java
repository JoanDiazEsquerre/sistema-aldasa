package com.model.aldasa.util;

public enum EstadoRequerimientoSeparacionType {

	APROBADO("Aprobado"), PENDIENTE("Pendiente"), ANULADO("Anulado"), RECHAZADO("Rechazado");
	
	private String descripcion;

	private EstadoRequerimientoSeparacionType (String descripcion) {
		this.descripcion=descripcion;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}
	
	
}
