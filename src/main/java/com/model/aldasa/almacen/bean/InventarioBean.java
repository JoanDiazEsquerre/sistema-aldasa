package com.model.aldasa.almacen.bean;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Distribucion;
import com.model.aldasa.entity.Inventario;
import com.model.aldasa.entity.InventarioBienes;
import com.model.aldasa.entity.Person;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.AreaService;
import com.model.aldasa.service.DistribucionService;
import com.model.aldasa.service.InventarioBienesService;
import com.model.aldasa.service.InventarioService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class InventarioBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{inventarioService}")
	private InventarioService inventarioService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{areaService}")
	private AreaService areaService; 
	
	@ManagedProperty(value = "#{distribucionService}")
	private DistribucionService distribucionService; 
	
	@ManagedProperty(value = "#{inventarioBienesService}")
	private InventarioBienesService inventarioBienesService;
	
	private LazyDataModel<Inventario> lstInventarioLazy;
	private LazyDataModel<InventarioBienes> lstInventarioBienesLazy;

	private List<Distribucion> lstDistribucion = new ArrayList<>();
	private List<Area> lstArea;

	
	private Boolean estado = true;
	private String tituloDialog, tituloDialogInventario;
	private String estadoInventarioBienes = "";
	
	private Inventario inventarioSelected;
	private InventarioBienes inventarioBienesSelected;

	private Distribucion distribucionSelected;
	
	@PostConstruct
	public void init() {
		iniciarLazy();
		lstArea=areaService.findByEstadoOrderByNombreAsc(true);
		iniciarLazyInventarioBienes();
	}
	
	public void editarDistribucion(Distribucion distribucion) {
		if(distribucion.getArea()==null) {
			addErrorMessage("Seleccionar área.");
			return;
		}
		if(distribucion.getCantidad()<=0) {
			addErrorMessage("Cantidad tiene que ser mayor que 0.");
			return;
		}
		
		int suma = 0;
		for(Distribucion d: lstDistribucion) {
			suma = suma + d.getCantidad();
		}
		if(suma > inventarioSelected.getCantidad()) {
			addErrorMessage("La cantidad máxima es " + inventarioSelected.getCantidad());
			verDetallesDistribucion();
			return;
		}
		
		Distribucion dist = distribucionService.save(distribucion);
		if(dist == null) {
			addErrorMessage("No se pudo modificó.");
		}else {
			addInfoMessage("Se modificó correctamente.");
			
		}
		verDetallesDistribucion();
    }
	
	public void verDetallesDistribucion() {
		distribucionSelected = new Distribucion();
		distribucionSelected.setEstado(true);
		distribucionSelected.setInventario(inventarioSelected);
		lstDistribucion = distribucionService.findByInventarioAndEstado(inventarioSelected, true);
	}
	
	public void saveDistribucion() {
		if(distribucionSelected.getArea()==null) {
			addErrorMessage("Seleccionar área.");
			return;
		}
		if(distribucionSelected.getCantidad()<=0) {
			addErrorMessage("Cantidad tiene que ser mayor que 0.");
			return;
		}
		
		int suma = 0;
		for(Distribucion d: lstDistribucion) {
			suma = suma + d.getCantidad();
		}
		suma = suma + distribucionSelected.getCantidad();
		if(suma > inventarioSelected.getCantidad()) {
			addErrorMessage("La cantidad máxima es " + inventarioSelected.getCantidad());
			return;
		}
		
		Distribucion distribucion = distribucionService.save(distribucionSelected);
		if(distribucion == null) {
			addErrorMessage("No se pudo guardar.");
		}else {
			addInfoMessage("Se guardo correctamente.");
			verDetallesDistribucion();
		}
		
		
	}

	public void newInventario() {
		tituloDialog = "NUEVO INVENTARIO";
		inventarioSelected = new Inventario();
		inventarioSelected.setEstado(true);
		inventarioSelected.setSucursal(navegacionBean.getSucursalLogin());
	}
	
	public void newInventarioBienes() {
		tituloDialogInventario = "NUEVO ITEM INVENTARIO";
		inventarioBienesSelected = new InventarioBienes();
		
	}
	
	public void updateInventario() {
		tituloDialog = "MODIFICAR INVENTARIO";
	}
	
	public void updateInventarioBienes() {
		tituloDialogInventario = "MODIFICAR ITEM INVENTARIO";
	}
	
	public void saveInventario() {
		
		if(inventarioSelected.getNombre().equals("")) {
			addErrorMessage("Ingresar nombre.");
			return;
		}
		if(inventarioSelected.getModelo().equals("")) {
			addErrorMessage("Ingresar modelo.");
			return;
		}
		if(inventarioSelected.getMarca().equals("")) {
			addErrorMessage("Ingresar marca.");
			return;
		}
		if(inventarioSelected.getCantidad() == null ) {
			addErrorMessage("Ingresar cantidad.");
			return;
		}else if(inventarioSelected.getCantidad() <= 0) {
			addErrorMessage("Cantidad debe ser mayor que 0.");
			return;
		}
		
		Inventario inv = inventarioService.save(inventarioSelected);
		if(inv == null) {
			addErrorMessage("No se pudo guardar.");
		}else {
			addInfoMessage("Se guardó correctamente.");
		}
		
		
	}

	
	
	public void iniciarLazy() {
		lstInventarioLazy = new LazyDataModel<Inventario>() {
			private List<Inventario> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Inventario getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Inventario inventario : datasource) {
					if (inventario.getId() == intRowKey) {
						return inventario;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Inventario inventario) {
				return String.valueOf(inventario.getId());
			}

			@Override
			public List<Inventario> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Sort sort = Sort.by("estado").ascending();
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
		
				
				Page<Inventario> pageInventario = inventarioService.findByEstadoAndSucursalEmpresa(estado, navegacionBean.getSucursalLogin().getEmpresa(), pageable);
						
				setRowCount((int) pageInventario.getTotalElements());
				return datasource = pageInventario.getContent();
			}
		};
	}
	
	public void iniciarLazyInventarioBienes() {
		lstInventarioBienesLazy = new LazyDataModel<InventarioBienes>() {
			private List<InventarioBienes> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public InventarioBienes getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (InventarioBienes inventarioBienes : datasource) {
					if (inventarioBienes.getId() == intRowKey) {
						return inventarioBienes;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(InventarioBienes inventarioBienes) {
				return String.valueOf(inventarioBienes.getId());
			}

			@Override
			public List<InventarioBienes> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Sort sort = Sort.by("id").descending();
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
				Page<InventarioBienes> pageInventarioBienes = null;
				
				if(!estadoInventarioBienes.equals("")) {
					pageInventarioBienes = inventarioBienesService.findByEstadoAndEmpresa(estadoInventarioBienes, navegacionBean.getSucursalLogin().getEmpresa(), pageable);
				}else {
					pageInventarioBienes = inventarioBienesService.findByEmpresa(navegacionBean.getSucursalLogin().getEmpresa(), pageable);
				}
				
						
				setRowCount((int) pageInventarioBienes.getTotalElements());
				return datasource = pageInventarioBienes.getContent();
			}
		};
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
	
	
	
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public LazyDataModel<Inventario> getLstInventarioLazy() {
		return lstInventarioLazy;
	}
	public void setLstInventarioLazy(LazyDataModel<Inventario> lstInventarioLazy) {
		this.lstInventarioLazy = lstInventarioLazy;
	}
	public InventarioService getInventarioService() {
		return inventarioService;
	}
	public void setInventarioService(InventarioService inventarioService) {
		this.inventarioService = inventarioService;
	}
	public Inventario getInventarioSelected() {
		return inventarioSelected;
	}
	public void setInventarioSelected(Inventario inventarioSelected) {
		this.inventarioSelected = inventarioSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public List<Distribucion> getLstDistribucion() {
		return lstDistribucion;
	}
	public void setLstDistribucion(List<Distribucion> lstDistribucion) {
		this.lstDistribucion = lstDistribucion;
	}
	public Distribucion getDistribucionSelected() {
		return distribucionSelected;
	}
	public void setDistribucionSelected(Distribucion distribucionSelected) {
		this.distribucionSelected = distribucionSelected;
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
	public DistribucionService getDistribucionService() {
		return distribucionService;
	}
	public void setDistribucionService(DistribucionService distribucionService) {
		this.distribucionService = distribucionService;
	}
	public LazyDataModel<InventarioBienes> getLstInventarioBienesLazy() {
		return lstInventarioBienesLazy;
	}
	public void setLstInventarioBienesLazy(LazyDataModel<InventarioBienes> lstInventarioBienesLazy) {
		this.lstInventarioBienesLazy = lstInventarioBienesLazy;
	}
	public InventarioBienesService getInventarioBienesService() {
		return inventarioBienesService;
	}
	public void setInventarioBienesService(InventarioBienesService inventarioBienesService) {
		this.inventarioBienesService = inventarioBienesService;
	}
	public String getEstadoInventarioBienes() {
		return estadoInventarioBienes;
	}
	public void setEstadoInventarioBienes(String estadoInventarioBienes) {
		this.estadoInventarioBienes = estadoInventarioBienes;
	}
	public InventarioBienes getInventarioBienesSelected() {
		return inventarioBienesSelected;
	}
	public void setInventarioBienesSelected(InventarioBienes inventarioBienesSelected) {
		this.inventarioBienesSelected = inventarioBienesSelected;
	}
	public String getTituloDialogInventario() {
		return tituloDialogInventario;
	}
	public void setTituloDialogInventario(String tituloDialogInventario) {
		this.tituloDialogInventario = tituloDialogInventario;
	}
	
	
}
