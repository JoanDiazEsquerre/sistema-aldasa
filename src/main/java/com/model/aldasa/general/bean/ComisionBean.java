package com.model.aldasa.general.bean;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Team;
import com.model.aldasa.service.ComisionProyectoService;
import com.model.aldasa.service.ConfiguracionComisionService;
import com.model.aldasa.service.MetaSupervisorService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class ComisionBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{configuracionComisionService}")
	private ConfiguracionComisionService configuracionComisionService;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService;
	
	@ManagedProperty(value = "#{metaSupervisorService}")
	private MetaSupervisorService metaSupervisorService;
	
	@ManagedProperty(value = "#{comisionProyectoService}")
	private ComisionProyectoService comisionProyectoService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	private LazyDataModel<ConfiguracionComision> lstComisionLazy;
	
	private List<Team> lstTeam;
	private List<MetaSupervisor> lstMetaSupervisor;
	private List<ComisionProyecto> lstComisionProyecto;
	private List<Project> lstProyecto;
	
	private ConfiguracionComision comisionSelected;
	private Team teamSelected;
	private MetaSupervisor metaSupervisorSelected;
	private ComisionProyecto comisionProyectoNew, comisionProyectoSelected;
	
	private String tituloDialog, anio;
	
	private Integer meta = null;
	
	private Date fechaIniFilter, fechaFinFilter;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy");
	
	@PostConstruct
	public void init() {
		iniciarComisionProyecto();
		anio=sdfY.format(new Date());
		getListaLazyComision();
		lstProyecto = projectService.findByStatusAndSucursalOrderByNameAsc(true, navegacionBean.getSucursalLogin());
	}
	
	public void eliminarComisionProyecto(ComisionProyecto cp) {
		cp.setEstado(false);
		comisionProyectoService.save(cp);
		listarComisionProyecto();
		addInfoMessage("Se eliminó correctamente.");
	}
	
	public void saveComisionProyecto() {
		if(comisionProyectoNew.getProyecto()==null) {
			addErrorMessage("Seleccionar un Proyecto");
			return;
		}else {
			ComisionProyecto cp = comisionProyectoService.findByConfiguracionComisionAndProyectoAndEstado(comisionSelected, comisionProyectoNew.getProyecto(), true);
			if(cp!=null) {
				addErrorMessage("Ya se agregó el proyecto.");
				return;
			}
		}
		
		if(comisionProyectoNew.getInteresContado()==null || comisionProyectoNew.getInteresContado().compareTo(BigDecimal.ZERO) <=0) {
			addErrorMessage("El interés al contado debe ser mayor que 0.");
			return;
		}
		
		if(comisionProyectoNew.getInteresCredito()==null || comisionProyectoNew.getInteresCredito().compareTo(BigDecimal.ZERO) <=0) {
			addErrorMessage("El interés al crédito debe ser mayor que 0.");
			return;
		}
		
		ComisionProyecto save = comisionProyectoService.save(comisionProyectoNew);
		if(save!=null) {
			addInfoMessage("Se guardó correctamente.");
			iniciarComisionProyecto();
			listarComisionProyecto();
		}
	}
	
	public void iniciarComisionProyecto() {
		comisionProyectoNew = new ComisionProyecto();
		comisionProyectoNew.setEstado(true);
		comisionProyectoNew.setConfiguracionComision(comisionSelected); 
	}
	
	public void listarComisionProyecto() {
		iniciarComisionProyecto();
		lstComisionProyecto = comisionProyectoService.findByConfiguracionComisionAndEstado(comisionSelected, true);
	}
	
	public void getListaLazyComision() {
		try {
			fechaIniFilter = sdf2.parse(anio+"-01-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fechaFinFilter = sdf2.parse(anio+"-12-31");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		iniciarLazy();
	}
	
	public void saveComision() {
		if(comisionSelected.getFechaInicio()==null || comisionSelected.getFechaFin()==null) {
			addErrorMessage("Completar todos los datos generales.");
			return ;
		}
		
		comisionSelected.setCodigo(sdfM.format(comisionSelected.getFechaInicio())+""+sdfY2.format(comisionSelected.getFechaInicio()));
				
		if(comisionSelected.getComisionContado()==null || comisionSelected.getComisionCredito()==null || comisionSelected.getVentasMetaContado()==null || 
				comisionSelected.getComisionContadoMeta()==null || comisionSelected.getMinimoVentaJunior()==null || comisionSelected.getMaximoVentaJunior()==null || 
				comisionSelected.getBonoJunior()==null || comisionSelected.getMinimoVentaSenior()==null || comisionSelected.getMaximoVentaSenior()==null || 
				comisionSelected.getBonoSenior()==null || comisionSelected.getMinimoVentaMaster()==null || comisionSelected.getMaximoVentaMaster()==null || 
				comisionSelected.getBonoMaster()==null){
			
			addErrorMessage("Completar los datos de Asesores Planilla.");
			return ;
		}
		
		if(comisionSelected.getComisionContadoExt()==null || comisionSelected.getComisionCreditoExt()==null || comisionSelected.getMinimoVentasExt()==null ||
				comisionSelected.getBonoVentaExt()==null || comisionSelected.getVentasMetaContadoExt()==null || comisionSelected.getComisionContadoMetaExt()==null) {
			
			addErrorMessage("Completar los datos de Asesores Externos.");			
			return ;
		}
		
		if(comisionSelected.getComisionContadoEmp()==null || comisionSelected.getComisionCreditoEmp()==null || comisionSelected.getMinimoVentasEmp()==null ||
				comisionSelected.getBonoVentaEmp()==null || comisionSelected.getVentasMetaContadoEmp()==null || comisionSelected.getComisionContadoMetaEmp()==null) {
			
			addErrorMessage("Completar los datos de Trabajadores Empresa.");			
			return ;
		}
		
		if(comisionSelected.getBonoSupervisor()==null || comisionSelected.getComisionSupervisor()==null) {
			addErrorMessage("Completar los datos de Supervisores.");			
			return ;
		}
		
		if(comisionSelected.getComisionJefeVenta()==null || comisionSelected.getPorcentajeBono()==null || comisionSelected.getBonoJefeVenta()==null || comisionSelected.getComisionJefeVentaMeta()==null) {
			addErrorMessage("Completar los datos de Jefe de Ventas.");			
			return ;
		}
		
		if(comisionSelected.getBonoCoordinador()==null) {
			addErrorMessage("Completar los datos de Coordinador.");			
			return ;
		}
		
		if(comisionSelected.getComisionSubgerente()==null) {
			addErrorMessage("Completar los datos de Subgerente.");			
			return ;
		}
		
		if (comisionSelected.getId() == null) {
			ConfiguracionComision validarExistencia = configuracionComisionService.findByEstadoAndCodigo(true, comisionSelected.getCodigo());
			if (validarExistencia == null) {
				comisionSelected.setBonojv(comisionSelected.getBonoJefeVentaObligatorio()); 
				comisionSelected.setMontoComisionjv(BigDecimal.ZERO);
				comisionSelected.setNumVendidojv(0);
				comisionSelected.setComisionPorcentajejv(comisionSelected.getComisionJefeVenta());
				comisionSelected.setMetajv(0);
				comisionSelected.setMontoComisionSubgerente(BigDecimal.ZERO); 
				
				configuracionComisionService.save(comisionSelected);
				addInfoMessage("Se guardo correctamente.");
				PrimeFaces.current().executeScript("PF('comisionDialog').hide();"); 
			}else { 
				addErrorMessage("Ya se programado una comision con el mismo mes y año(Fecha Inicio)."); 
			}
		} else {
			ConfiguracionComision validarExistencia = configuracionComisionService.findByCodigoAndIdException(comisionSelected.getCodigo(), comisionSelected.getId());
			if (validarExistencia == null) {
				configuracionComisionService.save(comisionSelected);
				addInfoMessage("Se guardo correctamente.");
				PrimeFaces.current().executeScript("PF('comisionDialog').hide();"); 
			} else { 
				addErrorMessage("Ya se programado una comision con el mismo mes y año(Fecha Inicio)."); 
			}
		}
		
	}

	public void saveMetaSupervisor() {
		if (teamSelected == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccionar equipo."));
			return ;
		}else if(teamSelected.getPersonSupervisor() == null){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El equipo no tiene supervisor."));
			return;
		}else {
			MetaSupervisor supervisor = metaSupervisorService.findByConfiguracionComisionAndEstadoAndPersonSupervisor(comisionSelected, true, teamSelected.getPersonSupervisor());
			if(supervisor != null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El supervisor ya esta registrado."));
				return;
			}
		}
		if(meta == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar meta."));
			return ;
		}
		
		MetaSupervisor guardaMeta = new MetaSupervisor();
		guardaMeta.setConfiguracionComision(comisionSelected);
		guardaMeta.setPersonSupervisor(teamSelected.getPersonSupervisor());
		guardaMeta.setMeta(meta);
		guardaMeta.setEstado(true);
		
		MetaSupervisor guardar = metaSupervisorService.save(guardaMeta);
		 if (guardar != null) {
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				cargarTeam();
		 }else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar."));
		 }
		
	
		
	}
	
	public void newComision() {
		tituloDialog="NUEVA COMISIÓN";
		comisionSelected=new ConfiguracionComision();
		setfechaInicioFin();
		
	
		comisionSelected.setComisionContado(new BigDecimal(8));
		comisionSelected.setComisionCredito(new BigDecimal(4));
		comisionSelected.setMinimoVentaJunior(2);
		comisionSelected.setMaximoVentaJunior(4); 
		comisionSelected.setBonoJunior(new BigDecimal(200));
		comisionSelected.setMinimoVentaSenior(5);
		comisionSelected.setMaximoVentaSenior(9);; 
		comisionSelected.setBonoSenior(new BigDecimal(500));
		comisionSelected.setMinimoVentaMaster(10);
		comisionSelected.setMaximoVentaMaster(100); 
		comisionSelected.setBonoMaster(new BigDecimal(1000));
		comisionSelected.setVentasMetaContado(5);
		comisionSelected.setComisionContadoMeta(new BigDecimal(10));
		
		comisionSelected.setComisionContadoExt(new BigDecimal(8));
		comisionSelected.setComisionCreditoExt(new BigDecimal(4));
		comisionSelected.setMinimoVentasExt(3);
		comisionSelected.setBonoVentaExt(new BigDecimal(1000));
		comisionSelected.setVentasMetaContadoExt(5); 
		comisionSelected.setComisionContadoMetaExt(new BigDecimal(10));
		
		comisionSelected.setComisionContadoEmp(new BigDecimal(8));
		comisionSelected.setComisionCreditoEmp(new BigDecimal(4));
		comisionSelected.setMinimoVentasEmp(3);
		comisionSelected.setBonoVentaEmp(BigDecimal.ZERO);
		comisionSelected.setVentasMetaContadoEmp(5); 
		comisionSelected.setComisionContadoMetaEmp(new BigDecimal(10));
		
		comisionSelected.setBonoSupervisor(new BigDecimal(2000));
		comisionSelected.setComisionSupervisor(BigDecimal.ONE); 
		
		comisionSelected.setComisionJefeVenta(new BigDecimal(0.50));  
		comisionSelected.setPorcentajeBono(new BigDecimal(70));
		comisionSelected.setBonoJefeVenta(new BigDecimal(1000)); 
		comisionSelected.setBonoJefeVentaObligatorio(new BigDecimal(1000));
		comisionSelected.setComisionJefeVentaMeta(BigDecimal.ONE);
		
		comisionSelected.setBonoCoordinador(new BigDecimal(2000));
		comisionSelected.setComisionSubgerente(BigDecimal.ONE);
		comisionSelected.setEstado(true); 
	}
	
	public void modifyComision( ) {
		tituloDialog="MODIFICAR COMISIÓN";
		
	}
	
	public void setfechaInicioFin() {
		try {
			Calendar calendar = Calendar.getInstance(); 

			//A la fecha actual le pongo el día 1
			calendar.set(Calendar.DAY_OF_MONTH,1);
			comisionSelected.setFechaInicio(calendar.getTime());

			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			
			int ultimoDiaMes=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			int mesActual = Integer.parseInt(sdfM.format(new Date())) ;
			int anioActual = Integer.parseInt(sdfY.format(new Date())) ;
			String fechaFinn=ultimoDiaMes + "/"+mesActual+"/"+anioActual;
			comisionSelected.setFechaFin(sdf.parse(fechaFinn)); 
			
		} catch (Exception e) {
			System.out.println("Error: "+e);
		}
	}

	public void iniciarLazy() {

		lstComisionLazy = new LazyDataModel<ConfiguracionComision>() {
			private List<ConfiguracionComision> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public ConfiguracionComision getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (ConfiguracionComision comision : datasource) {
                    if (comision.getId() == intRowKey) {
                        return comision;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(ConfiguracionComision comision) {
                return String.valueOf(comision.getId());
            }

			@Override
			public List<ConfiguracionComision> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
                Sort sort=Sort.by("fechaInicio").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }          
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
               
                Page<ConfiguracionComision> pageComision=null;
               
                
                pageComision= configuracionComisionService.findByEstadoAndFechaInicioBetween(true, fechaIniFilter,fechaFinFilter, pageable);
                
                setRowCount((int) pageComision.getTotalElements());
                return datasource = pageComision.getContent();
            }
		};
	}
	
	public void cargarTeam(){
		lstTeam = teamService.findByStatus(true);
		teamSelected = null;
		meta = null;
		lstMetaSupervisor = metaSupervisorService.findByConfiguracionComisionAndEstado(comisionSelected, true);
	}
	
	public void eliminarMetaSupervisor() {
		metaSupervisorSelected.setEstado(false);
		MetaSupervisor meta = metaSupervisorService.save(metaSupervisorSelected);
		if(meta != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se eliminó correctamente."));
			cargarTeam();
		}
	
	}
	
	public Converter getConversorTeam() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Team c = null;
                    for (Team si : lstTeam) {
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
                    return ((Team) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProyecto() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Project c = null;
                    for (Project si : lstProyecto) {
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
	

	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public ConfiguracionComisionService getConfiguracionComisionService() {
		return configuracionComisionService;
	}

	public void setConfiguracionComisionService(ConfiguracionComisionService configuracionComisionService) {
		this.configuracionComisionService = configuracionComisionService;
	}

	public LazyDataModel<ConfiguracionComision> getLstComisionLazy() {
		return lstComisionLazy;
	}

	public ConfiguracionComision getComisionSelected() {
		return comisionSelected;
	}

	public void setComisionSelected(ConfiguracionComision comisionSelected) {
		this.comisionSelected = comisionSelected;
	}

	public void setLstComisionLazy(LazyDataModel<ConfiguracionComision> lstComisionLazy) {
		this.lstComisionLazy = lstComisionLazy;
	}

	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public Date getFechaIniFilter() {
		return fechaIniFilter;
	}
	public void setFechaIniFilter(Date fechaIniFilter) {
		this.fechaIniFilter = fechaIniFilter;
	}
	public Date getFechaFinFilter() {
		return fechaFinFilter;
	}
	public void setFechaFinFilter(Date fechaFinFilter) {
		this.fechaFinFilter = fechaFinFilter;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public SimpleDateFormat getSdfM() {
		return sdfM;
	}
	public void setSdfM(SimpleDateFormat sdfM) {
		this.sdfM = sdfM;
	}
	public SimpleDateFormat getSdfY() {
		return sdfY;
	}
	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Team getTeamSelected() {
		return teamSelected;
	}
	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
	}
	public List<Team> getLstTeam() {
		return lstTeam;
	}
	public void setLstTeam(List<Team> lstTeam) {
		this.lstTeam = lstTeam;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public Integer getMeta() {
		return meta;
	}
	public void setMeta(Integer meta) {
		this.meta = meta;
	}
	public MetaSupervisorService getMetaSupervisorService() {
		return metaSupervisorService;
	}
	public void setMetaSupervisorService(MetaSupervisorService metaSupervisorService) {
		this.metaSupervisorService = metaSupervisorService;
	}
	public List<MetaSupervisor> getLstMetaSupervisor() {
		return lstMetaSupervisor;
	}
	public void setLstMetaSupervisor(List<MetaSupervisor> lstMetaSupervisor) {
		this.lstMetaSupervisor = lstMetaSupervisor;
	}
	public MetaSupervisor getMetaSupervisorSelected() {
		return metaSupervisorSelected;
	}
	public void setMetaSupervisorSelected(MetaSupervisor metaSupervisorSelected) {
		this.metaSupervisorSelected = metaSupervisorSelected;
	}
	public ComisionProyectoService getComisionProyectoService() {
		return comisionProyectoService;
	}
	public void setComisionProyectoService(ComisionProyectoService comisionProyectoService) {
		this.comisionProyectoService = comisionProyectoService;
	}
	public List<ComisionProyecto> getLstComisionProyecto() {
		return lstComisionProyecto;
	}
	public void setLstComisionProyecto(List<ComisionProyecto> lstComisionProyecto) {
		this.lstComisionProyecto = lstComisionProyecto;
	}
	public List<Project> getLstProyecto() {
		return lstProyecto;
	}
	public void setLstProyecto(List<Project> lstProyecto) {
		this.lstProyecto = lstProyecto;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public SimpleDateFormat getSdf2() {
		return sdf2;
	}
	public void setSdf2(SimpleDateFormat sdf2) {
		this.sdf2 = sdf2;
	}
	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}
	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public ComisionProyecto getComisionProyectoNew() {
		return comisionProyectoNew;
	}
	public void setComisionProyectoNew(ComisionProyecto comisionProyectoNew) {
		this.comisionProyectoNew = comisionProyectoNew;
	}
	public ComisionProyecto getComisionProyectoSelected() {
		return comisionProyectoSelected;
	}
	public void setComisionProyectoSelected(ComisionProyecto comisionProyectoSelected) {
		this.comisionProyectoSelected = comisionProyectoSelected;
	}
	
}
