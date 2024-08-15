package com.model.aldasa.util;

public enum EstadoRequerimientoSeparacionType {

	EN_PROCESO("En Proceso"), PENDIENTE("Pendiente"), ANULADO("Anulado"), RECHAZADO("Rechazado"), TERMINADO("Terminado"), SIN_ASIGNAR("Sin Asignar"), VENCIDO("Vencido");
	
	private String descripcion;

	private EstadoRequerimientoSeparacionType (String descripcion) {
		this.descripcion=descripcion;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}
	
	
}
