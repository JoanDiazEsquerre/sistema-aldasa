package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class CuentaBancariaBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void init() {
		
	}

}
