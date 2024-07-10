package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.CuotaService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class InicioBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String texto1, texto2, texto3, texto4;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{cuotaService}")
	private CuotaService cuotaService;

	
	private Manzana manzanaFilter;
	
	@PostConstruct
	public void init() {
		
		texto1 = "Cada uno según el don que ha recibido,";
		texto2 = "adminístrelo a los otros,";
		texto3 = "como buenos dispensadores de";
		texto4 = "las diferentes gracias de Dios. (Pedro 4:10)";
		
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "refreshPage?faces-redirect=true");
		
	}
	
	
	
	public String getTexto1() {
		return texto1;
	}
	public void setTexto1(String texto1) {
		this.texto1 = texto1;
	}
	public String getTexto2() {
		return texto2;
	}
	public void setTexto2(String texto2) {
		this.texto2 = texto2;
	}
	public String getTexto3() {
		return texto3;
	}
	public void setTexto3(String texto3) {
		this.texto3 = texto3;
	}
	public String getTexto4() {
		return texto4;
	}
	public void setTexto4(String texto4) {
		this.texto4 = texto4;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public Manzana getManzanaFilter() {
		return manzanaFilter;
	}
	public void setManzanaFilter(Manzana manzanaFilter) {
		this.manzanaFilter = manzanaFilter;
	}

	public CuotaService getCuotaService() {
		return cuotaService;
	}
	public void setCuotaService(CuotaService cuotaService) {
		this.cuotaService = cuotaService;
	}
	
}
