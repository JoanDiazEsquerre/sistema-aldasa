package com.model.aldasa.general.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.UsuarioService;

@ManagedBean
@ViewScoped
public class PasswordchangeBean implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	private String passActual="";
	private String passNueva="";
	private String passConfNueva="";
	private Usuario usuarioLogin = new Usuario();
	
	@PostConstruct
	public void init() {
		usuarioLogin = navegacionBean.getUsuarioLogin();

	}
	

	public void save() {
//		 Usuario usuario = usuarioService.findByUsername(usuarioLogin.getUsername());
		 
		if (passActual.equals("") || passActual==null) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar contraseña actual."));
			return;
		}
		if (passNueva.equals("") || passNueva==null) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar contraseña nueva."));
			return;
		}
		if (passConfNueva.equals("") || passConfNueva==null) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta confirmar la contraseña nueva."));
			return;
		}
		if (!passActual.equals(usuarioLogin.getPassword()) ) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contraseña actual incorrecta."));
			return;
		}
		if (!passNueva.equals(passConfNueva)) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contraseña nueva y de confirmación tienen que ser iguales."));
			return;
		}
		
		
		usuarioLogin.setPassword(passNueva);
		usuarioService.save(usuarioLogin);
		FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmacion", "Se guardo correctamente la contraseña."));
		passActual="";
		passNueva="";
		passConfNueva="";
	}
	
	public String getPassActual() {
		return passActual;
	}

	public void setPassActual(String passActual) {
		this.passActual = passActual;
	}

	public String getPassNueva() {
		return passNueva;
	}

	public void setPassNueva(String passNueva) {
		this.passNueva = passNueva;
	}

	public String getPassConfNueva() {
		return passConfNueva;
	}

	public void setPassConfNueva(String passConfNueva) {
		this.passConfNueva = passConfNueva;
	}

	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}

	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	
	
	
	
}
