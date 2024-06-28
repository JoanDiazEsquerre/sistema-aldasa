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
	
	private LazyDataModel<Lote> lstLoteLazy;
	private LazyDataModel<Cuota> lstCuotaLazy;

	
	private Manzana manzanaFilter;
	
	private boolean busqueda = true;
	private boolean busquedaCuota = true;


	
	@PostConstruct
	public void init() {
		iniciarLazy();
		iniciarLazyCuota();
		mostrarDialog();
		texto1 = "Cada uno según el don que ha recibido,";
		texto2 = "adminístrelo a los otros,";
		texto3 = "como buenos dispensadores de";
		texto4 = "las diferentes gracias de Dios. (Pedro 4:10)";
		
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "refreshPage?faces-redirect=true");
		
	}
	
	public void mostrarDialog() {
		
		Usuario usuarioLogin = navegacionBean.getUsuarioLogin();
		
		if(usuarioLogin.getProfile().getId()== Perfiles.ASISTENTE_ADMINISTRATIVO.getId()) {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('inicioDialog').show();");
		}
		if(usuarioLogin.getProfile().getId()== Perfiles.COBRANZA.getId() || usuarioLogin.getProfile().getId()== Perfiles.ASISTENTE_COBRANZA.getId()) {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('inicioDialogCobranza').show();");
		}
		
		
	}
	
	public void iniciarLazy() {
		lstLoteLazy = new LazyDataModel<Lote>() {
			private List<Lote> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Lote getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Lote lote : datasource) {
                    if (lote.getId() == intRowKey) {
                        return lote;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Lote lote) {
                return String.valueOf(lote.getId());
            }

			@Override
			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
				              
                Sort sort=Sort.by("numberLote").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	    System.out.println(entry.getKey() + "/" + entry.getValue());
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                	   }
                	}
                }
                
				Pageable pageable = PageRequest.of(first / pageSize, pageSize, sort);

				Date fechaIni = sumarDiasAFecha(new Date(), -1);
				Date fechaFin = sumarDiasAFecha(new Date(), 1);

				Page<Lote> pageLote;

				if (busqueda) {
					pageLote = loteService.findAllByStatusAndFechaVencimientoBetween(EstadoLote.SEPARADO.getName(),fechaIni, fechaFin, pageable);
				}else {
					pageLote = loteService.findAllByStatusAndFechaVencimientoLessThan(EstadoLote.SEPARADO.getName() , fechaIni, pageable);
				}

				setRowCount((int) pageLote.getTotalElements());
				return datasource = pageLote.getContent();
			}
		};
	}
	
	
	public void iniciarLazyCuota() {
		lstCuotaLazy = new LazyDataModel<Cuota>() {
			private List<Cuota> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Cuota getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Cuota cuota : datasource) {
                    if (cuota.getId() == intRowKey) {
                        return cuota;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Cuota cuota) {
                return String.valueOf(cuota.getId());
            }

			@Override
			public List<Cuota> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
				              
                Sort sort=Sort.by("fechaPago").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	    System.out.println(entry.getKey() + "/" + entry.getValue());
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                	   }
                	}
                }
                
				Pageable pageable = PageRequest.of(first / pageSize, pageSize, sort);

				Date fechaIni = sumarDiasAFecha(new Date(), -1);
				Date fechaFin = sumarDiasAFecha(new Date(), 3);

				Page<Cuota> pageCuota;

				if (busquedaCuota) {
					pageCuota = cuotaService.findByPagoTotalAndEstadoAndFechaPagoBetween("N", true, fechaIni, fechaFin, pageable);
				}else {
					pageCuota = cuotaService.findByPagoTotalAndEstadoAndFechaPagoLessThan("N", true, fechaIni, pageable);
				}

				setRowCount((int) pageCuota.getTotalElements());
				return datasource = pageCuota.getContent();
			}
		};
	}
	
	public  Date sumarDiasAFecha(Date fecha, int dias){
	      if (dias==0) return fecha;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      Date date= calendar.getTime(); 
	   
	      return date; 
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
	public LazyDataModel<Lote> getLstLoteLazy() {
		return lstLoteLazy;
	}
	public void setLstLoteLazy(LazyDataModel<Lote> lstLoteLazy) {
		this.lstLoteLazy = lstLoteLazy;
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
	public boolean isBusqueda() {
		return busqueda;
	}
	public void setBusqueda(boolean busqueda) {
		this.busqueda = busqueda;
	}
	public LazyDataModel<Cuota> getLstCuotaLazy() {
		return lstCuotaLazy;
	}
	public void setLstCuotaLazy(LazyDataModel<Cuota> lstCuotaLazy) {
		this.lstCuotaLazy = lstCuotaLazy;
	}
	public boolean isBusquedaCuota() {
		return busquedaCuota;
	}
	public void setBusquedaCuota(boolean busquedaCuota) {
		this.busquedaCuota = busquedaCuota;
	}
	public CuotaService getCuotaService() {
		return cuotaService;
	}
	public void setCuotaService(CuotaService cuotaService) {
		this.cuotaService = cuotaService;
	}
	
}
