package com.model.aldasa.util;

public enum EstadoContrato {
	
	ACTIVO("Activo"),TERMINADO("Terminado"), RESUELTO("Resuelto"),ANULADO("Anulado");
	
	private String name;
	
	private EstadoContrato(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
}
