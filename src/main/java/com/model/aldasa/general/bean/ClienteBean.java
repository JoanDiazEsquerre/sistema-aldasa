package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class ClienteBean extends BaseBean implements Serializable{
	
	@ManagedProperty(value = "#{clienteService}")
	private ClienteService clienteService; 
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService; 
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean; 
	
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Cliente> lstClienteLazy;
	
	private List<Person> lstPerson;
	
	private boolean estado = true;
	private boolean personaNaturalCliente = true;
	private boolean bloqueo;
	private String tituloDialog;
	private String razonSocialCliente;
	private String nombreComercialCliente;
	private String rucDniCliente;
	private String direccionCliente;
	private String email1Cliente;
	private String email2Cliente;
	private String email3Cliente;

	
	private Cliente clienteSelected;
	private Person personSelected;

	@PostConstruct
	public void init() {
		lstPerson=personService.findByStatus(true);
		iniciarLazy();
		
	}
	
	public void modifyCliente( ) {
		tituloDialog="MODIFICAR CLIENTE";
		bloqueo=true;
		personSelected = clienteSelected.getPerson();
		razonSocialCliente = clienteSelected.getRazonSocial();
		nombreComercialCliente = clienteSelected.getNombreComercial();
		personaNaturalCliente = clienteSelected.isPersonaNatural();
		if(personaNaturalCliente) {
			rucDniCliente=clienteSelected.getDni();
		}else {
			rucDniCliente=clienteSelected.getRuc();
		}
		direccionCliente = clienteSelected.getDireccion();
		email1Cliente = clienteSelected.getEmail1Fe();
		email2Cliente = clienteSelected.getEmail2Fe();
		email3Cliente = clienteSelected.getEmail3Fe();
				
	}
	
	public void newCliente() {
		tituloDialog="NUEVO CLIENTE";
		iniciarDatosCliente();
		bloqueo=false;

	}
	
	
	public void onChangePerson() {
        if(personSelected!=null) {
            razonSocialCliente=personSelected.getSurnames()+" "+ personSelected.getNames();
            nombreComercialCliente=personSelected.getSurnames()+" "+ personSelected.getNames();
            rucDniCliente=personSelected.getDni();
            direccionCliente=personSelected.getAddress();
            email1Cliente=personSelected.getEmail();;
            
        }else {
            iniciarDatosCliente();
        }
    }
    
    public void iniciarDatosCliente() {
        personaNaturalCliente = true;
        personSelected=null;
        razonSocialCliente = "";
        nombreComercialCliente = "";
        rucDniCliente = "";
        direccionCliente = "";
        email1Cliente = "";
        email2Cliente = "";
        email3Cliente = "";
    }
    
    public void saveCliente() {
        if(personSelected == null) {
            addErrorMessage("Debes seleccionar una persona o representante.");
            return;
        }
        if(razonSocialCliente.equals("")) {
            addErrorMessage("Debes ingresar la Razon Social.");
            return;
        }
        if(nombreComercialCliente.equals("")) {
            addErrorMessage("Debes ingresar un nombre Comercial.");
            return;
        }
        if(rucDniCliente.equals("")) {
            addErrorMessage("Debes ingresar RUC o DNI.");
            return;
        }else {
            if(!personaNaturalCliente) {
                if(!validarRuc(rucDniCliente)) {
                    return;
                }
            }
            
        }
        if(direccionCliente.equals("")) {
            addErrorMessage("Debes ingresar una direccion.");
            return;
        }
        Cliente busqueda = null;
        if(tituloDialog.equals("NUEVO CLIENTE")) {
        	if(personaNaturalCliente) {
        		busqueda = clienteService.findByPersonAndEstadoAndPersonaNatural(personSelected, true, true);
        		if(busqueda!=null) {
                    addErrorMessage("Ya existe un cliente con la persona seleccionada.");
                    return;
                }
        	}else {
                busqueda = clienteService.findByRucAndEstado(rucDniCliente, true);
                if(busqueda!=null) {
                    addErrorMessage("Ya existe un cliente con RUC "+ rucDniCliente);
                    return;
                }
            }
        
        }else {
        	
        	if(personaNaturalCliente) {
        		busqueda = clienteService.findByPersonIdAndEstadoAndPersonaNaturalExcepcion(personSelected.getId(), true, true, clienteSelected.getId());
        		if(busqueda!=null) {
                    addErrorMessage("Ya existe un cliente con la persona seleccionada.");
                    return;
                }
        	}else {
                busqueda = clienteService.findByRucAndEstadoExcepcion(rucDniCliente, true, clienteSelected.getId());
                if(busqueda!=null) {
                    addErrorMessage("Ya existe un cliente con RUC "+ rucDniCliente);
                    return;
                }
            }
        }
      
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setId(clienteSelected!=null ? clienteSelected.getId() : null);
        nuevoCliente.setPerson(personSelected);
        nuevoCliente.setRazonSocial(razonSocialCliente);
        nuevoCliente.setNombreComercial(nombreComercialCliente);
        if(personaNaturalCliente) {
            nuevoCliente.setDni(rucDniCliente);
            nuevoCliente.setRuc("");
        }else {
        	nuevoCliente.setDni("");
            nuevoCliente.setRuc(rucDniCliente);
        }
        nuevoCliente.setDireccion(direccionCliente);
        nuevoCliente.setPersonaNatural(personaNaturalCliente);
        nuevoCliente.setEstado(true);
        nuevoCliente.setFechaRegistro(new Date());
        nuevoCliente.setIdUsuarioRegistro(navegacionBean.getUsuarioLogin());
        nuevoCliente.setEmail1Fe(email1Cliente);
        nuevoCliente.setEmail2Fe(email2Cliente);
        nuevoCliente.setEmail3Fe(email3Cliente);
        clienteService.save(nuevoCliente);
        
        
        addInfoMessage("Se registro correctamente el cliente.");
        PrimeFaces.current().executeScript("PF('clienteDialog').hide();");
    }
    
    public boolean validarRuc(String ruc) {
        if(ruc.length()!=11) {
            addErrorMessage("El RUC debe tener 11 dígitos.");
            return false;
        }
        
        boolean valor = false;
        
        
        String primerosNumeros =ruc.substring(0,2);
        
        if(primerosNumeros.equals("10"))valor = true;
        
        if(primerosNumeros.equals("15"))valor = true;
        
        if(primerosNumeros.equals("17"))valor = true;
        
        if(primerosNumeros.equals("20"))valor = true;
        
        if(!valor) addErrorMessage("Ruc incorrecto, debe iniciar con 10, 15, 17 o 20");
        
        
        
        return valor;
    }
	
	public void iniciarLazy() {

		lstClienteLazy = new LazyDataModel<Cliente>() {
			private List<Cliente> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Cliente getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Cliente cliente : datasource) {
                    if (cliente.getId() == intRowKey) {
                        return cliente;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Cliente cliente) {
                return String.valueOf(cliente.getId());
            }

			@Override
			public List<Cliente> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String razonSocial = "%" + (filterBy.get("razonSocial") != null ? filterBy.get("razonSocial").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String nombreComercial = "%" + (filterBy.get("nombreComercial") != null ? filterBy.get("nombreComercial").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String ruc = "%" + (filterBy.get("ruc") != null ? filterBy.get("ruc").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String dni = "%" + (filterBy.get("dni") != null ? filterBy.get("dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";


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
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
               
                Page<Cliente> pageCliente=null;
               
                
                pageCliente= clienteService.findByRazonSocialLikeAndNombreComercialLikeAndRucLikeAndDniLikeAndEstado(razonSocial, nombreComercial, ruc, dni, estado, pageable);
                
                setRowCount((int) pageCliente.getTotalElements());
                return datasource = pageCliente.getContent();
            }
		};
	}
	
	public Converter getConversorPerson() {
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
	
	public List<Person> completePerson(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPerson) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	

	public LazyDataModel<Cliente> getLstClienteLazy() {
		return lstClienteLazy;
	}
	public void setLstClienteLazy(LazyDataModel<Cliente> lstClienteLazy) {
		this.lstClienteLazy = lstClienteLazy;
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
	public Cliente getClienteSelected() {
		return clienteSelected;
	}
	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public boolean isPersonaNaturalCliente() {
		return personaNaturalCliente;
	}
	public void setPersonaNaturalCliente(boolean personaNaturalCliente) {
		this.personaNaturalCliente = personaNaturalCliente;
	}
	public String getRazonSocialCliente() {
		return razonSocialCliente;
	}
	public void setRazonSocialCliente(String razonSocialCliente) {
		this.razonSocialCliente = razonSocialCliente;
	}
	public String getNombreComercialCliente() {
		return nombreComercialCliente;
	}
	public void setNombreComercialCliente(String nombreComercialCliente) {
		this.nombreComercialCliente = nombreComercialCliente;
	}
	public String getRucDniCliente() {
		return rucDniCliente;
	}
	public void setRucDniCliente(String rucDniCliente) {
		this.rucDniCliente = rucDniCliente;
	}
	public Person getPersonSelected() {
		return personSelected;
	}
	public void setPersonSelected(Person personSelected) {
		this.personSelected = personSelected;
	}
	public String getDireccionCliente() {
		return direccionCliente;
	}
	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}
	public String getEmail1Cliente() {
		return email1Cliente;
	}
	public void setEmail1Cliente(String email1Cliente) {
		this.email1Cliente = email1Cliente;
	}
	public String getEmail2Cliente() {
		return email2Cliente;
	}
	public void setEmail2Cliente(String email2Cliente) {
		this.email2Cliente = email2Cliente;
	}
	public String getEmail3Cliente() {
		return email3Cliente;
	}
	public void setEmail3Cliente(String email3Cliente) {
		this.email3Cliente = email3Cliente;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public boolean isBloqueo() {
		return bloqueo;
	}
	public void setBloqueo(boolean bloqueo) {
		this.bloqueo = bloqueo;
	}
	
	
	

}
