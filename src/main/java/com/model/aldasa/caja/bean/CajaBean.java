package com.model.aldasa.caja.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleCaja;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.CajaService;
import com.model.aldasa.service.DetalleCajaService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class CajaBean extends BaseBean {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{cajaService}")
	private CajaService cajaService;
	
	@ManagedProperty(value = "#{detalleCajaService}")
	private DetalleCajaService detalleCajaService;
	
	
	private LazyDataModel<Caja> lstCajaLazy;
	
	private List<DetalleCaja> lstDetalleCajaSelected, lstDetalleCajaSelectedDescarga;
	
	private Caja cajaSelected;
	private DetalleCaja detalleCajaSelected;
	private DetalleCaja newDetalleCajaSelected;
	private Date fechaDescarga;
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	private StreamedContent fileDes;
	private String nombreArchivo = "Detalle Caja.xlsx";

	
	@PostConstruct
	public void init() {
		iniciarLazy();
		fechaDescarga = new Date();
	}
	
	public void editarCorrelativo(DetalleCaja detalle) {
		if(detalle.getCorrelativo()!=null) {
			detalleCajaService.save(detalle);
			
            addInfoMessage("Se cambió el correlativo correctamente.");
		}
	}
	
	public void editarSustentoContable(DetalleCaja detalle) {
		if(detalle.getSustentoContable()!=null) {
			detalleCajaService.save(detalle);
			
            addInfoMessage("Se cambió el sustento contable correctamente.");
		}
	}
	
	public void editarDescripcion(DetalleCaja detalle) {
		if(detalle.getDescripcion()!=null) {
			detalleCajaService.save(detalle);
			
            addInfoMessage("Se cambió la descripción correctamente.");
		}
	}
	
	public void deleteDetalleCaja() {
			newDetalleCajaSelected.setEstado(false);
			detalleCajaService.save(newDetalleCajaSelected);
			
			
			listarDetallesCajaSelected();
			
			cajaService.save(cajaSelected, lstDetalleCajaSelected);
			addInfoMessage("Detalle Eliminado.");
	
	}
	
	public void procesarExcelReporteContable() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Detalle Caja");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
	
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
		Cell cellSubEmpleado = rowSubTitulo.createCell(1);cellSubEmpleado.setCellValue("NÚMERO DE COMPROBANTE");cellSubEmpleado.setCellStyle(styleTitulo);
		Cell cellSubEntrada1 = rowSubTitulo.createCell(2);cellSubEntrada1.setCellValue("INGRESO");cellSubEntrada1.setCellStyle(styleTitulo);
		Cell cellSubSalida1 = rowSubTitulo.createCell(3);cellSubSalida1.setCellValue("EGRESO");cellSubSalida1.setCellStyle(styleTitulo);
		Cell cellSubEntrada2 = rowSubTitulo.createCell(4);cellSubEntrada2.setCellValue("SALDO");cellSubEntrada2.setCellStyle(styleTitulo);
		
		int index = 2;
		
		BigDecimal saldoAnterior = cajaSelected.getMontoInicioEfectivo();
		Row rowDetail = sheet.createRow(1);
		Cell cellfecha = rowDetail.createCell(0);cellfecha.setCellValue("");cellfecha.setCellStyle(styleTitulo);
		Cell celldesc = rowDetail.createCell(1);celldesc.setCellValue("SALDO ANTERIOR");celldesc.setCellStyle(styleTitulo);
		Cell cellI0 = rowDetail.createCell(2);cellI0.setCellValue("");cellI0.setCellStyle(styleTitulo);
		Cell cellE0 = rowDetail.createCell(3);cellE0.setCellValue("");cellE0.setCellStyle(styleTitulo);
		Cell cellmonto = rowDetail.createCell(4);cellmonto.setCellValue(saldoAnterior +"");cellmonto.setCellStyle(styleTitulo);
		
		if (!lstDetalleCajaSelectedDescarga.isEmpty()) {
			for (int i = 0; i< lstDetalleCajaSelectedDescarga.size();i++) {	
				DetalleCaja detalle = lstDetalleCajaSelectedDescarga.get(i);
				
				Row rowDetalle = sheet.createRow(index);
				Cell cellfechah = rowDetalle.createCell(0);cellfechah.setCellValue(sdf.format(detalle.getFecha()));cellfechah.setCellStyle(styleBorder);
				Cell cellDesc = rowDetalle.createCell(1);cellDesc.setCellValue(detalle.getSustentoContable()+" / "+detalle.getCorrelativo());cellDesc.setCellStyle(styleBorder);
				Cell cellIngreso = rowDetalle.createCell(2);cellIngreso.setCellValue(detalle.getTipoMovimiento().equals("Ingreso") ? detalle.getMonto()+"" : "");cellIngreso.setCellStyle(styleBorder);
				Cell cellEgreso = rowDetalle.createCell(3);cellEgreso.setCellValue(detalle.getTipoMovimiento().equals("Egreso") ? detalle.getMonto()+"" : "");cellEgreso.setCellStyle(styleBorder);
				
				if(detalle.getTipoMovimiento().equals("Ingreso")) {
					saldoAnterior = saldoAnterior.add(detalle.getMonto());
					Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior +"");cellE1.setCellStyle(styleBorder);
				}else {
					saldoAnterior = saldoAnterior.subtract(detalle.getMonto());
					Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior+"");cellE1.setCellStyle(styleBorder);
				}
				index++;
			}
		}
		
		Row rowDetalle = sheet.createRow(index);
		Cell cellfechah = rowDetalle.createCell(0);cellfechah.setCellValue(sdf.format(lstDetalleCajaSelectedDescarga.get(lstDetalleCajaSelected.size()-1).getFecha()));cellfechah.setCellStyle(styleTitulo);
		Cell cellDesc = rowDetalle.createCell(1);cellDesc.setCellValue("SALDO FINAL");cellDesc.setCellStyle(styleTitulo);
		Cell cellIngreso = rowDetalle.createCell(2);cellIngreso.setCellValue("");cellIngreso.setCellStyle(styleTitulo);
		Cell cellEgreso = rowDetalle.createCell(3);cellEgreso.setCellValue("");cellEgreso.setCellStyle(styleTitulo);
		Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior+"");cellE1.setCellStyle(styleTitulo);
		
		for (int j = 0; j <= 4; j++) {
			sheet.autoSizeColumn(j);
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
					.build();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void procesarExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Detalle Caja");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
	
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
		Cell cellSubEmpleado = rowSubTitulo.createCell(1);cellSubEmpleado.setCellValue("DESCRIPCION");cellSubEmpleado.setCellStyle(styleTitulo);
		Cell cellSubEntrada1 = rowSubTitulo.createCell(2);cellSubEntrada1.setCellValue("INGRESO");cellSubEntrada1.setCellStyle(styleTitulo);
		Cell cellSubSalida1 = rowSubTitulo.createCell(3);cellSubSalida1.setCellValue("EGRESO");cellSubSalida1.setCellStyle(styleTitulo);
		Cell cellSubEntrada2 = rowSubTitulo.createCell(4);cellSubEntrada2.setCellValue("SALDO");cellSubEntrada2.setCellStyle(styleTitulo);
		
		int index = 2;
		
		BigDecimal saldoAnterior = cajaSelected.getMontoInicioEfectivo();
		Row rowDetail = sheet.createRow(1);
		Cell cellfecha = rowDetail.createCell(0);cellfecha.setCellValue("");cellfecha.setCellStyle(styleTitulo);
		Cell celldesc = rowDetail.createCell(1);celldesc.setCellValue("SALDO ANTERIOR");celldesc.setCellStyle(styleTitulo);
		Cell cellI0 = rowDetail.createCell(2);cellI0.setCellValue("");cellI0.setCellStyle(styleTitulo);
		Cell cellE0 = rowDetail.createCell(3);cellE0.setCellValue("");cellE0.setCellStyle(styleTitulo);
		Cell cellmonto = rowDetail.createCell(4);cellmonto.setCellValue(saldoAnterior +"");cellmonto.setCellStyle(styleTitulo);
		
		if (!lstDetalleCajaSelectedDescarga.isEmpty()) {
			for (int i = 0; i< lstDetalleCajaSelectedDescarga.size();i++) {	
				DetalleCaja detalle = lstDetalleCajaSelectedDescarga.get(i);
				
				if(i!=0) {
					String fechaAnterior = sdf.format(lstDetalleCajaSelectedDescarga.get(i-1).getFecha());
					String fechaActual = sdf.format(lstDetalleCajaSelectedDescarga.get(i).getFecha());
					
					if(!fechaAnterior.equals(fechaActual)) {
						Row rowDetalle = sheet.createRow(index);
						Cell cellfechah = rowDetalle.createCell(0);cellfechah.setCellValue(fechaAnterior);cellfechah.setCellStyle(styleTitulo);
						Cell cellDesc = rowDetalle.createCell(1);cellDesc.setCellValue("SALDO FINAL");cellDesc.setCellStyle(styleTitulo);
						Cell cellIngreso = rowDetalle.createCell(2);cellIngreso.setCellValue("");cellIngreso.setCellStyle(styleTitulo);
						Cell cellEgreso = rowDetalle.createCell(3);cellEgreso.setCellValue("");cellEgreso.setCellStyle(styleTitulo);
						Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior+"");cellE1.setCellStyle(styleTitulo);
						index++;
					}
				}
				
				Row rowDetalle = sheet.createRow(index);
				Cell cellfechah = rowDetalle.createCell(0);cellfechah.setCellValue(sdf.format(detalle.getFecha()));cellfechah.setCellStyle(styleBorder);
				Cell cellDesc = rowDetalle.createCell(1);cellDesc.setCellValue( detalle.getSustentoContable().concat(!detalle.getSustentoContable().equals("") ? " " : "")+ detalle.getCorrelativo().concat(!detalle.getCorrelativo().equals("") ? " / " : "")  + detalle.getDescripcion());cellDesc.setCellStyle(styleBorder);
				Cell cellIngreso = rowDetalle.createCell(2);cellIngreso.setCellValue(detalle.getTipoMovimiento().equals("Ingreso") ? detalle.getMonto()+"" : "");cellIngreso.setCellStyle(styleBorder);
				Cell cellEgreso = rowDetalle.createCell(3);cellEgreso.setCellValue(detalle.getTipoMovimiento().equals("Egreso") ? detalle.getMonto()+"" : "");cellEgreso.setCellStyle(styleBorder);
				
				if(detalle.getTipoMovimiento().equals("Ingreso")) {
					saldoAnterior = saldoAnterior.add(detalle.getMonto());
					Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior +"");cellE1.setCellStyle(styleBorder);
				}else {
					saldoAnterior = saldoAnterior.subtract(detalle.getMonto());
					Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior+"");cellE1.setCellStyle(styleBorder);
				}
				index++;
			}
		}
		
		Row rowDetalle = sheet.createRow(index);
		Cell cellfechah = rowDetalle.createCell(0);cellfechah.setCellValue(sdf.format(lstDetalleCajaSelectedDescarga.get(lstDetalleCajaSelected.size()-1).getFecha()));cellfechah.setCellStyle(styleTitulo);
		Cell cellDesc = rowDetalle.createCell(1);cellDesc.setCellValue("SALDO FINAL");cellDesc.setCellStyle(styleTitulo);
		Cell cellIngreso = rowDetalle.createCell(2);cellIngreso.setCellValue("");cellIngreso.setCellStyle(styleTitulo);
		Cell cellEgreso = rowDetalle.createCell(3);cellEgreso.setCellValue("");cellEgreso.setCellStyle(styleTitulo);
		Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior+"");cellE1.setCellStyle(styleTitulo);
		
		for (int j = 0; j <= 4; j++) {
			sheet.autoSizeColumn(j);
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
					.build();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void procesarExcelPorDia() {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Detalle Caja");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
	
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
		Cell cellSubEmpleado = rowSubTitulo.createCell(1);cellSubEmpleado.setCellValue("DESCRIPCION");cellSubEmpleado.setCellStyle(styleTitulo);
		Cell cellSubEntrada1 = rowSubTitulo.createCell(2);cellSubEntrada1.setCellValue("INGRESO");cellSubEntrada1.setCellStyle(styleTitulo);
		Cell cellSubSalida1 = rowSubTitulo.createCell(3);cellSubSalida1.setCellValue("EGRESO");cellSubSalida1.setCellStyle(styleTitulo);
		Cell cellSubEntrada2 = rowSubTitulo.createCell(4);cellSubEntrada2.setCellValue("SALDO");cellSubEntrada2.setCellStyle(styleTitulo);
		
		int index = 2;
		
		BigDecimal saldoAnterior = cajaSelected.getMontoInicioEfectivo();
		Row rowDetail = sheet.createRow(1);
		Cell cellfecha = rowDetail.createCell(0);cellfecha.setCellValue(sdf.format(cajaSelected.getFecha()));cellfecha.setCellStyle(styleTitulo);
		Cell celldesc = rowDetail.createCell(1);celldesc.setCellValue("SALDO ANTERIOR");celldesc.setCellStyle(styleTitulo);
		Cell cellI0 = rowDetail.createCell(2);cellI0.setCellValue("");cellI0.setCellStyle(styleTitulo);
		Cell cellE0 = rowDetail.createCell(3);cellE0.setCellValue("");cellE0.setCellStyle(styleTitulo);
		Cell cellmonto = rowDetail.createCell(4);cellmonto.setCellValue(saldoAnterior +"");cellmonto.setCellStyle(styleTitulo);
		
		if (!lstDetalleCajaSelectedDescarga.isEmpty()) {
			for (int i = 0; i< lstDetalleCajaSelectedDescarga.size();i++) {	
				DetalleCaja detalle = lstDetalleCajaSelectedDescarga.get(i);
				
				String fechaDesc = sdf.format(fechaDescarga);
				String fechaRecorrido = sdf.format(detalle.getFecha());
				
				
				if(!fechaDesc.equals(fechaRecorrido)){
					if(detalle.getTipoMovimiento().equals("Ingreso")) {
						saldoAnterior = saldoAnterior.add(detalle.getMonto());
					}else {
						saldoAnterior = saldoAnterior.subtract(detalle.getMonto());
					}
				}else {
					if(i!=0) {
						cellfecha.setCellValue(sdf.format(lstDetalleCajaSelectedDescarga.get(i-1).getFecha()));
						cellmonto.setCellValue(saldoAnterior+"");
					}
					
					break;
				}
				
			}
			String capturaFecha = "";
			
			for (int i = 0; i< lstDetalleCajaSelectedDescarga.size();i++) {	
				DetalleCaja detalle = lstDetalleCajaSelectedDescarga.get(i);
				capturaFecha=sdf.format(detalle.getFecha());
				
				String fechaDesc = sdf.format(fechaDescarga);
				String fechaRecorrido = sdf.format(detalle.getFecha());
				
				
				if(fechaDesc.equals(fechaRecorrido)){
					Row rowDetalle = sheet.createRow(index);
					Cell cellfechah = rowDetalle.createCell(0);cellfechah.setCellValue(sdf.format(detalle.getFecha()));cellfechah.setCellStyle(styleBorder);
					Cell cellDesc = rowDetalle.createCell(1);cellDesc.setCellValue( detalle.getSustentoContable().concat(!detalle.getSustentoContable().equals("") ? " " : "")+ detalle.getCorrelativo().concat(!detalle.getCorrelativo().equals("") ? " / " : "")  + detalle.getDescripcion());cellDesc.setCellStyle(styleBorder);
					Cell cellIngreso = rowDetalle.createCell(2);cellIngreso.setCellValue(detalle.getTipoMovimiento().equals("Ingreso") ? detalle.getMonto()+"" : "");cellIngreso.setCellStyle(styleBorder);
					Cell cellEgreso = rowDetalle.createCell(3);cellEgreso.setCellValue(detalle.getTipoMovimiento().equals("Egreso") ? detalle.getMonto()+"" : "");cellEgreso.setCellStyle(styleBorder);
					
					if(detalle.getTipoMovimiento().equals("Ingreso")) {
						saldoAnterior = saldoAnterior.add(detalle.getMonto());
						Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior +"");cellE1.setCellStyle(styleBorder);
					}else {
						saldoAnterior = saldoAnterior.subtract(detalle.getMonto());
						Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior+"");cellE1.setCellStyle(styleBorder);
					}
					index++;
				}
			}
			
			Row rowDetalle = sheet.createRow(index);
			Cell cellfechah = rowDetalle.createCell(0);cellfechah.setCellValue(capturaFecha);cellfechah.setCellStyle(styleTitulo);
			Cell cellDesc = rowDetalle.createCell(1);cellDesc.setCellValue("SALDO FINAL");cellDesc.setCellStyle(styleTitulo);
			Cell cellIngreso = rowDetalle.createCell(2);cellIngreso.setCellValue("");cellIngreso.setCellStyle(styleTitulo);
			Cell cellEgreso = rowDetalle.createCell(3);cellEgreso.setCellValue("");cellEgreso.setCellStyle(styleTitulo);
			Cell cellE1 = rowDetalle.createCell(4);cellE1.setCellValue(saldoAnterior+"");cellE1.setCellStyle(styleTitulo);
			
		}
		
		
//		
		for (int j = 0; j <= 4; j++) {
			sheet.autoSizeColumn(j);
		}
		try {
			ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/" + nombreArchivo);
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			fileDes = DefaultStreamedContent.builder().name(nombreArchivo).contentType("aplication/xls")
					.stream(() -> FacesContext.getCurrentInstance().getExternalContext()
							.getResourceAsStream("/WEB-INF/fileAttachments/" + nombreArchivo))
					.build();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	public void validaCerrarCaja() {
		if(cajaSelected.getEstado().equals("Cerrada")) {
			addErrorMessage("Ésta caja está cerrada.");
			return;
		}
		PrimeFaces.current().executeScript("PF('cerrarCaja').show();");
	}
	
	public void cerrarCaja() {
		BigDecimal totalEfectivo = cajaSelected.getMontoInicioEfectivo();
		List<DetalleCaja> lstdetalle = detalleCajaService.findByCajaAndEstadoOrderByFechaDesc(cajaSelected, true);
		for(DetalleCaja detalle: lstdetalle) {
			if(detalle.getOrigen().equals("Efectivo")) {
				if(detalle.getTipoMovimiento().equals("Ingreso")) {
					totalEfectivo = totalEfectivo.add(detalle.getMonto());
				}else {
					totalEfectivo = totalEfectivo.subtract(detalle.getMonto());
				}
			}
			
		}
		BigDecimal totalPos = cajaSelected.getMontoInicioPos();
		lstdetalle = detalleCajaService.findByCajaAndEstadoOrderByFechaDesc(cajaSelected, true);
		for(DetalleCaja detalle: lstdetalle) {
			if(detalle.getOrigen().equals("POS")) {
				if(detalle.getTipoMovimiento().equals("Ingreso")) {
					totalPos = totalPos.add(detalle.getMonto());
				}else {
					totalPos = totalPos.subtract(detalle.getMonto());
				}
			}
			
		}
		
		cajaSelected.setMontoFinalEfectivo(totalEfectivo);
		cajaSelected.setMontoFinalPos(totalPos);
		cajaSelected.setEstado("Cerrada");
		cajaService.save(cajaSelected);
		addInfoMessage("Caja cerrada correctamente.");
	}
	
	
	public BigDecimal calcularFinalPos(Caja caja) {
		BigDecimal total = caja.getMontoInicioPos();
		List<DetalleCaja> lstdetalle = detalleCajaService.findByCajaAndEstadoOrderByFechaDesc(caja, true);
		for(DetalleCaja detalle: lstdetalle) {
			if(detalle.getOrigen().equals("POS")) {
				if(detalle.getTipoMovimiento().equals("Ingreso")) {
					total = total.add(detalle.getMonto());
				}else {
					total = total.subtract(detalle.getMonto());
				}
			}
		}
		return total;
	}
	
	public void saveDetalleCaja() {
		if(detalleCajaSelected.getDescripcion().equals("")) {
			addErrorMessage("Debe ingresar una descripcion.");
			return;
		}
		
		if(detalleCajaSelected.getMonto()==null) {
			addErrorMessage("Debe ingresar un monto");
			return;
		}
		
		
		detalleCajaSelected.setCaja(cajaSelected);
		detalleCajaSelected.setFecha(new Date());
		detalleCajaSelected.setEstado(true);

		detalleCajaService.save(detalleCajaSelected);
		
		
		listarDetallesCajaSelected();
		
		cajaService.save(cajaSelected, lstDetalleCajaSelected);
		addInfoMessage("Se guardó el movimiento de caja correctamente."); 
		
	}
	
	public void listarDetallesCajaSelected() {
		lstDetalleCajaSelected = detalleCajaService.findByCajaAndEstadoOrderByFechaDesc(cajaSelected, true);
		lstDetalleCajaSelectedDescarga = detalleCajaService.findByCajaAndEstadoOrderByFechaAsc(cajaSelected, true);
		detalleCajaSelected = new DetalleCaja();
		detalleCajaSelected.setTipoMovimiento("Ingreso");
		detalleCajaSelected.setOrigen("Efectivo");
		
	} 
	
	public void saveCaja() {
		if(cajaSelected.getFecha()==null) {
			addErrorMessage("Debe seleccionar una fecha.");{
				return;
			}
		}else {
			List<Caja> lstCaja = cajaService.findBySucursalAndFecha(navegacionBean.getSucursalLogin(), cajaSelected.getFecha());
			if(!lstCaja.isEmpty()) {
				addErrorMessage("Ya se resgistró una caja con la misma fecha.");{
					return;
				}
			}
		}
		
		if(cajaSelected.getMontoInicioEfectivo()==null) {
			addErrorMessage("Debe ingresar un monto inicial de efectivo.");{
				return;
			}
		}
		
		if(cajaSelected.getMontoInicioPos()==null) {
			addErrorMessage("Debe ingresar un monto inicial del POS.");{
				return;
			}
		}
		
		cajaSelected.setMontoFinalEfectivo(BigDecimal.ZERO);
		cajaSelected.setMontoFinalPos(BigDecimal.ZERO);
		cajaSelected.setUsuario(navegacionBean.getUsuarioLogin());
		cajaSelected.setEstado("Abierta");
		cajaSelected.setSucursal(navegacionBean.getSucursalLogin());
		
		Caja caja =cajaService.save(cajaSelected);
		if(caja!=null) {
			addInfoMessage("Se aperturó correctamente la caja.");
			PrimeFaces.current().executeScript("PF('cajaDialog').hide();");
		}else {
			addErrorMessage("No se puedo guardar la caja.");
		}

	
	}
	
	public void  aperturarCaja() {
		List<Caja> lstCaja = cajaService.findBySucursalAndEstado(navegacionBean.getSucursalLogin(), "Abierta");
		if(!lstCaja.isEmpty()) { 
			addErrorMessage("Existe una caja abierta.");
			return;
		}
		
		Caja ultimaCaja = cajaService.findFirstBySucursalAndEstadoOrderByIdDesc(navegacionBean.getSucursalLogin(), "Cerrada");
		
		cajaSelected = new Caja();
		cajaSelected.setFecha(new Date());
		if(ultimaCaja!=null) {
			cajaSelected.setMontoInicioEfectivo(ultimaCaja.getMontoFinalEfectivo());
			cajaSelected.setMontoInicioPos(ultimaCaja.getMontoFinalPos());
		}
		
		
		PrimeFaces.current().executeScript("PF('cajaDialog').show();");

	}
	
	public void iniciarLazy() {

		lstCajaLazy = new LazyDataModel<Caja>() {
			private List<Caja> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Caja getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Caja caja : datasource) {
                    if (caja.getId() == intRowKey) {
                        return caja;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Caja caja) {
                return String.valueOf(caja.getId());
            }

			@Override
			public List<Caja> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
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

            
                
                Page<Caja> pageEmpleado= cajaService.findBySucursalOrderByIdDesc(navegacionBean.getSucursalLogin(), pageable);
                
                
                setRowCount((int) pageEmpleado.getTotalElements());
                return datasource = pageEmpleado.getContent();
            }
		};
	}
	
	public String convertirHora(Date hora) {
		String a="";
		if(hora!=null) {
			a = sdf.format(hora);

		}
		return a;
	}
	
	public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            addInfoMessage("Hora guarda");
        }
    }
	
	public void editarFecha(DetalleCaja d) {
		if(d.getFecha()!=null) {
			detalleCajaService.save(d);
            addInfoMessage("Se cambió la fecha correctamente.");
		}
	}
	
	public String convertirHoraFull(Date hora) {
		String a="";
		if(hora!=null) {
			a = sdfFull.format(hora);

		}
		return a;
	}

	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public CajaService getCajaService() {
		return cajaService;
	}
	public void setCajaService(CajaService cajaService) {
		this.cajaService = cajaService;
	}
	public DetalleCajaService getDetalleCajaService() {
		return detalleCajaService;
	}
	public void setDetalleCajaService(DetalleCajaService detalleCajaService) {
		this.detalleCajaService = detalleCajaService;
	}
	public LazyDataModel<Caja> getLstCajaLazy() {
		return lstCajaLazy;
	}
	public void setLstCajaLazy(LazyDataModel<Caja> lstCajaLazy) {
		this.lstCajaLazy = lstCajaLazy;
	}
	public Caja getCajaSelected() {
		return cajaSelected;
	}
	public void setCajaSelected(Caja cajaSelected) {
		this.cajaSelected = cajaSelected;
	}
	public List<DetalleCaja> getLstDetalleCajaSelected() {
		return lstDetalleCajaSelected;
	}
	public void setLstDetalleCajaSelected(List<DetalleCaja> lstDetalleCajaSelected) {
		this.lstDetalleCajaSelected = lstDetalleCajaSelected;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public DetalleCaja getDetalleCajaSelected() {
		return detalleCajaSelected;
	}
	public void setDetalleCajaSelected(DetalleCaja detalleCajaSelected) {
		this.detalleCajaSelected = detalleCajaSelected;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
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
	public DetalleCaja getNewDetalleCajaSelected() {
		return newDetalleCajaSelected;
	}
	public void setNewDetalleCajaSelected(DetalleCaja newDetalleCajaSelected) {
		this.newDetalleCajaSelected = newDetalleCajaSelected;
	}
	public Date getFechaDescarga() {
		return fechaDescarga;
	}
	public void setFechaDescarga(Date fechaDescarga) {
		this.fechaDescarga = fechaDescarga;
	}
	public List<DetalleCaja> getLstDetalleCajaSelectedDescarga() {
		return lstDetalleCajaSelectedDescarga;
	}
	public void setLstDetalleCajaSelectedDescarga(List<DetalleCaja> lstDetalleCajaSelectedDescarga) {
		this.lstDetalleCajaSelectedDescarga = lstDetalleCajaSelectedDescarga;
	}

	
}
