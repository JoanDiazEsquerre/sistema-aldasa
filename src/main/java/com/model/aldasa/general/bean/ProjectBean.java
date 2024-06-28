package com.model.aldasa.general.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.PrimeFaces;

import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Province;
import com.model.aldasa.entity.ProyectoPartida;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.service.CountryService;
import com.model.aldasa.service.DepartmentService;
import com.model.aldasa.service.DistrictService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProvinceService;
import com.model.aldasa.service.ProyectoPartidaService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class ProjectBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;

	@ManagedProperty(value = "#{departmentService}")
	private DepartmentService departmentService;

	@ManagedProperty(value = "#{provinceService}")
	private ProvinceService provinceService;

	@ManagedProperty(value = "#{districtService}")
	private DistrictService districtService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{proyectoPartidaService}")
	private ProyectoPartidaService proyectoPartidaService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	
	private List<Project> listProject;

	private Project projectSelected;
	private boolean estado = true;
	
	private Country countrySelected;
	private Department departmentSelected;
	private Province provinceSelected;
	private District districtSelected;
	private ProyectoPartida proyectoPartidaNew, proyectoPartidaSelected;
	
	private List<Country> lstCountry;
	private List<Department> lstDepartment;
	private List<Province> lstProvince;
	private List<District> lstDistrict;
	private List<ProyectoPartida> lstProyectoPartidaSelected;
	private List<Manzana> lstManzana;

	
	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		listarProject();
		listarPais();
		lstManzana = manzanaService.findByStatusOrderByNameAsc(true);
	}
	
	public void eliminarProyectoPartida() {
		proyectoPartidaSelected.setEstado(false);
		proyectoPartidaService.save(proyectoPartidaSelected);
		listarProyectoPartida();
		addInfoMessage("Se eliminó correctamente."); 
	}
	
	public void iniciarDatosProyectoPartidaNew() {
		proyectoPartidaNew = new ProyectoPartida();
		proyectoPartidaNew.setEstado(true);
		
		listarProyectoPartida();
	}
	
	public void saveProyectoPartida() {
		if(proyectoPartidaNew.getManzana()==null) {
			addErrorMessage("Seleccionar una manzana.");
			return;
		}else {
			ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, projectSelected, proyectoPartidaNew.getManzana());
			if(busqueda!=null) {
				addErrorMessage("Ya se ha registrado la manzana seleccionada."); 
				return;
			}
		}
		
		if(proyectoPartidaNew.getCodigoPredio().equals("")) {
			addErrorMessage("Ingresar codigo de predio."); 
			return;
		}
		
		if(proyectoPartidaNew.getAreaHectarea().equals("")) {
			addErrorMessage("Ingresar Area."); 
			return;
		}
		
		if(proyectoPartidaNew.getUnidadCatastral().equals("")) {
			addErrorMessage("Ingresar Unidad Catastral."); 
			return;
		}
		
		if(proyectoPartidaNew.getNumPartidaElectronica().equals("")) {
			addErrorMessage("Ingresar número de partida electrónica."); 
			return;
		}
		
		proyectoPartidaNew.setProyecto(projectSelected);
		
		
		proyectoPartidaService.save(proyectoPartidaNew);
		
		iniciarDatosProyectoPartidaNew();
		addInfoMessage("Se guardó correctamente.");
	}
	
	public void listarProyectoPartida() {
		lstProyectoPartidaSelected = proyectoPartidaService.findByEstadoAndProyecto(true, projectSelected);
	}
	
	public void listarPais() {
		lstCountry = countryService.findAll();
	}

	public void listarDepartamentos() {
		lstDepartment = new ArrayList<>();
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();

		if (countrySelected != null) {
			lstDepartment = departmentService.findByCountry(countrySelected);
		}
	}

	public void listarProvincias() {
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();

		if (departmentSelected != null) {
			lstProvince = provinceService.findByDepartment(departmentSelected);
		}
	}

	public void listarDistritos() {
		lstDistrict = new ArrayList<>();

		if (provinceSelected != null) {
			lstDistrict = districtService.findByProvince(provinceSelected);
		}
	}
	
	public void listarProject (){
		
		listProject= projectService.findByStatusAndSucursalOrderByNameAsc(estado, navegacionBean.getSucursalLogin());
		
	}
	
	public void newProject() {
		tituloDialog="NUEVO PROYECTO";
		projectSelected=new Project();
		projectSelected.setStatus(true);
		projectSelected.setName("");
		setearPais();
		
	}
	
	public void modifyProject( ) {
		tituloDialog="MODIFICAR PROYECTO";
		setearPais();
		if(projectSelected.getDistrict()!=null) {
			countrySelected = projectSelected.getDistrict().getProvince().getDepartment().getCountry();
			listarDepartamentos();
			departmentSelected = projectSelected.getDistrict().getProvince().getDepartment();
			listarProvincias();
			provinceSelected = projectSelected.getDistrict().getProvince();
			listarDistritos();
			districtSelected = projectSelected.getDistrict();
		}
	}
	
	public void setearPais() {
		countrySelected = null;
		departmentSelected = null;
		provinceSelected = null;
		districtSelected = null;
	}
	
	
	public void saveProject() {
		if(projectSelected.getName().equals("") || projectSelected.getName()==null) {
			addErrorMessage("Ingresar Nombre del proyecto.");
			listarProject();
			return ;
		} 
		
		if(projectSelected.getUbicacion().equals("") || projectSelected.getUbicacion()==null) {
			addErrorMessage("Ingresar ubicación.");
			return ;
		} 
		
		if(projectSelected.getSector().equals("") || projectSelected.getSector()==null) {
			addErrorMessage("Ingresar sector.");
			return ;
		} 
		
		if(projectSelected.getPredio().equals("") || projectSelected.getPredio()==null) {
			addErrorMessage("Ingresar predio.");
			return ;
		} 
		
		if(projectSelected.getCodigoPredio().equals("") || projectSelected.getCodigoPredio()==null) {
			addErrorMessage("Ingresar código de predio.");
			return ;
		} 
		
		if(projectSelected.getAreaHectarea().equals("") || projectSelected.getAreaHectarea()==null) {
			addErrorMessage("Ingresar área / hectárea.");
			return ;
		} 
		
		if(projectSelected.getUnidadCatastral().equals("") || projectSelected.getUnidadCatastral()==null) {
			addErrorMessage("Ingresar unidad catastral.");
			return ;
		} 
		
		if(projectSelected.getNumPartidaElectronica().equals("") || projectSelected.getNumPartidaElectronica()==null) {
			addErrorMessage("Ingresar N° de partida electrónica.");
			return ;
		} 
		
		if(projectSelected.getZonaRegistral().equals("") || projectSelected.getZonaRegistral()==null) {
			addErrorMessage("Ingresar zona registral.");
			return ;
		} 
		
		if(districtSelected==null ) {
			addErrorMessage("Seleccionar distrito.");
			return ;
		} else {
			projectSelected.setDistrict(districtSelected);
			
			}
		
		if (tituloDialog.equals("NUEVO PROYECTO")) {
			Project validarExistencia = projectService.findByName(projectSelected.getName());
			if (validarExistencia == null) {
				projectSelected.setSucursal(navegacionBean.getSucursalLogin());
				projectService.save(projectSelected);
				newProject();
				PrimeFaces.current().executeScript("PF('projectDialog').hide();");
				addInfoMessage("Se guardo correctamente.");
				listarProject();
			} else { 
				addErrorMessage("El proyecto ya existe.");
				listarProject();
			}
		} else {
			Project validarExistencia = projectService.findByNameException(projectSelected.getName(), projectSelected.getId());
			if (validarExistencia == null) {
				projectService.save(projectSelected);
				PrimeFaces.current().executeScript("PF('projectDialog').hide();");
				addInfoMessage("Se guardo correctamente.");
				listarProject();
			} else { 
				addErrorMessage("El proyecto ya existe.");
				listarProject();
			}
		}
		
	}
	
	public List<Manzana> completeManzana(String query) {
        List<Manzana> lista = new ArrayList<>();
        for (Manzana c : lstManzana) {
            if (c.getName().toUpperCase().contains(query.toUpperCase()) ) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public Converter getConversorCountry() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Country c = null;
					for (Country si : lstCountry) {
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
					return ((Country) value).getId() + "";
				}
			}
		};
	}

	public Converter getConversorDepartment() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Department c = null;
					for (Department si : lstDepartment) {
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
					return ((Department) value).getId() + "";
				}
			}
		};
	}
	
	public Converter getConversorProvince() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Province c = null;
					for (Province si : lstProvince) {
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
					return ((Province) value).getId() + "";
				}
			}
		};
	}
	
	public Converter getConversorDistrict() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					District c = null;
					for (District si : lstDistrict) {
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
					return ((District) value).getId() + "";
				}
			}
		};
	}
	
	
	public Converter getConversorManzana() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Manzana c = null;
					for (Manzana si : lstManzana) {
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
					return ((Manzana) value).getId() + "";
				}
			}
		};
	}
	
	
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	public Project getProjectSelected() {
		return projectSelected;
	}
	public void setProjectSelected(Project projectSelected) {
		this.projectSelected = projectSelected;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public void setListProject(List<Project> listProject) {
		this.listProject = listProject;
	}
	public List<Project> getListProject() {
		return listProject;
	}
	public Country getCountrySelected() {
		return countrySelected;
	}
	public void setCountrySelected(Country countrySelected) {
		this.countrySelected = countrySelected;
	}
	public Department getDepartmentSelected() {
		return departmentSelected;
	}
	public void setDepartmentSelected(Department departmentSelected) {
		this.departmentSelected = departmentSelected;
	}
	public Province getProvinceSelected() {
		return provinceSelected;
	}
	public void setProvinceSelected(Province provinceSelected) {
		this.provinceSelected = provinceSelected;
	}
	public District getDistrictSelected() {
		return districtSelected;
	}
	public void setDistrictSelected(District districtSelected) {
		this.districtSelected = districtSelected;
	}
	public List<Country> getLstCountry() {
		return lstCountry;
	}
	public void setLstCountry(List<Country> lstCountry) {
		this.lstCountry = lstCountry;
	}
	public List<Department> getLstDepartment() {
		return lstDepartment;
	}
	public void setLstDepartment(List<Department> lstDepartment) {
		this.lstDepartment = lstDepartment;
	}
	public List<Province> getLstProvince() {
		return lstProvince;
	}
	public void setLstProvince(List<Province> lstProvince) {
		this.lstProvince = lstProvince;
	}
	public List<District> getLstDistrict() {
		return lstDistrict;
	}
	public void setLstDistrict(List<District> lstDistrict) {
		this.lstDistrict = lstDistrict;
	}
	public CountryService getCountryService() {
		return countryService;
	}
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
	public DepartmentService getDepartmentService() {
		return departmentService;
	}
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	public ProvinceService getProvinceService() {
		return provinceService;
	}
	public void setProvinceService(ProvinceService provinceService) {
		this.provinceService = provinceService;
	}
	public DistrictService getDistrictService() {
		return districtService;
	}
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public ProyectoPartidaService getProyectoPartidaService() {
		return proyectoPartidaService;
	}
	public void setProyectoPartidaService(ProyectoPartidaService proyectoPartidaService) {
		this.proyectoPartidaService = proyectoPartidaService;
	}
	public List<ProyectoPartida> getLstProyectoPartidaSelected() {
		return lstProyectoPartidaSelected;
	}
	public void setLstProyectoPartidaSelected(List<ProyectoPartida> lstProyectoPartidaSelected) {
		this.lstProyectoPartidaSelected = lstProyectoPartidaSelected;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ProyectoPartida getProyectoPartidaNew() {
		return proyectoPartidaNew;
	}
	public void setProyectoPartidaNew(ProyectoPartida proyectoPartidaNew) {
		this.proyectoPartidaNew = proyectoPartidaNew;
	}
	public ProyectoPartida getProyectoPartidaSelected() {
		return proyectoPartidaSelected;
	}
	public void setProyectoPartidaSelected(ProyectoPartida proyectoPartidaSelected) {
		this.proyectoPartidaSelected = proyectoPartidaSelected;
	}

	
	
}
