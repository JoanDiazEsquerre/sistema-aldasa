package com.model.aldasa.proyecto.bean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.primefaces.PrimeFaces;
import org.primefaces.component.column.Column;
import org.primefaces.context.PrimeRequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.ContratoArchivo;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DetalleImagenLote;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.ImagenPlantillaVenta;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.ObservacionContrato;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.ProyectoPartida;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Simulador;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.proyecto.jrdatasource.DataSourceCronogramaPago;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.ContratoArchivoService;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.CuotaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DetalleImagenLoteService;
import com.model.aldasa.service.DocumentoVentaService;
import com.model.aldasa.service.ImagenPlantillaVentaService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.ObservacionContratoService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.PlantillaVentaService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProyectoPartidaService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.service.VoucherService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoContrato;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.EstadoRequerimientoSeparacionType;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.Perfiles;
import com.model.aldasa.util.UtilXls;
import com.model.aldasa.ventas.bean.LoadImageDocumentoBean;
import java.time.LocalDate;
import java.time.Period;

import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.ChartData;

@ManagedBean
@ViewScoped
public class ContratoBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{contratoService}")
	private ContratoService contratoService;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{cuotaService}")
	private CuotaService cuotaService;
	
	@ManagedProperty(value = "#{requerimientoSeparacionService}")
	private RequerimientoSeparacionService requerimientoSeparacionService;
	
	@ManagedProperty(value = "#{voucherService}")
	private VoucherService voucherService;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	
	@ManagedProperty(value = "#{reportGenBo}")
	private ReportGenBo reportGenBo;
	
	@ManagedProperty(value = "#{observacionContratoService}")
	private ObservacionContratoService observacionContratoService;
	
	@ManagedProperty(value = "#{plantillaVentaService}")
	private PlantillaVentaService plantillaVentaService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	@ManagedProperty(value = "#{loadImageDocumentoBean}")
	private LoadImageDocumentoBean loadImageDocumentoBean;
	
	@ManagedProperty(value = "#{proyectoPartidaService}")
	private ProyectoPartidaService proyectoPartidaService;
	
	@ManagedProperty(value = "#{imagenPlantillaVentaService}")
	private ImagenPlantillaVentaService imagenPlantillaVentaService;
	
	@ManagedProperty(value = "#{documentoVentaService}")
	private DocumentoVentaService documentoVentaService;
	
	@ManagedProperty(value = "#{detalleImagenLoteService}")
	private DetalleImagenLoteService detalleImagenLoteService;
	
	@ManagedProperty(value = "#{contratoArchivoService}")
	private ContratoArchivoService contratoArchivoService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	
	private String meses[]= {"ENERO","FEBRERO","MARZO","ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE","DICIEMBRE"};
	
	private LazyDataModel<Contrato> lstContratoLazy;
	private LazyDataModel<Lote> lstLotesSinContratoLazy;

	
	private List<Person> lstPerson;
	private List<CuentaBancaria> lstCuentaBancaria = new ArrayList<>();
	private List<Simulador> lstSimulador = new ArrayList<>();
	private List<Simulador> lstSimuladorPrevio = new ArrayList<>();
	private List<ObservacionContrato> lstObservacionContrato = new ArrayList<>();
	private List<Usuario> lstUsuarioCobranza;
	private List<Cuota> lstCuotaVista = new ArrayList<>();
	private List<Project> lstProject, lstProyectosCambio;
	private List<Imagen> lstImagenVoucher;
	private List<ObservacionContrato> lstObsContrato = new ArrayList<>();
	private List<ContratoArchivo> lstContratoArchivoSelected;
	private List<Manzana> lstManzanasCambio;
	private List<Lote> lstLotesCambio;

	private Lote loteSelected, loteCambio;
	private Contrato contratoSelected;
	private ObservacionContrato obsSelected;
	private Project projectFilter, projectFilterLote, proyectoCambio;
	private ContratoArchivo archivoSelected;
	private Manzana manzanaCambio;

	
	private StreamedContent fileDes, fileDesVoucher, fileResumen;
	private String nombreArchivo = "Contrato.docx";
	private String nombreArchivoReporte = "Reporte Contratos.xlsx";
	private String nombreArchivoReporteVoucher = "Reporte Voucher.xlsx";
	private String nombreArchivoObsContrato = "Reporte Observaciones Contratos.xlsx";
	private String nombreResumenContrato = "Resumen Ventas Contratos.xlsx";

	private String nombreLoteSelected;
	private Date fechaVenta, fechaPrimeraCuota, fechaCompromiso; 
	private Date fechaIniObs = new Date(); 
	private Date fechaFinObs = new Date();
	private Person persona1, persona2, persona3, persona4, persona5;
	private BigDecimal montoVenta, montoInicial, interes; 
	private BigDecimal sumaCuotaSI, sumaInteres, sumaCuotaTotal, montoCuotaEspecial;
	private String tipoPago ="";
	private String observacion ="";
	private int nroCuotas, totalCuotaEspecial, year, month;
	private Integer hastaCuotaEspecial;
	private String estado, nombreArchivoUpload;
	private boolean cuotaEspecial= false, cuotaEspecialFilter=false, compromisoPago=false;
	private String compromisoPagoFilter;
	
	private NumeroALetra numeroAletra = new NumeroALetra();
	
	private Map<String, Object> parametros;
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

    private DataSourceCronogramaPago dt; 
    private UploadedFile file;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");  
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy"); 
	SimpleDateFormat sdfDay = new SimpleDateFormat("dd"); 
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat sdfQuery = new SimpleDateFormat("yyyy-MM-dd");  
	
	@PostConstruct
	public void init() {
		lstProyectosCambio = projectService.findByStatusOrderByNameAsc(true);
		compromisoPagoFilter="%%";
		lstProject= projectService.findByStatusAndSucursalOrderByNameAsc(true, navegacionBean.getSucursalLogin());
		estado = EstadoContrato.ACTIVO.getName();
		lstUsuarioCobranza = usuarioService.findByProfileIdAndStatus(Perfiles.ASISTENTE_COBRANZA.getId(), true);
		iniciarLazy();
		listarPersonas();
		lstCuentaBancaria = cuentaBancariaService.findByEstado(true);
		observacion="";
		
		iniciarLotesSinContratoLazy();
		sumarCuotasEspeciales();
		
		month = 0;
		year = 0;
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
		
		if(contratoSelected.getLote().getId().equals(loteCambio.getId())) {
			addErrorMessage("Has seleccionado el mismo lote."); 
			return;
		}
		
		Contrato contratoActivo = contratoService.findByLoteAndEstado(loteCambio, EstadoContrato.ACTIVO.getName());
		Contrato contratoTerminado = contratoService.findByLoteAndEstado(loteCambio, EstadoContrato.TERMINADO.getName());
		
		if(contratoActivo != null || contratoTerminado != null) {
			addErrorMessage("Ya existe un contrato ACTIVO o TERMINADO con el lote seleccionado.");
			return;
		}
		
	
		Lote loteNuevo = loteService.findById(loteCambio.getId());
		
		if(loteNuevo.getStatus().equals(EstadoLote.SEPARADO.getName()) || loteNuevo.getStatus().equals(EstadoLote.VENDIDO.getName())) {
			addErrorMessage("El lote al que deseas cambiar ya se encuentra "+ loteNuevo.getStatus().toUpperCase()); 
			return;
		}
		
		
		contratoSelected.getLote().setStatus(EstadoLote.DISPONIBLE.getName()); 
		loteService.save(contratoSelected.getLote());
		
		contratoSelected.setLote(loteCambio);
		loteCambio.setStatus(EstadoLote.VENDIDO.getName());
		loteService.save(loteCambio);
		
		contratoService.save(contratoSelected);
		
		addInfoMessage("Se cambio de lote correctamente.");
		PrimeFaces.current().executeScript("PF('cambiarLoteDialog').hide();");
		
	}
	
	public void listarLotes() {
		if(manzanaCambio != null) {
			lstLotesCambio = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyectoCambio, manzanaCambio, "%%");
		}else {
			lstLotesCambio = new ArrayList<>();
		}
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
	
	public void actualizarCuotasAtrasadas() {
		List<Contrato> lstContratos = contratoService.findByEstado(EstadoContrato.ACTIVO.getName());
		for(Contrato c : lstContratos) {
	
			int numCuotasAtrasadas=0;
			
			List<Cuota> lstcuotas = cuotaService.findByContratoAndEstado(c, true);
			if(!lstcuotas.isEmpty()) {
				for(Cuota cuota : lstcuotas) {
					if(cuota.getNroCuota()!=0) { 
						if(cuota.getPagoTotal().equals("N") && !cuota.isPrepago()) {	
							if(cuota.getFechaPago().before(new Date())) {
								numCuotasAtrasadas++;
								
							}
						}
					}
				
				}
			}
			
			if(numCuotasAtrasadas != c.getCuotasAtrasadas()) {
				c.setCuotasAtrasadas(numCuotasAtrasadas);
				contratoService.save(c);
			}
		
		}	
		
		addInfoMessage("Se actualizó las cuotas atrasadas de los contratos "+ estado);
 		
	}
	
	public void sumarCuotasEspeciales() {
		totalCuotaEspecial = contratoService.totalCuotasEspeciales(estado);
	}
	
	public void eliminarArchivo() {
        File archivo = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaArchivoContrato() + archivoSelected.getNombreArchivo());
        
        if (archivo.exists()) {
            if (archivo.delete()) {
            	
            	archivoSelected.setEstado(false);
            	contratoArchivoService.save(archivoSelected);
            	
            	listarArchivosContrato();
            	
            	if(archivoSelected.isCompromisoPago()) {
            		contratoSelected.setCompromisoPago("NO");
                	contratoSelected.setFechaVencimientoComp(null);
                	contratoService.save(contratoSelected);
            	}	
            	
            	addInfoMessage("El archivo ha sido eliminado correctamente.");
            } else {
            	addErrorMessage("No se pudo eliminar el archivo.");
            }
        } else {
        	addErrorMessage("El archivo no existe.");
        }
    }
	
	public void subirImagenes() {
		if(file == null) {
			addErrorMessage("Por favor seleccionar un archivo."); 
			return;
		}
		
		if(nombreArchivoUpload.equals("")) {
			addErrorMessage("Ingresar un nombre para el archivo.");
			return;
		}else {
			nombreArchivoUpload = nombreArchivoUpload.trim();
			
			ContratoArchivo busqueda = contratoArchivoService.findByEstadoAndNombre(true, nombreArchivoUpload);
			if(busqueda!=null) {
				addErrorMessage("Ya existe un archivo con el mismo nombre, por favor intenta agregando los datos del lote.");
				return;
			}
			
		}
		
		if(compromisoPago) {
			if(fechaCompromiso==null) {
				addErrorMessage("Seleccionar una fecha de vencimiento del compromiso.");
				return;
			}
		}
		
		String rename = nombreArchivoUpload + "." + getExtension(file.getFileName()); 
		
		ContratoArchivo contratoArchivo = new ContratoArchivo();
		contratoArchivo.setContrato(contratoSelected);
		contratoArchivo.setNombreArchivo(rename);
		contratoArchivo.setNombre(nombreArchivoUpload);
		contratoArchivo.setEstado(true);
		contratoArchivo.setCompromisoPago(compromisoPago); 
		contratoArchivo.setFechaVencimiento(compromisoPago?fechaCompromiso:null);
		contratoArchivoService.save(contratoArchivo);
		
		if(compromisoPago) {
			contratoSelected.setCompromisoPago("SI");
			contratoSelected.setFechaVencimientoComp(fechaCompromiso);
			contratoService.save(contratoSelected);
		}
		
        subirArchivo(rename, file);
        listarArchivosContrato();
        nombreArchivoUpload= "";
        file=null;
        compromisoPago=false;
//        addInfoMessage("Se subío correctamente el archivo");
            
		
	}
	
	public void subirArchivo(String nombre, UploadedFile file) {
		//  File result = new File("/home/imagen/IMG-DOCUMENTO-VENTA/" + nombre);
		//  File result = new File("C:\\IMG-DOCUMENTO-VENTA\\" + nombre);
	  File result = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaArchivoContrato() + nombre);
	
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
		
		      addInfoMessage("El archivo subió correctamente.");
		
		  } catch (IOException e) {
		      e.printStackTrace();     
		      addErrorMessage("El archivo no se pudo subir.");
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
	
	public void listarArchivosContrato() {
		lstContratoArchivoSelected = contratoArchivoService.findByEstadoAndContrato(true, contratoSelected);
	}
	
	public void procesarExcelObsContrato() {
		PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').show();"); 
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Observacion Contrato");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("PROYECTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("MANZANA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("LOTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("OBSERVACIÓN");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("FECHA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("USUARIO");cellSub1.setCellStyle(styleTitulo);
		
		int index = 1;
		
		fechaIniObs.setHours(0);
		fechaIniObs.setMinutes(0);
		fechaIniObs.setSeconds(0);
        fechaFinObs.setHours(23);
        fechaFinObs.setMinutes(59);
        fechaFinObs.setSeconds(59);
		
		lstObsContrato = observacionContratoService.findByEstadoAndFechaRegistroBetween(true, fechaIniObs, fechaFinObs);
		
		if (!lstObsContrato.isEmpty()) {
			for (ObservacionContrato d : lstObsContrato) {
				
				
				rowSubTitulo = sheet.createRow(index);
				cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(d.getContrato().getLote().getProject().getName());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(d.getContrato().getLote().getManzana().getName());cellSub1.setCellStyle(styleBorder);				
				cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(d.getContrato().getLote().getNumberLote());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(d.getObservacion() == null ? "" : d.getObservacion());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(d.getFechaRegistro() == null ? "" : sdfFull.format(d.getFechaRegistro()));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(d.getUsuario() == null ? "" : d.getUsuario().getUsername());cellSub1.setCellStyle(styleBorder);
				
				
				index++;
			}
		}
		
		
		for (int j = 0; j <= 5; j++) {
			sheet.autoSizeColumn(j);
			
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivoObsContrato);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivoObsContrato).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivoObsContrato))
					.build();
			
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 


		} catch (FileNotFoundException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		} catch (IOException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		}

	}
	
	public void procesarResumenContrato() {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Resumen Venta Contrato");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("PROYECTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("MANZANA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("LOTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("FECHA VENTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("TIPO VENTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("MONTO VENTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("INICIAL");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("NUMERO CUOTAS");cellSub1.setCellStyle(styleTitulo); 
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("CLIENTE(S)");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("DNI");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("DIRECCION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue("CORREO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("TELEFONO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue("CELULAR");cellSub1.setCellStyle(styleTitulo);
		
		int index = 1;
		
        List<Contrato> lstContrato = new ArrayList<>();
        
        if(year==0 && month ==0) {
        	lstContrato = contratoService.findByEstado(estado);
        }
        if(year!=0 && month ==0) {
        	lstContrato = contratoService.findByEstadoAndFechaVentaYear(estado,year);
        }
        if(year==0 && month !=0) {
        	lstContrato = contratoService.findByEstadoAndFechaVentaMonth(estado, month);
        }
        if(year!=0 && month !=0) {
        	lstContrato = contratoService.findByEstadoAndFechaVentaYearAndFechaVentaMonth(estado, year, month);
        }
        
		
		if (!lstContrato.isEmpty()) {
			for (Contrato d : lstContrato) {
				
				
				rowSubTitulo = sheet.createRow(index);
				cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(d.getLote().getProject().getName());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(d.getLote().getManzana().getName());cellSub1.setCellStyle(styleBorder);				
				cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(d.getLote().getNumberLote());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(sdf.format(d.getFechaVenta()));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(d.getTipoPago());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(d.getMontoVenta()+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(d.getMontoInicial()+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(d.getNumeroCuota());cellSub1.setCellStyle(styleBorder);
				
				String nomClientes = d.getPersonVenta().getSurnames()+" "+d.getPersonVenta().getNames()+" \n";
				if(d.getPersonVenta2() != null) nomClientes = nomClientes+ d.getPersonVenta2().getSurnames()+" "+d.getPersonVenta2().getNames()+" \n";
				if(d.getPersonVenta3() != null) nomClientes = nomClientes+ d.getPersonVenta3().getSurnames()+" "+d.getPersonVenta3().getNames()+" \n";
				if(d.getPersonVenta4() != null) nomClientes = nomClientes+ d.getPersonVenta4().getSurnames()+" "+d.getPersonVenta4().getNames()+" \n";
				if(d.getPersonVenta5() != null) nomClientes = nomClientes+ d.getPersonVenta5().getSurnames()+" "+d.getPersonVenta5().getNames()+" \n";
				
				String dniClientes = d.getPersonVenta().getDni()+" \n";
				if(d.getPersonVenta2() != null) dniClientes = dniClientes+ d.getPersonVenta2().getDni()+" \n";
				if(d.getPersonVenta3() != null) dniClientes = dniClientes+ d.getPersonVenta3().getDni()+" \n";
				if(d.getPersonVenta4() != null) dniClientes = dniClientes+ d.getPersonVenta4().getDni()+" \n";
				if(d.getPersonVenta5() != null) dniClientes = dniClientes+ d.getPersonVenta5().getDni()+" \n";
				
				String direccionClientes = d.getPersonVenta().getAddress()+" \n";
				if(d.getPersonVenta2() != null) direccionClientes = direccionClientes+ d.getPersonVenta2().getAddress()+" \n";
				if(d.getPersonVenta3() != null) direccionClientes = direccionClientes+ d.getPersonVenta3().getAddress()+" \n";
				if(d.getPersonVenta4() != null) direccionClientes = direccionClientes+ d.getPersonVenta4().getAddress()+" \n";
				if(d.getPersonVenta5() != null) direccionClientes = direccionClientes+ d.getPersonVenta5().getAddress()+" \n";
				
				String correoClientes = d.getPersonVenta().getEmail()+" \n";
				if(d.getPersonVenta2() != null) correoClientes = correoClientes+ d.getPersonVenta2().getEmail()+" \n";
				if(d.getPersonVenta3() != null) correoClientes = correoClientes+ d.getPersonVenta3().getEmail()+" \n";
				if(d.getPersonVenta4() != null) correoClientes = correoClientes+ d.getPersonVenta4().getEmail()+" \n";
				if(d.getPersonVenta5() != null) correoClientes = correoClientes+ d.getPersonVenta5().getEmail()+" \n";
				
				String telefonoClientes = d.getPersonVenta().getPhone()+" \n";
				if(d.getPersonVenta2() != null) telefonoClientes = telefonoClientes+ d.getPersonVenta2().getPhone()+" \n";
				if(d.getPersonVenta3() != null) telefonoClientes = telefonoClientes+ d.getPersonVenta3().getPhone()+" \n";
				if(d.getPersonVenta4() != null) telefonoClientes = telefonoClientes+ d.getPersonVenta4().getPhone()+" \n";
				if(d.getPersonVenta5() != null) telefonoClientes = telefonoClientes+ d.getPersonVenta5().getPhone()+" \n";
				
				String celularClientes = d.getPersonVenta().getCellphone()+" \n";
				if(d.getPersonVenta2() != null) celularClientes = celularClientes+ d.getPersonVenta2().getCellphone()+" \n";
				if(d.getPersonVenta3() != null) celularClientes = celularClientes+ d.getPersonVenta3().getCellphone()+" \n";
				if(d.getPersonVenta4() != null) celularClientes = celularClientes+ d.getPersonVenta4().getCellphone()+" \n";
				if(d.getPersonVenta5() != null) celularClientes = celularClientes+ d.getPersonVenta5().getCellphone()+" \n";
				
				
				
				cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(nomClientes);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(dniClientes);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue(direccionClientes);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue(correoClientes);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue(telefonoClientes);cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue(celularClientes);cellSub1.setCellStyle(styleBorder);
				
				
				index++;
			}
		}
		
		
		for (int j = 0; j <= 13; j++) {
			sheet.autoSizeColumn(j);
			
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreResumenContrato);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileResumen = DefaultStreamedContent.builder().name(nombreResumenContrato).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreResumenContrato))
					.build();
			
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 


		} catch (FileNotFoundException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		} catch (IOException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		}

	}
	
	public String convertirHoraFull(Date hora) {
		String a = "";
		if(hora != null) {
			a = sdfFull.format(hora);
		}
		
		return a;
	}
	
	public void verVouchersPago() {
		loadImageDocumentoBean.setNombreArchivo("0.png");
		lstImagenVoucher = new ArrayList<>();
		List<DocumentoVenta> lstDocumentoVentaVoucher = new ArrayList<>();
		
//		List<Imagen> lstImagenLote = imagenService.findByLoteAndEstado(contratoSelected.getLote(), true);
//		if(!lstImagenLote.isEmpty()) {
//			lstImagenVoucher.addAll(lstImagenLote);
//		}
		
		List<DetalleImagenLote> lstDetalleImagen =  detalleImagenLoteService.findByEstadoAndLoteAndImagenEstadoAndImagenResuelto(true, contratoSelected.getLote(), true, false); 
		if(!lstDetalleImagen.isEmpty()) {
			for(DetalleImagenLote det : lstDetalleImagen) {
				lstImagenVoucher.add(det.getImagen());
			}
			
		}
		
		
		List<DetalleDocumentoVenta> lstDetalleDocumentoVenta = detalleDocumentoVentaService.findByDocumentoVentaEstadoAndCuotaContratoOrderByCuotaContratoIdAsc(true, contratoSelected);
		List<DetalleDocumentoVenta> lstDetalleDocumentoVenta2 = detalleDocumentoVentaService.findByDocumentoVentaEstadoAndCuotaPrepagoContratoOrderByCuotaContratoIdAsc(true, contratoSelected);
		
		lstDetalleDocumentoVenta.addAll(lstDetalleDocumentoVenta2);
		
		
//		if(lstDetalleDocumentoVenta.isEmpty()) {
//			addWarnMessage("No se ha encontrado ningun documento de venta."); 
//			return;
//		}
		
		for(DetalleDocumentoVenta det1: lstDetalleDocumentoVenta) {
			if(lstDocumentoVentaVoucher.isEmpty()) {
				if(!det1.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("C") && !det1.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("D")) {
					if(det1.getDocumentoVenta().getNotacredito()==null &&  det1.getDocumentoVenta().getNotaDebito()==null) {
						lstDocumentoVentaVoucher.add(det1.getDocumentoVenta());
					}else {
						if(det1.getDocumentoVenta().getNotacredito()!=null) {
							if(det1.getDocumentoVenta().getNotacredito()==false) {
								lstDocumentoVentaVoucher.add(det1.getDocumentoVenta());
								continue;
							}
							
						}
						if(det1.getDocumentoVenta().getNotaDebito()!=null) {
							if(det1.getDocumentoVenta().getNotaDebito()==false) { 
								lstDocumentoVentaVoucher.add(det1.getDocumentoVenta());
								continue;
							}
						}
						
					}
					
				}
				
			}else {
				boolean encuentra = false;
				for(DocumentoVenta dv : lstDocumentoVentaVoucher) {
					if(dv.getId().equals(det1.getDocumentoVenta().getId())) {
						encuentra = true;
					}
				}
				
				if(!encuentra) {
					if(!det1.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("C") && !det1.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("D")) {
						if(det1.getDocumentoVenta().getNotacredito()==null &&  det1.getDocumentoVenta().getNotaDebito()==null) {
							lstDocumentoVentaVoucher.add(det1.getDocumentoVenta());
						}else {
							if(det1.getDocumentoVenta().getNotacredito()!=null) {
								if(det1.getDocumentoVenta().getNotacredito()==false) {
									lstDocumentoVentaVoucher.add(det1.getDocumentoVenta());
									continue;
								}
								
							}
							if(det1.getDocumentoVenta().getNotaDebito()!=null) {
								if(det1.getDocumentoVenta().getNotaDebito()==false) { 
									lstDocumentoVentaVoucher.add(det1.getDocumentoVenta());
									continue;
								}
							}
							
						}
						
					}
				}
			}
		}
		
		for(DocumentoVenta d : lstDocumentoVentaVoucher) {
			List<Imagen> lstImagenDV = imagenService.findByDocumentoVentaAndEstadoAndResuelto(d, true, false);
			if(!lstImagenDV.isEmpty()) {
				lstImagenVoucher.addAll(lstImagenDV);
			}
		}
		
		PrimeFaces.current().executeScript("PF('voucherDialog').show();"); 
		
	}
	
	public String obtenerDetalleBoleta(DocumentoVenta documentoVenta) {
		String detalle="";
		if(documentoVenta!=null) {
			
			List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
			
			int cont = 0;
			for(DetalleDocumentoVenta det: lstDet) {
				if(cont !=0) {
					detalle = detalle+ " \n";
				}
				
				detalle = detalle + det.getDescripcion()+".";
				cont++;
			}
			
			
		}
		
		
		return detalle;
	}
	
	public void actualizarUsuarioCobranza(Contrato contrato) {
        contratoService.save(contrato);
        addInfoMessage("Se actualizó el Usuario de Cobranza correctamente.");
    }
	
	public void actualizarPersonaContrato() {
        contratoService.save(contratoSelected);
        PrimeFaces.current().executeScript("PF('cambiarTitularDialog').hide();");
        addInfoMessage("Se actualizó los titulares del contrato.");
    }
	
	
	public void procesarExcel() {		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Reporte Contratos");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("PROYECTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("MANZANA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("LOTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("CLIENTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("DNI");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("FECHA PRIMERO CUOTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("FECHA PROX CUOTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("IMPORTE PROX CUOTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("CUOTAS PROGRAMADAS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("MESES TRANSCURRIDOS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("CUOTAS PAGADAS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue("CUOTAS ATRADASAS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("MONTO CUOTAS PAGADAS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue("MONTO CUOTAS ATRASADAS");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue("SALDO PENDIENTE");cellSub1.setCellStyle(styleTitulo); 
		cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue("ASESOR COBRANZA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(16);cellSub1.setCellValue("MINUTA LEGALIZADA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(17);cellSub1.setCellValue("MINUTA AUTORIZADA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(18);cellSub1.setCellValue("ESCRITURA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(19);cellSub1.setCellValue("INDEPENDIZACION");cellSub1.setCellStyle(styleTitulo);

		List<Contrato> lstContratos = contratoService.findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(EstadoContrato.ACTIVO.getName(), navegacionBean.getSucursalLogin(), "Crédito", false); 
		
		int index = 1;
		BigDecimal total = BigDecimal.ZERO;
		
		if (!lstContratos.isEmpty()){
			for(Contrato c : lstContratos) {
				int numCuotasProg = 0;
				int numCuotasPagadas=0;
				int numCuotasPendientes=0;
				int numCuotasAtrasadas=0;
				
				Date fechaProxCuota = null;
				BigDecimal montoProxCuota = BigDecimal.ZERO;
				BigDecimal montoPagado = BigDecimal.ZERO;
				BigDecimal montoAtrasado = BigDecimal.ZERO;
				BigDecimal montoPendiente = BigDecimal.ZERO;
				
				LocalDate fecha1 = c.getFechaPrimeraCuota().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
		        LocalDate fecha2 = new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

		        // Calcula el período entre las dos fechas
		        Period periodo = Period.between(fecha1, fecha2);

		        // Calcula los meses transcurridos
		        int mesesTranscurridos = periodo.getYears() * 12 + periodo.getMonths();				
				List<Cuota> lstcuotas = cuotaService.findByContratoAndEstado(c, true);
				List<Cuota> lstcuotasPendientes = new ArrayList<>();
				List<Cuota> lstcuotasPagadas = new ArrayList<>();
				
				System.out.println("tmaño cuota: "+lstcuotas.size());
				if(!lstcuotas.isEmpty()) {
					for(Cuota cuota : lstcuotas) {
						if(cuota.getNroCuota()!=0) {
							numCuotasProg++;
							
							if(cuota.getPagoTotal().equals("S") && !cuota.isPrepago()) {
								numCuotasPagadas++;
								montoPagado = montoPagado.add(cuota.getCuotaTotal());
							}
							
							if(cuota.getPagoTotal().equals("N") && !cuota.isPrepago()) {
								numCuotasPendientes++;
								montoPendiente = montoPendiente.add(cuota.getCuotaTotal().subtract(cuota.getAdelanto()));
								
								
								if(fechaProxCuota==null) {
									fechaProxCuota = cuota.getFechaPago();
									montoProxCuota = cuota.getCuotaTotal();
								}
								
								if(cuota.getFechaPago().before(new Date())) {
									numCuotasAtrasadas++;
									montoAtrasado = montoAtrasado.add(cuota.getCuotaTotal().subtract(cuota.getAdelanto()));
								}
							}
							
							if(cuota.getPagoTotal().equals("S") && cuota.isPrepago()) {
								montoPagado = montoPagado.add(cuota.getCuotaTotal());
							}
						}
					}
				}
				
				total = total.add(montoPendiente);
				
				
				rowSubTitulo = sheet.createRow(index);
				cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(c.getLote().getProject().getName());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(c.getLote().getManzana().getName());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(c.getLote().getNumberLote());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(c.getPersonVenta().getSurnames()+" "+ c.getPersonVenta().getNames());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(c.getPersonVenta().getDni());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(sdf.format(c.getFechaPrimeraCuota()));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(fechaProxCuota==null?"": sdf.format(fechaProxCuota));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(montoProxCuota+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(numCuotasProg+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(mesesTranscurridos+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue(numCuotasPagadas+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue(numCuotasAtrasadas+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue(montoPagado.doubleValue());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue(montoAtrasado.doubleValue());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue(montoPendiente.doubleValue());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue(c.getUsuarioCobranza() == null? "": c.getUsuarioCobranza().getUsername());cellSub1.setCellStyle(styleBorder);
					
				cellSub1 = rowSubTitulo.createCell(16);cellSub1.setCellValue(c.isMinutaLegalizada()?"SI":"NO");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(17);cellSub1.setCellValue(c.isMinutaAutorizada()?"SI":"NO");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(18);cellSub1.setCellValue(c.isEscritura()?"SI":"NO");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(19);cellSub1.setCellValue(c.isIndependizacion()?"SI":"NO");cellSub1.setCellStyle(styleBorder);

				index++;
			}
		}
		
		rowSubTitulo = sheet.createRow(index);
		cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue(total.doubleValue());cellSub1.setCellStyle(styleBorder);
		
		
		for (int j = 0; j <= 19; j++) {
			sheet.autoSizeColumn(j);
			
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivoReporte);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivoReporte).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivoReporte))
					.build();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void procesarExcelVoucher() {		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Reporte Voucher");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("N° CUENTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("MONTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("BANCO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("FECHA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("N° OPERACION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("DOCUMENTO DE VENTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("DETALLE DOCUMENTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("COMENTARIO");cellSub1.setCellStyle(styleTitulo);
		
		
		int index = 1;
		for(Imagen c : lstImagenVoucher) {
			rowSubTitulo = sheet.createRow(index);
			cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(c.getCuentaBancaria().getNumero());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(c.getMonto()+"");cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(c.getCuentaBancaria().getBanco().getNombre());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(c.getFecha()==null ? "" : sdfFull.format(c.getFecha()));cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(c.getNumeroOperacion());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(c.getDocumentoVenta() !=null? c.getDocumentoVenta().getSerie()+"-"+c.getDocumentoVenta().getNumero():"");cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(c.getDocumentoVenta() !=null? obtenerDetalleBoleta(c.getDocumentoVenta()): "");cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(c.getComentario() !=null? c.getComentario() : "");cellSub1.setCellStyle(styleBorder);
			
			
			index++;
		}
		
		
		rowSubTitulo = sheet.createRow(index);
		
		
		for (int j = 0; j <= 7; j++) {
			sheet.autoSizeColumn(j);
			
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivoReporteVoucher);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDesVoucher = DefaultStreamedContent.builder().name(nombreArchivoReporteVoucher).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivoReporteVoucher))
					.build();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cambiarEstadoFirma(Contrato contrato) {
		if(contrato.isFirma()) {
			contrato.setFirma(false);
		}else {
			contrato.setFirma(true);
		}
		Contrato cambioFirma = contratoService.save(contrato);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó la firma del contrato correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar la firma del contrato.");
		}
	}
	
	public void cambiarEstadoMinutaLegalizada(Contrato contrato) {
		if(contrato.isMinutaLegalizada()) {
			contrato.setMinutaLegalizada(false);
		}else {
			contrato.setMinutaLegalizada(true);
		}
		Contrato cambioFirma = contratoService.save(contrato);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó la minuta legalizada del contrato correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar el contrato.");
		}
	}
	
	public void cambiarEstadoMinutaAutorizada(Contrato contrato) {
		if(contrato.isMinutaAutorizada()) {
			contrato.setMinutaAutorizada(false);
		}else {
			contrato.setMinutaAutorizada(true);
		}
		Contrato cambioFirma = contratoService.save(contrato);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó la minuta autorizada del contrato correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar el contrato.");
		}
	}
	
	public void cambiarEstadoEscritura(Contrato contrato) {
		if(contrato.isEscritura()) {
			contrato.setEscritura(false);
		}else {
			contrato.setEscritura(true);
		}
		Contrato cambioFirma = contratoService.save(contrato);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó la escritura del contrato correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar el contrato.");
		}
	}
	
	public void cambiarEstadoIndependizacion(Contrato contrato) {
		if(contrato.isIndependizacion()) {
			contrato.setIndependizacion(false);
		}else {
			contrato.setIndependizacion(true);
		}
		Contrato cambioFirma = contratoService.save(contrato);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó la independizacion del contrato correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar el contrato.");
		}
	}
	
	public void cambiarEstadoPagoIndependizacion(Contrato contrato) {
		if(contrato.isPagoIndependizacion()) {
			contrato.setPagoIndependizacion(false);
		}else {
			contrato.setPagoIndependizacion(true);
		}
		Contrato cambioFirma = contratoService.save(contrato);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó el pago de Independización correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar la firma del contrato.");
		}
	}
	
	public void cambiarPagoTotalCuota(Cuota cuota) {
		if(cuota.getPagoTotal().equals("S")) {
			cuota.setPagoTotal("N");
		}else {
			cuota.setPagoTotal("S");
		}
		Cuota cambioFirma = cuotaService.save(cuota);
		if(cambioFirma!=null) {
			addInfoMessage("Se actualizó la cuota correctamente.");
		}else {
			addErrorMessage("No se pudo actualizar la cuota.");
		}
	}
	
	public void deleteObs() {
		
		obsSelected.setEstado(false);
		observacionContratoService.save(obsSelected);
		addInfoMessage("Observación eliminado.");
		cargarListaObservacion();
	}
	
	public void cargarListaObservacion() {
		lstObservacionContrato = observacionContratoService.findByEstadoAndContratoOrderByFechaRegistroDesc(true, contratoSelected);
	}
	
	public void anadirObsContrato() {
		
		if(observacion.equals("")) {
			addErrorMessage("Agrege una observación.");
			return;
		}
		
		ObservacionContrato obs = new ObservacionContrato(); 
		obs.setContrato(contratoSelected);
		obs.setUsuario(navegacionBean.getUsuarioLogin());
		obs.setObservacion(observacion);
		obs.setFechaRegistro(new Date());
		obs.setEstado(true);
		ObservacionContrato observ =  observacionContratoService.save(obs);
			
		if(observ!=null) {
			cargarListaObservacion();
			addInfoMessage("Se guardó correctamente.");
			observacion="";
			
		}else {
			addErrorMessage("No se pudo guardar.");
		}
		
		
	}
	
	public void editarFecha(Cuota cuota) {
		if(cuota.getFechaPago()!=null) {
			cuotaService.save(cuota);
			
            addInfoMessage("Se cambió la fecha correctamente.");
		}
		verCronogramaPago();
	}
	
	public void editarInteres(Cuota cuota) {
		if(cuota.getInteres()!=null) {
			cuota.setCuotaTotal(cuota.getCuotaSI().add(cuota.getInteres()));
			cuotaService.save(cuota);
			
            addInfoMessage("Se cambió el interés correctamente.");
		}
		verCronogramaPago();
	}
	
	public void editarTotalCuota(Cuota cuota) {
		if(cuota.getCuotaTotal()!=null) {
			cuotaService.save(cuota);
			
            addInfoMessage("Se cambió el total correctamente.");
		}
		verCronogramaPago();
	}
	
	public void editarAdelanto(Cuota cuota) {
		if(cuota.getAdelanto()!=null) {
			cuotaService.save(cuota);
			
            addInfoMessage("Se cambió el adelanto correctamente.");
		}
		verCronogramaPago();
	}
	
	public void onCellEdit(CellEditEvent event) throws ParseException {
		
		Cuota cuota = lstCuotaVista.get(event.getRowIndex());
		Cuota cuotasiguiente = null;
		if(event.getRowIndex()!= (lstCuotaVista.size()-1)) {
			cuotasiguiente = lstCuotaVista.get(event.getRowIndex()+1);
		}
		Column column = (Column) event.getColumn();
		
		if (column.getId().equals("idCuotaSI")) {
			BigDecimal montoOld = new BigDecimal(event.getOldValue().toString());
						
			if(event.getNewValue()==null) {
				cuota.setCuotaSI(montoOld);
				addErrorMessage("Ingresar monto.");
				return;
			}
			
			BigDecimal monto = new BigDecimal(event.getNewValue().toString());
			
			
			if(monto.compareTo(BigDecimal.ZERO)==-1) {
				cuota.setCuotaSI(montoOld);
				addErrorMessage("El monto tiene que se mayor a 0.");
				return;
			}
			if(monto.compareTo(BigDecimal.ZERO)==0) {
				cuota.setCuotaSI(montoOld);
				addErrorMessage("El monto tiene que se mayor a 0.");
				return;
			}
			
		
			BigDecimal diferencia = BigDecimal.ZERO;   
			if(monto.compareTo(montoOld)==-1) {
				diferencia = montoOld.subtract(monto);
				if(cuotasiguiente!=null)cuotasiguiente.setCuotaSI(cuotasiguiente.getCuotaSI().add(diferencia));
			}else if(monto.compareTo(montoOld)==1){
				diferencia = monto.subtract(montoOld);
				if(cuotasiguiente!=null)cuotasiguiente.setCuotaSI(cuotasiguiente.getCuotaSI().subtract(diferencia));
			}
			
			cuota.setCuotaTotal(cuota.getCuotaSI().add(cuota.getInteres()));
			if(cuotasiguiente!=null)cuotasiguiente.setCuotaTotal(cuotasiguiente.getCuotaSI().add(cuotasiguiente.getInteres()));
			
			cuotaService.save(cuota);
			if(cuotasiguiente!=null)cuotaService.save(cuotasiguiente);
			verCronogramaPago();
			addInfoMessage("Se cambió correctamente el monto");
		}
			
    }

	public void generarPdfCronograma() {
		 if (lstCuotaVista == null || lstCuotaVista.isEmpty()) {
	            addInfoMessage("No hay datos para mostrar");
	        } else {
	        	
	        	dt = new DataSourceCronogramaPago();
	            for (int i = 0; i < lstCuotaVista.size(); i++) {
	               
	            	lstCuotaVista.get(i).setContrato(contratoSelected);;
	                dt.addResumenDetalle(lstCuotaVista.get(i));
	            }
	        	
	        	
	            parametros = new HashMap<String, Object>();
	            parametros.put("MZ-LT", contratoSelected.getLote().getManzana().getName()+"-"+contratoSelected.getLote().getNumberLote());
	            parametros.put("PROYECTO", contratoSelected.getLote().getProject().getName());	            
	            parametros.put("RUTAIMAGEN", getRutaGrafico("/recursos/images/LOGO.png"));
	            
	            String path = "secured/view/modulos/proyecto/reportes/jasper/repCronogramaPago.jasper"; 
	            reportGenBo.exportByFormatNotConnectDb(dt, path, "pdf", parametros, "CRONOGRAMA DE PAGO " , contratoSelected.getLote().getManzana().getName()+"-"+contratoSelected.getLote().getNumberLote());
	            dt = null;
	            parametros = null;
	      
	        }
	}
	
	public void verCronogramaPago() {
		lstCuotaVista = new ArrayList<>();
		List<Cuota>lstCuotaPagadas=cuotaService.findByPagoTotalAndEstadoAndContratoOrderById("S", true, contratoSelected);
		List<Cuota>lstCuotaPendientes = cuotaService.findByPagoTotalAndEstadoAndContratoOrderById("N", true, contratoSelected);
		lstCuotaVista.addAll(lstCuotaPagadas);
		lstCuotaVista.addAll(lstCuotaPendientes);
		
		sumaCuotaSI = BigDecimal.ZERO;
		sumaInteres = BigDecimal.ZERO;
		sumaCuotaTotal = BigDecimal.ZERO;
		
		for(Cuota c:lstCuotaVista) {
			if(c.getNroCuota()!=0 && c.isPrepago()==false) {
				sumaCuotaSI = sumaCuotaSI.add(c.getCuotaSI());
				sumaInteres = sumaInteres.add(c.getInteres());
				sumaCuotaTotal = sumaCuotaTotal.add(c.getCuotaTotal());
			}
			
		}
		
	}
	
	public void cambiarTipoPago() {
		if(tipoPago.equals("Contado")) {
			montoInicial=null;
			nroCuotas=0;
			interes=null;

		}
	}
	
	public void botonVerCuota(boolean agregaBD, Contrato contrato) {
		PrimeRequestContext context = PrimeRequestContext.getCurrentInstance(); 
		if(fechaVenta==null) {
			addErrorMessage("Ingresar Fecha de Venta.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(montoVenta==null) {
			addErrorMessage("Ingresar Monto de Venta.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}else if(montoVenta==BigDecimal.ZERO) {
			addErrorMessage("El monto de venta debe ser mayor a 0.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(montoInicial==null) {
			addErrorMessage("Ingresar Monto Inicial.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}else if(montoInicial==BigDecimal.ZERO) {
			addErrorMessage("El monto inicial debe ser mayor a 0.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(nroCuotas==0) {
			addErrorMessage("El número de cuotas debe ser mayor a 0.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(interes==null) {
			addErrorMessage("Ingresar Interés.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(fechaPrimeraCuota==null) {
			addErrorMessage("Ingresar Fecha de Primera Cuota.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		if(persona1==null) {
			addErrorMessage("Ingresar Persona de Venta.");
			context.getCallbackParams().put("noEsValido", false);
			return;
		}
		
		if(cuotaEspecial) {
			if(montoCuotaEspecial==null) {
				addErrorMessage("Ingresar un monto de cuota especial.");
				context.getCallbackParams().put("noEsValido", false);
				return;
			}else {
				if(montoCuotaEspecial.compareTo(BigDecimal.ZERO) < 1) {
					addErrorMessage("El monto de la cuota especial tiene que ser mayor que cero.");
					context.getCallbackParams().put("noEsValido", false);
					return;
				}
			}
			
			if(hastaCuotaEspecial==null) {
				addErrorMessage("Ingresar una duración de la cuota especial.");
				context.getCallbackParams().put("noEsValido", false);
				return;
			}else {
				if(hastaCuotaEspecial<=0) {
					addErrorMessage("La duración de la cuota especial tiene que ser mayor que cero.");
					context.getCallbackParams().put("noEsValido", false);
					return;
				}
			}
		}
		
		lstSimuladorPrevio.clear();
		
		Simulador filaInicio = new Simulador();
		filaInicio.setNroCuota("0");
		filaInicio.setFechaPago(fechaVenta);
		filaInicio.setInicial(montoInicial);
		filaInicio.setCuotaSI(BigDecimal.ZERO);
		filaInicio.setInteres(BigDecimal.ZERO);
		filaInicio.setCuotaTotal(BigDecimal.ZERO);
		lstSimuladorPrevio.add(filaInicio);
		
		if(agregaBD) {
			Cuota cuotaCero = new Cuota();
			cuotaCero.setNroCuota(0);
			cuotaCero.setFechaPago(fechaVenta);
			cuotaCero.setCuotaSI(montoInicial);
			cuotaCero.setInteres(BigDecimal.ZERO);
			cuotaCero.setCuotaTotal(montoInicial);
			cuotaCero.setAdelanto(BigDecimal.ZERO);

			RequerimientoSeparacion requerimiento = requerimientoSeparacionService.findAllByLoteAndEstado(contrato.getLote(), "Aprobado");
			if(requerimiento!=null) {
				List<DetalleDocumentoVenta> detalle = detalleDocumentoVentaService.findByRequerimientoSeparacionAndDocumentoVentaEstado(requerimiento, true);
				if(!detalle.isEmpty()) {
					for(DetalleDocumentoVenta d: detalle) {
						if(d.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("F") || d.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("B")) {
							cuotaCero.setAdelanto(d.getImporteVenta());
						}
					}
				}
			} 
			
			cuotaCero.setPagoTotal("N");
			cuotaCero.setContrato(contrato);
			cuotaCero.setEstado(true);
			cuotaCero.setOriginal(true);
			cuotaService.save(cuotaCero);
		}
		
		
		BigDecimal montoInteres=BigDecimal.ZERO;
		BigDecimal montoDeuda = montoVenta.subtract(montoInicial);
		BigDecimal sumaDecimales = BigDecimal.ZERO;
		BigDecimal sumaCuotaSI = BigDecimal.ZERO;
		BigDecimal cuota = montoDeuda.divide(new BigDecimal(nroCuotas), 0, RoundingMode.HALF_UP);
		
		if(interes.compareTo(BigDecimal.ZERO)==0) {
			
			for(int i=0; i<nroCuotas;i++) {
				Simulador filaCouta = new Simulador();
				if(i==0) {
					filaCouta.setFechaPago(fechaPrimeraCuota);
				}else {
					filaCouta.setFechaPago(sumarRestarMeses(fechaPrimeraCuota, i));
				}
				filaCouta.setNroCuota((i+1)+"");
				filaCouta.setInicial(BigDecimal.ZERO);
				filaCouta.setCuotaSI(cuota);
				filaCouta.setInteres(BigDecimal.ZERO);
				filaCouta.setCuotaTotal(cuota);
				
				String decimalCuotaTotal = filaCouta.getCuotaTotal().toString();
				String separador = Pattern.quote(".");
				String[] partes = decimalCuotaTotal.split(separador);
				if(partes.length>1) {
					String enteroTexto = partes[0];
					BigDecimal entero = new  BigDecimal(enteroTexto);
					BigDecimal decimal = cuota.subtract(entero);
					sumaDecimales = sumaDecimales.add(decimal);
					filaCouta.setCuotaSI(filaCouta.getCuotaSI().subtract(decimal));
					filaCouta.setCuotaTotal(filaCouta.getCuotaSI());
				}
				sumaCuotaSI = sumaCuotaSI.add(filaCouta.getCuotaSI());
				lstSimuladorPrevio.add(filaCouta);
			}
			for(Simulador s:lstSimuladorPrevio) {
				if(s.getNroCuota().equals("1")) {
					BigDecimal decimales = sumaCuotaSI.subtract(montoDeuda);
					s.setCuotaSI(s.getCuotaSI().subtract(decimales));
					s.setCuotaTotal(s.getCuotaSI());
					
				}
			}
			
			for(Simulador filaCuota:lstSimuladorPrevio) {
				if(agregaBD) {
					if(!filaCuota.getNroCuota().equals("0")) {
						Cuota cuotaBD = new Cuota();
						cuotaBD.setNroCuota(Integer.parseInt(filaCuota.getNroCuota()) );
						cuotaBD.setFechaPago(filaCuota.getFechaPago());
						cuotaBD.setCuotaSI(filaCuota.getCuotaSI());
						cuotaBD.setInteres(filaCuota.getInteres());
						cuotaBD.setCuotaTotal(filaCuota.getCuotaTotal());
						cuotaBD.setAdelanto(filaCuota.getInicial());
						cuotaBD.setPagoTotal("N");
						cuotaBD.setContrato(contrato);
						cuotaBD.setEstado(true);
						cuotaBD.setOriginal(true);
						cuotaService.save(cuotaBD);
					}
				}
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(montoInicial);
			filaTotal.setCuotaSI(montoDeuda);
			filaTotal.setInteres(BigDecimal.ZERO);
			filaTotal.setCuotaTotal(montoDeuda);
			lstSimuladorPrevio.add(filaTotal);
			
		}else {
			
			BigDecimal porc = interes;
			BigDecimal porcMin = (porc.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
			BigDecimal totalInteres = montoDeuda.multiply(porcMin);
//			montoInteres = montoDeuda.multiply(porcMin);     
			montoInteres = totalInteres.divide(new BigDecimal(nroCuotas), 0, RoundingMode.HALF_UP); 
			
			

			for(int i=0; i<nroCuotas;i++) {
				Simulador filaCouta = new Simulador();
				if(i==0) {
					filaCouta.setFechaPago(fechaPrimeraCuota);
					filaCouta.setCuotaSI(montoDeuda.subtract(cuota.multiply(new BigDecimal(nroCuotas-1))));
					filaCouta.setInteres(totalInteres.subtract(montoInteres.multiply(new BigDecimal(nroCuotas-1))));
				}else {
					filaCouta.setFechaPago(sumarRestarMeses(fechaPrimeraCuota, i));
					filaCouta.setCuotaSI(cuota);
					filaCouta.setInteres(montoInteres);
				}
				filaCouta.setNroCuota((i+1)+"");
				filaCouta.setInicial(BigDecimal.ZERO);
				
				filaCouta.setCuotaTotal(filaCouta.getCuotaSI().add(filaCouta.getInteres()));
				
				String decimalCuotaTotal = filaCouta.getCuotaTotal().toString();
				String separador = Pattern.quote(".");
				String[] partes = decimalCuotaTotal.split(separador);
				if(partes.length>1) {
					String enteroTexto = partes[0];
					BigDecimal entero = new  BigDecimal(enteroTexto);
					BigDecimal decimal = cuota.add(montoInteres).subtract(entero);
					sumaDecimales = sumaDecimales.add(decimal);
					
					filaCouta.setCuotaSI(filaCouta.getCuotaSI().subtract(decimal));
					filaCouta.setCuotaTotal(filaCouta.getCuotaSI().add(filaCouta.getInteres()));
				}
				sumaCuotaSI = sumaCuotaSI.add(filaCouta.getCuotaSI());
				lstSimuladorPrevio.add(filaCouta);
				
			}
			
			for(Simulador s:lstSimuladorPrevio) {
				if(s.getNroCuota().equals("1")) {
					BigDecimal decimales = sumaCuotaSI.subtract(montoDeuda);
					s.setCuotaSI(s.getCuotaSI().subtract(decimales));
					s.setCuotaTotal(s.getCuotaSI().add(s.getInteres()));
					
				}
			}
			
			if(cuotaEspecial) {
				BigDecimal sumaSI = BigDecimal.ZERO;
				BigDecimal sumaInteres = BigDecimal.ZERO;
				
				int cont = 1;
				for(Simulador filaCouta:lstSimuladorPrevio) {
					if(!filaCouta.getNroCuota().equals("0")) {
						if(cont <= hastaCuotaEspecial) {
							
							if(cont== hastaCuotaEspecial ) {
								filaCouta.setCuotaSI(filaCouta.getCuotaSI().add(sumaSI)); 
								filaCouta.setInteres(filaCouta.getInteres().add(sumaInteres)); 
								
								filaCouta.setCuotaTotal(filaCouta.getCuotaSI().add(filaCouta.getInteres()));
								
							}else {
								sumaSI  = sumaSI.add(filaCouta.getCuotaSI().subtract(montoCuotaEspecial));
								filaCouta.setCuotaSI(montoCuotaEspecial);
								
								sumaInteres = sumaInteres.add(filaCouta.getInteres());
								filaCouta.setInteres(BigDecimal.ZERO);
								
								filaCouta.setCuotaTotal(montoCuotaEspecial); 
							}
							
							cont++;
							
							
						}
					
					}
				}
			}
			
			for(Simulador filaCouta:lstSimuladorPrevio) {
				if(agregaBD) {
					if(!filaCouta.getNroCuota().equals("0")) {
						Cuota cuotaBD = new Cuota();
						cuotaBD.setNroCuota(Integer.parseInt(filaCouta.getNroCuota()));
						cuotaBD.setFechaPago(filaCouta.getFechaPago());
						cuotaBD.setCuotaSI(filaCouta.getCuotaSI());
						cuotaBD.setInteres(filaCouta.getInteres());
						cuotaBD.setCuotaTotal(filaCouta.getCuotaTotal());
						cuotaBD.setAdelanto(filaCouta.getInicial());
						cuotaBD.setPagoTotal("N");
						cuotaBD.setContrato(contrato);
						cuotaBD.setEstado(true);
						cuotaBD.setOriginal(true);
						cuotaService.save(cuotaBD);
					}
				}
			}
			
			BigDecimal sumaInicial = BigDecimal.ZERO;
			BigDecimal sumaSI = BigDecimal.ZERO;
			BigDecimal sumaInteres = BigDecimal.ZERO;
			BigDecimal sumaTotal = BigDecimal.ZERO;
			
			for(Simulador sim:lstSimuladorPrevio) {
				sumaInicial=sumaInicial.add(sim.getInicial());
				sumaSI=sumaSI.add(sim.getCuotaSI());
				sumaInteres=sumaInteres.add(sim.getInteres());
				sumaTotal=sumaTotal.add(sim.getCuotaTotal());
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(sumaInicial);
			filaTotal.setCuotaSI(sumaSI);
			filaTotal.setInteres(sumaInteres);
			filaTotal.setCuotaTotal(sumaTotal);
			lstSimuladorPrevio.add(filaTotal);
			
		}
		
		context.getCallbackParams().put("noEsValido", true);
	}
	
	public void anularContrato() {
		
		List<Cuota> lstCuotas = cuotaService.findByContrato(contratoSelected);
		for(Cuota c:lstCuotas) {
			if(c.isPrepago()) {
				List<DetalleDocumentoVenta> lstD = detalleDocumentoVentaService.findByDocumentoVentaEstadoAndCuotaPrepago(true, c);
				if(!lstD.isEmpty()) {
					
					for(DetalleDocumentoVenta detalle : lstD) {
						if(!detalle.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("C") && !detalle.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("D")) {
							DocumentoVenta docuemento = documentoVentaService.findByDocumentoVentaRefAndEstado(detalle.getDocumentoVenta(), true);
							if(docuemento==null) {
								addErrorMessage("No se puede anular el contrato porque existe boletas/facturas de Pre-pago Activas o necesitan generar nota de crédito.");
								return;
							}
						}
					}
					
					
				}
			}else {
				List<DetalleDocumentoVenta> lstD = detalleDocumentoVentaService.findByDocumentoVentaEstadoAndCuota(true, c);
				if(!lstD.isEmpty()) {
					for(DetalleDocumentoVenta detalle : lstD) {
						if(!detalle.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("C") && !detalle.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("D")) {
							DocumentoVenta docuemento = documentoVentaService.findByDocumentoVentaRefAndEstado(detalle.getDocumentoVenta(), true);
							if(docuemento==null) {
								addErrorMessage("No se puede anular el contrato porque existe boletas/facturas de Pre-pago Activas o necesitan generar nota de crédito.");
								return;
							}
						}
						
					}
				}
			}
			
		}
		
		
		contratoSelected.setEstado(EstadoContrato.ANULADO.getName());
		contratoService.save(contratoSelected);
		contratoSelected.getLote().setRealizoContrato("N");
		contratoSelected.getLote().setStatus(EstadoLote.DISPONIBLE.getName()); 
		loteService.save(contratoSelected.getLote());
		
		
		if(contratoSelected.getTipoPago().equals("Crédito")) {
			List<Cuota> lstcuota = cuotaService.findByContratoAndEstado(contratoSelected, true);
			for(Cuota c:lstcuota) {
				c.setEstado(false);
				c.setOriginal(false);
				cuotaService.save(c);
			}
		}
		
		List<PlantillaVenta> lstPlantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
		if(!lstPlantilla.isEmpty()) {
			for(PlantillaVenta p : lstPlantilla) {
				p.setRealizoContrato(false);
				plantillaVentaService.save(p);
			}
		}
		
		addInfoMessage("Contrato Anulado Correctamente.");
	}
	
	public void terminarContrato() {
		
		List<Cuota> lstCuotas = cuotaService.findByContratoAndEstado(contratoSelected, true);
		for(Cuota c:lstCuotas) {
			if(c.getPagoTotal().equals("N")) {
				addErrorMessage("No se puede terminar el contrato porque aun tiene pagos pendientes.");
				return;
			}
			
		}
		
		contratoSelected.setEstado(EstadoContrato.TERMINADO.getName());
		contratoService.save(contratoSelected);
		
	
		addInfoMessage("Contrato Terminado Correctamente.");
	}
	
	public void resolverContrato() {
		contratoSelected.setEstado(EstadoContrato.RESUELTO.getName());
		contratoService.save(contratoSelected);
		
		contratoSelected.getLote().setStatus(EstadoLote.DISPONIBLE.getName());
		contratoSelected.getLote().setRealizoContrato("N");
		loteService.save(contratoSelected.getLote());
		
		List<Cuota> lstCuotas = cuotaService.findByContratoAndEstado(contratoSelected, true);
		for(Cuota c : lstCuotas) {
			c.setEstado(false);
			cuotaService.save(c);
		}
		
		verVouchersPago();
		
		for(Imagen imagen: lstImagenVoucher) {
			imagen.setResuelto(true);
			imagenService.save(imagen);
		}
		
		
	
		addInfoMessage("Contrato Resuelto Correctamente.");
	}
	
	public void listarPersonas() {
		lstPerson=personService.findByStatus(true);
	}
	
	public void generarExcel() throws IOException, XmlException, InvalidFormatException {
		if(contratoSelected.getTipoPago().equals("Crédito")) {
			if(contratoSelected.getLote().getProject().getId() ==1) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==2) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==3) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==4) { 
				// HIGAL 3 ETAPA 1
				formatoCredito_higal3_etapa1();
				
			}
			if(contratoSelected.getLote().getProject().getId() ==5) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==6) { 
				// HIGAL 3 ETAPA 3
				formatoCredito_higal3_etapa3();		
			}
			if(contratoSelected.getLote().getProject().getId() ==7) { 
				// HIGAL 3 ETAPA 4
				formatoCredito_higal3_etapa4();
			}
			if(contratoSelected.getLote().getProject().getId() ==8 || contratoSelected.getLote().getProject().getId() ==9) { 
				// VALLE DEL AGUILA
				formatoCredito_VDA();
			}
			if(contratoSelected.getLote().getProject().getId() ==10) { 
				formatoCredito_ASR_IV();
			}
			if(contratoSelected.getLote().getProject().getId() ==11) { 
				formatoCredito();
			}
			if(contratoSelected.getLote().getProject().getId() ==12) { 
				formatoCredito();		
			}
			if(contratoSelected.getLote().getProject().getId() ==13) { 
				formatoCredito_ASR_V();
			}
			
			
		}else {
			if(contratoSelected.getLote().getProject().getId() ==1) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==2) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==3) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==4) { 
				// HIGAL 3 ETAPA 1
				formatoContado_higal3_etapa1();
			}
			if(contratoSelected.getLote().getProject().getId() ==5) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==6) { 
				// HIGAL 3 ETAPA 3
				formatoContado_higal3_etapa3();	
			}
			if(contratoSelected.getLote().getProject().getId() ==7) {
				// HIGAL 3 ETAPA 4
				formatoContado_higal3_etapa4();
			}
			if(contratoSelected.getLote().getProject().getId() ==8 || contratoSelected.getLote().getProject().getId() ==9) { 
				formatoContado_VDA();
			}
			
			if(contratoSelected.getLote().getProject().getId() ==10) { 
				formatoContado_ASR_IV();
			}
			if(contratoSelected.getLote().getProject().getId() ==11) { 
				formatoContado();
			}
			if(contratoSelected.getLote().getProject().getId() ==12) { 
				formatoContado();			
			}
			if(contratoSelected.getLote().getProject().getId() ==13) { 
				formatoContado_ASR_V();
			}
			
		}
	}
	
	public void formatoCredito_higal3_etapa1() throws XmlException, InvalidFormatException, FileNotFoundException, IOException {
		String areaHectarea = contratoSelected.getLote().getProject().getAreaHectarea();
		String unidadCatastral = contratoSelected.getLote().getProject().getUnidadCatastral();
		String numPartidaElectronica = contratoSelected.getLote().getProject().getNumPartidaElectronica();
		String codigoPredio = contratoSelected.getLote().getProject().getCodigoPredio();
		
		ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, contratoSelected.getLote().getProject(),contratoSelected.getLote().getManzana()); 
		if(busqueda!=null) {
			areaHectarea = busqueda.getAreaHectarea();
			unidadCatastral = busqueda.getUnidadCatastral();
			numPartidaElectronica = busqueda.getNumPartidaElectronica();
			codigoPredio = busqueda.getCodigoPredio();
		}
		
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, ALDASA INMOBILIARIA S.A.C., CON RUC Nº 20607274526, REPRESENTADA POR SU " );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);				
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11352661 ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FISCAL EN AV. SANTA VICTORIA 719 URB. "
				+ "SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA)(LOS)SR. (A.) (ES.) ");estiloNormalTexto(run);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation()==null?"XXXXXXXXXXX": p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null?"XXXXXXXXXXX": p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni()==null?"XXXXXXXXXXX":p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone()==null?"XXXXXXXXXXX":p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail()==null?"XXXXXXXXXXX":p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress()==null?"XXXXXXXXXXX":p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(" Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:. ");estiloNormalTexto(run);
		
		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+codigoPredio.toUpperCase()+", ÁREA "
				+ areaHectarea.toUpperCase()+" U.C. "+unidadCatastral.toUpperCase()+", DISTRITO DE "+contratoSelected.getLote().getProject().getDistrict().getName().toUpperCase()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName().toUpperCase()+", DEPARTAMENTO DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName().toUpperCase()+", EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N° "+numPartidaElectronica.toUpperCase()+", ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL "+contratoSelected.getLote().getProject().getZonaRegistral().toUpperCase()+".");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE "
				+ "LOTIZACIÓN EL HIGAL III ETAPA I, EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE "
				+ "ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° "+numPartidaElectronica+".");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
				+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("COMO INICIAL, EL MONTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoInicial())+" ("+numeroAletra.Convertir(contratoSelected.getMontoInicial()+"", true, "")+" SOLES) ");estiloNegritaTexto(run);
 		run = paragrapha.createRun();run.setText("CON DEPÓSITO(S) EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011-285-000100180945-46, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, CCI 80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
 		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO DIRECTO QUE BRINDA ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, "
				+ "DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();run.setText("- PAGO DEL SALDO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getNumeroCuota()+" ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("CUOTAS MENSUALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE EFECTUARÁN EL DÍA "+sdfDay.format(contratoSelected.getFechaPrimeraCuota())+" DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA CUAL PERTENECE AL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011 285 000100180945 46 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTAS DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062; ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, MEDIO NOTARIAL O REQUERIMIENTO ALGUNO, SI TRANSCURRIDO DICHO TÉRMINO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("INCURRE EN MORA, AUTOMÁTICAMENTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y DOCUMENTOS EMITIDOS POR ÉL,  SI EXISTIERA UN RECIBO DE PAGO "
				+ "EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER CANCELADO LAS ANTERIORES.");estiloNormalTexto(run);
	
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE "
				+ "ESTA CLÁUSULA TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE "+contratoSelected.getNumeroCuota()+" "
				+ "MESES, MZ "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE (S) "+contratoSelected.getLote().getNumberLote()+" DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA Y POSTERIOR ESCRITURA PÚBLICA DE COMPRA VENTA, PARA QUE PUEDA(N) REALIZAR "
				+ "LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, "
				+ "PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL INMUEBLE, "
				+ "MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA O ASPECTO, NI AJUSTES "
				+ "DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);

		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y "
				+ "DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE EL "
				+ "PROYECTO INMOBILIARIO.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO DEL PROYECTO.");estiloNormalTexto(run);

		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS "
				+ "O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR "
				+ "EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
				
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O "
				+ "PROCESO JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, PROCESAL Y/O "
				+ "ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UNA VEZ CANCELADO EL SALDO TOTAL POR LA COMPRA VENTA DEL INMUEBLE, ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) "
				+ "ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE "
				+ "Y ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA, UNA VEZ QUE HAYA REALIZADO EL PAGO TOTAL DEL SALDO DE LA COMPRA VENTA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE ESTAS GENEREN, "
				+ "ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE CORRESPONDAN "
				+ "ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO INDEPENDIENTE DE EL (LOS) "
				+ "LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y ÁREAS VERDES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA "
				+ "DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA AFECTAR "
				+ "EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE "
				+ "LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO N°1345 "
				+ "Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE ESTE MODO, "
				+ "EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE "
				+ "EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS "
				+ "COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR "
				+ "A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD "
				+ "DE AMBAS PARTES.");estiloNormalTexto(run);
		
		
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE A LA "
				+ "JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, LOS CONSIGNADOS "
				+ "EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME AL "
				+ "ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE "
				+ "5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS Y "
				+ "SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SI EL PAGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA "
				+ "PREPAGO DE CAPITAL; EN CASO, QUE EXISTA PREPAGO PARCIAL, SE OMITIRÁ INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y "
				+ "SE GENERA UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER ACORDADO CON ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("“LA PARTE VENDEDORA”.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO QUINTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TRES (03)CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS EN LA TERCERA CLÁUSULA DE ESTE CONTRATO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PUEDE UNILATERALMETE RESOLVER EL CONTRATO; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL SALDO DEUDOR A  ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA "
				+ "LO CUAL EL NOTIFICADOR SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO UNA "
				+ "IMAGEN FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE SE HAYAN "
				+ "EFECTUADO PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEXTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS "
				+ "NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();     
		
		
		// ************************** TABLAS **************************
		
		List<String> lstTextFirma = new ArrayList<>();
		String textF1 = "___________________________________/ALDASA INMOBILIARIA S.A.C./RUC: 20607274526";lstTextFirma.add(textF1);
		String textF2="";
		String textF3="";
		String textF4="";
		String textF5="";
		String textF6="";
		
		int contF = 1;
		for(Person p: lstPersonas) {
			if(contF==1) {
				textF2="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==2) {
				textF3="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==3) {
				textF4="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==4) {
				textF5="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==5) {
				textF6="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			
			contF++;
		}
		
		lstTextFirma.add(textF2);
		lstTextFirma.add(textF3);
		lstTextFirma.add(textF4);
		lstTextFirma.add(textF5);
		lstTextFirma.add(textF6);
		
		
		XWPFTable tableF = document.createTable(3, 2);   
        setTableWidth(tableF, "9000");  
        
        CTTblPr tblPr = tableF.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        
        int priColumnF=0;
        
        for (int rowIndex = 0; rowIndex < tableF.getNumberOfRows(); rowIndex++) {
            XWPFTableRow row2 = tableF.getRow(rowIndex);
            
            // Agregar primer celda
            XWPFParagraph paragraph1 = row2.getCell(0).addParagraph();
            paragraph1.setAlignment(ParagraphAlignment.CENTER);
            
            XWPFRun run1 = paragraph1.createRun();
            String[] parts = lstTextFirma.get(priColumnF).split("/");
            for(String texto: parts) {
            	run1.setText(texto);estiloNormalTexto(run1);
            	run1.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            
            priColumnF++;

            // Agregar segunda celda
            XWPFParagraph paragraph22 = row2.getCell(1).addParagraph();
            paragraph22.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run2 = paragraph22.createRun();
            String[] parts2 = lstTextFirma.get(priColumnF).split("/");
            for(String texto: parts2) {
            	run2.setText(texto);estiloNormalTexto(run2);
            	run2.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            priColumnF++;
        }
		
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "Empresa:  ALDASA INMOBILIARIA S.A.C.";lstTexto.add(text1);
		String text2 = "Fecha: "+ sdf.format(contratoSelected.getFechaVenta());lstTexto.add(text2); 
		String text3 = "Comprador(es): ";
		String text4 = "D.N.I: ";
		int cont = 1;
		for(Person p: lstPersonas) {
			
			if(lstPersonas.size()==cont){
				text3= text3+p.getSurnames()+" "+p.getNames();
				text4= text4+p.getDni();
			}else {
				text3= text3+p.getSurnames()+" "+p.getNames()+" / ";
				text4= text4+p.getDni()+" / ";
			}
			cont++;
		}
		
		lstTexto.add(text3.toUpperCase());
		lstTexto.add(text4.toUpperCase());
		
		String text5 = "Monto Total: S/"+String.format("%,.2f",contratoSelected.getMontoVenta());lstTexto.add(text5);
		String text6 = "Monto Deuda: S/"+String.format("%,.2f",contratoSelected.getMontoVenta().subtract(contratoSelected.getMontoInicial()));lstTexto.add(text6);
		String text7 = "N° Cuotas: "+contratoSelected.getNumeroCuota();lstTexto.add(text7);
		String text8 = "Moneda: Soles";lstTexto.add(text8);
		String text9 = "Cuotas Pendientes: "+contratoSelected.getNumeroCuota();lstTexto.add(text9);
		String text10 = "Interés: "+contratoSelected.getInteres()+"%";lstTexto.add(text10);
		String text11 = "Mz: "+contratoSelected.getLote().getManzana().getName() ;lstTexto.add(text11);
		String text12 = "Lote: "+contratoSelected.getLote().getNumberLote();lstTexto.add(text12);
		
		
		XWPFTable table1 = document.createTable(7, 2);   
		
        setTableWidth(table1, "9000");  
//        fillTable(table1, lstTexto);  
        mergeCellsHorizontal(table1,0,0,1);  
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = table1.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
            	estiloCentrarCeldaTabla(row1.getCell(0));
            }else {
            	XWPFTableRow row2 = table1.getRow(rowIndex);
            	row2.getCell(0).setText(lstTexto.get(priColumn));
            	priColumn++;
            	row2.getCell(1).setText(lstTexto.get(priColumn));
            	priColumn++;
            }
        }  
        
        paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();
        
        int cantRowsPagos = contratoSelected.getNumeroCuota()+4;
        XWPFTable tablePagos = document.createTable(cantRowsPagos, 6);
        
        setTableWidth(tablePagos, "9000");  
//      fillTable(table1, lstTexto);  
        mergeCellsHorizontal(tablePagos,0,0,5);
        mergeCellsHorizontal(tablePagos,tablePagos.getNumberOfRows()-1,0,1);  
        int index = 0;
        simularCuotas(contratoSelected);
        for (int rowIndex = 0; rowIndex < tablePagos.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = tablePagos.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE LA DEUDA");
            	estiloCentrarCeldaTabla(row1.getCell(0));
             
            }else if(rowIndex==1) {
            	XWPFTableRow row2 = tablePagos.getRow(rowIndex);
            	row2.getCell(0).setText("N° Cuotas");
            	row2.getCell(1).setText("Periodo");
            	row2.getCell(2).setText("Cuota inicial");
            	row2.getCell(3).setText("Cuota SI");
            	row2.getCell(4).setText("Interés");
            	row2.getCell(5).setText("Cuota Total");
            	
            	estiloCentrarCeldaTabla(row2.getCell(0));
            	estiloCentrarCeldaTabla(row2.getCell(1));
            	estiloCentrarCeldaTabla(row2.getCell(2));
            	estiloCentrarCeldaTabla(row2.getCell(3));
            	estiloCentrarCeldaTabla(row2.getCell(4));
            	estiloCentrarCeldaTabla(row2.getCell(5));
            }else {
            	Simulador sim = lstSimulador.get(index);
            	
            	XWPFTableRow rowContenido = tablePagos.getRow(rowIndex);
            	rowContenido.getCell(0).setText(sim.getNroCuota());
            	rowContenido.getCell(1).setText(sim.getFechaPago() != null?sdf.format(sim.getFechaPago()):""); 
            	rowContenido.getCell(2).setText("S/"+String.format("%,.2f",sim.getInicial()));
            	rowContenido.getCell(3).setText("S/"+String.format("%,.2f",sim.getCuotaSI()));
            	rowContenido.getCell(4).setText("S/"+String.format("%,.2f",sim.getInteres()));
            	rowContenido.getCell(5).setText("S/"+String.format("%,.2f",sim.getCuotaTotal()));
            	
            	estiloCentrarCeldaTabla(rowContenido.getCell(0));
            	estiloCentrarCeldaTabla(rowContenido.getCell(1));
            	estiloCentrarCeldaTabla(rowContenido.getCell(2));
            	estiloCentrarCeldaTabla(rowContenido.getCell(3));
            	estiloCentrarCeldaTabla(rowContenido.getCell(4));
            	estiloCentrarCeldaTabla(rowContenido.getCell(5));
            	
            	index++;
            	
            }
        }  
        
        
        List<PlantillaVenta> plantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
        if(!plantilla.isEmpty()) {
        	List<ImagenPlantillaVenta> lstImagenPlantillaVenta = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantilla.get(0), true);
        	for(ImagenPlantillaVenta imagen : lstImagenPlantillaVenta) {
        		XWPFParagraph  paragraphh = document.createParagraph();
                String imagePath = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+imagen.getNombre();
                FileInputStream imageStream = new FileInputStream(imagePath);

                // Agregar datos de la imagen al documento
                int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageName = "nombre_de_la_imagen";
                document.addPictureData(imageStream, pictureType);

                // Crear un objeto XWPFPicture para insertar la imagen en el documento
                XWPFPicture picture = paragraphh.createRun().getParagraph().createRun().addPicture(new FileInputStream(imagePath), pictureType, imageName, Units.toEMU(400), Units.toEMU(400));
                
        	}
        	
        	
        }
        
        
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void formatoCredito_higal3_etapa3() throws XmlException, InvalidFormatException, FileNotFoundException, IOException {
		String areaHectarea = contratoSelected.getLote().getProject().getAreaHectarea();
		String unidadCatastral = contratoSelected.getLote().getProject().getUnidadCatastral();
		String numPartidaElectronica = contratoSelected.getLote().getProject().getNumPartidaElectronica();
		String codigoPredio = contratoSelected.getLote().getProject().getCodigoPredio();
		
		ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, contratoSelected.getLote().getProject(),contratoSelected.getLote().getManzana()); 
		if(busqueda!=null) {
			areaHectarea = busqueda.getAreaHectarea();
			unidadCatastral = busqueda.getUnidadCatastral();
			numPartidaElectronica = busqueda.getNumPartidaElectronica();
			codigoPredio = busqueda.getCodigoPredio();
		}
		
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, ALDASA INMOBILIARIA S.A.C., CON RUC Nº 20607274526, REPRESENTADA POR SU " );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);				
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11352661 ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FISCAL EN AV. SANTA VICTORIA 719 URB. "
				+ "SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA)(LOS)SR. (A.) (ES.) ");estiloNormalTexto(run);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation()==null?"XXXXXXXXXXX": p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null?"XXXXXXXXXXX": p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni()==null?"XXXXXXXXXXX":p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone()==null?"XXXXXXXXXXX":p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail()==null?"XXXXXXXXXXX":p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress()==null?"XXXXXXXXXXX":p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(" Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:. ");estiloNormalTexto(run);
		
		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+codigoPredio.toUpperCase()+", ÁREA "
				+ areaHectarea.toUpperCase()+" U.C. "+unidadCatastral.toUpperCase()+", DISTRITO DE "+contratoSelected.getLote().getProject().getDistrict().getName().toUpperCase()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName().toUpperCase()+", DEPARTAMENTO DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName().toUpperCase()+", EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N° "+numPartidaElectronica.toUpperCase()+", ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL "+contratoSelected.getLote().getProject().getZonaRegistral().toUpperCase()+".");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE "
				+ "LOTIZACIÓN EL HIGAL III ETAPA III, EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE "
				+ "ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° "+numPartidaElectronica+".");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
				+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("COMO INICIAL, EL MONTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoInicial())+" ("+numeroAletra.Convertir(contratoSelected.getMontoInicial()+"", true, "")+" SOLES) ");estiloNegritaTexto(run);
 		run = paragrapha.createRun();run.setText("CON DEPÓSITO(S) EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011-285-000100180945-46, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, CCI 80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
 		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO DIRECTO QUE BRINDA ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, "
				+ "DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();run.setText("- PAGO DEL SALDO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getNumeroCuota()+" ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("CUOTAS MENSUALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE EFECTUARÁN EL DÍA "+sdfDay.format(contratoSelected.getFechaPrimeraCuota())+" DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA CUAL PERTENECE AL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011 285 000100180945 46 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTAS DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062; ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, MEDIO NOTARIAL O REQUERIMIENTO ALGUNO, SI TRANSCURRIDO DICHO TÉRMINO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("INCURRE EN MORA, AUTOMÁTICAMENTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y DOCUMENTOS EMITIDOS POR ÉL,  SI EXISTIERA UN RECIBO DE PAGO "
				+ "EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER CANCELADO LAS ANTERIORES.");estiloNormalTexto(run);
	
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE "
				+ "ESTA CLÁUSULA TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE "+contratoSelected.getNumeroCuota()+" "
				+ "MESES, MZ "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE (S) "+contratoSelected.getLote().getNumberLote()+" DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA Y POSTERIOR ESCRITURA PÚBLICA DE COMPRA VENTA, PARA QUE PUEDA(N) REALIZAR "
				+ "LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, "
				+ "PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL INMUEBLE, "
				+ "MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA O ASPECTO, NI AJUSTES "
				+ "DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);

		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y "
				+ "DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE EL "
				+ "PROYECTO INMOBILIARIO.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO DEL PROYECTO.");estiloNormalTexto(run);

		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS "
				+ "O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR "
				+ "EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
				
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O "
				+ "PROCESO JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, PROCESAL Y/O "
				+ "ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UNA VEZ CANCELADO EL SALDO TOTAL POR LA COMPRA VENTA DEL INMUEBLE, ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) "
				+ "ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE "
				+ "Y ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA, UNA VEZ QUE HAYA REALIZADO EL PAGO TOTAL DEL SALDO DE LA COMPRA VENTA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE ESTAS GENEREN, "
				+ "ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE CORRESPONDAN "
				+ "ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO INDEPENDIENTE DE EL (LOS) "
				+ "LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y ÁREAS VERDES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA "
				+ "DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA AFECTAR "
				+ "EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE "
				+ "LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO N°1345 "
				+ "Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE ESTE MODO, "
				+ "EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE "
				+ "EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS "
				+ "COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR "
				+ "A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD "
				+ "DE AMBAS PARTES.");estiloNormalTexto(run);
		
		
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE A LA "
				+ "JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, LOS CONSIGNADOS "
				+ "EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME AL "
				+ "ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE "
				+ "5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS Y "
				+ "SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SI EL PAGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA "
				+ "PREPAGO DE CAPITAL; EN CASO, QUE EXISTA PREPAGO PARCIAL, SE OMITIRÁ INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y "
				+ "SE GENERA UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER ACORDADO CON ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("“LA PARTE VENDEDORA”.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO QUINTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TRES (03)CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS EN LA TERCERA CLÁUSULA DE ESTE CONTRATO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PUEDE UNILATERALMETE RESOLVER EL CONTRATO; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL SALDO DEUDOR A  ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA "
				+ "LO CUAL EL NOTIFICADOR SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO UNA "
				+ "IMAGEN FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE SE HAYAN "
				+ "EFECTUADO PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEXTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS "
				+ "NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();     
		
		
		// ************************** TABLAS **************************
		
		List<String> lstTextFirma = new ArrayList<>();
		String textF1 = "___________________________________/ALDASA INMOBILIARIA S.A.C./RUC: 20607274526";lstTextFirma.add(textF1);
		String textF2="";
		String textF3="";
		String textF4="";
		String textF5="";
		String textF6="";
		
		int contF = 1;
		for(Person p: lstPersonas) {
			if(contF==1) {
				textF2="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==2) {
				textF3="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==3) {
				textF4="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==4) {
				textF5="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==5) {
				textF6="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			
			contF++;
		}
		
		lstTextFirma.add(textF2);
		lstTextFirma.add(textF3);
		lstTextFirma.add(textF4);
		lstTextFirma.add(textF5);
		lstTextFirma.add(textF6);
		
		
		XWPFTable tableF = document.createTable(3, 2);   
        setTableWidth(tableF, "9000");  
        
        CTTblPr tblPr = tableF.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        
        int priColumnF=0;
        
        for (int rowIndex = 0; rowIndex < tableF.getNumberOfRows(); rowIndex++) {
            XWPFTableRow row2 = tableF.getRow(rowIndex);
            
            // Agregar primer celda
            XWPFParagraph paragraph1 = row2.getCell(0).addParagraph();
            paragraph1.setAlignment(ParagraphAlignment.CENTER);
            
            XWPFRun run1 = paragraph1.createRun();
            String[] parts = lstTextFirma.get(priColumnF).split("/");
            for(String texto: parts) {
            	run1.setText(texto);estiloNormalTexto(run1);
            	run1.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            
            priColumnF++;

            // Agregar segunda celda
            XWPFParagraph paragraph22 = row2.getCell(1).addParagraph();
            paragraph22.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run2 = paragraph22.createRun();
            String[] parts2 = lstTextFirma.get(priColumnF).split("/");
            for(String texto: parts2) {
            	run2.setText(texto);estiloNormalTexto(run2);
            	run2.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            priColumnF++;
        }
		
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "Empresa:  ALDASA INMOBILIARIA S.A.C.";lstTexto.add(text1);
		String text2 = "Fecha: "+ sdf.format(contratoSelected.getFechaVenta());lstTexto.add(text2); 
		String text3 = "Comprador(es): ";
		String text4 = "D.N.I: ";
		int cont = 1;
		for(Person p: lstPersonas) {
			
			if(lstPersonas.size()==cont){
				text3= text3+p.getSurnames()+" "+p.getNames();
				text4= text4+p.getDni();
			}else {
				text3= text3+p.getSurnames()+" "+p.getNames()+" / ";
				text4= text4+p.getDni()+" / ";
			}
			cont++;
		}
		
		lstTexto.add(text3.toUpperCase());
		lstTexto.add(text4.toUpperCase());
		
		String text5 = "Monto Total: S/"+String.format("%,.2f",contratoSelected.getMontoVenta());lstTexto.add(text5);
		String text6 = "Monto Deuda: S/"+String.format("%,.2f",contratoSelected.getMontoVenta().subtract(contratoSelected.getMontoInicial()));lstTexto.add(text6);
		String text7 = "N° Cuotas: "+contratoSelected.getNumeroCuota();lstTexto.add(text7);
		String text8 = "Moneda: Soles";lstTexto.add(text8);
		String text9 = "Cuotas Pendientes: "+contratoSelected.getNumeroCuota();lstTexto.add(text9);
		String text10 = "Interés: "+contratoSelected.getInteres()+"%";lstTexto.add(text10);
		String text11 = "Mz: "+contratoSelected.getLote().getManzana().getName() ;lstTexto.add(text11);
		String text12 = "Lote: "+contratoSelected.getLote().getNumberLote();lstTexto.add(text12);
		
		
		XWPFTable table1 = document.createTable(7, 2);   
		
        setTableWidth(table1, "9000");  
//        fillTable(table1, lstTexto);  
        mergeCellsHorizontal(table1,0,0,1);  
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = table1.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
            	estiloCentrarCeldaTabla(row1.getCell(0));
            }else {
            	XWPFTableRow row2 = table1.getRow(rowIndex);
            	row2.getCell(0).setText(lstTexto.get(priColumn));
            	priColumn++;
            	row2.getCell(1).setText(lstTexto.get(priColumn));
            	priColumn++;
            }
        }  
        
        paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();
        
        int cantRowsPagos = contratoSelected.getNumeroCuota()+4;
        XWPFTable tablePagos = document.createTable(cantRowsPagos, 6);
        
        setTableWidth(tablePagos, "9000");  
//      fillTable(table1, lstTexto);  
        mergeCellsHorizontal(tablePagos,0,0,5);
        mergeCellsHorizontal(tablePagos,tablePagos.getNumberOfRows()-1,0,1);  
        int index = 0;
        simularCuotas(contratoSelected);
        for (int rowIndex = 0; rowIndex < tablePagos.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = tablePagos.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE LA DEUDA");
            	estiloCentrarCeldaTabla(row1.getCell(0));
             
            }else if(rowIndex==1) {
            	XWPFTableRow row2 = tablePagos.getRow(rowIndex);
            	row2.getCell(0).setText("N° Cuotas");
            	row2.getCell(1).setText("Periodo");
            	row2.getCell(2).setText("Cuota inicial");
            	row2.getCell(3).setText("Cuota SI");
            	row2.getCell(4).setText("Interés");
            	row2.getCell(5).setText("Cuota Total");
            	
            	estiloCentrarCeldaTabla(row2.getCell(0));
            	estiloCentrarCeldaTabla(row2.getCell(1));
            	estiloCentrarCeldaTabla(row2.getCell(2));
            	estiloCentrarCeldaTabla(row2.getCell(3));
            	estiloCentrarCeldaTabla(row2.getCell(4));
            	estiloCentrarCeldaTabla(row2.getCell(5));
            }else {
            	Simulador sim = lstSimulador.get(index);
            	
            	XWPFTableRow rowContenido = tablePagos.getRow(rowIndex);
            	rowContenido.getCell(0).setText(sim.getNroCuota());
            	rowContenido.getCell(1).setText(sim.getFechaPago() != null?sdf.format(sim.getFechaPago()):""); 
            	rowContenido.getCell(2).setText("S/"+String.format("%,.2f",sim.getInicial()));
            	rowContenido.getCell(3).setText("S/"+String.format("%,.2f",sim.getCuotaSI()));
            	rowContenido.getCell(4).setText("S/"+String.format("%,.2f",sim.getInteres()));
            	rowContenido.getCell(5).setText("S/"+String.format("%,.2f",sim.getCuotaTotal()));
            	
            	estiloCentrarCeldaTabla(rowContenido.getCell(0));
            	estiloCentrarCeldaTabla(rowContenido.getCell(1));
            	estiloCentrarCeldaTabla(rowContenido.getCell(2));
            	estiloCentrarCeldaTabla(rowContenido.getCell(3));
            	estiloCentrarCeldaTabla(rowContenido.getCell(4));
            	estiloCentrarCeldaTabla(rowContenido.getCell(5));
            	
            	index++;
            	
            }
        }  
        
        
        List<PlantillaVenta> plantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
        if(!plantilla.isEmpty()) {
        	List<ImagenPlantillaVenta> lstImagenPlantillaVenta = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantilla.get(0), true);
        	for(ImagenPlantillaVenta imagen : lstImagenPlantillaVenta) {
        		XWPFParagraph  paragraphh = document.createParagraph();
                String imagePath = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+imagen.getNombre();
                FileInputStream imageStream = new FileInputStream(imagePath);

                // Agregar datos de la imagen al documento
                int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageName = "nombre_de_la_imagen";
                document.addPictureData(imageStream, pictureType);

                // Crear un objeto XWPFPicture para insertar la imagen en el documento
                XWPFPicture picture = paragraphh.createRun().getParagraph().createRun().addPicture(new FileInputStream(imagePath), pictureType, imageName, Units.toEMU(400), Units.toEMU(400));
                
        	}
        	
        	
        }
        
        
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void formatoCredito_higal3_etapa4() throws XmlException, InvalidFormatException, FileNotFoundException, IOException {
		String areaHectarea = contratoSelected.getLote().getProject().getAreaHectarea();
		String unidadCatastral = contratoSelected.getLote().getProject().getUnidadCatastral();
		String numPartidaElectronica = contratoSelected.getLote().getProject().getNumPartidaElectronica();
		String codigoPredio = contratoSelected.getLote().getProject().getCodigoPredio();
		
		ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, contratoSelected.getLote().getProject(),contratoSelected.getLote().getManzana()); 
		if(busqueda!=null) {
			areaHectarea = busqueda.getAreaHectarea();
			unidadCatastral = busqueda.getUnidadCatastral();
			numPartidaElectronica = busqueda.getNumPartidaElectronica();
			codigoPredio = busqueda.getCodigoPredio();
		}
		
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, ALDASA INMOBILIARIA S.A.C., CON RUC Nº 20607274526, REPRESENTADA POR SU " );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);				
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11352661 ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FISCAL EN AV. SANTA VICTORIA 719 URB. "
				+ "SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA)(LOS)SR. (A.) (ES.) ");estiloNormalTexto(run);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation()==null?"XXXXXXXXXXX": p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null?"XXXXXXXXXXX": p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni()==null?"XXXXXXXXXXX":p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone()==null?"XXXXXXXXXXX":p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail()==null?"XXXXXXXXXXX":p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress()==null?"XXXXXXXXXXX":p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(" Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:. ");estiloNormalTexto(run);
		
		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+codigoPredio.toUpperCase()+", ÁREA "
				+ areaHectarea.toUpperCase()+" U.C. "+unidadCatastral.toUpperCase()+", DISTRITO DE "+contratoSelected.getLote().getProject().getDistrict().getName().toUpperCase()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName().toUpperCase()+", DEPARTAMENTO DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName().toUpperCase()+", EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N° "+numPartidaElectronica.toUpperCase()+", ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL "+contratoSelected.getLote().getProject().getZonaRegistral().toUpperCase()+".");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE "
				+ "LOTIZACIÓN EL HIGAL III ETAPA IV, EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE "
				+ "ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° "+numPartidaElectronica+".");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
				+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("COMO INICIAL, EL MONTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoInicial())+" ("+numeroAletra.Convertir(contratoSelected.getMontoInicial()+"", true, "")+" SOLES) ");estiloNegritaTexto(run);
 		run = paragrapha.createRun();run.setText("CON DEPÓSITO(S) EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011-285-000100180945-46, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, CCI 80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
 		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO DIRECTO QUE BRINDA ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, "
				+ "DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();run.setText("- PAGO DEL SALDO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getNumeroCuota()+" ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("CUOTAS MENSUALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE EFECTUARÁN EL DÍA "+sdfDay.format(contratoSelected.getFechaPrimeraCuota())+" DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA CUAL PERTENECE AL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011 285 000100180945 46 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTAS DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062; ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, MEDIO NOTARIAL O REQUERIMIENTO ALGUNO, SI TRANSCURRIDO DICHO TÉRMINO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("INCURRE EN MORA, AUTOMÁTICAMENTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y DOCUMENTOS EMITIDOS POR ÉL,  SI EXISTIERA UN RECIBO DE PAGO "
				+ "EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER CANCELADO LAS ANTERIORES.");estiloNormalTexto(run);
	
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE "
				+ "ESTA CLÁUSULA TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE "+contratoSelected.getNumeroCuota()+" "
				+ "MESES, MZ "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE (S) "+contratoSelected.getLote().getNumberLote()+" DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA Y POSTERIOR ESCRITURA PÚBLICA DE COMPRA VENTA, PARA QUE PUEDA(N) REALIZAR "
				+ "LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, "
				+ "PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL INMUEBLE, "
				+ "MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA O ASPECTO, NI AJUSTES "
				+ "DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);

		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y "
				+ "DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE EL "
				+ "PROYECTO INMOBILIARIO.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO DEL PROYECTO.");estiloNormalTexto(run);

		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS "
				+ "O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR "
				+ "EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
				
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O "
				+ "PROCESO JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, PROCESAL Y/O "
				+ "ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UNA VEZ CANCELADO EL SALDO TOTAL POR LA COMPRA VENTA DEL INMUEBLE, ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) "
				+ "ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE "
				+ "Y ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA, UNA VEZ QUE HAYA REALIZADO EL PAGO TOTAL DEL SALDO DE LA COMPRA VENTA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE ESTAS GENEREN, "
				+ "ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE CORRESPONDAN "
				+ "ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO INDEPENDIENTE DE EL (LOS) "
				+ "LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y ÁREAS VERDES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA "
				+ "DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA AFECTAR "
				+ "EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE "
				+ "LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO N°1345 "
				+ "Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE ESTE MODO, "
				+ "EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE "
				+ "EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS "
				+ "COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR "
				+ "A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD "
				+ "DE AMBAS PARTES.");estiloNormalTexto(run);
		
		
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE A LA "
				+ "JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, LOS CONSIGNADOS "
				+ "EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME AL "
				+ "ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE "
				+ "5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS Y "
				+ "SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SI EL PAGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA "
				+ "PREPAGO DE CAPITAL; EN CASO, QUE EXISTA PREPAGO PARCIAL, SE OMITIRÁ INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y "
				+ "SE GENERA UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER ACORDADO CON ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("“LA PARTE VENDEDORA”.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO QUINTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TRES (03)CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS EN LA TERCERA CLÁUSULA DE ESTE CONTRATO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PUEDE UNILATERALMETE RESOLVER EL CONTRATO; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL SALDO DEUDOR A  ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA "
				+ "LO CUAL EL NOTIFICADOR SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO UNA "
				+ "IMAGEN FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE SE HAYAN "
				+ "EFECTUADO PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEXTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS "
				+ "NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();     
		
		
		// ************************** TABLAS **************************
		
		List<String> lstTextFirma = new ArrayList<>();
		String textF1 = "___________________________________/ALDASA INMOBILIARIA S.A.C./RUC: 20607274526";lstTextFirma.add(textF1);
		String textF2="";
		String textF3="";
		String textF4="";
		String textF5="";
		String textF6="";
		
		int contF = 1;
		for(Person p: lstPersonas) {
			if(contF==1) {
				textF2="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==2) {
				textF3="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==3) {
				textF4="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==4) {
				textF5="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==5) {
				textF6="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			
			contF++;
		}
		
		lstTextFirma.add(textF2);
		lstTextFirma.add(textF3);
		lstTextFirma.add(textF4);
		lstTextFirma.add(textF5);
		lstTextFirma.add(textF6);
		
		
		XWPFTable tableF = document.createTable(3, 2);   
        setTableWidth(tableF, "9000");  
        
        CTTblPr tblPr = tableF.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        
        int priColumnF=0;
        
        for (int rowIndex = 0; rowIndex < tableF.getNumberOfRows(); rowIndex++) {
            XWPFTableRow row2 = tableF.getRow(rowIndex);
            
            // Agregar primer celda
            XWPFParagraph paragraph1 = row2.getCell(0).addParagraph();
            paragraph1.setAlignment(ParagraphAlignment.CENTER);
            
            XWPFRun run1 = paragraph1.createRun();
            String[] parts = lstTextFirma.get(priColumnF).split("/");
            for(String texto: parts) {
            	run1.setText(texto);estiloNormalTexto(run1);
            	run1.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            
            priColumnF++;

            // Agregar segunda celda
            XWPFParagraph paragraph22 = row2.getCell(1).addParagraph();
            paragraph22.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run2 = paragraph22.createRun();
            String[] parts2 = lstTextFirma.get(priColumnF).split("/");
            for(String texto: parts2) {
            	run2.setText(texto);estiloNormalTexto(run2);
            	run2.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            priColumnF++;
        }
		
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "Empresa:  ALDASA INMOBILIARIA S.A.C.";lstTexto.add(text1);
		String text2 = "Fecha: "+ sdf.format(contratoSelected.getFechaVenta());lstTexto.add(text2); 
		String text3 = "Comprador(es): ";
		String text4 = "D.N.I: ";
		int cont = 1;
		for(Person p: lstPersonas) {
			
			if(lstPersonas.size()==cont){
				text3= text3+p.getSurnames()+" "+p.getNames();
				text4= text4+p.getDni();
			}else {
				text3= text3+p.getSurnames()+" "+p.getNames()+" / ";
				text4= text4+p.getDni()+" / ";
			}
			cont++;
		}
		
		lstTexto.add(text3.toUpperCase());
		lstTexto.add(text4.toUpperCase());
		
		String text5 = "Monto Total: S/"+String.format("%,.2f",contratoSelected.getMontoVenta());lstTexto.add(text5);
		String text6 = "Monto Deuda: S/"+String.format("%,.2f",contratoSelected.getMontoVenta().subtract(contratoSelected.getMontoInicial()));lstTexto.add(text6);
		String text7 = "N° Cuotas: "+contratoSelected.getNumeroCuota();lstTexto.add(text7);
		String text8 = "Moneda: Soles";lstTexto.add(text8);
		String text9 = "Cuotas Pendientes: "+contratoSelected.getNumeroCuota();lstTexto.add(text9);
		String text10 = "Interés: "+contratoSelected.getInteres()+"%";lstTexto.add(text10);
		String text11 = "Mz: "+contratoSelected.getLote().getManzana().getName() ;lstTexto.add(text11);
		String text12 = "Lote: "+contratoSelected.getLote().getNumberLote();lstTexto.add(text12);
		
		
		XWPFTable table1 = document.createTable(7, 2);   
		
        setTableWidth(table1, "9000");  
//        fillTable(table1, lstTexto);  
        mergeCellsHorizontal(table1,0,0,1);  
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = table1.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
            	estiloCentrarCeldaTabla(row1.getCell(0));
            }else {
            	XWPFTableRow row2 = table1.getRow(rowIndex);
            	row2.getCell(0).setText(lstTexto.get(priColumn));
            	priColumn++;
            	row2.getCell(1).setText(lstTexto.get(priColumn));
            	priColumn++;
            }
        }  
        
        paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();
        
        int cantRowsPagos = contratoSelected.getNumeroCuota()+4;
        XWPFTable tablePagos = document.createTable(cantRowsPagos, 6);
        
        setTableWidth(tablePagos, "9000");  
//      fillTable(table1, lstTexto);  
        mergeCellsHorizontal(tablePagos,0,0,5);
        mergeCellsHorizontal(tablePagos,tablePagos.getNumberOfRows()-1,0,1);  
        int index = 0;
        simularCuotas(contratoSelected);
        for (int rowIndex = 0; rowIndex < tablePagos.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = tablePagos.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE LA DEUDA");
            	estiloCentrarCeldaTabla(row1.getCell(0));
             
            }else if(rowIndex==1) {
            	XWPFTableRow row2 = tablePagos.getRow(rowIndex);
            	row2.getCell(0).setText("N° Cuotas");
            	row2.getCell(1).setText("Periodo");
            	row2.getCell(2).setText("Cuota inicial");
            	row2.getCell(3).setText("Cuota SI");
            	row2.getCell(4).setText("Interés");
            	row2.getCell(5).setText("Cuota Total");
            	
            	estiloCentrarCeldaTabla(row2.getCell(0));
            	estiloCentrarCeldaTabla(row2.getCell(1));
            	estiloCentrarCeldaTabla(row2.getCell(2));
            	estiloCentrarCeldaTabla(row2.getCell(3));
            	estiloCentrarCeldaTabla(row2.getCell(4));
            	estiloCentrarCeldaTabla(row2.getCell(5));
            }else {
            	Simulador sim = lstSimulador.get(index);
            	
            	XWPFTableRow rowContenido = tablePagos.getRow(rowIndex);
            	rowContenido.getCell(0).setText(sim.getNroCuota());
            	rowContenido.getCell(1).setText(sim.getFechaPago() != null?sdf.format(sim.getFechaPago()):""); 
            	rowContenido.getCell(2).setText("S/"+String.format("%,.2f",sim.getInicial()));
            	rowContenido.getCell(3).setText("S/"+String.format("%,.2f",sim.getCuotaSI()));
            	rowContenido.getCell(4).setText("S/"+String.format("%,.2f",sim.getInteres()));
            	rowContenido.getCell(5).setText("S/"+String.format("%,.2f",sim.getCuotaTotal()));
            	
            	estiloCentrarCeldaTabla(rowContenido.getCell(0));
            	estiloCentrarCeldaTabla(rowContenido.getCell(1));
            	estiloCentrarCeldaTabla(rowContenido.getCell(2));
            	estiloCentrarCeldaTabla(rowContenido.getCell(3));
            	estiloCentrarCeldaTabla(rowContenido.getCell(4));
            	estiloCentrarCeldaTabla(rowContenido.getCell(5));
            	
            	index++;
            	
            }
        }  
        
        
        List<PlantillaVenta> plantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
        if(!plantilla.isEmpty()) {
        	List<ImagenPlantillaVenta> lstImagenPlantillaVenta = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantilla.get(0), true);
        	for(ImagenPlantillaVenta imagen : lstImagenPlantillaVenta) {
        		XWPFParagraph  paragraphh = document.createParagraph();
                String imagePath = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+imagen.getNombre();
                FileInputStream imageStream = new FileInputStream(imagePath);

                // Agregar datos de la imagen al documento
                int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageName = "nombre_de_la_imagen";
                document.addPictureData(imageStream, pictureType);

                // Crear un objeto XWPFPicture para insertar la imagen en el documento
                XWPFPicture picture = paragraphh.createRun().getParagraph().createRun().addPicture(new FileInputStream(imagePath), pictureType, imageName, Units.toEMU(400), Units.toEMU(400));
                
        	}
        	
        	
        }
        
        
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void formatoCredito_VDA() throws IOException, XmlException, InvalidFormatException {
		String areaHectarea = contratoSelected.getLote().getProject().getAreaHectarea();
		String unidadCatastral = contratoSelected.getLote().getProject().getUnidadCatastral();
		String numPartidaElectronica = contratoSelected.getLote().getProject().getNumPartidaElectronica();
		String codigoPredio = contratoSelected.getLote().getProject().getCodigoPredio();
		
		ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, contratoSelected.getLote().getProject(),contratoSelected.getLote().getManzana()); 
		if(busqueda!=null) {
			areaHectarea = busqueda.getAreaHectarea();
			unidadCatastral = busqueda.getUnidadCatastral();
			numPartidaElectronica = busqueda.getNumPartidaElectronica();
			codigoPredio = busqueda.getCodigoPredio();
		}
		
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, ABARCA BIENES RAÍCES E.I.R.L., CON RUC Nº 20609173093, REPRESENTADA POR SU " );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("TITULAR GERENTE ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);				
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11389528 ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FISCAL EN AV. SANTA VICTORIA 719 URB. "
				+ "SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA)(LOS)SR. (A.) (ES.) ");estiloNormalTexto(run);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation()==null?"XXXXXXXXXXX": p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null?"XXXXXXXXXXX": p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni()==null?"XXXXXXXXXXX":p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone()==null?"XXXXXXXXXXX":p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail()==null?"XXXXXXXXXXX":p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress()==null?"XXXXXXXXXXX":p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(" Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON INTERVENCIÓN DE EL SR. ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DE NACIONALIDAD PERUANA, IDENTIFICADO CON DNI. N°44922055, DE OCUPACION EMPRESARIO, DE ESTADO CIVIL CASADO, BAJO RÉGIMEN DE SEPARACIÓN DE "
				+ "PATRIMONIOS INSCRITO EN LA P.E. Nº 11385293 DEL REGISTRO PERSONAL DE LA OR DE CHICLAYO, CON DOMICILIO EN CALLE INDUSTRIAL 681 URB. SAN LORENZO, DISTRITO DE JOSÉ LEONARDO "
				+ "ORTIZ, PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; CON EL RESPALDO COMERCIAL DE ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC Nº 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11352661 ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, "
				+ "DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);
	
		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+codigoPredio.toUpperCase()+", ÁREA "
				+ areaHectarea.toUpperCase()+" U.C. "+unidadCatastral.toUpperCase()+", DISTRITO DE "+contratoSelected.getLote().getProject().getDistrict().getName().toUpperCase()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName().toUpperCase()+", DEPARTAMENTO DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName().toUpperCase()+", EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N° "+numPartidaElectronica.toUpperCase()+", ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL "+contratoSelected.getLote().getProject().getZonaRegistral().toUpperCase()+".");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE "
				+ "LOTIZACIÓN VALLE DEL ÁGUILA I Y EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE "
				+ "ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° "+numPartidaElectronica+".");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
				+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("COMO INICIAL, EL MONTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoInicial())+" ("+numeroAletra.Convertir(contratoSelected.getMontoInicial()+"", true, "")+" SOLES) ");estiloNegritaTexto(run);
 		run = paragrapha.createRun();run.setText("CON DEPÓSITO(S) EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);

 		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO DIRECTO QUE BRINDA ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, "
				+ "DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();run.setText("- PAGO DEL SALDO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getNumeroCuota()+" ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("CUOTAS MENSUALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE EFECTUARÁN EL DÍA "+sdfDay.format(contratoSelected.getFechaPrimeraCuota())+" DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA CUAL PERTENECE AL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001685121664; ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, MEDIO NOTARIAL O REQUERIMIENTO ALGUNO, SI TRANSCURRIDO DICHO TÉRMINO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("INCURRE EN MORA, AUTOMÁTICAMENTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y DOCUMENTOS EMITIDOS POR ÉL, "
				+ "SI EXISTIERA UN RECIBO DE PAGO EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER CANCELADO LAS ANTERIORES.");estiloNormalTexto(run);		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE "
				+ "ESTA CLÁUSULA TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE "+contratoSelected.getNumeroCuota()+" "
				+ "MESES, MZ "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE (S) "+contratoSelected.getLote().getNumberLote()+" DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA Y POSTERIOR ESCRITURA PÚBLICA DE COMPRA VENTA, PARA QUE PUEDA(N) REALIZAR "
				+ "LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, "
				+ "PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL INMUEBLE, "
				+ "MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA O ASPECTO, NI AJUSTES "
				+ "DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);

		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y "
				+ "DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE EL "
				+ "PROYECTO INMOBILIARIO.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO DEL PROYECTO.");estiloNormalTexto(run);

		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS "
				+ "O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR "
				+ "EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
				
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O "
				+ "PROCESO JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, PROCESAL Y/O "
				+ "ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UNA VEZ CANCELADO EL SALDO TOTAL POR LA COMPRA VENTA DEL INMUEBLE, ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) "
				+ "ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE "
				+ "Y ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA, UNA VEZ QUE HAYA REALIZADO EL PAGO TOTAL DEL SALDO DE LA COMPRA VENTA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE ESTAS GENEREN, "
				+ "ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE CORRESPONDAN "
				+ "ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO INDEPENDIENTE DE EL (LOS) "
				+ "LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y ÁREAS VERDES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA "
				+ "DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA AFECTAR "
				+ "EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE "
				+ "LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO N°1345 "
				+ "Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE ESTE MODO, "
				+ "EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE "
				+ "EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS "
				+ "COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR "
				+ "A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD "
				+ "DE AMBAS PARTES.");estiloNormalTexto(run);
		
		
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE A LA "
				+ "JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, LOS CONSIGNADOS "
				+ "EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME AL "
				+ "ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE "
				+ "5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS Y "
				+ "SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SI EL PAGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA "
				+ "PREPAGO DE CAPITAL; EN CASO, QUE EXISTA PREPAGO PARCIAL, SE OMITIRÁ INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y "
				+ "SE GENERA UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER ACORDADO CON ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("“LA PARTE VENDEDORA”.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO QUINTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TRES (03)CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS EN LA TERCERA CLÁUSULA DE ESTE CONTRATO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PUEDE UNILATERALMETE RESOLVER EL CONTRATO; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL SALDO DEUDOR A  ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA "
				+ "LO CUAL EL NOTIFICADOR SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO UNA "
				+ "IMAGEN FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE SE HAYAN "
				+ "EFECTUADO PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEXTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS "
				+ "NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();     
		
		
		// ************************** TABLAS **************************
		
		List<String> lstTextFirma = new ArrayList<>();
		String textF1 = "___________________________________/ABARCA BIENES RAICES E.I.R.L/RUC: 20609173093";lstTextFirma.add(textF1);
		String textF2="";
		String textF3="";
		String textF4="";
		String textF5="";
		String textF6="";
		
		int contF = 1;
		for(Person p: lstPersonas) {
			if(contF==1) {
				textF2="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==2) {
				textF3="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==3) {
				textF4="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==4) {
				textF5="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(contF==5) {
				textF6="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			
			contF++;
		}
		
		lstTextFirma.add(textF2);
		lstTextFirma.add(textF3);
		lstTextFirma.add(textF4);
		lstTextFirma.add(textF5);
		lstTextFirma.add(textF6);
		
		
		XWPFTable tableF = document.createTable(3, 2);   
        setTableWidth(tableF, "9000");  
        
        CTTblPr tblPr = tableF.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        
        int priColumnF=0;
        
        for (int rowIndex = 0; rowIndex < tableF.getNumberOfRows(); rowIndex++) {
            XWPFTableRow row2 = tableF.getRow(rowIndex);
            
            // Agregar primer celda
            XWPFParagraph paragraph1 = row2.getCell(0).addParagraph();
            paragraph1.setAlignment(ParagraphAlignment.CENTER);
            
            XWPFRun run1 = paragraph1.createRun();
            String[] parts = lstTextFirma.get(priColumnF).split("/");
            for(String texto: parts) {
            	run1.setText(texto);estiloNormalTexto(run1);
            	run1.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            
            priColumnF++;

            // Agregar segunda celda
            XWPFParagraph paragraph22 = row2.getCell(1).addParagraph();
            paragraph22.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run2 = paragraph22.createRun();
            String[] parts2 = lstTextFirma.get(priColumnF).split("/");
            for(String texto: parts2) {
            	run2.setText(texto);estiloNormalTexto(run2);
            	run2.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            priColumnF++;
        }
		
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "Empresa:  ABARCA BIENES RAICES E.I.R.L.";lstTexto.add(text1);
		String text2 = "Fecha: "+ sdf.format(contratoSelected.getFechaVenta());lstTexto.add(text2); 
		String text3 = "Comprador(es): ";
		String text4 = "D.N.I: ";
		int cont = 1;
		for(Person p: lstPersonas) {
			
			if(lstPersonas.size()==cont){
				text3= text3+p.getSurnames()+" "+p.getNames();
				text4= text4+p.getDni();
			}else {
				text3= text3+p.getSurnames()+" "+p.getNames()+" / ";
				text4= text4+p.getDni()+" / ";
			}
			cont++;
		}
		
		lstTexto.add(text3.toUpperCase());
		lstTexto.add(text4.toUpperCase());
		
		String text5 = "Monto Total: S/"+String.format("%,.2f",contratoSelected.getMontoVenta());lstTexto.add(text5);
		String text6 = "Monto Deuda: S/"+String.format("%,.2f",contratoSelected.getMontoVenta().subtract(contratoSelected.getMontoInicial()));lstTexto.add(text6);
		String text7 = "N° Cuotas: "+contratoSelected.getNumeroCuota();lstTexto.add(text7);
		String text8 = "Moneda: Soles";lstTexto.add(text8);
		String text9 = "Cuotas Pendientes: "+contratoSelected.getNumeroCuota();lstTexto.add(text9);
		String text10 = "Interés: "+contratoSelected.getInteres()+"%";lstTexto.add(text10);
		String text11 = "Mz: "+contratoSelected.getLote().getManzana().getName() ;lstTexto.add(text11);
		String text12 = "Lote: "+contratoSelected.getLote().getNumberLote();lstTexto.add(text12);
		
		
		XWPFTable table1 = document.createTable(7, 2);   
		
        setTableWidth(table1, "9000");  
//        fillTable(table1, lstTexto);  
        mergeCellsHorizontal(table1,0,0,1);  
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = table1.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
            	estiloCentrarCeldaTabla(row1.getCell(0));
            }else {
            	XWPFTableRow row2 = table1.getRow(rowIndex);
            	row2.getCell(0).setText(lstTexto.get(priColumn));
            	priColumn++;
            	row2.getCell(1).setText(lstTexto.get(priColumn));
            	priColumn++;
            }
        }  
        
        paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();
        
        int cantRowsPagos = contratoSelected.getNumeroCuota()+4;
        XWPFTable tablePagos = document.createTable(cantRowsPagos, 6);
        
        setTableWidth(tablePagos, "9000");  
//      fillTable(table1, lstTexto);  
        mergeCellsHorizontal(tablePagos,0,0,5);
        mergeCellsHorizontal(tablePagos,tablePagos.getNumberOfRows()-1,0,1);  
        int index = 0;
        simularCuotas(contratoSelected);
        for (int rowIndex = 0; rowIndex < tablePagos.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = tablePagos.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE LA DEUDA");
            	estiloCentrarCeldaTabla(row1.getCell(0));
             
            }else if(rowIndex==1) {
            	XWPFTableRow row2 = tablePagos.getRow(rowIndex);
            	row2.getCell(0).setText("N° Cuotas");
            	row2.getCell(1).setText("Periodo");
            	row2.getCell(2).setText("Cuota inicial");
            	row2.getCell(3).setText("Cuota SI");
            	row2.getCell(4).setText("Interés");
            	row2.getCell(5).setText("Cuota Total");
            	
            	estiloCentrarCeldaTabla(row2.getCell(0));
            	estiloCentrarCeldaTabla(row2.getCell(1));
            	estiloCentrarCeldaTabla(row2.getCell(2));
            	estiloCentrarCeldaTabla(row2.getCell(3));
            	estiloCentrarCeldaTabla(row2.getCell(4));
            	estiloCentrarCeldaTabla(row2.getCell(5));
            }else {
            	Simulador sim = lstSimulador.get(index);
            	
            	XWPFTableRow rowContenido = tablePagos.getRow(rowIndex);
            	rowContenido.getCell(0).setText(sim.getNroCuota());
            	rowContenido.getCell(1).setText(sim.getFechaPago() != null?sdf.format(sim.getFechaPago()):""); 
            	rowContenido.getCell(2).setText("S/"+String.format("%,.2f",sim.getInicial()));
            	rowContenido.getCell(3).setText("S/"+String.format("%,.2f",sim.getCuotaSI()));
            	rowContenido.getCell(4).setText("S/"+String.format("%,.2f",sim.getInteres()));
            	rowContenido.getCell(5).setText("S/"+String.format("%,.2f",sim.getCuotaTotal()));
            	
            	estiloCentrarCeldaTabla(rowContenido.getCell(0));
            	estiloCentrarCeldaTabla(rowContenido.getCell(1));
            	estiloCentrarCeldaTabla(rowContenido.getCell(2));
            	estiloCentrarCeldaTabla(rowContenido.getCell(3));
            	estiloCentrarCeldaTabla(rowContenido.getCell(4));
            	estiloCentrarCeldaTabla(rowContenido.getCell(5));
            	
            	index++;
            	
            }
        }  
        
        
        List<PlantillaVenta> plantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
        if(!plantilla.isEmpty()) {
        	List<ImagenPlantillaVenta> lstImagenPlantillaVenta = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantilla.get(0), true);
        	for(ImagenPlantillaVenta imagen : lstImagenPlantillaVenta) {
        		XWPFParagraph  paragraphh = document.createParagraph();
                String imagePath = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+imagen.getNombre();
                FileInputStream imageStream = new FileInputStream(imagePath);

                // Agregar datos de la imagen al documento
                int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageName = "nombre_de_la_imagen";
                document.addPictureData(imageStream, pictureType);

                // Crear un objeto XWPFPicture para insertar la imagen en el documento
                XWPFPicture picture = paragraphh.createRun().getParagraph().createRun().addPicture(new FileInputStream(imagePath), pictureType, imageName, Units.toEMU(400), Units.toEMU(400));
                
        	}
        	
        	
        }
        
        
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}  

	public void formatoCredito_ASR_V() throws IOException, XmlException {
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, " );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);				
		run = paragraph2.createRun();run.setText("DE NACIONALIDAD PERUANA, IDENTIFICADO CON DNI. N°44922055, DE OCUPACION EMPRESARIO, DE ESTADO CIVIL "
				+ "CASADO, BAJO RÉGIMEN DE SEPARACIÓN DE PATRIMONIOS INSCRITO EN LA P.E. N° 11385293 DEL REGISTRO PERSONAL DE LA OR DE CHICLAYO, CON "
				+ "DOMICILIO EN CALLE INDUSTRIAL 681 URB. SAN LORENZO, DISTRITO DE JOSÉ LEONARDO ORTIZ, PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, "
				+ "A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
	
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation() == null ? "" :p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null ? "":p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress() == null ? "" : p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run);
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON INTERVENCIÓN Y EL RESPALDO COMERCIAL DE ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ABARCA BIENES RAÍCES E.I.R.L., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("RUC N° 20609173093, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("TITULAR GERENTE ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11389528 ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO FISCAL "
				+ "EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE Y");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC N° 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO EN AV. "
				+ "SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; EL CONTRATO SE CELEBRA "
				+ "CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);
		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. VALLE DE CHANCAY / SECTOR YENCALA BOGGIANO / PREDIO FUNDO SANTA ROSA – COD. PREDIO. 7_6159260_27110, ÁREA 1.9600 U.C. "
				+ "27110, DISTRITO DE LAMBAYEQUE, PROVINCIA DE LAMBAYEQUE, DEPARTAMENTO DE LAMBAYEQUE, EN LO SUCESIVO DENOMINADO EL BIEN. LOS LINDEROS, "
				+ "MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N°02266512, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N°II- SEDE CHICLAYO.");estiloNormalTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ "
				+ "EL PROYECTO DE LOTIZACIÓN LOS ALTOS DE SAN ROQUE V Y EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE "
				+ "ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° 02266512.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+(contratoSelected.getLote().getLimiteFrontal() == null ? "" : contratoSelected.getLote().getLimiteFrontal()));
		run.addBreak();
		run.setText("Fondo         : "+(contratoSelected.getLote().getLimiteFondo() == null ? "" : contratoSelected.getLote().getLimiteFondo()));
		run.addBreak();
		run.setText("Derecha     : "+(contratoSelected.getLote().getLimiteDerecha() == null ? "" : contratoSelected.getLote().getLimiteDerecha()));
		run.addBreak();
		run.setText("Izquierda    : "+(contratoSelected.getLote().getLimiteIzquierda() == null ? "" : contratoSelected.getLote().getLimiteIzquierda()));
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
				+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("COMO INICIAL, EL MONTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoInicial())+" ("+numeroAletra.Convertir(contratoSelected.getMontoInicial()+"", true, "")+" SOLES) ");estiloNegritaTexto(run);
 		run = paragrapha.createRun();run.setText("CON DEPÓSITO(S) EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
 		
// 		if(lstCuentaBancaria!=null) {
// 			int count = 1;
// 			for(CuentaBancaria cta:lstCuentaBancaria) {
// 				run = paragrapha.createRun();run.setText("EN CUENTA DEL ");estiloNormalTexto(run);
// 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
// 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
// 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
// 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
// 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
// 				
// 				if(count==lstCuentaBancaria.size()) {
// 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
// 				}else {
// 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
// 				}
// 				count++;
// 			}
// 		}
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO DIRECTO QUE BRINDA ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, "
				+ "DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();run.setText("- PAGO DEL SALDO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getNumeroCuota()+" ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("CUOTAS MENSUALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE EFECTUARÁN EL DÍA "+sdfDay.format(contratoSelected.getFechaPrimeraCuota())+" DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA CUAL PERTENECE AL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA; SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, "
				+ "MEDIO NOTARIAL O REQUERIMIENTO ALGUNO, SI TRANSCURRIDO DICHO TÉRMINO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("INCURRE EN MORA, AUTOMÁTICAMENTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y "
				+ "DOCUMENTOS EMITIDOS POR ÉL,  SI EXISTIERA UN RECIBO DE PAGO EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER CANCELADO "
				+ "LAS ANTERIORES.");estiloNormalTexto(run);

		
//		if(lstCuentaBancaria!=null) {
// 			int count = 1;
// 			for(CuentaBancaria cta:lstCuentaBancaria) {
// 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
// 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
// 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
// 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
// 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
// 				
// 				if(count==lstCuentaBancaria.size()) {
// 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
// 				}else {
// 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
// 				}
// 				count++;
// 			}
// 		}
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE "
				+ "ESTA CLÁUSULA TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE "+contratoSelected.getNumeroCuota()+" "
				+ "MESES, MZ "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE (S) "+contratoSelected.getLote().getNumberLote()+" DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE EL (LOS) LOTE(S), "
				+ "SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA, PARA QUE PUEDA(N) REALIZAR LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, "
				+ "MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE(LOS) LOTE(S), COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A EL (LOS) LOTE(S), "
				+ "SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, "
				+ "SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A EL (LOS) LOTE(S).");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE EL "
				+ "PROYECTO INMOBILIARIO.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO DEL PROYECTO.");estiloNormalTexto(run);

		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA "
				+ "EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, "
				+ "EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
				
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, "
				+ "LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO JUDICIAL DE PRESCRIPCIÓN "
				+ "ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, MEDIDA "
				+ "INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE "
				+ "DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
				+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA "
				+ "ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, IMPULSADO POR ALGÚN PRECARIO "
				+ "Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, PERSONAL "
				+ "Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
		run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI COMO LA OFICINA DEL REGISTRO "
				+ "PUBLICO CORRESPONDIENTE ASI COMO A REALIZAR EL MISMO PROCEDIMIENTO ANTE LA OFICINA DE REGISTROS PUBLICOS.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE ESTAS GENEREN, "
				+ "ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE CORRESPONDAN "
				+ "ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO INDEPENDIENTE DE EL (LOS) "
				+ "LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y ÁREAS VERDES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA "
				+ "DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE "
				+ "SERVICIO DE AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, "
				+ "QUE PUDIERA AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA "
				+ "A PARTIR DE LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO "
				+ "N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE "
				+ "ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) "
				+ "RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS "
				+ "OBLIGACIONES Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN "
				+ "DE 05 DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA "
				+ "SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
		
		
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, "
				+ "SOMETERSE A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y "
				+ "SEÑALANDO COMO TALES, LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
				+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
				+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
				+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SI EL PAGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA PREPAGO "
				+ "DE CAPITAL; EN CASO, QUE EXISTA PREPAGO PARCIAL, SE OMITIRÁ INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y SE GENERA "
				+ "UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER ACORDADO CON ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("“LA PARTE VENDEDORA”.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO QUINTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TRES (03)CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS "
				+ "EN LA TERCERA CLÁUSULA DE ESTE CONTRATO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PUEDE UNILATERALMETE RESOLVER EL CONTRATO; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL  DEUDOR A  ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA "
				+ "LO CUAL EL NOTIFICADOR SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO UNA IMAGEN "
				+ "FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE SE HAYAN EFECTUADO "
				+ "PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEXTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS "
				+ "NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
		run.setText("___________________________________");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "Empresa:  ALDASA INMOBILIARIA S.A.C.";lstTexto.add(text1);
		String text2 = "Fecha: "+ sdf.format(contratoSelected.getFechaVenta());lstTexto.add(text2); 
		String text3 = "Comprador(es): ";
		String text4 = "D.N.I: ";
		int cont = 1;
		for(Person p: lstPersonas) {
			
			if(lstPersonas.size()==cont){
				text3= text3+p.getSurnames()+" "+p.getNames();
				text4= text4+p.getDni();
			}else {
				text3= text3+p.getSurnames()+" "+p.getNames()+" / ";
				text4= text4+p.getDni()+" / ";
			}
			cont++;
		}
		
		lstTexto.add(text3.toUpperCase());
		lstTexto.add(text4.toUpperCase());
		
		String text5 = "Monto Total: S/"+String.format("%,.2f",contratoSelected.getMontoVenta());lstTexto.add(text5);
		String text6 = "Monto Deuda: S/"+String.format("%,.2f",contratoSelected.getMontoVenta().subtract(contratoSelected.getMontoInicial()));lstTexto.add(text6);
		String text7 = "N° Cuotas: "+contratoSelected.getNumeroCuota();lstTexto.add(text7);
		String text8 = "Moneda: Soles";lstTexto.add(text8);
		String text9 = "Cuotas Pendientes: "+contratoSelected.getNumeroCuota();lstTexto.add(text9);
		String text10 = "Interés: "+contratoSelected.getInteres()+"%";lstTexto.add(text10);
		String text11 = "Mz: "+contratoSelected.getLote().getManzana().getName() ;lstTexto.add(text11);
		String text12 = "Lote: "+contratoSelected.getLote().getNumberLote();lstTexto.add(text12);
		
		
		XWPFTable table1 = document.createTable(7, 2);   
		
        setTableWidth(table1, "9000");  
//        fillTable(table1, lstTexto);  
        mergeCellsHorizontal(table1,0,0,1);  
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = table1.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
            	estiloCentrarCeldaTabla(row1.getCell(0));
            }else {
            	XWPFTableRow row2 = table1.getRow(rowIndex);
            	row2.getCell(0).setText(lstTexto.get(priColumn));
            	priColumn++;
            	row2.getCell(1).setText(lstTexto.get(priColumn));
            	priColumn++;
            }
        }  
        
        paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();
        
        int cantRowsPagos = contratoSelected.getNumeroCuota()+4;
        XWPFTable tablePagos = document.createTable(cantRowsPagos, 6);
        
        setTableWidth(tablePagos, "9000");  
//      fillTable(table1, lstTexto);  
        mergeCellsHorizontal(tablePagos,0,0,5);
        mergeCellsHorizontal(tablePagos,tablePagos.getNumberOfRows()-1,0,1);  
        int index = 0;
        simularCuotas(contratoSelected);
        for (int rowIndex = 0; rowIndex < tablePagos.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = tablePagos.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE LA DEUDA");
            	estiloCentrarCeldaTabla(row1.getCell(0));
             
            }else if(rowIndex==1) {
            	XWPFTableRow row2 = tablePagos.getRow(rowIndex);
            	row2.getCell(0).setText("N° Cuotas");
            	row2.getCell(1).setText("Periodo");
            	row2.getCell(2).setText("Cuota inicial");
            	row2.getCell(3).setText("Cuota SI");
            	row2.getCell(4).setText("Interés");
            	row2.getCell(5).setText("Cuota Total");
            	
            	estiloCentrarCeldaTabla(row2.getCell(0));
            	estiloCentrarCeldaTabla(row2.getCell(1));
            	estiloCentrarCeldaTabla(row2.getCell(2));
            	estiloCentrarCeldaTabla(row2.getCell(3));
            	estiloCentrarCeldaTabla(row2.getCell(4));
            	estiloCentrarCeldaTabla(row2.getCell(5));
            }else {
            	Simulador sim = lstSimulador.get(index);
            	
            	XWPFTableRow rowContenido = tablePagos.getRow(rowIndex);
            	rowContenido.getCell(0).setText(sim.getNroCuota());
            	rowContenido.getCell(1).setText(sim.getFechaPago() != null?sdf.format(sim.getFechaPago()):""); 
            	rowContenido.getCell(2).setText("S/"+String.format("%,.2f",sim.getInicial()));
            	rowContenido.getCell(3).setText("S/"+String.format("%,.2f",sim.getCuotaSI()));
            	rowContenido.getCell(4).setText("S/"+String.format("%,.2f",sim.getInteres()));
            	rowContenido.getCell(5).setText("S/"+String.format("%,.2f",sim.getCuotaTotal()));
            	
            	estiloCentrarCeldaTabla(rowContenido.getCell(0));
            	estiloCentrarCeldaTabla(rowContenido.getCell(1));
            	estiloCentrarCeldaTabla(rowContenido.getCell(2));
            	estiloCentrarCeldaTabla(rowContenido.getCell(3));
            	estiloCentrarCeldaTabla(rowContenido.getCell(4));
            	estiloCentrarCeldaTabla(rowContenido.getCell(5));
            	
            	index++;
            	
            }
        }  

	        
//	        XWPFTable table = document.createTable();
	        
        //Creating first Row
//        XWPFTableRow row1 = table.getRow(0);
//        XWPFTableCell cell = table.getRow(0).getCell(0);
//        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//        row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
//
//        //Creating second Row
//        XWPFTableRow row2 = table.createRow();
//        row2.getCell(0).setText("Second Row, First Column");
//        row2.getCell(1).setText("Second Row, Second Column");
//
//        //create third row
//        XWPFTableRow row3 = table.createRow();
//        row3.getCell(0).setText("Third Row, First Column");
//        row3.getCell(1).setText("Third Row, Second Column");
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}  
	
	public void formatoCredito_ASR_IV() throws IOException, XmlException {
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, ABARCA BIENES RAÍCES E.I.R.L., CON RUC N° 20609173093, "
				+ "REPRESENTADA POR SU" );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("TITULAR GERENTE ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);				
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11389528 "); estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO FISCAL "
				+ "EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE "
				+ "DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run);
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON INTERVENCIÓN DE EL SR. ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DE NACIONALIDAD PERUANA, IDENTIFICADO CON DNI. N°44922055, DE OCUPACION EMPRESARIO, DE ESTADO CIVIL "
				+ "CASADO, BAJO RÉGIMEN DE SEPARACIÓN DE PATRIMONIOS INSCRITO EN LA P.E. N° 11385293 DEL REGISTRO PERSONAL DE LA OR DE CHICLAYO, CON "
				+ "DOMICILIO EN CALLE INDUSTRIAL 681 URB. SAN LORENZO, DISTRITO DE JOSÉ LEONARDO ORTIZ, PROVINCIA DE CHICLAYO, DEPARTAMENTO DE "
				+ "LAMBAYEQUE; CON EL RESPALDO COMERCIAL DE ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC N° 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO EN AV. "
				+ "SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; EL CONTRATO SE CELEBRA CON "
				+ "ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);


		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. VALLE DE CHANCAY / SECTOR YENCALA BOGGIANO / PREDIO YENCALA BOGGIANO– COD. PREDIO. 7_6159260_67034, ÁREA 4.1730 U.C. "
				+ "67034, DISTRITO DE LAMBAYEQUE, PROVINCIA DE LAMBAYEQUE, DEPARTAMENTO DE LAMBAYEQUE, EN LO SUCESIVO DENOMINADO EL BIEN. LOS LINDEROS, "
				+ "MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N°11072174, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N°II- SEDE CHICLAYO.");estiloNormalTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("QUE CON FECHA 18 DE MARZO DE 2023 SE SUSCRIBIÓ UN CONTRATO DE COMPRA VENTA CON ARRAS CONFIRMATORIAS ENTRE ALAN CRUZADO BALCAZAR Y "
				+ "ABARCA BIENES RAÍCES EIRL, MEDIANTE EL CUAL SE ADQUIRIO LA PROPIEDAD DESCRTITA EN LA CLAUSULA 1.1 PARA EL DESARROLLO DEL PROYECTO DE "
				+ "LOTIZACION MATERIA DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("3. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ "
				+ "EL PROYECTO DE LOTIZACIÓN LOS ALTOS DE SAN ROQUE IV Y EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE "
				+ "CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° 11072174.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
				+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("COMO INICIAL, EL MONTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoInicial())+" ("+numeroAletra.Convertir(contratoSelected.getMontoInicial()+"", true, "")+" SOLES) ");estiloNegritaTexto(run);
 		run = paragrapha.createRun();run.setText("CON DEPÓSITO(S) EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
 		
// 		if(lstCuentaBancaria!=null) {
// 			int count = 1;
// 			for(CuentaBancaria cta:lstCuentaBancaria) {
// 				run = paragrapha.createRun();run.setText("EN CUENTA DEL ");estiloNormalTexto(run);
// 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
// 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
// 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
// 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
// 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
// 				
// 				if(count==lstCuentaBancaria.size()) {
// 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
// 				}else {
// 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
// 				}
// 				count++;
// 			}
// 		}
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO DIRECTO QUE BRINDA ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, "
				+ "DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();run.setText("- PAGO DEL SALDO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getNumeroCuota()+" ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("CUOTAS MENSUALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE EFECTUARÁN EL DÍA "+sdfDay.format(contratoSelected.getFechaPrimeraCuota())+" DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA CUAL PERTENECE AL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA; SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, "
				+ "MEDIO NOTARIAL O REQUERIMIENTO ALGUNO, SI TRANSCURRIDO DICHO TÉRMINO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("INCURRE EN MORA, AUTOMÁTICAMENTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y "
				+ "DOCUMENTOS EMITIDOS POR ÉL,  SI EXISTIERA UN RECIBO DE PAGO EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER CANCELADO "
				+ "LAS ANTERIORES.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE "
				+ "ESTA CLÁUSULA TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE "+contratoSelected.getNumeroCuota()+" "
				+ "MESES, MZ "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE (S) "+contratoSelected.getLote().getNumberLote()+" DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE EL (LOS) LOTE(S), "
				+ "SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA, PARA QUE PUEDA(N) REALIZAR LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, "
				+ "MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE(LOS) LOTE(S), COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A EL (LOS) LOTE(S), "
				+ "SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, "
				+ "SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A EL (LOS) LOTE(S).");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE EL "
				+ "PROYECTO INMOBILIARIO.");estiloNormalTexto(run);

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO DEL PROYECTO.");estiloNormalTexto(run);

		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA "
				+ "EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, "
				+ "EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
				
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, "
				+ "LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO JUDICIAL DE PRESCRIPCIÓN "
				+ "ADQUISITIVA DE DOMINIO, REIVINDICACIN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, MEDIDA "
				+ "INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE "
				+ "DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
				+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA "
				+ "ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, IMPULSADO POR ALGÚN PRECARIO "
				+ "Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, PERSONAL "
				+ "Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
		run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI COMO LA OFICINA DEL REGISTRO "
				+ "PUBLICO CORRESPONDIENTE ASI COMO A REALIZAR EL MISMO PROCEDIMIENTO ANTE LA OFICINA DE REGISTROS PUBLICOS.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE ESTAS GENEREN, "
				+ "ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE CORRESPONDAN "
				+ "ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO INDEPENDIENTE DE EL (LOS) "
				+ "LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y ÁREAS VERDES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA "
				+ "DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE "
				+ "SERVICIO DE AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, "
				+ "QUE PUDIERA AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA "
				+ "A PARTIR DE LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO "
				+ "N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE "
				+ "ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) "
				+ "RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS "
				+ "OBLIGACIONES Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN "
				+ "DE 05 DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA "
				+ "SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
		
		
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, "
				+ "SOMETERSE A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y "
				+ "SEÑALANDO COMO TALES, LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
				+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
				+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
				+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SI EL PAGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA PREPAGO "
				+ "DE CAPITAL; EN CASO, QUE EXISTA PREPAGO PARCIAL, SE OMITIRÁ INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y SE GENERA "
				+ "UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER ACORDADO CON ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("“LA PARTE VENDEDORA”.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO QUINTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TRES (03)CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS EN LA TERCERA CLÁUSULA DE ESTE CONTRATO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PUEDE UNILATERALMETE RESOLVER EL CONTRATO AL AMPARO DEL ARTÍCULO 1430 DEL CÓDIGO CIVIL; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL SALDO DEUDOR A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA "
				+ "LO CUAL EL NOTIFICADOR SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO UNA IMAGEN "
				+ "FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE SE HAYAN EFECTUADO "
				+ "PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEXTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS "
				+ "NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
		run.setText("___________________________________");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "Empresa:  ALDASA INMOBILIARIA S.A.C.";lstTexto.add(text1);
		String text2 = "Fecha: "+ sdf.format(contratoSelected.getFechaVenta());lstTexto.add(text2); 
		String text3 = "Comprador(es): ";
		String text4 = "D.N.I: ";
		int cont = 1;
		for(Person p: lstPersonas) {
			
			if(lstPersonas.size()==cont){
				text3= text3+p.getSurnames()+" "+p.getNames();
				text4= text4+p.getDni();
			}else {
				text3= text3+p.getSurnames()+" "+p.getNames()+" / ";
				text4= text4+p.getDni()+" / ";
			}
			cont++;
		}
		
		lstTexto.add(text3.toUpperCase());
		lstTexto.add(text4.toUpperCase());
		
		String text5 = "Monto Total: S/"+String.format("%,.2f",contratoSelected.getMontoVenta());lstTexto.add(text5);
		String text6 = "Monto Deuda: S/"+String.format("%,.2f",contratoSelected.getMontoVenta().subtract(contratoSelected.getMontoInicial()));lstTexto.add(text6);
		String text7 = "N° Cuotas: "+contratoSelected.getNumeroCuota();lstTexto.add(text7);
		String text8 = "Moneda: Soles";lstTexto.add(text8);
		String text9 = "Cuotas Pendientes: "+contratoSelected.getNumeroCuota();lstTexto.add(text9);
		String text10 = "Interés: "+contratoSelected.getInteres()+"%";lstTexto.add(text10);
		String text11 = "Mz: "+contratoSelected.getLote().getManzana().getName() ;lstTexto.add(text11);
		String text12 = "Lote: "+contratoSelected.getLote().getNumberLote();lstTexto.add(text12);
		
		
		XWPFTable table1 = document.createTable(7, 2);   
		
        setTableWidth(table1, "9000");  
//        fillTable(table1, lstTexto);  
        mergeCellsHorizontal(table1,0,0,1);  
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = table1.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
            	estiloCentrarCeldaTabla(row1.getCell(0));
            }else {
            	XWPFTableRow row2 = table1.getRow(rowIndex);
            	row2.getCell(0).setText(lstTexto.get(priColumn));
            	priColumn++;
            	row2.getCell(1).setText(lstTexto.get(priColumn));
            	priColumn++;
            }
        }  
        
        paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();
        
        int cantRowsPagos = contratoSelected.getNumeroCuota()+4;
        XWPFTable tablePagos = document.createTable(cantRowsPagos, 6);
        
        setTableWidth(tablePagos, "9000");  
//      fillTable(table1, lstTexto);  
        mergeCellsHorizontal(tablePagos,0,0,5);
        mergeCellsHorizontal(tablePagos,tablePagos.getNumberOfRows()-1,0,1);  
        int index = 0;
        simularCuotas(contratoSelected);
        for (int rowIndex = 0; rowIndex < tablePagos.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = tablePagos.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE LA DEUDA");
            	estiloCentrarCeldaTabla(row1.getCell(0));
             
            }else if(rowIndex==1) {
            	XWPFTableRow row2 = tablePagos.getRow(rowIndex);
            	row2.getCell(0).setText("N° Cuotas");
            	row2.getCell(1).setText("Periodo");
            	row2.getCell(2).setText("Cuota inicial");
            	row2.getCell(3).setText("Cuota SI");
            	row2.getCell(4).setText("Interés");
            	row2.getCell(5).setText("Cuota Total");
            	
            	estiloCentrarCeldaTabla(row2.getCell(0));
            	estiloCentrarCeldaTabla(row2.getCell(1));
            	estiloCentrarCeldaTabla(row2.getCell(2));
            	estiloCentrarCeldaTabla(row2.getCell(3));
            	estiloCentrarCeldaTabla(row2.getCell(4));
            	estiloCentrarCeldaTabla(row2.getCell(5));
            }else {
            	Simulador sim = lstSimulador.get(index);
            	
            	XWPFTableRow rowContenido = tablePagos.getRow(rowIndex);
            	rowContenido.getCell(0).setText(sim.getNroCuota());
            	rowContenido.getCell(1).setText(sim.getFechaPago() != null?sdf.format(sim.getFechaPago()):""); 
            	rowContenido.getCell(2).setText("S/"+String.format("%,.2f",sim.getInicial()));
            	rowContenido.getCell(3).setText("S/"+String.format("%,.2f",sim.getCuotaSI()));
            	rowContenido.getCell(4).setText("S/"+String.format("%,.2f",sim.getInteres()));
            	rowContenido.getCell(5).setText("S/"+String.format("%,.2f",sim.getCuotaTotal()));
            	
            	estiloCentrarCeldaTabla(rowContenido.getCell(0));
            	estiloCentrarCeldaTabla(rowContenido.getCell(1));
            	estiloCentrarCeldaTabla(rowContenido.getCell(2));
            	estiloCentrarCeldaTabla(rowContenido.getCell(3));
            	estiloCentrarCeldaTabla(rowContenido.getCell(4));
            	estiloCentrarCeldaTabla(rowContenido.getCell(5));
            	
            	index++;
            	
            }
        }  

	        
//	        XWPFTable table = document.createTable();
	        
        //Creating first Row
//        XWPFTableRow row1 = table.getRow(0);
//        XWPFTableCell cell = table.getRow(0).getCell(0);
//        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//        row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
//
//        //Creating second Row
//        XWPFTableRow row2 = table.createRow();
//        row2.getCell(0).setText("Second Row, First Column");
//        row2.getCell(1).setText("Second Row, Second Column");
//
//        //create third row
//        XWPFTableRow row3 = table.createRow();
//        row3.getCell(0).setText("Third Row, First Column");
//        row3.getCell(1).setText("Third Row, Second Column");
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}  
	
	
	//******************************************************************************************************************
		
	public void formatoContado_higal3_etapa1() throws XmlException, InvalidFormatException, IOException  {
		String areaHectarea = contratoSelected.getLote().getProject().getAreaHectarea();
		String unidadCatastral = contratoSelected.getLote().getProject().getUnidadCatastral();
		String numPartidaElectronica = contratoSelected.getLote().getProject().getNumPartidaElectronica();
		String codigoPredio = contratoSelected.getLote().getProject().getCodigoPredio();
		
		ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, contratoSelected.getLote().getProject(),contratoSelected.getLote().getManzana()); 
		if(busqueda!=null) {
			areaHectarea = busqueda.getAreaHectarea();
			unidadCatastral = busqueda.getUnidadCatastral();
			numPartidaElectronica = busqueda.getNumPartidaElectronica();
			codigoPredio = busqueda.getCodigoPredio();
		}
		
		
		
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, " );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC Nº 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FISCAL EN AV. SANTA "
				+ "VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");estiloNormalTexto(run);
		run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
			
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation()==null?"XXXXXXXXXXX": p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null?"XXXXXXXXXXX": p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni()==null?"XXXXXXXXXXX":p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone()==null?"XXXXXXXXXXX":p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail()==null?"XXXXXXXXXXX":p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress()==null?"XXXXXXXXXXX":p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(" Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);
		
		
			
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.addBreak();runPrimero.addBreak();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+codigoPredio.toUpperCase()+", ÁREA "
				+ areaHectarea.toUpperCase()+" U.C. "+unidadCatastral.toUpperCase()+", DISTRITO DE "+contratoSelected.getLote().getProject().getDistrict().getName().toUpperCase()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName().toUpperCase()+", DEPARTAMENTO DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName().toUpperCase()+", EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N° "+numPartidaElectronica.toUpperCase()+", ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL "+contratoSelected.getLote().getProject().getZonaRegistral().toUpperCase()+".");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN HIGAL III ETAPA I, "
				+ "EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA "
				+ "DE ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");estiloNormalTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° "+numPartidaElectronica.toUpperCase()+".");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+ (contratoSelected.getLote().getLimiteFrontal()== null?"":contratoSelected.getLote().getLimiteFrontal().toUpperCase()));
		run.addBreak();
		run.setText("Fondo         : "+(contratoSelected.getLote().getLimiteFondo() == null ? "" : contratoSelected.getLote().getLimiteFondo().toUpperCase()));
		run.addBreak();
		run.setText("Derecha     : "+(contratoSelected.getLote().getLimiteDerecha() == null ? "" : contratoSelected.getLote().getLimiteDerecha().toUpperCase()));
		run.addBreak();
		run.setText("Izquierda    : "+(contratoSelected.getLote().getLimiteIzquierda() == null ? "" : contratoSelected.getLote().getLimiteIzquierda().toUpperCase()));
		run.addBreak();run.addBreak();

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) PORPORCIONADAS "
				+ "MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL. ");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA - VENTA.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DEPÓSITO(S) EN CUENTA(S) DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011 285 000100180945 46 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, CCI 80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
			
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA DECLARA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL INMUEBLE, MOTIVO POR EL CUAL NO SE "
				+ "ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL "
				+ "INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);
		
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES "
				+ "Y DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
				
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ DESPUÉS DE LA CANCELACIÓN DEL PAGO TOTAL DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("POR PARTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DEL DEPÓSITO REALIZADO EN LA CUENTA DE LA PARTE VENDEDORA, PARA LUEGO "
				+ "REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE Y POSTERIOR ESCRITURA PÚBLICA.");estiloNormalTexto(run);

			
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE "
				+ "EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO "
				+ "MAXIMO DEL PROYECTO.");estiloNormalTexto(run);	
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA "
				+ "DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA "
				+ "A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
			
				
					
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();	
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODO DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO "
				+ "JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
				+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
		
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE ENCUENTRA "
				+ "AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER "
				+ "CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
					
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI "
				+ "COMO LA OFICINA DEL REGISTRO PUBLICO CORRESPONDIENTE.");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE "
				+ "ESTAS GENEREN, ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU "
				+ "INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE "
				+ "CORRESPONDAN ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO "
				+ "INDEPENDIENTE DE EL (LOS) LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
			
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y AREAS VERDES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.  ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA "
				+ "AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA A PARTIR DE LA "
				+ "CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENERE LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO "
				+ "N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE "
				+ "ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) "
				+ "RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES "
				+ "Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 "
				+ "DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL "
				+ "DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE "
				+ "A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, "
				+ "LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
				+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
				+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
				+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
				+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
			
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();     
				
			
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "___________________________________/ALDASA INMOBILIARIA S.A.C./RUC: 20607274526";lstTexto.add(text1);
		String text2="";
		String text3="";
		String text4="";
		String text5="";
		String text6="";
		
		int cont = 1;
		for(Person p: lstPersonas) {
			if(cont==1) {
				text2="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==2) {
				text3="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==3) {
				text4="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==4) {
				text5="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==5) {
				text6="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			
			cont++;
		}
		
		lstTexto.add(text2);
		lstTexto.add(text3);
		lstTexto.add(text4);
		lstTexto.add(text5);
		lstTexto.add(text6);
		
		
		XWPFTable table1 = document.createTable(3, 2);   
        setTableWidth(table1, "9000");  
        
        CTTblPr tblPr = table1.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {
            XWPFTableRow row2 = table1.getRow(rowIndex);
            
            // Agregar primer celda
            XWPFParagraph paragraph1 = row2.getCell(0).addParagraph();
            paragraph1.setAlignment(ParagraphAlignment.CENTER);
            
            XWPFRun run1 = paragraph1.createRun();
            String[] parts = lstTexto.get(priColumn).split("/");
            for(String texto: parts) {
            	run1.setText(texto);estiloNormalTexto(run1);
            	run1.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            
            priColumn++;

            // Agregar segunda celda
            XWPFParagraph paragraph22 = row2.getCell(1).addParagraph();
            paragraph22.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run2 = paragraph22.createRun();
            String[] parts2 = lstTexto.get(priColumn).split("/");
            for(String texto: parts2) {
            	run2.setText(texto);estiloNormalTexto(run2);
            	run2.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            priColumn++;
        }
        
        
        List<PlantillaVenta> plantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
        if(!plantilla.isEmpty()) {
        	List<ImagenPlantillaVenta> lstImagenPlantillaVenta = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantilla.get(0), true);
        	for(ImagenPlantillaVenta imagen : lstImagenPlantillaVenta) {
        		XWPFParagraph  paragraphh = document.createParagraph();
                String imagePath = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+imagen.getNombre();
                FileInputStream imageStream = new FileInputStream(imagePath);

                // Agregar datos de la imagen al documento
                int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageName = "nombre_de_la_imagen";
                document.addPictureData(imageStream, pictureType);

                // Crear un objeto XWPFPicture para insertar la imagen en el documento
                XWPFPicture picture = paragraphh.createRun().getParagraph().createRun().addPicture(new FileInputStream(imagePath), pictureType, imageName, Units.toEMU(400), Units.toEMU(400));
                
        	}
        	
        	
        }
       
				
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void formatoContado_higal3_etapa3() throws XmlException, InvalidFormatException, IOException  {
		String areaHectarea = contratoSelected.getLote().getProject().getAreaHectarea();
		String unidadCatastral = contratoSelected.getLote().getProject().getUnidadCatastral();
		String numPartidaElectronica = contratoSelected.getLote().getProject().getNumPartidaElectronica();
		String codigoPredio = contratoSelected.getLote().getProject().getCodigoPredio();
		
		ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, contratoSelected.getLote().getProject(),contratoSelected.getLote().getManzana()); 
		if(busqueda!=null) {
			areaHectarea = busqueda.getAreaHectarea();
			unidadCatastral = busqueda.getUnidadCatastral();
			numPartidaElectronica = busqueda.getNumPartidaElectronica();
			codigoPredio = busqueda.getCodigoPredio();
		}
		
		
		
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, " );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC Nº 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FISCAL EN AV. SANTA "
				+ "VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");estiloNormalTexto(run);
		run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
			
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation()==null?"XXXXXXXXXXX": p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null?"XXXXXXXXXXX": p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni()==null?"XXXXXXXXXXX":p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone()==null?"XXXXXXXXXXX":p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail()==null?"XXXXXXXXXXX":p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress()==null?"XXXXXXXXXXX":p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(" Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);
		
		
			
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.addBreak();runPrimero.addBreak();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+codigoPredio.toUpperCase()+", ÁREA "
				+ areaHectarea.toUpperCase()+" U.C. "+unidadCatastral.toUpperCase()+", DISTRITO DE "+contratoSelected.getLote().getProject().getDistrict().getName().toUpperCase()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName().toUpperCase()+", DEPARTAMENTO DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName().toUpperCase()+", EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N° "+numPartidaElectronica.toUpperCase()+", ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL "+contratoSelected.getLote().getProject().getZonaRegistral().toUpperCase()+".");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN HIGAL III ETAPA III, "
				+ "EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA "
				+ "DE ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");estiloNormalTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° "+numPartidaElectronica.toUpperCase()+".");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+ (contratoSelected.getLote().getLimiteFrontal()== null?"":contratoSelected.getLote().getLimiteFrontal().toUpperCase()));
		run.addBreak();
		run.setText("Fondo         : "+(contratoSelected.getLote().getLimiteFondo() == null ? "" : contratoSelected.getLote().getLimiteFondo().toUpperCase()));
		run.addBreak();
		run.setText("Derecha     : "+(contratoSelected.getLote().getLimiteDerecha() == null ? "" : contratoSelected.getLote().getLimiteDerecha().toUpperCase()));
		run.addBreak();
		run.setText("Izquierda    : "+(contratoSelected.getLote().getLimiteIzquierda() == null ? "" : contratoSelected.getLote().getLimiteIzquierda().toUpperCase()));
		run.addBreak();run.addBreak();

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) PORPORCIONADAS "
				+ "MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL. ");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA - VENTA.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DEPÓSITO(S) EN CUENTA(S) DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011 285 000100180945 46 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, CCI 80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
			
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA DECLARA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL INMUEBLE, MOTIVO POR EL CUAL NO SE "
				+ "ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL "
				+ "INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);
		
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES "
				+ "Y DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
				
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ DESPUÉS DE LA CANCELACIÓN DEL PAGO TOTAL DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("POR PARTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DEL DEPÓSITO REALIZADO EN LA CUENTA DE LA PARTE VENDEDORA, PARA LUEGO "
				+ "REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE Y POSTERIOR ESCRITURA PÚBLICA.");estiloNormalTexto(run);

			
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE "
				+ "EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO "
				+ "MAXIMO DEL PROYECTO.");estiloNormalTexto(run);	
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA "
				+ "DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA "
				+ "A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
			
				
					
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();	
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODO DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO "
				+ "JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
				+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
		
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE ENCUENTRA "
				+ "AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER "
				+ "CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
					
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI "
				+ "COMO LA OFICINA DEL REGISTRO PUBLICO CORRESPONDIENTE.");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE "
				+ "ESTAS GENEREN, ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU "
				+ "INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE "
				+ "CORRESPONDAN ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO "
				+ "INDEPENDIENTE DE EL (LOS) LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
			
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y AREAS VERDES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.  ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA "
				+ "AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA A PARTIR DE LA "
				+ "CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENERE LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO "
				+ "N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE "
				+ "ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) "
				+ "RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES "
				+ "Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 "
				+ "DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL "
				+ "DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE "
				+ "A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, "
				+ "LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
				+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
				+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
				+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
				+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
			
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();     
				
			
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "___________________________________/ALDASA INMOBILIARIA S.A.C./RUC: 20607274526";lstTexto.add(text1);
		String text2="";
		String text3="";
		String text4="";
		String text5="";
		String text6="";
		
		int cont = 1;
		for(Person p: lstPersonas) {
			if(cont==1) {
				text2="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==2) {
				text3="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==3) {
				text4="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==4) {
				text5="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==5) {
				text6="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			
			cont++;
		}
		
		lstTexto.add(text2);
		lstTexto.add(text3);
		lstTexto.add(text4);
		lstTexto.add(text5);
		lstTexto.add(text6);
		
		
		XWPFTable table1 = document.createTable(3, 2);   
        setTableWidth(table1, "9000");  
        
        CTTblPr tblPr = table1.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {
            XWPFTableRow row2 = table1.getRow(rowIndex);
            
            // Agregar primer celda
            XWPFParagraph paragraph1 = row2.getCell(0).addParagraph();
            paragraph1.setAlignment(ParagraphAlignment.CENTER);
            
            XWPFRun run1 = paragraph1.createRun();
            String[] parts = lstTexto.get(priColumn).split("/");
            for(String texto: parts) {
            	run1.setText(texto);estiloNormalTexto(run1);
            	run1.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            
            priColumn++;

            // Agregar segunda celda
            XWPFParagraph paragraph22 = row2.getCell(1).addParagraph();
            paragraph22.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run2 = paragraph22.createRun();
            String[] parts2 = lstTexto.get(priColumn).split("/");
            for(String texto: parts2) {
            	run2.setText(texto);estiloNormalTexto(run2);
            	run2.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            priColumn++;
        }
        
        
        List<PlantillaVenta> plantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
        if(!plantilla.isEmpty()) {
        	List<ImagenPlantillaVenta> lstImagenPlantillaVenta = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantilla.get(0), true);
        	for(ImagenPlantillaVenta imagen : lstImagenPlantillaVenta) {
        		XWPFParagraph  paragraphh = document.createParagraph();
                String imagePath = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+imagen.getNombre();
                FileInputStream imageStream = new FileInputStream(imagePath);

                // Agregar datos de la imagen al documento
                int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageName = "nombre_de_la_imagen";
                document.addPictureData(imageStream, pictureType);

                // Crear un objeto XWPFPicture para insertar la imagen en el documento
                XWPFPicture picture = paragraphh.createRun().getParagraph().createRun().addPicture(new FileInputStream(imagePath), pictureType, imageName, Units.toEMU(400), Units.toEMU(400));
                
        	}
        	
        	
        }
       
				
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void formatoContado_higal3_etapa4() throws XmlException, InvalidFormatException, IOException  {
		String areaHectarea = contratoSelected.getLote().getProject().getAreaHectarea();
		String unidadCatastral = contratoSelected.getLote().getProject().getUnidadCatastral();
		String numPartidaElectronica = contratoSelected.getLote().getProject().getNumPartidaElectronica();
		String codigoPredio = contratoSelected.getLote().getProject().getCodigoPredio();
		
		ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, contratoSelected.getLote().getProject(),contratoSelected.getLote().getManzana()); 
		if(busqueda!=null) {
			areaHectarea = busqueda.getAreaHectarea();
			unidadCatastral = busqueda.getUnidadCatastral();
			numPartidaElectronica = busqueda.getNumPartidaElectronica();
			codigoPredio = busqueda.getCodigoPredio();
		}
		
		
		
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, " );estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC Nº 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FISCAL EN AV. SANTA "
				+ "VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");estiloNormalTexto(run);
		run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
			
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation()==null?"XXXXXXXXXXX": p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null?"XXXXXXXXXXX": p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni()==null?"XXXXXXXXXXX":p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone()==null?"XXXXXXXXXXX":p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail()==null?"XXXXXXXXXXX":p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress()==null?"XXXXXXXXXXX":p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(" Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);
		
		
			
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.addBreak();runPrimero.addBreak();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+codigoPredio.toUpperCase()+", ÁREA "
				+ areaHectarea.toUpperCase()+" U.C. "+unidadCatastral.toUpperCase()+", DISTRITO DE "+contratoSelected.getLote().getProject().getDistrict().getName().toUpperCase()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName().toUpperCase()+", DEPARTAMENTO DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName().toUpperCase()+", EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N° "+numPartidaElectronica.toUpperCase()+", ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL "+contratoSelected.getLote().getProject().getZonaRegistral().toUpperCase()+".");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN HIGAL III ETAPA III, "
				+ "EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA "
				+ "DE ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");estiloNormalTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° "+numPartidaElectronica.toUpperCase()+".");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+ (contratoSelected.getLote().getLimiteFrontal()== null?"":contratoSelected.getLote().getLimiteFrontal().toUpperCase()));
		run.addBreak();
		run.setText("Fondo         : "+(contratoSelected.getLote().getLimiteFondo() == null ? "" : contratoSelected.getLote().getLimiteFondo().toUpperCase()));
		run.addBreak();
		run.setText("Derecha     : "+(contratoSelected.getLote().getLimiteDerecha() == null ? "" : contratoSelected.getLote().getLimiteDerecha().toUpperCase()));
		run.addBreak();
		run.setText("Izquierda    : "+(contratoSelected.getLote().getLimiteIzquierda() == null ? "" : contratoSelected.getLote().getLimiteIzquierda().toUpperCase()));
		run.addBreak();run.addBreak();

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) PORPORCIONADAS "
				+ "MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL. ");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA - VENTA.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DEPÓSITO(S) EN CUENTA(S) DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("8983003839960, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("00389800300383996043 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO BBVA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("0011 0285 01 00180945, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("011 285 000100180945 46 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("CAJA TRUJILLO ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("122321409341, CCI 80201200232140934153 ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-4775107, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001477510763, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-02-3090720, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521002309072062, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
			
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA DECLARA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL INMUEBLE, MOTIVO POR EL CUAL NO SE "
				+ "ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL "
				+ "INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);
		
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES "
				+ "Y DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
				
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ DESPUÉS DE LA CANCELACIÓN DEL PAGO TOTAL DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("POR PARTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DEL DEPÓSITO REALIZADO EN LA CUENTA DE LA PARTE VENDEDORA, PARA LUEGO "
				+ "REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE Y POSTERIOR ESCRITURA PÚBLICA.");estiloNormalTexto(run);

			
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE "
				+ "EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO "
				+ "MAXIMO DEL PROYECTO.");estiloNormalTexto(run);	
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA "
				+ "DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA "
				+ "A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
			
				
					
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();	
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODO DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO "
				+ "JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
				+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
		
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE ENCUENTRA "
				+ "AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER "
				+ "CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
					
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI "
				+ "COMO LA OFICINA DEL REGISTRO PUBLICO CORRESPONDIENTE.");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE "
				+ "ESTAS GENEREN, ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU "
				+ "INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE "
				+ "CORRESPONDAN ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO "
				+ "INDEPENDIENTE DE EL (LOS) LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
			
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y AREAS VERDES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.  ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA "
				+ "AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA A PARTIR DE LA "
				+ "CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENERE LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO "
				+ "N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE "
				+ "ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) "
				+ "RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES "
				+ "Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 "
				+ "DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL "
				+ "DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE "
				+ "A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, "
				+ "LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
				+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
				+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
				+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
				+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
			
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();     
				
			
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "___________________________________/ALDASA INMOBILIARIA S.A.C./RUC: 20607274526";lstTexto.add(text1);
		String text2="";
		String text3="";
		String text4="";
		String text5="";
		String text6="";
		
		int cont = 1;
		for(Person p: lstPersonas) {
			if(cont==1) {
				text2="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==2) {
				text3="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==3) {
				text4="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==4) {
				text5="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==5) {
				text6="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			
			cont++;
		}
		
		lstTexto.add(text2);
		lstTexto.add(text3);
		lstTexto.add(text4);
		lstTexto.add(text5);
		lstTexto.add(text6);
		
		
		XWPFTable table1 = document.createTable(3, 2);   
        setTableWidth(table1, "9000");  
        
        CTTblPr tblPr = table1.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {
            XWPFTableRow row2 = table1.getRow(rowIndex);
            
            // Agregar primer celda
            XWPFParagraph paragraph1 = row2.getCell(0).addParagraph();
            paragraph1.setAlignment(ParagraphAlignment.CENTER);
            
            XWPFRun run1 = paragraph1.createRun();
            String[] parts = lstTexto.get(priColumn).split("/");
            for(String texto: parts) {
            	run1.setText(texto);estiloNormalTexto(run1);
            	run1.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            
            priColumn++;

            // Agregar segunda celda
            XWPFParagraph paragraph22 = row2.getCell(1).addParagraph();
            paragraph22.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run2 = paragraph22.createRun();
            String[] parts2 = lstTexto.get(priColumn).split("/");
            for(String texto: parts2) {
            	run2.setText(texto);estiloNormalTexto(run2);
            	run2.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            priColumn++;
        }
        
        
        List<PlantillaVenta> plantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
        if(!plantilla.isEmpty()) {
        	List<ImagenPlantillaVenta> lstImagenPlantillaVenta = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantilla.get(0), true);
        	for(ImagenPlantillaVenta imagen : lstImagenPlantillaVenta) {
        		XWPFParagraph  paragraphh = document.createParagraph();
                String imagePath = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+imagen.getNombre();
                FileInputStream imageStream = new FileInputStream(imagePath);

                // Agregar datos de la imagen al documento
                int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageName = "nombre_de_la_imagen";
                document.addPictureData(imageStream, pictureType);

                // Crear un objeto XWPFPicture para insertar la imagen en el documento
                XWPFPicture picture = paragraphh.createRun().getParagraph().createRun().addPicture(new FileInputStream(imagePath), pictureType, imageName, Units.toEMU(400), Units.toEMU(400));
                
        	}
        	
        	
        }
       
				
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void formatoContado_VDA() throws XmlException, InvalidFormatException, IOException {
		String areaHectarea = contratoSelected.getLote().getProject().getAreaHectarea();
		String unidadCatastral = contratoSelected.getLote().getProject().getUnidadCatastral();
		String numPartidaElectronica = contratoSelected.getLote().getProject().getNumPartidaElectronica();
		String codigoPredio = contratoSelected.getLote().getProject().getCodigoPredio();
		
		ProyectoPartida busqueda = proyectoPartidaService.findByEstadoAndProyectoAndManzana(true, contratoSelected.getLote().getProject(),contratoSelected.getLote().getManzana()); 
		if(busqueda!=null) {
			areaHectarea = busqueda.getAreaHectarea();
			unidadCatastral = busqueda.getUnidadCatastral();
			numPartidaElectronica = busqueda.getNumPartidaElectronica();
			codigoPredio = busqueda.getCodigoPredio();
		}
		
		
		
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, " );estiloNormalTexto(run);
		
		run = paragraph2.createRun();run.setText("ABARCA BIENES RAÍCES E.I.R.L., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC Nº 20609173093, REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("TITULAR GERENTE ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11389528 ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO FISCAL EN AV. SANTA "
				+ "VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");estiloNormalTexto(run);
		run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
			
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation()==null?"XXXXXXXXXXX": p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus()==null?"XXXXXXXXXXX": p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni()==null?"XXXXXXXXXXX":p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone()==null?"XXXXXXXXXXX":p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail()==null?"XXXXXXXXXXX":p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress()==null?"XXXXXXXXXXX":p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(" Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON INTERVENCIÓN DE EL SR. ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DE NACIONALIDAD PERUANA, IDENTIFICADO CON DNI. N°44922055, DE OCUPACION EMPRESARIO, DE ESTADO CIVIL CASADO, BAJO RÉGIMEN "
				+ "DE SEPARACIÓN DE PATRIMONIOS INSCRITO EN LA P.E. Nº 11385293 DEL REGISTRO PERSONAL DE LA OR DE CHICLAYO, CON DOMICILIO EN CALLE INDUSTRIAL 681 URB. SAN "
				+ "LORENZO, DISTRITO DE JOSÉ LEONARDO ORTIZ, PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; CON EL RESPALDO COMERCIAL DE ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC Nº 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA Nº 11352661 ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL Nº II - SEDE - CHICLAYO, CON DOMICILIO EN AV. SANTA VICTORIA 719 "
				+ "URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);
		
			
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.addBreak();runPrimero.addBreak();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+codigoPredio.toUpperCase()+", ÁREA "
				+ areaHectarea.toUpperCase()+" U.C. "+unidadCatastral.toUpperCase()+", DISTRITO DE "+contratoSelected.getLote().getProject().getDistrict().getName().toUpperCase()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName().toUpperCase()+", DEPARTAMENTO DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName().toUpperCase()+", EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N° "+numPartidaElectronica.toUpperCase()+", ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL "+contratoSelected.getLote().getProject().getZonaRegistral().toUpperCase()+".");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN VALLE DEL "
				+ "ÁGUILA I Y EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA "
				+ "DE ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");estiloNormalTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° "+numPartidaElectronica.toUpperCase()+".");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+ (contratoSelected.getLote().getLimiteFrontal()== null?"":contratoSelected.getLote().getLimiteFrontal().toUpperCase()));
		run.addBreak();
		run.setText("Fondo         : "+(contratoSelected.getLote().getLimiteFondo() == null ? "" : contratoSelected.getLote().getLimiteFondo().toUpperCase()));
		run.addBreak();
		run.setText("Derecha     : "+(contratoSelected.getLote().getLimiteDerecha() == null ? "" : contratoSelected.getLote().getLimiteDerecha().toUpperCase()));
		run.addBreak();
		run.setText("Izquierda    : "+(contratoSelected.getLote().getLimiteIzquierda() == null ? "" : contratoSelected.getLote().getLimiteIzquierda().toUpperCase()));
		run.addBreak();run.addBreak();

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) PORPORCIONADAS "
				+ "MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL. ");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA - VENTA.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DEPÓSITO(S) EN CUENTA(S) DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
			
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA DECLARA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL INMUEBLE, MOTIVO POR EL CUAL NO SE "
				+ "ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL "
				+ "INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);
		
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES "
				+ "Y DERECHOS SOBRE ÉL, LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
				
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ DESPUÉS DE LA CANCELACIÓN DEL PAGO TOTAL DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("POR PARTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DEL DEPÓSITO REALIZADO EN LA CUENTA DE LA PARTE VENDEDORA, PARA LUEGO "
				+ "REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE Y POSTERIOR ESCRITURA PÚBLICA.");estiloNormalTexto(run);

			
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE "
				+ "EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO "
				+ "MAXIMO DEL PROYECTO.");estiloNormalTexto(run);	
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA "
				+ "DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA "
				+ "A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
			
				
					
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();	
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODO DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO "
				+ "JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
				+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
		
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE ENCUENTRA "
				+ "AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER "
				+ "CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
					
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI "
				+ "COMO LA OFICINA DEL REGISTRO PUBLICO CORRESPONDIENTE.");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE "
				+ "ESTAS GENEREN, ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU "
				+ "INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE "
				+ "CORRESPONDAN ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO "
				+ "INDEPENDIENTE DE EL (LOS) LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
			
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y AREAS VERDES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.  ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA "
				+ "AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA A PARTIR DE LA "
				+ "CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENERE LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL ARTÍCULO "
				+ "N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE ALGÚN TERCERO. DE "
				+ "ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS PARTES (VENDEDOR Y COMPRADOR) "
				+ "RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES "
				+ "Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 "
				+ "DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL "
				+ "DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE "
				+ "A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, "
				+ "LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
				+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
				+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
				+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
				+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
			
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();     
				
			
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "___________________________________/ABARCA BIENES RAICES E.I.R.L/RUC: 20609173093";lstTexto.add(text1);
		String text2="";
		String text3="";
		String text4="";
		String text5="";
		String text6="";
		
		int cont = 1;
		for(Person p: lstPersonas) {
			if(cont==1) {
				text2="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==2) {
				text3="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==3) {
				text4="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==4) {
				text5="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			if(cont==5) {
				text6="___________________________________/"+p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase()+"/DNI: "+p.getDni().toUpperCase();
			}
			
			cont++;
		}
		
		lstTexto.add(text2);
		lstTexto.add(text3);
		lstTexto.add(text4);
		lstTexto.add(text5);
		lstTexto.add(text6);
		
		
		XWPFTable table1 = document.createTable(3, 2);   
        setTableWidth(table1, "9000");  
        
        CTTblPr tblPr = table1.getCTTbl().getTblPr();
        CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders() : tblPr.addNewTblBorders();
        borders.addNewBottom().setVal(STBorder.NONE);
        borders.addNewTop().setVal(STBorder.NONE);
        borders.addNewLeft().setVal(STBorder.NONE);
        borders.addNewRight().setVal(STBorder.NONE);
        borders.addNewInsideH().setVal(STBorder.NONE);
        borders.addNewInsideV().setVal(STBorder.NONE);
        
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {
            XWPFTableRow row2 = table1.getRow(rowIndex);
            
            // Agregar primer celda
            XWPFParagraph paragraph1 = row2.getCell(0).addParagraph();
            paragraph1.setAlignment(ParagraphAlignment.CENTER);
            
            XWPFRun run1 = paragraph1.createRun();
            String[] parts = lstTexto.get(priColumn).split("/");
            for(String texto: parts) {
            	run1.setText(texto);estiloNormalTexto(run1);
            	run1.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            
            priColumn++;

            // Agregar segunda celda
            XWPFParagraph paragraph22 = row2.getCell(1).addParagraph();
            paragraph22.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run2 = paragraph22.createRun();
            String[] parts2 = lstTexto.get(priColumn).split("/");
            for(String texto: parts2) {
            	run2.setText(texto);estiloNormalTexto(run2);
            	run2.addBreak();
            	
            }
            run1.addBreak();run1.addBreak();
            priColumn++;
        }
        
        
        List<PlantillaVenta> plantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSelected.getLote());
        if(!plantilla.isEmpty()) {
        	List<ImagenPlantillaVenta> lstImagenPlantillaVenta = imagenPlantillaVentaService.findByPlantillaVentaAndEstado(plantilla.get(0), true);
        	for(ImagenPlantillaVenta imagen : lstImagenPlantillaVenta) {
        		XWPFParagraph  paragraphh = document.createParagraph();
                String imagePath = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+imagen.getNombre();
                FileInputStream imageStream = new FileInputStream(imagePath);

                // Agregar datos de la imagen al documento
                int pictureType = XWPFDocument.PICTURE_TYPE_PNG;
                String imageName = "nombre_de_la_imagen";
                document.addPictureData(imageStream, pictureType);

                // Crear un objeto XWPFPicture para insertar la imagen en el documento
                XWPFPicture picture = paragraphh.createRun().getParagraph().createRun().addPicture(new FileInputStream(imagePath), pictureType, imageName, Units.toEMU(400), Units.toEMU(400));
                
        	}
        	
        	
        }
       
				
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
		
	public void formatoContado_ASR_IV() throws XmlException {
		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, " );estiloNormalTexto(run);
		
		run = paragraph2.createRun();run.setText("ABARCA BIENES RAÍCES E.I.R.L.");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText(", CON RUC N° 20609173093, REPRESENTADA POR SU ");estiloNormalTexto(run);
				
		run = paragraph2.createRun();run.setText("TITULAR GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11389528 "); estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO FICAL EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
			
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			
			
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON INTERVENCIÓN DE EL SR. ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DE NACIONALIDAD PERUANA, IDENTIFICADO CON DNI. N°44922055, DE OCUPACION EMPRESARIO, DE ESTADO CIVIL CASADO, BAJO RÉGIMEN DE SEPARACIÓN DE PATRIMONIOS INSCRITO EN LA P.E. N° 11385293 DEL REGISTRO PERSONAL DE LA OR DE CHICLAYO, CON DOMICILIO EN CALLE INDUSTRIAL 681 URB. SAN LORENZO, DISTRITO DE JOSÉ LEONARDO ORTIZ, PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; CON EL RESPALDO COMERCIAL DE ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("CON RUC N° 20607274526, REPRESENTADA POR SU");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);
		
//			run = paragraph2.createRun();run.setText("");estiloNormalTexto(run);
//			run = paragraph2.createRun();run.setText("");estiloNegritaTexto(run);
			
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.addBreak();runPrimero.addBreak();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. VALLE DE CHANCAY / SECTOR YENCALA BOGGIANO / PREDIO YENCALA BOGGIANO– COD. PREDIO. 7_6159260_67034, ÁREA "
				+ "4.1730 U.C. 67034, DISTRITO DE LAMBAYEQUE, PROVINCIA DE LAMBAYEQUE, DEPARTAMENTO DE LAMBAYEQUE, EN LO SUCESIVO DENOMINADO EL "
				+ "BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N°11072174, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N°II- SEDE CHICLAYO.");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("QUE CON FECHA 18 DE MARZO DE 2023 SE SUSCRIBIÓ UN CONTRATO DE COMPRA VENTA CON ARRAS CONFIRMATORIAS ENTRE ALAN CRUZADO "
				+ "BALCAZAR Y ABARCA BIENES RAÍCES EIRL, MEDIANTE EL CUAL SE ADQUIRIO LA PROPIEDAD DESCRTITA EN LA CLAUSULA 1.1 PARA EL DESARROLLO "
				+ "DEL PROYECTO DE LOTIZACION MATERIA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("3. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE "
				+ "DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN LOS ALTOS DE SAN ROQUE IV Y EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA "
				+ "DE ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° 11072174");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		run.addBreak();run.addBreak();

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
				+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA - VENTA.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DEPÓSITO(S) EN CUENTA(S) DEL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
			
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA, PARA QUE PUEDA(N) REALIZAR LOS DIFERENTES PROCEDIMIENTOS "
				+ "ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, "
				+ "PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA. ");estiloNormalTexto(run);
	
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, "
				+ "LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
			
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
			
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
				+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE "
				+ "EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
				+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO "
				+ "DEL PROYECTO.");estiloNormalTexto(run);	
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA "
				+ "DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA "
				+ "A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
			
				
					
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();	
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODO DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO "
				+ "JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
				+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
				+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
		
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
				+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
				+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE ENCUENTRA "
				+ "AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER "
				+ "CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
					
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI "
				+ "COMO LA OFICINA DEL REGISTRO PUBLICO CORRESPONDIENTE.");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("E) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE "
				+ "ESTAS GENEREN, ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU "
				+ "INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
				
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("F) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE "
				+ "CORRESPONDAN ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO "
				+ "INDEPENDIENTE DE EL (LOS) LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
			
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
				+ "PRINCIPALES, AVENIDAS Y AREAS VERDES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.  ");estiloNormalTexto(run);

			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
				+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA "
				+ "AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA A PARTIR DE LA "
				+ "CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENERE LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL "
				+ "ARTÍCULO N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE "
				+ "ALGÚN TERCERO. DE ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS "
				+ "PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA "
				+ "EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN "
				+ "QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO "
				+ "ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE "
				+ "A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, "
				+ "LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
				+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
				+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
				+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
				+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
			
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
			
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
				+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
			
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
		run.setText("___________________________________");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
	     
		
		XWPFTable table1 = document.createTable(7, 2);   
			
			
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	public void formatoContado_ASR_V() throws XmlException {
		// initialize a blank document
			XWPFDocument document = new XWPFDocument();
			// create a new file
			// create a new paragraph paragraph
			XWPFParagraph paragraph = document.createParagraph();
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			
			XWPFRun runTitle = paragraph.createRun();
			runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
			runTitle.setBold(true);
			runTitle.setFontFamily("Century Gothic");
			runTitle.setFontSize(12);

			
			XWPFParagraph paragraph2 = document.createParagraph();
			paragraph2.setAlignment(ParagraphAlignment.BOTH);
			
			XWPFRun run = paragraph2.createRun();
			run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN DE UNA PARTE, " );estiloNormalTexto(run);
			
			run = paragraph2.createRun();run.setText("ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("DE NACIONALIDAD PERUANA, IDENTIFICADO CON DNI. N°44922055, DE OCUPACION EMPRESARIO, DE ESTADO "
					+ "CIVIL CASADO, BAJO RÉGIMEN DE SEPARACIÓN DE PATRIMONIOS INSCRITO EN LA P.E. N° 11385293 DEL REGISTRO PERSONAL DE LA OR DE "
					+ "CHICLAYO, CON DOMICILIO EN CALLE INDUSTRIAL 681 URB. SAN LORENZO, DISTRITO DE JOSÉ LEONARDO ORTIZ, PROVINCIA DE CHICLAYO, "
					+ "DEPARTAMENTO DE LAMBAYEQUE, A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
			run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
			
			List<Person> lstPersonas = new ArrayList<>();
			if(contratoSelected.getPersonVenta()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta());
			}
			if(contratoSelected.getPersonVenta2()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta2());
			}
			if(contratoSelected.getPersonVenta3()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta3());
			}
			if(contratoSelected.getPersonVenta4()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta4());
			}
			if(contratoSelected.getPersonVenta5()!=null) {
				lstPersonas.add(contratoSelected.getPersonVenta5());
			}
				
			int contador = 1;
			for(Person p: lstPersonas) {
				run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getOccupation().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run); 
				run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getAddress().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
				run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
				run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
				
				
				
				
				if(lstPersonas.size()==contador){
					run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
				}else {
					run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
				}
				contador++;
			}
			run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("CON INTERVENCIÓN Y EL RESPALDO COMERCIAL DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("ABARCA BIENES RAÍCES E.I.R.L., ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("CON ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("RUC N° 20607274526, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", REPRESENTADA POR SU ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("TITULAR GERENTE ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
			
			
			run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11389528 ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO "
					+ "FISCAL EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE Y ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C., ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("CON RUC N° 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N°44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 ");estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO "
					+ "EN AV. SANTA VICTORIA 719 URB. SANTA VICTORIA, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; EL CONTRATO SE "
					+ "CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run); 	
			
//			run = paragraph2.createRun();run.setText("");estiloNormalTexto(run);
//			run = paragraph2.createRun();run.setText("");estiloNegritaTexto(run);
				
			XWPFParagraph paragraphPrimero = document.createParagraph();
			paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
			
			XWPFRun runPrimero = paragraphPrimero.createRun();
			runPrimero.addBreak();runPrimero.addBreak();
			runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
			runPrimero.setUnderline(UnderlinePatterns.SINGLE);
			
			String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
					+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
					+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
					+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
					+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
					+ "</w:abstractNum>";

			String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
					+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
					+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
					+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
					+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
					+ "</w:abstractNum>";

					
			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//			CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
			CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
			XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
			XWPFNumbering numbering = document.createNumbering();
			BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
			BigInteger numID = numbering.addNum(abstractNumID);

			XWPFParagraph paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

			paragrapha = document.createParagraph();
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
			

			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("1. ");
			run.setBold(true);
			run = paragrapha.createRun();
			run.setText("UBIC, RUR. VALLE DE CHANCAY / SECTOR YENCALA BOGGIANO / PREDIO FUNDO SANTA ROSA – COD. PREDIO. 7_6159260_27110, ÁREA "
					+ "1.9600 U.C. 27110, DISTRITO DE LAMBAYEQUE, PROVINCIA DE LAMBAYEQUE, DEPARTAMENTO DE LAMBAYEQUE, EN LO SUCESIVO DENOMINADO "
					+ "EL BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN INSCRITOS EN LA ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA N°02266512, ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N°II- SEDE CHICLAYO.");estiloNormalTexto(run);
			

			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("2. ");
			run.setBold(true);
			run = paragrapha.createRun();
			run.setText("LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, EN EL CUAL SE "
					+ "DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN LOS ALTOS DE SAN ROQUE V Y EL CUAL ES MATERIA DE VENTA A TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
					
			
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("SEGUNDO.");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
			
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("OBJETO");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA "
					+ "DE ESTE CONTRATO, EL (LOS) CUAL(ES) TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS:");estiloNormalTexto(run); 
			

			paragrapha = document.createParagraph();
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();
			run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
					+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
					+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA: N° 02266512.");estiloNegritaTexto(run);
			
			
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.setUnderline(UnderlinePatterns.SINGLE); 
			run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
			

			paragrapha = document.createParagraph();
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
			run.addBreak();
			run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
			run.addBreak();
			run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
			run.addBreak();
			run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
			run.addBreak();
			run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
			run.addBreak();
			run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
			run.addBreak();run.addBreak();

			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("SIENDO ASI, ");estiloNormalTexto(run); 
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA Y LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("ACUERDAN DE FORMA EXPRESA E IRREVOCABLE QUE LAS MEDIDAS Y/O AREA DE EL (LOS) LOTE(S) "
					+ "PORPORCIONADAS MEDIANTE EL PRESENTE CONTRATO PODRIAN TOLERAR VARIACIONES MÍNIMAS CONFORME AL ARTICULO 1577° DEL CODIGO CIVIL.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("TERCERO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("PRECIO DE COMPRA - VENTA.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DEPÓSITO(S) EN CUENTA(S) DEL ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("BANCO INTERBANK, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA EN SOLES ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("7003004825505, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("003-700-00300482550520, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("EN DÓLARES ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("7003004825512, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("003-700-00300482551226, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("Y/O EN CUENTA(S) DE CAJA PIURA EN SOLES ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("210-01-6851216, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("80104521001685121664, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
				
			
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("CUARTO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SOLICITARÁ A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA, PARA QUE PUEDA(N) REALIZAR LOS DIFERENTES PROCEDIMIENTOS "
					+ "ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, "
					+ "PARA LO CUAL ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA. ");estiloNormalTexto(run);
		
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, "
					+ "LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
				
				
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA. ");estiloNegritaTexto(run);
				
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA ENTREGA FÍSICA DE LOS LOTES SE REALIZARÁ UNA VEZ QUE SE COMPLETE EL PROYECTO INMOBILIARIO "
					+ "QUE PUEDE ESTAR SUJETO A MODIFICACIONES PREVIA COMUNICACIÓN A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("PERO QUE SE HARÁ EFECTIVA LA TRANSFERENCIA DE DERECHOS Y POSESIÓN EN EL MOMENTO QUE SE CULMINE "
					+ "EL PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
					
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("EN TAL SENTIDO, LA PARTE COMPRADORA, DE MANERA INCONDICIONAL E IRREVOCABLE, RECONOCE QUE EL "
					+ "(LOS) LOTE(S) SI EXISTE(N) A LA FECHA DE SUSCRIPCION DEL PRESENTE CONTRATO, SINO QUE SERA ENTREGADO DENTRO DEL PLAZO MAXIMO "
					+ "DEL PROYECTO.");estiloNormalTexto(run);	
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA "
					+ "DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA "
					+ "A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
				
					
						
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();	
			run.setText("QUINTO.");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
			
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, LIBRE DE TODO DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO "
					+ "JUDICIAL DE PRESCRIPCIÓN ADQUISITIVA DE DOMINIO, REIVINDICACIÓN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, "
					+ "MEDIDA INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
					+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE DISPOSICIÓN DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN CONOCER LA SITUACIÓN ACTUAL, FÍSICA, LEGAL Y ADMINISTRATIVA DEL INMUEBLE. "
					+ "SIN EMBARGO, EL VENDEDOR SE OBLIGA AL SANEAMIENTO DE LEY SOBRE LAS CARGAS QUE PESARAN EN EL INMUEBLE MATERIA DE TRANSFERENCIA.");estiloNormalTexto(run);
			
				
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, "
					+ "IMPULSADO POR ALGÚN PRECARIO Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, "
					+ "PERSONAL Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
					+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE ENCUENTRA "
					+ "AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER "
					+ "CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
						
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("SEXTO");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
				
			run.addBreak();
			run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("A) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL ASI "
					+ "COMO LA OFICINA DEL REGISTRO PUBLICO CORRESPONDIENTE.");estiloNormalTexto(run); 

			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("B) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
					+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("C) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("D) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("E) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("SUSCRIBIR TODA DOCUMENTACIÓN LEGAL (MINUTAS ACLARATORIAS, ESCRITURAS PÚBLICAS QUE "
					+ "ESTAS GENEREN, ENTRE OTRAS) Y ADMINISTRATIVA NECESARIA PARA LOGRAR LA PUESTA EN PRÁCTICA DEL PRESENTE CONTRATO Y SU "
					+ "INSCRIPCIÓN EN LOS REGISTROS PÚBLICOS. ");estiloNormalTexto(run);
					
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("F) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("EL COMPRADOR REALIZA TODAS LAS GESTIONES NECESARIAS Y SUSCRIBIR TODOS LOS DOCUMENTOS QUE "
					+ "CORRESPONDAN ANTE LAS ENTIDADES PRESTADORAS DE SERVICIOS PÚBLICOS PARA OBTENER EL SUMINISTRO ELÉCTRICO Y/O SANITARIO "
					+ "INDEPENDIENTE DE EL (LOS) LOTE(S), ASI COMO PAGAR LOS DERECHOS CORRESPONDIENTES. ");estiloNormalTexto(run);

				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
			run.setBold(true);
			run.setFontFamily("Century Gothic");
			run.setFontSize(9);
				
			run.addBreak();
			run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("A) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
				run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("B) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA Y PARQUES HABILITADOS, AFIRMAMENTO DE CALLES "
					+ "PRINCIPALES, AVENIDAS Y AREAS VERDES.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("C) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTA, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			paragrapha.setIndentationLeft(500);
			run = paragrapha.createRun();
			run.setText("D) ");
			run.setBold(true);
			run = paragrapha.createRun();run.setText("SUSCRIBIR TODOS LOS DOCUMENTOS QUE SEAN NECESARIOS, A FIN DE QUE SE FORMALICE LA TRANSFERENCIA DE PROPIEDAD DE EL INMUEBLE A FAVOR DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
			run = paragrapha.createRun();run.setText("DESPUÉS DE CULMINADO EL PROYECTO INMOBILIARIO.  ");estiloNormalTexto(run);

				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("OCTAVO.");estiloNegritaTexto(run); 
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun(); 
			run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE SERVICIO DE "
					+ "AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN DE MEJORAS, QUE PUDIERA "
					+ "AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA LA FIRMA DE LA MINUTA, FECHA A PARTIR DE LA "
					+ "CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("NOVENO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
			run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENERE LA MINUTA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();
			run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD CON EL "
					+ "ARTÍCULO N°1435 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, A FAVOR DE "
					+ "ALGÚN TERCERO. DE ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA Y AMBAS "
					+ "PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, SERÍA "
					+ "EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS RESTRICCIÓN "
					+ "QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, NOTARIAL O CORREO "
					+ "ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
			
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("COMPETENCIA JURISDICCIONAL");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();
			run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE "
					+ "A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, "
					+ "LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("DOMICILIO");estiloNegritaTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, "
					+ "LUGARES A LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR.");estiloNormalTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE CONFORME "
					+ "AL ARTICULO 40° DEL CODIGO CIVIL, QUE SERA NOTIFICADA MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO "
					+ "MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR "
					+ "NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
				
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();
			run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
			run.setUnderline(UnderlinePatterns.SINGLE);
				
			paragrapha = document.createParagraph();
			paragrapha.setNumID(numID);
			run = paragrapha.createRun();
			run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
			
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
					+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			paragrapha.setAlignment(ParagraphAlignment.BOTH);
			run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
					+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
				
			paragrapha = document.createParagraph();
			run = paragrapha.createRun();
			run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
			run.setText("___________________________________");estiloNormalTexto(run);
			run.addBreak();
			run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
			run.addBreak();
			run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
		     
				
				
				try {			
					 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
			            File file = new File(filePath);
			            FileOutputStream out = new FileOutputStream(file);
			            document.write(out);
			            out.close();
			            fileDes=null;
			            fileDes = DefaultStreamedContent.builder()
			                    .name(nombreArchivo)
			                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
			                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
			                    .build();
		            
		    		 
		      
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		
	}
	
	public void formatoCredito() throws IOException, XmlException {

		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CRÉDITO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN, DE UNA PARTE, " );estiloNormalTexto(run);
		
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C.");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText(", CON RUC N° 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
				
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 "); estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO EN CAL. LOS AMARANTOS NRO. 245 URB. FEDERICO VILLARREAL, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run);
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);

		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+contratoSelected.getLote().getProject().getCodigoPredio().toUpperCase()+", "
						+ "ÁREA "+contratoSelected.getLote().getProject().getAreaHectarea().toUpperCase()+" U.C. "+contratoSelected.getLote().getProject().getUnidadCatastral().toUpperCase()+", DISTRITO "
						+ "DE "+contratoSelected.getLote().getProject().getDistrict().getName()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName()+", DEPARTAMENTO "
						+ "DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName()+", "
						+ "EN LO SUCESIVO DENOMINADO EL BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN "
						+ "INSCRITOS EN LA PARTIDA ELECTRÓNICA N° ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText(", DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N° ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getLote().getProject().getZonaRegistral()+".");estiloNegritaTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText(
				"LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, "
						+ "EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN "+contratoSelected.getLote().getProject().getName().toUpperCase()+" Y EL CUAL ES MATERIA DE VENTA A "
						+ "TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO, EL (LOS) CUAL(ES) "
				+ "TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");estiloNormalTexto(run); 
		
		
		
	

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA:"+contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE COMPROMETE A ENTREGAR LA UNIDAD INMOBILIARIA MATERIA DE LA PRESENTE COMPRA VENTA EN LAS "
				+ "CONDICIONES QUE SE ESTIPULAN EN EL PRESENTE CONTRATO Y QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ACEPTAN EXPRESAMENTE.");estiloNormalTexto(run); 
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGARÁ DE LA SIGUIENTE MANERA: ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("COMO INICIAL, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoInicial())+" ("+numeroAletra.Convertir(contratoSelected.getMontoInicial()+"", true, "")+" SOLES)");estiloNegritaTexto(run);
 		run = paragrapha.createRun();run.setText("EL MONTO DE  CON DEPÓSITO(S) ");estiloNormalTexto(run);
 		
 		
 		if(lstCuentaBancaria!=null) {
 			int count = 1;
 			for(CuentaBancaria cta:lstCuentaBancaria) {
 				run = paragrapha.createRun();run.setText("EN CUENTA DEL ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
 				
 				if(count==lstCuentaBancaria.size()) {
 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
 				}else {
 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
 				}
 				count++;
 			}
 		}
 		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("LA FORMA DE PAGO DEL SALDO POR LA COMPRAVENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("SE DA A RAZÓN DE LA POLÍTICA DE FINANCIAMIENTO DIRECTO QUE BRINDA ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("TIENE VARIAS OPCIONES, LAS CUALES ADOPTARÁN SEGÚN SU CRITERIO Y MEJOR PARECER; A CONTINUACIÓN, "
				+ "DENTRO DE LOS ESPACIOS SEÑALADOS ELEGIR LA OPCION DE PAGO: ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(800);
		run = paragrapha.createRun();run.setText("- PAGO DEL SALDO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getNumeroCuota()+" ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("CUOTAS MENSUALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LOS PAGOS MENSUALES A QUE ALUDE LA CLÁUSULA PRECEDENTE, EN LA OPCIÓN SEÑALADA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE EFECTUARÁN EL DÍA "+sdfDay.format(contratoSelected.getFechaPrimeraCuota())+" DE CADA MES CON DEPÓSITO EN LA CUENTA A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA CUAL PERTENECE AL ");estiloNormalTexto(run);
		
		if(lstCuentaBancaria!=null) {
 			int count = 1;
 			for(CuentaBancaria cta:lstCuentaBancaria) {
 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
 				
 				if(count==lstCuentaBancaria.size()) {
 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
 				}else {
 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
 				}
 				count++;
 			}
 		}
		
		run = paragrapha.createRun();run.setText("SIN NECESIDAD DE NOTIFICACIÓN, CARTA CURSADA, MEDIO NOTARIAL O REQUERIMIENTO ALGUNO, SI TRANSCURRIDO "
				+ "DICHO TÉRMINO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("INCURRE EN MORA, AUTOMÁTICAMENTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("RECONOCERÁ COMO VÁLIDOS SOLAMENTE LOS PAGOS QUE SE EFECTÚEN DE ACUERDO A SUS SISTEMAS DE COBRANZAS Y "
				+ "DOCUMENTOS EMITIDOS POR ÉL, SI EXISTIERA UN RECIBO DE PAGO EFECTUADO RESPECTO A UNA CUOTA, NO CONSTITUYE PRESUNCIÓN DE HABER "
				+ "CANCELADO LAS ANTERIORES.");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SE ACUERDA ENTRE LAS PARTES QUE LOS PAGOS SE REALIZARÁN EN LAS FECHAS ESTABLECIDAS EN LOS PÁRRAFOS DE ESTA CLÁUSULA "
				+ "TERCERA SIN PRÓRROGAS NI ALTERACIONES; MÁS QUE LAS CONVENIDAS EN ESTE CONTRATO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("TENIENDO EN CUENTA QUE EL OBJETO DEL PRESENTE CONTRATO ES LA COMPRAVENTA A PLAZOS DE "+contratoSelected.getNumeroCuota()+" "
				+ "MESES, MZ "+contratoSelected.getLote().getManzana().getName().toUpperCase()+" LOTE (S) "+contratoSelected.getLote().getNumberLote()+" DE TERRENO(S) DE UN BIEN INMUEBLE DE MAYOR EXTENSIÓN, LAS PARTES PRECISAN QUE "
				+ "POR ACUERDO INTERNO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("UNA VEZ CANCELADO EL SALDO POR EL TOTAL DEL PRECIO DE EL (LOS) LOTE(S), "
				+ "SOLICITARÁ A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("LA FORMALIZACIÓN DE LA MINUTA Y POSTERIOR ESCRITURA PÚBLICA DE COMPRA VENTA, "
				+ "PARA QUE PUEDA(N) REALIZAR LOS DIFERENTES PROCEDIMIENTOS ADMINISTRATIVOS, MUNICIPALES, NOTARIALES Y REGISTRALES "
				+ "EN PRO DE SU INSCRIPCIÓN DE INDEPENDIZACIÓN, PARA LO CUAL ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CORRERÁ CON LOS GASTOS Y TRÁMITES QUE EL PROCESO ADMINISTRATIVO, MUNICIPAL, NOTARIAL Y REGISTRAL IMPLICA. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL "
				+ "INMUEBLE, MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA "
				+ "O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, "
				+ "LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL SALDO POR PARTE DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LUEGO REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE.");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA "
				+ "EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, "
				+ "EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
				
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, "
				+ "LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO JUDICIAL DE PRESCRIPCIÓN "
				+ "ADQUISITIVA DE DOMINIO, REIVINDICACIN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, MEDIDA "
				+ "INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE "
				+ "DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA "
				+ "ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, IMPULSADO POR ALGÚN PRECARIO "
				+ "Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, PERSONAL "
				+ "Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UNA VEZ CANCELADO EL SALDO TOTAL POR LA COMPRA VENTA DEL INMUEBLE, ES DE SU CARGO REALIZAR "
				+ "LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA, UNA VEZ QUE HAYA REALIZADO EL PAGO TOTAL DEL SALDO DE LA COMPRA VENTA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA, AFIRMAMENTO DE CALLES PRINCIPALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTAS Y ESCRITURAS PÚBLICAS A LA CANCELACIÓN DE LOS SALDOS POR COMPRA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run); 
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE "
				+ "SERVICIO DE AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN "
				+ "DE MEJORAS, QUE PUDIERA AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA "
				+ "DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CLAUSULA DE CESION DE POSICION CONTRACTUAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("POR LA PRESENTE CLÁUSULA, AMBAS PARTES DAN CONSENTIMIENTO PREVIO, EXPRESO E IRREVOCABLE DE CONFORMIDAD "
				+ "CON EL ARTÍCULO N°1345 Y SIGUIENTES DEL CÓDIGO CIVIL, A QUE EL VENDEDOR PUEDA CEDER SU POSICIÓN CONTRACTUAL, "
				+ "A FAVOR DE ALGÚN TERCERO. DE ESTE MODO, EL VENDEDOR PODRÁ APARTARSE TOTALMENTE DE LA RELACIÓN JURÍDICA PRIMIGENIA "
				+ "Y AMBAS PARTES (VENDEDOR Y COMPRADOR) RECONOCEN QUE EL TERCERO AL QUE SE LE PODRÍA CEDER LA POSICIÓN DE VENDEDOR, "
				+ "SERÍA EL ÚNICO RESPONSABLE DE TODAS LAS OBLIGACIONES Y DERECHOS COMPRENDIDO EN EL PRESENTE CONTRATO, SIN MÁS "
				+ "RESTRICCIÓN QUE HACER DE CONOCIMIENTO CON UNA ANTICIPACIÓN DE 05 DÍAS A EL COMPRADOR A TRAVÉS DE CARTA SIMPLE, "
				+ "NOTARIAL O CORREO ELECTRÓNICO;  LA SUSCRIPCIÓN DE LA PRESENTE ES PLENA SEÑAL DE CONSENTIMIENTO Y CONFORMIDAD DE AMBAS PARTES.");estiloNormalTexto(run);
		
		
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, "
				+ "SOMETERSE A LA JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y "
				+ "SEÑALANDO COMO TALES, LOS CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, LUGARES A "
				+ "LOS QUE SERÁN DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. ");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, DEBERÁ SER COMUNICADO A LA OTRA PARTE MEDIANTE CARTA CURSADA POR "
				+ "CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE 5 (CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS "
				+ "EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO "
				+ "DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("BENEFICIO POR CONDUCTA DE BUEN PAGADOR.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("SI EL PAGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIEREN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE ADELANTA Y/O CANCELA EN SU TOTALIDAD, SE OMITIRÁ TODO PAGO DE INTERÉS FUTURO, SE CONSIDERA PREPAGO DE CAPITAL; EN CASO, QUE "
				+ "EXISTA PREPAGO PARCIAL, SE OMITIRÁ INTERESES FUTUROS POR EL MONTO QUE EL CLIENTE PRE-PAGUE Y SE GENERA UN NUEVO CRONOGRAMA DE PAGOS, EL CUAL PUEDE SER ACORDADO CON ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("“LA PARTE VENDEDORA”.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO QUINTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("RESOLUCIÓN DE CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES ESTABLECEN MEDIANTE ESTA CLÁUSULA QUE; ANTE EL INCUMPLIMIENTO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TRES (03)CUOTAS SUCESIVAS O NO DE LOS PAGOS EN LOS PLAZOS ESTABLECIDOS DE LAS CUOTAS CONSIGNADAS EN LA TERCERA CLÁUSULA DE ESTE CONTRATO, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PUEDE UNILATERALMETE RESOLVER EL CONTRATO; O EN SU DEFECTO; EXIGIR EL PAGO TOTAL DEL  DEUDOR A  ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LO CUAL REMITIRÁ LA NOTIFICACIÓN RESPECTIVA  AL DOMICILIO QUE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("HAYA SEÑALADO COMO SUYO EN LA PRIMERA CLÁUSULA DE ESTE CONTRATO, EL PLAZO MÁXIMO DE CONTESTACIÓN QUE TENDRÁN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SERÁ DE TRES (03) DIAS CALENDARIO, A PARTIR DE HABER RECIBIDO LA NOTIFICACIÓN; SEA QUE HAYA SIDO RECIBIDA POR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("ALGUNA PERSONA QUE RESIDA EN EL DOMICILIO INIDICADO O EN SU DEFECTO SE HAYA COLOCADO BAJO PUERTA (PARA LO CUAL EL NOTIFICADOR "
				+ "SEÑALARA EN EL MISMO CARGO DE RECEPCIÓN DE LA NOTIFICACIÓN LAS CARACTERISTICAS DEL DOMICILIO, ASÍ COMO UNA IMAGEN FOTOGRÁFICA DEL MISMO), LUEGO DE LO CUAL "
				+ "EL CONTRATO QUEDARÁ RESUELTO DE PLENO DERECHO Y EL MONTO O LOS MONTOS QUE SE HAYAN EFECTUADO PASARÁN A CONSIGNARSE COMO UNA INDEMNIZACIÓN A FAVOR DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEXTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACIÓN SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO POR LAS NORMAS DEL "
				+ "CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
		run.setText("___________________________________");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setPageBreak(true);
		
		// ************************** TABLAS **************************
		
		List<String> lstTexto = new ArrayList<>();
		String text1 = "Empresa:  ALDASA INMOBILIARIA S.A.C.";lstTexto.add(text1);
		String text2 = "Fecha: "+ sdf.format(contratoSelected.getFechaVenta());lstTexto.add(text2); 
		String text3 = "Comprador(es): ";
		String text4 = "D.N.I: ";
		int cont = 1;
		for(Person p: lstPersonas) {
			
			if(lstPersonas.size()==cont){
				text3= text3+p.getSurnames()+" "+p.getNames();
				text4= text4+p.getDni();
			}else {
				text3= text3+p.getSurnames()+" "+p.getNames()+" / ";
				text4= text4+p.getDni()+" / ";
			}
			cont++;
		}
		
		lstTexto.add(text3.toUpperCase());
		lstTexto.add(text4.toUpperCase());
		
		String text5 = "Monto Total: S/"+String.format("%,.2f",contratoSelected.getMontoVenta());lstTexto.add(text5);
		String text6 = "Monto Deuda: S/"+String.format("%,.2f",contratoSelected.getMontoVenta().subtract(contratoSelected.getMontoInicial()));lstTexto.add(text6);
		String text7 = "N° Cuotas: "+contratoSelected.getNumeroCuota();lstTexto.add(text7);
		String text8 = "Moneda: Soles";lstTexto.add(text8);
		String text9 = "Cuotas Pendientes: "+contratoSelected.getNumeroCuota();lstTexto.add(text9);
		String text10 = "Interés: "+contratoSelected.getInteres()+"%";lstTexto.add(text10);
		String text11 = "Mz: "+contratoSelected.getLote().getManzana().getName() ;lstTexto.add(text11);
		String text12 = "Lote: "+contratoSelected.getLote().getNumberLote();lstTexto.add(text12);
		
		
		XWPFTable table1 = document.createTable(7, 2);   
		
        setTableWidth(table1, "9000");  
//        fillTable(table1, lstTexto);  
        mergeCellsHorizontal(table1,0,0,1);  
        int priColumn=0;
        
        for (int rowIndex = 0; rowIndex < table1.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = table1.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
            	estiloCentrarCeldaTabla(row1.getCell(0));
            }else {
            	XWPFTableRow row2 = table1.getRow(rowIndex);
            	row2.getCell(0).setText(lstTexto.get(priColumn));
            	priColumn++;
            	row2.getCell(1).setText(lstTexto.get(priColumn));
            	priColumn++;
            }
        }  
        
        paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();
        
        int cantRowsPagos = contratoSelected.getNumeroCuota()+4;
        XWPFTable tablePagos = document.createTable(cantRowsPagos, 6);
        
        setTableWidth(tablePagos, "9000");  
//      fillTable(table1, lstTexto);  
        mergeCellsHorizontal(tablePagos,0,0,5);
        mergeCellsHorizontal(tablePagos,tablePagos.getNumberOfRows()-1,0,1);  
        int index = 0;
        simularCuotas(contratoSelected);
        for (int rowIndex = 0; rowIndex < tablePagos.getNumberOfRows(); rowIndex++) {  
            if(rowIndex==0) {
                //Creating first Row
            	XWPFTableRow row1 = tablePagos.getRow(rowIndex);
            	row1.getCell(0).setText("CRONOGRAMA DE LA DEUDA");
            	estiloCentrarCeldaTabla(row1.getCell(0));
             
            }else if(rowIndex==1) {
            	XWPFTableRow row2 = tablePagos.getRow(rowIndex);
            	row2.getCell(0).setText("N° Cuotas");
            	row2.getCell(1).setText("Periodo");
            	row2.getCell(2).setText("Cuota inicial");
            	row2.getCell(3).setText("Cuota SI");
            	row2.getCell(4).setText("Interés");
            	row2.getCell(5).setText("Cuota Total");
            	
            	estiloCentrarCeldaTabla(row2.getCell(0));
            	estiloCentrarCeldaTabla(row2.getCell(1));
            	estiloCentrarCeldaTabla(row2.getCell(2));
            	estiloCentrarCeldaTabla(row2.getCell(3));
            	estiloCentrarCeldaTabla(row2.getCell(4));
            	estiloCentrarCeldaTabla(row2.getCell(5));
            }else {
            	Simulador sim = lstSimulador.get(index);
            	
            	XWPFTableRow rowContenido = tablePagos.getRow(rowIndex);
            	rowContenido.getCell(0).setText(sim.getNroCuota());
            	rowContenido.getCell(1).setText(sim.getFechaPago() != null?sdf.format(sim.getFechaPago()):""); 
            	rowContenido.getCell(2).setText("S/"+String.format("%,.2f",sim.getInicial()));
            	rowContenido.getCell(3).setText("S/"+String.format("%,.2f",sim.getCuotaSI()));
            	rowContenido.getCell(4).setText("S/"+String.format("%,.2f",sim.getInteres()));
            	rowContenido.getCell(5).setText("S/"+String.format("%,.2f",sim.getCuotaTotal()));
            	
            	estiloCentrarCeldaTabla(rowContenido.getCell(0));
            	estiloCentrarCeldaTabla(rowContenido.getCell(1));
            	estiloCentrarCeldaTabla(rowContenido.getCell(2));
            	estiloCentrarCeldaTabla(rowContenido.getCell(3));
            	estiloCentrarCeldaTabla(rowContenido.getCell(4));
            	estiloCentrarCeldaTabla(rowContenido.getCell(5));
            	
            	index++;
            	
            }
        }  

	        
//	        XWPFTable table = document.createTable();
	        
        //Creating first Row
//        XWPFTableRow row1 = table.getRow(0);
//        XWPFTableCell cell = table.getRow(0).getCell(0);
//        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
//        row1.getCell(0).setText("CRONOGRAMA DE PAGOS");
//
//        //Creating second Row
//        XWPFTableRow row2 = table.createRow();
//        row2.getCell(0).setText("Second Row, First Column");
//        row2.getCell(1).setText("Second Row, Second Column");
//
//        //create third row
//        XWPFTableRow row3 = table.createRow();
//        row3.getCell(0).setText("Third Row, First Column");
//        row3.getCell(1).setText("Third Row, Second Column");
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}  
		
	public void iniciarLotesSinContratoLazy() {

		lstLotesSinContratoLazy = new LazyDataModel<Lote>() {
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
			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String lote = "%" + (filterBy.get("numberLote") != null ? filterBy.get("numberLote").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String manzana = "%" + (filterBy.get("manzana.name") != null ? filterBy.get("manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String project = "%" + (filterBy.get("project.name") != null ? filterBy.get("project.nema").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

				
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
               
                Page<Lote> pageLote=null;
                
                
                if(projectFilterLote != null) {
                    pageLote= loteService.findByStatusAndProjectSucursalAndRealizoContratoAndNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndProject(EstadoLote.VENDIDO.getName(), navegacionBean.getSucursalLogin(), "N", lote, manzana, project, projectFilterLote, pageable);

				}else {
	                pageLote= loteService.findByStatusAndProjectSucursalAndRealizoContratoAndNumberLoteLikeAndManzanaNameLikeAndProjectNameLike(EstadoLote.VENDIDO.getName(), navegacionBean.getSucursalLogin(), "N", lote, manzana, project, pageable);
	                
				}
                
                setRowCount((int) pageLote.getTotalElements());
                return datasource = pageLote.getContent();
            }
		};
	}
	
	public void seleccionarLote() {
		nombreLoteSelected = loteSelected.getNumberLote()+" -  MZ "+loteSelected.getManzana().getName()+" / "+ loteSelected.getProject().getName();
		
		List<PlantillaVenta> lstPlantillaAprobada = plantillaVentaService.findByEstadoAndLote("Aprobado", loteSelected);
		
		if(!lstPlantillaAprobada.isEmpty()) {
			PlantillaVenta plantillaVenta = lstPlantillaAprobada.get(0);
			
			if(plantillaVenta.getTipoPago().equals("Crédito")) {
				montoInicial = plantillaVenta.getMontoInicial();
				nroCuotas = plantillaVenta.getNumeroCuota();
				interes = plantillaVenta.getInteres();
			}else {
				montoInicial = BigDecimal.ZERO;
				nroCuotas = 0 ;
				interes = BigDecimal.ZERO;
			}
			
			fechaVenta = plantillaVenta.getFechaVenta();
			montoVenta = plantillaVenta.getMontoVenta();
			tipoPago = plantillaVenta.getTipoPago();
			persona1 = plantillaVenta.getPerson();
		}
		
	}
	
	public void eliminarLoteSelected() {
		loteSelected=null;
		
		fechaVenta=null;
		montoVenta = null;
		tipoPago = null;
		montoInicial = null;
		nroCuotas = 0;
		interes = null;
		persona1 = null;
		persona2=null;
		persona3 = null;
		persona4 = null; 
		persona5 = null;
		fechaPrimeraCuota=null;
		cuotaEspecial= false;
	}
	
	public void saveContrato() {
	
		if(loteSelected == null) {
			addErrorMessage("Seleccionar un lote.");
			return ;  
		}
		if(fechaVenta==null) {
			addErrorMessage("Seleccionar fecha de venta.");
			return ;  
		}
		if(montoVenta==null) {
			addErrorMessage("Ingresar monto de venta.");
			return ;  
		}else if(montoVenta==BigDecimal.ZERO){
			addErrorMessage("Monto venta debe ser mayor a 0.");
			return ;
		}
		
		if(tipoPago.equals("")) {
			addErrorMessage("Seleccionar tipo de pago.");
			return ;  
		}else if(tipoPago.equals("Crédito")){
			if(montoInicial==null) {
				addErrorMessage("Ingresar monto inicial.");
				return ;  
			}else if (montoInicial==BigDecimal.ZERO){
				addErrorMessage("Monto inicial debe ser mayor a 0.");
				return ;
			}
			if(nroCuotas==0){
				addErrorMessage("Número de cuotas debe ser mayor a 0.");
				return ;
			}
			if(interes==null) {
				addErrorMessage("Ingresar interes.");
				return ;  
			}
			if(fechaPrimeraCuota==null) {
				addErrorMessage("Ingresar la fecha de primera cuota.");
				return ;  
			}
		}
		
		if(persona1==null && persona2==null && persona3==null && persona4==null && persona5==null ) {
			addErrorMessage("Seleccionar al menos una persona de venta.");
			return ;  
		}
		Contrato contrato = new Contrato();
		contrato.setLote(loteSelected);
		contrato.setEstado(EstadoContrato.ACTIVO.getName());
		contrato.setFechaVenta(fechaVenta);
		contrato.setMontoVenta(montoVenta);
		contrato.setTipoPago(tipoPago);
		
		if(contrato.getTipoPago().equals("Contado")) {
			contrato.setMontoInicial(null);
			contrato.setNumeroCuota(null); 
			contrato.setInteres(null);
		}else {
			contrato.setMontoInicial(montoInicial);
			contrato.setNumeroCuota(nroCuotas); 
			contrato.setInteres(interes);
			contrato.setFechaPrimeraCuota(fechaPrimeraCuota); 
		}
		
		contrato.setPersonVenta(persona1);
		contrato.setPersonVenta2(persona2);
		contrato.setPersonVenta3(persona3);
		contrato.setPersonVenta4(persona4);
		contrato.setPersonVenta5(persona5);
		contrato.setFirma(false);
		contrato.setPagoIndependizacion(false);
		contrato.setCuotaEspecial(cuotaEspecial);
		contrato.setCompromisoPago("NO");
		contrato.setFechaVencimientoComp(null); 
		
		if(cuotaEspecial) {
			if(montoCuotaEspecial==null) {
				addErrorMessage("Ingresar un monto de cuota especial.");
				return;
			}else {
				if(montoCuotaEspecial.compareTo(BigDecimal.ZERO) < 1) {
					addErrorMessage("El monto de la cuota especial tiene que ser mayor que cero.");
					return;
				}
			}
			
			if(hastaCuotaEspecial==null) {
				addErrorMessage("Ingresar una duración de la cuota especial.");
				return;
			}else {
				if(hastaCuotaEspecial<=0) {
					addErrorMessage("La duración de la cuota especial tiene que ser mayor que cero.");
					return;
				}
			}
		}
		
		Contrato contratoSave = contratoService.save(contrato);
		
		if(contratoSave!= null) {
			if(contratoSave.getTipoPago().equals("Crédito")) {
				botonVerCuota(true, contratoSave); 
			}else {
				Cuota cuota = new Cuota();
				cuota.setNroCuota(0);
				cuota.setFechaPago(fechaVenta);
				cuota.setCuotaSI(montoVenta);
				cuota.setInteres(BigDecimal.ZERO);
				cuota.setCuotaTotal(montoVenta);
				cuota.setAdelanto(BigDecimal.ZERO);
				
				RequerimientoSeparacion requerimiento = requerimientoSeparacionService.findAllByLoteAndEstado(contrato.getLote(), "Aprobado");
				if(requerimiento!=null) {
					List<DetalleDocumentoVenta> detalle = detalleDocumentoVentaService.findByRequerimientoSeparacionAndDocumentoVentaEstado(requerimiento, true); 
					if(!detalle.isEmpty()) {
						for(DetalleDocumentoVenta d: detalle) {
							if(d.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("F") || d.getDocumentoVenta().getTipoDocumento().getAbreviatura().equals("B")) {
								cuota.setAdelanto(d.getImporteVenta());
							}
						}
						
					}
				} 
				
				cuota.setPagoTotal("N");
				cuota.setContrato(contrato);
				cuota.setEstado(true);
				cuota.setOriginal(true);
				cuotaService.save(cuota);
			}
			
			
			List<PlantillaVenta> lstPlantilla = plantillaVentaService.findByEstadoAndLote("Aprobado", contratoSave.getLote());
			if(!lstPlantilla.isEmpty()) {
				PlantillaVenta p = lstPlantilla.get(lstPlantilla.size()-1);
				p.setRealizoContrato(true);
				plantillaVentaService.save(p);
			}
			
			
		}
		
		loteSelected.setRealizoContrato("S"); 
		loteService.save(loteSelected);
		eliminarLoteSelected();
		addInfoMessage("Se guardó correctamente el contrato"); 
		
	
		

	}
	
	public void iniciarLazy() {

		lstContratoLazy = new LazyDataModel<Contrato>() {
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
               
				String personaVenta = "%" + (filterBy.get("personVenta.surnames") != null ? filterBy.get("personVenta.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String manzana = "%" + (filterBy.get("lote.manzana.name") != null ? filterBy.get("lote.manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String numLote = "%" + (filterBy.get("lote.numberLote") != null ? filterBy.get("lote.numberLote").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
	
				
               Sort sort=Sort.by("fechaVenta").ascending();
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
               
                Page<Contrato> pageContrato=null;
               
                if(projectFilter != null) {
                	if(cuotaEspecialFilter) {
    	                pageContrato= contratoService.findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(estado, navegacionBean.getSucursalLogin(),projectFilter, manzana, numLote, personaVenta, cuotaEspecialFilter, compromisoPagoFilter,pageable);

                	}else {
    	                pageContrato= contratoService.findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(estado, navegacionBean.getSucursalLogin(),projectFilter, manzana, numLote, personaVenta,compromisoPagoFilter,pageable);

                	}

				}else {
					if(cuotaEspecialFilter) {
		                pageContrato= contratoService.findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCuotaEspecialAndCompromisoPagoLike(estado, navegacionBean.getSucursalLogin(), manzana, numLote, personaVenta, cuotaEspecialFilter,compromisoPagoFilter,pageable);

					}else {
		                pageContrato= contratoService.findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndPersonVentaSurnamesLikeAndCompromisoPagoLike(estado, navegacionBean.getSucursalLogin(), manzana, numLote, personaVenta,compromisoPagoFilter,pageable);

					}
	                
				}
                
                
                setRowCount((int) pageContrato.getTotalElements());
                return datasource = pageContrato.getContent();
            }
		};
	}
	
	public void formatoContado() throws IOException, XmlException {

		// initialize a blank document
		XWPFDocument document = new XWPFDocument();
		// create a new file
		// create a new paragraph paragraph
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun runTitle = paragraph.createRun();
		runTitle.setText("CONTRATO DE COMPRA VENTA DE BIEN INMUEBLE AL CONTADO");
		runTitle.setBold(true);
		runTitle.setFontFamily("Century Gothic");
		runTitle.setFontSize(12);

		
		XWPFParagraph paragraph2 = document.createParagraph();
		paragraph2.setAlignment(ParagraphAlignment.BOTH);
		
		XWPFRun run = paragraph2.createRun();
		run.setText("POR INTERMEDIO DEL PRESENTE DOCUMENTO QUE CELEBRAN, DE UNA PARTE, " );estiloNormalTexto(run);
		
		run = paragraph2.createRun();run.setText("ALDASA INMOBILIARIA S.A.C.");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText(", CON RUC N° 20607274526, REPRESENTADA POR SU ");estiloNormalTexto(run);
				
		run = paragraph2.createRun();run.setText("GERENTE GENERAL ALAN CRUZADO BALCÁZAR, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("IDENTIFICADO CON DNI. N° 44922055, DEBIDAMENTE INSCRITO EN LA ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("PARTIDA ELECTRÓNICA N° 11352661 "); estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("DEL REGISTRO DE PERSONAS JURÍDICAS DE LA ZONA REGISTRAL N° II - SEDE - CHICLAYO, CON DOMICILIO EN CAL. LOS AMARANTOS NRO. 245 URB. FEDERICO VILLARREAL, DISTRITO Y PROVINCIA DE CHICLAYO, DEPARTAMENTO DE LAMBAYEQUE; A QUIEN SE LE DENOMINARÁ EN LO SUCESIVO ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText("LA PARTE VENDEDORA; ");estiloNegritaTexto(run); 
		run = paragraph2.createRun();run.setText("A FAVOR DE EL (LA) (LOS) SR. (A.) (ES.) ");run.setFontFamily("Century Gothic");run.setFontSize(9);
		
		List<Person> lstPersonas = new ArrayList<>();
		if(contratoSelected.getPersonVenta()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta());
		}
		if(contratoSelected.getPersonVenta2()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta2());
		}
		if(contratoSelected.getPersonVenta3()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta3());
		}
		if(contratoSelected.getPersonVenta4()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta4());
		}
		if(contratoSelected.getPersonVenta5()!=null) {
			lstPersonas.add(contratoSelected.getPersonVenta5());
		}
		
		int contador = 1;
		for(Person p: lstPersonas) {
			run = paragraph2.createRun();run.setText(p.getNames().toUpperCase()+" "+p.getSurnames().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DE OCUPACION ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getOccupation().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", ESTADO CIVIL ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCivilStatus().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", IDENTIFICADO(A) CON DNI N° ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDni());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CON DOMICILIO EN ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getAddress().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DISTRITO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", PROVINCIA DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", DEPARTAMENTO DE ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getDistrict().getProvince().getDepartment().getName().toUpperCase());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CELULAR ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getCellphone());estiloNegritaTexto(run);
			run = paragraph2.createRun();run.setText(", CORREO ELECTRONICO ");estiloNormalTexto(run);
			run = paragraph2.createRun();run.setText(p.getEmail().toUpperCase());estiloNegritaTexto(run); 
			
			
			if(lstPersonas.size()==contador){
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO, ");estiloNormalTexto(run);
			}else {
				run = paragraph2.createRun();run.setText(", PARA ESTE ACTO Y ");estiloNormalTexto(run);
			}
			contador++;
		}
		run = paragraph2.createRun();run.setText("A QUIEN(ES) EN LO SUCESIVO SE LE(S) DENOMINARÁ");estiloNormalTexto(run);
		run = paragraph2.createRun();run.setText(" LA PARTE COMPRADORA, ");estiloNegritaTexto(run);
		run = paragraph2.createRun();run.setText("EL CONTRATO SE CELEBRA CON ARREGLO A LAS SIGUIENTES CONSIDERACIONES:");estiloNormalTexto(run);

		
		
		XWPFParagraph paragraphPrimero = document.createParagraph();
		paragraphPrimero.setAlignment(ParagraphAlignment.LEFT);
		
		XWPFRun runPrimero = paragraphPrimero.createRun();
		runPrimero.addBreak();runPrimero.addBreak();
		runPrimero.setText("PRIMERO.");estiloNegritaTexto(runPrimero);
		runPrimero.setUnderline(UnderlinePatterns.SINGLE);
		
		
		

		String cTAbstractNumBulletXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Symbol\" w:hAnsi=\"Symbol\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"o\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Courier New\" w:hAnsi=\"Courier New\" w:cs=\"Courier New\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"bullet\"/><w:lvlText w:val=\"\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr><w:rPr><w:rFonts w:ascii=\"Wingdings\" w:hAnsi=\"Wingdings\" w:hint=\"default\"/></w:rPr></w:lvl>"
				+ "</w:abstractNum>";

		String cTAbstractNumDecimalXML = "<w:abstractNum xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" w:abstractNumId=\"0\">"
				+ "<w:multiLevelType w:val=\"hybridMultilevel\"/>"
				+ "<w:lvl w:ilvl=\"0\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"720\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"1\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"1440\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "<w:lvl w:ilvl=\"2\" w:tentative=\"1\"><w:start w:val=\"1\"/><w:numFmt w:val=\"decimal\"/><w:lvlText w:val=\"%1.%2.%3\"/><w:lvlJc w:val=\"left\"/><w:pPr><w:ind w:left=\"2160\" w:hanging=\"360\"/></w:pPr></w:lvl>"
				+ "</w:abstractNum>";

				
		CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumBulletXML);
//	CTNumbering cTNumbering = CTNumbering.Factory.parse(cTAbstractNumDecimalXML);
		CTAbstractNum cTAbstractNum = cTNumbering.getAbstractNumArray(0);
		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
		XWPFNumbering numbering = document.createNumbering();
		BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
		BigInteger numID = numbering.addNum(abstractNumID);

		XWPFParagraph paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ANTEDEDENTES");estiloNegritaTexto(run);

		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES PROPIETARIO DE LOS BIENES INMUEBLES IDENTIFICADOS COMO: ");estiloNormalTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText("UBIC, RUR. "+contratoSelected.getLote().getProject().getUbicacion().toUpperCase()+" / SECTOR "+contratoSelected.getLote().getProject().getSector().toUpperCase()+" / PREDIO "+contratoSelected.getLote().getProject().getPredio().toUpperCase()+" – COD. PREDIO. "+contratoSelected.getLote().getProject().getCodigoPredio().toUpperCase()+", "
						+ "ÁREA "+contratoSelected.getLote().getProject().getAreaHectarea().toUpperCase()+" U.C. "+contratoSelected.getLote().getProject().getUnidadCatastral().toUpperCase()+", DISTRITO "
						+ "DE "+contratoSelected.getLote().getProject().getDistrict().getName()+", PROVINCIA DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getName()+", DEPARTAMENTO "
						+ "DE "+contratoSelected.getLote().getProject().getDistrict().getProvince().getDepartment().getName()+", "
						+ "EN LO SUCESIVO DENOMINADO EL BIEN. LOS LINDEROS, MEDIDAS PERIMÉTRICAS, DESCRIPCIÓN Y DOMINIO DEL BIEN CORREN "
						+ "INSCRITOS EN LA PARTIDA ELECTRÓNICA N° ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText(", DEL REGISTRO DE PREDIOS DE LA ZONA REGISTRAL N° ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText(contratoSelected.getLote().getProject().getZonaRegistral()+".");estiloNegritaTexto(run); 
		

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("2. ");
		run.setBold(true);
		run = paragrapha.createRun();
		run.setText(
				"LOS PREDIOS SEÑALADOS EN LOS PÁRRAFOS QUE PRECEDEN, FORMAN UN SOLO PREDIO EN TERRENO Y UBICACIÓN FÍSICA, "
						+ "EN EL CUAL SE DESARROLLARÁ EL PROYECTO DE LOTIZACIÓN "+contratoSelected.getLote().getProject().getName().toUpperCase()+" Y EL CUAL ES MATERIA DE VENTA A "
						+ "TRAVÉS DEL PRESENTE CONTRATO. ");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEGUNDO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBJETO");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("POR EL PRESENTE CONTRATO, ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("VENDE A LA ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DE TERRENO(S) POR INDEPENDIZAR DEL BIEN DE MAYOR EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO, EL (LOS) CUAL(ES) "
				+ "TIENE(N) LAS SIGUIENTES CARACTERÍSTICAS: ");estiloNormalTexto(run); 
		
		
		
	

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("1. MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" (ÁREA TOTAL "+String.format("%,.2f",contratoSelected.getLote().getArea()) +" M2)");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("EL ÁREA DE EL LOTE, MATERIA DE ESTE CONTRATO, SE ENCUENTRA DENTRO DE LA MANZANA "+contratoSelected.getLote().getManzana().getName()+" LOTE "+contratoSelected.getLote().getNumberLote()+" "
				+ "EN LA CUAL CONSTA UN ÁREA DE "+String.format("%,.2f",contratoSelected.getLote().getArea())+" M2 Y QUE FORMA PARTE DEL PROYECTO DE LOTIZACIÓN DEL BIEN DE MAYOR "
				+ "EXTENSIÓN ESPECIFICADO EN LA CLÁUSULA PRIMERA DE ESTE CONTRATO ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("PARTIDA ELECTRÓNICA:"+contratoSelected.getLote().getProject().getNumPartidaElectronica());estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.setUnderline(UnderlinePatterns.SINGLE); 
		run.setText("LINDEROS Y MEDIDAS PERIMÉTRICAS:");estiloNegritaTexto(run);
		

		paragrapha = document.createParagraph();
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("Área del lote: "+String.format("%,.2f",contratoSelected.getLote().getArea())+" m2");estiloNormalTexto(run);
		run.addBreak();
		run.setText("Perímetro del lote: "+String.format("%,.2f",contratoSelected.getLote().getPerimetro())+" ml ");estiloNormalTexto(run);
		run.addBreak();
		run.addBreak();
		run.setText("LINDEROS");
		run.addBreak();
		run.setText("Frente         : "+contratoSelected.getLote().getLimiteFrontal());
		run.addBreak();
		run.setText("Fondo         : "+contratoSelected.getLote().getLimiteFondo());
		run.addBreak();
		run.setText("Derecha     : "+contratoSelected.getLote().getLimiteDerecha());
		run.addBreak();
		run.setText("Izquierda    : "+contratoSelected.getLote().getLimiteIzquierda());
		

		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("PRECIO DE COMPRA VENTA.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();run.setText("EL PRECIO PACTADO POR LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DESCRITO EN LA CLÁUSULA SEGUNDA ES DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("S./"+String.format("%,.2f",contratoSelected.getMontoVenta())+" ("+numeroAletra.Convertir(contratoSelected.getMontoVenta()+"", true, "")+" SOLES), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL CUAL SE PAGÓ AL CONTADO CON DESPÓSITOS ");estiloNormalTexto(run);
	
 		if(lstCuentaBancaria!=null) {
 			int count = 1;
 			for(CuentaBancaria cta:lstCuentaBancaria) {
 				run = paragrapha.createRun();run.setText("EN CUENTA DEL ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getBanco().getNombre().toUpperCase()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CON NÚMERO DE CUENTA ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getNumero()+", ");estiloNegritaTexto(run);
 				run = paragrapha.createRun();run.setText("CCI ");estiloNormalTexto(run);
 				run = paragrapha.createRun();run.setText(cta.getCci());estiloNegritaTexto(run);
 				
 				if(count==lstCuentaBancaria.size()) {
 					run = paragrapha.createRun();run.setText(", ");estiloNormalTexto(run);
 				}else {
 					run = paragrapha.createRun();run.setText(" Y/O ");estiloNormalTexto(run);
 				}
 				count++;
 			}
 		}
 		run = paragrapha.createRun();run.setText("A FAVOR DE LA PARTE VENDEDORA, EL MEDIO DE PAGO SE PRESENTA A LA FIRMA DEL PRESENTE CONTRATO.");estiloNormalTexto(run);
 		
		
	
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("CUARTO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TÉRMINOS DEL CONTRATO");estiloNegritaTexto(run);
			
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA CONOCER A CABALIDAD EL ESTADO DE CONSERVACIÓN FÍSICA Y SITUACIÓN TÉCNICO-LEGAL DEL "
				+ "INMUEBLE, MOTIVO POR EL CUAL NO SE ACEPTARÁN RECLAMOS POR LOS INDICADOS CONCEPTOS, NI POR CUALQUIER OTRA CIRCUNSTANCIA "
				+ "O ASPECTO, NI AJUSTES DE VALOR, EN RAZÓN DE TRANSFERIRSE EL INMUEBLE EN LA CONDICIÓN DE “CÓMO ESTÁ” Y “AD-CORPUS”.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ALCANCES DE LA COMPRAVENTA DEFINITIVA");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA VENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("(LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("COMPRENDERÁ TODO CUANTO DE HECHO Y POR DERECHO CORRESPONDE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SIN RESERVA NI LIMITACIÓN ALGUNA, INCLUYENDO EL SUELO, SUBSUELO, SOBRESUELO, LAS CONSTRUCCIONES Y DERECHOS SOBRE ÉL, "
				+ "LOS AIRES, ENTRADAS, SALIDAS Y CUALQUIER DERECHO QUE LE CORRESPONDA A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("ENTREGA DE “LOS LOTES”:");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES PRECISAN, QUE LA ENTREGA DE LA POSESIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE REALIZARÁ A LA CANCELACIÓN DEL PAGO TOTAL DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("POR PARTE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("CON LA VERIFICACIÓN DE LOS DEPÓSITOS REALIZADOS EN LA CUENTA DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA, ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("PARA LUEGO REALIZAR LA SUSCRIPCIÓN DE LA MINUTA CORRESPONDIENTE Y POSTERIOR ESCRITURA PÚBLICA.");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("CONMUTATIVIDAD DE PRESTACIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LAS PARTES DECLARAN QUE ENTRE EL PRECIO Y ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("QUE SE ENAJENA(N), EXISTE LA MÁS JUSTA Y PERFECTA "
				+ "EQUIVALENCIA Y QUE SI HUBIERE ALGUNA DIFERENCIA DE MÁS O DE MENOS, SE HACEN MUTUAS Y RECÍPROCA DONACIÓN, RENUNCIANDO, "
				+ "EN CONSECUENCIA, A CUALQUIER ACCIÓN POSTERIOR QUE TIENDA A INVALIDAR EL PRESENTE CONTRATO Y A LOS PLAZOS PARA INTERPONERLA.");estiloNormalTexto(run);
		
			
				
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();	
		run.setText("QUINTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("LIBRE DISPONIBILIDAD DE DOMINIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("DECLARA QUE TRANSFIERE A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("OBJETO DE ESTE CONTRATO, "
				+ "LIBRE DE TODA CARGA O GRAVAMEN, DERECHO REAL DE GARANTÍA, PROCEDIMIENTO Y/O PROCESO JUDICIAL DE PRESCRIPCIÓN "
				+ "ADQUISITIVA DE DOMINIO, REIVINDICACIN, TÍTULOS SUPLETORIO, LABORAL, PROCESO ADMINISTRATIVO, EMBARGO, MEDIDA "
				+ "INCOATIVA, Y/O CUALQUIER MEDIDA CAUTELAR, ACCIÓN JUDICIAL O EXTRAJUDICIAL Y, EN GENERAL, DE TODO ACTO JURÍDICO, "
				+ "PROCESAL Y/O ADMINISTRATIVO, HECHO O CIRCUNSTANCIA QUE CUESTIONE, IMPIDA, PRIVE O LIMITE LA PROPIEDAD Y LIBRE "
				+ "DISPOSICIÓN DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DEL PRESENTE CONTRATO, POSESIÓN O USO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S).");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, SIN PERJUICIO DE LO SEÑALADO EN EL PÁRRAFO ANTERIOR, CON RELACIÓN A ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S), ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO EXISTE NINGUNA "
				+ "ACCIÓN O LITIGIO JUDICIAL, ARBITRAL, ADMINISTRATIVO, NI DE CUALQUIER OTRA ÍNDOLE, IMPULSADO POR ALGÚN PRECARIO "
				+ "Y/O COPROPIETARIO NO REGISTRADO, Y/O CUALQUIER TERCERO QUE ALEGUE, RECLAME Y/O INVOQUE DERECHO REAL, PERSONAL "
				+ "Y/O DE CRÉDITO ALGUNO, Y EN GENERAL CUALQUIER DERECHO SUBJETIVO Y/O CONSTITUCIONAL.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN SUPERPOSICIÓN O DUPLICIDAD REGISTRAL, CON OTRO(S) BIEN(ES) INMUEBLE(S) "
				+ "INSCRITO(S), EXTENDIÉNDOSE ESTA AFIRMACIÓN A CUALQUIER OTRO(S) BIEN(ES) INMUEBLE(S) NO INSCRITO(S), Y QUE NO SE "
				+ "ENCUENTRA AFECTADO POR TRAZO DE VÍA(S) ALGUNA(S), NI UBICADO EN “ZONA DE RIESGO” QUE IMPIDA O DIFICULTE EL DESARROLLO "
				+ "DE CUALQUIER CONSTRUCCIÓN Y/O PROYECTO INMOBILIARIO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("QUE, ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("NO SE ENCUENTRA(N) EN ZONA MONUMENTAL O ZONA ARQUEOLÓGICA QUE IMPIDA O DIFICULTE EL DESARROLLO DE "
				+ "CUALQUIER PROYECTO INMOBILIARIO. ");estiloNormalTexto(run);
				
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SEXTO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		run.addBreak();
		run.setText("LA PARTE COMPRADORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("UNA VEZ ENTREGADA LA MINUTA FIRMADA POR LA PARTE VENDEDORA A ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA, ");estiloNegritaTexto(run); 
		run = paragrapha.createRun();run.setText("ES DE SU CARGO REALIZAR LA INDEPENDIZACIÓN DE SU(S) LOTE(S) ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE.");estiloNormalTexto(run); 

		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("DECLARAR LA COMPRA DE ");estiloNormalTexto(run); 
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("QUE ADQUIERE(N) EN VIRTUD DEL PRESENTE DOCUMENTO ANTE LA MUNICIPALIDAD DISTRITAL CORRESPONDIENTE Y "
				+ "ANTE LAS OFICINAS DEL SERVICIO DE ADMINISTRACIÓN TRIBUTARIA.");estiloNormalTexto(run); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO A LA ALCABALA EN CASO CORRESPONDA.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("D) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("PAGAR EL IMPUESTO PREDIAL Y ARBITRIOS, UNA VEZ ADQUIRIDO(S) ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("Y DECLARADO(S) ANTE LA MUNICIPALIDAD DISTRITAL RESPECTIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("SÉPTIMO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("OBLIGACIONES DE LA PARTE VENDEDORA.");
		run.setBold(true);
		run.setFontFamily("Century Gothic");
		run.setFontSize(9);
		
		run.addBreak();
		run.setText("LA PARTE VENDEDORA SE OBLIGA A:"); 
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("A) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("INSTALACIÓN DE LUZ Y AGUA EN EL PROYECTO INMOBILIARIO, CON REDES TRONCALES, MAS NO EN ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("EL (LOS) LOTE(S) ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("MATERIA DE VENTA DEL CONTRATO.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("B) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("CONSTRUCCIÓN E INSTALACIÓN DE PÓRTICO DE ENTRADA, AFIRMAMENTO DE CALLES PRINCIPALES.");estiloNormalTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		paragrapha.setIndentationLeft(500);
		run = paragrapha.createRun();
		run.setText("C) ");
		run.setBold(true);
		run = paragrapha.createRun();run.setText("OTORGAMIENTO DE MINUTAS Y ESCRITURAS PÚBLICAS, PARA LOS RESPECTIVOS TRÁMITES QUE TENGAN QUE REALIZAR ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run); 
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("OCTAVO.");estiloNegritaTexto(run); 
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun(); 
		run.setText("PAGO DE TRIBUTOS Y OTRAS IMPOSICIONES.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE VENDEDORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE SOLIDARIZA FRENTE AL FISCO RESPECTO DE CUALQUIER IMPUESTO, CONTRIBUCIÓN O DERECHOS DE "
				+ "SERVICIO DE AGUA POTABLE O ENERGÍA ELÉCTRICA, ASÍ COMO EL IMPUESTO PREDIAL, ARBITRIOS MUNICIPALES Y CONTRIBUCIÓN "
				+ "DE MEJORAS, QUE PUDIERA AFECTAR EL (LOS) LOTE(S) QUE SE VENDEN Y QUE SE ENCUENTREN PENDIENTES DE PAGO HASTA EL DIA "
				+ "DE PRODUCIDA LA TRANSFERENCIA, FECHA A PARTIR DE LA CUAL SERÁN DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA.");estiloNegritaTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("NOVENO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("TRIBUTOS QUE AFECTAN AL CONTRATO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("ES DE CARGO DE ");estiloNormalTexto(run);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("EL PAGO DEL IMPUESTO DE ALCABALA A QUE HUBIERE LUGAR.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("LA PARTE COMPRADORA ");estiloNegritaTexto(run);
		run = paragrapha.createRun();run.setText("SE HARÁ CARGO DE LOS GASTOS NOTARIALES QUE GENEREN LA MINUTA Y ESCRITURA PÚBLICA DE COMPRAVENTA DEFINITIVA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO PRIMERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("COMPETENCIA JURISDICCIONAL.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("PARA TODO LO RELACIONADO CON EL FIEL CUMPLIMIENTO DE LAS CLÁUSULAS DE ESTE CONTRATO, LAS PARTES ACUERDAN, SOMETERSE A LA "
				+ "JURISDICCIÓN DE LOS JUECES Y TRIBUNALES DE CHICLAYO, RENUNCIANDO AL FUERO DE SUS DOMICILIOS Y SEÑALANDO COMO TALES, LOS "
				+ "CONSIGNADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO SEGUNDO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("DOMICILIO.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();
		run.setText("LAS PARTES SEÑALAN COMO SUS DOMICILIOS LOS INDICADOS EN LA INTRODUCCIÓN DEL PRESENTE DOCUMENTO, LUGARES A LOS QUE SERÁN "
				+ "DIRIGIDAS TODAS LAS COMUNICACIONES O NOTIFICACIONES A QUE HUBIERA LUGAR. CUALQUIER CAMBIO DE DOMICILIO, PARA SER VÁLIDO, "
				+ "DEBERÁ SER COMUNICADO A LA OTRA PARTE MEDIANTE CARTA CURSADA POR CONDUCTO NOTARIAL CON UNA ANTICIPACIÓN NO MENOR DE 5 "
				+ "(CINCO) DÍAS, ESTABLECIÉNDOSE QUE LOS CAMBIOS NO COMUNICADOS EN LA FORMA PREVISTA EN ESTA CLÁUSULA SE TENDRÁN POR NO HECHOS "
				+ "Y SERÁN VALIDAS LAS COMUNICACIONES CURSADAS AL ÚLTIMO DOMICILIO CONSTITUIDO SEGÚN LA PRESENTE CLÁUSULA.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();
		run.setText("DÉCIMO TERCERO.");estiloNegritaTexto(run);
		run.setUnderline(UnderlinePatterns.SINGLE);
		
		paragrapha = document.createParagraph();
		paragrapha.setNumID(numID);
		run = paragrapha.createRun();
		run.setText("APLICACION SUPLETORIA DE LA LEY.");estiloNegritaTexto(run);
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN LO NO PREVISTO POR LAS PARTES EN EL PRESENTE CONTRATO, AMBAS SE SOMETEN A LO ESTABLECIDO "
				+ "POR LAS NORMAS DEL CÓDIGO CIVIL Y DEMÁS DEL SISTEMA JURÍDICO NACIONAL QUE RESULTEN APLICABLES.");estiloNormalTexto(run);
		
		
		paragrapha = document.createParagraph();
		paragrapha.setAlignment(ParagraphAlignment.BOTH);
		run = paragrapha.createRun();run.setText("EN SEÑAL DE CONFORMIDAD LAS PARTES SUSCRIBEN ESTE DOCUMENTO EN LA CIUDAD DE CHICLAYO A LOS "+numeroAletra.convertirSoloNumero(sdfDay.format(contratoSelected.getFechaVenta())).toUpperCase()+" ("+sdfDay.format(contratoSelected.getFechaVenta())+") "
				+ "DÍAS DEL MES DE "+meses[Integer.parseInt(sdfM.format(contratoSelected.getFechaVenta()))-1]+" DE "+sdfY.format(contratoSelected.getFechaVenta())+" ("+numeroAletra.convertirSoloNumero(sdfY.format(contratoSelected.getFechaVenta())).toUpperCase()+").");estiloNormalTexto(run);
		
		
		
		paragrapha = document.createParagraph();
		run = paragrapha.createRun();
		run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();run.addBreak();
		run.setText("___________________________________");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("      ALAN CRUZADO BALCÁZAR");estiloNormalTexto(run);
		run.addBreak();
		run = paragrapha.createRun();run.setText("                  DNI: 44922055");estiloNormalTexto(run);
     
		
		
		try {			
			 ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
	            File file = new File(filePath);
	            FileOutputStream out = new FileOutputStream(file);
	            document.write(out);
	            out.close();
	            fileDes=null;
	            fileDes = DefaultStreamedContent.builder()
	                    .name(nombreArchivo)
	                    .contentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
	                    .build();
            
    		 
      
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}  
	
	public  void estiloCentrarCeldaTabla(XWPFTableCell cell) {  
        CTTc cttc = cell.getCTTc();  
        CTTcPr ctPr = cttc.addNewTcPr();  
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);  
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);  
    } 
	
	public void estiloNegritaTexto(XWPFRun run) {
		run.setBold(true); run.setFontFamily("Century Gothic");run.setFontSize(9);
	}
	
	public void estiloNormalTexto(XWPFRun run) {
		run.setFontFamily("Century Gothic");run.setFontSize(9);
	}

	public void setTableWidth(XWPFTable table,String width){  
        CTTbl ttbl = table.getCTTbl();  
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();  
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();  
        CTJc cTJc=tblPr.addNewJc();  
        cTJc.setVal(STJc.Enum.forString("center"));  
        tblWidth.setW(new BigInteger(width));  
        tblWidth.setType(STTblWidth.DXA);  
    } 
	
	public  void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {  
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {  
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);  
            if ( cellIndex == fromCell ) {  
                // The first merged cell is set with RESTART merge value  
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);  
            } else {  
                // Cells which join (merge) the first one, are set with CONTINUE  
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);  
            }  
        }  
    }  
	
	public  void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {  
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {  
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);  
            if ( rowIndex == fromRow ) {  
                // The first merged cell is set with RESTART merge value  
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);  
            } else {  
                // Cells which join (merge) the first one, are set with CONTINUE  
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);  
            }  
        }  
    } 
	 
	public  void fillTable(XWPFTable table, List<String> texto) {  
        for (int rowIndex = 0; rowIndex < table.getNumberOfRows(); rowIndex++) {  
            XWPFTableRow row = table.getRow(rowIndex);  
            row.setHeight(380);  
            for (int colIndex = 0; colIndex < row.getTableCells().size(); colIndex++) {  
                XWPFTableCell cell = row.getCell(colIndex);  
                
                cell.setText(texto.get(colIndex));
//                if(rowIndex%2==0){  
//                     setCellText(cell, " cell " + rowIndex + colIndex + " ", "D4DBED", 1000);  
//                }else{  
//                     setCellText(cell, " cell " + rowIndex + colIndex + " ", "AEDE72", 1000);  
//                }  
            }  
        }  
    }
	
	public Date sumarRestarMeses(Date fecha, int meses) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH, meses);
		return calendar.getTime();
	}
	
	public void simularCuotas (Contrato contrato) {
		lstSimulador.clear();
		
//		Simulador filaInicio = new Simulador();
//		filaInicio.setNroCuota("0");
//		filaInicio.setFechaPago(contrato.getFechaVenta());
//		filaInicio.setInicial(contrato.getMontoInicial());
//		filaInicio.setCuotaSI(BigDecimal.ZERO);
//		filaInicio.setInteres(BigDecimal.ZERO);
//		filaInicio.setCuotaTotal(BigDecimal.ZERO);
//		lstSimulador.add(filaInicio);
//		
//		// una pista
		BigDecimal totalSI=BigDecimal.ZERO;
		BigDecimal totalInteres=BigDecimal.ZERO;
		BigDecimal totalCuotaTotal=BigDecimal.ZERO;
		
		List<Cuota> lstCuotaContrato = cuotaService.findByContratoAndOriginal(contrato, true);
		int contador = 0;
		for(Cuota c:lstCuotaContrato) {
			Simulador simulador = new Simulador();
			simulador.setNroCuota(c.getNroCuota()+"");
			simulador.setFechaPago(c.getFechaPago()); 
			if(contador==0) {
				simulador.setInicial(contrato.getMontoInicial());
				simulador.setCuotaSI(BigDecimal.ZERO);
			}else {
				simulador.setInicial(BigDecimal.ZERO);
				simulador.setCuotaSI(c.getCuotaSI());
			}
			
			simulador.setInteres(c.getInteres());
			simulador.setCuotaTotal(c.getCuotaTotal());
			lstSimulador.add(simulador);
			
			totalSI = totalSI.add(simulador.getCuotaSI()); //totalSI+simulador.getCuotaSI(); lo sbigdecimal se sunan de estam manera
			totalInteres = totalInteres.add(simulador.getInteres());
			totalCuotaTotal = totalCuotaTotal.add(simulador.getCuotaTotal());
			contador++;
		}
			
		Simulador filaTotal = new Simulador();
		filaTotal.setNroCuota("TOTAL");
		filaTotal.setInicial(contrato.getMontoInicial());
		filaTotal.setCuotaSI(totalSI);
		filaTotal.setInteres(totalInteres);
		filaTotal.setCuotaTotal(totalCuotaTotal); 
		lstSimulador.add(filaTotal);
			
		
	}
		
	public List<Person> completePersonSurnames(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : getLstPerson()) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }	
		
	public Converter getConversorPersonSurnames() {
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
	
	public Converter getConversorUsuario() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Usuario c = null;
                    for (Usuario si : lstUsuarioCobranza) {
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
                    return ((Usuario) value).getId() + "";
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
		
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public String getNombreLoteSelected() {
		return nombreLoteSelected;
	}
	public void setNombreLoteSelected(String nombreLoteSelected) {
		this.nombreLoteSelected = nombreLoteSelected;
	}
	public Date getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	public Date getFechaPrimeraCuota() {
		return fechaPrimeraCuota;
	}
	public void setFechaPrimeraCuota(Date fechaPrimeraCuota) {
		this.fechaPrimeraCuota = fechaPrimeraCuota;
	}
	public Person getPersona1() {
		return persona1;
	}
	public void setPersona1(Person persona1) {
		this.persona1 = persona1;
	}
	public Person getPersona2() {
		return persona2;
	}
	public void setPersona2(Person persona2) {
		this.persona2 = persona2;
	}
	public Person getPersona3() {
		return persona3;
	}
	public void setPersona3(Person persona3) {
		this.persona3 = persona3;
	}
	public Person getPersona4() {
		return persona4;
	}
	public void setPersona4(Person persona4) {
		this.persona4 = persona4;
	}
	public Person getPersona5() {
		return persona5;
	}
	public void setPersona5(Person persona5) {
		this.persona5 = persona5;
	}
	public BigDecimal getMontoVenta() {
		return montoVenta;
	}
	public void setMontoVenta(BigDecimal montoVenta) {
		this.montoVenta = montoVenta;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public BigDecimal getInteres() {
		return interes;
	}
	public void setInteres(BigDecimal interes) {
		this.interes = interes;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public Integer getNroCuotas() {
		return nroCuotas;
	}
	public void setNroCuotas(Integer nroCuotas) {
		this.nroCuotas = nroCuotas;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public ContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(ContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public LazyDataModel<Contrato> getLstContratoLazy() {
		return lstContratoLazy;
	}
	public void setLstContratoLazy(LazyDataModel<Contrato> lstContratoLazy) {
		this.lstContratoLazy = lstContratoLazy;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Contrato getContratoSelected() {
		return contratoSelected;
	}
	public void setContratoSelected(Contrato contratoSelected) {
		this.contratoSelected = contratoSelected;
	}
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
	}
	public String[] getMeses() {
		return meses;
	}
	public void setMeses(String[] meses) {
		this.meses = meses;
	}
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public List<Simulador> getLstSimulador() {
		return lstSimulador;
	}
	public void setLstSimulador(List<Simulador> lstSimulador) {
		this.lstSimulador = lstSimulador;
	}
	public StreamedContent getFileDes() {
		return fileDes;
	}
	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public NumeroALetra getNumeroAletra() {
		return numeroAletra;
	}
	public void setNumeroAletra(NumeroALetra numeroAletra) {
		this.numeroAletra = numeroAletra;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public SimpleDateFormat getSdfM() {
		return sdfM;
	}
	public void setSdfM(SimpleDateFormat sdfM) {
		this.sdfM = sdfM;
	}
	public SimpleDateFormat getSdfY() {
		return sdfY;
	}
	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}
	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}
	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}
	public SimpleDateFormat getSdfDay() {
		return sdfDay;
	}
	public void setSdfDay(SimpleDateFormat sdfDay) {
		this.sdfDay = sdfDay;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public List<Simulador> getLstSimuladorPrevio() {
		return lstSimuladorPrevio;
	}
	public void setLstSimuladorPrevio(List<Simulador> lstSimuladorPrevio) {
		this.lstSimuladorPrevio = lstSimuladorPrevio;
	}
	public CuotaService getCuotaService() {
		return cuotaService;
	}
	public void setCuotaService(CuotaService cuotaService) {
		this.cuotaService = cuotaService;
	}
	public RequerimientoSeparacionService getRequerimientoSeparacionService() {
		return requerimientoSeparacionService;
	}
	public void setRequerimientoSeparacionService(RequerimientoSeparacionService requerimientoSeparacionService) {
		this.requerimientoSeparacionService = requerimientoSeparacionService;
	}
	public void setNroCuotas(int nroCuotas) {
		this.nroCuotas = nroCuotas;
	}
	public VoucherService getVoucherService() {
		return voucherService;
	}
	public void setVoucherService(VoucherService voucherService) {
		this.voucherService = voucherService;
	}
	public DetalleDocumentoVentaService getDetalleDocumentoVentaService() {
		return detalleDocumentoVentaService;
	}
	public void setDetalleDocumentoVentaService(DetalleDocumentoVentaService detalleDocumentoVentaService) {
		this.detalleDocumentoVentaService = detalleDocumentoVentaService;
	}
	public List<Cuota> getLstCuotaVista() {
		return lstCuotaVista;
	}
	public void setLstCuotaVista(List<Cuota> lstCuotaVista) {
		this.lstCuotaVista = lstCuotaVista;
	}
	public DataSourceCronogramaPago getDt() {
		return dt;
	}
	public void setDt(DataSourceCronogramaPago dt) {
		this.dt = dt;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public ReportGenBo getReportGenBo() {
		return reportGenBo;
	}
	public void setReportGenBo(ReportGenBo reportGenBo) {
		this.reportGenBo = reportGenBo;
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
	public List<ObservacionContrato> getLstObservacionContrato() {
		return lstObservacionContrato;
	}
	public void setLstObservacionContrato(List<ObservacionContrato> lstObservacionContrato) {
		this.lstObservacionContrato = lstObservacionContrato;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public ObservacionContratoService getObservacionContratoService() {
		return observacionContratoService;
	}
	public void setObservacionContratoService(ObservacionContratoService observacionContratoService) {
		this.observacionContratoService = observacionContratoService;
	}
	public ObservacionContrato getObsSelected() {
		return obsSelected;
	}
	public void setObsSelected(ObservacionContrato obsSelected) {
		this.obsSelected = obsSelected;
	}
	public PlantillaVentaService getPlantillaVentaService() {
		return plantillaVentaService;
	}
	public void setPlantillaVentaService(PlantillaVentaService plantillaVentaService) {
		this.plantillaVentaService = plantillaVentaService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public String getNombreArchivoReporte() {
		return nombreArchivoReporte;
	}
	public void setNombreArchivoReporte(String nombreArchivoReporte) {
		this.nombreArchivoReporte = nombreArchivoReporte;
	}
	public List<Usuario> getLstUsuarioCobranza() {
		return lstUsuarioCobranza;
	}
	public void setLstUsuarioCobranza(List<Usuario> lstUsuarioCobranza) {
		this.lstUsuarioCobranza = lstUsuarioCobranza;
	}
	public Project getProjectFilter() {
		return projectFilter;
	}
	public void setProjectFilter(Project projectFilter) {
		this.projectFilter = projectFilter;
	}
	public List<Project> getLstProject() {
		return lstProject;
	}
	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public LazyDataModel<Lote> getLstLotesSinContratoLazy() {
		return lstLotesSinContratoLazy;
	}
	public void setLstLotesSinContratoLazy(LazyDataModel<Lote> lstLotesSinContratoLazy) {
		this.lstLotesSinContratoLazy = lstLotesSinContratoLazy;
	}
	public Project getProjectFilterLote() {
		return projectFilterLote;
	}
	public void setProjectFilterLote(Project projectFilterLote) {
		this.projectFilterLote = projectFilterLote;
	}

	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
	}
	public List<Imagen> getLstImagenVoucher() {
		return lstImagenVoucher;
	}
	public void setLstImagenVoucher(List<Imagen> lstImagenVoucher) {
		this.lstImagenVoucher = lstImagenVoucher;
	}
	public LoadImageDocumentoBean getLoadImageDocumentoBean() {
		return loadImageDocumentoBean;
	}
	public void setLoadImageDocumentoBean(LoadImageDocumentoBean loadImageDocumentoBean) {
		this.loadImageDocumentoBean = loadImageDocumentoBean;
	}
	public StreamedContent getFileDesVoucher() {
		return fileDesVoucher;
	}
	public void setFileDesVoucher(StreamedContent fileDesVoucher) {
		this.fileDesVoucher = fileDesVoucher;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public ProyectoPartidaService getProyectoPartidaService() {
		return proyectoPartidaService;
	}
	public void setProyectoPartidaService(ProyectoPartidaService proyectoPartidaService) {
		this.proyectoPartidaService = proyectoPartidaService;
	}
	public String getNombreArchivoReporteVoucher() {
		return nombreArchivoReporteVoucher;
	}
	public void setNombreArchivoReporteVoucher(String nombreArchivoReporteVoucher) {
		this.nombreArchivoReporteVoucher = nombreArchivoReporteVoucher;
	}
	public ImagenPlantillaVentaService getImagenPlantillaVentaService() {
		return imagenPlantillaVentaService;
	}
	public void setImagenPlantillaVentaService(ImagenPlantillaVentaService imagenPlantillaVentaService) {
		this.imagenPlantillaVentaService = imagenPlantillaVentaService;
	}
	public DocumentoVentaService getDocumentoVentaService() {
		return documentoVentaService;
	}
	public void setDocumentoVentaService(DocumentoVentaService documentoVentaService) {
		this.documentoVentaService = documentoVentaService;
	}
	public List<ObservacionContrato> getLstObsContrato() {
		return lstObsContrato;
	}
	public void setLstObsContrato(List<ObservacionContrato> lstObsContrato) {
		this.lstObsContrato = lstObsContrato;
	}
	public String getNombreArchivoObsContrato() {
		return nombreArchivoObsContrato;
	}
	public void setNombreArchivoObsContrato(String nombreArchivoObsContrato) {
		this.nombreArchivoObsContrato = nombreArchivoObsContrato;
	}
	public Date getFechaIniObs() {
		return fechaIniObs;
	}
	public void setFechaIniObs(Date fechaIniObs) {
		this.fechaIniObs = fechaIniObs;
	}
	public Date getFechaFinObs() {
		return fechaFinObs;
	}
	public void setFechaFinObs(Date fechaFinObs) {
		this.fechaFinObs = fechaFinObs;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public DetalleImagenLoteService getDetalleImagenLoteService() {
		return detalleImagenLoteService;
	}
	public void setDetalleImagenLoteService(DetalleImagenLoteService detalleImagenLoteService) {
		this.detalleImagenLoteService = detalleImagenLoteService;
	}
	public ContratoArchivoService getContratoArchivoService() {
		return contratoArchivoService;
	}
	public void setContratoArchivoService(ContratoArchivoService contratoArchivoService) {
		this.contratoArchivoService = contratoArchivoService;
	}
	public List<ContratoArchivo> getLstContratoArchivoSelected() {
		return lstContratoArchivoSelected;
	}
	public void setLstContratoArchivoSelected(List<ContratoArchivo> lstContratoArchivoSelected) {
		this.lstContratoArchivoSelected = lstContratoArchivoSelected;
	}
	public ContratoArchivo getArchivoSelected() {
		return archivoSelected;
	}
	public void setArchivoSelected(ContratoArchivo archivoSelected) {
		this.archivoSelected = archivoSelected;
	}
	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	public String getNombreArchivoUpload() {
		return nombreArchivoUpload;
	}
	public void setNombreArchivoUpload(String nombreArchivoUpload) {
		this.nombreArchivoUpload = nombreArchivoUpload;
	}
	public boolean isCuotaEspecial() {
		return cuotaEspecial;
	}
	public void setCuotaEspecial(boolean cuotaEspecial) {
		this.cuotaEspecial = cuotaEspecial;
	}
	public int getTotalCuotaEspecial() {
		return totalCuotaEspecial;
	}
	public void setTotalCuotaEspecial(int totalCuotaEspecial) {
		this.totalCuotaEspecial = totalCuotaEspecial;
	}
	public boolean isCuotaEspecialFilter() {
		return cuotaEspecialFilter;
	}
	public void setCuotaEspecialFilter(boolean cuotaEspecialFilter) {
		this.cuotaEspecialFilter = cuotaEspecialFilter;
	}
	public BigDecimal getMontoCuotaEspecial() {
		return montoCuotaEspecial;
	}
	public void setMontoCuotaEspecial(BigDecimal montoCuotaEspecial) {
		this.montoCuotaEspecial = montoCuotaEspecial;
	}
	public Integer getHastaCuotaEspecial() {
		return hastaCuotaEspecial;
	}
	public void setHastaCuotaEspecial(Integer hastaCuotaEspecial) {
		this.hastaCuotaEspecial = hastaCuotaEspecial;
	}
	public boolean isCompromisoPago() {
		return compromisoPago;
	}
	public void setCompromisoPago(boolean compromisoPago) {
		this.compromisoPago = compromisoPago;
	}
	public Date getFechaCompromiso() {
		return fechaCompromiso;
	}
	public void setFechaCompromiso(Date fechaCompromiso) {
		this.fechaCompromiso = fechaCompromiso;
	}
	public String getCompromisoPagoFilter() {
		return compromisoPagoFilter;
	}
	public void setCompromisoPagoFilter(String compromisoPagoFilter) {
		this.compromisoPagoFilter = compromisoPagoFilter;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public StreamedContent getFileResumen() {
		return fileResumen;
	}
	public void setFileResumen(StreamedContent fileResumen) {
		this.fileResumen = fileResumen;
	}
	public String getNombreResumenContrato() {
		return nombreResumenContrato;
	}
	public void setNombreResumenContrato(String nombreResumenContrato) {
		this.nombreResumenContrato = nombreResumenContrato;
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
	public List<Lote> getLstLotesCambio() {
		return lstLotesCambio;
	}
	public void setLstLotesCambio(List<Lote> lstLotesCambio) {
		this.lstLotesCambio = lstLotesCambio;
	}
	public Lote getLoteCambio() {
		return loteCambio;
	}
	public void setLoteCambio(Lote loteCambio) {
		this.loteCambio = loteCambio;
	}
	public Project getProyectoCambio() {
		return proyectoCambio;
	}
	public void setProyectoCambio(Project proyectoCambio) {
		this.proyectoCambio = proyectoCambio;
	}
	public Manzana getManzanaCambio() {
		return manzanaCambio;
	}
	public void setManzanaCambio(Manzana manzanaCambio) {
		this.manzanaCambio = manzanaCambio;
	}
	public SimpleDateFormat getSdfQuery() {
		return sdfQuery;
	}
	public void setSdfQuery(SimpleDateFormat sdfQuery) {
		this.sdfQuery = sdfQuery;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
		
	
}
