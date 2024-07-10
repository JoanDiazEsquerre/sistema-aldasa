package com.model.aldasa.proyecto.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException; 
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.ServletContext;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.ImagenPlantillaVenta;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.ObservacionContrato;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.ProyectoPartida;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Simulador;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.proyecto.jrdatasource.DataSourceCronogramaPago;
import com.model.aldasa.reporteBo.ReportGenBo;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.CuotaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
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
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.Perfiles;
import com.model.aldasa.util.UtilXls;
import com.model.aldasa.ventas.bean.LoadImageDocumentoBean;
import com.model.aldasa.ventas.jrdatasource.DataSourceDocumentoVenta; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.Period;

@ManagedBean
@ViewScoped
public class ReporteDeudaBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{cuotaService}")
	private CuotaService cuotaService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{observacionContratoService}")
	private ObservacionContratoService observacionContratoService;
	
	@ManagedProperty(value = "#{contratoService}")
	private ContratoService contratoService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	
	private LazyDataModel<Cuota> lstCuotaLazy;
	private LazyDataModel<Contrato> lstContratoLazy;
	private List<Project> lstProject;
	private List<Manzana> lstManzana;
	private List<Usuario> lstUsuarioCobranza;

	
	private Project projectFilter, projectGrafico, projectContratoFilter;
	private Manzana manzanaContratoFilter;
	
	String estadoFilter, manzanaFilter="", loteFilter="", surnamesFilter="", dniFilter="", estadoGrafico;
	int year, month, yearGrafico, busquedaCuotasAtrasadas = 0;
	BigDecimal total = BigDecimal.ZERO;
	private String estadoDeudaGrafico;
	
	private Cuota cuotaSelected;
	
	private BarChartModel barModel2;
	private DonutChartModel donutModel;
	
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
	
	private List<ObservacionContrato> lstObservacionContrato = new ArrayList<>();
	private List<Contrato> lstContratosGrafico;
	
	SimpleDateFormat sdfQuery = new SimpleDateFormat("yyyy-MM-dd");  
	
	@PostConstruct
	public void init() {
		estadoFilter = "N";
		year= Integer.parseInt(sdfY.format(new Date()));
		yearGrafico= Integer.parseInt(sdfY.format(new Date()));
		month= Integer.parseInt(sdfM.format(new Date()));
		lstProject= projectService.findByStatusOrderByNameAsc(true);
		lstManzana = manzanaService.findByStatusOrderByNameAsc(true);
		
		lstUsuarioCobranza = usuarioService.findByProfileIdAndStatus(Perfiles.ASISTENTE_COBRANZA.getId(), true);
		
		iniciarLazy();
		
		createBarModel2();
		
		estadoGrafico = EstadoContrato.ACTIVO.getName();
		estadoDeudaGrafico= "al Dia";
//		busquedaDatosGrafico();
		crearDonut();

		iniciarLazyContratos();
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
		
		addInfoMessage("Se actualizó las cuotas atrasadas de los contratos ACTIVOS");
 		
	}
	
//	public int calcularCuotasAtrasadas(Contrato c) {
//		
//		int numCuotasAtrasadas=0;
//			
//		List<Cuota> lstcuotas = cuotaService.findByContratoAndEstado(c, true);
//		if(!lstcuotas.isEmpty()) {
//			for(Cuota cuota : lstcuotas) {
//				if(cuota.getNroCuota()!=0) { 
//					if(cuota.getPagoTotal().equals("N") && !cuota.isPrepago()) {	
//						if(cuota.getFechaPago().before(new Date())) {
//							numCuotasAtrasadas++;
//							
//						}
//					}
//				}
//			
//			}
//		}
//			
//		return numCuotasAtrasadas;
//				
//		
//	}
	
	public void actualizarUsuarioCobranza(Contrato contrato) {
        contratoService.save(contrato);
        addInfoMessage("Se actualizó el Usuario de Cobranza correctamente.");
    }
	
	
	private void crearDonut() {
        donutModel = new DonutChartModel();
        ChartData data = new ChartData();

        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();
        
        List<Contrato> alDia = contratoService.findByEstadoAndCuotasAtrasadas(estadoGrafico, 0);
        List<Contrato> conMora = contratoService.findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(estadoGrafico, 0, "NO");
        List<Contrato> conCompromiso = contratoService.findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPago(estadoGrafico, 0, "SI");
        
//        List<Contrato> lstcompromiso = new ArrayList<>();
//		
//		for(Contrato c :conMora) {
//			if(c.getCompromisoPago().equals("SI")) { 
//				if(c.getFechaVencimientoComp().after(new Date()) || sdfQuery.format(c.getFechaVencimientoComp()).equals(sdfQuery.format(new Date()))) {   
//					lstcompromiso.add(c);
//				}
//			}
//		}
		
		
	
        
        values.add(conMora.size());
        values.add(alDia.size());
        values.add(conCompromiso.size());
//        values.add(lstcompromiso.size());
      
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        bgColors.add("rgb(25, 229, 28)");
       
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);

        List<String> labels = new ArrayList<>();
        labels.add("Atrasados");
        labels.add("Al Día");
        labels.add("Con Compromiso");
        data.setLabels(labels);

        donutModel.setData(data);
    }
	
	
	public void cargarListaObservacion() {
		lstObservacionContrato = observacionContratoService.findByEstadoAndContratoOrderByFechaRegistroDesc(true, cuotaSelected.getContrato());
	}
	
	public void createBarModel2() {
        barModel2 = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("CUOTAS PENDIENTES");
        barDataSet.setBackgroundColor("rgba(255, 99, 132, 0.2)");
        barDataSet.setBorderColor("rgb(255, 99, 132)");
        barDataSet.setBorderWidth(1);
        List<Number> values = new ArrayList<>();
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 1) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 1, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 2) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 2, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 3) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 3, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 4) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 4, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 5) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 5, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 6) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 6, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 7) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 7, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 8) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 8, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 9) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 9, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 10) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 10, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 11) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 11, projectGrafico.getId()));
        values.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "N", yearGrafico, 12) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "N", yearGrafico, 12, projectGrafico.getId()));
        barDataSet.setData(values);

        BarChartDataSet barDataSet2 = new BarChartDataSet();
        barDataSet2.setLabel("CUOTAS PAGADAS");
        barDataSet2.setBackgroundColor("rgba(75, 192, 192, 0.2)");
        barDataSet2.setBorderColor("rgb(75, 192, 192)");
        barDataSet2.setBorderWidth(1);
        List<Number> values2 = new ArrayList<>();
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 1) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 1, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 2) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 2, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 3) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 3, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 4) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 4, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 5) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 5, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 6) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 6, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 7) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 7, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 8) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 8, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 9) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 9, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 10) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 10, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 11) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 11, projectGrafico.getId()));
        values2.add(projectGrafico == null ? cuotaService.totalCuotasReporteGrafico_general(true, "S", yearGrafico, 12) : cuotaService.totalCuotasReporteGrafico_porProyecto(true, "S", yearGrafico, 12, projectGrafico.getId()));
        barDataSet2.setData(values2);

        data.addChartDataSet(barDataSet);
        data.addChartDataSet(barDataSet2);

        List<String> labels = new ArrayList<>();
        labels.add("Enero");
        labels.add("Febrero");
        labels.add("Marzo");
        labels.add("Abril");
        labels.add("Mayo");
        labels.add("Junio");
        labels.add("Julio");
        labels.add("Agosto");
        labels.add("Septiembre");
        labels.add("Octubre");
        labels.add("Noviembre");
        labels.add("Diciembre");
        data.setLabels(labels);
        barModel2.setData(data);

        //Options
//        BarChartOptions options = new BarChartOptions();
//        options.setMaintainAspectRatio(false);
//        CartesianScales cScales = new CartesianScales();
//        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
//        linearAxes.setStacked(true);
//        linearAxes.setOffset(true);
//        cScales.addXAxesData(linearAxes);
//        cScales.addYAxesData(linearAxes);
//        options.setScales(cScales);
//
//        Title title = new Title();
//        title.setDisplay(true);
//        title.setText("Bar Chart");
//        options.setTitle(title);
//
//        barModel2.setOptions(options);

       
    }
	
	public void iniciarLazy() {

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
               
				String manzana = "%" + manzanaFilter+ "%";
				String numLote = "%" + loteFilter+ "%";
				String cliente = "%" + surnamesFilter + "%";
				String dni = "%" + dniFilter + "%";
				
							
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
                
                if(projectFilter==null) {
                	 if(month == 0 && year == 0) {
                     	pageCuota = cuotaService.findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(estadoFilter, true, EstadoContrato.ACTIVO.getName(), manzana, numLote,cliente, dni,pageable);
                     	total = cuotaService.sumaTotal(estadoFilter, true, EstadoContrato.ACTIVO.getName(), manzana, numLote,cliente, dni);
                	 }
                     if(month != 0 && year == 0) {
                     	pageCuota = cuotaService.findByMonth(estadoFilter, true, EstadoContrato.ACTIVO.getName(), month, manzana, numLote,cliente, dni, pageable);
                     	total = cuotaService.sumaTotalMonth(estadoFilter, true, EstadoContrato.ACTIVO.getName(), month, manzana, numLote,cliente, dni);
                     }
                     if(month == 0 && year != 0) {
                     	pageCuota = cuotaService.findByYear(estadoFilter, true, EstadoContrato.ACTIVO.getName(), year, manzana, numLote,cliente,dni,pageable);
                     	total = cuotaService.sumaTotalYear(estadoFilter, true, EstadoContrato.ACTIVO.getName(), year, manzana, numLote,cliente,dni);
                     }
                     if(month != 0 && year != 0) {
                     	pageCuota = cuotaService.findByYearMonth(estadoFilter, true,EstadoContrato.ACTIVO.getName() , year ,month, manzana, numLote,cliente,dni, pageable);
                     	total = cuotaService.sumaTotalYearMonth(estadoFilter, true,EstadoContrato.ACTIVO.getName() , year ,month, manzana, numLote,cliente,dni);
                     }
                }else {
                	if(month == 0 && year == 0) {
                     	pageCuota = cuotaService.findByPagoTotalAndEstadoAndContratoEstadoAndContratoLoteProjectAndContratoLoteManzanaNameLikeAndContratoLoteNumberLoteLikeAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(estadoFilter, true, EstadoContrato.ACTIVO.getName(),projectFilter, manzana, numLote,cliente, dni, pageable);
                     	total = cuotaService.sumaTotalProyecto(estadoFilter, true, EstadoContrato.ACTIVO.getName(),projectFilter.getId(), manzana, numLote,cliente, dni);
                	}
	                if(month != 0 && year == 0) {
	                 	pageCuota = cuotaService.findByMonthProyecto(estadoFilter, true, EstadoContrato.ACTIVO.getName(), month, projectFilter.getId(), manzana, numLote,cliente, dni, pageable);
	                 	total = cuotaService.sumaTotalMonthProyecto(estadoFilter, true, EstadoContrato.ACTIVO.getName(), month, projectFilter.getId(), manzana, numLote,cliente, dni);
	                }
	                if(month == 0 && year != 0) {
	                	pageCuota = cuotaService.findByYearProyecto(estadoFilter, true, EstadoContrato.ACTIVO.getName(), year, projectFilter.getId(), manzana, numLote,cliente,dni,pageable);
	                	total = cuotaService.sumaTotalYearProyecto(estadoFilter, true, EstadoContrato.ACTIVO.getName(), year, projectFilter.getId(), manzana, numLote,cliente,dni);
	                } 
	                if(month != 0 && year != 0) {
	                	pageCuota = cuotaService.findByYearMonthProyecto(estadoFilter, true,EstadoContrato.ACTIVO.getName() , year ,month , projectFilter.getId(), manzana, numLote,cliente,dni, pageable);
	                	total = cuotaService.sumaTotalYearMonthProyecto(estadoFilter, true,EstadoContrato.ACTIVO.getName() , year ,month , projectFilter.getId(), manzana, numLote,cliente,dni);
	                }
                }
                  
                setRowCount((int) pageCuota.getTotalElements());
                return datasource = pageCuota.getContent();
            }
		};
	}
	
	
	
	public void iniciarLazyContratos() {

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
                for (Contrato cuota : datasource) {
                    if (cuota.getId() == intRowKey) {
                        return cuota;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Contrato cuota) {
                return String.valueOf(cuota.getId());
            }

			@Override
			public List<Contrato> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				
				String personaVenta = "%" + (filterBy.get("personVenta.surnames") != null ? filterBy.get("personVenta.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String proyecto = "%" + (filterBy.get("lote.project.name") != null ? filterBy.get("lote.project.name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String manzana = "%" + (filterBy.get("lote.manzana.name") != null ? filterBy.get("lote.manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String numLote = "%" + (filterBy.get("lote.numberLote") != null ? filterBy.get("lote.numberLote").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
        				
							
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
               
                Page<Contrato> pageCuota=null;
                
                if(estadoDeudaGrafico.equals("al Dia")) { 
                	pageCuota = contratoService.findByEstadoAndCuotasAtrasadasAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estadoGrafico, 0,personaVenta,proyecto,manzana, numLote, pageable);
                }else if(estadoDeudaGrafico.equals("Atrasados")) { 
                	if(busquedaCuotasAtrasadas == 0) {
                		pageCuota = contratoService.findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estadoGrafico, 0, "NO",personaVenta,proyecto,manzana, numLote, pageable) ; 
                	}else {
                		pageCuota = contratoService.findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estadoGrafico, busquedaCuotasAtrasadas, "NO", personaVenta,proyecto,manzana, numLote,pageable);
                	}
                }else {
                	if(busquedaCuotasAtrasadas == 0) {
                		pageCuota = contratoService.findByEstadoAndCuotasAtrasadasGreaterThanAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estadoGrafico, 0, "SI",personaVenta,proyecto,manzana, numLote, pageable) ; 
                	}else {
                		pageCuota = contratoService.findByEstadoAndCuotasAtrasadasAndCompromisoPagoAndPersonVentaSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(estadoGrafico, busquedaCuotasAtrasadas, "SI",personaVenta,proyecto,manzana, numLote, pageable);
                	}
                }
             	
                     	
                  
                setRowCount((int) pageCuota.getTotalElements());
                return datasource = pageCuota.getContent();
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
	
	public Converter getConversorManzana() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Manzana c = null;
                    for (Manzana si : lstManzana) {
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

	public String getEstadoFilter() {
		return estadoFilter;
	}
	public void setEstadoFilter(String estadoFilter) {
		this.estadoFilter = estadoFilter;
	}
	public LazyDataModel<Cuota> getLstCuotaLazy() {
		return lstCuotaLazy;
	}
	public void setLstCuotaLazy(LazyDataModel<Cuota> lstCuotaLazy) {
		this.lstCuotaLazy = lstCuotaLazy;
	}
	public CuotaService getCuotaService() {
		return cuotaService;
	}
	public void setCuotaService(CuotaService cuotaService) {
		this.cuotaService = cuotaService;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public String getManzanaFilter() {
		return manzanaFilter;
	}
	public void setManzanaFilter(String manzanaFilter) {
		this.manzanaFilter = manzanaFilter;
	}
	public String getLoteFilter() {
		return loteFilter;
	}
	public void setLoteFilter(String loteFilter) {
		this.loteFilter = loteFilter;
	}
	public String getSurnamesFilter() {
		return surnamesFilter;
	}
	public void setSurnamesFilter(String surnamesFilter) {
		this.surnamesFilter = surnamesFilter;
	}
	public String getDniFilter() {
		return dniFilter;
	}
	public void setDniFilter(String dniFilter) {
		this.dniFilter = dniFilter;
	}
	public int getYearGrafico() {
		return yearGrafico;
	}
	public void setYearGrafico(int yearGrafico) {
		this.yearGrafico = yearGrafico;
	}
	public Project getProjectGrafico() {
		return projectGrafico;
	}
	public void setProjectGrafico(Project projectGrafico) {
		this.projectGrafico = projectGrafico;
	}
	public BarChartModel getBarModel2() {
		return barModel2;
	}
	public void setBarModel2(BarChartModel barModel2) {
		this.barModel2 = barModel2;
	}
	public ObservacionContratoService getObservacionContratoService() {
		return observacionContratoService;
	}
	public void setObservacionContratoService(ObservacionContratoService observacionContratoService) {
		this.observacionContratoService = observacionContratoService;
	}
	public Cuota getCuotaSelected() {
		return cuotaSelected;
	}
	public void setCuotaSelected(Cuota cuotaSelected) {
		this.cuotaSelected = cuotaSelected;
	}
	public List<ObservacionContrato> getLstObservacionContrato() {
		return lstObservacionContrato;
	}
	public void setLstObservacionContrato(List<ObservacionContrato> lstObservacionContrato) {
		this.lstObservacionContrato = lstObservacionContrato;
	}
	public String getEstadoGrafico() {
		return estadoGrafico;
	}
	public void setEstadoGrafico(String estadoGrafico) {
		this.estadoGrafico = estadoGrafico;
	}
	public ContratoService getContratoService() {
		return contratoService;
	}
	public void setContratoService(ContratoService contratoService) {
		this.contratoService = contratoService;
	}
	public String getEstadoDeudaGrafico() {
		return estadoDeudaGrafico;
	}
	public void setEstadoDeudaGrafico(String estadoDeudaGrafico) {
		this.estadoDeudaGrafico = estadoDeudaGrafico;
	}

	public DonutChartModel getDonutModel() {
		return donutModel;
	}
	public void setDonutModel(DonutChartModel donutModel) {
		this.donutModel = donutModel;
	}
	public List<Contrato> getLstContratosGrafico() {
		return lstContratosGrafico;
	}
	public void setLstContratosGrafico(List<Contrato> lstContratosGrafico) {
		this.lstContratosGrafico = lstContratosGrafico;
	}
	public SimpleDateFormat getSdfQuery() {
		return sdfQuery;
	}
	public void setSdfQuery(SimpleDateFormat sdfQuery) {
		this.sdfQuery = sdfQuery;
	}
	public int getBusquedaCuotasAtrasadas() {
		return busquedaCuotasAtrasadas;
	}
	public void setBusquedaCuotasAtrasadas(int busquedaCuotasAtrasadas) {
		this.busquedaCuotasAtrasadas = busquedaCuotasAtrasadas;
	}
	public Project getProjectContratoFilter() {
		return projectContratoFilter;
	}
	public void setProjectContratoFilter(Project projectContratoFilter) {
		this.projectContratoFilter = projectContratoFilter;
	}
	public List<Manzana> getLstManzana() {
		return lstManzana;
	}
	public void setLstManzana(List<Manzana> lstManzana) {
		this.lstManzana = lstManzana;
	}
	public Manzana getManzanaContratoFilter() {
		return manzanaContratoFilter;
	}
	public void setManzanaContratoFilter(Manzana manzanaContratoFilter) {
		this.manzanaContratoFilter = manzanaContratoFilter;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public List<Usuario> getLstUsuarioCobranza() {
		return lstUsuarioCobranza;
	}
	public void setLstUsuarioCobranza(List<Usuario> lstUsuarioCobranza) {
		this.lstUsuarioCobranza = lstUsuarioCobranza;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public LazyDataModel<Contrato> getLstContratoLazy() {
		return lstContratoLazy;
	}

	public void setLstContratoLazy(LazyDataModel<Contrato> lstContratoLazy) {
		this.lstContratoLazy = lstContratoLazy;
	}

	
	
}
