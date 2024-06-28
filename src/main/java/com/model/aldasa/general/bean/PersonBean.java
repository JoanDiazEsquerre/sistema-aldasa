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

import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.District;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Province;
import com.model.aldasa.service.CountryService;
import com.model.aldasa.service.DepartmentService;
import com.model.aldasa.service.DistrictService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProvinceService;
import com.model.aldasa.util.BaseBean;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@ManagedBean
@ViewScoped
public class PersonBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{personService}")
	private PersonService personService;

	@ManagedProperty(value = "#{countryService}")
	private CountryService countryService;

	@ManagedProperty(value = "#{departmentService}")
	private DepartmentService departmentService;

	@ManagedProperty(value = "#{provinceService}")
	private ProvinceService provinceService;

	@ManagedProperty(value = "#{districtService}")
	private DistrictService districtService;

	private LazyDataModel<Person> lstPersonsLazy;

	private Person personSelected, personNew;
	private Country countrySelected;
	private Department departmentSelected;
	private Province provinceSelected;
	private District districtSelected;

	private Country countrySelectedCyg;
	private Department departmentSelectedCyg;
	private Province provinceSelectedCyg;
	private District districtSelectedCyg;

	private List<Country> lstCountry;
	private List<Department> lstDepartment;
	private List<Province> lstProvince;
	private List<District> lstDistrict;

	private List<Country> lstCountryCyg;
	private List<Department> lstDepartmentCyg;
	private List<Province> lstProvinceCyg;
	private List<District> lstDistrictCyg;

	private Boolean estado = true;
	private String tituloDialog;

	private Person personConyuge = new Person();

	@PostConstruct
	public void init() {
		listarCliente();
		listarPais();
	}
	
	public void buscar() {
		
		if(!personNew.getDni().equals("")) {
			HttpClient httpClient = HttpClientBuilder.create().build();
	        String apiUrl = "https://api.apis.net.pe/v1/dni?numero="+personNew.getDni().trim();

	        HttpGet request = new HttpGet(apiUrl);

	        try {
	            HttpResponse response = httpClient.execute(request);

	            int statusCode = response.getStatusLine().getStatusCode();
	            System.out.println("Status Code: " + statusCode);

	            if (statusCode == 200) {
	                String responseBody = EntityUtils.toString(response.getEntity());
	                System.out.println("Response: " + responseBody);
	                // Aquí puedes procesar la respuesta JSON según lo necesites
	                
	             // Parsear el JSON
	                JSONObject json = new JSONObject(responseBody);

	                // Acceder a los campos del JSON
//	                String nombre = json.getString("nombre");
//	                String apellidoPaterno = json.getString("apellidoPaterno");
//	                String apellidoMaterno = json.getString("apellidoMaterno");
	                if(!"".equals(json.getString("apellidoPaterno"))) {
	                	personNew.setSurnames(json.getString("apellidoPaterno")+" "+ json.getString("apellidoMaterno"));
	                }
	                if(!"".equals(json.getString("nombres"))) {
	                	personNew.setNames(json.getString("nombres"));
	                }
	                if(!"".equals(json.getString("direccion"))) {
	                	personNew.setAddress(json.getString("direccion"));
	                }
	                
	                
	                
	                
	                
	                
	            } else {
	                System.out.println("Error al obtener la respuesta. Código de estado: " + statusCode);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
	}

	public void listarPais() {
		lstCountry = countryService.findAll();
		lstCountryCyg = countryService.findAll();
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

	public void listarDepartamentosCyg() {
		lstDepartmentCyg = new ArrayList<>();
		lstProvinceCyg = new ArrayList<>();
		lstDistrictCyg = new ArrayList<>();

		if (countrySelectedCyg != null) {
			lstDepartmentCyg = departmentService.findByCountry(countrySelectedCyg);
		}
	}

	public void listarProvinciasCyg() {
		lstProvinceCyg = new ArrayList<>();
		lstDistrictCyg = new ArrayList<>();

		if (departmentSelectedCyg != null) {
			lstProvinceCyg = provinceService.findByDepartment(departmentSelectedCyg);
		}
	}

	public void listarDistritosCyg() {
		lstDistrictCyg = new ArrayList<>();

		if (provinceSelectedCyg != null) {
			lstDistrictCyg = districtService.findByProvince(provinceSelectedCyg);
		}
	}

	public void limpiarDatosCuidades() {
		countrySelected = null;
		departmentSelected = null;
		provinceSelected = null;
		lstDepartment = new ArrayList<>();
		lstProvince = new ArrayList<>();
		lstDistrict = new ArrayList<>();

		countrySelectedCyg = null;
		departmentSelectedCyg = null;
		provinceSelectedCyg = null;
		lstDepartmentCyg = new ArrayList<>();
		lstProvinceCyg = new ArrayList<>();
		lstDistrictCyg = new ArrayList<>();
	}

	public void listarCliente() {
//		listarPersonas();
		iniciarLazy();
	}

	public void iniciarLazy() {
		lstPersonsLazy = new LazyDataModel<Person>() {
			private List<Person> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Person getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Person person : datasource) {
					if (person.getId() == intRowKey) {
						return person;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Person person) {
				return String.valueOf(person.getId());
			}

			@Override
			public List<Person> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				// Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al
				// final y reemplazo los espacios por %, para hacer el LIKE
				// Si debageas aqui te vas a dar cuenta como lo captura
				String dni = "%" + (filterBy.get("dni") != null ? filterBy.get("dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String names = "%" + (filterBy.get("surnames") != null ? filterBy.get("surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

				Sort sort = Sort.by("surnames").ascending();
				if (sortBy != null) {
					for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
						System.out.println(entry.getKey() + "/" + entry.getValue());
						if (entry.getValue().getOrder().isAscending()) {
							sort = Sort.by(entry.getKey()).descending();
						} else {
							sort = Sort.by(entry.getKey()).ascending();

						}

					}
				}

				Pageable pageable = PageRequest.of(first / pageSize, pageSize, sort);
				// Aqui llamo al servicio que a su vez llama al repositorio que contiene la
				// sentencia LIKE,
				// Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre
				// a modo de ejemplo
				// Tu deberias preparar el metodo para cada filtro que tengas en la tabla
				Page<Person> pagePerson = personService.findAllByDniLikeAndSurnamesLikeAndStatus(dni, names, estado,
						pageable);
				setRowCount((int) pagePerson.getTotalElements());
				return datasource = pagePerson.getContent();
			}
		};
	}

//	public void listarPersonas() {
//		lstPersons = personService.findByStatus(estado);
//	}

	public void newPerson() {
		tituloDialog = "NUEVA PERSONA";

		personNew = new Person();
		personNew.setStatus(true);

		limpiarDatosCuidades();
		personConyuge = new Person();
		personConyuge.setStatus(true);

	}

	public void updatePerson() {
		tituloDialog = "MODIFICAR PERSONA";
		limpiarDatosCuidades();

		personNew = personSelected;

		if (personSelected.getDistrict() != null) {
			countrySelected = personSelected.getDistrict().getProvince().getDepartment().getCountry();
			listarDepartamentos();
			departmentSelected = personSelected.getDistrict().getProvince().getDepartment();
			listarProvincias();
			provinceSelected = personSelected.getDistrict().getProvince();
			listarDistritos();
			districtSelected = personSelected.getDistrict();
		}

		if (personSelected.getPersonconyuge() != null) {
			personConyuge = personSelected.getPersonconyuge();
			if (personConyuge.getDistrict() != null) {
				countrySelectedCyg = personSelected.getPersonconyuge().getDistrict().getProvince().getDepartment()
						.getCountry();
				listarDepartamentosCyg();
				departmentSelectedCyg = personSelected.getPersonconyuge().getDistrict().getProvince().getDepartment();
				listarProvinciasCyg();
				provinceSelectedCyg = personSelected.getPersonconyuge().getDistrict().getProvince();
				listarDistritosCyg();
				districtSelectedCyg = personSelected.getPersonconyuge().getDistrict();
			}
		} else {
			personConyuge = new Person();
			personConyuge.setStatus(true);
		}

	}

	public void completar() {
		if(!personConyuge.getDni().equals("") && !personNew.getDni().equals("")) {
			if(personConyuge.getDni().equals(personNew.getDni())) {
				addErrorMessage("El DNI no puede ser igual al de la persona.");
				
				return ;
			}
		}
		Person buscarPorDni = personService.findByDni(personConyuge.getDni());
		if (buscarPorDni != null) {
			personConyuge = buscarPorDni;

			cargarCuidadPersonaCyg(personConyuge);
		}
	}

	public void cargarCuidadPersonaCyg(Person person) {
		if (person.getDistrict() != null) {
			countrySelectedCyg = person.getDistrict().getProvince().getDepartment().getCountry();
			listarDepartamentosCyg();
			departmentSelectedCyg = person.getDistrict().getProvince().getDepartment();
			listarProvinciasCyg();
			provinceSelectedCyg = person.getDistrict().getProvince();
			listarDistritosCyg();
			districtSelectedCyg = person.getDistrict();

		}
	}

	public boolean validarDatosPersona(Person person) {
		boolean valor = true;

		if (person.getDni().equals("") || person.getDni() == null) {
			addErrorMessage("Ingresar DNI.");

		} else {
			if (tituloDialog.equals("NUEVA PERSONA")) {
				Person buscarPorDni = personService.findByDni(person.getDni());
				if (buscarPorDni != null) {
					addErrorMessage("El DNI ya existe.");
					
					return false;
				}
			} else {
				Person buscarPorDni = personService.findByDniException(person.getDni(), person.getId());
				if (buscarPorDni != null) {
					addErrorMessage("El DNI ya existe.");
					
					return false;
				}
			}

		}
		if (person.getSurnames().equals("") || person.getSurnames() == null) {
			addErrorMessage("Ingresar Apellidos.");
			
			return false;
		}
		if (person.getNames().equals("") || person.getNames() == null) {
			addErrorMessage("Ingresar Nombres.");
			
			return false;
		}
		if (person.getAddress().equals("") || person.getAddress() == null) {
			addErrorMessage("Ingresar dirección.");
			
			return false;
		}
		if (person.getPhone().equals("") || person.getPhone() == null) {
			addErrorMessage("Ingresar Teléfono.");
			
			return false;
		}

		if (districtSelected == null) {
			addErrorMessage("Seleccionar distrito de la persona.");
			
			return false;
		} else {
			personNew.setDistrict(districtSelected);
			;
		}

		return valor;

	}

	public boolean validarDatosPersonaCyg(Person person) {
		boolean valor = true;

		if (person.getDni().equals("") || person.getDni() == null) {
			personConyuge.setDni("");

		} else {
			if (tituloDialog.equals("NUEVA PERSONA")) {
				Person buscarPorDni = personService.findByDni(person.getDni());
				if (buscarPorDni != null) {
					addErrorMessage("El DNI ya existe.");
					
					return false;
				}
			} else {
				if (person.getId() != null) {
					Person buscarPorDni = personService.findByDniException(person.getDni(), person.getId());
					if (buscarPorDni != null) {
						addErrorMessage("El DNI del conyuge ya existe.");
						
						return false;
					}
				} else {
					Person buscarPorDni = personService.findByDni(person.getDni());
					if (buscarPorDni != null) {
						addErrorMessage("El DNI del conyuge ya existe.");
						
						return false;
					}
				}
			}

		}
		if (person.getSurnames().equals("") || person.getSurnames() == null) {
			addErrorMessage("Ingresar Apellidos del conyuge.");
			
			return false;
		}
		if (person.getNames().equals("") || person.getNames() == null) {
			addErrorMessage("Ingresar Nombres del conyuge.");
			
			return false;
		}
		if (person.getAddress().equals("") || person.getAddress() == null) {
			addErrorMessage("Ingresar dirección del conyuge.");
			
			return false;
		}
		if (person.getPhone().equals("") || person.getPhone() == null) {
			addErrorMessage("Ingresar Teléfono del conyuge.");
		
			return false;
		}

		if (districtSelectedCyg == null) {
			addErrorMessage("Seleccionar distrito del conyuge.");
			
			return false;
		} else {
			personConyuge.setDistrict(districtSelectedCyg);
		}

		return valor;

	}

	public void savePerson() {
		boolean valida = validarDatosPersona(personNew);

		if (!personConyuge.getDni().equals("")) {

			boolean validaCyg = validarDatosPersonaCyg(personConyuge);
			if (!validaCyg) {
				return;
			}
		}

		if (valida) {
			Person per = personService.save(personNew);
			if (per == null) {
				addErrorMessage("No se puede guardar.");
				
			} else {

				if (!personConyuge.getDni().equals("")) {
					Person perCyg = personService.save(personConyuge);
					per.setPersonconyuge(perCyg);
					personService.save(per);

					perCyg.setPersonconyuge(per);
					personService.save(perCyg);

				}
				PrimeFaces.current().executeScript("PF('personNewDialog').hide();");
				addInfoMessage("Se guardó correctamente.");
				
				
				if (tituloDialog.equals("NUEVA PERSONA")) {
					newPerson();
					countrySelected = null;
					lstDepartment.clear();
					lstProvince.clear();
					lstDistrict.clear();
				}
			}
		}
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

	public Converter getConversorCountryCyg() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Country c = null;
					for (Country si : lstCountryCyg) {
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

	public Converter getConversorDepartmentCyg() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Department c = null;
					for (Department si : lstDepartmentCyg) {
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

	public Converter getConversorProvinceCyg() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Province c = null;
					for (Province si : lstProvinceCyg) {
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

	public Converter getConversorDistrictCyg() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					District c = null;
					for (District si : lstDistrictCyg) {
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

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

//	public List<Person> getLstPersons() {
//		return lstPersons;
//	}
//	public void setLstPersons(List<Person> lstPersons) {
//		this.lstPersons = lstPersons;
//	}
	public Person getPersonSelected() {
		return personSelected;
	}

	public void setPersonSelected(Person personSelected) {
		this.personSelected = personSelected;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getTituloDialog() {
		return tituloDialog;
	}

	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}

	public LazyDataModel<Person> getLstPersonsLazy() {
		return lstPersonsLazy;
	}

	public void setLstPersonsLazy(LazyDataModel<Person> lstPersonsLazy) {
		this.lstPersonsLazy = lstPersonsLazy;
	}

	public Person getPersonNew() {
		return personNew;
	}

	public void setPersonNew(Person personNew) {
		this.personNew = personNew;
	}

	/**/
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public Country getCountrySelected() {
		return countrySelected;
	}

	public void setCountrySelected(Country countrySelected) {
		this.countrySelected = countrySelected;
	}

	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public List<Country> getLstCountry() {
		return lstCountry;
	}

	public void setLstCountry(List<Country> lstCountry) {
		this.lstCountry = lstCountry;
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
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

	public Department getDepartmentSelected() {
		return departmentSelected;
	}

	public void setDepartmentSelected(Department departmentSelected) {
		this.departmentSelected = departmentSelected;
	}

	public ProvinceService getProvinceService() {
		return provinceService;
	}

	public void setProvinceService(ProvinceService provinceService) {
		this.provinceService = provinceService;
	}

	public Province getProvinceSelected() {
		return provinceSelected;
	}

	public void setProvinceSelected(Province provinceSelected) {
		this.provinceSelected = provinceSelected;
	}

	public DistrictService getDistrictService() {
		return districtService;
	}

	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	public Person getPersonConyuge() {
		return personConyuge;
	}

	public void setPersonConyuge(Person personConyuge) {
		this.personConyuge = personConyuge;
	}

	public District getDistrictSelected() {
		return districtSelected;
	}

	public void setDistrictSelected(District districtSelected) {
		this.districtSelected = districtSelected;
	}

	public Country getCountrySelectedCyg() {
		return countrySelectedCyg;
	}

	public void setCountrySelectedCyg(Country countrySelectedCyg) {
		this.countrySelectedCyg = countrySelectedCyg;
	}

	public Department getDepartmentSelectedCyg() {
		return departmentSelectedCyg;
	}

	public void setDepartmentSelectedCyg(Department departmentSelectedCyg) {
		this.departmentSelectedCyg = departmentSelectedCyg;
	}

	public Province getProvinceSelectedCyg() {
		return provinceSelectedCyg;
	}

	public void setProvinceSelectedCyg(Province provinceSelectedCyg) {
		this.provinceSelectedCyg = provinceSelectedCyg;
	}

	public District getDistrictSelectedCyg() {
		return districtSelectedCyg;
	}

	public void setDistrictSelectedCyg(District districtSelectedCyg) {
		this.districtSelectedCyg = districtSelectedCyg;
	}

	public List<Country> getLstCountryCyg() {
		return lstCountryCyg;
	}

	public void setLstCountryCyg(List<Country> lstCountryCyg) {
		this.lstCountryCyg = lstCountryCyg;
	}

	public List<Department> getLstDepartmentCyg() {
		return lstDepartmentCyg;
	}

	public void setLstDepartmentCyg(List<Department> lstDepartmentCyg) {
		this.lstDepartmentCyg = lstDepartmentCyg;
	}

	public List<Province> getLstProvinceCyg() {
		return lstProvinceCyg;
	}

	public void setLstProvinceCyg(List<Province> lstProvinceCyg) {
		this.lstProvinceCyg = lstProvinceCyg;
	}

	public List<District> getLstDistrictCyg() {
		return lstDistrictCyg;
	}

	public void setLstDistrictCyg(List<District> lstDistrictCyg) {
		this.lstDistrictCyg = lstDistrictCyg;
	}

}
