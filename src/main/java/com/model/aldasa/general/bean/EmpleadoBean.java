package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Cargo;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.FondoPension;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Team;
import com.model.aldasa.service.AreaService;
import com.model.aldasa.service.CargoService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.FondoPensionService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class EmpleadoBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService; 
	
	@ManagedProperty(value = "#{areaService}")
	private AreaService areaService; 
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService; 
	
	@ManagedProperty(value = "#{cargoService}")
	private CargoService cargoService; 
	
	@ManagedProperty(value = "#{sucursalService}")
	private SucursalService sucursalService;
	
	@ManagedProperty(value = "#{fondoPensionService}")
	private FondoPensionService fondoPensionService;
		
	private LazyDataModel<Empleado> lstEmpleadoLazy;
	
	private List<Person> lstPerson;
	private List<Area> lstArea;
	private List<Team> lstTeam;
	private List<Cargo> lstCargo;
	private List<Sucursal> lstSucursal;
	private List<FondoPension> lstFondoPension;
	
	private Empleado empleadoSelected;
	private Cargo cargoFilter;
	private Area areaFilter;
	private boolean estado = true;
	
	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		iniciarLazy();
		listarPersonas();
		lstArea=areaService.findByEstadoOrderByNombreAsc(true);
		lstTeam=teamService.findByStatus(true);
		lstCargo=cargoService.findByEstadoOrderByDescripcionAsc(true);
		lstSucursal =sucursalService.findByEstado(true);
		lstFondoPension =fondoPensionService.findByEstado(true);
	}
	
	public void cambiarPlanilla() {
		if(!empleadoSelected.isPlanilla()) {
			empleadoSelected.setFondoPension(null);
		}
	}
	public void newEmpleado() {
		tituloDialog="NUEVO EMPLEADO";
		empleadoSelected = new Empleado();
		empleadoSelected.setEstado(true);
		empleadoSelected.setExterno(false);
		empleadoSelected.setPlanilla(false); 
		empleadoSelected.setSucursal(navegacionBean.getSucursalLogin());

	}


	public void modifyEmpleado( ) {
		tituloDialog="MODIFICAR EMPLEADO";
	}
	
	public void saveEmpleado() {
		if(empleadoSelected.getPerson()==null) {
			addErrorMessage("Ingresar empleado.");
			return ;
		}
		if(empleadoSelected.getSueldoBasico()==null) {
			addErrorMessage("Ingresar sueldo básico.");
			return ;
		}
		if(empleadoSelected.getArea()==null) {
			addErrorMessage("Seleccionar área.");
			return ;
		}
		if(empleadoSelected.getCargo().equals("")) {
			addErrorMessage("Ingresar cargo.");
			return ;
		}
		if(empleadoSelected.getFechaIngreso()==null && empleadoSelected.getFechaSalida()==null) {
			addErrorMessage("Seleccionar fechas.");
			return ;
		}
		
		if(empleadoSelected.isPlanilla()==true) {
			if(empleadoSelected.getFondoPension()==null) {
				addErrorMessage("El empleado tiene plantilla, seleccionar un fonde de pensión.");
				return;
			}
		}else {
			empleadoSelected.setFondoPension(null);
		}
		
		if (tituloDialog.equals("NUEVO EMPLEADO")) {
			Empleado validarExistencia = empleadoService.findByPersonId(empleadoSelected.getPerson().getId());
			if (validarExistencia == null) {
				empleadoService.save(empleadoSelected);
				PrimeFaces.current().executeScript("PF('empleadoDialog').hide();");
				addInfoMessage("Se guardo correctamente.");
				newEmpleado();
			} else { 
				addErrorMessage("Persona ya se encuentra registrado como empleado.");
			}
		} else {
			Empleado validarExistencia = empleadoService.findByPersonIdException(empleadoSelected.getPerson().getId() , empleadoSelected.getId());
			if (validarExistencia == null) {
				empleadoService.save(empleadoSelected);
				PrimeFaces.current().executeScript("PF('empleadoDialog').hide();");
				addInfoMessage("Se guardo correctamente.");
			} else { 
				addErrorMessage("Persona ya se encuentra registrado como empleado.");
			}
		}
		
	}
	
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}
	
	public void iniciarLazy() {

		lstEmpleadoLazy = new LazyDataModel<Empleado>() {
			private List<Empleado> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Empleado getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Empleado empleado : datasource) {
                    if (empleado.getId() == intRowKey) {
                        return empleado;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Empleado empleado) {
                return String.valueOf(empleado.getId());
            }

			@Override
			public List<Empleado> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("person.surnames").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }
                
                String cargo = "%%";
                String area = "%%";
                
                if(cargoFilter!=null) {
                	cargo = "%"+cargoFilter.getDescripcion()+"%";
                }
                
                if(areaFilter!=null) {
                	area = "%"+areaFilter.getNombre()+"%";
                }
                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
               
                Page<Empleado> pageEmpleado=null;
               
                
                pageEmpleado= empleadoService.findByPersonSurnamesLikeAndEstadoAndCargoDescripcionLikeAndAreaNombreLike(names, estado, cargo, area, pageable);
                
                setRowCount((int) pageEmpleado.getTotalElements());
                return datasource = pageEmpleado.getContent();
            }
		};
	}

	public Converter getConversorPersonSurnames() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Person c = null;
                    for (Person si : lstPerson) {
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
                    return ((Person) value).getId() + "";
                }
            }
        };
    }
	
	public List<Person> completePersonSurnames(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : getLstPerson()) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }	
	
	public Converter getConversorArea() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Area c = null;
                    for (Area si : lstArea) {
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
                    return ((Area) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorCargo() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Cargo c = null;
                    for (Cargo si : lstCargo) {
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
                    return ((Cargo) value).getId() + "";
                }
            }
        };
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
	
	public Converter getConversorSucursal() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Sucursal c = null;
                    for (Sucursal si : lstSucursal) {
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
                    return ((Sucursal) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorFondoPension() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	FondoPension c = null;
                    for (FondoPension si : lstFondoPension) {
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
                    return ((FondoPension) value).getId() + "";
                }
            }
        };
    }
	
	public Empleado getEmpleadoSelected() {
		return empleadoSelected;
	}
	public void setEmpleadoSelected(Empleado empleadoSelected) {
		this.empleadoSelected = empleadoSelected;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public LazyDataModel<Empleado> getLstEmpleadoLazy() {
		return lstEmpleadoLazy;
	}
	public void setLstEmpleadoLazy(LazyDataModel<Empleado> lstEmpleadoLazy) {
		this.lstEmpleadoLazy = lstEmpleadoLazy;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public List<Area> getLstArea() {
		return lstArea;
	}
	public void setLstArea(List<Area> lstArea) {
		this.lstArea = lstArea;
	}
	public AreaService getAreaService() {
		return areaService;
	}
	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public List<Team> getLstTeam() {
		return lstTeam;
	}
	public void setLstTeam(List<Team> lstTeam) {
		this.lstTeam = lstTeam;
	}
	public CargoService getCargoService() {
		return cargoService;
	}
	public void setCargoService(CargoService cargoService) {
		this.cargoService = cargoService;
	}
	public List<Cargo> getLstCargo() {
		return lstCargo;
	}
	public void setLstCargo(List<Cargo> lstCargo) {
		this.lstCargo = lstCargo;
	}
	public Cargo getCargoFilter() {
		return cargoFilter;
	}
	public void setCargoFilter(Cargo cargoFilter) {
		this.cargoFilter = cargoFilter;
	}
	public Area getAreaFilter() {
		return areaFilter;
	}
	public void setAreaFilter(Area areaFilter) {
		this.areaFilter = areaFilter;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public List<Sucursal> getLstSucursal() {
		return lstSucursal;
	}
	public void setLstSucursal(List<Sucursal> lstSucursal) {
		this.lstSucursal = lstSucursal;
	}
	public FondoPensionService getFondoPensionService() {
		return fondoPensionService;
	}
	public void setFondoPensionService(FondoPensionService fondoPensionService) {
		this.fondoPensionService = fondoPensionService;
	}
	public List<FondoPension> getLstFondoPension() {
		return lstFondoPension;
	}
	public void setLstFondoPension(List<FondoPension> lstFondoPension) {
		this.lstFondoPension = lstFondoPension;
	}
	
}
