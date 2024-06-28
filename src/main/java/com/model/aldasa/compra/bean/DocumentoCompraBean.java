package com.model.aldasa.compra.bean;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class DocumentoCompraBean extends BaseBean {
	
	private boolean estado;
	
	@PostConstruct
	public void init() {
		
	}

	
	
	
	
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
		
	
}
