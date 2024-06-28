package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.Province;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.CountryService;
import com.model.aldasa.service.DepartmentService;
import com.model.aldasa.service.DistrictService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.service.ProspectionService;
import com.model.aldasa.service.ProvinceService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class ProspectoBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{prospectService}")
	private ProspectService prospectService;
	
	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;
	
	@ManagedProperty(value = "#{departmentService}")
	private DepartmentService departmentService;
	
	@ManagedProperty(value = "#{provinceService}")
	private ProvinceService provinceService;
	
	@ManagedProperty(value = "#{districtService}")
	private DistrictService districtService;
	
	@ManagedProperty(value = "#{prospectionService}")
	private ProspectionService prospectionService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	private LazyDataModel<Prospect> lstProspectLazy;
	private Prospect prospectSelected;
	private Person personNew;
	
	private Country countrySelected;
	private Department departmentSelected;
	private Province provinceSelected;
	private District districtSelected;
	
	private List<Country> lstCountry= new ArrayList<>();
	private List<Department> lstDepartment = new ArrayList<>();
	private List<Province> lstProvince= new ArrayList<>();
	private List<District> lstDistrict= new ArrayList<>();
	
	private String username,tituloDialog;
	private Usuario usuarioLogin = new Usuario();

	@PostConstruct
	public void init() {
		usuarioLogin = navegacionBean.getUsuarioLogin();
		iniciarLazy();
		listarPais();
	}
	
	public void listarPais() {
		lstCountry = countryService.findAll();
	}
	
	public void listarProvincias() {
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();
		
		if(departmentSelected !=null) {
			lstProvince = provinceService.findByDepartment(departmentSelected); 
		}
	}
	
	public void listarDepartamentos() {
		lstDepartment = new ArrayList<>();
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();
		
		if(countrySelected !=null) {
			lstDepartment = departmentService.findByCountry(countrySelected);
		}
	}
	public void listarDistritos() {
		lstDistrict = new ArrayList<>();
		
		if(provinceSelected!=null) {
			lstDistrict = districtService.findByProvince(provinceSelected); 
		}
	}
	public void onPageLoad(){
		usuarioLogin = usuarioService.findByUsername(navegacionBean.getUsername());
		iniciarLazy();
	}
	
	public void iniciarLazy() {
		lstProspectLazy = new LazyDataModel<Prospect>() {
			private List<Prospect> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Prospect getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Prospect prospect : datasource) {
                    if (prospect.getId() == intRowKey) {
                        return prospect;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Prospect prospect) {
                return String.valueOf(prospect.getId());
            }

			@Override
			public List<Prospect> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				String dni="%"+ (filterBy.get("person.dni")!=null?filterBy.get("person.dni").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String surnamesPerson="%"+ (filterBy.get("person.surnames")!=null?filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
//				String surnamesAssessor="%"+ (filterBy.get("personAssessor.surnames")!=null?filterBy.get("personAssessor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
//				String surnamesSupervisor="%"+ (filterBy.get("personSupervisor.surnames")!=null?filterBy.get("personSupervisor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";

				 Sort sort=Sort.by("person.dni").ascending();
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
				//Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE, 
				//Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
				//Tu deberias preparar el metodo para cada filtro que tengas en la tabla
				
				Page<Prospect> pagePerson = null; 
				
				if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
					// si es asesor
					if(!dni.equals("")) {
						pagePerson= prospectService.findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessorAndSucursal(dni, surnamesPerson, usuarioLogin.getPerson(), navegacionBean.getSucursalLogin(), pageable); 
					}else {
						pagePerson= prospectService.findByPersonSurnamesLikeAndPersonAssessorAndSucursal(surnamesPerson, usuarioLogin.getPerson(), navegacionBean.getSucursalLogin(), pageable); 
					}
					
				}else if(usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())){
					// Es supervisor
					if(!dni.equals("")) {
						pagePerson= prospectService.findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisorAndSucursal(dni, surnamesPerson, usuarioLogin.getPerson(), navegacionBean.getSucursalLogin(), pageable); 
					}else {
						pagePerson= prospectService.findByPersonSurnamesLikeAndPersonSupervisorAndSucursal(surnamesPerson, usuarioLogin.getPerson(), navegacionBean.getSucursalLogin(), pageable); 
					}
				}else {
					// si es Administrador
					if(!dni.equals("")) {
						pagePerson= prospectService.findByPersonSurnamesLikeAndPersonDniLikeAndSucursal(surnamesPerson,dni,navegacionBean.getSucursalLogin(),pageable);
					}else {
						pagePerson= prospectService.findByPersonSurnamesLikeAndSucursal(surnamesPerson,navegacionBean.getSucursalLogin(),pageable);
					}
				}
				
				
				setRowCount((int) pagePerson.getTotalElements());
				return datasource = pagePerson.getContent();
			}
		};
	}
	
	public void newPerson() {
		personNew = new Person();
		personNew.setStatus(true);
		tituloDialog = "NUEVA PERSONA";
	}
	
	public void updatePerson() {
		limpiarDatosCiudades();
		tituloDialog = "MODIFICAR PERSONA";
		personNew=prospectSelected.getPerson();
		cargarCuidadPersona(prospectSelected.getPerson());
	}
	
	public void cargarCuidadPersona(Person person) {
		if(person.getDistrict()!=null) {
			countrySelected = person.getDistrict().getProvince().getDepartment().getCountry();
			listarDepartamentos();
			departmentSelected = person.getDistrict().getProvince().getDepartment();
			listarProvincias();
			provinceSelected = person.getDistrict().getProvince();
			listarDistritos();
			districtSelected = person.getDistrict();
			
		}
	}

	public void completar() {
		if(tituloDialog.equals("NUEVA PERSONA")) {
			Person buscarPorDni = personService.findByDni(personNew.getDni());
			if(buscarPorDni!=null) {
				personNew = buscarPorDni;
				personNew.setPhone("");
				personNew.setCellphone("");
				
				cargarCuidadPersona(personNew);
			}	
		}
		
	}
	
	public void savePerson() {	
		if(personNew.getSurnames().equals("") || personNew.getSurnames()==null) {
			addErrorMessage("Falta ingresar Apellidos.");
			return;
		}
		if(personNew.getNames().equals("") || personNew.getNames()==null) {
			addErrorMessage("Falta ingresar Nombres.");
			return;
		}
		
		if(personNew.getPhone().equals("") && personNew.getCellphone().equals("")) {
			addErrorMessage("Ingrese telefono o celular.");
			return;
		}
		
		if(districtSelected==null) {
			addErrorMessage("Ingrese distrito.");
			return;
		} else {
			personNew.setDistrict(districtSelected);
		}
	
		
		if(tituloDialog.equals("NUEVA PERSONA")) {
			
			
			if (!personNew.getDni().equals("")) {
				Person buscarPersona = personService.findByDni(personNew.getDni());
				if (buscarPersona != null) {
					personNew.setId(buscarPersona.getId());
					Prospect buscarProspecto = prospectService.findByPersonAndSucursal(buscarPersona, navegacionBean.getSucursalLogin());
					if (buscarProspecto != null) {
						Date fechaRest = sumaRestarFecha(buscarProspecto.getDateBlock(), 180);
						if(fechaRest.after(new Date())) {
							if (buscarProspecto.getPersonAssessor() != null) {
								Usuario buscarInactivo = usuarioService.findByPerson(buscarProspecto.getPersonAssessor());
								addErrorMessage("El prospecto está a cargo por el asesor "+ buscarProspecto.getPersonAssessor().getSurnames() + " "+ buscarProspecto.getPersonAssessor().getNames());
								return;
							} else if (buscarProspecto.getPersonSupervisor() != null) {
								addErrorMessage("El prospecto está a cargo por el supervisor "+ buscarProspecto.getPersonSupervisor().getSurnames() + " "+ buscarProspecto.getPersonSupervisor().getNames());
								return;
							} 
						}else {
							if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
								buscarProspecto.setPersonAssessor(null);
							} else if (usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
								buscarProspecto.setPersonAssessor(usuarioLogin.getPerson());
								buscarProspecto.setPersonSupervisor(usuarioLogin.getTeam().getPersonSupervisor());
							} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
								buscarProspecto.setPersonSupervisor(usuarioLogin.getPerson());
							}
							
							
							
						
							personService.save(personNew);
							buscarProspecto.setDateBlock(new Date());
							buscarProspecto.setSucursal(navegacionBean.getSucursalLogin());
 							prospectService.save(buscarProspecto);
 							
 							Prospection prospection = prospectionService.findByProspectAndStatus(buscarProspecto, "En seguimiento");
 							if(prospection!=null) {
 								prospection.setStatus("Terminado");
 	 							prospection.setResult("Rechazado");
 	 							prospectionService.save(prospection);
 	 							
 							}
 							limpiarDatosCiudades();
 							PrimeFaces.current().executeScript("PF('personNewDialog').hide();");
 							addInfoMessage("El prospecto se guardó correctamente");
							newPerson();
							return;
						}
					}
				}
			}
			
			Prospect prospectNew = new Prospect();
			
			Person person =personService.save(personNew);
			
			prospectNew.setDateBlock(new Date());
			prospectNew.setPerson(person);
			prospectNew.setSucursal(navegacionBean.getSucursalLogin());
			if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
				prospectNew.setPersonAssessor(null);
			} else if (usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
				prospectNew.setPersonAssessor(usuarioLogin.getPerson());
				prospectNew.setPersonSupervisor(usuarioLogin.getTeam().getPersonSupervisor());
			} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
				prospectNew.setPersonSupervisor(usuarioLogin.getPerson());
			}
			
			prospectService.save(prospectNew);
		    limpiarDatosCiudades();
		    PrimeFaces.current().executeScript("PF('personNewDialog').hide();");
		    addInfoMessage("El prospecto se guardó correctamente");
			newPerson();
			
			
		}else {
			if (!personNew.getDni().equals("")) {
				Person buscarPorDni = personService.findByDniException(personNew.getDni(),personNew.getId());
				if(buscarPorDni!=null) {
					addErrorMessage("El DNI ya existe.");
					return;
				}
			}
			
			personService.save(personNew);
			PrimeFaces.current().executeScript("PF('personNewDialog').hide();");
			addInfoMessage("El prospecto se guardó correctamente");
		}
		
	}
	
	public void limpiarDatosCiudades() {
		countrySelected=null;
		departmentSelected=null;
		provinceSelected=null;
		districtSelected=null;
		lstDepartment.clear();
		lstProvince.clear();
		lstDistrict.clear();
	}
	
	public Date sumaRestarFecha(Date fecha, int sumaresta){
        Calendar calendar = Calendar.getInstance();
        try{

            calendar.setTime(fecha);
            
            calendar.add(Calendar.DAY_OF_WEEK, sumaresta);
     
        }
        catch(Exception e)
        {
            System.out.println("Error:\n" + e);
        }
        return calendar.getTime();
    }
	
	
	public String getUsername() {
		if(SecurityContextHolder.getContext().getAuthentication()!=null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    username = ((UserDetails)principal).getUsername();
		}
		return username;
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
	
	public ProspectService getProspectService() {
		return prospectService;
	}
	public void setProspectService(ProspectService prospectService) {
		this.prospectService = prospectService;
	}
	public LazyDataModel<Prospect> getLstProspectLazy() {
		return lstProspectLazy;
	}
	public void setLstProspectLazy(LazyDataModel<Prospect> lstProspectLazy) {
		this.lstProspectLazy = lstProspectLazy;
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
	public Prospect getProspectSelected() {
		return prospectSelected;
	}
	public void setProspectSelected(Prospect prospectSelected) {
		this.prospectSelected = prospectSelected;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public Person getPersonNew() {
		return personNew;
	}
	public void setPersonNew(Person personNew) {
		this.personNew = personNew;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public String getTituloDialog() {
		return tituloDialog;
	}

	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}

	public ProspectionService getProspectionService() {
		return prospectionService;
	}

	public void setProspectionService(ProspectionService prospectionService) {
		this.prospectionService = prospectionService;
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

	
	
	
}
