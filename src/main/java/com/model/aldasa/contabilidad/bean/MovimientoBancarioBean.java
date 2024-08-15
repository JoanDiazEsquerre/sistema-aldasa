package com.model.aldasa.contabilidad.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
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
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleMovimientoBancario;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.MovimientoBancario;
import com.model.aldasa.entity.ObservacionContrato;
import com.model.aldasa.entity.Producto;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.TipoMovimiento;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.DetalleMovimientoBancarioService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.MovimientoBancarioService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.TipoMovimientoService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoContrato;
import com.model.aldasa.util.UtilXls;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import javax.inject.Named;
import javax.servlet.ServletContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ManagedBean
@ViewScoped
public class MovimientoBancarioBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{movimientoBancarioService}")
	private MovimientoBancarioService movimientoBancarioService;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{detalleMovimientoBancarioRepository}")
	private DetalleMovimientoBancarioService detalleMovimientoBancarioService;
	
	@ManagedProperty(value = "#{tipoMovimientoService}")
	private TipoMovimientoService tipoMovimientoService;
	
	
	private LazyDataModel<MovimientoBancario> lstMovimientoBancLazy;
	private LazyDataModel<DetalleMovimientoBancario> lstDetalleMovimientoBancLazy;
	
	private List<DetalleMovimientoBancario> lstDetMovBancSelected, lstDetalleImportar;
	private List<CuentaBancaria> lstCuentaBancaria, lstCuentaBancariaAll;
	private List<TipoMovimiento> lstTipoMovEntrada, lstTipoMovSalida;
	private List<Project> lstProyectos;
	private List<Manzana> lstManzanas;
	private List<Lote> lstLotes;
	
	private MovimientoBancario movimientoBancarioSelected;
	private Project proyectoMov;	
	private Manzana manzanaMov;
	private DetalleMovimientoBancario detalleMovimientoSelected;
	private CuentaBancaria cuentaBancariaFilter;
	
	private boolean estado = true;
	private String tituloDialog, busquedaPor;
	private BigDecimal saldoFinalMovimiento, dineroActualCuenta, dineroTemporalCuenta, totalAbonoIdent, totalCargoIdent, totalAbonoSinIdent, totalCargoSinIdent;
	private Date fechaDetalleFilter;
	
	private UploadedFile file;
	private StreamedContent fileFormato;
	private StreamedContent fileDes;
	
	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfQuery = new SimpleDateFormat("yyyy-mm-dd");
	
	@PostConstruct
	public void init() {
		busquedaPor = "todos";
		
		iniciarLazy();
		lstCuentaBancaria=cuentaBancariaService.findByEstadoAndSucursal(true, navegacionBean.getSucursalLogin());
		lstCuentaBancariaAll = cuentaBancariaService.findBySucursal(navegacionBean.getSucursalLogin());
		
		lstTipoMovEntrada = tipoMovimientoService.findByEstadoAndTipoOrderByNombreAsc(true, "E");
		lstTipoMovSalida = tipoMovimientoService.findByEstadoAndTipoOrderByNombreAsc(true, "S");
		lstProyectos = projectService.findByStatusOrderByNameAsc(true);
	}
	
	public void calcularMontosIdentificados() {
		totalAbonoIdent = BigDecimal.ZERO;
		totalCargoIdent = BigDecimal.ZERO;
		totalAbonoSinIdent = BigDecimal.ZERO;
		totalCargoSinIdent = BigDecimal.ZERO;
		
		if(busquedaPor.equals("abonosIdentificados")) {
			totalAbonoIdent = detalleMovimientoBancarioService.totalImporteAbonoIdentificado(movimientoBancarioSelected.getId());			
        
		}else if(busquedaPor.equals("cargosIdentificados")) {
        	totalCargoIdent = detalleMovimientoBancarioService.totalImporteCargoIdentificado(movimientoBancarioSelected.getId());			
        	
        }else if(busquedaPor.equals("abonosCargosIdentificados")) {
        	totalAbonoIdent = detalleMovimientoBancarioService.totalImporteAbonoIdentificado(movimientoBancarioSelected.getId());
        	totalCargoIdent = detalleMovimientoBancarioService.totalImporteCargoIdentificado(movimientoBancarioSelected.getId());			
   	
        } 
		
		if(busquedaPor.equals("abonosSinIdentificar")) {
        	totalAbonoSinIdent = detalleMovimientoBancarioService.totalImporteAbonoSinIdentificar(movimientoBancarioSelected.getId());
		
		}else if(busquedaPor.equals("cargosSinIdentificar")) {
	    	totalCargoSinIdent = detalleMovimientoBancarioService.totalImporteCargoSinIdentificar(movimientoBancarioSelected.getId());
	    
		}else if(busquedaPor.equals("abonosCargosSinIdentificar")) {
        	totalAbonoSinIdent = detalleMovimientoBancarioService.totalImporteAbonoSinIdentificar(movimientoBancarioSelected.getId());
	    	totalCargoSinIdent = detalleMovimientoBancarioService.totalImporteCargoSinIdentificar(movimientoBancarioSelected.getId());

        }
	}
	
	public void procesarExcel() {		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Reporte Contratos");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("FECHA OPERACIÓN");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("FECHA PROCESO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("HORA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("NUMERO OPERACIÓN");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("DESCRIPCION / REFERENCIA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("TIPO MOVIMIENTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("CARGO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("ABONO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("OBSERVACION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("LOTE-MANZANA / PROYECTO");cellSub1.setCellStyle(styleTitulo);
		
		List<DetalleMovimientoBancario> lstDetalleMovimientoBancarios = new ArrayList<>();
		
		if(busquedaPor.equals("todos")) {
			lstDetalleMovimientoBancarios = detalleMovimientoBancarioService.findByEstadoAndMovimientoBancario(true, movimientoBancarioSelected);
			
        }else if(busquedaPor.equals("abonosIdentificados")) {
    		lstDetalleMovimientoBancarios= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, "", BigDecimal.ZERO );
			
        }else if(busquedaPor.equals("cargosIdentificados")) {
        	lstDetalleMovimientoBancarios= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, "", BigDecimal.ZERO );
			
        	
        }else if(busquedaPor.equals("abonosCargosIdentificados")) {
        	lstDetalleMovimientoBancarios= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, "");
			
        }else if(busquedaPor.equals("abonosSinIdentificar")) {
        	lstDetalleMovimientoBancarios= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(movimientoBancarioSelected.getId());
        }
        else if(busquedaPor.equals("cargosSinIdentificar")) {
        	lstDetalleMovimientoBancarios= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(movimientoBancarioSelected.getId());
        }
        else if(busquedaPor.equals("abonosCargosSinIdentificar")) {
        	lstDetalleMovimientoBancarios= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(movimientoBancarioSelected.getId());
        }
		
		
		int index = 1;
		BigDecimal sumaAbono = BigDecimal.ZERO, sumaCargo = BigDecimal.ZERO;
		
		
		if (!lstDetalleMovimientoBancarios.isEmpty()){
			for(DetalleMovimientoBancario c : lstDetalleMovimientoBancarios) {				
				rowSubTitulo = sheet.createRow(index);
				cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(c.getFechaOperacion() == null ? "": sdf.format(c.getFechaOperacion()));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(c.getFechaProceso() == null ? "": sdf.format(c.getFechaProceso()));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(c.getHora() == null ? "" : c.getHora());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(c.getNumeroOperacion() == null ? "" : c.getNumeroOperacion());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(c.getDescripcion() == null ? "" : c.getDescripcion());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(c.getTipoMovimiento() == null ? "" : c.getTipoMovimiento().getNombre());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(c.getImporte().compareTo(BigDecimal.ZERO)>0 ? "" : c.getImporte()+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(c.getImporte().compareTo(BigDecimal.ZERO)<0 ? "" : c.getImporte()+"");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(c.getObservacion() == null ? "" : c.getObservacion());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(c.getLote() == null ? "" : c.getLote().getNumberLote()+"-"+c.getLote().getManzana().getName()+" / "+c.getLote().getProject().getName());cellSub1.setCellStyle(styleBorder);
				
				
				if(c.getImporte().compareTo(BigDecimal.ZERO)>0) {
					sumaAbono = sumaAbono.add(c.getImporte());
				}else {
					sumaCargo = sumaCargo.add(c.getImporte());
				}

				index++;
			}
		}
		
		rowSubTitulo = sheet.createRow(index);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(sumaCargo+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(sumaAbono+"");cellSub1.setCellStyle(styleBorder);
		
		
		for (int j = 0; j <= 9; j++) {
			sheet.autoSizeColumn(j);
			
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + "Reporte Movimientos.xlsx");
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name("Reporte Movimientos.xlsx").contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + "Reporte Movimientos.xlsx"))
					.build();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void modificarLoteDetalle() {
		
		proyectoMov = null;
		manzanaMov = null;
		lstManzanas = new ArrayList<>();
		lstLotes = new ArrayList<>();
		
		if(detalleMovimientoSelected.getLote()!=null){
		
			proyectoMov = detalleMovimientoSelected.getLote().getProject();
			listarManzanas();
		
			manzanaMov = detalleMovimientoSelected.getLote().getManzana();
			listarLotes();	
		}
		
	}
	
	public void procesarExcelFormato() {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Formato");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("FECHA OPERACION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("FECHA PROCESO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("HORA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("NUMERO OPERACION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("DESCRIPCION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("IMPORTE");cellSub1.setCellStyle(styleTitulo);
		
		
		
		
		
		for (int j = 0; j <= 5; j++) {
			sheet.autoSizeColumn(j);
			
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/formato.xlsx");
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileFormato = DefaultStreamedContent.builder().name("formato.xlsx").contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/formato.xlsx"))
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
		
	public void listarLotes() {
		if(manzanaMov != null) {
			lstLotes = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyectoMov, manzanaMov, "%%");
		}else {
			lstLotes = new ArrayList<>();
		}
	}
	
	public void listarManzanas() {
		manzanaMov=null;
		lstLotes = new ArrayList<>();
		if(proyectoMov != null) {
			lstManzanas = manzanaService.findByProject(proyectoMov.getId());
		}else {
			lstManzanas = new ArrayList<>();
		}
	}
	
	public void actualizarDetalleMovimiento(DetalleMovimientoBancario detalle) {
		detalleMovimientoBancarioService.save(detalle);
		addInfoMessage("Se actualizó correctamente el detalle."); 
	}
	
	public void actualizarLoteDetalleMovimiento() {
		detalleMovimientoBancarioService.save(detalleMovimientoSelected);
		PrimeFaces.current().executeScript("PF('loteDialog').hide()"); 
		addInfoMessage("Se actualizó el lote del detalle correctamente."); 
	}
	
	public void verDetalles() {
		saldoFinalMovimiento = detalleMovimientoBancarioService.totalImporteMovimiento(movimientoBancarioSelected.getId());
		if(saldoFinalMovimiento == null) {
			saldoFinalMovimiento = BigDecimal.ZERO;
		}
		saldoFinalMovimiento = movimientoBancarioSelected.getSaldoInicial().add(saldoFinalMovimiento);
		
		fechaDetalleFilter = null;
		busquedaPor = "todos";
		iniciarLazyDetalle();
	}
	
	public BigDecimal obtenerSaldoFinalCabecera(MovimientoBancario movimientoBancario) {
		BigDecimal saldoFinal = BigDecimal.ZERO;
		
		saldoFinal = detalleMovimientoBancarioService.totalImporteMovimiento(movimientoBancario.getId());
		if(saldoFinal == null) {
			saldoFinal = BigDecimal.ZERO;
		}
		saldoFinal = movimientoBancario.getSaldoInicial().add(saldoFinal);
		
		
		return saldoFinal;
	}
	
	public void importarDetallesMovimientos() {
		if(lstDetalleImportar.isEmpty()) {
			addErrorMessage("La lista se encuentra vacía.");
			return;
		}
		
		for(DetalleMovimientoBancario detalle : lstDetalleImportar) {
			detalleMovimientoBancarioService.save(detalle);
		}
		
		addInfoMessage("Se realizó la importacion correctamente."); 
		PrimeFaces.current().executeScript("PF('importarMovimientoDialog').hide()"); 
		
	}
	
	public void limpiarListaImportacion() {
		lstDetalleImportar = new ArrayList<>();
		
		dineroActualCuenta = detalleMovimientoBancarioService.totalImporteMovimiento(movimientoBancarioSelected.getId()).add(movimientoBancarioSelected.getSaldoInicial());
		if(dineroActualCuenta==null) {
			dineroActualCuenta = BigDecimal.ZERO;
		}
		dineroTemporalCuenta = dineroActualCuenta;
	}
	
	public void handleFileUpload() throws ParseException {
		if(file == null) {
			addErrorMessage("Seleccionar un excel para la importación.");
			return;
		}
		
		
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream); // Usamos XSSFWorkbook para archivos .xlsx
            
            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja
            DataFormatter dataFormatter = new DataFormatter();
            
            int contFilas = 0;
         // Recorrer por filas
            for (Row row : sheet) {
            	int cont= 0;
                // Recorrer por celdas de la fila
            	DetalleMovimientoBancario nuevoDetalle = new DetalleMovimientoBancario();
            	
                for (Cell cell : row) {
                	 // Procesar cada celda
                	
                	if(contFilas!=0) {
                		String cellValue =dataFormatter.formatCellValue(cell);
                    	if(cont==0 && "".equals(cellValue)) {
                    		return;
                    	}
                    	
                    	switch(cont) {
                    	  case 0:
                    		  String[] parts = cellValue.split("/");
                    		  String part1 = parts[0]; // 123 
                    		  String part2 = parts[1]; // 654321
                    		  String part3 = parts[2]; // 654321
                    		  
                    		  boolean valida = false;
                    		  if(part1.length()==1) {
                    			  part1 = "0"+part1;
                    			  valida = true;
                    		  }
                    		  if(part2.length()==1) {
                    			  part2 = "0"+part2;
                    			  valida = true;
                    		  }
                    		  if(part3.length()==2) {
                    			  part3 = "20"+part3;
                    			  valida = true;
                    		  }
                    		  
                    		  if(!valida) {
                    			  cellValue = part1+"/"+part2+"/"+part3; 
                    		  }else {
                    			  cellValue = part2+"/"+part1+"/"+part3;
                    		  }
                    		  
                    		  
                    		  
                    	    nuevoDetalle.setFechaOperacion(sdf.parse(cellValue));
                    	    break;
                    	  case 1:
                    		  if(cellValue.equals("-")) {
                    			  nuevoDetalle.setFechaProceso(null); 
                    		  }else {
                    			  String[] partsPro = cellValue.split("/");
                        		  String part1Pro = partsPro[0]; // 123 
                        		  String part2Pro = partsPro[1]; // 654321
                        		  String part3Pro = partsPro[2]; // 654321
                        		  
                        		  boolean validaPro = false;
                        		  if(part1Pro.length()==1) {
                        			  part1Pro = "0"+part1Pro;
                        			  validaPro = true;
                        		  }
                        		  if(part2Pro.length()==1) {
                        			  part2Pro = "0"+part2Pro;
                        			  validaPro = true;
                        		  }
                        		  if(part3Pro.length()==2) {
                        			  part3Pro = "20"+part3Pro;
                        			  validaPro = true;
                        		  }
                        		  
                        		  if(!validaPro) {
                        			  cellValue = part1Pro+"/"+part2Pro+"/"+part3Pro;
                        		  }else {
                        			  cellValue = part2Pro+"/"+part1Pro+"/"+part3Pro;
                        		  }
                        		  
                    			  
                    			  nuevoDetalle.setFechaProceso(sdf.parse(cellValue));
                    		  }
                    		  break;
                    	  case 2:
                    		  if(cellValue.equals("-")) {
                    			  nuevoDetalle.setHora(""); 
                    		  }else {
                    			  nuevoDetalle.setHora(cellValue);
                    		  }
                    		  
                    		  break;
                    	  case 3:
                    		  if(cellValue.equals("-")) {
                    			  nuevoDetalle.setNumeroOperacion("");
                    		  }else {
                    			  nuevoDetalle.setNumeroOperacion(cellValue); 
                    		  }
                    		  
                    		  break;
                    	  case 4:
                    		  nuevoDetalle.setDescripcion(cellValue); 
                    		  break;
                    	  case 5:
                    		  
                    		  cellValue = cellValue.replace(",", "");
                    		  cellValue = cellValue.replace(" ", "");
                    		  cellValue = cellValue.replace("/", "");
                    		  cellValue = cellValue.replace("S", "");
                    		  nuevoDetalle.setImporte(new BigDecimal(cellValue));
                    		  
                    		  dineroTemporalCuenta = dineroTemporalCuenta.add(nuevoDetalle.getImporte());
                    		  break;
                    	}
                    	
                    	cont++;
                	}
                	
                	
                }
                
                if(contFilas!=0) {
                	nuevoDetalle.setMovimientoBancario(movimientoBancarioSelected);
                    nuevoDetalle.setObservacion(""); 
                    nuevoDetalle.setEstado(true); 
                    lstDetalleImportar.add(nuevoDetalle);
                }
                
                System.out.println(); // Nueva línea para nueva fila
                contFilas++;
            }
            
            addInfoMessage("Se pueden ver todos los detalles correctamente.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public List<TipoMovimiento> listarTipoMovimiento(DetalleMovimientoBancario detalle){
		if(detalle.getImporte().compareTo(BigDecimal.ZERO)==1) { 				
			return lstTipoMovEntrada;
		}else {
			return lstTipoMovSalida;
		}
	}
	
	public void anularMovimiento() {
		movimientoBancarioSelected.setEstado(false);
		movimientoBancarioService.save(movimientoBancarioSelected);
		addInfoMessage("Se anuló correctamente el movimiento."); 
	}
	
	public void saveMovimientoBancario() {
		if(movimientoBancarioSelected.getSaldoInicial()==null) {
			addErrorMessage("Ingresar un saldo Inicial");
			return;
		}
		
		
		if(movimientoBancarioSelected.getFechaRegistro()==null) {
			movimientoBancarioSelected.setFechaRegistro(new Date());
			movimientoBancarioSelected.setUsuario(navegacionBean.getUsuarioLogin()); 
			
		}
		movimientoBancarioSelected.setEstado(true);
		movimientoBancarioService.save(movimientoBancarioSelected);
		PrimeFaces.current().executeScript("PF('movimientoDialog').hide();"); 
		addInfoMessage("Se guardó correctamente");
	}
	
	public void nuevoMovimientoBancario() {
		movimientoBancarioSelected = new MovimientoBancario();
		movimientoBancarioSelected.setAnio(sdfYear.format(new Date()));
		
		tituloDialog = "NUEVO MOVIMIENTO BANCARIO";
		
		
	}
	
	public void modificarMovimientoBancario() {
		tituloDialog = "MODIFICAR MOVIMIENTO BANCARIO";
	}
	
	public void iniciarLazy() {

		lstMovimientoBancLazy = new LazyDataModel<MovimientoBancario>() {
			private List<MovimientoBancario> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public MovimientoBancario getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (MovimientoBancario profile : datasource) {
                    if (profile.getId() == intRowKey) {
                        return profile;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(MovimientoBancario profile) {
                return String.valueOf(profile.getId());
            }

			@Override
			public List<MovimientoBancario> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String mes = "%" + (filterBy.get("mes") != null ? filterBy.get("mes").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String anio = "%" + (filterBy.get("anio") != null ? filterBy.get("anio").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
				String cuentaBanc = "%%";
				if(cuentaBancariaFilter != null) {
					cuentaBanc = "%"+cuentaBancariaFilter.getNumero()+"%";
				}

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
               
                Page<MovimientoBancario> pageProfile=null;
               
                
                pageProfile= movimientoBancarioService.findByEstadoAndMesLikeAndAnioLikeAndCuentaBancariaNumeroLikeAndCuentaBancariaSucursal(estado, mes, anio, cuentaBanc, navegacionBean.getSucursalLogin(), pageable);
                
                setRowCount((int) pageProfile.getTotalElements());
                return datasource = pageProfile.getContent();
            }
		};
	}
	
	public void iniciarLazyDetalle() {

		lstDetalleMovimientoBancLazy = new LazyDataModel<DetalleMovimientoBancario>() {
			private List<DetalleMovimientoBancario> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public DetalleMovimientoBancario getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (DetalleMovimientoBancario profile : datasource) {
                    if (profile.getId() == intRowKey) {
                        return profile;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(DetalleMovimientoBancario profile) {
                return String.valueOf(profile.getId());
            }

			@Override
			public List<DetalleMovimientoBancario> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
//				String mes = "%" + (filterBy.get("mes") != null ? filterBy.get("mes").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
//				String anio = "%" + (filterBy.get("anio") != null ? filterBy.get("anio").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				

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
               
                Page<DetalleMovimientoBancario> pageProfile=null;
               
                if(busquedaPor.equals("todos")) {
                	if(fechaDetalleFilter == null) {
                    	pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancario(true, movimientoBancarioSelected, pageable);
    				}else {
    					pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndFechaOperacion(true, movimientoBancarioSelected, fechaDetalleFilter, pageable);
    				}
                }else if(busquedaPor.equals("abonosIdentificados")) {
                	if(fechaDetalleFilter == null) {
                    	pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, "", BigDecimal.ZERO ,pageable);
    				}else {
    					pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndImporteGreaterThanAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, fechaDetalleFilter, "", BigDecimal.ZERO, pageable);
    				}
                	
                }else if(busquedaPor.equals("cargosIdentificados")) {
                	if(fechaDetalleFilter == null) {
                    	pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, "", BigDecimal.ZERO ,pageable);
    				}else {
    					pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndImporteLessThanAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, fechaDetalleFilter, "", BigDecimal.ZERO, pageable);
    				}
                	
                }else if(busquedaPor.equals("abonosCargosIdentificados")) {
                	if(fechaDetalleFilter == null) {
                    	pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndObservacionIsNotAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, "" ,pageable);
    				}else {
    					pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionIsNotAndTipoMovimientoIsNotNull(true, movimientoBancarioSelected, fechaDetalleFilter, "", pageable);
    				}
                	
                	
                	
                	
                	
                	
                	
                }else if(busquedaPor.equals("abonosSinIdentificar")) {	
                	if(fechaDetalleFilter == null) {
                    	pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(true, movimientoBancarioSelected.getId(), pageable);
    				}else {
    					pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteGreaterThanAndObservacionOrTipoMovimientoIsNull(true, movimientoBancarioSelected.getId(), sdfQuery.format(fechaDetalleFilter), pageable);
    				}
                	
                }else if(busquedaPor.equals("cargosSinIdentificar")) {
                	if(fechaDetalleFilter == null) {
                    	pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(true, movimientoBancarioSelected.getId(), pageable);
    				}else {
    					pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndFechaOperacionAndImporteLessThanAndObservacionOrTipoMovimientoIsNull(true, movimientoBancarioSelected.getId(), sdfQuery.format(fechaDetalleFilter), pageable);
    				}
                	
                }else if(busquedaPor.equals("abonosCargosSinIdentificar")) {
                	if(fechaDetalleFilter == null) {
                    	pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndObservacionOrTipoMovimientoIsNull(true, movimientoBancarioSelected.getId() ,pageable);
    				}else {
    					pageProfile= detalleMovimientoBancarioService.findByEstadoAndMovimientoBancarioAndFechaOperacionAndObservacionOrTipoMovimientoIsNull(true, movimientoBancarioSelected.getId(), sdfQuery.format(fechaDetalleFilter), pageable);
    				}
                }
                

                
                
                
                setRowCount((int) pageProfile.getTotalElements());
                return datasource = pageProfile.getContent();
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
	
	public Converter getConversorTipoMovimiento(DetalleMovimientoBancario detalleMov) {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	TipoMovimiento c = null;
                	
                	if(detalleMov.getImporte().compareTo(BigDecimal.ZERO) == 1) {
                		for (TipoMovimiento si : lstTipoMovEntrada) {
                            if (si.getId().toString().equals(value)) {
                                c = si;
                            }
                		}
                    }else {
                    	for (TipoMovimiento si : lstTipoMovSalida) {
                            if (si.getId().toString().equals(value)) {
                                c = si;
                            }
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
                    return ((TipoMovimiento) value).getId() + "";
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
                    for (Project si : lstProyectos) {
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
                    for (Manzana si : lstManzanas) {
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
                    for (Lote si : lstLotes) {
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
	
	public Converter getConversorCuentaBancariaAll() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	CuentaBancaria c = null;
                    for (CuentaBancaria si : lstCuentaBancariaAll) {
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

	public MovimientoBancarioService getMovimientoBancarioService() {
		return movimientoBancarioService;
	}
	public void setMovimientoBancarioService(MovimientoBancarioService movimientoBancarioService) {
		this.movimientoBancarioService = movimientoBancarioService;
	}
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
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
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public LazyDataModel<MovimientoBancario> getLstMovimientoBancLazy() {
		return lstMovimientoBancLazy;
	}
	public void setLstMovimientoBancLazy(LazyDataModel<MovimientoBancario> lstMovimientoBancLazy) {
		this.lstMovimientoBancLazy = lstMovimientoBancLazy;
	}
	public List<DetalleMovimientoBancario> getLstDetMovBancSelected() {
		return lstDetMovBancSelected;
	}
	public void setLstDetMovBancSelected(List<DetalleMovimientoBancario> lstDetMovBancSelected) {
		this.lstDetMovBancSelected = lstDetMovBancSelected;
	}
	public MovimientoBancario getMovimientoBancarioSelected() {
		return movimientoBancarioSelected;
	}
	public void setMovimientoBancarioSelected(MovimientoBancario movimientoBancarioSelected) {
		this.movimientoBancarioSelected = movimientoBancarioSelected;
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
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public DetalleMovimientoBancarioService getDetalleMovimientoBancarioService() {
		return detalleMovimientoBancarioService;
	}
	public void setDetalleMovimientoBancarioService(DetalleMovimientoBancarioService detalleMovimientoBancarioService) {
		this.detalleMovimientoBancarioService = detalleMovimientoBancarioService;
	}
	public SimpleDateFormat getSdfYear() {
		return sdfYear;
	}
	public void setSdfYear(SimpleDateFormat sdfYear) {
		this.sdfYear = sdfYear;
	}
	public LazyDataModel<DetalleMovimientoBancario> getLstDetalleMovimientoBancLazy() {
		return lstDetalleMovimientoBancLazy;
	}
	public void setLstDetalleMovimientoBancLazy(LazyDataModel<DetalleMovimientoBancario> lstDetalleMovimientoBancLazy) {
		this.lstDetalleMovimientoBancLazy = lstDetalleMovimientoBancLazy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public TipoMovimientoService getTipoMovimientoService() {
		return tipoMovimientoService;
	}
	public void setTipoMovimientoService(TipoMovimientoService tipoMovimientoService) {
		this.tipoMovimientoService = tipoMovimientoService;
	}
	public List<TipoMovimiento> getLstTipoMovEntrada() {
		return lstTipoMovEntrada;
	}
	public void setLstTipoMovEntrada(List<TipoMovimiento> lstTipoMovEntrada) {
		this.lstTipoMovEntrada = lstTipoMovEntrada;
	}
	public List<TipoMovimiento> getLstTipoMovSalida() {
		return lstTipoMovSalida;
	}
	public void setLstTipoMovSalida(List<TipoMovimiento> lstTipoMovSalida) {
		this.lstTipoMovSalida = lstTipoMovSalida;
	}
	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	public List<DetalleMovimientoBancario> getLstDetalleImportar() {
		return lstDetalleImportar;
	}
	public void setLstDetalleImportar(List<DetalleMovimientoBancario> lstDetalleImportar) {
		this.lstDetalleImportar = lstDetalleImportar;
	}
	public BigDecimal getSaldoFinalMovimiento() {
		return saldoFinalMovimiento;
	}
	public void setSaldoFinalMovimiento(BigDecimal saldoFinalMovimiento) {
		this.saldoFinalMovimiento = saldoFinalMovimiento;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public Project getProyectoMov() {
		return proyectoMov;
	}
	public void setProyectoMov(Project proyectoMov) {
		this.proyectoMov = proyectoMov;
	}
	public Manzana getManzanaMov() {
		return manzanaMov;
	}
	public void setManzanaMov(Manzana manzanaMov) {
		this.manzanaMov = manzanaMov;
	}
	public List<Project> getLstProyectos() {
		return lstProyectos;
	}
	public void setLstProyectos(List<Project> lstProyectos) {
		this.lstProyectos = lstProyectos;
	}
	public List<Manzana> getLstManzanas() {
		return lstManzanas;
	}
	public void setLstManzanas(List<Manzana> lstManzanas) {
		this.lstManzanas = lstManzanas;
	}
	public List<Lote> getLstLotes() {
		return lstLotes;
	}
	public void setLstLotes(List<Lote> lstLotes) {
		this.lstLotes = lstLotes;
	}
	public DetalleMovimientoBancario getDetalleMovimientoSelected() {
		return detalleMovimientoSelected;
	}
	public void setDetalleMovimientoSelected(DetalleMovimientoBancario detalleMovimientoSelected) {
		this.detalleMovimientoSelected = detalleMovimientoSelected;
	}
	public StreamedContent getFileFormato() {
		return fileFormato;
	}
	public void setFileFormato(StreamedContent fileFormato) {
		this.fileFormato = fileFormato;
	}
	public BigDecimal getDineroActualCuenta() {
		return dineroActualCuenta;
	}
	public void setDineroActualCuenta(BigDecimal dineroActualCuenta) {
		this.dineroActualCuenta = dineroActualCuenta;
	}
	public BigDecimal getDineroTemporalCuenta() {
		return dineroTemporalCuenta;
	}
	public void setDineroTemporalCuenta(BigDecimal dineroTemporalCuenta) {
		this.dineroTemporalCuenta = dineroTemporalCuenta;
	}
	public List<CuentaBancaria> getLstCuentaBancariaAll() {
		return lstCuentaBancariaAll;
	}
	public void setLstCuentaBancariaAll(List<CuentaBancaria> lstCuentaBancariaAll) {
		this.lstCuentaBancariaAll = lstCuentaBancariaAll;
	}
	public CuentaBancaria getCuentaBancariaFilter() {
		return cuentaBancariaFilter;
	}
	public void setCuentaBancariaFilter(CuentaBancaria cuentaBancariaFilter) {
		this.cuentaBancariaFilter = cuentaBancariaFilter;
	}
	public Date getFechaDetalleFilter() {
		return fechaDetalleFilter;
	}
	public void setFechaDetalleFilter(Date fechaDetalleFilter) {
		this.fechaDetalleFilter = fechaDetalleFilter;
	}
	public String getBusquedaPor() {
		return busquedaPor;
	}
	public void setBusquedaPor(String busquedaPor) {
		this.busquedaPor = busquedaPor;
	}
	public StreamedContent getFileDes() {
		return fileDes;
	}
	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	public BigDecimal getTotalAbonoIdent() {
		return totalAbonoIdent;
	}
	public void setTotalAbonoIdent(BigDecimal totalAbonoIdent) {
		this.totalAbonoIdent = totalAbonoIdent;
	}
	public BigDecimal getTotalCargoIdent() {
		return totalCargoIdent;
	}
	public void setTotalCargoIdent(BigDecimal totalCargoIdent) {
		this.totalCargoIdent = totalCargoIdent;
	}
	public BigDecimal getTotalAbonoSinIdent() {
		return totalAbonoSinIdent;
	}
	public void setTotalAbonoSinIdent(BigDecimal totalAbonoSinIdent) {
		this.totalAbonoSinIdent = totalAbonoSinIdent;
	}
	public BigDecimal getTotalCargoSinIdent() {
		return totalCargoSinIdent;
	}
	public void setTotalCargoSinIdent(BigDecimal totalCargoSinIdent) {
		this.totalCargoSinIdent = totalCargoSinIdent;
	}
	
	
	
}
