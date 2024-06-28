package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.model.aldasa.entity.ModuloSistema;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.service.ModuloSistemaService;
import com.model.aldasa.service.ProfileService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class ProfileBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{profileService}")
	private ProfileService profileService;
	
	@ManagedProperty(value = "#{moduloSistemaService}")
	private ModuloSistemaService moduloSistemaService;
	
	private LazyDataModel<Profile> lstProfileLazy;
	
	private List<ModuloSistema> lstModulos;
	private List<ModuloSistema> lstModulosSelected;
	
	private Profile profileSelected;
	private ModuloSistema moduloSelected;
	private boolean estado = true;
	
	private String tituloDialog;
	private String nuevoPermiso="";
	
	@PostConstruct
	public void init() {
		iniciarLazy();
		lstModulos = moduloSistemaService.findByEstadoOrderByNombreAsc(true);
	}
	
	public void agregarPermiso() {
		if(moduloSelected == null) {
			addErrorMessage("Debe seleccionar un modulo.");
			return;
		}
		
		if(!lstModulosSelected.isEmpty()) {
			for(ModuloSistema ms: lstModulosSelected) {
				if(ms.getId().equals(moduloSelected.getId())) {
					addErrorMessage("Ya tiene asignado el modulo: "+moduloSelected.getNombre()); 
					return;
				}
			}	
		}
		
		lstModulosSelected.add(moduloSelected);	
		nuevoPermiso="";
		lstModulosSelected.stream().sorted(Comparator.comparing(ModuloSistema::getNombre)).collect(Collectors.toList()).forEach(obj->{
			nuevoPermiso = nuevoPermiso+obj.getId()+",";
		});
		
		nuevoPermiso = nuevoPermiso.substring(0, nuevoPermiso.length() - 1);
		profileSelected.setPermiso(nuevoPermiso);
		profileService.save(profileSelected);
		addInfoMessage("Se agregó el permiso correctamente.");
		
	}
	
	public void eliminarPermiso(ModuloSistema ms) {
		String nuevoPermiso = "";
		
		if(!profileSelected.getPermiso().equals("")) {
			String[] idPermisos = profileSelected.getPermiso().split(",");
			for(String s : idPermisos) {
				if(!ms.getId().toString().equals(s)) { 
					nuevoPermiso = nuevoPermiso+s+",";
				}
			}
			
			nuevoPermiso = nuevoPermiso.substring(0, nuevoPermiso.length() - 1);
			profileSelected.setPermiso(nuevoPermiso);
			profileService.save(profileSelected);
			
			verPermisos();
			addInfoMessage("Se eliminó el permiso correctamente.");
		}
	}

	public void verPermisos() {
		moduloSelected=null;
		lstModulosSelected = new ArrayList<>();
		if(!profileSelected.getPermiso().equals("")) {
			String[] idPermisos = profileSelected.getPermiso().split(",");
			for(String s : idPermisos) {
				Optional<ModuloSistema> m = moduloSistemaService.findById(Integer.parseInt(s));
				lstModulosSelected.add(m.get());
			}
		}
	}
	
	public void newProfile() {
		tituloDialog="NUEVO PERFIL";
		profileSelected=new Profile();
		profileSelected.setStatus(true);
		profileSelected.setName("");
		
	}
	
	public void modifyProfile( ) {
		tituloDialog="MODIFICAR PERFIL";
		
	}
	
	
	public void saveProfile() {
		if(profileSelected.getName().equals("") || profileSelected.getName()==null) {
			addErrorMessage("Ingresar Nombre del perfil.");
			
			return ;
		} 
		if (tituloDialog.equals("NUEVO PERFIL")) {
			Profile validarExistencia = profileService.findByName(profileSelected.getName());
			if (validarExistencia == null) {
				profileSelected.setPermiso("");
				profileService.save(profileSelected);
				PrimeFaces.current().executeScript("PF('profileDialog').hide();");
				addInfoMessage("Se guardo correctamente.");
				newProfile();
			} else { 
				addErrorMessage("El perfil ya existe.");
			}
		} else {
			Profile validarExistencia = profileService.findByNameException(profileSelected.getName(), profileSelected.getId());
			if (validarExistencia == null) {
				profileService.save(profileSelected);
				PrimeFaces.current().executeScript("PF('profileDialog').hide();");
				addInfoMessage("Se guardo correctamente.");
			} else { 
				addErrorMessage("El perfil ya existe.");
			}
		}
		
	}
	
	public void iniciarLazy() {

		lstProfileLazy = new LazyDataModel<Profile>() {
			private List<Profile> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Profile getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Profile profile : datasource) {
                    if (profile.getId() == intRowKey) {
                        return profile;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Profile profile) {
                return String.valueOf(profile.getId());
            }

			@Override
			public List<Profile> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String names = "%" + (filterBy.get("name") != null ? filterBy.get("name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("name").ascending();
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
               
                Page<Profile> pageProfile=null;
               
                
                pageProfile= profileService.findByNameLikeAndStatus(names, estado, pageable);
                
                setRowCount((int) pageProfile.getTotalElements());
                return datasource = pageProfile.getContent();
            }
		};
	}

	public Converter getConversorModuloSistema() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	ModuloSistema c = null;
                    for (ModuloSistema si : lstModulos) {
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
                    return ((ModuloSistema) value).getId() + "";
                }
            }
        };
    }
	
	public ProfileService getProfileService() {
		return profileService;
	}
	
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}
	public Profile getProfileSelected() {
		return profileSelected;
	}
	public void setProfileSelected(Profile profileSelected) {
		this.profileSelected = profileSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public LazyDataModel<Profile> getLstProfileLazy() {
		return lstProfileLazy;
	}
	public void setLstProfileLazy(LazyDataModel<Profile> lstProfileLazy) {
		this.lstProfileLazy = lstProfileLazy;
	}
	public List<ModuloSistema> getLstModulos() {
		return lstModulos;
	}
	public void setLstModulos(List<ModuloSistema> lstModulos) {
		this.lstModulos = lstModulos;
	}
	public ModuloSistema getModuloSelected() {
		return moduloSelected;
	}
	public void setModuloSelected(ModuloSistema moduloSelected) {
		this.moduloSelected = moduloSelected;
	}
	public ModuloSistemaService getModuloSistemaService() {
		return moduloSistemaService;
	}
	public void setModuloSistemaService(ModuloSistemaService moduloSistemaService) {
		this.moduloSistemaService = moduloSistemaService;
	}
	public List<ModuloSistema> getLstModulosSelected() {
		return lstModulosSelected;
	}
	public void setLstModulosSelected(List<ModuloSistema> lstModulosSelected) {
		this.lstModulosSelected = lstModulosSelected;
	}
	public String getNuevoPermiso() {
		return nuevoPermiso;
	}
	public void setNuevoPermiso(String nuevoPermiso) {
		this.nuevoPermiso = nuevoPermiso;
	}
}
