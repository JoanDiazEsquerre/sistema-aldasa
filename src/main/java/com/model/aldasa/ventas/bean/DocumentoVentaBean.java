package com.model.aldasa.ventas.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
//import org.json.JSONObject;
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

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Identificador;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.MotivoNota;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.TipoDocumento;
import com.model.aldasa.entity.TipoOperacion;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.VoucherTemp;
import com.model.aldasa.fe.ConsumingPostBoImpl;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.ClienteService;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.CuotaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.IdentificadorService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.MotivoNotaService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProductoService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.SerieDocumentoService;
import com.model.aldasa.service.TipoDocumentoService;
import com.model.aldasa.service.TipoOperacionService;
import com.model.aldasa.service.VoucherTempService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoContrato;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.TipoProductoType;
import com.model.aldasa.ventas.jrdatasource.DataSourceDocumentoVenta;




@ManagedBean
@ViewScoped
public class DocumentoVentaBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{serieDocumentoService}")
	private SerieDocumentoService serieDocumentoService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{cuotaService}")
	private CuotaService cuotaService;
	
	@ManagedProperty(value = "#{requerimientoSeparacionService}")
	private RequerimientoSeparacionService requerimientoSeparacionService;
	
	@ManagedProperty(value = "#{productoService}")
	private ProductoService productoService;
	
	@ManagedProperty(value = "#{clienteService}")
	private ClienteService clienteService;
	
	@ManagedProperty(value = "#{reportGenBo}")
	private ReportGenBo reportGenBo;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	
	@ManagedProperty(value = "#{contratoService}")
	private ContratoService contratoService;
	
	@ManagedProperty(value = "#{tipoDocumentoService}")
	private TipoDocumentoService tipoDocumentoService;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	@ManagedProperty(value = "#{loadImageDocumentoBean}")
	private LoadImageDocumentoBean loadImageDocumentoBean;
	
	@ManagedProperty(value = "#{motivoNotaService}")
	private MotivoNotaService motivoNotaService;
	
	@ManagedProperty(value = "#{tipoOperacionService}")
	private TipoOperacionService tipoOperacionService;
	
	@ManagedProperty(value = "#{identificadorService}")
	private IdentificadorService identificadorService;
	
	@ManagedProperty(value = "#{consumingPostBo}")
	private ConsumingPostBoImpl consumingPostBo;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;	
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;	
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{voucherTempService}")
	private VoucherTempService voucherTempService;
	
	private boolean estado = true;
	private Boolean estadoSunat;

	private LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy;
	private LazyDataModel<Cuota> lstCuotaLazy;
	private LazyDataModel<RequerimientoSeparacion> lstRequerimientoLazy;
	private LazyDataModel<Cuota> lstPrepagoLazy;
	private LazyDataModel<Contrato> lstContratosPendientesLazy;

	
	private List<SerieDocumento> lstSerieDocumento;
	private List<SerieDocumento> lstSerieNotaDocumento;
	private List<Cuota> lstCuota;    
//	private List<Voucher> lstVoucher;
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVenta = new ArrayList<>();
	private List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected = new ArrayList<>(); 
	private List<DocumentoVenta> lstDocumentoVenta = new ArrayList<>();
	private List<Cuota> lstCuotaPagadas = new ArrayList<>();
	private List<Cuota> lstCuotaVista = new ArrayList<>();
	private List<Cuota> lstCuotaPendientes = new ArrayList<>();
	private List<Cuota> lstCuotaPendientesTemporal = new ArrayList<>();
	private List<TipoDocumento> lstTipoDocumento = new ArrayList<>();
	private List<TipoDocumento> lstTipoDocumentoNota = new ArrayList<>();
	private List<TipoDocumento> lstTipoDocumentoEnvioSunat = new ArrayList<>();
	private List<MotivoNota> lstMotivoNota = new ArrayList<>();
	private List<TipoOperacion> lstTipoOperacion = new ArrayList<>();
	private List<Identificador> lstIdentificador = new ArrayList<>();
	private List<Person> lstPerson;
	private List<Cliente> lstCliente;
	private List<Project> lstProject;
	private List<Producto> lstProducto;
	private List<CuentaBancaria> lstCuentaBancaria = new ArrayList<>();
	
	private DocumentoVenta documentoVentaSelected ;
	private SerieDocumento serieDocumentoSelected ;
	private SerieDocumento serieNotaDocumentoSelected ;
	private Cuota cuotaSelected ;
	private RequerimientoSeparacion requerimientoSelected ;
	private Cuota prepagoSelected ;
	private Cliente clienteSelected;
	private Contrato contratoPendienteSelected;
	private Cuota cuotaPendienteContratoSelected;
	private TipoDocumento tipoDocumentoSelected, tipoDocumentoEnvioSunat, tipoDocumentoFilter;
	private TipoDocumento tipoDocumentoNotaSelected;
	private MotivoNota motivoNotaSelected;
	private TipoOperacion tipoOperacionSelected;
	private Identificador identificadorSelected;
	private Person personSelected;
	private Project projectFilter;
	private Imagen imagenSelected;


	private DocumentoVenta documentoVentaNew;
	private DetalleDocumentoVenta detalleDocumentoVentaSelected;
	private DetalleDocumentoVenta detalleDocumentoVenta;
	private Producto productoCuota,productoAdelanto,productoInteres, productoVoucher,productoAmortizacion, productoInicial;
//	private Person persona;
	private Usuario usuarioLogin = new Usuario();
	private CuentaBancaria ctaBanc1, ctaBanc2, ctaBanc3, ctaBanc4, ctaBanc5, ctaBanc6, ctaBanc7, ctaBanc8, ctaBanc9, ctaBanc10, ctaBanc11, ctaBanc12, ctaBanc13, ctaBanc14, ctaBanc15, cuentaVoucherDialog;
	
	private Date fechaEmision = new Date() ;
	private Date fechaEmisionNotaVenta = new Date() ;
	private Date fechaImag1, fechaImag2, fechaImag3, fechaImag4, fechaImag5, fechaImag6, fechaImag7, fechaImag8, fechaImag9, fechaImag10, fechaImag11, fechaImag12, fechaImag13, fechaImag14, fechaImag15, fechaEnvioSunat ;
	private BigDecimal montoImag1, montoImag2, montoImag3, montoImag4, montoImag5, montoImag6, montoImag7, montoImag8, montoImag9, montoImag10, montoImag11, montoImag12, montoImag13, montoImag14, montoImag15;
	private String nroOperImag1, nroOperImag2, nroOperImag3, nroOperImag4, nroOperImag5, nroOperImag6, nroOperImag7, nroOperImag8, nroOperImag9, nroOperImag10, nroOperImag11, nroOperImag12, nroOperImag13, nroOperImag14, nroOperImag15;
	private String fechaTextoVista, montoLetra;
	private String observacion, numero, numeroNota, razonSocialCliente, nombreComercialCliente,rucDniCliente, direccionCliente, email1Cliente, email2Cliente, email3Cliente  ; 
	private String tipoPago = "Contado";
	private String tipoPrepago ="PC";
	private String incluirUltimaCuota = "No";
	private Integer nuevoNroCuotas, numMuestraImagen;
	private Date fechaVoucherDialog;
	private BigDecimal montoVoucherDialog, sumaMontoVoucher;
	private String nroOperacionVoucherDialog, tipoTransaccionDialog, ctaBancDialog;
	private String motivo="";
	private String motivoSunat="";
	private String razonSocialText, direccionText, email1Text, email2Text, email3Text;

	private boolean pagoTotalPrepago = false;
	private boolean habilitarBoton = true;
	private boolean habilitarMontoPrepago = false;
	private boolean personaNaturalCliente = true;
	private boolean incluirIgv = false;


	private String moneda = "S";
	private BigDecimal anticipos = BigDecimal.ZERO;
	private BigDecimal opGravada = BigDecimal.ZERO;
	private BigDecimal opExonerada = BigDecimal.ZERO;
	private BigDecimal opInafecta = BigDecimal.ZERO;
	private BigDecimal opGratuita = BigDecimal.ZERO;
	private BigDecimal descuentos = BigDecimal.ZERO;
	private BigDecimal ISC = BigDecimal.ZERO;
	private BigDecimal IGV = BigDecimal.ZERO;
	private BigDecimal otrosCargos = BigDecimal.ZERO;
	private BigDecimal otrosTributos = BigDecimal.ZERO;
	private BigDecimal importeTotal = BigDecimal.ZERO;
	
	private BigDecimal deudaActualSinInteres = BigDecimal.ZERO;
	private BigDecimal deudaActualConInteres = BigDecimal.ZERO;
	private BigDecimal montoPrepago = BigDecimal.ZERO;
	private BigDecimal nuevoInteres;
	private BigDecimal sumaCuotaSI, sumaInteres, sumaCuotaTotal;
	
	private String imagen1, imagen2, imagen3, imagen4, imagen5, imagen6, imagen7, imagen8, imagen9, imagen10, imagen11, imagen12, imagen13, imagen14, imagen15;
	private String tipoTransaccion1, tipoTransaccion2, tipoTransaccion3, tipoTransaccion4, tipoTransaccion5, tipoTransaccion6, tipoTransaccion7, tipoTransaccion8, tipoTransaccion9, tipoTransaccion10, tipoTransaccion11, tipoTransaccion12, tipoTransaccion13, tipoTransaccion14, tipoTransaccion15;

	private NumeroALetra numeroALetra = new  NumeroALetra();
	
	private Map<String, Object> parametros;
    private DataSourceDocumentoVenta dt; 
    
    private UploadedFile file1, file2, file3, file4, file5, file6, file7, file8, file9, file10, file11, file12, file13, file14, file15;


	SimpleDateFormat sdf = new SimpleDateFormat("dd 'de'  MMMMM 'del' yyyy");
	SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat sdfFullQuery = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private SelectItem[] cdoTipoVenta;
	
	@PostConstruct
	public void init() {
		List<String> lstCodigoFB=new ArrayList<>();
		lstCodigoFB.add("01");
		lstCodigoFB.add("03");
		lstTipoDocumento = tipoDocumentoService.findByEstadoAndCodigoIn(true, lstCodigoFB);
		tipoDocumentoSelected = lstTipoDocumento.get(0);
		
		List<String> lstCodigoCD=new ArrayList<>();
		lstCodigoCD.add("07");
		lstCodigoCD.add("08");
		lstTipoDocumentoNota = tipoDocumentoService.findByEstadoAndCodigoIn(true, lstCodigoCD);
		tipoDocumentoNotaSelected = lstTipoDocumentoNota.get(0);
		
		List<String> lstCodigoSunat=new ArrayList<>();
		lstCodigoSunat.add("01");
		lstCodigoSunat.add("03"); 
		lstCodigoSunat.add("07");
		lstCodigoSunat.add("08");
		lstTipoDocumentoEnvioSunat = tipoDocumentoService.findByEstadoAndCodigoIn(true, lstCodigoSunat);
		tipoDocumentoEnvioSunat = lstTipoDocumentoEnvioSunat.get(0);
		tipoDocumentoFilter = null;
		crearFiltroTipoVenta();
		
		iniciarLazy();
		iniciarDatosDocVenta();	
		listarSerie();
		iniciarLazyCuota(); 
		iniciarLazyRequerimiento();
		iniciarLazyPrepago();
		iniciarLazyContratosPendientes();
		productoCuota = productoService.findByEstadoAndTipoProducto(true,TipoProductoType.CUOTA.getTipo());
		productoInteres = productoService.findByEstadoAndTipoProducto(true,TipoProductoType.INTERES.getTipo());
		productoVoucher = productoService.findByEstadoAndTipoProducto(true, TipoProductoType.SEPARACION.getTipo());
		productoAmortizacion = productoService.findByEstadoAndTipoProducto(true, TipoProductoType.AMORTIZACION.getTipo());
		productoAdelanto = productoService.findByEstadoAndTipoProducto(true, TipoProductoType.ADELANTO.getTipo());
		productoInicial = productoService.findByEstadoAndTipoProducto(true, TipoProductoType.INICIAL.getTipo());
		
		lstTipoOperacion = tipoOperacionService.findByEstado(true);
		lstIdentificador = identificadorService.findByEstado(true); 
		numMuestraImagen=1;
		fechaEnvioSunat= new Date();
		lstPerson=personService.findByStatus(true);
		lstProject= projectService.findByStatusAndSucursalOrderByNameAsc(true, navegacionBean.getSucursalLogin());
		lstProducto = productoService.findByEstado(true);
		lstCuentaBancaria=cuentaBancariaService.findByEstado(true);
	}
	
	public void seleccionarPersona() {
		if(personaNaturalCliente) {
			onChangePerson();
		}
	}
	
	
	public void buscar() {
		if(!personaNaturalCliente) {
			if(!rucDniCliente.equals("") ) {
				HttpClient httpClient = HttpClientBuilder.create().build();
		        String apiUrl = "https://api.apis.net.pe/v1/ruc?numero="+rucDniCliente.trim();

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
		                org.json.JSONObject json = new org.json.JSONObject(responseBody);

		                // Acceder a los campos del JSON
		                razonSocialCliente = json.getString("nombre");
		                nombreComercialCliente = json.getString("nombre");
		                direccionCliente = json.getString("direccion");
//		                String apellidoMaterno = json.getString("apellidoMaterno");
		                
		                
		            } else {
		                System.out.println("Error al obtener la respuesta. Código de estado: " + statusCode);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		}
	}
	
	public String convertirHora(Date hora) {
		String a = "";
		if(hora != null) {
			a = sdf2.format(hora);
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

	
	public void saveVoucherSelected() {
		if(imagenSelected == null) {
			addErrorMessage("Primero selecciona una imagen.");
			return;
		}
		
		if(fechaVoucherDialog==null) {
			addErrorMessage("Seleccionar una fecha.");
			return;
		}
		
		if(montoVoucherDialog == null) {
			addErrorMessage("Ingresar un monto.");
			return;		
		}else if (montoVoucherDialog.compareTo(BigDecimal.ZERO) <=0) {
			addErrorMessage("Ingresar un monto mayor a 0.");
			return;	
		}
		
		if(nroOperacionVoucherDialog.equals("")) {
			addErrorMessage("Ingresar número de operación.");
			return;	
		}
		
		if(cuentaVoucherDialog==null) {
			addErrorMessage("seleccionar una cuenta bancaria.");
			return;	
		}
		
		List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(true, sdfFullQuery.format(fechaVoucherDialog), montoVoucherDialog, nroOperacionVoucherDialog, cuentaVoucherDialog.getId(),imagenSelected.getId());
		if(!buscarImagen.isEmpty()) {
			addErrorMessage("Ya existe el voucher.");
		}else {
			imagenSelected.setFecha(fechaVoucherDialog);
			imagenSelected.setMonto(montoVoucherDialog);
			imagenSelected.setNumeroOperacion(nroOperacionVoucherDialog);
			imagenSelected.setCuentaBancaria(cuentaVoucherDialog);
			imagenSelected.setTipoTransaccion(tipoTransaccionDialog);
			imagenSelected.setPorRegularizar(cuentaVoucherDialog.getSucursal().getId().equals(documentoVentaSelected.getSucursal().getId())?"NO":"SI");
			imagenService.save(imagenSelected);
			addInfoMessage("Se guardó correctamente el voucher.");
		}
	}
	
	public void cambioIgv() {
		if(importeTotal.compareTo(BigDecimal.ZERO)>0) {
			if(incluirIgv) {
				BigDecimal opGrav = importeTotal.divide(new BigDecimal(1.18), 2, RoundingMode.HALF_UP);
				BigDecimal igv = opGrav.multiply(new BigDecimal(0.18));
				igv = igv.setScale(2, RoundingMode.HALF_UP);
				
				opInafecta = BigDecimal.ZERO;
				opGravada = opGrav;
				IGV = igv;
				
				for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
					BigDecimal sinIGV = d.getImporteVenta().divide(new BigDecimal(1.18), 2, RoundingMode.HALF_UP);
					
					d.setImporteVentaSinIgv(sinIGV); 
					d.setPrecioSinIgv(sinIGV);
				}
				
			}else {
				opInafecta = importeTotal;
				opGravada = BigDecimal.ZERO;
				IGV = BigDecimal.ZERO;
				
				for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
					d.setImporteVentaSinIgv(BigDecimal.ZERO); 
					d.setPrecioSinIgv(BigDecimal.ZERO);
				}
			}
		}
	}
	
	private void crearFiltroTipoVenta() {
        cdoTipoVenta = new SelectItem[4];
        cdoTipoVenta[0] = new SelectItem("", "Todos");
        cdoTipoVenta[1] = new SelectItem("F", "Factura");
        cdoTipoVenta[2] = new SelectItem("S", "Servicio");
        cdoTipoVenta[3] = new SelectItem("M", "Muestra");
    }
	
	public void agregarDetalle() {
		DetalleDocumentoVenta det = new DetalleDocumentoVenta();
		det.setProducto(lstProducto.get(0));
		det.setAmortizacion(BigDecimal.ZERO);
		det.setInteres(BigDecimal.ZERO);
		det.setAdelanto(BigDecimal.ZERO);
		det.setImporteVenta(BigDecimal.ZERO);
		det.setEstado(true); 
		det.setImporteVentaSinIgv(BigDecimal.ZERO);
		det.setPrecioSinIgv(BigDecimal.ZERO); 
		lstDetalleDocumentoVenta.add(det);
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
		if(personaNaturalCliente) {
			busqueda = clienteService.findByPersonAndEstadoAndPersonaNatural(personSelected, true, personaNaturalCliente);
			if(busqueda!=null) {
				addErrorMessage("Ya existe el cliente."); 
				return;
			}
		}else {
			busqueda = clienteService.findByRucAndEstado(rucDniCliente, true);
			if(busqueda!=null) {
				addErrorMessage("Ya existe un cliente con RUC "+ rucDniCliente); 
				return;
			}
		}
		
		Cliente nuevoCliente = new Cliente();
		nuevoCliente.setPerson(personSelected);
		nuevoCliente.setRazonSocial(razonSocialCliente);
		nuevoCliente.setNombreComercial(nombreComercialCliente);
		if(personaNaturalCliente) {
			nuevoCliente.setDni(rucDniCliente);
			nuevoCliente.setRuc("");
		}else {
			nuevoCliente.setRuc(rucDniCliente);
			nuevoCliente.setDni("");
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
		
		listarClientes();
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
	
	public void enviarDocumentoSunatMasivo() {
		if(fechaEnvioSunat ==  null) {
			addErrorMessage("Seleccione una fecha.");
			return;
		}
		
		List<DocumentoVenta> lstDoc = documentoVentaService.findByEstadoAndSucursalAndFechaEmisionAndEnvioSunat(true, navegacionBean.getSucursalLogin(), fechaEnvioSunat, false);
		if(!lstDoc.isEmpty()) {
			int cont = 0;
			for(DocumentoVenta d:lstDoc) {
				List<DetalleDocumentoVenta> lstDetalle = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(d, true);
				cont = cont+enviarDocumentoSunat(d, lstDetalle); 
//				consumingPostBo.apiConsume(d, lstDetalle);
				
//				d.setEnvioSunat(true);
//				documentoVentaService.save(d);
				
			}
			addInfoMessage("Se enviaron correctamente "+ cont + " documentos a SUNAT.");
			
		}else {
			addErrorMessage("No se encontraron documentos pendientes de envio para el dia "+ sdf.format(fechaEnvioSunat));
		}
		
	}
	
	public void enviarDocumentoSunat() {
		//ENVIAMOS FACTURA A NUBEFACT
        //JSONObject json_rspta = null;
        JSONObject json_rspta = consumingPostBo.apiConsume(documentoVentaSelected, lstDetalleDocumentoVentaSelected);
        System.out.println("Error => " + json_rspta);
        if (json_rspta != null) {
            if (json_rspta.get("errors") != null) {
                System.out.println("Error => " + json_rspta.get("errors"));
                addErrorMessage("Error al Enviar el comprobante electrónico: " + json_rspta.get("errors"));
            } else {
            	documentoVentaSelected.setEnvioSunat(true);
                /*JSONParser parsearRsptaDetalleOK = new JSONParser();
                JSONObject json_rspta_ok = (JSONObject) parsearRsptaDetalleOK.parse(json_rspta.get("invoice").toString());*/

                if (json_rspta.get("aceptada_por_sunat") != null) {
                    documentoVentaSelected.setEnvioAceptadaPorSunat(json_rspta.get("aceptada_por_sunat").toString());
                }

                if (json_rspta.get("sunat_description") != null) {
                	documentoVentaSelected.setEnvioSunatDescription(json_rspta.get("sunat_description").toString());
                }

                if (json_rspta.get("sunat_note") != null) {
                	documentoVentaSelected.setEnvioSunatNote(json_rspta.get("sunat_note").toString());
                }

                if (json_rspta.get("sunat_responsecode") != null) {
                	documentoVentaSelected.setEnvioSunatResponseCode(json_rspta.get("sunat_responsecode").toString());
                }

                if (json_rspta.get("sunat_soap_error") != null) {
                	documentoVentaSelected.setEnvioSunatSoapError(json_rspta.get("sunat_soap_error").toString());
                }

                if (json_rspta.get("enlace_del_pdf") != null) {
                	documentoVentaSelected.setEnvioEnlaceDelPdf(json_rspta.get("enlace_del_pdf").toString());
                }

                if (json_rspta.get("enlace_del_xml") != null) {
                	documentoVentaSelected.setEnvioEnlaceDelXml(json_rspta.get("enlace_del_xml").toString());
                }

                if (json_rspta.get("enlace_del_cdr") != null) {
                	documentoVentaSelected.setEnvioEnlaceDelCdr(json_rspta.get("enlace_del_cdr").toString());
                }

                if (json_rspta.get("cadena_para_codigo_qr") != null) {
                	documentoVentaSelected.setEnvioCadenaCodigoQr(json_rspta.get("cadena_para_codigo_qr").toString());
                }

                if (json_rspta.get("codigo_hash") != null) {
                	documentoVentaSelected.setEnvioCodigoHash(json_rspta.get("codigo_hash").toString());
                }

                documentoVentaService.save(documentoVentaSelected);
                addInfoMessage("Documento Electrónico enviado a Sunat Correctamente...");
            }
        }
		
	}
	
	public int enviarDocumentoSunat(DocumentoVenta docVenta, List<DetalleDocumentoVenta> lstDetalle) {
		int num = 0;
		//ENVIAMOS FACTURA A NUBEFACT
        //JSONObject json_rspta = null;
        JSONObject json_rspta = consumingPostBo.apiConsume(docVenta, lstDetalle);
        System.out.println("Error => " + json_rspta);
        if (json_rspta != null) {
            if (json_rspta.get("errors") != null) {
                System.out.println("Error => " + json_rspta.get("errors"));
                addErrorMessage("Error al Enviar el comprobante electrónico: " + json_rspta.get("errors"));
            } else {
            	num++;
            	docVenta.setEnvioSunat(true);
                /*JSONParser parsearRsptaDetalleOK = new JSONParser();
                JSONObject json_rspta_ok = (JSONObject) parsearRsptaDetalleOK.parse(json_rspta.get("invoice").toString());*/

                if (json_rspta.get("aceptada_por_sunat") != null) {
                	docVenta.setEnvioAceptadaPorSunat(json_rspta.get("aceptada_por_sunat").toString());
                }

                if (json_rspta.get("sunat_description") != null) {
                	docVenta.setEnvioSunatDescription(json_rspta.get("sunat_description").toString());
                }

                if (json_rspta.get("sunat_note") != null) {
                	docVenta.setEnvioSunatNote(json_rspta.get("sunat_note").toString());
                }

                if (json_rspta.get("sunat_responsecode") != null) {
                	docVenta.setEnvioSunatResponseCode(json_rspta.get("sunat_responsecode").toString());
                }

                if (json_rspta.get("sunat_soap_error") != null) {
                	docVenta.setEnvioSunatSoapError(json_rspta.get("sunat_soap_error").toString());
                }

                if (json_rspta.get("enlace_del_pdf") != null) {
                	docVenta.setEnvioEnlaceDelPdf(json_rspta.get("enlace_del_pdf").toString());
                }

                if (json_rspta.get("enlace_del_xml") != null) {
                	docVenta.setEnvioEnlaceDelXml(json_rspta.get("enlace_del_xml").toString());
                }

                if (json_rspta.get("enlace_del_cdr") != null) {
                	docVenta.setEnvioEnlaceDelCdr(json_rspta.get("enlace_del_cdr").toString());
                }

                if (json_rspta.get("cadena_para_codigo_qr") != null) {
                	docVenta.setEnvioCadenaCodigoQr(json_rspta.get("cadena_para_codigo_qr").toString());
                }

                if (json_rspta.get("codigo_hash") != null) {
                	docVenta.setEnvioCodigoHash(json_rspta.get("codigo_hash").toString());
                }

                documentoVentaService.save(docVenta);
                addInfoMessage("Documento Electrónico enviado a Sunat Correctamente...");
            }
        }
        return num;
		
	}
	
	
	public void menorarImagen() {
		if(numMuestraImagen !=1) {
			numMuestraImagen--;
		}
	}
	
	public void aumentarImagen() {
		if(numMuestraImagen !=15) {
			numMuestraImagen++;
		}
	}
	
	public void saveNota() {
		List<DocumentoVenta> lstBuscarNotaExistente = documentoVentaService.findByDocumentoVentaRefAndTipoDocumentoAndEstado(documentoVentaSelected, tipoDocumentoNotaSelected, true);
		
		if(!lstBuscarNotaExistente.isEmpty()) {
			addErrorMessage( "Ya se registró una " + tipoDocumentoNotaSelected.getDescripcion() + " para la " + documentoVentaSelected.getTipoDocumento().getDescripcion());
			return; 
		}
		
		DocumentoVenta doc = new DocumentoVenta();
		doc.setCliente(documentoVentaSelected.getCliente());
		doc.setDocumentoVentaRef(documentoVentaSelected);
		doc.setSucursal(documentoVentaSelected.getSucursal());
		doc.setTipoDocumento(tipoDocumentoNotaSelected);
		doc.setSerie(serieNotaDocumentoSelected.getSerie());
		doc.setNumero(numeroNota);
		doc.setRuc(documentoVentaSelected.getRuc());
		doc.setRazonSocial(documentoVentaSelected.getRazonSocial());
		doc.setNombreComercial(documentoVentaSelected.getNombreComercial());
		doc.setDireccion(documentoVentaSelected.getDireccion());
		doc.setFechaEmision(fechaEmisionNotaVenta);
		doc.setFechaVencimiento(fechaEmisionNotaVenta);
		doc.setTipoMoneda(documentoVentaSelected.getTipoMoneda());
		doc.setObservacion("");
		doc.setTipoPago(documentoVentaSelected.getTipoPago());
		doc.setSubTotal(documentoVentaSelected.getSubTotal());
		doc.setIgv(documentoVentaSelected.getIgv());
		doc.setTotal(documentoVentaSelected.getTotal());
		doc.setFechaRegistro(new Date());
		doc.setUsuarioRegistro(documentoVentaSelected.getUsuarioRegistro());
		doc.setEstado(true);
		doc.setAnticipos(documentoVentaSelected.getAnticipos());
		doc.setOpGravada(documentoVentaSelected.getOpGravada());
		doc.setOpExonerada(documentoVentaSelected.getOpExonerada());
		doc.setOpInafecta(documentoVentaSelected.getOpInafecta());
		doc.setOpGratuita(documentoVentaSelected.getOpGratuita());
		doc.setDescuentos(documentoVentaSelected.getDescuentos());
		doc.setIsc(documentoVentaSelected.getIsc());
		doc.setOtrosCargos(documentoVentaSelected.getOtrosCargos());
		doc.setOtrosTributos(documentoVentaSelected.getOtrosTributos());
		doc.setMotivoNota(motivoNotaSelected);
		doc.setTipoOperacion(tipoOperacionSelected);
		doc.setIdentificador(identificadorSelected);
		
		DocumentoVenta saveDocNota = documentoVentaService.saveNota(doc);
		if(saveDocNota!=null) {
			SerieDocumento serie = serieDocumentoService.findById(serieNotaDocumentoSelected.getId()).get();
			String numeroActual = String.format("%0" + serie.getTamanioNumero() + "d", Integer.valueOf(serie.getNumero()));

			Integer aumento = Integer.parseInt(serie.getNumero())+1;

			serie.setNumero(aumento+"");
			serieDocumentoService.save(serie);

			saveDocNota.setNumero(numeroActual); 
			documentoVentaService.save(saveDocNota);
			
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVentaSelected) {
				d.setId(null);
				d.setDocumentoVenta(saveDocNota);
				d.setEstado(true);
				detalleDocumentoVentaService.save(d);	
			}
			
//			aqui actualizamos los campos del documento de origen
			
			if(tipoDocumentoNotaSelected.getAbreviatura().equals("C")) {
				documentoVentaSelected.setNotacredito(true);
				documentoVentaSelected.setNumeroNotaCredito(saveDocNota.getSerie() + "-" + saveDocNota.getNumero());
			}else {
				documentoVentaSelected.setNotaDebito(true);
				documentoVentaSelected.setNumeroNotaDebito(saveDocNota.getSerie() + "-" + saveDocNota.getNumero());
			}
			documentoVentaService.save(documentoVentaSelected);
			
			// aqui anulamos las imagenes
			String nombreBusqueda = "%"+documentoVentaSelected.getId() +"_%";
			List<Imagen> lstImagen = imagenService.findByNombreLikeAndEstado(nombreBusqueda, true);
			for(Imagen i:lstImagen) {
				i.setEstado(false);
				imagenService.save(i);
			}
			
			addInfoMessage("Se guardó el documento correctamente.");
			PrimeFaces.current().executeScript("PF('notaCreditoDebitoDialog').hide();");
		}else {
			addErrorMessage("No se pudo guardar la nota.");
		}
	}
	
	public void obtenerDatosVoucher(String nombre) {
		imagenSelected = imagenService.findByNombre(nombre); 
		if(imagenSelected!=null) {
			fechaVoucherDialog=imagenSelected.getFecha();
			montoVoucherDialog = imagenSelected.getMonto();
			nroOperacionVoucherDialog = imagenSelected.getNumeroOperacion();
			tipoTransaccionDialog = imagenSelected.getTipoTransaccion();
			cuentaVoucherDialog = imagenSelected.getCuentaBancaria();
			if(imagenSelected.getCuentaBancaria()!=null) {
				ctaBancDialog = imagenSelected.getCuentaBancaria().getBanco().getAbreviatura()+" "+ imagenSelected.getCuentaBancaria().getNumero();
			}
			
		}
	}
	
	public void verVoucher() {
		imagenSelected = null;
		if(documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B") || documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("F")) {
			fechaVoucherDialog = null;
			montoVoucherDialog=null;
			nroOperacionVoucherDialog="";
			tipoTransaccionDialog="";
			ctaBancDialog="";
			cuentaVoucherDialog = null;
			
			loadImageDocumentoBean.setNombreArchivo("0.png");
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
			
			
//			String nombreBusqueda = "%"+documentoVentaSelected.getId() +"_%";
			
			List<Imagen> lstImagen = imagenService.findByDocumentoVentaAndEstado(documentoVentaSelected, true); 
			int contador = 1;
			for(Imagen i:lstImagen) {
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
			PrimeFaces.current().executeScript("PF('voucherDocumentoDialog').show();");
		}else {
			addErrorMessage("Las notas de Credito/Debito no tienen voucher");
			return;
		}
		
		
	}
	
	
	public void simularPrepago() {
		
		habilitarBoton=true;
		habilitarMontoPrepago=false;
		
		if(contratoPendienteSelected == null) {
			addErrorMessage("Debes importar un contrato.");
			return;
		}
		
		lstCuotaPendientesTemporal = new ArrayList<>();
		
//		---------------------------------------------------------------------------------
		
		if(tipoPrepago.equals("PC")) {
			
			if(montoPrepago==null) {
				addErrorMessage("Ingresar monto.");
				return;
			}
			if(montoPrepago.compareTo(BigDecimal.ZERO)<=0) {
				addErrorMessage("Monto tiene que ser mayor a 0.");
				return;
			}
			
			if(montoPrepago.compareTo(deudaActualConInteres)==0) {
				pagoTotalPrepago = true;
			}else if(montoPrepago.compareTo(deudaActualConInteres)==1) {
				pagoTotalPrepago = false;
				addErrorMessage("El monto prepago no debe ser mayor a la deuda actual.");
				return;
			}else {
				pagoTotalPrepago = false;
			}
			
			int primeraCuotaPendiente = lstCuotaPendientes.get(0).getNroCuota();
			
			if(primeraCuotaPendiente<=6 && montoPrepago.compareTo(deudaActualSinInteres)>0) {
				addErrorMessage("Por estar dentro de los 6 primeros meses, el monto a prepagar debe ser " + deudaActualSinInteres);
				return;
			}
						
			BigDecimal saldo = contratoPendienteSelected.getMontoVenta();
			for(Cuota c:lstCuotaPagadas) {
				saldo = saldo.subtract(c.getCuotaSI());
			}
			saldo = saldo.subtract(montoPrepago);			
			
			Cuota cuota = new Cuota();
			cuota.setCuotaSI(montoPrepago);
			cuota.setCuotaTotal(montoPrepago);
			
			lstCuotaVista.clear();

			if(incluirUltimaCuota.equals("Si")) {
				cuota.setCuotaRef(lstCuotaPendientes.get(0));
				lstCuotaPendientes.get(0).setPagoTotal("S");
				lstCuotaPagadas.add(lstCuotaPendientes.get(0));
				cuota.setCuotaSI(montoPrepago.subtract(lstCuotaPendientes.get(0).getCuotaTotal()));
				cuota.setCuotaTotal(montoPrepago.subtract(lstCuotaPendientes.get(0).getCuotaTotal()));
				saldo = saldo.add(lstCuotaPendientes.get(0).getInteres());
				lstCuotaPendientes.remove(0);
			}
			
			lstCuotaVista.addAll(lstCuotaPagadas);
		
			cuota.setNroCuota(0);
			cuota.setFechaPago(new Date());
			cuota.setInteres(BigDecimal.ZERO);
			cuota.setAdelanto(BigDecimal.ZERO);
			cuota.setPagoTotal("N");
			cuota.setContrato(contratoPendienteSelected);
			cuota.setEstado(true);
			cuota.setOriginal(false);
			cuota.setPrepago(true);
			lstCuotaVista.add(cuota);
			lstCuotaPendientesTemporal.add(cuota);
			
			BigDecimal nuevaCuotaSI = saldo.divide(new BigDecimal(lstCuotaPendientes.size()), 2, RoundingMode.HALF_UP);
			BigDecimal nuevoInteres = nuevaCuotaSI.multiply(contratoPendienteSelected.getInteres().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

			for (Cuota c:lstCuotaPendientes) {

				Cuota nuevaCuota = new Cuota(c);
				nuevaCuota.setCuotaSI(nuevaCuotaSI);
				nuevaCuota.setInteres(nuevoInteres);
				nuevaCuota.setCuotaTotal(nuevaCuotaSI.add(nuevoInteres));
				lstCuotaVista.add(nuevaCuota);
				lstCuotaPendientesTemporal.add(nuevaCuota);
			} 
			
//			---------------------------------------------------------------------------------
			
		}else if(tipoPrepago.equals("AR")) {
			if(nuevoNroCuotas == null) {
				addErrorMessage("Ingresar el nuevo número de cuotas.");
				return;
			}
			
			if(nuevoInteres == null) {
				addErrorMessage("Ingresar el nuevo interés.");
				return;
			}
			

			BigDecimal saldo = contratoPendienteSelected.getMontoVenta();
			for(Cuota c:lstCuotaPagadas) {
				saldo = saldo.subtract(c.getCuotaSI());
				if(nuevoInteres.compareTo(BigDecimal.ZERO)==0) {
					saldo=saldo.subtract(c.getInteres());
				}
			}
			
			lstCuotaVista.clear();

			lstCuotaVista.addAll(lstCuotaPagadas);
			int cantidadCuotasPagadas = 0;
			for(Cuota cp:lstCuotaPagadas) {
				if(cp.getNroCuota()!=0) {
					cantidadCuotasPagadas ++;
				}
			}
			
			Integer nuevoNroCuotasPendientes = nuevoNroCuotas - cantidadCuotasPagadas ;
			BigDecimal nuevaCuotaSI = saldo.divide(new BigDecimal(nuevoNroCuotasPendientes), 2, RoundingMode.HALF_UP);
			BigDecimal nuevoInteresRedAmpl = nuevaCuotaSI.multiply(nuevoInteres.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
			Integer nroCuota = cantidadCuotasPagadas +1;
			Date fecha = null;
			if(lstCuotaPagadas.size()==1) {
				fecha = lstCuotaPendientes.get(0).getFechaPago();
			}else {
				fecha = lstCuotaPagadas.get(1).getFechaPago();
			}

			
			for(int i=0; i<nuevoNroCuotasPendientes;i++) {
				
				Cuota nuevaCuota = new Cuota();
				nuevaCuota.setNroCuota(nroCuota);
				nuevaCuota.setFechaPago(sumarRestarMeses(fecha, lstCuotaPagadas.size()-1+i));
				nuevaCuota.setCuotaSI(nuevaCuotaSI);
				nuevaCuota.setInteres(nuevoInteresRedAmpl);
				nuevaCuota.setCuotaTotal(nuevaCuotaSI.add(nuevoInteresRedAmpl));
				nuevaCuota.setAdelanto(BigDecimal.ZERO);
				nuevaCuota.setPagoTotal("N");
				nuevaCuota.setContrato(contratoPendienteSelected);
				nuevaCuota.setEstado(true);
				nuevaCuota.setOriginal(false);
				nuevaCuota.setPrepago(false);
				nuevaCuota.setCuotaRef(null);
				
				lstCuotaVista.add(nuevaCuota);
				lstCuotaPendientesTemporal.add(nuevaCuota);
				nroCuota++;
				
			}
			
//			---------------------------------------------------------------------------------
			
		}else {
			if(montoPrepago==null) {
				addErrorMessage("Ingresar monto.");
				return;
			}
			if(montoPrepago.compareTo(BigDecimal.ZERO)<=0) {
				addErrorMessage("Monto tiene que ser mayor a 0.");
				return;
			}
			
			if(montoPrepago.compareTo(deudaActualConInteres)==0) {
				pagoTotalPrepago = true;
			}else if(montoPrepago.compareTo(deudaActualConInteres)==1) {
				pagoTotalPrepago = false;
				addErrorMessage("El monto prepago no debe ser mayor a la deuda actual.");
				return;
			}else {
				pagoTotalPrepago = false;
			}
			
			int primeraCuotaPendiente = lstCuotaPendientes.get(0).getNroCuota();
			
			if(primeraCuotaPendiente<=6 && montoPrepago.compareTo(deudaActualSinInteres)>0) {
				addErrorMessage("Por estar dentro de los 6 primeros meses, el monto a prepagar debe ser " + deudaActualSinInteres);
				return;
			}
			if(nuevoNroCuotas == null) {
				addErrorMessage("Ingresar el nuevo número de cuotas.");
				return;
			}
			
			if(nuevoInteres == null) {
				addErrorMessage("Ingresar el nuevo interés.");
				return;
			}
			

			BigDecimal saldo = contratoPendienteSelected.getMontoVenta();
			for(Cuota c:lstCuotaPagadas) {
				saldo = saldo.subtract(c.getCuotaSI());
				if(nuevoInteres.compareTo(BigDecimal.ZERO)==0) {
					saldo=saldo.subtract(c.getInteres());
				}
			}
			
			saldo = saldo.subtract(montoPrepago);	
			
			Cuota cuota = new Cuota();
			cuota.setCuotaSI(montoPrepago);
			cuota.setCuotaTotal(montoPrepago);
			
			lstCuotaVista.clear();

			if(incluirUltimaCuota.equals("Si")) {
				cuota.setCuotaRef(lstCuotaPendientes.get(0));
				lstCuotaPendientes.get(0).setPagoTotal("S");
				lstCuotaPagadas.add(lstCuotaPendientes.get(0));
				cuota.setCuotaSI(montoPrepago.subtract(lstCuotaPendientes.get(0).getCuotaTotal()));
				cuota.setCuotaTotal(montoPrepago.subtract(lstCuotaPendientes.get(0).getCuotaTotal()));
				saldo = saldo.add(lstCuotaPendientes.get(0).getInteres());
				lstCuotaPendientes.remove(0);
			}
			
			lstCuotaVista.addAll(lstCuotaPagadas);
		
			cuota.setNroCuota(0);
			cuota.setFechaPago(new Date());
			cuota.setInteres(BigDecimal.ZERO);
			cuota.setAdelanto(BigDecimal.ZERO);
			cuota.setPagoTotal("N");
			cuota.setContrato(contratoPendienteSelected);
			cuota.setEstado(true);
			cuota.setOriginal(false);
			cuota.setPrepago(true);
			lstCuotaVista.add(cuota);
			lstCuotaPendientesTemporal.add(cuota);
			
			
			
			Integer nuevoNroCuotasPendientes = nuevoNroCuotas - lstCuotaPagadas.size()+1;
			BigDecimal nuevaCuotaSI = saldo.divide(new BigDecimal(nuevoNroCuotasPendientes), 2, RoundingMode.HALF_UP);
			BigDecimal nuevoInteresRedAmpl = nuevaCuotaSI.multiply(nuevoInteres.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
			Integer nroCuota = lstCuotaPagadas.size();
			Date fecha = null;
			if(lstCuotaPagadas.size()==1) {
				fecha = lstCuotaPendientes.get(0).getFechaPago();
			}else {
				fecha = lstCuotaPagadas.get(1).getFechaPago();
			}

			
			for(int i=0; i<nuevoNroCuotasPendientes;i++) {
				
				Cuota nuevaCuota = new Cuota();
				nuevaCuota.setNroCuota(nroCuota);
				nuevaCuota.setFechaPago(sumarRestarMeses(fecha, lstCuotaPagadas.size()-1+i));
				nuevaCuota.setCuotaSI(nuevaCuotaSI);
				nuevaCuota.setInteres(nuevoInteresRedAmpl);
				nuevaCuota.setCuotaTotal(nuevaCuotaSI.add(nuevoInteresRedAmpl));
				nuevaCuota.setAdelanto(BigDecimal.ZERO);
				nuevaCuota.setPagoTotal("N");
				nuevaCuota.setContrato(contratoPendienteSelected);
				nuevaCuota.setEstado(true);
				nuevaCuota.setOriginal(false);
				nuevaCuota.setPrepago(false);
				nuevaCuota.setCuotaRef(null);
				
				lstCuotaVista.add(nuevaCuota);
				lstCuotaPendientesTemporal.add(nuevaCuota);
				nroCuota++;
				
			}
			
			
		}
	
		habilitarBoton=false;
		habilitarMontoPrepago=true;
		
		sumaCuotaSI = BigDecimal.ZERO;
		sumaInteres = BigDecimal.ZERO;
		sumaCuotaTotal = BigDecimal.ZERO;
		int contador = 0;
		
		for(Cuota c:lstCuotaVista) {
			if(contador!=0){
				sumaCuotaSI = sumaCuotaSI.add(c.getCuotaSI());
				sumaInteres = sumaInteres.add(c.getInteres());
				sumaCuotaTotal = sumaCuotaTotal.add(c.getCuotaTotal());
			}
			contador++;
		}
	
	}
	
	public Date sumarRestarMeses(Date fecha, int meses) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH, meses);
		return calendar.getTime();
	}
	
	public void savePrepago() {
	
		if(contratoPendienteSelected==null) {
			addErrorMessage("Primero debes importar contrato.");
			return;
		}
		
		if(pagoTotalPrepago) {
			for(Cuota c:lstCuotaPendientes) {
				c.setPagoTotal("S");
				cuotaService.save(c);
			}
			contratoPendienteSelected.setCancelacionTotal(true);
			contratoService.save(contratoPendienteSelected);
			

		}else {
			if(!lstCuotaPendientesTemporal.isEmpty()) {
				if(!tipoPrepago.equals("PC")) {
					contratoPendienteSelected.setInteres(nuevoInteres);
					contratoService.save(contratoPendienteSelected);
				}
				
				for(Cuota c:lstCuotaPendientes) {
					c.setEstado(false);
					cuotaService.save(c);
				}
			
				for(Cuota c:lstCuotaPendientesTemporal) {
					c.setEstado(true);
					cuotaService.save(c);
				}
				
			}else {
				addErrorMessage("Primero debes simular un prepago.");
				return;
			}
		}
		
		
		
//		Cliente cliente = clienteService.findByPersonAndEstado(contratoPendienteSelected.getPersonVenta(), true);
//		if(cliente == null){
//			 cliente = new Cliente();
//			 cliente.setPerson(contratoPendienteSelected.getPersonVenta());
//			 cliente.setRazonSocial(contratoPendienteSelected.getPersonVenta().getSurnames()+" "+contratoPendienteSelected.getPersonVenta().getNames());
//			 cliente.setNombreComercial(contratoPendienteSelected.getPersonVenta().getSurnames()+" "+contratoPendienteSelected.getPersonVenta().getNames());
//			 cliente.setRuc(contratoPendienteSelected.getPersonVenta().getDni());
//			 cliente.setDireccion(contratoPendienteSelected.getPersonVenta().getAddress());
//			 cliente.setPersonaNatural(true);
//			 cliente.setEstado(true);
//			 cliente.setFechaRegistro(new Date());
//			 cliente.setIdUsuarioRegistro(navegacionBean.getUsuarioLogin());
//			 cliente = clienteService.save(cliente);
//			 
//		}		
		
		addInfoMessage("Se guardo el prepago correctamente.");
		lstCuotaPendientes.clear();
		cancelarContrato();
	}	
	
	public void importarContrato() {
		lstCuotaVista = new ArrayList<>();
		lstCuotaPagadas=cuotaService.findByPagoTotalAndEstadoAndContratoOrderById("S", true, contratoPendienteSelected);
		lstCuotaPendientes = cuotaService.findByPagoTotalAndEstadoAndContratoOrderById("N", true, contratoPendienteSelected);
		lstCuotaVista.addAll(lstCuotaPagadas);
		lstCuotaVista.addAll(lstCuotaPendientes);
		deudaActualSinInteres= BigDecimal.ZERO;
		deudaActualConInteres= BigDecimal.ZERO;

		for(Cuota c:lstCuotaPendientes) {
			if(c.getNroCuota()!=0) {
				BigDecimal a = c.getCuotaSI().subtract(c.getAdelanto());
				BigDecimal b = c.getCuotaTotal().subtract(c.getAdelanto()); 
				deudaActualSinInteres = deudaActualSinInteres.add(a);	
				deudaActualConInteres = deudaActualConInteres.add(b);	
			}
		}
		
//		deudaActualSinInteres= BigDecimal.ZERO;
//		for(Cuota c:lstCuotaPagadas) {
//			deudaActualSinInteres = deudaActualSinInteres.add(c.getCuotaTotal());
//		}
//		deudaActualSinInteres = contratoPendienteSelected.getMontoVenta().subtract(deudaActualSinInteres);
		
	}
	
	private void anulacionFinalDeDocumento() {
		DocumentoVenta doc= documentoVentaService.anular(documentoVentaSelected);
		if(doc!=null) {
			addInfoMessage("Documento de venta anulado.");	
		}else {
			addErrorMessage("No se pudo anular el documento.");
		}
	}
	
	
	public void anularDocumento() {
		//si es boleta y  anulo el mismo de la emision, mandar mensaje de espererar 24 horas
		if(documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B")) {
			String fechaEmi = sdf.format(documentoVentaSelected.getFechaEmision());
			String fechaactual = sdf.format(new Date());
			if(fechaEmi.equals(fechaactual)) {
				addWarnMessage("Debe esperar 24 horas para poder anular el comprobante."); 
				return;
			}
			
		}
		
		if(!documentoVentaSelected.isEnvioSunat()) {
			anulacionFinalDeDocumento();
			return;
		}
		//ENVIAMO LA BAJA A NUBEFACT
        JSONObject json_rspta = consumingPostBo.apiConsumeDelete(documentoVentaSelected);

        if (json_rspta != null) {
            if (json_rspta.get("errors") != null) {
                addErrorMessage("Error al Enviar Anulación de comprobante electrónico: " + json_rspta.get("errors").toString());
            } else {

                if (json_rspta.get("sunat_ticket_numero") != null) {
                    documentoVentaSelected.setAnulacionSunatTicketNumero(json_rspta.get("sunat_ticket_numero").toString());
                    addInfoMessage("Se envió la baja a Sunat correctamente Nro de Ticket:" + json_rspta.get("sunat_ticket_numero").toString());
                }

                if (json_rspta.get("aceptada_por_sunat") != null) {
                    documentoVentaSelected.setAnulacionAceptadaPorSunat(json_rspta.get("aceptada_por_sunat").toString());
                }

                if (json_rspta.get("sunat_description") != null) {
                    documentoVentaSelected.setAnulacionSunatDescription(json_rspta.get("sunat_description").toString());
                }

                if (json_rspta.get("sunat_note") != null) {
                    documentoVentaSelected.setAnulacionSunatNote(json_rspta.get("sunat_note").toString());
                }

                if (json_rspta.get("sunat_responsecode") != null) {
                    documentoVentaSelected.setAnulacionSunatResponsecode(json_rspta.get("sunat_responsecode").toString());
                }

                if (json_rspta.get("sunat_soap_error") != null) {
                    documentoVentaSelected.setAnulacionSunatSoapError(json_rspta.get("sunat_soap_error").toString());
                }

                anulacionFinalDeDocumento();
        		
        			
            }
        }
		
		
		
		
	}
	
	public boolean esImpar(int iNumero) {// mandas el numero, y retorna falso si es impar, 
		if (iNumero % 2 != 0)// esta linea devuelve el residuo de una division,ejemplo, cuanto es 1/2? 0.5
			return true;
		else
			return false;
	}
	public void updateAdelanto(DetalleDocumentoVenta detalle) {
		if(detalle.getAdelanto()==null) {
			detalle.setAdelanto(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getAmortizacion().subtract(detalle.getAdelanto()));
		}else if (detalle.getAdelanto().compareTo(BigDecimal.ZERO) ==-1) {
			detalle.setAdelanto(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getAmortizacion().subtract(detalle.getAdelanto()));
		}else {
			detalle.setImporteVenta(detalle.getAmortizacion().subtract(detalle.getAdelanto()));
		}
		
		calcularTotales();
	}
	
	public void updateInteres(DetalleDocumentoVenta detalle) {
		if(detalle.getInteres()==null) {
			detalle.setInteres(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else if (detalle.getInteres().compareTo(BigDecimal.ZERO) ==-1) {
			detalle.setInteres(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else {
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}
		
		calcularTotales();
		
		
	}
	
	public void updateAmortizacion(DetalleDocumentoVenta detalle) {
		if(detalle.getTotalTemp()==null) {
			detalle.setTotalTemp(detalle.getImporteVenta());
		}
		if(detalle.getAmortizacion()==null) {
			detalle.setAmortizacion(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else if (detalle.getAmortizacion().compareTo(BigDecimal.ZERO) ==-1) {
			detalle.setAmortizacion(BigDecimal.ZERO);
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
		}else {
			detalle.setImporteVenta(detalle.getInteres().add(detalle.getAmortizacion()));
//			detalle.setProducto(productoAdelanto);
		}
		
		calcularTotales();
		
		
	}
	
	public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
	
	public void listarDetalleDocumentoVenta( ) {
		
		montoLetra = numeroALetra.Convertir(documentoVentaSelected.getTotal()+"", true, "SOLES");
		lstDetalleDocumentoVentaSelected = new ArrayList<>();
		lstDetalleDocumentoVentaSelected = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVentaSelected, true);
	}
	
	public String extension(String filename) {
		String valor = "" ;
		int index = filename.lastIndexOf('.');
		if (index == -1) {
			return valor;
		} else {
			valor = filename.substring(index + 1);
		}
		return valor;
	}
	
	public void editarCorreoCliente() {
		if(documentoVentaSelected.getCliente().getEmail1Fe() != null) {
			if(documentoVentaSelected.getCliente().getEmail1Fe().equals("")) {
				documentoVentaSelected.getCliente().setEmail1Fe(null);
			}
		}
		
		if(documentoVentaSelected.getCliente().getEmail2Fe() != null) {
			if(documentoVentaSelected.getCliente().getEmail2Fe().equals("")) {
				documentoVentaSelected.getCliente().setEmail2Fe(null);
			}
		}
		
		if(documentoVentaSelected.getCliente().getEmail3Fe() != null) {
			if(documentoVentaSelected.getCliente().getEmail3Fe().equals("")) {
				documentoVentaSelected.getCliente().setEmail3Fe(null);
			}
		}
		
		clienteService.save(documentoVentaSelected.getCliente());
		addInfoMessage("Se actualizó el correo correctamente");
	}
	
	public boolean validarDatosImagen() {
		boolean valida=false;
		if(file2!=null){
			if(fechaImag2==null) {
				addErrorMessage("Ingresar fecha del segundo voucher");
				return true;
			}else {
				if(fechaImag2.after(new Date())) {
					addErrorMessage("La fecha del segundo voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag2==null) {
				addErrorMessage("Ingresar monto del segundo voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag2);
			}
			if(nroOperImag2.equals("")) {
				addErrorMessage("Ingresar número de operación del segundo voucher");
				return true;
			}
			
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag2, montoImag2, nroOperImag2, ctaBanc2);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 2.");
				return true;
			}
		}
		
		if(file3!=null){
			if(fechaImag3==null) {
				addErrorMessage("Ingresar fecha del tercer voucher");
				return true;
			}else {
				if(fechaImag3.after(new Date())) {
					addErrorMessage("La fecha del tercer voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag3==null) {
				addErrorMessage("Ingresar monto del tercer voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag3);
			}
			if(nroOperImag3.equals("")) {
				addErrorMessage("Ingresar número de operación del tercer voucher");
				return true;
			}
			
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag3, montoImag3, nroOperImag3, ctaBanc3);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 3.");
				return true;
			}
		}
		
		if(file4!=null){
			if(fechaImag4==null) {
				addErrorMessage("Ingresar fecha del cuarto voucher");
				return true;
			}else {
				if(fechaImag4.after(new Date())) {
					addErrorMessage("La fecha del cuarto voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag4==null) {
				addErrorMessage("Ingresar monto del cuarto voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag4);
			}
			if(nroOperImag4.equals("")) {
				addErrorMessage("Ingresar número de operación del cuarto voucher");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag4, montoImag4, nroOperImag4, ctaBanc4);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 4.");
				return true;
			}
		}
		
		if(file5!=null){
			if(fechaImag5==null) {
				addErrorMessage("Ingresar fecha del quinto voucher");
				return true;
			}else {
				if(fechaImag5.after(new Date())) {
					addErrorMessage("La fecha del quinto voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag5==null) {
				addErrorMessage("Ingresar monto del quinto voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag5);
			}
			if(nroOperImag5.equals("")) {
				addErrorMessage("Ingresar número de operación del quinto voucher");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag5, montoImag5, nroOperImag5, ctaBanc5);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 5.");
				return true;
			}
		}
		
		if(file6!=null){
			if(fechaImag6==null) {
				addErrorMessage("Ingresar fecha del sexto voucher");
				return true;
			}else {
				if(fechaImag6.after(new Date())) {
					addErrorMessage("La fecha del sexto voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag6==null) {
				addErrorMessage("Ingresar monto del sexto voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag6);
			}
			if(nroOperImag6.equals("")) {
				addErrorMessage("Ingresar número de operación del sexto voucher");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag6, montoImag6, nroOperImag6, ctaBanc6);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 6.");
				return true;
			}
		}
		
		if(file7!=null){
			if(fechaImag7==null) {
				addErrorMessage("Ingresar fecha del séptimo voucher");
				return true;
			}else {
				if(fechaImag7.after(new Date())) {
					addErrorMessage("La fecha del séptimo voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag7==null) {
				addErrorMessage("Ingresar monto del séptimo voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag7);
			}
			if(nroOperImag7.equals("")) {
				addErrorMessage("Ingresar número de operación del séptimo voucher");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag7, montoImag7, nroOperImag7, ctaBanc7);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 7.");
				return true;
			}
		}
		
		if(file8!=null){
			if(fechaImag8==null) {
				addErrorMessage("Ingresar fecha del octavo voucher");
				return true;
			}else {
				if(fechaImag8.after(new Date())) {
					addErrorMessage("La fecha del octavo voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag8==null) {
				addErrorMessage("Ingresar monto del octavo voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag8);
			}
			if(nroOperImag8.equals("")) {
				addErrorMessage("Ingresar número de operación del octavo voucher");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag8, montoImag8, nroOperImag8, ctaBanc8);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 8.");
				return true;
			}
		}
		
		if(file9!=null){
			if(fechaImag9==null) {
				addErrorMessage("Ingresar fecha del noveno voucher");
				return true;
			}else {
				if(fechaImag9.after(new Date())) {
					addErrorMessage("La fecha del noveno voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag9==null) {
				addErrorMessage("Ingresar monto del noveno voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag9);
			}
			if(nroOperImag9.equals("")) {
				addErrorMessage("Ingresar número de operación del noveno voucher");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag9, montoImag9, nroOperImag9, ctaBanc9);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 9.");
				return true;
			}
		}
		
		if(file10!=null){
			if(fechaImag10==null) {
				addErrorMessage("Ingresar fecha del décimo voucher");
				return true;
			}else {
				if(fechaImag10.after(new Date())) {
					addErrorMessage("La fecha del décimo voucher es incorrecta.");
					return true;
				}
			}
			if(montoImag10==null) {
				addErrorMessage("Ingresar monto del décimo voucher");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag10);
			}
			if(nroOperImag10.equals("")) {
				addErrorMessage("Ingresar número de operación del décimo voucher");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag10, montoImag10, nroOperImag10, ctaBanc10);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 10.");
				return true;
			}
		}
		
		if(file11!=null){
			if(fechaImag11==null) {
				addErrorMessage("Ingresar fecha del voucher nro. 11");
				return true;
			}
			if(montoImag11==null) {
				addErrorMessage("Ingresar monto del voucher nro. 11");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag11);
			}
			if(nroOperImag11.equals("")) {
				addErrorMessage("Ingresar número de operación del voucher nro. 11");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag11, montoImag11, nroOperImag11, ctaBanc11);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 11.");
				return true;
			}
		}
		
		if(file12!=null){
			if(fechaImag12==null) {
				addErrorMessage("Ingresar fecha del voucher nro. 12");
				return true;
			}
			if(montoImag12==null) {
				addErrorMessage("Ingresar monto del voucher nro. 12");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag12);
			}
			if(nroOperImag12.equals("")) {
				addErrorMessage("Ingresar número de operación del voucher nro. 12");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag12, montoImag12, nroOperImag12, ctaBanc12);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 12.");
				return true;
			}
		}
		
		if(file13!=null){
			if(fechaImag13==null) {
				addErrorMessage("Ingresar fecha del voucher nro. 13");
				return true;
			}
			if(montoImag13==null) {
				addErrorMessage("Ingresar monto del voucher nro. 13");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag13);
			}
			if(nroOperImag13.equals("")) {
				addErrorMessage("Ingresar número de operación del voucher nro. 13");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag13, montoImag13, nroOperImag13, ctaBanc13);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 13.");
				return true;
			}
		}
		
		if(file14!=null){
			if(fechaImag14==null) {
				addErrorMessage("Ingresar fecha del voucher nro. 14");
				return true;
			}
			if(montoImag14==null) {
				addErrorMessage("Ingresar monto del voucher nro. 14");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag14);
			}
			if(nroOperImag14.equals("")) {
				addErrorMessage("Ingresar número de operación del voucher nro. 14");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag14, montoImag14, nroOperImag14, ctaBanc14);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 14.");
				return true;
			}
		}
		
		if(file15!=null){
			if(fechaImag15==null) {
				addErrorMessage("Ingresar fecha del voucher nro. 15");
				return true;
			}
			if(montoImag15==null) {
				addErrorMessage("Ingresar monto del voucher nro. 15");
				return true;
			}else {
				sumaMontoVoucher = sumaMontoVoucher.add(montoImag15);
			}
			if(nroOperImag15.equals("")) {
				addErrorMessage("Ingresar número de operación del voucher nro. 15");
				return true;
			}
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag15, montoImag15, nroOperImag15, ctaBanc15);
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 15.");
				return true;
			}
		}
		
		return valida;
	}
	
	public void validarFormularioDocumentoVenta() {
		if(lstDetalleDocumentoVenta.isEmpty()) { 
			addErrorMessage("Debes importar al menos un documento.");
			return;
		}else {
			for(DetalleDocumentoVenta dt : lstDetalleDocumentoVenta) {
				if(dt.getImporteVenta().compareTo(BigDecimal.ZERO)==0) {
					addErrorMessage("Elimina el detalle que tiene el importe de venta 0 (cero)");
					return;
				}
			}
		}
		
		if(clienteSelected == null) {
			addErrorMessage("Debes seleccionar un cliente");
			return;
		}else {
			if(razonSocialText.equals("")) {
				addErrorMessage("Ingresar Razon Social para el cliente");
				return;
			}
			if(direccionText.equals("")) {
				addErrorMessage("Ingresar Direccion para el cliente");
				return;
			}
		}

		sumaMontoVoucher = BigDecimal.ZERO;
		
		if(file1 == null) {
			addErrorMessage("Seleccione una imagen (voucher)");
			return;
		}else {
			if(fechaImag1==null) {
				addErrorMessage("Ingresar fecha del primer voucher");
				return;
			}else {
				if(fechaImag1.after(new Date())) {
					addErrorMessage("La fecha del voucher es incorrecta.");
					return;
				}
			}
			
			if(montoImag1==null) {
				addErrorMessage("Ingresar monto del primer voucher");
				return;
			}else {
				sumaMontoVoucher=sumaMontoVoucher.add(montoImag1);
			}
			if(nroOperImag1.equals("")) {
				addErrorMessage("Ingresar número de operación del primer voucher");
				return;
			}
			
			List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, fechaImag1, montoImag1, nroOperImag1, ctaBanc1);
		
			if(!buscarImagen.isEmpty()) {
				addErrorMessage("Ya existe el voucher numero 1.");
				return;
			}
			
		}
		boolean validaImagenes = validarDatosImagen();
		
		if(validaImagenes) {
			return;
		}else {
			if(sumaMontoVoucher.compareTo(importeTotal)!=0) {
				addErrorMessage("Los importes de los voucher no coinciden con el total del documento.");
				return;
			}
		}
		
		PrimeFaces.current().executeScript("PF('saveDocumento').show();");

	}
	
	public void saveDocumentoVenta() {
//			aqui actualiza los datos del cliente y guarda run razon doreccion
		clienteSelected.setRazonSocial(razonSocialText);
		clienteSelected.setDireccion(direccionText);
		clienteSelected.setEmail1Fe(email1Text);
		clienteSelected.setEmail2Fe(email2Text);
		clienteSelected.setEmail3Fe(email3Text);

		if(email1Text!=null) {
			if(email1Text.equals("")) {
				clienteSelected.setEmail1Fe(null);
			}
		}
		
		if(email2Text!=null) {
			if(email2Text.equals("")) {
				clienteSelected.setEmail2Fe(null);
			}
		}
		
		if(email3Text!=null) {
			if(email3Text.equals("")) {
				clienteSelected.setEmail3Fe(null);
			}
		}
		
		clienteService.save(clienteSelected);
		
		
		DocumentoVenta documentoVenta = new DocumentoVenta();
		documentoVenta.setCliente(clienteSelected);
		documentoVenta.setDocumentoVentaRef(null);
		documentoVenta.setSucursal(navegacionBean.getSucursalLogin());
		documentoVenta.setTipoDocumento(tipoDocumentoSelected);
		documentoVenta.setSerie(serieDocumentoSelected.getSerie());
		documentoVenta.setNumero(""); // vamos a setear el numero despues de haber guardado el documento
		if(tipoDocumentoSelected.getAbreviatura().equals("F")) {
			documentoVenta.setRuc(clienteSelected.getRuc());
		}else {
			documentoVenta.setRuc(clienteSelected.getDni());
		}
		documentoVenta.setRazonSocial(clienteSelected.getRazonSocial());
		documentoVenta.setNombreComercial(clienteSelected.getNombreComercial());
		documentoVenta.setDireccion(clienteSelected.getDireccion());
		documentoVenta.setFechaEmision(fechaEmision);
		documentoVenta.setFechaVencimiento(fechaEmision);
		documentoVenta.setTipoMoneda(moneda);
		documentoVenta.setObservacion(observacion);
		documentoVenta.setTipoPago(tipoPago);
		if(incluirIgv) {
			documentoVenta.setSubTotal(opGravada);
		}else {
			documentoVenta.setSubTotal(importeTotal);
		}
		documentoVenta.setIgv(IGV);
		documentoVenta.setTotal(importeTotal);
		documentoVenta.setFechaRegistro(new Date());
		documentoVenta.setUsuarioRegistro(navegacionBean.getUsuarioLogin());
		documentoVenta.setEstado(true);
		documentoVenta.setAnticipos(anticipos);
		documentoVenta.setOpGravada(opGravada);
		documentoVenta.setOpExonerada(opExonerada);
		documentoVenta.setOpInafecta(opInafecta);
		documentoVenta.setOpGratuita(opGratuita);
		documentoVenta.setDescuentos(descuentos);
		documentoVenta.setIsc(ISC);
		documentoVenta.setOtrosCargos(otrosCargos);
		documentoVenta.setOtrosTributos(otrosTributos);
		
		DocumentoVenta documento = documentoVentaService.save(documentoVenta, lstDetalleDocumentoVenta, serieDocumentoSelected); 
		if(documento != null) {
			int envio =enviarDocumentoSunat(documento, lstDetalleDocumentoVenta);
			
			lstDetalleDocumentoVenta.clear();// claer es limpiar en ingles prueba
			clienteSelected=null;
			calcularTotales();
			
			subirImagenes(documento.getId() + "", documento);
			setearInfoVoucher();
			
			razonSocialText = "";
			direccionText = "";
			email1Text = "";
			email2Text = "";
			email3Text = "";
			incluirIgv=false;
			
			String addMensaje = envio>0?"Se envio correctamente a SUNAT":"No se pudo enviar a SUNAT";
			addInfoMessage("Se guardó el documento correctamente. "+addMensaje);
			
		}else {
			addErrorMessage("No se puede guardar el documento."); 
			return;
		}
		
	}
	
	
	
	public void setearInfoVoucher() {
		fechaImag1=null;fechaImag2=null;fechaImag3=null;fechaImag4=null;fechaImag5=null;fechaImag6=null;fechaImag7=null;fechaImag8=null;fechaImag9=null;fechaImag10=null;
		
		montoImag1=null;montoImag2=null;montoImag3=null;montoImag4=null;montoImag5=null;montoImag6=null;montoImag7=null;montoImag8=null;montoImag9=null;montoImag10=null;
		
		nroOperImag1="";nroOperImag2="";nroOperImag3="";nroOperImag4="";nroOperImag5="";nroOperImag6="";nroOperImag7="";nroOperImag8="";nroOperImag9="";nroOperImag10="";
	}
	
	public void subirImagenes(String idDocumento, DocumentoVenta docVenta) {
		
		
		if(file1 != null) {
			String rename = idDocumento +"_1" + "." + getExtension(file1.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag1);
			registroImagen.setMonto(montoImag1);
			registroImagen.setNumeroOperacion(nroOperImag1);
			registroImagen.setCuentaBancaria(ctaBanc1);
			registroImagen.setTipoTransaccion(tipoTransaccion1);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date()); 
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc1.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file1);
		}
		if(file2 != null) {
			String rename = idDocumento +"_2" + "." + getExtension(file2.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag2);
			registroImagen.setMonto(montoImag2);
			registroImagen.setNumeroOperacion(nroOperImag2);
			registroImagen.setCuentaBancaria(ctaBanc2);
			registroImagen.setTipoTransaccion(tipoTransaccion2);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc2.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file2);
		}
		if(file3 != null) {
			String rename = idDocumento +"_3" + "." + getExtension(file3.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag3);
			registroImagen.setMonto(montoImag3);
			registroImagen.setNumeroOperacion(nroOperImag3);
			registroImagen.setCuentaBancaria(ctaBanc3);
			registroImagen.setTipoTransaccion(tipoTransaccion3);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc3.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file3);
		}
		if(file4 != null) {
			String rename = idDocumento +"_4" + "." + getExtension(file4.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag4);
			registroImagen.setMonto(montoImag4);
			registroImagen.setNumeroOperacion(nroOperImag4);
			registroImagen.setCuentaBancaria(ctaBanc4);
			registroImagen.setTipoTransaccion(tipoTransaccion4);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc4.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file4);
		}
		if(file5 != null) {
			String rename = idDocumento +"_5" + "." + getExtension(file5.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag5);
			registroImagen.setMonto(montoImag5);
			registroImagen.setNumeroOperacion(nroOperImag5);
			registroImagen.setCuentaBancaria(ctaBanc5);
			registroImagen.setTipoTransaccion(tipoTransaccion5);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc5.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file5);
		}
		if(file6 != null) {
			String rename = idDocumento +"_6" + "." + getExtension(file6.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag6);
			registroImagen.setMonto(montoImag6);
			registroImagen.setNumeroOperacion(nroOperImag6);
			registroImagen.setCuentaBancaria(ctaBanc6);
			registroImagen.setTipoTransaccion(tipoTransaccion6);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc6.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file6);
		}
		if(file7 != null) {
			String rename = idDocumento +"_7" + "." + getExtension(file7.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag7);
			registroImagen.setMonto(montoImag7);
			registroImagen.setNumeroOperacion(nroOperImag7);
			registroImagen.setCuentaBancaria(ctaBanc7);
			registroImagen.setTipoTransaccion(tipoTransaccion7);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc7.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file7);
		}
		if(file8 != null) {
			String rename = idDocumento +"_8" + "." + getExtension(file8.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag8);
			registroImagen.setMonto(montoImag8);
			registroImagen.setNumeroOperacion(nroOperImag8);
			registroImagen.setCuentaBancaria(ctaBanc8);
			registroImagen.setTipoTransaccion(tipoTransaccion8);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc8.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file8);
		}
		if(file9 != null) {
			String rename = idDocumento +"_9" + "." + getExtension(file9.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag9);
			registroImagen.setMonto(montoImag9);
			registroImagen.setNumeroOperacion(nroOperImag9);
			registroImagen.setCuentaBancaria(ctaBanc9);
			registroImagen.setTipoTransaccion(tipoTransaccion9);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc9.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file9);
		}
		if(file10 != null) {
			String rename = idDocumento +"_10" + "." + getExtension(file10.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag10);
			registroImagen.setMonto(montoImag10);
			registroImagen.setNumeroOperacion(nroOperImag10);
			registroImagen.setCuentaBancaria(ctaBanc10);
			registroImagen.setTipoTransaccion(tipoTransaccion10);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc10.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file10);
		}
		
		if(file11 != null) {
			String rename = idDocumento +"_11" + "." + getExtension(file11.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag11);
			registroImagen.setMonto(montoImag11);
			registroImagen.setNumeroOperacion(nroOperImag11);
			registroImagen.setCuentaBancaria(ctaBanc11);
			registroImagen.setTipoTransaccion(tipoTransaccion11);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc11.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file11);
		}
		
		if(file12 != null) {
			String rename = idDocumento +"_12" + "." + getExtension(file12.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag12);
			registroImagen.setMonto(montoImag12);
			registroImagen.setNumeroOperacion(nroOperImag12);
			registroImagen.setCuentaBancaria(ctaBanc12);
			registroImagen.setTipoTransaccion(tipoTransaccion12);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc12.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file12);
		}
		
		if(file13 != null) {
			String rename = idDocumento +"_13" + "." + getExtension(file13.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag13);
			registroImagen.setMonto(montoImag13);
			registroImagen.setNumeroOperacion(nroOperImag13);
			registroImagen.setCuentaBancaria(ctaBanc13);
			registroImagen.setTipoTransaccion(tipoTransaccion13);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc13.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file13);
		}
		
		if(file14 != null) {
			String rename = idDocumento +"_14" + "." + getExtension(file14.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag14);
			registroImagen.setMonto(montoImag14);
			registroImagen.setNumeroOperacion(nroOperImag14);
			registroImagen.setCuentaBancaria(ctaBanc14);
			registroImagen.setTipoTransaccion(tipoTransaccion14);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc14.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file14);
		}
		
		if(file15 != null) {
			String rename = idDocumento +"_15" + "." + getExtension(file15.getFileName());
			Imagen registroImagen = new Imagen();
			registroImagen.setNombre(rename);
			registroImagen.setCarpeta("IMG-DOCUMENTO-VENTA");
			registroImagen.setEstado(true);
			registroImagen.setFecha(fechaImag15);
			registroImagen.setMonto(montoImag15);
			registroImagen.setNumeroOperacion(nroOperImag15);
			registroImagen.setCuentaBancaria(ctaBanc15);
			registroImagen.setTipoTransaccion(tipoTransaccion15);
			registroImagen.setDocumentoVenta(docVenta);
			registroImagen.setUsuario(navegacionBean.getUsuarioLogin());
			registroImagen.setFechaRegistro(new Date());
			registroImagen.setPorRegularizar(navegacionBean.getSucursalLogin().getId().equals(ctaBanc15.getSucursal().getId())?"NO":"SI");
			registroImagen.setComentario(""); 
			imagenService.save(registroImagen);
			
            subirArchivo(rename, file15);
		}
	}
	
	public void subirArchivo(String nombre, UploadedFile file) {
//      File result = new File("/home/imagen/IMG-DOCUMENTO-VENTA/" + nombre);
//      File result = new File("C:\\IMG-DOCUMENTO-VENTA\\" + nombre);
      File result = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaDocumentoVenta() + nombre);

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
	     
	public void cancelarDocumentoVenta() {
		observacion="";
		clienteSelected=null;
		razonSocialText = "";
		direccionText = "";
		email1Text = "";
		email2Text = "";
		email3Text = "";

		lstDetalleDocumentoVenta.clear();
		calcularTotales();
		numMuestraImagen=1;
	}
	
	public void cancelarContrato() {
		contratoPendienteSelected=null;
		lstCuotaVista = new ArrayList<>();
		lstCuotaPendientes = new ArrayList<>();
		lstCuotaPendientesTemporal = new ArrayList<>();
		lstCuotaPagadas=new ArrayList<>();
		deudaActualSinInteres = BigDecimal.ZERO;
		deudaActualConInteres = BigDecimal.ZERO;
		montoPrepago = BigDecimal.ZERO;
		tipoPrepago = "PC";
		habilitarBoton=true;
		habilitarMontoPrepago=false;
		pagoTotalPrepago=false;
		sumaCuotaSI = null;
		sumaCuotaTotal = null;
		sumaInteres = null;

	}
	
	public void validacionFecha() {
		if(fechaEmision==null) {
			fechaEmision = new Date();
		}
	}
	
//	public void listarVoucher() {
//		lstVoucher = new ArrayList<>();
//		lstVoucher = voucherService.findByEstado(true);
//		
//	}
	
	public void listarCuota() {
		lstCuota = new ArrayList<>();
		lstCuota = cuotaService.findByPagoTotalAndEstado("N", true);
		
	}
	
	public void onChangeCliente() {
		if(clienteSelected !=null) {
			razonSocialText = clienteSelected.getRazonSocial();
			direccionText = clienteSelected.getDireccion();
			email1Text = clienteSelected.getEmail1Fe();
			email2Text = clienteSelected.getEmail2Fe();
			email3Text = clienteSelected.getEmail3Fe();
		}else {
			razonSocialText = "";
			direccionText = "";
			email1Text = "";
			email2Text = "";
			email3Text = "";
		}
	
	}
	
	public void importarCuota() {
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				if(d.getCuota() != null) {
					if(cuotaSelected.getId().equals(d.getCuota().getId())) {
						addErrorMessage("Ya seleccionó la cuota");			
						return;
					}
					if(!cuotaSelected.getContrato().getPersonVenta().getId().equals(d.getCuota().getContrato().getPersonVenta().getId()) ) {
						addErrorMessage("La cuota debe ser de la misma persona.");
						return;
					}
				}
			}

		}
		
		clienteSelected=null;
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(cuotaSelected.getContrato().getPersonVenta(), true, true);
		}else {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(cuotaSelected.getContrato().getPersonVenta(), true, false);
		}
		onChangeCliente();
		
		if(cuotaSelected.getNroCuota() ==0) {
			DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
			//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
			detalle.setDocumentoVenta(null);
			detalle.setProducto(productoInicial);
			detalle.setDescripcion("PAGO DE INICIAL POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ cuotaSelected.getContrato().getLote().getNumberLote() +" MZ - "+ cuotaSelected.getContrato().getLote().getManzana().getName() +" , UBICADO EN " + cuotaSelected.getContrato().getLote().getProject().getName());
			detalle.setAmortizacion(cuotaSelected.getCuotaSI());
			detalle.setInteres(BigDecimal.ZERO);
			detalle.setAdelanto(cuotaSelected.getAdelanto());
			detalle.setImporteVenta(cuotaSelected.getCuotaSI().subtract(cuotaSelected.getAdelanto()));
			detalle.setCuota(cuotaSelected);
			detalle.setRequerimientoSeparacion(null);
			detalle.setCuotaPrepago(null);
			detalle.setImporteVentaSinIgv(BigDecimal.ZERO);
			detalle.setPrecioSinIgv(BigDecimal.ZERO);
			lstDetalleDocumentoVenta.add(detalle);
			
			List<VoucherTemp> lstVoucherTemp = voucherTempService.findByPlantillaVentaEstadoAndPlantillaVentaLoteAndEstado("Aprobado", cuotaSelected.getContrato().getLote(), true);
			if(!lstVoucherTemp.isEmpty()) {
				numMuestraImagen = lstVoucherTemp.size();
				int cont = 1;
				for(VoucherTemp temp : lstVoucherTemp) {
					if(cont ==1) {
						fechaImag1 = temp.getFechaOperacion();
						montoImag1 = temp.getMonto();
						nroOperImag1 = temp.getNumeroOperacion();
						ctaBanc1 = temp.getCuentaBancaria();
						tipoTransaccion1 = temp.getTipoTransaccion();
					}
					if(cont ==2) {
						fechaImag2 = temp.getFechaOperacion();
						montoImag2 = temp.getMonto();
						nroOperImag2 = temp.getNumeroOperacion();
						ctaBanc2 = temp.getCuentaBancaria();
						tipoTransaccion2 = temp.getTipoTransaccion();
					}
					if(cont ==3) {
						fechaImag3 = temp.getFechaOperacion();
						montoImag3 = temp.getMonto();
						nroOperImag3 = temp.getNumeroOperacion();
						ctaBanc3 = temp.getCuentaBancaria();
						tipoTransaccion3 = temp.getTipoTransaccion();
					}
					if(cont ==4) {
						fechaImag4 = temp.getFechaOperacion();
						montoImag4 = temp.getMonto();
						nroOperImag4 = temp.getNumeroOperacion();
						ctaBanc4 = temp.getCuentaBancaria();
						tipoTransaccion4 = temp.getTipoTransaccion();
					}
					if(cont ==5) {
						fechaImag5 = temp.getFechaOperacion();
						montoImag5 = temp.getMonto();
						nroOperImag5 = temp.getNumeroOperacion();
						ctaBanc5 = temp.getCuentaBancaria();
						tipoTransaccion5 = temp.getTipoTransaccion();
					}
					if(cont ==6) {
						fechaImag6 = temp.getFechaOperacion();
						montoImag6 = temp.getMonto();
						nroOperImag6 = temp.getNumeroOperacion();
						ctaBanc6 = temp.getCuentaBancaria();
						tipoTransaccion6 = temp.getTipoTransaccion();
					}
					if(cont ==7) {
						fechaImag7 = temp.getFechaOperacion();
						montoImag7 = temp.getMonto();
						nroOperImag7 = temp.getNumeroOperacion();
						ctaBanc7 = temp.getCuentaBancaria();
						tipoTransaccion7 = temp.getTipoTransaccion();
					}
					if(cont ==8) {
						fechaImag8 = temp.getFechaOperacion();
						montoImag8 = temp.getMonto();
						nroOperImag8 = temp.getNumeroOperacion();
						ctaBanc8 = temp.getCuentaBancaria();
						tipoTransaccion8 = temp.getTipoTransaccion();
					}
					if(cont ==9) {
						fechaImag9 = temp.getFechaOperacion();
						montoImag9 = temp.getMonto();
						nroOperImag9 = temp.getNumeroOperacion();
						ctaBanc9 = temp.getCuentaBancaria();
						tipoTransaccion9 = temp.getTipoTransaccion();
					}
					if(cont ==10) {
						fechaImag10 = temp.getFechaOperacion();
						montoImag10 = temp.getMonto();
						nroOperImag10 = temp.getNumeroOperacion();
						ctaBanc10 = temp.getCuentaBancaria();
						tipoTransaccion10 = temp.getTipoTransaccion();
					}
					if(cont ==11) {
						fechaImag11 = temp.getFechaOperacion();
						montoImag11 = temp.getMonto();
						nroOperImag11 = temp.getNumeroOperacion();
						ctaBanc11 = temp.getCuentaBancaria();
						tipoTransaccion11 = temp.getTipoTransaccion();
					}
					if(cont ==12) {
						fechaImag12 = temp.getFechaOperacion();
						montoImag12 = temp.getMonto();
						nroOperImag12 = temp.getNumeroOperacion();
						ctaBanc12 = temp.getCuentaBancaria();
						tipoTransaccion12 = temp.getTipoTransaccion();
					}
					if(cont ==13) {
						fechaImag13 = temp.getFechaOperacion();
						montoImag13 = temp.getMonto();
						nroOperImag13 = temp.getNumeroOperacion();
						ctaBanc13 = temp.getCuentaBancaria();
						tipoTransaccion13 = temp.getTipoTransaccion();
					}
					if(cont ==14) {
						fechaImag14 = temp.getFechaOperacion();
						montoImag14 = temp.getMonto();
						nroOperImag14 = temp.getNumeroOperacion();
						ctaBanc14 = temp.getCuentaBancaria();
						tipoTransaccion14 = temp.getTipoTransaccion();
					}
					if(cont ==15) {
						fechaImag15 = temp.getFechaOperacion();
						montoImag15 = temp.getMonto();
						nroOperImag15 = temp.getNumeroOperacion();
						ctaBanc15 = temp.getCuentaBancaria();
						tipoTransaccion15 = temp.getTipoTransaccion();
					}
					
					cont++;
					
				}
				
			}
		}else {
			if(cuotaSelected.getAdelanto().compareTo(BigDecimal.ZERO) == 0) {
				DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
				//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
				detalle.setDocumentoVenta(null);
				detalle.setProducto(productoCuota);
				detalle.setDescripcion("PAGO DE LA CUOTA N° "+ cuotaSelected.getNroCuota() +" POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ cuotaSelected.getContrato().getLote().getNumberLote() +" MZ - "+ cuotaSelected.getContrato().getLote().getManzana().getName() +" , UBICADO EN " + cuotaSelected.getContrato().getLote().getProject().getName());
				detalle.setAmortizacion(cuotaSelected.getCuotaSI());
				detalle.setInteres(BigDecimal.ZERO);
				detalle.setAdelanto(cuotaSelected.getAdelanto());		
				detalle.setImporteVenta(cuotaSelected.getCuotaSI().subtract(cuotaSelected.getAdelanto()));
				detalle.setCuota(cuotaSelected);
				detalle.setRequerimientoSeparacion(null);
				detalle.setCuotaPrepago(null);
				detalle.setImporteVentaSinIgv(BigDecimal.ZERO);
				detalle.setPrecioSinIgv(BigDecimal.ZERO);
				lstDetalleDocumentoVenta.add(detalle);
				
				if(cuotaSelected.getInteres().compareTo(BigDecimal.ZERO)!=0) {
					DetalleDocumentoVenta detalleInteres = new DetalleDocumentoVenta();
					//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
					detalleInteres.setDocumentoVenta(null);
					detalleInteres.setProducto(productoInteres);
					detalleInteres.setDescripcion("POR EL INTERES CORRESPONDIENTE A LA CUOTA N°" + cuotaSelected.getNroCuota());
					detalleInteres.setAmortizacion(BigDecimal.ZERO);
					detalleInteres.setInteres(cuotaSelected.getInteres());
					detalleInteres.setAdelanto(BigDecimal.ZERO);
					detalleInteres.setImporteVenta(cuotaSelected.getInteres());
					detalleInteres.setCuota(cuotaSelected);
					detalleInteres.setRequerimientoSeparacion(null);
					detalleInteres.setCuotaPrepago(null);
					detalleInteres.setImporteVentaSinIgv(BigDecimal.ZERO);
					detalleInteres.setPrecioSinIgv(BigDecimal.ZERO);
					lstDetalleDocumentoVenta.add(detalleInteres);
				}
				
			}else {
				BigDecimal diferencia = cuotaSelected.getCuotaTotal().subtract(cuotaSelected.getAdelanto());
				if(diferencia.compareTo(cuotaSelected.getInteres()) ==1) {
					
				
					DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
					//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
					detalle.setDocumentoVenta(null);
					detalle.setProducto(productoCuota);
					detalle.setDescripcion("PAGO DE LA CUOTA N° "+ cuotaSelected.getNroCuota() +" POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ cuotaSelected.getContrato().getLote().getNumberLote() +" MZ - "+ cuotaSelected.getContrato().getLote().getManzana().getName() +" , UBICADO EN " + cuotaSelected.getContrato().getLote().getProject().getName());
					detalle.setAmortizacion(cuotaSelected.getCuotaSI().subtract(cuotaSelected.getAdelanto()));
					detalle.setInteres(BigDecimal.ZERO);
					detalle.setAdelanto(cuotaSelected.getAdelanto());
					detalle.setImporteVenta(cuotaSelected.getCuotaSI().subtract(cuotaSelected.getAdelanto()));
					detalle.setCuota(cuotaSelected);
					detalle.setRequerimientoSeparacion(null);
					detalle.setCuotaPrepago(null);
					detalle.setImporteVentaSinIgv(BigDecimal.ZERO);
					detalle.setPrecioSinIgv(BigDecimal.ZERO);
					lstDetalleDocumentoVenta.add(detalle);
					
					DetalleDocumentoVenta detalleInteres = new DetalleDocumentoVenta();
					//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
					detalleInteres.setDocumentoVenta(null);
					detalleInteres.setProducto(productoInteres);
					detalleInteres.setDescripcion("POR EL INTERES CORRESPONDIENTE A LA CUOTA N°" + cuotaSelected.getNroCuota());
					detalleInteres.setAmortizacion(BigDecimal.ZERO);
					detalleInteres.setInteres(cuotaSelected.getInteres());
					detalleInteres.setAdelanto(BigDecimal.ZERO);
					detalleInteres.setImporteVenta(cuotaSelected.getInteres());
					detalleInteres.setCuota(cuotaSelected);
					detalleInteres.setRequerimientoSeparacion(null);
					detalleInteres.setCuotaPrepago(null);
					detalleInteres.setImporteVentaSinIgv(BigDecimal.ZERO);
					detalleInteres.setPrecioSinIgv(BigDecimal.ZERO);
					lstDetalleDocumentoVenta.add(detalleInteres);
					
				}else {

					DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
					//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
					detalle.setDocumentoVenta(null);
					detalle.setProducto(productoCuota);
					detalle.setDescripcion("PAGO DE LA CUOTA N° "+ cuotaSelected.getNroCuota() +" POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ cuotaSelected.getContrato().getLote().getNumberLote() +" MZ - "+ cuotaSelected.getContrato().getLote().getManzana().getName() +" , UBICADO EN " + cuotaSelected.getContrato().getLote().getProject().getName());
					detalle.setAmortizacion(cuotaSelected.getCuotaSI().add(cuotaSelected.getInteres()).subtract(cuotaSelected.getAdelanto()));
					detalle.setInteres(BigDecimal.ZERO);
					detalle.setAdelanto(cuotaSelected.getAdelanto());		
					detalle.setImporteVenta(cuotaSelected.getCuotaSI().add(cuotaSelected.getInteres()).subtract(cuotaSelected.getAdelanto()));
					detalle.setCuota(cuotaSelected);
					detalle.setRequerimientoSeparacion(null);
					detalle.setCuotaPrepago(null);
					detalle.setImporteVentaSinIgv(BigDecimal.ZERO);
					detalle.setPrecioSinIgv(BigDecimal.ZERO);
					lstDetalleDocumentoVenta.add(detalle);
				}
				
				
			}
			
			
		}
		
		
		
		
		
		calcularTotales();
//		persona = cuotaSelected.getContrato().getPersonVenta();
		addInfoMessage("Cuota importada correctamente."); 
		
	
	}
	
	public void importarRequerimiento() {
		
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				if(d.getRequerimientoSeparacion() != null) {
					if(requerimientoSelected.getId()==d.getRequerimientoSeparacion().getId()) {
						addErrorMessage("Ya seleccionó el voucher");			
						return;
					}
					
					if(!requerimientoSelected.getPerson().getId().equals(d.getRequerimientoSeparacion().getPerson().getId())) {
						addErrorMessage("El voucher debe ser de la misma persona.");
						return;
					}
					
					
				}
			} 

		}
		
		List<VoucherTemp> lstVoucherTemp = voucherTempService.findByRequerimientoSeparacionEstadoAndRequerimientoSeparacionLoteAndEstado("Aprobado", requerimientoSelected.getLote(), true);
		if(!lstVoucherTemp.isEmpty()) {
			numMuestraImagen = lstVoucherTemp.size();
			int cont = 1;
			for(VoucherTemp temp : lstVoucherTemp) {
				if(cont ==1) {
					fechaImag1 = temp.getFechaOperacion();
					montoImag1 = temp.getMonto();
					nroOperImag1 = temp.getNumeroOperacion();
					ctaBanc1 = temp.getCuentaBancaria();
					tipoTransaccion1 = temp.getTipoTransaccion();
				}
				if(cont ==2) {
					fechaImag2 = temp.getFechaOperacion();
					montoImag2 = temp.getMonto();
					nroOperImag2 = temp.getNumeroOperacion();
					ctaBanc2 = temp.getCuentaBancaria();
					tipoTransaccion2 = temp.getTipoTransaccion();
				}
				if(cont ==3) {
					fechaImag3 = temp.getFechaOperacion();
					montoImag3 = temp.getMonto();
					nroOperImag3 = temp.getNumeroOperacion();
					ctaBanc3 = temp.getCuentaBancaria();
					tipoTransaccion3 = temp.getTipoTransaccion();
				}
				if(cont ==4) {
					fechaImag4 = temp.getFechaOperacion();
					montoImag4 = temp.getMonto();
					nroOperImag4 = temp.getNumeroOperacion();
					ctaBanc4 = temp.getCuentaBancaria();
					tipoTransaccion4 = temp.getTipoTransaccion();
				}
				if(cont ==5) {
					fechaImag5 = temp.getFechaOperacion();
					montoImag5 = temp.getMonto();
					nroOperImag5 = temp.getNumeroOperacion();
					ctaBanc5 = temp.getCuentaBancaria();
					tipoTransaccion5 = temp.getTipoTransaccion();
				}
				if(cont ==6) {
					fechaImag6 = temp.getFechaOperacion();
					montoImag6 = temp.getMonto();
					nroOperImag6 = temp.getNumeroOperacion();
					ctaBanc6 = temp.getCuentaBancaria();
					tipoTransaccion6 = temp.getTipoTransaccion();
				}
				if(cont ==7) {
					fechaImag7 = temp.getFechaOperacion();
					montoImag7 = temp.getMonto();
					nroOperImag7 = temp.getNumeroOperacion();
					ctaBanc7 = temp.getCuentaBancaria();
					tipoTransaccion7 = temp.getTipoTransaccion();
				}
				if(cont ==8) {
					fechaImag8 = temp.getFechaOperacion();
					montoImag8 = temp.getMonto();
					nroOperImag8 = temp.getNumeroOperacion();
					ctaBanc8 = temp.getCuentaBancaria();
					tipoTransaccion8 = temp.getTipoTransaccion();
				}
				if(cont ==9) {
					fechaImag9 = temp.getFechaOperacion();
					montoImag9 = temp.getMonto();
					nroOperImag9 = temp.getNumeroOperacion();
					ctaBanc9 = temp.getCuentaBancaria();
					tipoTransaccion9 = temp.getTipoTransaccion();
				}
				if(cont ==10) {
					fechaImag10 = temp.getFechaOperacion();
					montoImag10 = temp.getMonto();
					nroOperImag10 = temp.getNumeroOperacion();
					ctaBanc10 = temp.getCuentaBancaria();
					tipoTransaccion10 = temp.getTipoTransaccion();
				}
				
				cont++;
				
			}
		}
		
		clienteSelected=null;
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(requerimientoSelected.getPerson(), true, true);
		}else {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(requerimientoSelected.getPerson(), true, false);
		}
		onChangeCliente();
		
		DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
		//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
		detalle.setDocumentoVenta(null);
		detalle.setProducto(productoVoucher);
//		detalle.setDescripcion("PAGO DE SEPARACIÓN POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ requerimientoSelected.getLote().getNumberLote() +" MZ - "+ requerimientoSelected.getLote().getManzana().getName() +" , UBICADO EN " + requerimientoSelected.getLote().getProject().getName());
		detalle.setDescripcion("PAGO DE SEPARACIÓN POR LA VENTA DE UN LOTE DE TERRENO.");
		detalle.setAmortizacion(requerimientoSelected.getMonto());
		detalle.setInteres(BigDecimal.ZERO);
		detalle.setAdelanto(BigDecimal.ZERO);
		detalle.setImporteVenta(requerimientoSelected.getMonto());
		detalle.setCuota(null);
		detalle.setRequerimientoSeparacion(requerimientoSelected);
		detalle.setCuotaPrepago(null);
		detalle.setEstado(true);
		detalle.setImporteVentaSinIgv(BigDecimal.ZERO);
		detalle.setPrecioSinIgv(BigDecimal.ZERO);
		lstDetalleDocumentoVenta.add(detalle);
		
		calcularTotales();
//		persona = voucherSelected.getRequerimientoSeparacion().getProspection().getProspect().getPerson(); 
		addInfoMessage("Voucher importado correctamente."); 
	}
	
	public void importarPrepago() {
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				if(d.getCuotaPrepago() != null) {
					if(prepagoSelected.getId()==d.getCuotaPrepago().getId()) {
						addErrorMessage("Ya seleccionó el prepago");			
						return;
					}
					
					if(prepagoSelected.getContrato().getPersonVenta().getId() != d.getCuotaPrepago().getContrato().getPersonVenta().getId()) {
						addErrorMessage("El prepago debe ser de la misma persona.");
						return;
					}
				}
			}

		}
		
		clienteSelected=null;
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(prepagoSelected.getContrato().getPersonVenta(), true, true);
		}else {
			clienteSelected = clienteService.findByPersonAndEstadoAndPersonaNatural(prepagoSelected.getContrato().getPersonVenta(), true, false);
		}
		onChangeCliente();
		
		if(prepagoSelected.getCuotaRef()!=null) {
			cuotaSelected=prepagoSelected.getCuotaRef();
			importarCuota();
		}
		
		DetalleDocumentoVenta detalle = new DetalleDocumentoVenta();
		//null porque se tiene que guardar primero el documento de venta, luego asignar documentoVenta a todos los detalles
		detalle.setDocumentoVenta(null);
		detalle.setProducto(productoAmortizacion);
		detalle.setDescripcion(detalle.getProducto().getDescripcion().toUpperCase() + " POR LA VENTA DE UN LOTE DE TERRENO CON N° "+ prepagoSelected.getContrato().getLote().getNumberLote() +" MZ - "+ prepagoSelected.getContrato().getLote().getManzana().getName() +" , UBICADO EN " + prepagoSelected.getContrato().getLote().getProject().getName());
																												
		detalle.setAmortizacion(prepagoSelected.getCuotaTotal());
		detalle.setInteres(BigDecimal.ZERO);
		detalle.setAdelanto(BigDecimal.ZERO);
		detalle.setImporteVenta(prepagoSelected.getCuotaTotal());
		detalle.setCuota(null);
		detalle.setRequerimientoSeparacion(null);
		detalle.setCuotaPrepago(prepagoSelected);
		detalle.setEstado(true);
		detalle.setImporteVentaSinIgv(BigDecimal.ZERO);
		detalle.setPrecioSinIgv(BigDecimal.ZERO);
		lstDetalleDocumentoVenta.add(detalle);
		
		calcularTotales();
//		persona = prepagoSelected.getContrato().getPersonVenta(); 
		addInfoMessage("Prepago importado correctamente."); 
				
	}
	
	public void calcularTotales() {
		anticipos = BigDecimal.ZERO;
		opInafecta=BigDecimal.ZERO;
		importeTotal=BigDecimal.ZERO;
		opGravada=BigDecimal.ZERO;
		IGV = BigDecimal.ZERO;
		if(!lstDetalleDocumentoVenta.isEmpty()) {
			for(DetalleDocumentoVenta d:lstDetalleDocumentoVenta) {
				opInafecta= opInafecta.add(d.getImporteVenta());
				importeTotal= importeTotal.add(d.getImporteVenta());
//				if(d.getTotalTemp()!=null) {
//					anticipos = anticipos.add(d.getAmortizacion());
//				} 
				if(d.getCuota()!=null) {
//					if(!d.getProducto().getTipoProducto().equals(TipoProductoType.INTERES.getTipo())) {
						if(d.getProducto().getTipoProducto().equals(TipoProductoType.INICIAL.getTipo())) {
							anticipos=anticipos.add(d.getCuota().getAdelanto());
						}
						
//					}
					
				}
			}
		}
	} 
	
	public void eliminarDetalleVenta(int index) {
		lstDetalleDocumentoVenta.remove(index);
		calcularTotales();
		if(lstDetalleDocumentoVenta.isEmpty()) {
			clienteSelected = null;
			cuotaSelected = null;
//			persona=null;
		}
		addInfoMessage("Detalle eliminado");
		
	}
	
	public void iniciarDatosDocVenta() {
		
		documentoVentaNew= new DocumentoVenta(); 
		documentoVentaNew.setFechaEmision(new Date());
//		persona=null;
		
	}
	
	public void listarSerie() {
		lstSerieDocumento = serieDocumentoService.findByTipoDocumentoAndSucursal(tipoDocumentoSelected, navegacionBean.getSucursalLogin());
		serieDocumentoSelected=lstSerieDocumento.get(0);

		numero =  String.format("%0" + serieDocumentoSelected.getTamanioNumero()  + "d", Integer.valueOf(serieDocumentoSelected.getNumero()) ); 
//		changeTipoDocumentoVenta();
		
		listarClientes();
	}
	
	public void listarClientes() {
		if(tipoDocumentoSelected.getAbreviatura().equals("B")) {
			lstCliente = clienteService.findByEstadoAndPersonaNatural(true, true);
		}else {
			lstCliente = clienteService.findByEstadoAndPersonaNatural(true, false);
		}
	}
	
	public void listarSerieNota() {
		
		if(documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B") || documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("F")) {
			String anio = sdfYear.format(fechaEmisionNotaVenta);
			
			String codigoInt = "";
			
			if(tipoDocumentoNotaSelected.getAbreviatura().equals("C")) {
				codigoInt = "NC" + documentoVentaSelected.getTipoDocumento().getAbreviatura();
			}else {
				codigoInt = "ND" + documentoVentaSelected.getTipoDocumento().getAbreviatura();
			}
			
			
			lstSerieNotaDocumento = serieDocumentoService.findByTipoDocumentoAndAnioAndSucursalAndCodigoInterno(tipoDocumentoNotaSelected, anio, navegacionBean.getSucursalLogin(), codigoInt);
			serieNotaDocumentoSelected=lstSerieNotaDocumento.get(0);

			numeroNota =  String.format("%0" + serieNotaDocumentoSelected.getTamanioNumero()  + "d", Integer.valueOf(serieNotaDocumentoSelected.getNumero()) ); 
			listarMotivoNota();	
			listarDetalleDocumentoVenta();
			tipoOperacionSelected=lstTipoOperacion.get(0);
			identificadorSelected=lstIdentificador.get(0);
		}else {
			addErrorMessage("Debe seleccionar una factura o boleta.");
			return;
		}
		PrimeFaces.current().executeScript("PF('notaCreditoDebitoDialog').show();");
		
	}
	
	public void listarMotivoNota() {
	
		lstMotivoNota = motivoNotaService.findByTipoDocumentoAndEstado(tipoDocumentoNotaSelected.getAbreviatura(), true);
		
	}
	
	public void cambiarSerie() {
		numero =  String.format("%0" + serieDocumentoSelected.getTamanioNumero()  + "d", Integer.valueOf(serieDocumentoSelected.getNumero()) ); 
	}
	
	public void iniciarLazy() {
		lstDocumentoVentaLazy = new LazyDataModel<DocumentoVenta>() {
			private List<DocumentoVenta> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public DocumentoVenta getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (DocumentoVenta documentoVenta : datasource) {
                    if (documentoVenta.getId() == intRowKey) {
                        return documentoVenta;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(DocumentoVenta documentoVenta) {
                return String.valueOf(documentoVenta.getId());
            }

			@Override
			public List<DocumentoVenta> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String razonSocial = "%" + (filterBy.get("razonSocial") != null ? filterBy.get("razonSocial").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String numero = "%" + (filterBy.get("numero") != null ? filterBy.get("numero").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String ruc = "%" + (filterBy.get("ruc") != null ? filterBy.get("ruc").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("fechaEmision").descending();
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
               
                Page<DocumentoVenta> pageDocumentoVenta=null;
               
                if(estadoSunat==null) {
                	if(tipoDocumentoFilter==null) {
                        pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, pageable);
                	}else {
                        pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndTipoDocumento(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc, tipoDocumentoFilter, pageable);
                	}
                }else {
                	if(tipoDocumentoFilter==null) {
                        pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunat(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc,estadoSunat, pageable);
                	}else {
                		pageDocumentoVenta= documentoVentaService.findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLikeAndEnvioSunatAndTipoDocumento(estado, navegacionBean.getSucursalLogin(), razonSocial, numero, ruc,estadoSunat, tipoDocumentoFilter, pageable);
                	}
                }
                
                setRowCount((int) pageDocumentoVenta.getTotalElements());
                return datasource = pageDocumentoVenta.getContent();
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
			public List<Cuota> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				String names = "%" + (filterBy.get("contrato.personVenta.surnames") != null ? filterBy.get("contrato.personVenta.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String dni = "%" + (filterBy.get("contrato.personVenta.dni") != null ? filterBy.get("contrato.personVenta.dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String numLote = "%" + (filterBy.get("contrato.lote.numberLote") != null ? filterBy.get("contrato.lote.numberLote").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String manzana = "%" + (filterBy.get("contrato.lote.manzana.name") != null ? filterBy.get("contrato.lote.manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
				
                Sort sort=Sort.by("fechaPago").ascending();
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
               
                Page<Cuota> pageCuota=null;
                if(projectFilter != null) {
                    pageCuota= cuotaService.findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectNameAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLike("N", true, names, dni, projectFilter.getName(), navegacionBean.getSucursalLogin(),numLote,manzana,pageable);

				}else {
                    pageCuota= cuotaService.findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLike("N", true, names, dni,navegacionBean.getSucursalLogin(),numLote,manzana, pageable);

				}
                
                
                setRowCount((int) pageCuota.getTotalElements());
                return datasource = pageCuota.getContent();
            }
		};
	}
		
	public void iniciarLazyRequerimiento() {

		lstRequerimientoLazy = new LazyDataModel<RequerimientoSeparacion>() {
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
                for (RequerimientoSeparacion requerimientoSeparacion : datasource) {
                    if (requerimientoSeparacion.getId() == intRowKey) {
                        return requerimientoSeparacion;
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
               
//				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

                Sort sort=Sort.by("estado").ascending();
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
               
                Page<RequerimientoSeparacion> pageVoucher= requerimientoSeparacionService.findAllByEstadoAndLoteProjectSucursalAndGeneraDocumento("Aprobado", navegacionBean.getSucursalLogin(), false, pageable);
                
                setRowCount((int) pageVoucher.getTotalElements());
                return datasource = pageVoucher.getContent();
            }
		};
	}
	
	public void iniciarLazyPrepago() {

		lstPrepagoLazy = new LazyDataModel<Cuota>() {
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
			public List<Cuota> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               

                Sort sort=Sort.by("fechaPago").ascending();
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
               
                Page<Cuota>pageCuota= cuotaService.findByPagoTotalAndEstadoAndPrepago("N", true, true, pageable);
                
                setRowCount((int) pageCuota.getTotalElements());
                return datasource = pageCuota.getContent();
            }
		};
	}
	
	public void iniciarLazyContratosPendientes() {

		lstContratosPendientesLazy = new LazyDataModel<Contrato>() {
			private List<Contrato> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Contrato getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Contrato contrato : datasource) {
                    if (contrato.getId() == intRowKey) {
                        return contrato;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Contrato contrato) {
                return String.valueOf(contrato.getId());
            }

			@Override
			public List<Contrato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
//				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

				String names = "%" + (filterBy.get("personVenta.surnames") != null ? filterBy.get("personVenta.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String dni = "%" + (filterBy.get("personVenta.dni") != null ? filterBy.get("personVenta.dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
				
                Sort sort=Sort.by("lote.manzana.name").ascending();
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
               
                Page<Contrato> pageContrato= contratoService.findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(names, dni, EstadoContrato.ACTIVO.getName(), false, navegacionBean.getSucursalLogin(), pageable);
                
                setRowCount((int) pageContrato.getTotalElements());
                return datasource = pageContrato.getContent();
            }
		};
	}

	public Converter getConversorSerie() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	SerieDocumento c = null;
                    for (SerieDocumento si : lstSerieDocumento) {
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
                    return ((SerieDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorSerieNota() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	SerieDocumento c = null;
                    for (SerieDocumento si : lstSerieNotaDocumento) {
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
                    return ((SerieDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public void pdfDocumentoElectronico() {

        if (lstDetalleDocumentoVentaSelected == null || lstDetalleDocumentoVentaSelected.isEmpty()) {
            addInfoMessage("No hay datos para mostrar");
        } else {
        	
        	dt = new DataSourceDocumentoVenta();
            for (int i = 0; i < lstDetalleDocumentoVentaSelected.size(); i++) {
               
            	lstDetalleDocumentoVentaSelected.get(i).setDocumentoVenta(documentoVentaSelected);
                dt.addResumenDetalle(lstDetalleDocumentoVentaSelected.get(i));
            }
        	
        	
            parametros = new HashMap<String, Object>();
            parametros.put("TOTALSTRING", montoLetra);
            if (documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B")) {
            	 parametros.put("TIPOCOMPROBANTE", "BOLETA DE VENTA");
            }else {
            	parametros.put("TIPOCOMPROBANTE", documentoVentaSelected.getTipoDocumento().getDescripcion().toUpperCase());
            }   
            
            parametros.put("NOMBRECOMERCIAL", documentoVentaSelected.getRazonSocial());
            parametros.put("RUC", documentoVentaSelected.getRuc());
            parametros.put("DIRECCION", documentoVentaSelected.getDireccion());
            parametros.put("NOMBRECOMERCIAL", documentoVentaSelected.getRazonSocial());
            parametros.put("FECHAEMISION", sdf2.format(documentoVentaSelected.getFechaEmision()));
            parametros.put("TIPOMONEDA", documentoVentaSelected.getTipoMoneda().equals("S")?"Soles":"Dólares");
            parametros.put("OBSERVACION", documentoVentaSelected.getObservacion());
            parametros.put("CONDICIONPAGO", documentoVentaSelected.getTipoPago());
            
            if(lstDetalleDocumentoVentaSelected.get(0).getProducto().getId()==productoVoucher.getId()) {
            	parametros.put("ANTICIPOS", documentoVentaSelected.getOpInafecta());
            }else {
                parametros.put("ANTICIPOS", documentoVentaSelected.getAnticipos());

            }
            String fecha = sdf2.format(documentoVentaSelected.getFechaEmision());
            parametros.put("OPGRAVADA", documentoVentaSelected.getOpGravada());
            parametros.put("OPEXONERADA", documentoVentaSelected.getOpExonerada());
            parametros.put("OPINAFECTA", documentoVentaSelected.getOpInafecta());
            parametros.put("OPGRATUITA", documentoVentaSelected.getOpGratuita());
            parametros.put("DESCUENTOS", documentoVentaSelected.getDescuentos());
            parametros.put("ISC", documentoVentaSelected.getIsc());
            parametros.put("IGV", documentoVentaSelected.getIgv());
            parametros.put("OTROSCARGOS", documentoVentaSelected.getOtrosCargos());
            parametros.put("OTROSTRIBUTOS", documentoVentaSelected.getOtrosTributos());
            parametros.put("IMPORTETOTAL", documentoVentaSelected.getTotal());
            parametros.put("QR", navegacionBean.getSucursalLogin().getRuc() + "|" + documentoVentaSelected.getTipoDocumento().getCodigo() + "|" + 
	    		documentoVentaSelected.getSerie() + "|" + documentoVentaSelected.getNumero() + "|" + "0" + "|" + documentoVentaSelected.getTotal() + "|" + 
	    		fecha + "|" + (documentoVentaSelected.getTipoDocumento().getAbreviatura().equals("B")?"1":"6") + "|" + documentoVentaSelected.getRuc() + "|");



            String bar = navegacionBean.getSucursalLogin().getRuc().trim() + "|" + documentoVentaSelected.getSerie()+"-"+documentoVentaSelected.getNumero() + "|" + documentoVentaSelected.getFechaEmision();
            parametros.put("BARCODESTRING", bar);
            parametros.put("RUCEMPRESA", navegacionBean.getSucursalLogin().getRuc());
            
            if(documentoVentaSelected.getDocumentoVentaRef()!=null) {
            	parametros.put("TIPODOCUMENTOREF", "DOCUMENTO RELACIONADO:");
            	parametros.put("DOCUMENTOREF", documentoVentaSelected.getDocumentoVentaRef().getSerie() + "-"+documentoVentaSelected.getDocumentoVentaRef().getNumero());
            }else if(documentoVentaSelected.getNumeroNotaCredito() != null) {
            	if(!documentoVentaSelected.getNumeroNotaCredito().equals("")) {
            		parametros.put("TIPODOCUMENTOREF", "NOTA DE CRÉDITO:");
                	parametros.put("DOCUMENTOREF", documentoVentaSelected.getNumeroNotaCredito());
            	}
            }else if(documentoVentaSelected.getNumeroNotaDebito() != null) {
            	if(!documentoVentaSelected.getNumeroNotaDebito().equals("")) {
            		parametros.put("TIPODOCUMENTOREF", "NOTA DE DÉBITO:");
                	parametros.put("DOCUMENTOREF", documentoVentaSelected.getNumeroNotaDebito());
            	}
            }else {
            	parametros.put("TIPODOCUMENTOREF", "");
            	parametros.put("DOCUMENTOREF", "");
            }
            
            
            
            parametros.put("RUTAIMAGEN", getRutaGrafico(navegacionBean.getRutaLogo()));
            
            String path = "secured/view/modulos/ventas/reportes/jasper/repDocumentoFacturaElectronica.jasper"; 
            reportGenBo.exportByFormatNotConnectDb(dt, path, "pdf", parametros, "DOCUMENTO " , "hh");
            dt = null;
            parametros = null;
       

        }
    }

	public Converter getConversorTipoDocumento() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoDocumento c = null;
                    for (TipoDocumento si : lstTipoDocumento) {
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
                    return ((TipoDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorTipoDocumentoSunat() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoDocumento c = null;
                    for (TipoDocumento si : lstTipoDocumentoEnvioSunat) {
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
                    return ((TipoDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorProducto() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Producto c = null;
                    for (Producto si : lstProducto) {
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
                    return ((Producto) value).getId() + "";
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
	
	public Converter getConversorNotaDocumento() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoDocumento c = null;
                    for (TipoDocumento si : lstTipoDocumentoNota) {
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
                    return ((TipoDocumento) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorMotivoNota() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	MotivoNota c = null;
                    for (MotivoNota si : lstMotivoNota) {
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
                    return ((MotivoNota) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorTipoOperacion() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoOperacion c = null;
                    for (TipoOperacion si : lstTipoOperacion) {
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
                    return ((TipoOperacion) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorIdentificador() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Identificador c = null;
                    for (Identificador si : lstIdentificador) {
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
                    return ((Identificador) value).getId() + "";
                }
            }
        };
    }
	
	public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
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
	
	public Converter getConversorCliente() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Cliente c = null;
                    for (Cliente si : lstCliente) {
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
                    return ((Cliente) value).getId() + "";
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
	
	public List<Cliente> completeCliente(String query) {
        List<Cliente> lista = new ArrayList<>();
        for (Cliente c : lstCliente) {
        	if(tipoDocumentoSelected.getAbreviatura().equals("F")) {
        		if (c.getRuc().toUpperCase().contains(query.toUpperCase()) ){
                    lista.add(c);
                } 
        	}else {
        		if (c.getDni().toUpperCase().contains(query.toUpperCase()) ){
                	lista.add(c);
                }
        	}
            
        }
        return lista;
    }
	
	
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public DocumentoVentaService getDocumentoVentaService() {
		return documentoVentaService;
	}
	public void setDocumentoVentaService(DocumentoVentaService documentoVentaService) {
		this.documentoVentaService = documentoVentaService;
	}
	public LazyDataModel<DocumentoVenta> getLstDocumentoVentaLazy() {
		return lstDocumentoVentaLazy;
	}
	public void setLstDocumentoVentaLazy(LazyDataModel<DocumentoVenta> lstDocumentoVentaLazy) {
		this.lstDocumentoVentaLazy = lstDocumentoVentaLazy;
	}
	public DocumentoVenta getDocumentoVentaSelected() {
		return documentoVentaSelected;
	}
	public void setDocumentoVentaSelected(DocumentoVenta documentoVentaSelected) {
		this.documentoVentaSelected = documentoVentaSelected;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getFechaTextoVista() {
		return fechaTextoVista;
	}
	public void setFechaTextoVista(String fechaTextoVista) {
		this.fechaTextoVista = fechaTextoVista;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public DocumentoVenta getDocumentoVentaNew() {
		return documentoVentaNew;
	}
	public void setDocumentoVentaNew(DocumentoVenta documentoVentaNew) {
		this.documentoVentaNew = documentoVentaNew;
	}
	public List<SerieDocumento> getLstSerieDocumento() {
		return lstSerieDocumento;
	}
	public void setLstSerieDocumento(List<SerieDocumento> lstSerieDocumento) {
		this.lstSerieDocumento = lstSerieDocumento;
	}
	public SerieDocumentoService getSerieDocumentoService() {
		return serieDocumentoService;
	}
	public void setSerieDocumentoService(SerieDocumentoService serieDocumentoService) {
		this.serieDocumentoService = serieDocumentoService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public SerieDocumento getSerieDocumentoSelected() {
		return serieDocumentoSelected;
	}
	public void setSerieDocumentoSelected(SerieDocumento serieDocumentoSelected) {
		this.serieDocumentoSelected = serieDocumentoSelected;
	}
	public CuotaService getCuotaService() {
		return cuotaService;
	}
	public void setCuotaService(CuotaService cuotaService) {
		this.cuotaService = cuotaService;
	}
	public List<Cuota> getLstCuota() {
		return lstCuota;
	}
	public void setLstCuota(List<Cuota> lstCuota) {
		this.lstCuota = lstCuota;
	}
	public Cuota getCuotaSelected() {
		return cuotaSelected;
	}
	public void setCuotaSelected(Cuota cuotaSelected) {
		this.cuotaSelected = cuotaSelected;
	}
	public RequerimientoSeparacion getRequerimientoSelected() {
		return requerimientoSelected;
	}
	public void setRequerimientoSelected(RequerimientoSeparacion requerimientoSelected) {
		this.requerimientoSelected = requerimientoSelected;
	}

	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public BigDecimal getAnticipos() {
		return anticipos;
	}
	public void setAnticipos(BigDecimal anticipos) {
		this.anticipos = anticipos;
	}
	public BigDecimal getOpGravada() {
		return opGravada;
	}
	public void setOpGravada(BigDecimal opGravada) {
		this.opGravada = opGravada;
	}
	public BigDecimal getOpExonerada() {
		return opExonerada;
	}
	public void setOpExonerada(BigDecimal opExonerada) {
		this.opExonerada = opExonerada;
	}
	public BigDecimal getOpInafecta() {
		return opInafecta;
	}
	public void setOpInafecta(BigDecimal opInafecta) {
		this.opInafecta = opInafecta;
	}
	public BigDecimal getOpGratuita() {
		return opGratuita;
	}
	public void setOpGratuita(BigDecimal opGratuita) {
		this.opGratuita = opGratuita;
	}
	public BigDecimal getDescuentos() {
		return descuentos;
	}
	public void setDescuentos(BigDecimal descuentos) {
		this.descuentos = descuentos;
	}
	public BigDecimal getISC() {
		return ISC;
	}
	public void setISC(BigDecimal iSC) {
		ISC = iSC;
	}
	public BigDecimal getIGV() {
		return IGV;
	}
	public void setIGV(BigDecimal iGV) {
		IGV = iGV;
	}
	public BigDecimal getOtrosCargos() {
		return otrosCargos;
	}
	public void setOtrosCargos(BigDecimal otrosCargos) {
		this.otrosCargos = otrosCargos;
	}
	public BigDecimal getOtrosTributos() {
		return otrosTributos;
	}
	public void setOtrosTributos(BigDecimal otrosTributos) {
		this.otrosTributos = otrosTributos;
	}
	public BigDecimal getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}
	public DetalleDocumentoVenta getDetalleDocumentoVentaSelected() {
		return detalleDocumentoVentaSelected;
	}
	public void setDetalleDocumentoVentaSelected(DetalleDocumentoVenta detalleDocumentoVentaSelected) {
		this.detalleDocumentoVentaSelected = detalleDocumentoVentaSelected;
	}
	public List<DetalleDocumentoVenta> getLstDetalleDocumentoVenta() {
		return lstDetalleDocumentoVenta;
	}
	public void setLstDetalleDocumentoVenta(List<DetalleDocumentoVenta> lstDetalleDocumentoVenta) {
		this.lstDetalleDocumentoVenta = lstDetalleDocumentoVenta;
	}
	public ProductoService getProductoService() {
		return productoService;
	}
	public void setProductoService(ProductoService productoService) {
		this.productoService = productoService;
	}
	public Producto getProductoCuota() {
		return productoCuota;
	}
	public void setProductoCuota(Producto productoCuota) {
		this.productoCuota = productoCuota;
	}
	public Producto getProductoInteres() {
		return productoInteres;
	}
	public void setProductoInteres(Producto productoInteres) {
		this.productoInteres = productoInteres;
	}
	public List<DocumentoVenta> getLstDocumentoVenta() {
		return lstDocumentoVenta;
	}
	public void setLstDocumentoVenta(List<DocumentoVenta> lstDocumentoVenta) {
		this.lstDocumentoVenta = lstDocumentoVenta;
	}
	public LazyDataModel<Cuota> getLstCuotaLazy() {
		return lstCuotaLazy;
	}
	public void setLstCuotaLazy(LazyDataModel<Cuota> lstCuotaLazy) {
		this.lstCuotaLazy = lstCuotaLazy;
	}
	public RequerimientoSeparacionService getRequerimientoSeparacionService() {
		return requerimientoSeparacionService;
	}
	public void setRequerimientoSeparacionService(RequerimientoSeparacionService requerimientoSeparacionService) {
		this.requerimientoSeparacionService = requerimientoSeparacionService;
	}
	public LazyDataModel<RequerimientoSeparacion> getLstRequerimientoLazy() {
		return lstRequerimientoLazy;
	}
	public void setLstRequerimientoLazy(LazyDataModel<RequerimientoSeparacion> lstRequerimientoLazy) {
		this.lstRequerimientoLazy = lstRequerimientoLazy;
	}

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public Cliente getClienteSelected() {
		return clienteSelected;
	}
	public void setClienteSelected(Cliente clienteSelected) {
		this.clienteSelected = clienteSelected;
	}
	public DetalleDocumentoVentaService getDetalleDocumentoVentaService() {
		return detalleDocumentoVentaService;
	}
	public void setDetalleDocumentoVentaService(DetalleDocumentoVentaService detalleDocumentoVentaService) {
		this.detalleDocumentoVentaService = detalleDocumentoVentaService;
	}
	
	public String getMontoLetra() {
		return montoLetra;
	}
	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}
	public NumeroALetra getNumeroALetra() {
		return numeroALetra;
	}
	public void setNumeroALetra(NumeroALetra numeroALetra) {
		this.numeroALetra = numeroALetra;
	}
	public List<DetalleDocumentoVenta> getLstDetalleDocumentoVentaSelected() {
		return lstDetalleDocumentoVentaSelected;
	}
	public void setLstDetalleDocumentoVentaSelected(List<DetalleDocumentoVenta> lstDetalleDocumentoVentaSelected) {
		this.lstDetalleDocumentoVentaSelected = lstDetalleDocumentoVentaSelected;
	}
	public DetalleDocumentoVenta getDetalleDocumentoVenta() {
		return detalleDocumentoVenta;
	}
	public void setDetalleDocumentoVenta(DetalleDocumentoVenta detalleDocumentoVenta) {
		this.detalleDocumentoVenta = detalleDocumentoVenta;
	}
	public Producto getProductoVoucher() {
		return productoVoucher;
	}
	public void setProductoVoucher(Producto productoVoucher) {
		this.productoVoucher = productoVoucher;
	}
	public ReportGenBo getReportGenBo() {
		return reportGenBo;
	}
	public void setReportGenBo(ReportGenBo reportGenBo) {
		this.reportGenBo = reportGenBo;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public DataSourceDocumentoVenta getDt() {
		return dt;
	}
	public void setDt(DataSourceDocumentoVenta dt) {
		this.dt = dt;
	}
	public LazyDataModel<Contrato> getLstContratosPendientesLazy() {
		return lstContratosPendientesLazy;
	}
	public void setLstContratosPendientesLazy(LazyDataModel<Contrato> lstContratosPendientesLazy) {
		this.lstContratosPendientesLazy = lstContratosPendientesLazy;
	}
	public ContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(ContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public Contrato getContratoPendienteSelected() {
		return contratoPendienteSelected;
	}
	public void setContratoPendienteSelected(Contrato contratoPendienteSelected) {
		this.contratoPendienteSelected = contratoPendienteSelected;
	}
	public List<Cuota> getLstCuotaPagadas() {
		return lstCuotaPagadas;
	}
	public void setLstCuotaPagadas(List<Cuota> lstCuotaPagadas) {
		this.lstCuotaPagadas = lstCuotaPagadas;
	}
	public Cuota getCuotaPendienteContratoSelected() {
		return cuotaPendienteContratoSelected;
	}
	public void setCuotaPendienteContratoSelected(Cuota cuotaPendienteContratoSelected) {
		this.cuotaPendienteContratoSelected = cuotaPendienteContratoSelected;
	}
	public BigDecimal getMontoPrepago() {
		return montoPrepago;
	}
	public void setMontoPrepago(BigDecimal montoPrepago) {
		this.montoPrepago = montoPrepago;
	}
	public String getTipoPrepago() {
		return tipoPrepago;
	}
	public void setTipoPrepago(String tipoPrepago) {
		this.tipoPrepago = tipoPrepago;
	}
	public List<Cuota> getLstCuotaVista() {
		return lstCuotaVista;
	}
	public void setLstCuotaVista(List<Cuota> lstCuotaVista) {
		this.lstCuotaVista = lstCuotaVista;
	}
	public List<Cuota> getLstCuotaPendientes() {
		return lstCuotaPendientes;
	}
	public void setLstCuotaPendientes(List<Cuota> lstCuotaPendientes) {
		this.lstCuotaPendientes = lstCuotaPendientes;
	}
	public List<Cuota> getLstCuotaPendientesTemporal() {
		return lstCuotaPendientesTemporal;
	}
	public void setLstCuotaPendientesTemporal(List<Cuota> lstCuotaPendientesTemporal) {
		this.lstCuotaPendientesTemporal = lstCuotaPendientesTemporal;
	}
	public BigDecimal getDeudaActualSinInteres() {
		return deudaActualSinInteres;
	}
	public void setDeudaActualSinInteres(BigDecimal deudaActualSinInteres) {
		this.deudaActualSinInteres = deudaActualSinInteres;
	}
	public BigDecimal getDeudaActualConInteres() {
		return deudaActualConInteres;
	}
	public void setDeudaActualConInteres(BigDecimal deudaActualConInteres) {
		this.deudaActualConInteres = deudaActualConInteres;
	}
	public boolean isPagoTotalPrepago() {
		return pagoTotalPrepago;
	}
	public void setPagoTotalPrepago(boolean pagoTotalPrepago) {
		this.pagoTotalPrepago = pagoTotalPrepago;
	}
	public boolean isHabilitarBoton() {
		return habilitarBoton;
	}
	public void setHabilitarBoton(boolean habilitarBoton) {
		this.habilitarBoton = habilitarBoton;
	}
	public LazyDataModel<Cuota> getLstPrepagoLazy() {
		return lstPrepagoLazy;
	}
	public void setLstPrepagoLazy(LazyDataModel<Cuota> lstPrepagoLazy) {
		this.lstPrepagoLazy = lstPrepagoLazy;
	}
	public Producto getProductoAmortizacion() {
		return productoAmortizacion;
	}
	public void setProductoAmortizacion(Producto productoAmortizacion) {
		this.productoAmortizacion = productoAmortizacion;
	}
	public void setPrepagoSelected(Cuota prepagoSelected) {
		this.prepagoSelected = prepagoSelected;
	}
	public boolean isHabilitarMontoPrepago() {
		return habilitarMontoPrepago;
	}
	public void setHabilitarMontoPrepago(boolean habilitarMontoPrepago) {
		this.habilitarMontoPrepago = habilitarMontoPrepago;
	}
	public List<TipoDocumento> getLstTipoDocumento() {
		return lstTipoDocumento;
	}
	public void setLstTipoDocumento(List<TipoDocumento> lstTipoDocumento) {
		this.lstTipoDocumento = lstTipoDocumento;
	}
	public TipoDocumento getTipoDocumentoSelected() {
		return tipoDocumentoSelected;
	}
	public void setTipoDocumentoSelected(TipoDocumento tipoDocumentoSelected) {
		this.tipoDocumentoSelected = tipoDocumentoSelected;
	}
	public TipoDocumentoService getTipoDocumentoService() {
		return tipoDocumentoService;
	}
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	public SimpleDateFormat getSdf2() {
		return sdf2;
	}
	public void setSdf2(SimpleDateFormat sdf2) {
		this.sdf2 = sdf2;
	}
	public Producto getProductoAdelanto() {
		return productoAdelanto;
	}
	public void setProductoAdelanto(Producto productoAdelanto) {
		this.productoAdelanto = productoAdelanto;
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
	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
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
	public LoadImageDocumentoBean getLoadImageDocumentoBean() {
		return loadImageDocumentoBean;
	}
	public void setLoadImageDocumentoBean(LoadImageDocumentoBean loadImageDocumentoBean) {
		this.loadImageDocumentoBean = loadImageDocumentoBean;
	}
	public String getIncluirUltimaCuota() {
		return incluirUltimaCuota;
	}
	public void setIncluirUltimaCuota(String incluirUltimaCuota) {
		this.incluirUltimaCuota = incluirUltimaCuota;
	}
	public Cuota getPrepagoSelected() {
		return prepagoSelected;
	}
	public Integer getNuevoNroCuotas() {
		return nuevoNroCuotas;
	}
	public void setNuevoNroCuotas(Integer nuevoNroCuotas) {
		this.nuevoNroCuotas = nuevoNroCuotas;
	}
	public BigDecimal getNuevoInteres() {
		return nuevoInteres;
	}
	public void setNuevoInteres(BigDecimal nuevoInteres) {
		this.nuevoInteres = nuevoInteres;
	}
	public Date getFechaImag1() {
		return fechaImag1;
	}
	public void setFechaImag1(Date fechaImag1) {
		this.fechaImag1 = fechaImag1;
	}
	public Date getFechaImag2() {
		return fechaImag2;
	}
	public void setFechaImag2(Date fechaImag2) {
		this.fechaImag2 = fechaImag2;
	}
	public Date getFechaImag3() {
		return fechaImag3;
	}
	public void setFechaImag3(Date fechaImag3) {
		this.fechaImag3 = fechaImag3;
	}
	public Date getFechaImag4() {
		return fechaImag4;
	}
	public void setFechaImag4(Date fechaImag4) {
		this.fechaImag4 = fechaImag4;
	}
	public Date getFechaImag5() {
		return fechaImag5;
	}
	public void setFechaImag5(Date fechaImag5) {
		this.fechaImag5 = fechaImag5;
	}
	public Date getFechaImag6() {
		return fechaImag6;
	}
	public void setFechaImag6(Date fechaImag6) {
		this.fechaImag6 = fechaImag6;
	}
	public Date getFechaImag7() {
		return fechaImag7;
	}
	public void setFechaImag7(Date fechaImag7) {
		this.fechaImag7 = fechaImag7;
	}
	public Date getFechaImag8() {
		return fechaImag8;
	}
	public void setFechaImag8(Date fechaImag8) {
		this.fechaImag8 = fechaImag8;
	}
	public Date getFechaImag9() {
		return fechaImag9;
	}
	public void setFechaImag9(Date fechaImag9) {
		this.fechaImag9 = fechaImag9;
	}
	public Date getFechaImag10() {
		return fechaImag10;
	}
	public void setFechaImag10(Date fechaImag10) {
		this.fechaImag10 = fechaImag10;
	}
	public BigDecimal getMontoImag1() {
		return montoImag1;
	}
	public void setMontoImag1(BigDecimal montoImag1) {
		this.montoImag1 = montoImag1;
	}
	public BigDecimal getMontoImag2() {
		return montoImag2;
	}
	public void setMontoImag2(BigDecimal montoImag2) {
		this.montoImag2 = montoImag2;
	}
	public BigDecimal getMontoImag3() {
		return montoImag3;
	}
	public void setMontoImag3(BigDecimal montoImag3) {
		this.montoImag3 = montoImag3;
	}
	public BigDecimal getMontoImag4() {
		return montoImag4;
	}
	public void setMontoImag4(BigDecimal montoImag4) {
		this.montoImag4 = montoImag4;
	}
	public BigDecimal getMontoImag5() {
		return montoImag5;
	}
	public void setMontoImag5(BigDecimal montoImag5) {
		this.montoImag5 = montoImag5;
	}
	public BigDecimal getMontoImag6() {
		return montoImag6;
	}
	public void setMontoImag6(BigDecimal montoImag6) {
		this.montoImag6 = montoImag6;
	}
	public BigDecimal getMontoImag7() {
		return montoImag7;
	}
	public void setMontoImag7(BigDecimal montoImag7) {
		this.montoImag7 = montoImag7;
	}
	public BigDecimal getMontoImag8() {
		return montoImag8;
	}
	public void setMontoImag8(BigDecimal montoImag8) {
		this.montoImag8 = montoImag8;
	}
	public BigDecimal getMontoImag9() {
		return montoImag9;
	}
	public void setMontoImag9(BigDecimal montoImag9) {
		this.montoImag9 = montoImag9;
	}
	public BigDecimal getMontoImag10() {
		return montoImag10;
	}
	public void setMontoImag10(BigDecimal montoImag10) {
		this.montoImag10 = montoImag10;
	}
	public String getNroOperImag1() {
		return nroOperImag1;
	}
	public void setNroOperImag1(String nroOperImag1) {
		this.nroOperImag1 = nroOperImag1;
	}
	public String getNroOperImag2() {
		return nroOperImag2;
	}
	public void setNroOperImag2(String nroOperImag2) {
		this.nroOperImag2 = nroOperImag2;
	}
	public String getNroOperImag3() {
		return nroOperImag3;
	}
	public void setNroOperImag3(String nroOperImag3) {
		this.nroOperImag3 = nroOperImag3;
	}
	public String getNroOperImag4() {
		return nroOperImag4;
	}
	public void setNroOperImag4(String nroOperImag4) {
		this.nroOperImag4 = nroOperImag4;
	}
	public String getNroOperImag5() {
		return nroOperImag5;
	}
	public void setNroOperImag5(String nroOperImag5) {
		this.nroOperImag5 = nroOperImag5;
	}
	public String getNroOperImag6() {
		return nroOperImag6;
	}
	public void setNroOperImag6(String nroOperImag6) {
		this.nroOperImag6 = nroOperImag6;
	}
	public String getNroOperImag7() {
		return nroOperImag7;
	}
	public void setNroOperImag7(String nroOperImag7) {
		this.nroOperImag7 = nroOperImag7;
	}
	public String getNroOperImag8() {
		return nroOperImag8;
	}
	public void setNroOperImag8(String nroOperImag8) {
		this.nroOperImag8 = nroOperImag8;
	}
	public String getNroOperImag9() {
		return nroOperImag9;
	}
	public void setNroOperImag9(String nroOperImag9) {
		this.nroOperImag9 = nroOperImag9;
	}
	public String getNroOperImag10() {
		return nroOperImag10;
	}
	public void setNroOperImag10(String nroOperImag10) {
		this.nroOperImag10 = nroOperImag10;
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
	public Date getFechaVoucherDialog() {
		return fechaVoucherDialog;
	}
	public void setFechaVoucherDialog(Date fechaVoucherDialog) {
		this.fechaVoucherDialog = fechaVoucherDialog;
	}
	public BigDecimal getMontoVoucherDialog() {
		return montoVoucherDialog;
	}
	public void setMontoVoucherDialog(BigDecimal montoVoucherDialog) {
		this.montoVoucherDialog = montoVoucherDialog;
	}
	public String getNroOperacionVoucherDialog() {
		return nroOperacionVoucherDialog;
	}
	public void setNroOperacionVoucherDialog(String nroOperacionVoucherDialog) {
		this.nroOperacionVoucherDialog = nroOperacionVoucherDialog;
	}
	public BigDecimal getSumaCuotaSI() {
		return sumaCuotaSI;
	}
	public void setSumaCuotaSI(BigDecimal sumaCuotaSI) {
		this.sumaCuotaSI = sumaCuotaSI;
	}
	public BigDecimal getSumaInteres() {
		return sumaInteres;
	}
	public void setSumaInteres(BigDecimal sumaInteres) {
		this.sumaInteres = sumaInteres;
	}
	public BigDecimal getSumaCuotaTotal() {
		return sumaCuotaTotal;
	}
	public void setSumaCuotaTotal(BigDecimal sumaCuotaTotal) {
		this.sumaCuotaTotal = sumaCuotaTotal;
	}
	public List<TipoDocumento> getLstTipoDocumentoNota() {
		return lstTipoDocumentoNota;
	}
	public void setLstTipoDocumentoNota(List<TipoDocumento> lstTipoDocumentoNota) {
		this.lstTipoDocumentoNota = lstTipoDocumentoNota;
	}
	public TipoDocumento getTipoDocumentoNotaSelected() {
		return tipoDocumentoNotaSelected;
	}
	public void setTipoDocumentoNotaSelected(TipoDocumento tipoDocumentoNotaSelected) {
		this.tipoDocumentoNotaSelected = tipoDocumentoNotaSelected;
	}
	public List<SerieDocumento> getLstSerieNotaDocumento() {
		return lstSerieNotaDocumento;
	}
	public void setLstSerieNotaDocumento(List<SerieDocumento> lstSerieNotaDocumento) {
		this.lstSerieNotaDocumento = lstSerieNotaDocumento;
	}
	public SerieDocumento getSerieNotaDocumentoSelected() {
		return serieNotaDocumentoSelected;
	}
	public void setSerieNotaDocumentoSelected(SerieDocumento serieNotaDocumentoSelected) {
		this.serieNotaDocumentoSelected = serieNotaDocumentoSelected;
	}
	public Date getFechaEmisionNotaVenta() {
		return fechaEmisionNotaVenta;
	}
	public void setFechaEmisionNotaVenta(Date fechaEmisionNotaVenta) {
		this.fechaEmisionNotaVenta = fechaEmisionNotaVenta;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getMotivoSunat() {
		return motivoSunat;
	}
	public void setMotivoSunat(String motivoSunat) {
		this.motivoSunat = motivoSunat;
	}
	public SimpleDateFormat getSdfYear() {
		return sdfYear;
	}
	public void setSdfYear(SimpleDateFormat sdfYear) {
		this.sdfYear = sdfYear;
	}
	public MotivoNotaService getMotivoNotaService() {
		return motivoNotaService;
	}
	public void setMotivoNotaService(MotivoNotaService motivoNotaService) {
		this.motivoNotaService = motivoNotaService;
	}
	public MotivoNota getMotivoNotaSelected() {
		return motivoNotaSelected;
	}
	public void setMotivoNotaSelected(MotivoNota motivoNotaSelected) {
		this.motivoNotaSelected = motivoNotaSelected;
	}
	public List<MotivoNota> getLstMotivoNota() {
		return lstMotivoNota;
	}
	public void setLstMotivoNota(List<MotivoNota> lstMotivoNota) {
		this.lstMotivoNota = lstMotivoNota;
	}
	public TipoOperacionService getTipoOperacionService() {
		return tipoOperacionService;
	}
	public void setTipoOperacionService(TipoOperacionService tipoOperacionService) {
		this.tipoOperacionService = tipoOperacionService;
	}
	public IdentificadorService getIdentificadorService() {
		return identificadorService;
	}
	public void setIdentificadorService(IdentificadorService identificadorService) {
		this.identificadorService = identificadorService;
	}
	public TipoOperacion getTipoOperacionSelected() {
		return tipoOperacionSelected;
	}
	public void setTipoOperacionSelected(TipoOperacion tipoOperacionSelected) {
		this.tipoOperacionSelected = tipoOperacionSelected;
	}
	public List<TipoOperacion> getLstTipoOperacion() {
		return lstTipoOperacion;
	}
	public void setLstTipoOperacion(List<TipoOperacion> lstTipoOperacion) {
		this.lstTipoOperacion = lstTipoOperacion;
	}
	public List<Identificador> getLstIdentificador() {
		return lstIdentificador;
	}
	public void setLstIdentificador(List<Identificador> lstIdentificador) {
		this.lstIdentificador = lstIdentificador;
	}
	public Identificador getIdentificadorSelected() {
		return identificadorSelected;
	}
	public void setIdentificadorSelected(Identificador identificadorSelected) {
		this.identificadorSelected = identificadorSelected;
	}
	public String getNumeroNota() {
		return numeroNota;
	}
	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}
	public Integer getNumMuestraImagen() {
		return numMuestraImagen;
	}
	public void setNumMuestraImagen(Integer numMuestraImagen) {
		this.numMuestraImagen = numMuestraImagen;
	}
	public ConsumingPostBoImpl getConsumingPostBo() {
		return consumingPostBo;
	}
	public void setConsumingPostBo(ConsumingPostBoImpl consumingPostBo) {
		this.consumingPostBo = consumingPostBo;
	}
	public List<TipoDocumento> getLstTipoDocumentoEnvioSunat() {
		return lstTipoDocumentoEnvioSunat;
	}
	public void setLstTipoDocumentoEnvioSunat(List<TipoDocumento> lstTipoDocumentoEnvioSunat) {
		this.lstTipoDocumentoEnvioSunat = lstTipoDocumentoEnvioSunat;
	}
	public TipoDocumento getTipoDocumentoEnvioSunat() {
		return tipoDocumentoEnvioSunat;
	}
	public void setTipoDocumentoEnvioSunat(TipoDocumento tipoDocumentoEnvioSunat) {
		this.tipoDocumentoEnvioSunat = tipoDocumentoEnvioSunat;
	}
	public Date getFechaEnvioSunat() {
		return fechaEnvioSunat;
	}
	public void setFechaEnvioSunat(Date fechaEnvioSunat) {
		this.fechaEnvioSunat = fechaEnvioSunat;
	}
	public Boolean getEstadoSunat() {
		return estadoSunat;
	}
	public void setEstadoSunat(Boolean estadoSunat) {
		this.estadoSunat = estadoSunat;
	}
	public Person getPersonSelected() {
		return personSelected;
	}
	public void setPersonSelected(Person personSelected) {
		this.personSelected = personSelected;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
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
	public boolean isPersonaNaturalCliente() {
		return personaNaturalCliente;
	}
	public void setPersonaNaturalCliente(boolean personaNaturalCliente) {
		this.personaNaturalCliente = personaNaturalCliente;
	}
	public String getRucDniCliente() {
		return rucDniCliente;
	}
	public void setRucDniCliente(String rucDniCliente) {
		this.rucDniCliente = rucDniCliente;
	}
	public List<Cliente> getLstCliente() {
		return lstCliente;
	}
	public void setLstCliente(List<Cliente> lstCliente) {
		this.lstCliente = lstCliente;
	}
	public String getRazonSocialText() {
		return razonSocialText;
	}
	public void setRazonSocialText(String razonSocialText) {
		this.razonSocialText = razonSocialText;
	}
	public String getDireccionText() {
		return direccionText;
	}
	public void setDireccionText(String direccionText) {
		this.direccionText = direccionText;
	}
	public String getEmail1Text() {
		return email1Text;
	}
	public void setEmail1Text(String email1Text) {
		this.email1Text = email1Text;
	}
	public String getEmail2Text() {
		return email2Text;
	}
	public void setEmail2Text(String email2Text) {
		this.email2Text = email2Text;
	}
	public String getEmail3Text() {
		return email3Text;
	}
	public void setEmail3Text(String email3Text) {
		this.email3Text = email3Text;
	}
	public SelectItem[] getCdoTipoVenta() {
		return cdoTipoVenta;
	}
	public void setCdoTipoVenta(SelectItem[] cdoTipoVenta) {
		this.cdoTipoVenta = cdoTipoVenta;
	}
	public TipoDocumento getTipoDocumentoFilter() {
		return tipoDocumentoFilter;
	}
	public void setTipoDocumentoFilter(TipoDocumento tipoDocumentoFilter) {
		this.tipoDocumentoFilter = tipoDocumentoFilter;
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
	public Project getProjectFilter() {
		return projectFilter;
	}
	public void setProjectFilter(Project projectFilter) {
		this.projectFilter = projectFilter;
	}
	public BigDecimal getSumaMontoVoucher() {
		return sumaMontoVoucher;
	}
	public void setSumaMontoVoucher(BigDecimal sumaMontoVoucher) {
		this.sumaMontoVoucher = sumaMontoVoucher;
	}
	public List<Producto> getLstProducto() {
		return lstProducto;
	}
	public void setLstProducto(List<Producto> lstProducto) {
		this.lstProducto = lstProducto;
	}
	public Producto getProductoInicial() {
		return productoInicial;
	}
	public void setProductoInicial(Producto productoInicial) {
		this.productoInicial = productoInicial;
	}
	public CuentaBancaria getCtaBanc1() {
		return ctaBanc1;
	}
	public void setCtaBanc1(CuentaBancaria ctaBanc1) {
		this.ctaBanc1 = ctaBanc1;
	}
	public CuentaBancaria getCtaBanc2() {
		return ctaBanc2;
	}
	public void setCtaBanc2(CuentaBancaria ctaBanc2) {
		this.ctaBanc2 = ctaBanc2;
	}
	public CuentaBancaria getCtaBanc3() {
		return ctaBanc3;
	}
	public void setCtaBanc3(CuentaBancaria ctaBanc3) {
		this.ctaBanc3 = ctaBanc3;
	}
	public CuentaBancaria getCtaBanc4() {
		return ctaBanc4;
	}
	public void setCtaBanc4(CuentaBancaria ctaBanc4) {
		this.ctaBanc4 = ctaBanc4;
	}
	public CuentaBancaria getCtaBanc5() {
		return ctaBanc5;
	}
	public void setCtaBanc5(CuentaBancaria ctaBanc5) {
		this.ctaBanc5 = ctaBanc5;
	}
	public CuentaBancaria getCtaBanc6() {
		return ctaBanc6;
	}
	public void setCtaBanc6(CuentaBancaria ctaBanc6) {
		this.ctaBanc6 = ctaBanc6;
	}
	public CuentaBancaria getCtaBanc7() {
		return ctaBanc7;
	}
	public void setCtaBanc7(CuentaBancaria ctaBanc7) {
		this.ctaBanc7 = ctaBanc7;
	}
	public CuentaBancaria getCtaBanc8() {
		return ctaBanc8;
	}
	public void setCtaBanc8(CuentaBancaria ctaBanc8) {
		this.ctaBanc8 = ctaBanc8;
	}
	public CuentaBancaria getCtaBanc9() {
		return ctaBanc9;
	}
	public void setCtaBanc9(CuentaBancaria ctaBanc9) {
		this.ctaBanc9 = ctaBanc9;
	}
	public CuentaBancaria getCtaBanc10() {
		return ctaBanc10;
	}
	public void setCtaBanc10(CuentaBancaria ctaBanc10) {
		this.ctaBanc10 = ctaBanc10;
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
	public String getTipoTransaccion1() {
		return tipoTransaccion1;
	}
	public void setTipoTransaccion1(String tipoTransaccion1) {
		this.tipoTransaccion1 = tipoTransaccion1;
	}
	public String getTipoTransaccion2() {
		return tipoTransaccion2;
	}
	public void setTipoTransaccion2(String tipoTransaccion2) {
		this.tipoTransaccion2 = tipoTransaccion2;
	}
	public String getTipoTransaccion3() {
		return tipoTransaccion3;
	}
	public void setTipoTransaccion3(String tipoTransaccion3) {
		this.tipoTransaccion3 = tipoTransaccion3;
	}
	public String getTipoTransaccion4() {
		return tipoTransaccion4;
	}
	public void setTipoTransaccion4(String tipoTransaccion4) {
		this.tipoTransaccion4 = tipoTransaccion4;
	}
	public String getTipoTransaccion5() {
		return tipoTransaccion5;
	}
	public void setTipoTransaccion5(String tipoTransaccion5) {
		this.tipoTransaccion5 = tipoTransaccion5;
	}
	public String getTipoTransaccion6() {
		return tipoTransaccion6;
	}
	public void setTipoTransaccion6(String tipoTransaccion6) {
		this.tipoTransaccion6 = tipoTransaccion6;
	}
	public String getTipoTransaccion7() {
		return tipoTransaccion7;
	}
	public void setTipoTransaccion7(String tipoTransaccion7) {
		this.tipoTransaccion7 = tipoTransaccion7;
	}
	public String getTipoTransaccion8() {
		return tipoTransaccion8;
	}
	public void setTipoTransaccion8(String tipoTransaccion8) {
		this.tipoTransaccion8 = tipoTransaccion8;
	}
	public String getTipoTransaccion9() {
		return tipoTransaccion9;
	}
	public void setTipoTransaccion9(String tipoTransaccion9) {
		this.tipoTransaccion9 = tipoTransaccion9;
	}
	public String getTipoTransaccion10() {
		return tipoTransaccion10;
	}
	public void setTipoTransaccion10(String tipoTransaccion10) {
		this.tipoTransaccion10 = tipoTransaccion10;
	}
	public String getTipoTransaccionDialog() {
		return tipoTransaccionDialog;
	}
	public void setTipoTransaccionDialog(String tipoTransaccionDialog) {
		this.tipoTransaccionDialog = tipoTransaccionDialog;
	}
	public String getCtaBancDialog() {
		return ctaBancDialog;
	}
	public void setCtaBancDialog(String ctaBancDialog) {
		this.ctaBancDialog = ctaBancDialog;
	}
	public boolean isIncluirIgv() {
		return incluirIgv;
	}
	public void setIncluirIgv(boolean incluirIgv) {
		this.incluirIgv = incluirIgv;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public Imagen getImagenSelected() {
		return imagenSelected;
	}
	public void setImagenSelected(Imagen imagenSelected) {
		this.imagenSelected = imagenSelected;
	}
	public CuentaBancaria getCuentaVoucherDialog() {
		return cuentaVoucherDialog;
	}
	public void setCuentaVoucherDialog(CuentaBancaria cuentaVoucherDialog) {
		this.cuentaVoucherDialog = cuentaVoucherDialog;
	}
	public VoucherTempService getVoucherTempService() {
		return voucherTempService;
	}
	public void setVoucherTempService(VoucherTempService voucherTempService) {
		this.voucherTempService = voucherTempService;
	}
	public CuentaBancaria getCtaBanc11() {
		return ctaBanc11;
	}
	public void setCtaBanc11(CuentaBancaria ctaBanc11) {
		this.ctaBanc11 = ctaBanc11;
	}
	public CuentaBancaria getCtaBanc12() {
		return ctaBanc12;
	}
	public void setCtaBanc12(CuentaBancaria ctaBanc12) {
		this.ctaBanc12 = ctaBanc12;
	}
	public CuentaBancaria getCtaBanc13() {
		return ctaBanc13;
	}
	public void setCtaBanc13(CuentaBancaria ctaBanc13) {
		this.ctaBanc13 = ctaBanc13;
	}
	public CuentaBancaria getCtaBanc14() {
		return ctaBanc14;
	}
	public void setCtaBanc14(CuentaBancaria ctaBanc14) {
		this.ctaBanc14 = ctaBanc14;
	}
	public CuentaBancaria getCtaBanc15() {
		return ctaBanc15;
	}
	public void setCtaBanc15(CuentaBancaria ctaBanc15) {
		this.ctaBanc15 = ctaBanc15;
	}
	public Date getFechaImag11() {
		return fechaImag11;
	}
	public void setFechaImag11(Date fechaImag11) {
		this.fechaImag11 = fechaImag11;
	}
	public Date getFechaImag12() {
		return fechaImag12;
	}
	public void setFechaImag12(Date fechaImag12) {
		this.fechaImag12 = fechaImag12;
	}
	public Date getFechaImag13() {
		return fechaImag13;
	}
	public void setFechaImag13(Date fechaImag13) {
		this.fechaImag13 = fechaImag13;
	}
	public Date getFechaImag14() {
		return fechaImag14;
	}
	public void setFechaImag14(Date fechaImag14) {
		this.fechaImag14 = fechaImag14;
	}
	public Date getFechaImag15() {
		return fechaImag15;
	}
	public void setFechaImag15(Date fechaImag15) {
		this.fechaImag15 = fechaImag15;
	}
	public BigDecimal getMontoImag11() {
		return montoImag11;
	}
	public void setMontoImag11(BigDecimal montoImag11) {
		this.montoImag11 = montoImag11;
	}
	public BigDecimal getMontoImag12() {
		return montoImag12;
	}
	public void setMontoImag12(BigDecimal montoImag12) {
		this.montoImag12 = montoImag12;
	}
	public BigDecimal getMontoImag13() {
		return montoImag13;
	}
	public void setMontoImag13(BigDecimal montoImag13) {
		this.montoImag13 = montoImag13;
	}
	public BigDecimal getMontoImag14() {
		return montoImag14;
	}
	public void setMontoImag14(BigDecimal montoImag14) {
		this.montoImag14 = montoImag14;
	}
	public BigDecimal getMontoImag15() {
		return montoImag15;
	}
	public void setMontoImag15(BigDecimal montoImag15) {
		this.montoImag15 = montoImag15;
	}
	public String getNroOperImag11() {
		return nroOperImag11;
	}
	public void setNroOperImag11(String nroOperImag11) {
		this.nroOperImag11 = nroOperImag11;
	}
	public String getNroOperImag12() {
		return nroOperImag12;
	}
	public void setNroOperImag12(String nroOperImag12) {
		this.nroOperImag12 = nroOperImag12;
	}
	public String getNroOperImag13() {
		return nroOperImag13;
	}
	public void setNroOperImag13(String nroOperImag13) {
		this.nroOperImag13 = nroOperImag13;
	}
	public String getNroOperImag14() {
		return nroOperImag14;
	}
	public void setNroOperImag14(String nroOperImag14) {
		this.nroOperImag14 = nroOperImag14;
	}
	public String getNroOperImag15() {
		return nroOperImag15;
	}
	public void setNroOperImag15(String nroOperImag15) {
		this.nroOperImag15 = nroOperImag15;
	}
	public String getTipoTransaccion11() {
		return tipoTransaccion11;
	}
	public void setTipoTransaccion11(String tipoTransaccion11) {
		this.tipoTransaccion11 = tipoTransaccion11;
	}
	public String getTipoTransaccion12() {
		return tipoTransaccion12;
	}
	public void setTipoTransaccion12(String tipoTransaccion12) {
		this.tipoTransaccion12 = tipoTransaccion12;
	}
	public String getTipoTransaccion13() {
		return tipoTransaccion13;
	}
	public void setTipoTransaccion13(String tipoTransaccion13) {
		this.tipoTransaccion13 = tipoTransaccion13;
	}
	public String getTipoTransaccion14() {
		return tipoTransaccion14;
	}
	public void setTipoTransaccion14(String tipoTransaccion14) {
		this.tipoTransaccion14 = tipoTransaccion14;
	}
	public String getTipoTransaccion15() {
		return tipoTransaccion15;
	}
	public void setTipoTransaccion15(String tipoTransaccion15) {
		this.tipoTransaccion15 = tipoTransaccion15;
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
	
	

}
