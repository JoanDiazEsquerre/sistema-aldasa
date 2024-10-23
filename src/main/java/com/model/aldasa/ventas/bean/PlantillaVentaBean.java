package com.model.aldasa.ventas.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import javax.faces.model.SelectItem;

import org.json.simple.JSONObject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.file.UploadedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.lowagie.text.Image;
import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Identificador;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.ImagenPlantillaVenta;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.MotivoNota;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.entity.TipoOperacion;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.entity.VoucherTemp;
import com.model.aldasa.fe.ConsumingPostBoImpl;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.prospeccion.bean.LoadImagePlantillaBean;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.ComisionSupervisorService;
import com.model.aldasa.service.ComisionesService;
import com.model.aldasa.service.ConfiguracionComisionService;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.CuotaService;
import com.model.aldasa.service.DetalleComisionesService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.IdentificadorService;
import com.model.aldasa.service.ImagenPlantillaVentaService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.MotivoNotaService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.PlantillaVentaService;
import com.model.aldasa.service.ProductoService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.SerieDocumentoService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.TipoDocumentoService;
import com.model.aldasa.service.TipoOperacionService;
import com.model.aldasa.service.VoucherService;
import com.model.aldasa.service.VoucherTempService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoContrato;
import com.model.aldasa.util.EstadoRequerimientoSeparacionType;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.Perfiles;
import com.model.aldasa.util.TipoProductoType;

@ManagedBean
@ViewScoped
public class PlantillaVentaBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{plantillaVentaService}")
	private PlantillaVentaService plantillaVentaService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{loadImagePlantillaBean}")
	private LoadImagePlantillaBean loadImagePlantillaBean;
	
	@ManagedProperty(value = "#{imagenPlantillaVentaService}")
	private ImagenPlantillaVentaService imagenPlantillaVentaService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{requerimientoSeparacionService}")
	private RequerimientoSeparacionService requerimientoSeparacionService;
	
	@ManagedProperty(value = "#{voucherTempService}")
	private VoucherTempService voucherTempService;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{contratoService}")
	private ContratoService contratoService;
	
	@ManagedProperty(value = "#{comisionSupervisorService}")
	private ComisionSupervisorService comisionSupervisorService;
	
	@ManagedProperty(value = "#{configuracionComisionService}")
	private ConfiguracionComisionService configuracionComisionService;
	
	@ManagedProperty(value = "#{comisionesService}")
	private ComisionesService comisionesService;
	
	@ManagedProperty(value = "#{detalleComisionesService}")
	private DetalleComisionesService detalleComisionesService;
	

	private LazyDataModel<PlantillaVenta> lstPlantillaLazy;
	
	private List<Project> lstProject;
	private List<Manzana> lstManzanaPlantilla;
	private List<Lote> lstLotePlantilla = new ArrayList<>();
	private List<CuentaBancaria> lstCuentaBancaria = new ArrayList<>();
	private List<Person> lstPerson = new ArrayList<>();
	private List<Team> lstTeam;
	private List<Person> lstPersonAsesor = new ArrayList<>();
	private List<VoucherTemp> lstVoucherTemporal;

	private PlantillaVenta plantillaVentaSelected;
	private CuentaBancaria cuentaBancariaSelected;
	private Project proyectoPlantilla;	
	private Manzana manzanaPlantilla;
	private Lote lotePlantilla;
	private Team team;
	private Person personCliente, personAsesor;
	private RequerimientoSeparacion requerimientoBusqueda = null;
	
	
	private String estadoPlantillaFilter = "Pendiente", mensajeSeparacion="",mensajeSeparacion2="", observacion;
	private String imagen1, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9, imagen10, imagen11, imagen12, imagen13, imagen14, imagen15;
	private BigDecimal monto, montoPlantilla, interesPlantilla, inicialPlantilla;
	private Date fechaOperacion = new Date() ;
	private String tipoTransaccion, numeroTransaccion, tipoPagoPlantilla;
	private Integer numeroCuotaPlantilla;
	private boolean valida;
	private Date fechaVenta;
	
	private UploadedFile file1,file2,file3,file4,file5,file6,file7,file8,file9,file10,file11,file12,file13,file14,file15;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	
	@PostConstruct
	public void init() {
		valida = false;
		iniciarPlantillaLazy();
		iniciarDatosPlantilla();
		lstProject=projectService.findByStatusAndSucursalOrderByNameAsc(true, navegacionBean.getSucursalLogin());
		lstCuentaBancaria=cuentaBancariaService.findByEstado(true);
		lstPerson = personService.findByStatus(true);
		lstTeam=teamService.findByStatus(true);
	}
	

	public void eliminarDatoTemporal(VoucherTemp temp) {
		temp.setEstado(false);
		voucherTempService.save(temp);
		
		listarDatosTemporales();
		addInfoMessage("Se eliminó correctamente");
		
	}
	
	public void saveVoucherTemp() {
		VoucherTemp busqueda = voucherTempService.findByPlantillaVentaAndMontoAndTipoTransaccionAndNumeroOperacionAndFechaOperacionAndCuentaBancariaAndEstado(plantillaVentaSelected, monto, tipoTransaccion, numeroTransaccion, fechaOperacion, cuentaBancariaSelected, true);

		if(busqueda != null) {
			addErrorMessage("El voucher ya se registró a datos temporales.");
		}else {
			busqueda = new VoucherTemp();
			busqueda.setPlantillaVenta(plantillaVentaSelected);
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
	
	public String convertirHora(Date hora) {
		String a = "";
		if(hora != null) {
			a = sdf.format(hora);
		}
		
		return a;
	}
    
    public String convertirHoraFull(Date hora) {
		String a = "";
		if(hora != null) {
			a = sdfFull.format(hora);
		}
		
		return a;
	}
	
	public void consultarSeparacion() {
		requerimientoBusqueda = null;
		mensajeSeparacion="";
		if(lotePlantilla != null) {
			
			requerimientoBusqueda = requerimientoSeparacionService.findAllByLoteAndEstado(lotePlantilla, EstadoRequerimientoSeparacionType.EN_PROCESO.getDescripcion());
			if(requerimientoBusqueda!=null) {
				mensajeSeparacion = "- El lote seleccionado tiene una separacion de "+ requerimientoBusqueda.getMonto() +" soles. ";
			}
		}
		
	}
	
	public void consultarSeparacionPorPersona() {
		mensajeSeparacion2="";
		
		if(personCliente != null) {
			List<RequerimientoSeparacion> lstReq = requerimientoSeparacionService.findByPersonId(personCliente.getId());
			
			for(RequerimientoSeparacion req : lstReq) {
				if(req.getEstado().equals(EstadoRequerimientoSeparacionType.PENDIENTE.getDescripcion()) || req.getEstado().equals(EstadoRequerimientoSeparacionType.EN_PROCESO.getDescripcion()) || 
						req.getEstado().equals(EstadoRequerimientoSeparacionType.SIN_ASIGNAR.getDescripcion()) || req.getEstado().equals(EstadoRequerimientoSeparacionType.VENCIDO.getDescripcion())) {
					
					mensajeSeparacion2 =mensajeSeparacion2.concat("- El cliente tiene separacion del lote: "+ req.getLote().getManzana().getName()+"-" + req.getLote().getNumberLote()+" "+ req.getLote().getProject().getName()+ " '"+req.getEstado().toUpperCase() + "' / ");
					
				}
				
				
			}
		}
		
	}
	
	public void validarAnulacion() {
		if(plantillaVentaSelected.getDocumentoVenta()!=null) {
			DocumentoVenta docNota = documentoVentaService.findByDocumentoVentaRefAndEstado(plantillaVentaSelected.getDocumentoVenta(), true);
			
			if(docNota==null) {
				if(plantillaVentaSelected.getDocumentoVenta().isEstado()) {
					addErrorMessage("Primero debe anular o generar nota de credito del documento de venta.");
					return;
				}
			}
		}
		
		Contrato contrato = contratoService.findByLoteAndEstado(plantillaVentaSelected.getLote(), EstadoContrato.ACTIVO.getName());
		if(contrato!=null) {
			addErrorMessage("Primero debe anular el contrato.");
			return;
		}
		
		PrimeFaces.current().executeScript("PF('anulaPlantilla').show();");
	}
	
	public void anularPlantilla() {
		
		
		plantillaVentaService.anularPlantilla(plantillaVentaSelected);
		addInfoMessage("Plantilla anulado correctamente.");	
	}
	
	public void listarAsesores() {
		personAsesor = null;
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
	
	public void iniciarDatosPlantilla() {
		fechaVenta = new Date();
		personCliente=null;
		team = null;
		personAsesor = null;
		proyectoPlantilla =null;
		manzanaPlantilla = null;
		lotePlantilla = null;
		tipoPagoPlantilla = "CONTADO";
		montoPlantilla = null;
		numeroCuotaPlantilla = null;
		interesPlantilla = null;
		inicialPlantilla = null;
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
		file11=null;
		file12=null;
		file13=null;
		file14=null;
		file15=null;
	}
	
	public void subirArchivo(String nombre, UploadedFile file) {
		//  File result = new File("/home/imagen/IMG-DOCUMENTO-VENTA/" + nombre);
		//  File result = new File("C:\\IMG-DOCUMENTO-VENTA\\" + nombre);
	  File result = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta() + nombre);
	
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
		
	
	public void subirImagenes(String idPlantilla, PlantillaVenta plantilla) {
		
		if(file1 != null) {
			String rename = idPlantilla +"_1" + "." + getExtension(file1.getFileName()); 
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file1);
		}
		if(file2 != null) {
			String rename = idPlantilla +"_2" + "." + getExtension(file2.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file2);
		}
		if(file3 != null) {
			String rename = idPlantilla +"_3" + "." + getExtension(file3.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file3);
		}
		if(file4 != null) {
			String rename = idPlantilla +"_4" + "." + getExtension(file4.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file4);
		}
		if(file5 != null) {
			String rename = idPlantilla +"_5" + "." + getExtension(file5.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file5);
		}
		if(file6 != null) {
			String rename = idPlantilla +"_6" + "." + getExtension(file6.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file6);
		}
		if(file7 != null) {
			String rename = idPlantilla +"_7" + "." + getExtension(file7.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file7);
		}
		if(file8 != null) {
			String rename = idPlantilla +"_8" + "." + getExtension(file8.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file8);
		}
		if(file9 != null) {
			String rename = idPlantilla +"_9" + "." + getExtension(file9.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file9);
		}
		if(file10 != null) {
			String rename = idPlantilla +"_10" + "." + getExtension(file10.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file10);
		}
		
		if(file11 != null) {
			String rename = idPlantilla +"_11" + "." + getExtension(file11.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file11);
		}
		
		if(file12 != null) {
			String rename = idPlantilla +"_12" + "." + getExtension(file12.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file12);
		}
		
		if(file13 != null) {
			String rename = idPlantilla +"_13" + "." + getExtension(file13.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file13);
		}
		
		if(file14 != null) {
			String rename = idPlantilla +"_14" + "." + getExtension(file14.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file14);
		}
		
		if(file15 != null) {
			String rename = idPlantilla +"_15" + "." + getExtension(file15.getFileName());
			ImagenPlantillaVenta registroImagen = new ImagenPlantillaVenta();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-PLANTILLA-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setPlantillaVenta(plantilla);
			imagenPlantillaVentaService.save(registroImagen);
			
            subirArchivo(rename, file15);
		}
	}
	
	public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
	
	public void aprobarPlantilla() {
		List<DetalleComisiones> lstDetBusqueda = detalleComisionesService.findByEstadoAndPlantillaVenta(true, plantillaVentaSelected);
		if(!lstDetBusqueda.isEmpty()) {
			addErrorMessage("Ya se ejecutó una comision con el lote seleccionado, comunicate con el área de sistemas."); 
			return;
		}
		
		
		ConfiguracionComision confComision = configuracionComisionService.findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(plantillaVentaSelected.getFechaVenta(), plantillaVentaSelected.getFechaVenta());
		ComisionSupervisor comisionSupervisor = null;
		
		if(confComision==null) {
			addWarnMessage("Primero se tiene que realizar una configuracion de de comisiones para el mes de la venta, por favor comunicale al area de Administración o Sistemas.");
			return;
		}else {
			comisionSupervisor = comisionSupervisorService.findByEstadoAndConfiguracionComisionAndPersonaSupervisor(true, confComision, plantillaVentaSelected.getPersonSupervisor());
			if(comisionSupervisor==null) {
				addWarnMessage("Aún no se le ha asignado una meta al supervidor seleccionado, por favor comunicale al area de Administración o Sistemas.");
				return;
			}
		}
		
		plantillaVentaSelected.setEstado("Aprobado");
		plantillaVentaSelected.setUsuarioAprueba(navegacionBean.getUsuarioLogin());
		plantillaVentaSelected.setFechaAprueba(new Date());
		plantillaVentaService.saveAprobarPlantilla(plantillaVentaSelected, confComision, comisionSupervisor);
		
				
		addInfoMessage("Se aprobó la plantilla de venta correctamente."); 
		PrimeFaces.current().executeScript("PF('plantillaNewDialog').hide();"); 

	}
	
	
	
//	public void actualizarDatosComision(ConfiguracionComision configuracionComision) {
//		
//		List<ComisionSupervisor> lstComisionSupervisor = comisionSupervisorService.findByEstadoAndConfiguracionComision(true, configuracionComision);
//		
//		
//		for(ComisionSupervisor cs : lstComisionSupervisor) {
//			
//			Empleado empleado = empleadoService.findByPersonId(cs.getPersonaSupervisor().getId());
//			if(empleado.getCargo().getDescripcion().equals("JEFE DE VENTAS")) {
//				
//			}else {
//				
//			}
//			
//			//PARA SUPERVISOR
//			BigDecimal montoComisionSuper = BigDecimal.ZERO;
//			int numVendidoSuper = 0;
//			
//			
//			List<Comisiones> lstComisiones = comisionesService.findByEstadoAndComisionSupervisor(true, cs);
//			for(Comisiones c : lstComisiones) {
//				//PARA ASESOR
//				BigDecimal montoComisionAsesor = BigDecimal.ZERO;
//				int numVendidoAsesor = 0;
//				
//				
//				
//				List<DetalleComisiones> lstDetalleComisiones = detalleComisionesService.findByEstadoAndComisiones(true, c);
//				for(DetalleComisiones cd : lstDetalleComisiones) {
//					montoComisionSuper = montoComisionSuper.add(cd.getMontoComisionSupervisor());
//					numVendidoSuper++;
//					
//					montoComisionAsesor = montoComisionAsesor.add(cd.getMontoComision());
//					numVendidoAsesor++;
//				}
//				
//				c.setNumVendido(numVendidoAsesor);
//				c.setMontoComision(montoComisionAsesor);
//				comisionesService.save(c);
//				
//			} 
//			
//			
//			cs.setNumVendido(numVendidoSuper);
//			cs.setMontoComision(montoComisionSuper);
//			if(cs.getNumVendido()> cs.getMeta()) {
//				cs.setBono(configuracionComision.getBonoSupervisor());
//			}else {
//				cs.setBono(BigDecimal.ZERO);
//			}
//			comisionSupervisorService.save(cs);
//		}
//	}
	
	public void rechazarPlantilla() {
//		plantillaVentaSelected.setObservacion(observacion); 
		plantillaVentaSelected.setEstado("Rechazado");
		plantillaVentaSelected.setUsuarioRechaza(navegacionBean.getUsuarioLogin());
		plantillaVentaSelected.setFechaRechaza(new Date());
		plantillaVentaService.save(plantillaVentaSelected);
		addInfoMessage("Se rechazó la plantilla de venta correctamente."); 
		PrimeFaces.current().executeScript("PF('plantillaNewDialog').hide();"); 

	}
	
	public void validaVoucher() {
		valida = false;
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
	
	public void savePlantillaVenta() {
		List<PlantillaVenta> lstPlantillaPen = plantillaVentaService.findByEstadoAndLote("Pendiente", lotePlantilla);
		if(!lstPlantillaPen.isEmpty()) {
			addErrorMessage("Ya existe una plantilla PENDIENTE con el lote seleccionado");
			return;
		}
		
		List<PlantillaVenta> lstPlantillaAprob = plantillaVentaService.findByEstadoAndLote("Aprobado", lotePlantilla);
		if(!lstPlantillaAprob.isEmpty()) {
			addErrorMessage("Ya existe una plantilla APROBADA con el lote seleccionado");
			return;
		}
		
		PlantillaVenta plantillaVentaNew = new PlantillaVenta();
		plantillaVentaNew.setFechaVenta(fechaVenta); 
		plantillaVentaNew.setPerson(personCliente);
		if(team.getPersonSupervisor()!=null)plantillaVentaNew.setPersonSupervisor(team.getPersonSupervisor());
		plantillaVentaNew.setPersonAsesor(personAsesor);
		plantillaVentaNew.setLote(lotePlantilla);
		plantillaVentaNew.setTipoPago(tipoPagoPlantilla);
		plantillaVentaNew.setMontoVenta(montoPlantilla);
		if(!tipoPagoPlantilla.equals("CONTADO")) {
			plantillaVentaNew.setInteres(interesPlantilla);
			plantillaVentaNew.setNumeroCuota(numeroCuotaPlantilla);
			plantillaVentaNew.setMontoInicial(inicialPlantilla);
		}
		
		plantillaVentaNew.setEstado("Pendiente");
		plantillaVentaNew.setUsuario(navegacionBean.getUsuarioLogin());
		plantillaVentaNew.setFecha(new Date());
		plantillaVentaNew.setRequerimientoSeparacion(requerimientoBusqueda);
		PlantillaVenta plantilla = plantillaVentaService.save(plantillaVentaNew);
		
		if(plantilla == null) {
			addErrorMessage("No se pudo guardar.");
			return;
		}else {
			mensajeSeparacion = "";
			mensajeSeparacion2 = "";
			subirImagenes(plantilla.getId() + "", plantilla);
			addInfoMessage("Se guardo correctamente.");
			iniciarDatosPlantilla();
			
		}
		
		
		PrimeFaces.current().executeScript("PF('savePlantillaVenta').hide();"); 
		
	}

	public void validaDatosPlantilla() {
		if(fechaVenta == null) {
			addErrorMessage("Debes seleccionar una fecha de venta.");
			return;
		}else if(fechaVenta.after(new Date())) {
			addErrorMessage("La fecha es inválida.");
			return;
		}
		
		if(personCliente==null) {
			addErrorMessage("Debes seleccionar una persona.");
			return;
		}
		if(team==null) {
			addErrorMessage("Debes seleccionar un equipo.");
			return;
		}
		if(personAsesor==null) {
			addErrorMessage("Debes seleccionar un asesor.");
			return;
		}
		if(proyectoPlantilla ==null) {
			addErrorMessage("Debes seleccionar Proyecto.");
			return;
		}
		if(manzanaPlantilla ==null) {
			addErrorMessage("Debes seleccionar Manzana.");
			return;
		}
		if(lotePlantilla==null) {
			addErrorMessage("Debes seleccionar Lote.");
			return;
		}else {
			Lote lote = loteService.findById(lotePlantilla.getId());
			if(lote.getStatus().equals("Vendido")) {
				addErrorMessage("El lote se encuentra vendido.");
				return;
			}
		}
		if(montoPlantilla==null) {
			addErrorMessage("Debes ingresar monto de venta.");
			return;
		}
		if(tipoPagoPlantilla.equals("CRÉDITO")) {
			if(inicialPlantilla==null) {
				addErrorMessage("Debes ingresar monto inicial.");
				return;
			}
			
			if(numeroCuotaPlantilla.equals("")) {
				addErrorMessage("Debes ingresar el plazo a pagar.");
				return;
			}
			
			if(interesPlantilla==null) {
				addErrorMessage("Debes ingresar interes.");
				return;
			}
		}

		if(file1 == null && file2 == null && file3 == null && file4 == null && file5 == null && file6 == null && file7 == null && file8 == null && file9 == null && file10 == null && file11 == null && file12 == null && file13 == null && file14 == null && file15 == null) {
			addErrorMessage("Debes ingresar al menos una imagen.");
			return;
		}
		
		PrimeFaces.current().executeScript("PF('savePlantillaVenta').show();"); 
	}
	
	public void listarLotesPlantilla() {
		if(manzanaPlantilla != null) {
			lstLotePlantilla = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyectoPlantilla, manzanaPlantilla, "%%");
		}else {
			lstLotePlantilla = new ArrayList<>();
		}
	}
	
	public void listarManzanaPlantilla() {
		manzanaPlantilla=null;
		lotePlantilla=null;
		lstLotePlantilla = new ArrayList<>();
		if(proyectoPlantilla != null) {
			lstManzanaPlantilla = manzanaService.findByProject(proyectoPlantilla.getId());
		}else {
			lstManzanaPlantilla = new ArrayList<>();
		}
	}
	
	public void listarDatosTemporales() {
		lstVoucherTemporal = voucherTempService.findByPlantillaVentaAndEstado(plantillaVentaSelected, true);
	}
	
	public void verVoucher() {
		listarDatosTemporales(); 
		valida = false;
		cuentaBancariaSelected = null;
		monto = null;
		tipoTransaccion = "";
		numeroTransaccion = "";
		fechaOperacion = new Date();
		
		loadImagePlantillaBean.setNombreArchivo("0.png");
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
		imagen11 = "";
		imagen12 = "";
		imagen13 = "";
		imagen14 = "";
		imagen15 = "";
	
		observacion = plantillaVentaSelected.getObservacion();
		
		List<ImagenPlantillaVenta> lstImagenPlantilla = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantillaVentaSelected, true);
		int contador = 1;
		for(ImagenPlantillaVenta i:lstImagenPlantilla) {
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
			if(contador==11) {
				imagen11 = i.getNombre();
			}
			if(contador==12) {
				imagen12 = i.getNombre();
			}
			if(contador==13) {
				imagen13 = i.getNombre();
			}
			if(contador==14) {
				imagen14 = i.getNombre();
			}
			if(contador==15) {
				imagen15 = i.getNombre();
			}
			contador ++;
		}
//			PrimeFaces.current().executeScript("PF('voucherDocumentoDialog').show();");
	
	}
	
	public void iniciarPlantillaLazy() {
		lstPlantillaLazy = new LazyDataModel<PlantillaVenta>() {
			private List<PlantillaVenta> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public PlantillaVenta getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (PlantillaVenta plantillaVenta : datasource) {
                    if (plantillaVenta.getId() == intRowKey) {
                        return plantillaVenta;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(PlantillaVenta plantillaVenta) {
                return String.valueOf(plantillaVenta.getId());
            }

			@Override
			public List<PlantillaVenta> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				
				String prospect="%"+ (filterBy.get("prospecto.person.surnames")!=null?filterBy.get("prospecto.person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String project="%"+ (filterBy.get("lote.project.name")!=null?filterBy.get("lote.project.name").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String manzana="%"+ (filterBy.get("lote.manzana.name")!=null?filterBy.get("lote.manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String lote="%"+ (filterBy.get("lote.numberLote")!=null?filterBy.get("lote.numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				
				
				 Sort sort=Sort.by("id").descending();
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
				Page<PlantillaVenta> pagePlantillaVenta=null;
				
				if(navegacionBean.getUsuarioLogin().getProfile().getId().equals(Perfiles.ASESOR.getId())) {
					pagePlantillaVenta= plantillaVentaService.findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonAsesor(estadoPlantillaFilter, prospect, project, manzana, lote, navegacionBean.getSucursalLogin(), navegacionBean.getUsuarioLogin().getPerson(), pageable);
				}else if(navegacionBean.getUsuarioLogin().getProfile().getId().equals(Perfiles.SUPERVISOR.getId())) {
					pagePlantillaVenta= plantillaVentaService.findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndPersonSupervisor(estadoPlantillaFilter, prospect, project, manzana, lote, navegacionBean.getSucursalLogin(), navegacionBean.getUsuarioLogin().getPerson(), pageable);
				}else{
					pagePlantillaVenta= plantillaVentaService.findByEstadoAndPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(estadoPlantillaFilter, prospect, project, manzana, lote, navegacionBean.getSucursalLogin(), pageable);
	
				}
			
				
				setRowCount((int) pagePlantillaVenta.getTotalElements());
				return datasource = pagePlantillaVenta.getContent();
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

	public Converter getConversorManzanaPlantilla() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Manzana c = null;
                    for (Manzana si : lstManzanaPlantilla) {
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
	
	public Converter getConversorLotePlantilla() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Lote c = null;
                    for (Lote si : lstLotePlantilla) {
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
	
	public String getEstadoPlantillaFilter() {
		return estadoPlantillaFilter;
	}
	public void setEstadoPlantillaFilter(String estadoPlantillaFilter) {
		this.estadoPlantillaFilter = estadoPlantillaFilter;
	}
	public LazyDataModel<PlantillaVenta> getLstPlantillaLazy() {
		return lstPlantillaLazy;
	}
	public void setLstPlantillaLazy(LazyDataModel<PlantillaVenta> lstPlantillaLazy) {
		this.lstPlantillaLazy = lstPlantillaLazy;
	}
	public PlantillaVentaService getPlantillaVentaService() {
		return plantillaVentaService;
	}
	public void setPlantillaVentaService(PlantillaVentaService plantillaVentaService) {
		this.plantillaVentaService = plantillaVentaService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public PlantillaVenta getPlantillaVentaSelected() {
		return plantillaVentaSelected;
	}
	public void setPlantillaVentaSelected(PlantillaVenta plantillaVentaSelected) {
		this.plantillaVentaSelected = plantillaVentaSelected;
	}
	public LoadImagePlantillaBean getLoadImagePlantillaBean() {
		return loadImagePlantillaBean;
	}
	public void setLoadImagePlantillaBean(LoadImagePlantillaBean loadImagePlantillaBean) {
		this.loadImagePlantillaBean = loadImagePlantillaBean;
	}
	public ImagenPlantillaVentaService getImagenPlantillaVentaService() {
		return imagenPlantillaVentaService;
	}
	public void setImagenPlantillaVentaService(ImagenPlantillaVentaService imagenPlantillaVentaService) {
		this.imagenPlantillaVentaService = imagenPlantillaVentaService;
	}
	public CuentaBancaria getCuentaBancariaSelected() {
		return cuentaBancariaSelected;
	}
	public void setCuentaBancariaSelected(CuentaBancaria cuentaBancariaSelected) {
		this.cuentaBancariaSelected = cuentaBancariaSelected;
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
	public String getImagen11() {
		return imagen11;
	}
	public void setImagen11(String imagen11) {
		this.imagen11 = imagen11;
	}
	public String getImagen12() {
		return imagen12;
	}
	public void setImagen12(String imagen12) {
		this.imagen12 = imagen12;
	}
	public String getImagen13() {
		return imagen13;
	}
	public void setImagen13(String imagen13) {
		this.imagen13 = imagen13;
	}
	public String getImagen14() {
		return imagen14;
	}
	public void setImagen14(String imagen14) {
		this.imagen14 = imagen14;
	}
	public String getImagen15() {
		return imagen15;
	}
	public void setImagen15(String imagen15) {
		this.imagen15 = imagen15;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public Date getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}
	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}
	public Project getProyectoPlantilla() {
		return proyectoPlantilla;
	}
	public void setProyectoPlantilla(Project proyectoPlantilla) {
		this.proyectoPlantilla = proyectoPlantilla;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public List<Project> getLstProject() {
		return lstProject;
	}
	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}
	public Manzana getManzanaPlantilla() {
		return manzanaPlantilla;
	}
	public void setManzanaPlantilla(Manzana manzanaPlantilla) {
		this.manzanaPlantilla = manzanaPlantilla;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public List<Manzana> getLstManzanaPlantilla() {
		return lstManzanaPlantilla;
	}
	public void setLstManzanaPlantilla(List<Manzana> lstManzanaPlantilla) {
		this.lstManzanaPlantilla = lstManzanaPlantilla;
	}
	public List<Lote> getLstLotePlantilla() {
		return lstLotePlantilla;
	}
	public void setLstLotePlantilla(List<Lote> lstLotePlantilla) {
		this.lstLotePlantilla = lstLotePlantilla;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
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
	public UploadedFile getFile11() {
		return file11;
	}
	public void setFile11(UploadedFile file11) {
		this.file11 = file11;
	}
	public UploadedFile getFile12() {
		return file12;
	}
	public void setFile12(UploadedFile file12) {
		this.file12 = file12;
	}
	public UploadedFile getFile13() {
		return file13;
	}
	public void setFile13(UploadedFile file13) {
		this.file13 = file13;
	}
	public UploadedFile getFile14() {
		return file14;
	}
	public void setFile14(UploadedFile file14) {
		this.file14 = file14;
	}
	public UploadedFile getFile15() {
		return file15;
	}
	public void setFile15(UploadedFile file15) {
		this.file15 = file15;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
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
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
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
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public Person getPersonCliente() {
		return personCliente;
	}
	public void setPersonCliente(Person personCliente) {
		this.personCliente = personCliente;
	}
	public Person getPersonAsesor() {
		return personAsesor;
	}
	public void setPersonAsesor(Person personAsesor) {
		this.personAsesor = personAsesor;
	}
	public Lote getLotePlantilla() {
		return lotePlantilla;
	}
	public void setLotePlantilla(Lote lotePlantilla) {
		this.lotePlantilla = lotePlantilla;
	}
	public BigDecimal getMontoPlantilla() {
		return montoPlantilla;
	}
	public void setMontoPlantilla(BigDecimal montoPlantilla) {
		this.montoPlantilla = montoPlantilla;
	}
	public BigDecimal getInteresPlantilla() {
		return interesPlantilla;
	}
	public void setInteresPlantilla(BigDecimal interesPlantilla) {
		this.interesPlantilla = interesPlantilla;
	}
	public String getTipoPagoPlantilla() {
		return tipoPagoPlantilla;
	}
	public void setTipoPagoPlantilla(String tipoPagoPlantilla) {
		this.tipoPagoPlantilla = tipoPagoPlantilla;
	}
	public Integer getNumeroCuotaPlantilla() {
		return numeroCuotaPlantilla;
	}
	public void setNumeroCuotaPlantilla(Integer numeroCuotaPlantilla) {
		this.numeroCuotaPlantilla = numeroCuotaPlantilla;
	}
	public BigDecimal getInicialPlantilla() {
		return inicialPlantilla;
	}
	public void setInicialPlantilla(BigDecimal inicialPlantilla) {
		this.inicialPlantilla = inicialPlantilla;
	}
	public String getMensajeSeparacion() {
		return mensajeSeparacion;
	}
	public void setMensajeSeparacion(String mensajeSeparacion) {
		this.mensajeSeparacion = mensajeSeparacion;
	}
	public RequerimientoSeparacionService getRequerimientoSeparacionService() {
		return requerimientoSeparacionService;
	}
	public void setRequerimientoSeparacionService(RequerimientoSeparacionService requerimientoSeparacionService) {
		this.requerimientoSeparacionService = requerimientoSeparacionService;
	}
	public RequerimientoSeparacion getRequerimientoBusqueda() {
		return requerimientoBusqueda;
	}
	public void setRequerimientoBusqueda(RequerimientoSeparacion requerimientoBusqueda) {
		this.requerimientoBusqueda = requerimientoBusqueda;
	}
	public boolean isValida() {
		return valida;
	}
	public void setValida(boolean valida) {
		this.valida = valida;
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
	public DocumentoVentaService getDocumentoVentaService() {
		return documentoVentaService;
	}
	public void setDocumentoVentaService(DocumentoVentaService documentoVentaService) {
		this.documentoVentaService = documentoVentaService;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Date getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	public ContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(ContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public ComisionSupervisorService getComisionSupervisorService() {
		return comisionSupervisorService;
	}
	public void setComisionSupervisorService(ComisionSupervisorService comisionSupervisorService) {
		this.comisionSupervisorService = comisionSupervisorService;
	}
	public ConfiguracionComisionService getConfiguracionComisionService() {
		return configuracionComisionService;
	}
	public void setConfiguracionComisionService(ConfiguracionComisionService configuracionComisionService) {
		this.configuracionComisionService = configuracionComisionService;
	}
	public ComisionesService getComisionesService() {
		return comisionesService;
	}
	public void setComisionesService(ComisionesService comisionesService) {
		this.comisionesService = comisionesService;
	}
	public DetalleComisionesService getDetalleComisionesService() {
		return detalleComisionesService;
	}
	public void setDetalleComisionesService(DetalleComisionesService detalleComisionesService) {
		this.detalleComisionesService = detalleComisionesService;
	}
	public String getMensajeSeparacion2() {
		return mensajeSeparacion2;
	}
	public void setMensajeSeparacion2(String mensajeSeparacion2) {
		this.mensajeSeparacion2 = mensajeSeparacion2;
	}

	
	
}
