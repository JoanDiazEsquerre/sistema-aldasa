package com.model.aldasa.prospeccion.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.aspectj.weaver.NewParentTypeMunger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.file.UploadedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Action;
import com.model.aldasa.entity.Banco;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DetalleRequerimientoSeparacion;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.ImagenPlantillaVenta;
import com.model.aldasa.entity.ImagenRequerimientoSeparacion;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.entity.VoucherTemp;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ActionService;
import com.model.aldasa.service.BancoService;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.DetalleRequerimientoSeparacionService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.ImagenRequerimientoSeparacionService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.ProspectionService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.service.VoucherService;
import com.model.aldasa.service.VoucherTempService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.EstadoRequerimientoSeparacionType;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class RequerimientoSeparacionBean extends BaseBean implements Serializable {
	
	@ManagedProperty(value = "#{requerimientoSeparacionService}")
	private RequerimientoSeparacionService requerimientoSeparacionService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(value = "#{actionService}")
	private ActionService actionService;
	
	@ManagedProperty(value = "#{prospectionDetailService}")
	private ProspectionDetailService prospectionDetailService;
	
	@ManagedProperty(value = "#{prospectionService}")
	private ProspectionService prospectionService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{voucherService}")
	private VoucherService voucherService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{imagenRequerimientoSeparacionService}")
	private ImagenRequerimientoSeparacionService imagenRequerimientoSeparacionService;
	
	@ManagedProperty(value = "#{loadImageBean}")
	private LoadImageBean loadImageBean;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{voucherTempService}")
	private VoucherTempService voucherTempService;
	
	@ManagedProperty(value = "#{detalleRequerimientoSeparacionService}")
	private DetalleRequerimientoSeparacionService detalleRequerimientoSeparacionService;
	
	
	private LazyDataModel<RequerimientoSeparacion> lstReqSepLazy;

	private RequerimientoSeparacion requerimientoSeparacionSelected;
	private CuentaBancaria cuentaBancariaSelected;
	private Project proyectoReq;	
	private Manzana manzanaReq, manzanaCambio;
	private RequerimientoSeparacion requerimientoSeparacionNew;
	private Team team;
	private Project projectFilter, proyectoCambio;
	private Lote loteCambio;
	
	private String estado = EstadoRequerimientoSeparacionType.PENDIENTE.getDescripcion();
	private String tipoTransaccion="";
	private String numeroTransaccion="";
	private String imagen1, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9, imagen10;
	private boolean valida;
	private int cantidad=0;
	private BigDecimal monto, montoAgregar;
	private Date fechaOperacion = new Date(), fechaCambio ;
	
	private List<CuentaBancaria> lstCuentaBancaria = new ArrayList<>();
	private List<Lote> lstLoteReq = new ArrayList<>();
	private List<Person> lstPerson = new ArrayList<>();
	private List<Team> lstTeam;
	private List<Person> lstPersonAsesor = new ArrayList<>();
	private List<Project> lstProject, lstProyectosCambio;
	private List<Manzana> lstManzanaReq, lstManzanasCambio;
	private List<VoucherTemp> lstVoucherTemporal;
	private List<Lote> lstLotesCambio;
	private List<ImagenRequerimientoSeparacion> lstImagenRequerimientoSeparacion;
	private List<DetalleRequerimientoSeparacion> lstDetalleRequerimientoSeparacionSelected;
	
	private UploadedFile file1,file2,file3,file4,file5,file6,file7,file8,file9,file10;
	private UploadedFile file1Add;

	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	

	@PostConstruct
	public void init() {
		valida = false;
		lstPerson = personService.findByStatus(true);
		lstTeam=teamService.findByStatus(true);
		lstProject=projectService.findByStatusAndSucursalOrderByNameAsc(true, navegacionBean.getSucursalLogin());
		lstProyectosCambio = projectService.findByStatusOrderByNameAsc(true);
		lstCuentaBancaria=cuentaBancariaService.findByEstado(true);
		
		iniciarLazy();
		iniciarDatosRequerimiento();
	}
	
	public void eliminarDetalleRequerimiento(DetalleRequerimientoSeparacion detalle) {
		List<ImagenRequerimientoSeparacion> lstImagen = imagenRequerimientoSeparacionService.findByDetalleRequerimientoSeparacionAndEstado(detalle, true);
		
		if(!lstImagen.isEmpty()) {
			for(ImagenRequerimientoSeparacion imagen : lstImagen) {
				imagen.setEstado(false);
				imagenRequerimientoSeparacionService.save(imagen);
			}
			
			detalle.setEstado(false); 
			detalleRequerimientoSeparacionService.save(detalle);
			
			addInfoMessage("Se eliminó correctamente el detalle");
			verVoucher();
		}else {
			addErrorMessage("No se puede eliminar el detalle.");
		}
		
	}
	
	public void saveDetalleRequerimientoSeparacion() {
		if(montoAgregar == null) {
			addErrorMessage("Ingresar un monto.");
			return;
		}
		
		if(montoAgregar.compareTo(BigDecimal.ZERO) <=0) {
			addErrorMessage("El monto debe ser mayor que cero.");
			return;
		}
		
		if(file1Add == null) {
			addErrorMessage("Debes seleccionar un voucher.");
			return;
		}
		
		DetalleRequerimientoSeparacion newDetalle =  new DetalleRequerimientoSeparacion();
		newDetalle.setRequerimientoSeparacion(requerimientoSeparacionSelected);
		newDetalle.setMonto(montoAgregar);
		newDetalle.setPagoTotal("N");
		newDetalle.setEstado(true);
		newDetalle.setFecha(new Date());
		
		newDetalle = detalleRequerimientoSeparacionService.save(newDetalle);
		if(newDetalle != null) {
			
			requerimientoSeparacionSelected.setMontoAdicional(requerimientoSeparacionSelected.getMontoAdicional().add(montoAgregar));
			requerimientoSeparacionSelected.setPagoTotal("N");
			requerimientoSeparacionService.save(requerimientoSeparacionSelected);
			
			
			
			
			subirImagenesAgregar(requerimientoSeparacionSelected.getId() + "", newDetalle);
			addInfoMessage("Se guardo correctamente.");
		}
		
		verVoucher();
		
		montoAgregar = BigDecimal.ZERO;
		file1Add = null;
		
		
	}
	
	public void subirImagenesAgregar(String idReq, DetalleRequerimientoSeparacion detalleRequerimientoSeparacion) {
		int num = lstImagenRequerimientoSeparacion.size()+1;
		
		String rename = idReq +"_"+num + "." + getExtension(file1Add.getFileName()); 
		ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
		registroImagen.setNombre(rename);
		registroImagen.setCarpeta("IMG-SEPARACIONES");
		registroImagen.setEstado(true);
		registroImagen.setRequerimientoSeparacion(detalleRequerimientoSeparacion.getRequerimientoSeparacion());
		registroImagen.setDetalleRequerimientoSeparacion(detalleRequerimientoSeparacion);
		imagenRequerimientoSeparacionService.save(registroImagen);
		
        subirArchivo(rename, file1Add);
	}
	
	public void subirImagenes(String idReq, DetalleRequerimientoSeparacion detallerequerimientoSeparacion) {
		if(file1 != null) {
			String rename = idReq +"_1" + "." + getExtension(file1.getFileName()); 
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file1);
		}
		if(file2 != null) {
			String rename = idReq +"_2" + "." + getExtension(file2.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file2);
		}
		if(file3 != null) {
			String rename = idReq +"_3" + "." + getExtension(file3.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file3);
		}
		if(file4 != null) {
			String rename = idReq +"_4" + "." + getExtension(file4.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file4);
		}
		if(file5 != null) {
			String rename = idReq +"_5" + "." + getExtension(file5.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file5);
		}
		if(file6 != null) {
			String rename = idReq +"_6" + "." + getExtension(file6.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file6);
		}
		if(file7 != null) {
			String rename = idReq +"_7" + "." + getExtension(file7.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file7);
		}
		if(file8 != null) {
			String rename = idReq +"_8" + "." + getExtension(file8.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file8);
		}
		if(file9 != null) {
			String rename = idReq +"_9" + "." + getExtension(file9.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file9);
		}
		if(file10 != null) {
			String rename = idReq +"_10" + "." + getExtension(file10.getFileName());
			ImagenRequerimientoSeparacion registroImagen = new ImagenRequerimientoSeparacion();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-SEPARACIONES");
			registroImagen.setEstado(true);
			registroImagen.setRequerimientoSeparacion(detallerequerimientoSeparacion.getRequerimientoSeparacion());
			imagenRequerimientoSeparacionService.save(registroImagen);
			
            subirArchivo(rename, file10);
		}
	}
	
	public void subirArchivo(String nombre, UploadedFile file) {
		//  File result = new File("/home/imagen/IMG-DOCUMENTO-VENTA/" + nombre);
		//  File result = new File("C:\\IMG-DOCUMENTO-VENTA\\" + nombre);
	  File result = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaVoucher() + nombre);
	
		  try {
		
		      FileOutputStream fileOutputStream = new FileOutputStream(result);
		
		      byte[] buffer = new byte[1024];
		
		      int bulk;
		
		      // Here you get uploaded picture bytes, while debugging you can see that 34818
		      InputStream inputStream = file.getInputStream();
		
		      while (true) {
		
		          bulk = inputStream.read(buffer);
		
		          if (bulk < 0) {
		
		              break;
		
		          } //end of if
		
		          fileOutputStream.write(buffer, 0, bulk);
		          fileOutputStream.flush();
		
		      } //end fo while(true)
		
		      fileOutputStream.close();
		      inputStream.close();
		
		      FacesMessage msg = new FacesMessage("El archivo subió correctamente.");
		      FacesContext.getCurrentInstance().addMessage(null, msg);
		
		  } catch (IOException e) {
		      e.printStackTrace();
		      FacesMessage error = new FacesMessage("The files were not uploaded!");
		      FacesContext.getCurrentInstance().addMessage(null, error);
		  }
	}
	
	public void opcionCambiarFechaVencimiento() {
		fechaCambio = requerimientoSeparacionSelected.getFechaVencimiento();
	}
	
	public void cambiarFechaVencimiento() {
		if(fechaCambio == null) {
			addErrorMessage("Debe seleccionar una fecha."); 
			return;
		}
		

		SimpleDateFormat sdfQuery = new SimpleDateFormat("yyyy-MM-dd");  
		String fechaHoyText = sdfQuery.format(new Date());
		Date fechaHoy = null;
		try {
			fechaHoy = sdfQuery.parse(fechaHoyText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String fechaReqText = sdfQuery.format(fechaCambio);
		Date fechaReq=null;
		try {
			fechaReq = sdfQuery.parse(fechaReqText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		if(fechaHoy.after(fechaReq) || fechaHoy.equals(fechaReq) ) { 
			addErrorMessage("La fecha de vencimiento tiene que ser mayor a la fecha actual.");
			return;
		}
		
		requerimientoSeparacionSelected.setFechaVencimiento(fechaCambio);
		requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.EN_PROCESO.getDescripcion());
		requerimientoSeparacionService.save(requerimientoSeparacionSelected);
		
		requerimientoSeparacionSelected.getLote().setStatus(EstadoLote.SEPARADO.getName());
		loteService.save(requerimientoSeparacionSelected.getLote());
		
		PrimeFaces.current().executeScript("PF('cambiarFechaVencDialog').hide();");
		
	}
	
	public void pasarSinAsignarRequerimiento() {
		requerimientoSeparacionSelected.setSucursal(navegacionBean.getSucursalLogin()); 
		requerimientoSeparacionSelected.setLote(null);
		requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.SIN_ASIGNAR.getDescripcion());
		requerimientoSeparacionService.save(requerimientoSeparacionSelected);
		
		addInfoMessage("Se pasó correctamente la separacion."); 
	}
	
	public void listarManzanas() {
		manzanaCambio=null;
		loteCambio = null;
		lstLotesCambio = new ArrayList<>();
		lstManzanasCambio = new ArrayList<>();
		if(proyectoCambio != null) {
			lstManzanasCambio = manzanaService.findByProject(proyectoCambio.getId());
		}
	}
	
	public void listarLotes() {
		if(manzanaCambio != null) {
			lstLotesCambio = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyectoCambio, manzanaCambio, "%%");
		}else {
			lstLotesCambio = new ArrayList<>();
		}
	}
	
	public void eliminarDatoTemporal(VoucherTemp temp) {
		temp.setEstado(false);
		voucherTempService.save(temp);
		
		listarDatosTemporales();
		addInfoMessage("Se eliminó correctamente");
		
	}
	
	public void saveVoucherTemp() {
		VoucherTemp busqueda = voucherTempService.findByRequerimientoSeparacionAndMontoAndTipoTransaccionAndNumeroOperacionAndFechaOperacionAndCuentaBancariaAndEstado(requerimientoSeparacionSelected, monto, tipoTransaccion, numeroTransaccion, fechaOperacion, cuentaBancariaSelected, true);
		if(busqueda != null) {
			addErrorMessage("El voucher ya se registró a datos temporales.");
		}else {
			busqueda = new VoucherTemp();
			busqueda.setRequerimientoSeparacion(requerimientoSeparacionSelected);
			busqueda.setMonto(monto);
			busqueda.setTipoTransaccion(tipoTransaccion);
			busqueda.setNumeroOperacion(numeroTransaccion);
			busqueda.setFechaOperacion(fechaOperacion);
			busqueda.setCuentaBancaria(cuentaBancariaSelected);
			busqueda.setEstado(true);
			voucherTempService.save(busqueda);
			listarDatosTemporales();
			addInfoMessage("El voucher se añadió a datos temporales correctamente");
			
		}
	}
	

	public void listarDatosTemporales() {
		lstVoucherTemporal = voucherTempService.findByRequerimientoSeparacionAndEstado(requerimientoSeparacionSelected, true);
	}
	
	public void validarAnulacion() {
		if(requerimientoSeparacionSelected.getDocumentoVenta()!=null) {
			DocumentoVenta docNota = documentoVentaService.findByDocumentoVentaRefAndEstado(requerimientoSeparacionSelected.getDocumentoVenta(), true);
			
			if(docNota==null) {
				if(requerimientoSeparacionSelected.getDocumentoVenta().isEstado()) {
					addErrorMessage("Primero debe anular o generar nota de credito del documento de venta.");
					return;
				}
			}
		}
		PrimeFaces.current().executeScript("PF('anularRequerimiento').show();");
		
	}
	
	public void iniciarVariablesCambioLote() {
		proyectoCambio=null;
		lstManzanasCambio = new ArrayList<>();
		manzanaCambio = null;
		lstLotesCambio = new ArrayList<>();
		loteCambio = null;
	}
	
	public void saveCambioLote() {
	
		if(loteCambio== null) {
			addErrorMessage("Debes seleccionar un lote.");
			return;
		}
		
		if(requerimientoSeparacionSelected.getLote() != null) {
			if(requerimientoSeparacionSelected.getLote().getId().equals(loteCambio.getId())) {
				addErrorMessage("Has seleccionado el mismo lote."); 
				return;
			}
			
			if(!loteCambio.getProject().getSucursal().getId().equals(requerimientoSeparacionSelected.getLote().getProject().getSucursal().getId()) && requerimientoSeparacionSelected.getDocumentoVenta()!=null) {
				addErrorMessage("Primero debe anular o generar nota de credito del documento de venta.");
				return;
			}
			
			RequerimientoSeparacion reqBusquedaPendiente = requerimientoSeparacionService.findAllByLoteAndEstado(loteCambio, EstadoRequerimientoSeparacionType.PENDIENTE.getDescripcion());
			RequerimientoSeparacion reqBusquedaAprobado = requerimientoSeparacionService.findAllByLoteAndEstado(loteCambio, EstadoRequerimientoSeparacionType.EN_PROCESO.getDescripcion());
			
			if(reqBusquedaAprobado != null || reqBusquedaPendiente != null) {
				addErrorMessage("Ya existe una separacion el lote seleccionado.");
				return;
			}
			
		
			Lote loteNuevo = loteService.findById(loteCambio.getId());
			Lote loteAnterior = loteService.findById(requerimientoSeparacionSelected.getLote().getId());
			
			if(loteNuevo.getStatus().equals(EstadoLote.SEPARADO.getName()) || loteNuevo.getStatus().equals(EstadoLote.VENDIDO.getName())) {
				addErrorMessage("El lote al que deseas cambiar ya se encuentra "+ loteNuevo.getStatus().toUpperCase()); 
				return;
			}
			
			if(loteAnterior.getStatus().equals(EstadoLote.SEPARADO.getName())) {
				loteAnterior.setStatus(EstadoLote.DISPONIBLE.getName()); 
				loteService.save(loteAnterior);
			}
			
			
			
			requerimientoSeparacionSelected.setLote(loteCambio);
			
			requerimientoSeparacionService.save(requerimientoSeparacionSelected);
			
			addInfoMessage("Se cambio de lote correctamente.");
	
			PrimeFaces.current().executeScript("PF('cambiarLoteDialog').hide();");
		}else {
			List<RequerimientoSeparacion> lstReqByLote = requerimientoSeparacionService.findByLote(loteCambio);
			for(RequerimientoSeparacion req : lstReqByLote) {
				if(!req.getEstado().equals(EstadoRequerimientoSeparacionType.ANULADO.getDescripcion()) && !req.getEstado().equals(EstadoRequerimientoSeparacionType.RECHAZADO.getDescripcion())) {
					addErrorMessage("Ya existe una separacion "+req.getEstado().toUpperCase()+" con el mismo lote.");
					return;
				}
			}
			
			
			
			
			SimpleDateFormat sdfQuery = new SimpleDateFormat("yyyy-MM-dd");  
			String fechaHoyText = sdfQuery.format(new Date());
			Date fechaHoy = null;
			try {
				fechaHoy = sdfQuery.parse(fechaHoyText);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String fechaReqText = sdfQuery.format(requerimientoSeparacionSelected.getFechaVencimiento());
			Date fechaReq=null;
			try {
				fechaReq = sdfQuery.parse(fechaReqText);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			if(fechaHoy.after(fechaReq)) {
				requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.VENCIDO.getDescripcion());
				requerimientoSeparacionService.save(requerimientoSeparacionSelected);
				
				loteCambio.setStatus(EstadoLote.DISPONIBLE.getName());
				loteService.save(loteCambio); 
			}else {
				requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.EN_PROCESO.getDescripcion());
				requerimientoSeparacionService.save(requerimientoSeparacionSelected);
				
				loteCambio.setStatus(EstadoLote.SEPARADO.getName());
				loteService.save(loteCambio);
			}
				
				
			
			
			
			requerimientoSeparacionSelected.setLote(loteCambio);
			requerimientoSeparacionService.save(requerimientoSeparacionSelected);
			addInfoMessage("Se cambio de lote correctamente.");
			
			PrimeFaces.current().executeScript("PF('cambiarLoteDialog').hide();");
		}
		
	}
	
	public void anularRequerimiento() {
		requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.ANULADO.getDescripcion());
		requerimientoSeparacionService.save(requerimientoSeparacionSelected);
		
		if(requerimientoSeparacionSelected.getLote().getStatus().equals(EstadoLote.SEPARADO.getName())) {
			requerimientoSeparacionSelected.getLote().setStatus(EstadoLote.DISPONIBLE.getName());
			loteService.save(requerimientoSeparacionSelected.getLote());
		}
		addInfoMessage("Se anuló correctamente el requerimiento"); 
	}
	
	public String convertirHora (Date hora) {
		String a = "";
		if(hora !=null) {
			a = sdf.format(hora);
		}
		return a;
	}
	
	public boolean detectarVencido (Date hora) {
		Date hoy = new Date() ;
		hoy.setHours(0); 
		hoy.setMinutes(0);
		hoy.setSeconds(0);
		
		boolean a  = false;
		if(hora !=null) {
			if(sdf.format(hoy).equals(sdf.format(hora))) {
				a = false;
			}else if(hoy.after(hora)) {
				a = true;
			}
		}
		return a;
	}
	
	public String convertirHoraFull (Date hora) {
		String a = "";
		if(hora !=null) {
			a = sdfFull.format(hora);
		}
		return a;
	}
	
	public void aprobarRequerimiento() {
		Lote lote = loteService.findById(requerimientoSeparacionSelected.getLote().getId());
		if(lote.getStatus().equals(EstadoLote.DISPONIBLE.getName())) {
			
			requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.EN_PROCESO.getDescripcion());
			requerimientoSeparacionSelected.setUsuarioAprueba(navegacionBean.getUsuarioLogin());
			requerimientoSeparacionSelected.setFechaAprueba(new Date());
			requerimientoSeparacionSelected.setFechaVencimiento(sumarRestarFecha(requerimientoSeparacionSelected.getFecha(), 7));
 			requerimientoSeparacionService.save(requerimientoSeparacionSelected);


 			lote.setStatus(EstadoLote.SEPARADO.getName());
 			lote.setFechaSeparacion(requerimientoSeparacionSelected.getFecha());
 			lote.setFechaVencimiento(sumarRestarFecha(requerimientoSeparacionSelected.getFecha(), 7));
 			lote.setPersonSupervisor(requerimientoSeparacionSelected.getPersonSupervisor());
 			lote.setPersonAssessor(requerimientoSeparacionSelected.getPersonAsesor());
			
			loteService.save(lote);
			
			PrimeFaces.current().executeScript("PF('plantillaNewDialog').hide();");
			addInfoMessage("Aprobado correctamente.");
		}else {
			addErrorMessage("El lote se encuentra " + lote.getStatus());

		}	
		
	}
	
	public void rechazarRequerimiento () {
		requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.RECHAZADO.getDescripcion());
		requerimientoSeparacionService.save(requerimientoSeparacionSelected);
	}
	
	public void validaVoucher() {
		valida=false;
		if(cuentaBancariaSelected == null) {
			addErrorMessage("Seleccionar una cuenta bancaria.");
			return;
		}
		
		if(monto == null) {
			addErrorMessage("Ingresar un monto.");
			return;
		}
		
		if(tipoTransaccion.equals("")) {
			addErrorMessage("Seleccionar un tipo de transacción.");
			return;
		}
		
		if(numeroTransaccion.equals("")) {
			addErrorMessage("Seleccionar un numero de Operación.");
			return;
		}
		
		if(fechaOperacion == null) {
			addErrorMessage("Ingresar una fecha de operación.");
			return;
		}
		
		List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaOperacion, monto, numeroTransaccion, cuentaBancariaSelected);
		if(!buscarImagen.isEmpty()) {
			addErrorMessage("Ya existe el voucher.");
		}else {
			addInfoMessage("Voucher aceptable"); 
			valida = true;
		}
	}
	
	
	public void verVoucher() {
		listarDatosTemporales();
		valida=false;
		montoAgregar = BigDecimal.ZERO;
		
		cuentaBancariaSelected = null;
		monto = null;
		tipoTransaccion = "";
		numeroTransaccion = "";
		fechaOperacion = new Date();
		
		loadImageBean.setNombreArchivo("0.png");
		imagen1 = "";
		imagen2 = "";
		imagen3 = "";
		imagen4 = "";
		imagen5 = "";
		imagen6 = "";
		imagen7 = "";
		imagen8 = "";
		imagen9 = "";
		imagen10 = "";
		
//		String nombreBusqueda = "%"+plantillaVentaSelected.getId() +"_%";
		
		lstDetalleRequerimientoSeparacionSelected = detalleRequerimientoSeparacionService.findByEstadoAndRequerimientoSeparacion(true, requerimientoSeparacionSelected);
		
		lstImagenRequerimientoSeparacion = imagenRequerimientoSeparacionService.findByRequerimientoSeparacionAndEstado(requerimientoSeparacionSelected, true);
		int contador = 1;
		for(ImagenRequerimientoSeparacion i:lstImagenRequerimientoSeparacion) {
			if(contador==1) {
				imagen1 = i.getNombre();
			}
			if(contador==2) {
				imagen2 = i.getNombre();
			}
			if(contador==3) {
				imagen3 = i.getNombre();
			}
			if(contador==4) {
				imagen4 = i.getNombre();
			}
			if(contador==5) {
				imagen5 = i.getNombre();
			}
			if(contador==6) {
				imagen6 = i.getNombre();
			}
			if(contador==7) {
				imagen7 = i.getNombre();
			}
			if(contador==8) {
				imagen8 = i.getNombre();
			}
			if(contador==9) {
				imagen9 = i.getNombre();
			}
			if(contador==10) {
				imagen10 = i.getNombre();
			}
			contador ++;
		}
//			PrimeFaces.current().executeScript("PF('voucherDocumentoDialog').show();");
	
	}
	
	public void saveRequerimientoSeparacion() {
		requerimientoSeparacionNew.setFecha(new Date());
		requerimientoSeparacionNew.setPersonSupervisor(team.getPersonSupervisor());
		requerimientoSeparacionNew.setEstado(EstadoRequerimientoSeparacionType.PENDIENTE.getDescripcion());
		requerimientoSeparacionNew.setGeneraDocumento(false);
		requerimientoSeparacionNew.setPagoTotal("N");
		requerimientoSeparacionNew.setMontoAdicional(BigDecimal.ZERO); 
		
		requerimientoSeparacionNew= requerimientoSeparacionService.save(requerimientoSeparacionNew); 
		
		if(requerimientoSeparacionNew == null) {
			addErrorMessage("No se pudo guardar.");
			return;
		}else {
			DetalleRequerimientoSeparacion newDetalle =  new DetalleRequerimientoSeparacion();
			newDetalle.setRequerimientoSeparacion(requerimientoSeparacionNew);
			newDetalle.setMonto(montoAgregar);
			newDetalle.setPagoTotal("N");
			newDetalle.setEstado(true);
			newDetalle.setFecha(new Date()); 
			
			newDetalle = detalleRequerimientoSeparacionService.save(newDetalle);
			
			
			subirImagenes(requerimientoSeparacionNew.getId() + "", newDetalle);
			addInfoMessage("Se guardo correctamente.");
			iniciarDatosRequerimiento();
			
		}
	}
	
	
		
	public void iniciarDatosRequerimiento() {
		requerimientoSeparacionNew = new RequerimientoSeparacion();
		team = null;
		proyectoReq = null;
		manzanaReq = null;
		
		lstManzanaReq = new ArrayList<>();
		lstLoteReq = new ArrayList<>();
		lstPersonAsesor = new ArrayList<>();

		file1=null;
		file2=null;
		file3=null;
		file4=null;
		file5=null;
		file6=null;
		file7=null;
		file8=null;
		file9=null;
		file10=null;
	}
	
	public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
	
	public void validaDatosRequerimiento() {
		if(requerimientoSeparacionNew.getPerson()==null) {
			addErrorMessage("Debes seleccionar una persona.");
			return;
		}
		if(team==null) {
			addErrorMessage("Debes seleccionar un equipo.");
			return;
		}
		if(requerimientoSeparacionNew.getPersonAsesor()==null) {
			addErrorMessage("Debes seleccionar un asesor.");
			return;
		}
		if(proyectoReq ==null) {
			addErrorMessage("Debes seleccionar Proyecto.");
			return;
		}
		if(manzanaReq ==null) {
			addErrorMessage("Debes seleccionar Manzana.");
			return;
		}
		if(requerimientoSeparacionNew.getLote()==null) {
			addErrorMessage("Debes seleccionar Lote.");
			return;
		}else {
			Lote lote = loteService.findById(requerimientoSeparacionNew.getLote().getId());
			if(lote.getStatus().equals(EstadoLote.VENDIDO.getName()) || lote.getStatus().equals(EstadoLote.SEPARADO.getName())) {
				addErrorMessage("El lote se encuentra "+lote.getStatus());
				return;
			}
		}
		if(requerimientoSeparacionNew.getMonto()==null) {
			addErrorMessage("Debes ingresar monto de venta.");
			return;
		}else {
			if (requerimientoSeparacionNew.getMonto().compareTo(BigDecimal.ZERO) <=0) {
				addErrorMessage("El monto debe ser mayor que 0.");
				return;
			}
		}
		

		if(file1 == null && file2 == null && file3 == null && file4 == null && file5 == null && file6 == null && file7 == null && file8 == null && file9 == null && file10 == null) {
			addErrorMessage("Debes ingresar al menos una imagen.");
			return;
		}
		
  		PrimeFaces.current().executeScript("PF('saveRequerimiento').show();"); 
	}
	
	public void listarManzanaPlantilla() {
		manzanaReq=null;
		requerimientoSeparacionNew.setLote(null);
		lstLoteReq = new ArrayList<>();
		if(proyectoReq != null) {
			lstManzanaReq = manzanaService.findByProject(proyectoReq.getId());
		}else {
			lstManzanaReq = new ArrayList<>();
		}
	}
	
	public void listarLotesPlantilla() {
		if(manzanaReq != null) {
			lstLoteReq = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyectoReq, manzanaReq, "%%");
		}else {
			lstLoteReq = new ArrayList<>();
		}
	}
	
//	public void modifyRequerimiento() {
//		fechaSeparacion = new Date();
//		fechaVencimiento = sumarRestarFecha(fechaSeparacion, 7);
//		
//	}
	
//	public void ejecutarRequerimiento() {
//		if(fechaSeparacion == null) {
//			addErrorMessage("Ingresar una fecha de Separación");
//			return;
//		}
//		if(fechaVencimiento==null) {
//			addErrorMessage("Ingresar una fecha de separación");
//			return;
//		}
//		
//		if(fechaSeparacion.after(fechaVencimiento)) {
//			addErrorMessage("La fecha de vencimiento tiene que ser mayor a fecha de separación");
//			return;
//		}
//		
//			
//		Lote lote = loteService.findById(requerimientoSeparacionSelected.getLote().getId());
//		if(lote.getStatus().equals(EstadoLote.DISPONIBLE.getName())) {
//			
//			requerimientoSeparacionSelected.setEstado(EstadoRequerimientoSeparacionType.EJECUTADO.getDescripcion());
//			requerimientoSeparacionService.save(requerimientoSeparacionSelected);
//			
//			lote.setStatus(EstadoLote.SEPARADO.getName());
//			lote.setFechaSeparacion(fechaSeparacion);
//			lote.setFechaVencimiento(fechaVencimiento);
//			lote.setPersonSupervisor(requerimientoSeparacionSelected.getProspection().getPersonSupervisor());
//			lote.setPersonAssessor(requerimientoSeparacionSelected.getProspection().getPersonAssessor());
//			loteService.save(lote);
//			
//			ProspectionDetail detalle = new ProspectionDetail();
//			detalle.setDate(fechaSeparacion);
//			detalle.setScheduled(false);
//			
//			Optional<Action> accion = actionService.findById(9);
//			
//			detalle.setAction(accion.get());
//			detalle.setProspection(requerimientoSeparacionSelected.getProspection());
//			
//			ProspectionDetail save = prospectionDetailService.save(detalle);
//			if(save!=null) {
//				
//				if(detalle.getAction().getPorcentage()> requerimientoSeparacionSelected.getProspection().getPorcentage()) {
//					requerimientoSeparacionSelected.getProspection().setPorcentage(detalle.getAction().getPorcentage());
//					prospectionService.save(requerimientoSeparacionSelected.getProspection());
//				}
//				
//			}	
//			addInfoMessage("Se ejecutó separación correctamente.");
//			
//		}else {
//			addErrorMessage("El lote se encuentra " + lote.getStatus());
//		}		
//
//	}
	
	public void iniciarLazy() {

		lstReqSepLazy = new LazyDataModel<RequerimientoSeparacion>() {
			private List<RequerimientoSeparacion> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public RequerimientoSeparacion getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (RequerimientoSeparacion req : datasource) {
                    if (req.getId() == intRowKey) {
                        return req;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(RequerimientoSeparacion requerimientoSeparacion) {
                return String.valueOf(requerimientoSeparacion.getId());
            }

			@Override
			public List<RequerimientoSeparacion> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				String manzana="%"+ (filterBy.get("lote.manzana.name")!=null?filterBy.get("lote.manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String numLote="%"+ (filterBy.get("lote.numberLote")!=null?filterBy.get("lote.numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String supervisor="%"+ (filterBy.get("personSupervisor.surnames")!=null?filterBy.get("personSupervisor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String asesor="%"+ (filterBy.get("personAsesor.surnames")!=null?filterBy.get("personAsesor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String persona="%"+ (filterBy.get("person.surnames")!=null?filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";

				
				Sort sort=Sort.by("fecha").descending();
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
				
				Page<RequerimientoSeparacion> pageReqSep=null;
				
				if(estado.equals(EstadoRequerimientoSeparacionType.SIN_ASIGNAR.getDescripcion())) { 
					pageReqSep= requerimientoSeparacionService.findAllByEstadoAndSucursalAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(estado, navegacionBean.getSucursalLogin(), persona, asesor, supervisor, pageable);
				}else {
					if(projectFilter!=null) {
						pageReqSep= requerimientoSeparacionService.findAllByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(estado, navegacionBean.getSucursalLogin(), projectFilter, manzana,numLote, persona, asesor, supervisor, pageable);
	                }else {
	    				pageReqSep= requerimientoSeparacionService.findAllByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonSurnamesLikeAndPersonAsesorSurnamesLikeAndPersonSupervisorSurnamesLike(estado, navegacionBean.getSucursalLogin(),manzana,numLote, persona, asesor, supervisor, pageable);

	                }

				}
				
								
				
				
				setRowCount((int) pageReqSep.getTotalElements());
				cantidad = getRowCount();
				return datasource = pageReqSep.getContent();
			}
		};
	}
	
	public void listarAsesores() {
		requerimientoSeparacionNew.setPersonAsesor(null); 
	
		lstPersonAsesor = new ArrayList<>();
		List<Empleado> lstEmpleado = null;
		if(team!=null) {
			lstEmpleado = empleadoService.findByEstadoAndTeam(true, team);
		}
		
		if(lstEmpleado!=null) {
			for(Empleado e : lstEmpleado) {
				lstPersonAsesor.add(e.getPerson());
			}
		}
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
	
	public List<Person> completePersonAsesor(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPersonAsesor) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public void obtenerBanco() {
		if(cuentaBancariaSelected!=null) {
			System.out.println("BANCO SELECCIONADO: "+ cuentaBancariaSelected.getBanco().getAbreviatura());
		}else {
			System.out.println("SIN SELECCIONAR ");
		}
		
	}
	
	public void obtenerTipoT() {
		System.out.println("TT SELECCIONADO: "+ tipoTransaccion);
	}
	
	public Converter getConversorCuentaBancaria() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	CuentaBancaria c = null;
                    for (CuentaBancaria si : lstCuentaBancaria) {
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
                    return ((CuentaBancaria) value).getId() + "";
                }
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
	
	public Converter getConversorPersonAsesor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Person c = null;
                    for (Person si : lstPersonAsesor) {
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
	
	public Converter getConversorProject() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Project c = null;
                    for (Project si : lstProject) {
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
	
	public Converter getConversorProyectoCambio() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Project c = null;
                    for (Project si : lstProyectosCambio) {
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
	
	public Converter getConversorManzanaPlantilla() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Manzana c = null;
                    for (Manzana si : lstManzanaReq) {
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
	
	public Converter getConversorLote() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Lote c = null;
                    for (Lote si : lstLoteReq) {
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
                    return ((Lote) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorManzanaCambio() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Manzana c = null;
                    for (Manzana si : lstManzanasCambio) {
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
	
	public Converter getConversorLoteCambio() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Lote c = null;
                    for (Lote si : lstLotesCambio) {
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
                    return ((Lote) value).getId() + "";
                }
            }
        };
    }
	
	public RequerimientoSeparacionService getRequerimientoSeparacionService() {
		return requerimientoSeparacionService;
	}
	public void setRequerimientoSeparacionService(RequerimientoSeparacionService requerimientoSeparacionService) {
		this.requerimientoSeparacionService = requerimientoSeparacionService;
	}
	public LazyDataModel<RequerimientoSeparacion> getLstReqSepLazy() {
		return lstReqSepLazy;
	}
	public void setLstReqSepLazy(LazyDataModel<RequerimientoSeparacion> lstReqSepLazy) {
		this.lstReqSepLazy = lstReqSepLazy;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public RequerimientoSeparacion getRequerimientoSeparacionSelected() {
		return requerimientoSeparacionSelected;
	}
	public void setRequerimientoSeparacionSelected(RequerimientoSeparacion requerimientoSeparacionSelected) {
		this.requerimientoSeparacionSelected = requerimientoSeparacionSelected;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public ActionService getActionService() {
		return actionService;
	}
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}
	public ProspectionDetailService getProspectionDetailService() {
		return prospectionDetailService;
	}
	public void setProspectionDetailService(ProspectionDetailService prospectionDetailService) {
		this.prospectionDetailService = prospectionDetailService;
	}
	public ProspectionService getProspectionService() {
		return prospectionService;
	}
	public void setProspectionService(ProspectionService prospectionService) {
		this.prospectionService = prospectionService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
	}
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public CuentaBancaria getCuentaBancariaSelected() {
		return cuentaBancariaSelected;
	}
	public void setCuentaBancariaSelected(CuentaBancaria cuentaBancariaSelected) {
		this.cuentaBancariaSelected = cuentaBancariaSelected;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}
	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public VoucherService getVoucherService() {
		return voucherService;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public List<Team> getLstTeam() {
		return lstTeam;
	}
	public void setLstTeam(List<Team> lstTeam) {
		this.lstTeam = lstTeam;
	}
	public List<Person> getLstPersonAsesor() {
		return lstPersonAsesor;
	}
	public void setLstPersonAsesor(List<Person> lstPersonAsesor) {
		this.lstPersonAsesor = lstPersonAsesor;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public Project getProyectoReq() {
		return proyectoReq;
	}
	public void setProyectoReq(Project proyectoReq) {
		this.proyectoReq = proyectoReq;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public List<Project> getLstProject() {
		return lstProject;
	}
	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}
	public Manzana getManzanaReq() {
		return manzanaReq;
	}
	public void setManzanaReq(Manzana manzanaReq) {
		this.manzanaReq = manzanaReq;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	public List<Lote> getLstLoteReq() {
		return lstLoteReq;
	}
	public void setLstLoteReq(List<Lote> lstLoteReq) {
		this.lstLoteReq = lstLoteReq;
	}
	public List<Manzana> getLstManzanaReq() {
		return lstManzanaReq;
	}
	public void setLstManzanaReq(List<Manzana> lstManzanaReq) {
		this.lstManzanaReq = lstManzanaReq;
	}
	
	public UploadedFile getFile1() {
		return file1;
	}
	public void setFile1(UploadedFile file1) {
		this.file1 = file1;
	}
	public UploadedFile getFile2() {
		return file2;
	}
	public void setFile2(UploadedFile file2) {
		this.file2 = file2;
	}
	public UploadedFile getFile3() {
		return file3;
	}
	public void setFile3(UploadedFile file3) {
		this.file3 = file3;
	}
	public UploadedFile getFile4() {
		return file4;
	}
	public void setFile4(UploadedFile file4) {
		this.file4 = file4;
	}
	public UploadedFile getFile5() {
		return file5;
	}
	public void setFile5(UploadedFile file5) {
		this.file5 = file5;
	}
	public UploadedFile getFile6() {
		return file6;
	}
	public void setFile6(UploadedFile file6) {
		this.file6 = file6;
	}
	public UploadedFile getFile7() {
		return file7;
	}
	public void setFile7(UploadedFile file7) {
		this.file7 = file7;
	}
	public UploadedFile getFile8() {
		return file8;
	}
	public void setFile8(UploadedFile file8) {
		this.file8 = file8;
	}
	public UploadedFile getFile9() {
		return file9;
	}
	public void setFile9(UploadedFile file9) {
		this.file9 = file9;
	}
	public UploadedFile getFile10() {
		return file10;
	}
	public void setFile10(UploadedFile file10) {
		this.file10 = file10;
	}
	public RequerimientoSeparacion getRequerimientoSeparacionNew() {
		return requerimientoSeparacionNew;
	}
	public void setRequerimientoSeparacionNew(RequerimientoSeparacion requerimientoSeparacionNew) {
		this.requerimientoSeparacionNew = requerimientoSeparacionNew;
	}
	public ImagenRequerimientoSeparacionService getImagenRequerimientoSeparacionService() {
		return imagenRequerimientoSeparacionService;
	}
	public void setImagenRequerimientoSeparacionService(ImagenRequerimientoSeparacionService imagenRequerimientoSeparacionService) {
		this.imagenRequerimientoSeparacionService = imagenRequerimientoSeparacionService;
	}
	public String getImagen1() {
		return imagen1;
	}
	public void setImagen1(String imagen1) {
		this.imagen1 = imagen1;
	}
	public String getImagen2() {
		return imagen2;
	}
	public void setImagen2(String imagen2) {
		this.imagen2 = imagen2;
	}
	public String getImagen3() {
		return imagen3;
	}
	public void setImagen3(String imagen3) {
		this.imagen3 = imagen3;
	}
	public String getImagen4() {
		return imagen4;
	}
	public void setImagen4(String imagen4) {
		this.imagen4 = imagen4;
	}
	public String getImagen5() {
		return imagen5;
	}
	public void setImagen5(String imagen5) {
		this.imagen5 = imagen5;
	}
	public String getImagen6() {
		return imagen6;
	}
	public void setImagen6(String imagen6) {
		this.imagen6 = imagen6;
	}
	public String getImagen7() {
		return imagen7;
	}
	public void setImagen7(String imagen7) {
		this.imagen7 = imagen7;
	}
	public String getImagen8() {
		return imagen8;
	}
	public void setImagen8(String imagen8) {
		this.imagen8 = imagen8;
	}
	public String getImagen9() {
		return imagen9;
	}
	public void setImagen9(String imagen9) {
		this.imagen9 = imagen9;
	}
	public String getImagen10() {
		return imagen10;
	}
	public void setImagen10(String imagen10) {
		this.imagen10 = imagen10;
	}
	public LoadImageBean getLoadImageBean() {
		return loadImageBean;
	}
	public void setLoadImageBean(LoadImageBean loadImageBean) {
		this.loadImageBean = loadImageBean;
	}
	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
	}
	public DocumentoVentaService getDocumentoVentaService() {
		return documentoVentaService;
	}
	public void setDocumentoVentaService(DocumentoVentaService documentoVentaService) {
		this.documentoVentaService = documentoVentaService;
	}
	public boolean isValida() {
		return valida;
	}
	public void setValida(boolean valida) {
		this.valida = valida;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public VoucherTempService getVoucherTempService() {
		return voucherTempService;
	}
	public void setVoucherTempService(VoucherTempService voucherTempService) {
		this.voucherTempService = voucherTempService;
	}
	public List<VoucherTemp> getLstVoucherTemporal() {
		return lstVoucherTemporal;
	}
	public void setLstVoucherTemporal(List<VoucherTemp> lstVoucherTemporal) {
		this.lstVoucherTemporal = lstVoucherTemporal;
	}
	public Project getProjectFilter() {
		return projectFilter;
	}
	public void setProjectFilter(Project projectFilter) {
		this.projectFilter = projectFilter;
	}
	public Manzana getManzanaCambio() {
		return manzanaCambio;
	}
	public void setManzanaCambio(Manzana manzanaCambio) {
		this.manzanaCambio = manzanaCambio;
	}
	public Project getProyectoCambio() {
		return proyectoCambio;
	}
	public void setProyectoCambio(Project proyectoCambio) {
		this.proyectoCambio = proyectoCambio;
	}
	public List<Project> getLstProyectosCambio() {
		return lstProyectosCambio;
	}
	public void setLstProyectosCambio(List<Project> lstProyectosCambio) {
		this.lstProyectosCambio = lstProyectosCambio;
	}
	public List<Manzana> getLstManzanasCambio() {
		return lstManzanasCambio;
	}
	public void setLstManzanasCambio(List<Manzana> lstManzanasCambio) {
		this.lstManzanasCambio = lstManzanasCambio;
	}
	public Lote getLoteCambio() {
		return loteCambio;
	}
	public void setLoteCambio(Lote loteCambio) {
		this.loteCambio = loteCambio;
	}
	public List<Lote> getLstLotesCambio() {
		return lstLotesCambio;
	}
	public void setLstLotesCambio(List<Lote> lstLotesCambio) {
		this.lstLotesCambio = lstLotesCambio;
	}
	public Date getFechaCambio() {
		return fechaCambio;
	}
	public void setFechaCambio(Date fechaCambio) {
		this.fechaCambio = fechaCambio;
	}
	public UploadedFile getFile1Add() {
		return file1Add;
	}
	public void setFile1Add(UploadedFile file1Add) {
		this.file1Add = file1Add;
	}
	public BigDecimal getMontoAgregar() {
		return montoAgregar;
	}
	public void setMontoAgregar(BigDecimal montoAgregar) {
		this.montoAgregar = montoAgregar;
	}
	public DetalleRequerimientoSeparacionService getDetalleRequerimientoSeparacionService() {
		return detalleRequerimientoSeparacionService;
	}
	public void setDetalleRequerimientoSeparacionService(
			DetalleRequerimientoSeparacionService detalleRequerimientoSeparacionService) {
		this.detalleRequerimientoSeparacionService = detalleRequerimientoSeparacionService;
	}
	public List<ImagenRequerimientoSeparacion> getLstImagenRequerimientoSeparacion() {
		return lstImagenRequerimientoSeparacion;
	}
	public void setLstImagenRequerimientoSeparacion(List<ImagenRequerimientoSeparacion> lstImagenRequerimientoSeparacion) {
		this.lstImagenRequerimientoSeparacion = lstImagenRequerimientoSeparacion;
	}
	public List<DetalleRequerimientoSeparacion> getLstDetalleRequerimientoSeparacionSelected() {
		return lstDetalleRequerimientoSeparacionSelected;
	}
	public void setLstDetalleRequerimientoSeparacionSelected(
			List<DetalleRequerimientoSeparacion> lstDetalleRequerimientoSeparacionSelected) {
		this.lstDetalleRequerimientoSeparacionSelected = lstDetalleRequerimientoSeparacionSelected;
	}
	

	
}
