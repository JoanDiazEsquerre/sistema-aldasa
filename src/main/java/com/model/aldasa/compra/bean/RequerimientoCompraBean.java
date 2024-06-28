package com.model.aldasa.compra.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleCaja;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DetalleRequerimientoCompra;
import com.model.aldasa.entity.RequerimientoCompra;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Unidad;
import com.model.aldasa.general.bean.EmailController;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.CajaService;
import com.model.aldasa.service.DetalleCajaService;
import com.model.aldasa.service.DetalleRequerimientoCompraService;
import com.model.aldasa.service.RequerimientoCompraService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.SerieDocumentoService;
import com.model.aldasa.service.UnidadService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class RequerimientoCompraBean extends BaseBean {
	
	@ManagedProperty(value = "#{detalleRequerimientoCompraService}")
	private DetalleRequerimientoCompraService detalleRequerimientoCompraService;
	
	@ManagedProperty(value = "#{requerimientoCompraService}")
	private RequerimientoCompraService requerimientoCompraService;
	
	@ManagedProperty(value = "#{unidadService}")
	private UnidadService unidadService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{serieDocumentoService}")
	private SerieDocumentoService serieDocumentoService;
	
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<RequerimientoCompra> lstRequerimientoCompraLazy;
	
	private List<DetalleRequerimientoCompra>  lstDetalleRequerimientoCompra;
	private List<Unidad> lstUnidad = new ArrayList<>();


	private RequerimientoCompra requerimientoCompraSelected;
	private DetalleRequerimientoCompra detalleRequerimientoCompraSelected;
	private Unidad unidadFilter;
	private SerieDocumento serieDocumentoReq;
	
	private String descripcionProducto;
	private String formaPago, observacion;
	private Date fecha;
	private BigDecimal cantidad, precio, total;

	private String estado = "Pendiente" ;
	
	@PostConstruct
	public void init() {
		iniciarLazy();
		lstUnidad= unidadService.findByEstado(true);
		lstDetalleRequerimientoCompra = new ArrayList<>();
		
		Optional<SerieDocumento> op = serieDocumentoService.findById(13);
		serieDocumentoReq = op.get();
	}
	
	public void listarListaDetalles() {
		lstDetalleRequerimientoCompra = detalleRequerimientoCompraService.findByRequerimientoCompraAndEstado(requerimientoCompraSelected, true);
	}
	
	public void aprobarOrdenCompra() {
		
		requerimientoCompraSelected.setEstado("Aprobado");
		requerimientoCompraSelected.setUsuarioAprueba(navegacionBean.getUsuarioLogin());
		requerimientoCompraSelected.setFechaAprueba(new Date());
		requerimientoCompraService.save(requerimientoCompraSelected);
		
		addInfoMessage("Se aprobó el requerimiento compra correctamente."); 
		PrimeFaces.current().executeScript("PF('ordenCompraDialog').hide();"); 
	}
	
	public void rechazarOrdenCompra() {
		
		requerimientoCompraSelected.setEstado("Rechazado");
		requerimientoCompraSelected.setUsuarioRechaza(navegacionBean.getUsuarioLogin());
		requerimientoCompraSelected.setFechaRechaza(new Date());
		requerimientoCompraService.save(requerimientoCompraSelected);
		addInfoMessage("Se rechazó el requerimiento compra correctamente."); 
		PrimeFaces.current().executeScript("PF('ordenCompraDialog').hide();"); 
	}
	
	public void deleteDetalle(DetalleRequerimientoCompra detalle) {
		lstDetalleRequerimientoCompra.remove(detalle);
		addInfoMessage("Detalle eliminado correctamente.");
	}
	
	public void calcularTotal() {
		if(cantidad!=null && precio!=null) {
			total=cantidad.multiply(precio);
		}
	}
	
	public void iniciarLazy() {

		lstRequerimientoCompraLazy = new LazyDataModel<RequerimientoCompra>() {
			private List<RequerimientoCompra> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public RequerimientoCompra getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (RequerimientoCompra ordenCompra : datasource) {
                    if (ordenCompra.getId() == intRowKey) {
                        return ordenCompra;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(RequerimientoCompra ordenCompra) {
                return String.valueOf(ordenCompra.getId());
            }

			@Override
			public List<RequerimientoCompra> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
	
                Sort sort=Sort.by("id").ascending();
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
               
                Page<RequerimientoCompra> pageOrdenCompra= null;
                pageOrdenCompra=requerimientoCompraService.findByEstado(estado, pageable);

				
                setRowCount((int) pageOrdenCompra.getTotalElements());
                return datasource = pageOrdenCompra.getContent();
            }
		};
	}

	public void agregarItem() {
		if(cantidad.equals("")) {
			addErrorMessage("Ingresar cantidad.");
			return;
		}
		
		if(unidadFilter==null) {
			addErrorMessage("Ingresar unidad.");
			return;
		}
		
		if(descripcionProducto.equals("")) {
			addErrorMessage("Ingresar descripción del producto.");
			return;
		}
		if(precio==null) {
			addErrorMessage("Ingresar precio.");
			return;
		}
		if(total==null) {
			addErrorMessage("Ingresar total.");
			return;
		}
		
		DetalleRequerimientoCompra detalle = new DetalleRequerimientoCompra();
		detalle.setCantidad(cantidad);
		detalle.setDescripcionProducto(descripcionProducto);
		detalle.setUnidad(unidadFilter);
		detalle.setEstado(true);
		detalle.setPrecio(precio);
		detalle.setTotal(total);
		lstDetalleRequerimientoCompra.add(detalle);
		cantidad = null;
		descripcionProducto="";
		precio = null;
		total = null;
		addInfoMessage("Item Agregado.");
		
	}
	
	public void saveRequerimientoCompra() {
		
		if(formaPago.equals("")) {
			addErrorMessage("Seleccionar forma de pago.");
			return;
		}
		
		if(!lstDetalleRequerimientoCompra.isEmpty()) {
			BigDecimal totalDetalle = BigDecimal.ZERO;
			for(DetalleRequerimientoCompra d:lstDetalleRequerimientoCompra) {
				totalDetalle = totalDetalle.add(d.getTotal());
			}
			RequerimientoCompra compra = new RequerimientoCompra();
			compra.setFechaEmision(new Date());
			compra.setEstado("Pendiente");
			compra.setUsuario(navegacionBean.getUsuarioLogin());
			compra.setFechaRegistro(new Date());
			compra.setFormaPago(formaPago);
			compra.setObservacion(observacion);
			compra.setTotal(totalDetalle);
			RequerimientoCompra guardar = requerimientoCompraService.save(compra, lstDetalleRequerimientoCompra, serieDocumentoReq);
			formaPago = "";
			observacion="";

			if(guardar!= null) {
				lstDetalleRequerimientoCompra.clear();
				enviarCorreo("contabilidad@aldasainmobiliaria.com;joanjesusanthony@gmail.com", "PRUEBA DE ENVIO PARA REQUERIMIENTOS DE COMPRA", "Esto es una prueba");
				addInfoMessage("Requerimiento guardado correctamente.");
				
			}else {
				addErrorMessage("No se pudo guardar el detalle");
			}
		}else {
			addErrorMessage("Lista de detalles esta vacía.");
		}
		
	}
	
	public Converter getConversorUnidad() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Unidad c = null;
                    for (Unidad si : lstUnidad) {
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
                    return ((Unidad) value).getId() + "";
                }
            }
        };
    }
	
	
	
	public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com"); // Servidor SMTP de Outlook para Office 365
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sistemas@aldasainmobiliaria.com", "2022@@sisinfor");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sistemas@aldasainmobiliaria.com"));
            
         // Dividir las direcciones de correo electrónico
            String[] toAddresses = destinatario.split(";");
            InternetAddress[] recipients = new InternetAddress[toAddresses.length];
            for (int i = 0; i < toAddresses.length; i++) {
                recipients[i] = new InternetAddress(toAddresses[i].trim());
            }

            // Establecer destinatarios
            message.setRecipients(Message.RecipientType.TO, recipients);
            
            
            message.setSubject(asunto);
            message.setText(cuerpo);

            Transport.send(message);

            FacesMessage facesMessage = new FacesMessage("Correo enviado con éxito");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        } catch (MessagingException e) {
            FacesMessage facesMessage = new FacesMessage("Error al enviar el correo: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            e.printStackTrace();
        }
    }
	
	

	public LazyDataModel<RequerimientoCompra> getLstRequerimientoCompraLazy() {
		return lstRequerimientoCompraLazy;
	}
	public void setLstRequerimientoCompraLazy(LazyDataModel<RequerimientoCompra> lstRequerimientoCompraLazy) {
		this.lstRequerimientoCompraLazy = lstRequerimientoCompraLazy;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	public String getDescripcionProducto() {
		return descripcionProducto;
	}
	public void setDescripcionProducto(String descripcionProducto) {
		this.descripcionProducto = descripcionProducto;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Unidad getUnidadFilter() {
		return unidadFilter;
	}
	public void setUnidadFilter(Unidad unidadFilter) {
		this.unidadFilter = unidadFilter;
	}
	public UnidadService getUnidadService() {
		return unidadService;
	}
	public void setUnidadService(UnidadService unidadService) {
		this.unidadService = unidadService;
	}
	public List<Unidad> getLstUnidad() {
		return lstUnidad;
	}
	public void setLstUnidad(List<Unidad> lstUnidad) {
		this.lstUnidad = lstUnidad;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public DetalleRequerimientoCompraService getDetalleRequerimientoCompraService() {
		return detalleRequerimientoCompraService;
	}
	public void setDetalleRequerimientoCompraService(DetalleRequerimientoCompraService detalleRequerimientoCompraService) {
		this.detalleRequerimientoCompraService = detalleRequerimientoCompraService;
	}
	public RequerimientoCompraService getRequerimientoCompraService() {
		return requerimientoCompraService;
	}
	public void setRequerimientoCompraService(RequerimientoCompraService requerimientoCompraService) {
		this.requerimientoCompraService = requerimientoCompraService;
	}
	public List<DetalleRequerimientoCompra> getLstDetalleRequerimientoCompra() {
		return lstDetalleRequerimientoCompra;
	}
	public void setLstDetalleRequerimientoCompra(List<DetalleRequerimientoCompra> lstDetalleRequerimientoCompra) {
		this.lstDetalleRequerimientoCompra = lstDetalleRequerimientoCompra;
	}
	public RequerimientoCompra getRequerimientoCompraSelected() {
		return requerimientoCompraSelected;
	}
	public void setRequerimientoCompraSelected(RequerimientoCompra requerimientoCompraSelected) {
		this.requerimientoCompraSelected = requerimientoCompraSelected;
	}
	public DetalleRequerimientoCompra getDetalleRequerimientoCompraSelected() {
		return detalleRequerimientoCompraSelected;
	}
	public void setDetalleRequerimientoCompraSelected(DetalleRequerimientoCompra detalleRequerimientoCompraSelected) {
		this.detalleRequerimientoCompraSelected = detalleRequerimientoCompraSelected;
	}
	public SerieDocumentoService getSerieDocumentoService() {
		return serieDocumentoService;
	}
	public void setSerieDocumentoService(SerieDocumentoService serieDocumentoService) {
		this.serieDocumentoService = serieDocumentoService;
	}
	public SerieDocumento getSerieDocumentoReq() {
		return serieDocumentoReq;
	}
	public void setSerieDocumentoReq(SerieDocumento serieDocumentoReq) {
		this.serieDocumentoReq = serieDocumentoReq;
	}
	
	
}
