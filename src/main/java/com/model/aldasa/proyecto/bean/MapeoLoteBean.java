package com.model.aldasa.proyecto.bean;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.PrimeFaces;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class MapeoLoteBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	private List<Project> lstProject = new ArrayList<>();
	private List<Manzana> lstManzana = new ArrayList<>();
	private List<Lote> lstLotes;
	
	private Project projectFilter;
	private Manzana manzanaFilterMapeo;
	private Lote loteSelected;

	private int cantidadLotes=0;
	
	@PostConstruct
	public void init() {
		lstProject= projectService.findByStatusOrderByNameAsc(true);
		projectFilter = lstProject.get(0);
		listarLotes();
	}
	
	
	
	
	public void listarLotes(){	
		listarManzanas();
		lstLotes = new ArrayList<>();
		if(!lstManzana.isEmpty()) {
			for(Manzana m:lstManzana) {
				List<Lote> lstLotesPorMz = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(projectFilter, m, "%%");
				if(!lstLotesPorMz.isEmpty()) {
					lstLotes.addAll(lstLotesPorMz);
					Lote lot = new Lote();
					lot.setId(0);
					lot.setNumberLote("x");
					lstLotes.add(lot);
				}
			}
		}
//		lstLotes= loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(projectFilter,manzanaFilterMapeo, "%%");
		cantidadLotes=0;
		
		if(!lstLotes.isEmpty()) {
			
			cantidadLotes = lstLotes.size();
			
		}
	}
	
	public void listarManzanas (){
	
		lstManzana= manzanaService.findByProject(projectFilter.getId());
		
		
//		manzanaFilterMapeo = null;
//		if(!lstManzana.isEmpty() && lstManzana != null) {
			manzanaFilterMapeo = lstManzana.get(0);
//		}
		
	}
	
	
	public Converter getConversorProject() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Project c = null;
                    for (Project si : lstProject) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Project) value).getId() + "";
                }
            }
        };
    }


	public Project getProjectFilter() {
		return projectFilter;
	}
	public void setProjectFilter(Project projectFilter) {
		this.projectFilter = projectFilter;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public List<Project> getLstProject() {
		return lstProject;
	}
	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public List<Manzana> getLstManzana() {
		return lstManzana;
	}
	public void setLstManzana(List<Manzana> lstManzana) {
		this.lstManzana = lstManzana;
	}
	public Manzana getManzanaFilterMapeo() {
		return manzanaFilterMapeo;
	}
	public void setManzanaFilterMapeo(Manzana manzanaFilterMapeo) {
		this.manzanaFilterMapeo = manzanaFilterMapeo;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public List<Lote> getLstLotes() {
		return lstLotes;
	}
	public void setLstLotes(List<Lote> lstLotes) {
		this.lstLotes = lstLotes;
	}
	public int getCantidadLotes() {
		return cantidadLotes;
	}
	public void setCantidadLotes(int cantidadLotes) {
		this.cantidadLotes = cantidadLotes;
	}
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}	
	
}
