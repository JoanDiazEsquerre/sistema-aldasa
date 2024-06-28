package com.model.aldasa.rrhh.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.model.aldasa.entity.DetalleCaja;
import com.model.aldasa.entity.DetallePlanilla;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.FondoPension;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Planilla;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.AsistenciaService;
import com.model.aldasa.service.DetallePlanillaService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.FondoPensionService;
import com.model.aldasa.service.PlanillaService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class PlanillaBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{empleadoService}")
	private PlanillaService planillaService;
	
	@ManagedProperty(value = "#{sucursalService}")
	private SucursalService sucursalService;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{detallePlanillaService}")
	private DetallePlanillaService detallePlanillaService;
	
	@ManagedProperty(value = "#{asistenciaService}")
	private AsistenciaService asistenciaService;
	
	@ManagedProperty(value = "#{fondoPensionService}")
	private FondoPensionService fondoPensionService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	private LazyDataModel<Planilla> lstPlanillaLazy;
	
	private StreamedContent fileDes;
	
	private Planilla planillaSelected;
	private Sucursal sucursal;
	private Planilla planillaNew;
	private Empleado empleado;
	
	private List<Sucursal> lstSucursal;
	private List<Empleado> lstEmpleadoCombo;
	private List<DetallePlanilla> lstDetallePlanillaTemp;
	private List<DetallePlanilla> lstDetallePlanillaSelected;

	private List<Empleado> lstEmpleadoTemp;
	private List<FondoPension> lstFondoPension;
	
	private SelectItem[] cboMes;
    private SelectItem[] cboTipoPlanilla;
    
    private String   periodo, mes;
	private boolean estado = true;
	private boolean bloqueo, tipoPlanilla;
	
	private String nombreArchivo = "Detalle Planilla.xlsx";
	
	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfDay = new SimpleDateFormat("MM");
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-aaaa");
	
	@PostConstruct
	public void init() {
		tipoPlanilla = true;
		lstEmpleadoCombo = empleadoService.findByEstado(true);
		lstSucursal =sucursalService.findByEstado(true);
		lstFondoPension = fondoPensionService.findByEstado(true);
		sucursal = lstSucursal.get(0);
		crearFiltroMes();
		crearFiltroTipoPlanilla();
		
		bloquearPantalla();
		
		iniciarLazy();
	}
	
	public void procesarExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Detalle Planilla");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
	
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellTitulo = null;
		cellTitulo = rowSubTitulo.createCell(0);cellTitulo.setCellValue("SUCURSAl");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(1);cellTitulo.setCellValue("EMPLEADO");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(2);cellTitulo.setCellValue("CARGO");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(3);cellTitulo.setCellValue("FONDO PENSIÓN");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(4);cellTitulo.setCellValue("SUELDO BASICO");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(5);cellTitulo.setCellValue("TARDANZA");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(6);cellTitulo.setCellValue("VACACIONES");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(7);cellTitulo.setCellValue("COMISIONES");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(8);cellTitulo.setCellValue("BONO");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(9);cellTitulo.setCellValue("TOTAL");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(10);cellTitulo.setCellValue("ONS SNP");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(11);cellTitulo.setCellValue("AP. OBLIG.");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(12);cellTitulo.setCellValue("SEG. INV.");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(13);cellTitulo.setCellValue("COM. VAR.");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(14);cellTitulo.setCellValue("REN. 5TA.");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(15);cellTitulo.setCellValue("DESC. MES ANT.");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(16);cellTitulo.setCellValue("FE SALUD");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(17);cellTitulo.setCellValue("PAGO VAC. TRUN.");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(18);cellTitulo.setCellValue("ADELANTO");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(19);cellTitulo.setCellValue("PRESTAMO");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(20);cellTitulo.setCellValue("TOTAL DESC.");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(21);cellTitulo.setCellValue("NETO A PAGAR");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(22);cellTitulo.setCellValue("ESSALUD");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(23);cellTitulo.setCellValue("ABONADO 30");cellTitulo.setCellStyle(styleTitulo);
		cellTitulo = rowSubTitulo.createCell(24);cellTitulo.setCellValue("ABONADO 4");cellTitulo.setCellStyle(styleTitulo);

		
		
		int index = 1;
		
	
		if (!lstDetallePlanillaSelected.isEmpty()) {
			for (DetallePlanilla detalle : lstDetallePlanillaSelected) {	
			
				Row rowDetalle = sheet.createRow(index);
				Cell cellSucursal = rowDetalle.createCell(0);cellSucursal.setCellValue(detalle.getEmpleado().getSucursal().getRazonSocial());cellSucursal.setCellStyle(styleBorder);
				Cell cellEmpleado = rowDetalle.createCell(1);cellEmpleado.setCellValue(detalle.getEmpleado().getPerson().getNames() + detalle.getEmpleado().getPerson().getSurnames());cellEmpleado.setCellStyle(styleBorder);
				Cell cellCargo= rowDetalle.createCell(2);cellCargo.setCellValue(detalle.getEmpleado().getCargo().getDescripcion());cellCargo.setCellStyle(styleBorder);
				Cell cellEgreso = rowDetalle.createCell(3);cellEgreso.setCellValue(detalle.getEmpleado().getFondoPension().getNombre());cellEgreso.setCellStyle(styleBorder);
				Cell cellSueldoBasico = rowDetalle.createCell(4);cellSueldoBasico.setCellValue(detalle.getEmpleado().getSueldoBasico() + "");cellSueldoBasico.setCellStyle(styleBorder);
				Cell cellTardanza = rowDetalle.createCell(5);cellTardanza.setCellValue(detalle.getTardanza() + "");cellTardanza.setCellStyle(styleBorder);
				Cell cellVacaciones = rowDetalle.createCell(6);cellVacaciones.setCellValue(detalle.getVacaciones() + "");cellVacaciones.setCellStyle(styleBorder);
				Cell cellComisiones = rowDetalle.createCell(7);cellComisiones.setCellValue(detalle.getComisiones() + "");cellComisiones.setCellStyle(styleBorder);
				Cell cellBono = rowDetalle.createCell(8);cellBono.setCellValue(detalle.getBono() + "");cellBono.setCellStyle(styleBorder);
				Cell cellTotal = rowDetalle.createCell(9);cellTotal.setCellValue(detalle.getTotal() + "");cellTotal.setCellStyle(styleBorder);
				Cell cellOnpSnp = rowDetalle.createCell(10);cellOnpSnp.setCellValue(detalle.getOnp()+"");cellOnpSnp.setCellStyle(styleBorder);
				Cell cellApOblig = rowDetalle.createCell(11);cellApOblig.setCellValue(detalle.getAporteObligatorio()+"");cellApOblig.setCellStyle(styleBorder);
				Cell cellSegInv = rowDetalle.createCell(12);cellSegInv.setCellValue(detalle.getPrimaSeguros()+"");cellSegInv.setCellStyle(styleBorder);
				Cell cellComVar = rowDetalle.createCell(13);cellComVar.setCellValue(detalle.getComisionVariable() +"");cellComVar.setCellStyle(styleBorder);
				Cell cellRentaQuinta = rowDetalle.createCell(14);cellRentaQuinta.setCellValue(detalle.getRentaQuinta()+"");cellRentaQuinta.setCellStyle(styleBorder);
				Cell cellDescMesAnterior = rowDetalle.createCell(15);cellDescMesAnterior.setCellValue(detalle.getDescMesAnterior()+"");cellDescMesAnterior.setCellStyle(styleBorder);
				Cell cellFeSalud = rowDetalle.createCell(16);cellFeSalud.setCellValue(detalle.getFeSalud()+"");cellFeSalud.setCellStyle(styleBorder);
				Cell cellPagoVacTrun = rowDetalle.createCell(17);cellPagoVacTrun.setCellValue(detalle.getPagoVacTrunca()+"");cellPagoVacTrun.setCellStyle(styleBorder);
				Cell cellAdelanto = rowDetalle.createCell(18);cellAdelanto.setCellValue(detalle.getAdelanto()+"");cellAdelanto.setCellStyle(styleBorder);
				Cell cellPrestamo = rowDetalle.createCell(19);cellPrestamo.setCellValue(detalle.getPrestamo()+"");cellPrestamo.setCellStyle(styleBorder);
				Cell cellTotalDesc = rowDetalle.createCell(20);cellTotalDesc.setCellValue(detalle.getTotalDescuento()+"");cellTotalDesc.setCellStyle(styleBorder);
				Cell cellNetoPagar = rowDetalle.createCell(21);cellNetoPagar.setCellValue(detalle.getNetoPagar()+"");cellNetoPagar.setCellStyle(styleBorder);
				Cell cellEsSalud = rowDetalle.createCell(22);cellEsSalud.setCellValue(detalle.getEsSalud()+"");cellEsSalud.setCellStyle(styleBorder);
				Cell cellAbonado30 = rowDetalle.createCell(23);cellAbonado30.setCellValue(detalle.getAbonado30()+"");cellAbonado30.setCellStyle(styleBorder);
				Cell cellAbonado4 = rowDetalle.createCell(24);cellAbonado4.setCellValue(detalle.getAbonado4()+"");cellAbonado4.setCellStyle(styleBorder);

				
				index++;
			}
		}
		
		for (int j = 0; j <= 24; j++) {
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
	
	public void saveDetallePlanilla() {
		planillaService.save(planillaSelected, lstDetallePlanillaSelected);
		
		addInfoMessage("Se guardó correctamente.");
	}
	
	public void listarDetallesPlanillaSelected() {
		lstDetallePlanillaSelected = detallePlanillaService.findByEstadoAndPlanilla(true, planillaSelected);
		
		
	} 
	
	public void terminarPlanilla() {
		planillaNew.setEstado(true);
		planillaNew.setTemporal(false);
		planillaService.save(planillaNew);
		bloquearPantalla();
		addInfoMessage("Se terminó la planilla correctamente.");
	}
	
	public void cancelarPlanilla() {
		planillaNew.setEstado(false);
		planillaService.save(planillaNew);
		bloquearPantalla();
		addInfoMessage("Se canceló la planilla correctamente.");
	}
	
	public void savePlanillaTemporal() {
//		planillaNew = planillaService.findByEstadoAndTemporal(true, true);
		if(planillaNew == null) {
			planillaNew = new Planilla();
			planillaNew.setSucursal(sucursal);
			planillaNew.setPeriodo(periodo);
			planillaNew.setMes(mes);
			if(tipoPlanilla) {
				planillaNew.setTipoPlanilla("DEPENDIENTE");
			}else {
				planillaNew.setTipoPlanilla("INDEPENDIENTE");
			}
			planillaNew.setEstado(true);
			planillaNew.setFechaRegistro(new Date());
			planillaNew.setUsuario(navegacionBean.getUsuarioLogin());
			planillaNew.setTemporal(true); 
			
			planillaService.save(planillaNew, lstDetallePlanillaTemp);
			bloquearPantalla();
			
		}else {
			planillaService.save(planillaNew, lstDetallePlanillaTemp);
			bloquearPantalla();
		}
		addInfoMessage("Se guardó correctamente.");
		
	}
	
	public void editarTablaTemporal(DetallePlanilla dt) {
		
		if(dt.getTardanza()==null) {
			dt.setTardanza(BigDecimal.ZERO);
		}
		
		if(dt.getVacaciones()==null) {
			dt.setVacaciones(BigDecimal.ZERO);
		}
		
		if(dt.getComisiones()==null) {
			dt.setComisiones(BigDecimal.ZERO);
		}
		
		if(dt.getBono()==null) {
			dt.setBono(BigDecimal.ZERO);
		}
		
		BigDecimal suma1 = dt.getEmpleado().getSueldoBasico().subtract(dt.getTardanza());
		BigDecimal suma2 = suma1.add(dt.getVacaciones());
		BigDecimal suma3 = suma2.add(dt.getComisiones());
		BigDecimal suma4 = suma3.add(dt.getBono());
		
		dt.setTotal(suma4);
		
		if(dt.getEmpleado().isPlanilla()) {
			if(dt.getEmpleado().getFondoPension().getNombre().equals("ONP")) {
				dt.setOnp(dt.getTotal().multiply(dt.getEmpleado().getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP))); 
			}else {
				dt.setAporteObligatorio(dt.getTotal().multiply(dt.getEmpleado().getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP)));		
				dt.setPrimaSeguros(dt.getTotal().multiply(dt.getEmpleado().getFondoPension().getPrimaSeguro().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP)));
				dt.setComisionVariable(BigDecimal.ZERO);
				
				if(dt.getEmpleado().isComisionVariable()) {
					BigDecimal calculo1 =dt.getEmpleado().getFondoPension().getComisionSobreFlujo1().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
					dt.setComisionVariable(calculo1.multiply(dt.getTotal()));
				}
			}
			
			dt.setTotalDescuento(dt.getOnp().add(dt.getAporteObligatorio()).add(dt.getPrimaSeguros()).add(dt.getComisionVariable()).add(dt.getRentaQuinta()).add(dt.getDescMesAnterior()).add(dt.getFeSalud()).add(dt.getPagoVacTrunca()).add(dt.getAdelanto()).add(dt.getPrestamo()));
			dt.setNetoPagar(dt.getTotal().subtract(dt.getTotalDescuento()));
			dt.setEsSalud(dt.getTotal().compareTo(new BigDecimal(1025)) == -1? new BigDecimal(92.25) : dt.getTotal().multiply( new BigDecimal(9).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP))); 
			dt.setAbonado30(dt.getNetoPagar());
		}
		
		addInfoMessage("Se calculó el total correctamente.");

	}
	
	
	
	public void editarTabla(DetallePlanilla dt) {
		
		if(dt.getTardanza()==null) {
			dt.setTardanza(BigDecimal.ZERO);
		}
		
		if(dt.getVacaciones()==null) {
			dt.setVacaciones(BigDecimal.ZERO);
		}
		
		if(dt.getComisiones()==null) {
			dt.setComisiones(BigDecimal.ZERO);
		}
		
		if(dt.getBono()==null) {
			dt.setBono(BigDecimal.ZERO);
		}
		
		BigDecimal suma1 = dt.getEmpleado().getSueldoBasico().subtract(dt.getTardanza());
		BigDecimal suma2 = suma1.add(dt.getVacaciones());
		BigDecimal suma3 = suma2.add(dt.getComisiones());
		BigDecimal suma4 = suma3.add(dt.getBono());
		
		dt.setTotal(suma4);
		
		if(dt.getEmpleado().isPlanilla()) {
			if(dt.getEmpleado().getFondoPension().getNombre().equals("ONP")) {
				dt.setOnp(dt.getTotal().multiply(dt.getEmpleado().getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP))); 
			}else {
				dt.setAporteObligatorio(dt.getTotal().multiply(dt.getEmpleado().getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP)));		
				dt.setPrimaSeguros(dt.getTotal().multiply(dt.getEmpleado().getFondoPension().getPrimaSeguro().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP)));
				dt.setComisionVariable(BigDecimal.ZERO);
				
				if(dt.getEmpleado().isComisionVariable()) {
					BigDecimal calculo1 =dt.getEmpleado().getFondoPension().getComisionSobreFlujo1().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
					dt.setComisionVariable(calculo1.multiply(dt.getTotal()));
				}
			}
			
			dt.setTotalDescuento(dt.getOnp().add(dt.getAporteObligatorio()).add(dt.getPrimaSeguros()).add(dt.getComisionVariable()).add(dt.getRentaQuinta()).add(dt.getDescMesAnterior()).add(dt.getFeSalud()).add(dt.getPagoVacTrunca()).add(dt.getAdelanto()).add(dt.getPrestamo()));
			dt.setNetoPagar(dt.getTotal().subtract(dt.getTotalDescuento()));
			dt.setEsSalud(dt.getTotal().compareTo(new BigDecimal(1025)) == -1? new BigDecimal(92.25) : dt.getTotal().multiply( new BigDecimal(9).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP))); 
			
			
//			dt.setAbonado30(dt.getNetoPagar());
			dt.setAbonado4(dt.getNetoPagar().subtract(dt.getAbonado30()));
		}
		
		addInfoMessage("Se calculó el total correctamente.");

	}
	
	public void editarRemuneracionMaxima(FondoPension fondoPension) {
		if(fondoPension.getRemuneracionMaxima()!=null) {
			fondoPensionService.save(fondoPension);
			
            addInfoMessage("Se cambió la remuneración máxima correctamente.");
		}
	}
	
	public void editarComisionAnualSobreSaldo(FondoPension fondoPension) {
		if(fondoPension.getComisionAnualSobreSaldo()!=null) {
			fondoPensionService.save(fondoPension);
			
            addInfoMessage("Se cambió la comisión sanual sobre saldo correctamente.");
		}
	}
	
	public void editarComisionSobreFlujo(FondoPension fondoPension) {
		if(fondoPension.getComisionSobreFlujo1()!=null) {
			fondoPensionService.save(fondoPension);
			
			for(DetallePlanilla d : lstDetallePlanillaTemp) {
            	
            	if(d.getEmpleado().getFondoPension()!=null) {
            		if(fondoPension.getNombre().equals(d.getEmpleado().getFondoPension().getNombre())) {
                		if(d.getEmpleado().isPlanilla()) {
                    		if(!d.getEmpleado().getFondoPension().getNombre().equals("ONP")) {
                    			d.setComisionVariable(BigDecimal.ZERO);
                    			
                    			if(d.getEmpleado().isComisionVariable()) {
                					BigDecimal calculo1 =fondoPension.getComisionSobreFlujo1().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
                					d.setComisionVariable(calculo1.multiply(d.getTotal()));
                				}
                    		}
                    	}
                	}
            	}	
    			d.setTotalDescuento(d.getOnp().add(d.getAporteObligatorio()).add(d.getPrimaSeguros()).add(d.getComisionVariable()).add(d.getRentaQuinta()).add(d.getDescMesAnterior()).add(d.getFeSalud()).add(d.getPagoVacTrunca()).add(d.getAdelanto()).add(d.getPrestamo()));
    			d.setNetoPagar(d.getTotal().subtract(d.getTotalDescuento()));
    			d.setAbonado30(d.getNetoPagar());
			}
			
            addInfoMessage("Se cambió la comisión sobre flujo correctamente.");
		}
	}
	
	public void editarPrimaSeguro(FondoPension fondoPension) {
		if(fondoPension.getPrimaSeguro()!=null) {
			fondoPensionService.save(fondoPension);
			
			for(DetallePlanilla d : lstDetallePlanillaTemp) {
            	
            	if(d.getEmpleado().getFondoPension()!=null) {
            		if(fondoPension.getNombre().equals(d.getEmpleado().getFondoPension().getNombre())) {
                		if(d.getEmpleado().isPlanilla()) {
                    		if(!d.getEmpleado().getFondoPension().getNombre().equals("ONP")) {
                				d.setPrimaSeguros(d.getTotal().multiply(fondoPension.getPrimaSeguro().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP)));
                    			
                    		}
                    	}
                	}
            	}	
    			d.setTotalDescuento(d.getOnp().add(d.getAporteObligatorio()).add(d.getPrimaSeguros()).add(d.getComisionVariable()).add(d.getRentaQuinta()).add(d.getDescMesAnterior()).add(d.getFeSalud()).add(d.getPagoVacTrunca()).add(d.getAdelanto()).add(d.getPrestamo()));
    			d.setNetoPagar(d.getTotal().subtract(d.getTotalDescuento()));
    			d.setAbonado30(d.getNetoPagar());
            }
			
            addInfoMessage("Se cambió la prima seguro correctamente.");
		}
	}
	
	public void editarAporteObligatorio(FondoPension fondoPension) {
		if(fondoPension.getAporteObligatorio()!=null) {
			fondoPensionService.save(fondoPension);
			
            for(DetallePlanilla d : lstDetallePlanillaTemp) {
            	
            	if(d.getEmpleado().getFondoPension()!=null) {
            		if(fondoPension.getNombre().equals(d.getEmpleado().getFondoPension().getNombre())) {
                		if(d.getEmpleado().isPlanilla()) {
                    		if(d.getEmpleado().getFondoPension().getNombre().equals("ONP")) {
                    			d.setOnp(d.getTotal().multiply(fondoPension.getAporteObligatorio().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP))); 
                    		}else {
                    			d.setAporteObligatorio(d.getTotal().multiply(fondoPension.getAporteObligatorio().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP)));		
                    			
                    		}
                    	}
                	}
            	}	
    			d.setTotalDescuento(d.getOnp().add(d.getAporteObligatorio()).add(d.getPrimaSeguros()).add(d.getComisionVariable()).add(d.getRentaQuinta()).add(d.getDescMesAnterior()).add(d.getFeSalud()).add(d.getPagoVacTrunca()).add(d.getAdelanto()).add(d.getPrestamo()));
    			d.setNetoPagar(d.getTotal().subtract(d.getTotalDescuento()));
    			d.setAbonado30(d.getNetoPagar());
            }
            addInfoMessage("Se cambió el aporte obligatorio correctamente.");
		}
	}
		
	public void editarTablaTemporalDatosPension(DetallePlanilla dt) {
				
		if(dt.getOnp()==null) {
			dt.setOnp(BigDecimal.ZERO);
		}
		
		if(dt.getAporteObligatorio()==null) {
			dt.setAporteObligatorio(BigDecimal.ZERO);
		}
		
		if(dt.getPrimaSeguros()==null) {
			dt.setPrimaSeguros(BigDecimal.ZERO);
		}
		
		if(dt.getComisionVariable()==null) {
			dt.setComisionVariable(BigDecimal.ZERO);
		}
		
		if(dt.getRentaQuinta()==null) {
			dt.setRentaQuinta(BigDecimal.ZERO);
		}
		
		if(dt.getDescMesAnterior()==null) {
			dt.setDescMesAnterior(BigDecimal.ZERO);
		}
		
		if(dt.getFeSalud()==null) {
			dt.setFeSalud(BigDecimal.ZERO);
		}
		
		if(dt.getPagoVacTrunca()==null) {
			dt.setPagoVacTrunca(BigDecimal.ZERO);
		}
		
		if(dt.getAdelanto()==null) {
			dt.setAdelanto(BigDecimal.ZERO);
		}
		
		if(dt.getPrestamo()==null) {
			dt.setPrestamo(BigDecimal.ZERO);
		}
		
		dt.setTotalDescuento(dt.getOnp().add(dt.getAporteObligatorio()).add(dt.getPrimaSeguros()).add(dt.getComisionVariable()).add(dt.getRentaQuinta()).add(dt.getDescMesAnterior()).add(dt.getFeSalud()).add(dt.getPagoVacTrunca()).add(dt.getAdelanto()).add(dt.getPrestamo()));
		dt.setNetoPagar(dt.getTotal().subtract(dt.getTotalDescuento()));
		dt.setAbonado30(dt.getNetoPagar());
		addInfoMessage("Se calculó el total correctamente.");

	}
	
	public void agregarDetalle() {
		if(empleado != null) {
			for(DetallePlanilla detalle : lstDetallePlanillaTemp) {
				if(detalle.getEmpleado().getId().equals(empleado.getId())){
					addErrorMessage("El empleado ya esta en la lista.");
					return;
				}
			}
			
			
			DetallePlanilla dt  = new DetallePlanilla();
			dt.setEmpleado(empleado);
			dt.setTardanza(BigDecimal.ZERO);
			dt.setVacaciones(BigDecimal.ZERO);
			dt.setComisiones(BigDecimal.ZERO);
			dt.setBono(BigDecimal.ZERO);
			dt.setTotal(empleado.getSueldoBasico().subtract(dt.getTardanza()).add(dt.getVacaciones()).add(dt.getComisiones()).add(dt.getBono()));
			
			dt.setOnp(BigDecimal.ZERO);
			dt.setAporteObligatorio(BigDecimal.ZERO);
			dt.setPrimaSeguros(BigDecimal.ZERO);
			dt.setComisionVariable(BigDecimal.ZERO);
			
			
			if(tipoPlanilla) {
				if(empleado.getFondoPension()!=null) {
					
					if(empleado.getFondoPension().getNombre().equals("ONP")) {
						
						dt.setOnp(dt.getTotal().multiply(empleado.getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 2 , RoundingMode.HALF_UP))); 
					}else {
						dt.setAporteObligatorio(dt.getTotal().multiply(empleado.getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 2 , RoundingMode.HALF_UP)));
						dt.setPrimaSeguros(dt.getTotal().multiply(empleado.getFondoPension().getPrimaSeguro()));
						dt.setComisionVariable(BigDecimal.ZERO);
						
					}
				}
			}
			
			dt.setRentaQuinta(BigDecimal.ZERO);
			dt.setDescMesAnterior(BigDecimal.ZERO);
			dt.setFeSalud(BigDecimal.ZERO);
			dt.setPagoVacTrunca(BigDecimal.ZERO);
			dt.setAdelanto(BigDecimal.ZERO);
			dt.setPrestamo(BigDecimal.ZERO);
			dt.setTotalDescuento(dt.getOnp().add(dt.getAporteObligatorio()).add(dt.getPrimaSeguros()).add(dt.getComisionVariable()).add(dt.getRentaQuinta()).add(dt.getDescMesAnterior()).add(dt.getFeSalud()).add(dt.getPagoVacTrunca()).add(dt.getAdelanto()).add(dt.getPrestamo()));
			
			dt.setNetoPagar(dt.getTotal().subtract(dt.getTotalDescuento()));
			dt.setEsSalud(dt.getTotal().compareTo(new BigDecimal(1025)) == -1? new BigDecimal(92.25) : dt.getTotal().multiply( new BigDecimal(9).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP))); 
			
			dt.setAbonado30(dt.getNetoPagar());
			dt.setAbonado4(BigDecimal.ZERO); 

			
			lstDetallePlanillaTemp.add(dt);
			addInfoMessage("Se agregó correctamente el empleado.");
		}
	}
		
	public void bloquearPantalla() {
		bloqueo=false;
		planillaNew = planillaService.findByEstadoAndTemporal(true, true);
		
		if(planillaNew != null) {
			periodo= planillaNew.getPeriodo();
			mes = planillaNew.getMes();
			if(planillaNew.getTipoPlanilla().equals("DEPENDIENTE")) {
				tipoPlanilla=true;
			}else {
				tipoPlanilla = false;
			}
			sucursal = planillaNew.getSucursal();
			
			lstDetallePlanillaTemp = detallePlanillaService.findByEstadoAndPlanilla(estado, planillaNew);
			
			bloqueo=true;
		}else {
			periodo= sdfYear.format(new Date());
			mes = sdfDay.format(new Date());
			tipoPlanilla = true;
			
			listarDetallePlantillaTemporal();
			
			
		}
	}
	
	public void listarDetallePlantillaTemporal() {
		lstDetallePlanillaTemp = new ArrayList<>();
		lstEmpleadoTemp = new ArrayList<>();
		
		lstEmpleadoTemp = empleadoService.findByEstadoAndPlanillaAndSucursal(true, tipoPlanilla, sucursal);
		
		
		for(Empleado e : lstEmpleadoTemp) {
			
			DetallePlanilla dt  = new DetallePlanilla();
			dt.setEmpleado(e);
			dt.setTardanza(BigDecimal.ZERO);
			
			dt.setVacaciones(BigDecimal.ZERO);
			dt.setComisiones(BigDecimal.ZERO);
			dt.setBono(BigDecimal.ZERO);
			
			dt.setOnp(BigDecimal.ZERO);
			dt.setAporteObligatorio(BigDecimal.ZERO);
			dt.setPrimaSeguros(BigDecimal.ZERO);
			
			
			dt.setComisionVariable(BigDecimal.ZERO);
			dt.setTotal(e.getSueldoBasico());
			
			if(tipoPlanilla) {
				
				
				Date fechaInicio = new Date();
				fechaInicio.setDate(1);
				fechaInicio.setMonth(Integer.parseInt(mes)-1);
				int anio = Integer.parseInt(periodo);
				
				 Calendar calendar2 = Calendar.getInstance();
			     calendar2.setTime(fechaInicio);
			     calendar2.set(Calendar.YEAR, anio);
			     fechaInicio = calendar2.getTime();
				
		        // Convierte la fecha a un objeto Calendar
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(fechaInicio);

		        // Establece la fecha al último día del mes actual
		        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		        // Convierte el Calendar de nuevo a un objeto Date
		        Date fechaFin = calendar.getTime();
		
				
				int contMinutos = 0;
		        
				Date fechaActual = fechaInicio;
		        while (!fechaActual.after(fechaFin)) {
		        	Date busquedaIni = new Date(fechaActual.getTime());
		        	busquedaIni.setSeconds(0);
		        	busquedaIni.setMinutes(0);
		        	busquedaIni.setHours(0);
		        	
		        	Date busquedaFin = new Date(fechaActual.getTime());
		        	busquedaFin.setSeconds(59);
		        	busquedaFin.setMinutes(59);
		        	busquedaFin.setHours(23);

					List<Asistencia> lstAsisEmpleado= asistenciaService.findByEmpleadoAndTipoAndHoraBetweenAndEstadoOrderByHoraAsc(e,"E" ,busquedaIni, busquedaFin, true);
					
					if(!lstAsisEmpleado.isEmpty()) {
						contMinutos = contMinutos + minutosTardanza(lstAsisEmpleado.get(0));
					}
		            
	
		            // Avanzar a la siguiente fecha
		            long tiempoEnMilisegundos = fechaActual.getTime();
		            tiempoEnMilisegundos += 24 * 60 * 60 * 1000; // Agregar un día en milisegundos
		            fechaActual.setTime(tiempoEnMilisegundos);
		        }
		        
		        if(contMinutos!=0) {
		        	BigDecimal sueldoDia = e.getSueldoBasico().divide(new BigDecimal(30), 2 , RoundingMode.HALF_UP);
		        	BigDecimal sueldoHora = sueldoDia.divide(new BigDecimal(8), 2 , RoundingMode.HALF_UP);
		        	BigDecimal sueldoMinuto = sueldoHora.divide(new BigDecimal(60), 2 , RoundingMode.HALF_UP);
		        	BigDecimal descuento = sueldoMinuto.multiply(new BigDecimal(contMinutos));
		        	dt.setTardanza(descuento);
		        }
		        
		        
		        BigDecimal suma1 = e.getSueldoBasico().subtract(dt.getTardanza());
				BigDecimal suma2 = suma1.add(dt.getVacaciones());
				BigDecimal suma3 = suma2.add(dt.getComisiones());
				BigDecimal suma4 = suma3.add(dt.getBono());
				
				
				dt.setTotal(suma4);
		        
		        
		        if(e.getFondoPension()!=null) {
					if(e.getFondoPension().getNombre().equals("ONP")) {
						dt.setOnp(dt.getTotal().multiply(e.getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP))); 
					}else {
						dt.setAporteObligatorio(dt.getTotal().multiply(e.getFondoPension().getAporteObligatorio().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP)));
						dt.setPrimaSeguros(dt.getTotal().multiply(e.getFondoPension().getPrimaSeguro().divide(new BigDecimal(100), 4 , RoundingMode.HALF_UP)));
						dt.setComisionVariable(BigDecimal.ZERO);
						
						if(e.isComisionVariable()) {
							BigDecimal calculo1 =e.getFondoPension().getComisionSobreFlujo1().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
							dt.setComisionVariable(calculo1.multiply(dt.getTotal()));
						}
					}
				}
		        			
			}
			
			
			
			dt.setRentaQuinta(BigDecimal.ZERO);
			dt.setDescMesAnterior(BigDecimal.ZERO);
			dt.setFeSalud(BigDecimal.ZERO);
			dt.setPagoVacTrunca(BigDecimal.ZERO);
			dt.setAdelanto(BigDecimal.ZERO);
			dt.setPrestamo(BigDecimal.ZERO);
			dt.setTotalDescuento(dt.getOnp().add(dt.getAporteObligatorio()).add(dt.getPrimaSeguros()).add(dt.getComisionVariable()).add(dt.getRentaQuinta()).add(dt.getDescMesAnterior()).add(dt.getFeSalud()).add(dt.getPagoVacTrunca()).add(dt.getAdelanto()).add(dt.getPrestamo()));
			
			dt.setNetoPagar(dt.getTotal().subtract(dt.getTotalDescuento()));
			dt.setEsSalud(dt.getTotal().compareTo(new BigDecimal(1025)) == -1? new BigDecimal(92.25) : dt.getTotal().multiply( new BigDecimal(9).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))); 
			
			dt.setAbonado30(dt.getNetoPagar());
			dt.setAbonado4(BigDecimal.ZERO); 

			dt.setEstado(true);
			
			lstDetallePlanillaTemp.add(dt);
			
			
		}
		
	}
	
	public int minutosTardanza(Asistencia asist) {

		int horaEntrada1 = 8;
		
		if(asist.getHora().getDay()==6) {
			horaEntrada1 = 9; 
		}
		
		int horaAlmuerzo = 13;
		int horaEntrada2 = 15;
		int minutosDiferencia = 0;

		if (asist.getTipo().equals("E")) {

			if (asist.getHora().getHours() >= horaEntrada1) {
				int horasDiferencia = asist.getHora().getHours() - horaEntrada1;
				minutosDiferencia = asist.getHora().getMinutes() + (horasDiferencia * 60) - 10;
				if (minutosDiferencia <= 0) {
					minutosDiferencia = 0;
				}
			}

			if (asist.getHora().getHours() >= horaAlmuerzo) {
				minutosDiferencia = 0;
			}

			if (asist.getHora().getHours() >= horaEntrada2) {
				int horasDiferencia = asist.getHora().getHours() - horaEntrada2;
				minutosDiferencia = asist.getHora().getMinutes() + (horasDiferencia * 60);
			}

		}
		return minutosDiferencia;
	}
	
	public void verDetalles() {
		System.out.println("aaaaaaaaaaaaaa");
	}
	
	private void crearFiltroMes() {
        cboMes = new SelectItem[13];
        cboMes[0] = new SelectItem("", "Todos");
        cboMes[1] = new SelectItem("01", "Enero");
        cboMes[2] = new SelectItem("02", "Febrero");
        cboMes[3] = new SelectItem("03", "Marzo");
        cboMes[4] = new SelectItem("04", "Abril");
        cboMes[5] = new SelectItem("05", "Mayo");
        cboMes[6] = new SelectItem("06", "Junio");
        cboMes[7] = new SelectItem("07", "Julio");
        cboMes[8] = new SelectItem("08", "Agosto");
        cboMes[9] = new SelectItem("09", "Septiembre");
        cboMes[10] = new SelectItem("10", "Octubre");
        cboMes[11] = new SelectItem("11", "Noviembre");
        cboMes[12] = new SelectItem("12", "Diciembre");
    }

    private void crearFiltroTipoPlanilla() {
        cboTipoPlanilla = new SelectItem[3];
        cboTipoPlanilla[0] = new SelectItem("", "[-Todos-]");
        cboTipoPlanilla[1] = new SelectItem("DEPENDIENTE", "DEPENDIENTE");
        cboTipoPlanilla[2] = new SelectItem("INDEPENDIENTE", "INDEPENDIENTE");
    }

	
	public void iniciarLazy() {

		lstPlanillaLazy = new LazyDataModel<Planilla>() {
			private List<Planilla> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Planilla getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Planilla empleado : datasource) {
                    if (empleado.getId() == intRowKey) {
                        return empleado;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Planilla empleado) {
                return String.valueOf(empleado.getId());
            }

			@Override
			public List<Planilla> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String names = "%" + (filterBy.get("person.surnames") != null ? filterBy.get("person.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";

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
               
                Page<Planilla> pagePlanilla=null;
               
                
                pagePlanilla= planillaService.findByEstadoAndTemporal(estado, false, pageable);
                
                setRowCount((int) pagePlanilla.getTotalElements());
                return datasource = pagePlanilla.getContent();
            }
		};
	}
	
	public List<Empleado> completeEmpleado(String query) {
        List<Empleado> lista = new ArrayList<>();
        for (Empleado c : getLstEmpleadoCombo()) {
            if (c.getPerson().getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getPerson().getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }	

	public Converter getConversorSucursal() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Sucursal c = null;
                    for (Sucursal si : lstSucursal) {
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
                    return ((Sucursal) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorEmpleado() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Empleado c = null;
                    for (Empleado si : lstEmpleadoCombo) {
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
                    return ((Empleado) value).getId() + "";
                }
            }
        };
    }
	

	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public PlanillaService getPlanillaService() {
		return planillaService;
	}
	public void setPlanillaService(PlanillaService planillaService) {
		this.planillaService = planillaService;
	}
	public LazyDataModel<Planilla> getLstPlanillaLazy() {
		return lstPlanillaLazy;
	}
	public void setLstPlanillaLazy(LazyDataModel<Planilla> lstPlanillaLazy) {
		this.lstPlanillaLazy = lstPlanillaLazy;
	}
	public Planilla getPlanillaSelected() {
		return planillaSelected;
	}
	public void setPlanillaSelected(Planilla planillaSelected) {
		this.planillaSelected = planillaSelected;
	}
	public SelectItem[] getCboMes() {
		return cboMes;
	}
	public void setCboMes(SelectItem[] cboMes) {
		this.cboMes = cboMes;
	}
	public SelectItem[] getCboTipoPlanilla() {
		return cboTipoPlanilla;
	}
	public void setCboTipoPlanilla(SelectItem[] cboTipoPlanilla) {
		this.cboTipoPlanilla = cboTipoPlanilla;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public List<Sucursal> getLstSucursal() {
		return lstSucursal;
	}
	public void setLstSucursal(List<Sucursal> lstSucursal) {
		this.lstSucursal = lstSucursal;
	}
	public List<Empleado> getLstEmpleadoTemp() {
		return lstEmpleadoTemp;
	}
	public void setLstEmpleadoTemp(List<Empleado> lstEmpleadoTemp) {
		this.lstEmpleadoTemp = lstEmpleadoTemp;
	}
	public boolean isTipoPlanilla() {
		return tipoPlanilla;
	}
	public void setTipoPlanilla(boolean tipoPlanilla) {
		this.tipoPlanilla = tipoPlanilla;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public SimpleDateFormat getSdfYear() {
		return sdfYear;
	}
	public void setSdfYear(SimpleDateFormat sdfYear) {
		this.sdfYear = sdfYear;
	}
	public SimpleDateFormat getSdfDay() {
		return sdfDay;
	}
	public void setSdfDay(SimpleDateFormat sdfDay) {
		this.sdfDay = sdfDay;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Planilla getPlanillaNew() {
		return planillaNew;
	}
	public void setPlanillaNew(Planilla planillaNew) {
		this.planillaNew = planillaNew;
	}
	public boolean isBloqueo() {
		return bloqueo;
	}
	public void setBloqueo(boolean bloqueo) {
		this.bloqueo = bloqueo;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public List<Empleado> getLstEmpleadoCombo() {
		return lstEmpleadoCombo;
	}
	public void setLstEmpleadoCombo(List<Empleado> lstEmpleadoCombo) {
		this.lstEmpleadoCombo = lstEmpleadoCombo;
	}
	public List<DetallePlanilla> getLstDetallePlanillaTemp() {
		return lstDetallePlanillaTemp;
	}
	public void setLstDetallePlanillaTemp(List<DetallePlanilla> lstDetallePlanillaTemp) {
		this.lstDetallePlanillaTemp = lstDetallePlanillaTemp;
	}
	public DetallePlanillaService getDetallePlanillaService() {
		return detallePlanillaService;
	}
	public void setDetallePlanillaService(DetallePlanillaService detallePlanillaService) {
		this.detallePlanillaService = detallePlanillaService;
	}
	public AsistenciaService getAsistenciaService() {
		return asistenciaService;
	}
	public void setAsistenciaService(AsistenciaService asistenciaService) {
		this.asistenciaService = asistenciaService;
	}
	public FondoPensionService getFondoPensionService() {
		return fondoPensionService;
	}
	public void setFondoPensionService(FondoPensionService fondoPensionService) {
		this.fondoPensionService = fondoPensionService;
	}
	public List<FondoPension> getLstFondoPension() {
		return lstFondoPension;
	}
	public void setLstFondoPension(List<FondoPension> lstFondoPension) {
		this.lstFondoPension = lstFondoPension;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public List<DetallePlanilla> getLstDetallePlanillaSelected() {
		return lstDetallePlanillaSelected;
	}
	public void setLstDetallePlanillaSelected(List<DetallePlanilla> lstDetallePlanillaSelected) {
		this.lstDetallePlanillaSelected = lstDetallePlanillaSelected;
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
	
	
	
}
