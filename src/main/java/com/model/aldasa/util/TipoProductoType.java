package com.model.aldasa.util;

public enum TipoProductoType {
	
	CUOTA("C"),INTERES("T"), INICIAL("I"), SEPARACION("S"), AMORTIZACION("P"), ADELANTO("A");
	
	private String tipo;
	
	private TipoProductoType(String tipo) {
		this.tipo=tipo;
	}
	
	public String getTipo() {
		return tipo;
	}

}
