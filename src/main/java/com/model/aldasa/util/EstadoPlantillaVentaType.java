package com.model.aldasa.util;

public enum EstadoPlantillaVentaType {
	
	PENDIENTE("Pendiente"),APROBADO("Aprobado"), TERMINADO("Terminado"), RECHAZADO("Rechazado"), ANULADO("Anulado");
	
	private String name;
	
	private EstadoPlantillaVentaType(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
}
