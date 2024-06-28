package com.model.aldasa.util;

public enum Perfiles {
	
	ADMINISTRADOR("Administrador",1),
	ASESOR("Asesor",2), 
	SUPERVISOR("Supervisor",3),
	ASISTENTE_ADMINISTRATIVO("Asistente Administrativo",4),
	ASISTENTE_COBRANZA("Asistente Cobranza",5),
	RECURSOS_HUMANOS("Recursos Humanos",6),
	CONTABILIDAD("Contabilidad", 7),
	COBRANZA("Cobranza", 8),
	ASISTENCIA("Asistencia",11),
	ASISTENTE_VENTA("Asistente de Venta",12);
	
	private String name;
	private int id;
	
	private Perfiles(String name,int id) {
		this.name=name;
		this.id=id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

}
