package com.model.aldasa.general.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
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
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jdt.internal.compiler.lookup.MostSpecificExceptionMethodBinding;
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

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Semana;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.AreaService;
import com.model.aldasa.service.AsistenciaService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.SemanaService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoProspeccion;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class ReporteAsistenciaBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;

	@ManagedProperty(value = "#{asistenciaService}")
	private AsistenciaService asistenciaService;
	
	@ManagedProperty(value = "#{areaService}")
	private AreaService areaService;
	
	@ManagedProperty(value = "#{semanaService}")
	private SemanaService semanaService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;


	private LazyDataModel<Asistencia> lstAsistenciaLazy;
	private LazyDataModel<Empleado> lstEmpeladoLazy;


	private List<Empleado> lstEmpleado;
	private List<Area> lstArea;

	private Empleado empleadoSelected;
	private Empleado empleadoBusqueda;
	private Area areaSelected;
	private Asistencia asistenciaSelected;
	private Semana semanaSelected;


//	private Empleado empleadoDialog;
//	private String tipoDialog;
//	private Date horaDialog;

	private String tipo;
	private String tituloDialog = "";
	private String nombreArchivo = "Reporte de Asistencia.xlsx";
	private Date fechaIni, fechaFin;
	private boolean mostrarBoton = false;
//	private Integer idSelected;
	private boolean estado = true;

	private StreamedContent fileDes;

	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");

	@PostConstruct
	public void init() {
		obtenerSemanaActual();
		lstEmpleado = empleadoService.findByEstadoOrderByPersonSurnamesAsc(true);
		fechaIni = new Date();
		fechaFin = new Date();
		tipo = "";
		iniciarLazy();
		lstArea=areaService.findByEstadoOrderByNombreAsc(true);
		iniciarEmpleadoLazy();
	}
	
	public String fechaTexto(Empleado empleado,String tipo, int sumaDia) {
		String fecha = "-";
		if(sumaDia==0) {
			Date fechaIni = sumarDiasAFecha(semanaSelected.getFechaIni(), -1);
			fechaIni = sumarDiasAFecha(fechaIni, 1);
			fechaIni.setHours(0);
			fechaIni.setMinutes(0);
			fechaIni.setSeconds(0);
			
			Date fechaFin = sumarDiasAFecha(semanaSelected.getFechaIni(), -1);
			fechaFin = sumarDiasAFecha(fechaIni, 1);
			fechaFin.setHours(23);
			fechaFin.setMinutes(59);
			fechaFin.setSeconds(59);
			
			List<Asistencia> lstAsist = new ArrayList<>();
			if(tipo.equals("E")) {
				lstAsist = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc("%"+empleado.getPerson().getDni()+"%", "%E%", fechaIni, fechaFin, true);
				if(!lstAsist.isEmpty()) {
					fecha=sdfTime.format(lstAsist.get(0).getHora());
				}
			}else {
				lstAsist = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc("%"+empleado.getPerson().getDni()+"%", "%S%", fechaIni, fechaFin, true);
				if(!lstAsist.isEmpty()) {
					fecha=sdfTime.format(lstAsist.get(0).getHora());
				}
			}

		}else {
			Date fechaIni = sumarDiasAFecha(semanaSelected.getFechaIni(), sumaDia);
			fechaIni.setHours(0);
			fechaIni.setMinutes(0);
			fechaIni.setSeconds(0);
			
			Date fechaFin =sumarDiasAFecha(semanaSelected.getFechaIni(), sumaDia);
			fechaFin.setHours(23);
			fechaFin.setMinutes(59);
			fechaFin.setSeconds(59);
			
			List<Asistencia> lstAsist = new ArrayList<>();
			if(tipo.equals("E")) {
				lstAsist = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc("%"+empleado.getPerson().getDni()+"%", "%E%", fechaIni, fechaFin, true);
				if(!lstAsist.isEmpty()) {
					fecha=sdfTime.format(lstAsist.get(0).getHora());
				}
			}else {
				lstAsist = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc("%"+empleado.getPerson().getDni()+"%", "%S%", fechaIni, fechaFin, true);
				if(!lstAsist.isEmpty()) {
					fecha=sdfTime.format(lstAsist.get(0).getHora());
				}
			}
		}
	
		return fecha;
	}
	
	public void semanaAnterior() {
		Integer idSemana = semanaSelected.getId();
		
		if(idSemana>=2) {
			Optional<Semana> sem = semanaService.findById(idSemana-1);
			semanaSelected=sem.get();
		}
	}
	
	public void semanaSiguiente() {
		Integer idSemana = semanaSelected.getId();
		
		Optional<Semana> sem = semanaService.findById(idSemana+1);
		semanaSelected=sem.get();
	}
	
	public void obtenerSemanaActual() {
		
		List<Semana> lstSemana = semanaService.findByFechaIniLessThanEqualOrderByFechaIniDesc(new Date());
		if(!lstSemana.isEmpty()) {
			semanaSelected = lstSemana.get(0);
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

	public void mostrarBotonAutogenerar() {
		mostrarBoton = false;
		if (empleadoSelected != null) {
			mostrarBoton = true;
		}
	}

	public void autogenerarRegistroMes() throws ParseException {
		if (fechaFin.before(fechaIni)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"La fecha fin debe ser mayor o igual que la fecha inicio");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		} else {
			String fechaIniTexto = sdf.format(fechaIni);
			String fechaFinTexto = sdf.format(fechaFin);
			Date fechaI = sdf.parse(fechaIniTexto);
			Date fechaF = sdf.parse(fechaFinTexto);

			while (fechaI.getTime() <= fechaF.getTime()) {
				System.out.println(sdf.format(fechaI));
				
				if(fechaI.getDay()==6) {
					Date fechaIni = sdf.parse(sdf.format(fechaI)); 

					Date fechafin = fechaI;
					fechafin.setHours(23);
					fechafin.setMinutes(59);
					fechafin.setSeconds(59);
					List<Asistencia> lstAsistencia = asistenciaService.findByEmpleadoAndHoraBetweenAndEstadoOrderByHoraAsc(empleadoSelected, fechaIni, fechafin, true);

					if (lstAsistencia.isEmpty()) {
						Date hora1 = fechaIni;
						hora1.setHours(9);
						hora1.setMinutes(0);
						hora1.setSeconds(0);
						registroAutomatico(empleadoSelected, "E", hora1);
						Date hora2 = fechaIni;
						hora2.setHours(13);
						hora2.setMinutes(0);
						hora2.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora2);
						Date hora3 = fechaIni;
						hora3.setHours(15);
						hora3.setMinutes(0);
						hora3.setSeconds(0);
						registroAutomatico(empleadoSelected, "E", hora3);
						Date hora4 = fechaIni;
						hora4.setHours(19);
						hora4.setMinutes(0); 
						hora4.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora4);

					} else if (lstAsistencia.size() == 1) {

						Date hora2 = fechaIni;
						hora2.setHours(13);
						hora2.setMinutes(0);
						hora2.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora2);
						Date hora3 = fechaIni;
						hora3.setHours(15);
						hora3.setMinutes(0);
						hora3.setSeconds(0);
						registroAutomatico(empleadoSelected, "E", hora3);
						Date hora4 = fechaIni;
						hora4.setHours(19);
						hora4.setMinutes(0);
						hora4.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora4);

					} else if (lstAsistencia.size() == 2) {
						for (Asistencia asistencia : lstAsistencia) {
							if (asistencia.getTipo().equals("S")) {
								asistencia.getHora().setHours(13);
								asistencia.getHora().setMinutes(0);
								asistencia.getHora().setSeconds(0);
								asistenciaService.save(asistencia);
							}

						}
						Date hora3 = fechaIni;
						hora3.setHours(15);
						hora3.setMinutes(0);
						hora3.setSeconds(0);
						registroAutomatico(empleadoSelected, "E", hora3);
						Date hora4 = fechaIni;
						hora4.setHours(19);
						hora4.setMinutes(0);
						hora4.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora4);
					} else if (lstAsistencia.size() == 3) {
						int contador = 0;
						for (Asistencia asistencia : lstAsistencia) {
							if (contador == 2) {
								asistencia.getHora().setHours(15);
								asistencia.getHora().setMinutes(0);
								asistencia.getHora().setSeconds(0);
								asistenciaService.save(asistencia);
							} else {
								if (asistencia.getTipo().equals("S")) {
									asistencia.getHora().setHours(13);
									asistencia.getHora().setMinutes(0);
									asistencia.getHora().setSeconds(0);
									asistenciaService.save(asistencia);
								}
							}

							contador = contador + 1;

						}
						Date hora4 = fechaIni;
						hora4.setHours(19);
						hora4.setMinutes(0);
						hora4.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora4);
					} else {
						int contador = 0;
						for (Asistencia asistencia : lstAsistencia) {
							if (contador >= 2) {
								if (asistencia.getTipo().equals("E")) {
									asistencia.getHora().setHours(15);
									asistencia.getHora().setMinutes(0);
									asistencia.getHora().setSeconds(0);
									asistenciaService.save(asistencia);
								} else {
									asistencia.getHora().setHours(19);
									asistencia.getHora().setMinutes(0);
									asistencia.getHora().setSeconds(0);
									asistenciaService.save(asistencia);
								}

							} else {
								if (asistencia.getTipo().equals("S")) {
									asistencia.getHora().setHours(13);
									asistencia.getHora().setMinutes(0);
									asistencia.getHora().setSeconds(0);
									asistenciaService.save(asistencia);
								}
							}
							contador = contador + 1;
						}
					}
				}
				
				if(fechaI.getDay()!=0 && fechaI.getDay()!=6) { // si es 0, entonces es domingo, para que haga el registro, debe ser diferente de 0
					Date fechaIni = sdf.parse(sdf.format(fechaI)); 

					Date fechafin = fechaI;
					fechafin.setHours(23);
					fechafin.setMinutes(59);
					fechafin.setSeconds(59);
					List<Asistencia> lstAsistencia = asistenciaService.findByEmpleadoAndHoraBetweenAndEstadoOrderByHoraAsc(empleadoSelected, fechaIni, fechafin, true);

					if (lstAsistencia.isEmpty()) {
						Date hora1 = fechaIni;
						hora1.setHours(8);
						hora1.setMinutes(0);
						hora1.setSeconds(0);
						registroAutomatico(empleadoSelected, "E", hora1);
						Date hora2 = fechaIni;
						hora2.setHours(13);
						hora2.setMinutes(0);
						hora2.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora2);
						Date hora3 = fechaIni;
						hora3.setHours(15);
						hora3.setMinutes(0);
						hora3.setSeconds(0);
						registroAutomatico(empleadoSelected, "E", hora3);
						Date hora4 = fechaIni;
						hora4.setHours(18);
						hora4.setMinutes(0);
						hora4.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora4);

					} else if (lstAsistencia.size() == 1) {

						Date hora2 = fechaIni;
						hora2.setHours(13);
						hora2.setMinutes(0);
						hora2.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora2);
						Date hora3 = fechaIni;
						hora3.setHours(15);
						hora3.setMinutes(0);
						hora3.setSeconds(0);
						registroAutomatico(empleadoSelected, "E", hora3);
						Date hora4 = fechaIni;
						hora4.setHours(18);
						hora4.setMinutes(0);
						hora4.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora4);

					} else if (lstAsistencia.size() == 2) {
						for (Asistencia asistencia : lstAsistencia) {
							if (asistencia.getTipo().equals("S")) {
								asistencia.getHora().setHours(13);
								asistencia.getHora().setMinutes(0);
								asistencia.getHora().setSeconds(0);
								asistenciaService.save(asistencia);
							}

						}
						Date hora3 = fechaIni;
						hora3.setHours(15);
						hora3.setMinutes(0);
						hora3.setSeconds(0);
						registroAutomatico(empleadoSelected, "E", hora3);
						Date hora4 = fechaIni;
						hora4.setHours(18);
						hora4.setMinutes(0);
						hora4.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora4);
					} else if (lstAsistencia.size() == 3) {
						int contador = 0;
						for (Asistencia asistencia : lstAsistencia) {
							if (contador == 2) {
								asistencia.getHora().setHours(15);
								asistencia.getHora().setMinutes(0);
								asistencia.getHora().setSeconds(0);
								asistenciaService.save(asistencia);
							} else {
								if (asistencia.getTipo().equals("S")) {
									asistencia.getHora().setHours(13);
									asistencia.getHora().setMinutes(0);
									asistencia.getHora().setSeconds(0);
									asistenciaService.save(asistencia);
								}
							}

							contador = contador + 1;

						}
						Date hora4 = fechaIni;
						hora4.setHours(18);
						hora4.setMinutes(0);
						hora4.setSeconds(0);
						registroAutomatico(empleadoSelected, "S", hora4);
					} else {
						int contador = 0;
						for (Asistencia asistencia : lstAsistencia) {
							if (contador >= 2) {
								if (asistencia.getTipo().equals("E")) {
									asistencia.getHora().setHours(15);
									asistencia.getHora().setMinutes(0);
									asistencia.getHora().setSeconds(0);
									asistenciaService.save(asistencia);
								} else {
									asistencia.getHora().setHours(18);
									asistencia.getHora().setMinutes(0);
									asistencia.getHora().setSeconds(0);
									asistenciaService.save(asistencia);
								}

							} else {
								if (asistencia.getTipo().equals("S")) {
									asistencia.getHora().setHours(13);
									asistencia.getHora().setMinutes(0);
									asistencia.getHora().setSeconds(0);
									asistenciaService.save(asistencia);
								}
							}
							contador = contador + 1;
						}
					}
					
				}
			
				fechaI = sumarDiasAFecha(fechaI, 1);
			}
		}
	}

	public void registroAutomatico(Empleado empleado, String tipo, Date hora) {
		Asistencia asistencia = new Asistencia();
		asistencia.setEmpleado(empleado);
		asistencia.setTipo(tipo);
		asistencia.setHora(hora);
		asistencia.setUserCrea(navegacionBean.getUsuarioLogin());
		asistencia.setFechaCrea(new Date());
		asistencia.setEstado(true);
		asistenciaService.save(asistencia);
	}

	public void updateAsistencia() {
		tituloDialog = "MODIFICAR ASISTENCIA";
		
//		idSelected = asistenciaSelected.getId();
//		empleadoDialog = asistenciaSelected.getEmpleado();
//		tipoDialog = asistenciaSelected.getTipo();
//		horaDialog = asistenciaSelected.getHora();
	}

	public void newAsistencia() {
		tituloDialog = "NUEVA ASISTENCIA";
		asistenciaSelected = new Asistencia();
//		idSelected = null;
//		empleadoDialog = null;
//		tipoDialog = "";
//		horaDialog = null;
	}
	
	public void eliminarAsistencia() {
		
		asistenciaSelected.setEstado(false);
		asistenciaSelected.setUserModifica(navegacionBean.getUsuarioLogin());
		asistenciaSelected.setFechaModifica(new Date());
		asistenciaService.save(asistenciaSelected);
		
		addInfoMessage("Eliminado correctamente.");
		return ;  
		
	}

	public void saveAsistencia() {
		if (asistenciaSelected.getEmpleado() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar empleado."));
			return;
		}
		if (asistenciaSelected.getTipo().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar tipo."));
			return;
		}
		if (asistenciaSelected.getHora() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar fecha."));
			return;
		}
		
		if(tituloDialog.equals("MODIFICAR ASISTENCIA")) {
			asistenciaSelected.setUserModifica(navegacionBean.getUsuarioLogin());
			asistenciaSelected.setFechaModifica(new Date());
		}else{
			asistenciaSelected.setUserCrea(navegacionBean.getUsuarioLogin());
			asistenciaSelected.setFechaCrea(new Date());
		}
		
		asistenciaSelected.setEstado(true);
		
		Asistencia asistencia = asistenciaService.save(asistenciaSelected);

		
		if (asistencia == null) {
			addErrorMessage("No se pudo guardar.");
		} else {
			addInfoMessage("Se guardó correctamente.");
			PrimeFaces.current().executeScript("PF('asistenciaNewDialog').hide();"); 
		}

	}

	public void procesarExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Asistencia");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		// CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');

//	        Row rowTituloHoja = sheet.createRow(0);
//	        Cell cellTituloHoja = rowTituloHoja.createCell(0);
//	        cellTituloHoja.setCellValue("Reporte de Acciones");
//	        cellTituloHoja.setCellStyle(styleBorder);
//	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo

		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
		Cell cellSubEmpleado = rowSubTitulo.createCell(1);cellSubEmpleado.setCellValue("EMPLEADO");cellSubEmpleado.setCellStyle(styleTitulo);
		Cell cellSubEntrada1 = rowSubTitulo.createCell(2);cellSubEntrada1.setCellValue("ENTRADA");cellSubEntrada1.setCellStyle(styleTitulo);
		Cell cellSubSalida1 = rowSubTitulo.createCell(3);cellSubSalida1.setCellValue("SALIDA");cellSubSalida1.setCellStyle(styleTitulo);
		Cell cellSubEntrada2 = rowSubTitulo.createCell(4);cellSubEntrada2.setCellValue("ENTRADA");cellSubEntrada2.setCellStyle(styleTitulo);
		Cell cellSubSalida2 = rowSubTitulo.createCell(5);cellSubSalida2.setCellValue("SALIDA");cellSubSalida2.setCellStyle(styleTitulo);
		Cell cellSubArea = rowSubTitulo.createCell(6);cellSubArea.setCellValue("ÁREA");cellSubArea.setCellStyle(styleTitulo);
		Cell cellSubMinTard = rowSubTitulo.createCell(7);cellSubMinTard.setCellValue("MINUTOS DE TARDANZA");cellSubMinTard.setCellStyle(styleTitulo);
//		Cell cellUserCrea = rowSubTitulo.createCell(8);cellUserCrea.setCellValue("USER CREA");cellUserCrea.setCellStyle(styleTitulo);
//		Cell cellFechaCrea = rowSubTitulo.createCell(9);cellFechaCrea.setCellValue("FECHA CREA");cellFechaCrea.setCellStyle(styleTitulo);
//		Cell cellUserModifica = rowSubTitulo.createCell(10);cellUserModifica.setCellValue("USER MODIFICA");cellUserModifica.setCellStyle(styleTitulo);
//		Cell cellFechaModifica = rowSubTitulo.createCell(11);cellFechaModifica.setCellValue("FECHA MODIFICA");cellFechaModifica.setCellStyle(styleTitulo);

		if (fechaFin.before(fechaIni)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"La fecha fin debe ser mayor o igual que la fecha inicio");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		} else {
			Date fecha1 = fechaIni;
			Date fecha2 = fechaFin;

			int index = 1;
			while (fecha1.getTime() <= fecha2.getTime()) {

				List<Empleado> lstempleados = new ArrayList<>();
				if (empleadoSelected != null) {
					Empleado emp = empleadoService.findByPersonId(empleadoSelected.getPerson().getId());
					lstempleados.add(emp);
				} else {
					lstempleados = empleadoService.findByEstadoAndExternoOrderByPersonSurnamesAsc(true, false);
				}

				if (!lstempleados.isEmpty()) {
					for (Empleado empleado : lstempleados) {
						Row rowDetail = sheet.createRow(index);
						Cell cellfecha = rowDetail.createCell(0);cellfecha.setCellValue(sdf.format(fecha1));cellfecha.setCellStyle(styleBorder);
						Cell cellEmpleado = rowDetail.createCell(1);cellEmpleado.setCellValue(empleado.getPerson().getSurnames() + " " + empleado.getPerson().getNames());cellEmpleado.setCellStyle(styleBorder);
						Cell cellE1 = rowDetail.createCell(2);cellE1.setCellStyle(styleBorder);
						Cell cellS1 = rowDetail.createCell(3);cellS1.setCellStyle(styleBorder);
						Cell cellE2 = rowDetail.createCell(4);cellE2.setCellStyle(styleBorder);
						Cell cellS2 = rowDetail.createCell(5);cellS2.setCellStyle(styleBorder);
						Cell cellArea = rowDetail.createCell(6);cellArea.setCellValue(empleado.getArea().getNombre());cellArea.setCellStyle(styleBorder);
						Cell cellMinTard = rowDetail.createCell(7);cellMinTard.setCellStyle(styleBorder);
						

						Date dia1 = fecha1;
						String day = sdf.format(dia1);
						try {
							dia1 = sdf.parse(day);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Date dia2 = fecha1;
						dia2.setHours(23);
						dia2.setMinutes(59);
						dia2.setSeconds(59);
						List<Asistencia> lstasistenciasEntrada = asistenciaService.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(empleado.getPerson().getDni(), "E", dia1, dia2, true);

						if (!lstasistenciasEntrada.isEmpty()) {
							Asistencia entrada1 = lstasistenciasEntrada.get(0);
							cellMinTard.setCellValue(minutosTardanza(entrada1));

							int a = 2;
							for (Asistencia asistencia : lstasistenciasEntrada) {
								Cell cellHora = rowDetail.createCell(a);
								cellHora.setCellValue(sdfTime.format(asistencia.getHora()));
								cellHora.setCellStyle(styleBorder);
								a = a + 2;
							}

						}

						List<Asistencia> lstasistenciasSalida = asistenciaService
								.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(empleado.getPerson().getDni(), "S", dia1,
										dia2, true);

						if (!lstasistenciasSalida.isEmpty()) {
							int b = 3;
							for (Asistencia asistencia : lstasistenciasSalida) {
								Cell cellHora = rowDetail.createCell(b);
								cellHora.setCellValue(sdfTime.format(asistencia.getHora()));
								cellHora.setCellStyle(styleBorder);
								b = b + 2;
							}

						}

						index++;
					}
				}
				fecha1 = sumarDiasAFecha(fecha1, 1);
			}

		}

		for (int j = 0; j <= 7; j++) {
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
	
	public void procesarExcelExternos() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Asistencia");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		// CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');

//	        Row rowTituloHoja = sheet.createRow(0);
//	        Cell cellTituloHoja = rowTituloHoja.createCell(0);
//	        cellTituloHoja.setCellValue("Reporte de Acciones");
//	        cellTituloHoja.setCellStyle(styleBorder);
//	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo

		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSubFecha = rowSubTitulo.createCell(0);cellSubFecha.setCellValue("FECHA");cellSubFecha.setCellStyle(styleTitulo);
		Cell cellSubEmpleado = rowSubTitulo.createCell(1);cellSubEmpleado.setCellValue("EMPLEADO");cellSubEmpleado.setCellStyle(styleTitulo);
		Cell cellSubEntrada1 = rowSubTitulo.createCell(2);cellSubEntrada1.setCellValue("ENTRADA");cellSubEntrada1.setCellStyle(styleTitulo);
		Cell cellSubSalida1 = rowSubTitulo.createCell(3);cellSubSalida1.setCellValue("SALIDA");cellSubSalida1.setCellStyle(styleTitulo);
		Cell cellSubEntrada2 = rowSubTitulo.createCell(4);cellSubEntrada2.setCellValue("ENTRADA");cellSubEntrada2.setCellStyle(styleTitulo);
		Cell cellSubSalida2 = rowSubTitulo.createCell(5);cellSubSalida2.setCellValue("SALIDA");cellSubSalida2.setCellStyle(styleTitulo);
		Cell cellSubArea = rowSubTitulo.createCell(6);cellSubArea.setCellValue("ÁREA");cellSubArea.setCellStyle(styleTitulo);
		Cell cellSubMinTard = rowSubTitulo.createCell(7);cellSubMinTard.setCellValue("MINUTOS DE TARDANZA");cellSubMinTard.setCellStyle(styleTitulo);
//		Cell cellUserCrea = rowSubTitulo.createCell(8);cellUserCrea.setCellValue("USER CREA");cellUserCrea.setCellStyle(styleTitulo);
//		Cell cellFechaCrea = rowSubTitulo.createCell(9);cellFechaCrea.setCellValue("FECHA CREA");cellFechaCrea.setCellStyle(styleTitulo);
//		Cell cellUserModifica = rowSubTitulo.createCell(10);cellUserModifica.setCellValue("USER MODIFICA");cellUserModifica.setCellStyle(styleTitulo);
//		Cell cellFechaModifica = rowSubTitulo.createCell(11);cellFechaModifica.setCellValue("FECHA MODIFICA");cellFechaModifica.setCellStyle(styleTitulo);

		if (fechaFin.before(fechaIni)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"La fecha fin debe ser mayor o igual que la fecha inicio");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		} else {
			Date fecha1 = fechaIni;
			Date fecha2 = fechaFin;

			int index = 1;
			while (fecha1.getTime() <= fecha2.getTime()) {

				List<Empleado> lstempleados = new ArrayList<>();
				if (empleadoSelected != null) {
					Empleado emp = empleadoService.findByPersonId(empleadoSelected.getPerson().getId());
					lstempleados.add(emp);
				} else {
					lstempleados = empleadoService.findByEstadoAndExternoOrderByPersonSurnamesAsc(true, true);
				}

				if (!lstempleados.isEmpty()) {
					for (Empleado empleado : lstempleados) {
						Row rowDetail = sheet.createRow(index);
						Cell cellfecha = rowDetail.createCell(0);cellfecha.setCellValue(sdf.format(fecha1));cellfecha.setCellStyle(styleBorder);
						Cell cellEmpleado = rowDetail.createCell(1);cellEmpleado.setCellValue(empleado.getPerson().getSurnames() + " " + empleado.getPerson().getNames());cellEmpleado.setCellStyle(styleBorder);
						Cell cellE1 = rowDetail.createCell(2);cellE1.setCellStyle(styleBorder);
						Cell cellS1 = rowDetail.createCell(3);cellS1.setCellStyle(styleBorder);
						Cell cellE2 = rowDetail.createCell(4);cellE2.setCellStyle(styleBorder);
						Cell cellS2 = rowDetail.createCell(5);cellS2.setCellStyle(styleBorder);
						Cell cellArea = rowDetail.createCell(6);cellArea.setCellValue(empleado.getArea().getNombre());cellArea.setCellStyle(styleBorder);
						Cell cellMinTard = rowDetail.createCell(7);cellMinTard.setCellStyle(styleBorder);
						

						Date dia1 = fecha1;
						String day = sdf.format(dia1);
						try {
							dia1 = sdf.parse(day);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Date dia2 = fecha1;
						dia2.setHours(23);
						dia2.setMinutes(59);
						dia2.setSeconds(59);
						List<Asistencia> lstasistenciasEntrada = asistenciaService.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(empleado.getPerson().getDni(), "E", dia1, dia2, true);

						if (!lstasistenciasEntrada.isEmpty()) {
							Asistencia entrada1 = lstasistenciasEntrada.get(0);
							cellMinTard.setCellValue(minutosTardanza(entrada1));

							int a = 2;
							for (Asistencia asistencia : lstasistenciasEntrada) {
								Cell cellHora = rowDetail.createCell(a);
								cellHora.setCellValue(sdfTime.format(asistencia.getHora()));
								cellHora.setCellStyle(styleBorder);
								a = a + 2;
							}

						}

						List<Asistencia> lstasistenciasSalida = asistenciaService
								.findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(empleado.getPerson().getDni(), "S", dia1,
										dia2, true);

						if (!lstasistenciasSalida.isEmpty()) {
							int b = 3;
							for (Asistencia asistencia : lstasistenciasSalida) {
								Cell cellHora = rowDetail.createCell(b);
								cellHora.setCellValue(sdfTime.format(asistencia.getHora()));
								cellHora.setCellStyle(styleBorder);
								b = b + 2;
							}

						}

						index++;
					}
				}
				fecha1 = sumarDiasAFecha(fecha1, 1);
			}

		}

		for (int j = 0; j <= 7; j++) {
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
	
	public void procesarExcelReporteSemanal() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Reporte Semanal Asistencia");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');


		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSubEmpleado = rowSubTitulo.createCell(0);cellSubEmpleado.setCellValue("EMPLEADO");cellSubEmpleado.setCellStyle(styleTitulo);
		Cell cellSubArea = rowSubTitulo.createCell(1);cellSubArea.setCellValue("AREA");cellSubArea.setCellStyle(styleTitulo);
		Cell cellSubLunes = rowSubTitulo.createCell(2);cellSubLunes.setCellValue("LUNES");cellSubLunes.setCellStyle(styleTitulo);
		Cell cellSubMartes = rowSubTitulo.createCell(3);cellSubMartes.setCellValue("MARTES");cellSubMartes.setCellStyle(styleTitulo);
		Cell cellSubMiercoles = rowSubTitulo.createCell(4);cellSubMiercoles.setCellValue("MIERCOLES");cellSubMiercoles.setCellStyle(styleTitulo);
		Cell cellSubJueves = rowSubTitulo.createCell(5);cellSubJueves.setCellValue("JUEVES");cellSubJueves.setCellStyle(styleTitulo);
		Cell cellSubViernes = rowSubTitulo.createCell(6);cellSubViernes.setCellValue("VIERNES");cellSubViernes.setCellStyle(styleTitulo);
		Cell cellSubSabado = rowSubTitulo.createCell(7);cellSubSabado.setCellValue("SABADO");cellSubSabado.setCellStyle(styleTitulo);
		Cell cellSubDomigo = rowSubTitulo.createCell(8);cellSubDomigo.setCellValue("DOMINGO");cellSubDomigo.setCellStyle(styleTitulo);

	
	
		
		List<Empleado> lstempleados = new ArrayList<>();
		
		if(empleadoBusqueda==null && areaSelected==null) {
			lstempleados = empleadoService.findByEstado(true);
		}else if(empleadoBusqueda!=null && areaSelected==null) {
			lstempleados = empleadoService.findByPersonAndEstado(empleadoBusqueda.getPerson(), true);
		}else if(empleadoBusqueda==null && areaSelected!= null) {
			lstempleados = empleadoService.findByEstadoAndArea(true, areaSelected);
		}else if(empleadoBusqueda!=null && areaSelected!= null) {
			lstempleados = empleadoService.findByPersonAndEstadoAndArea(empleadoBusqueda.getPerson(), true, areaSelected);
		}
		
		int index = 1;
		

		if (!lstempleados.isEmpty()) {
			for (Empleado empleado : lstempleados) {
				Row rowDetail = sheet.createRow(index);
				Cell cellEmpleado = rowDetail.createCell(0);cellEmpleado.setCellValue(empleado.getPerson().getSurnames() + " " + empleado.getPerson().getNames());cellEmpleado.setCellStyle(styleBorder);
				Cell cellArea = rowDetail.createCell(1);cellArea.setCellValue(empleado.getArea().getNombre());cellArea.setCellStyle(styleBorder);
				
				Cell cellLunes = rowDetail.createCell(2);cellLunes.setCellValue("E:" +fechaTexto(empleado, "E", 0) + " / S:" + fechaTexto(empleado, "S", 0));cellLunes.setCellStyle(styleBorder); 

				Cell cellMartes = rowDetail.createCell(3);cellMartes.setCellValue("E:" +fechaTexto(empleado, "E", 1) + " / S:" + fechaTexto(empleado, "S", 1));cellMartes.setCellStyle(styleBorder);
				Cell cellMiercoles = rowDetail.createCell(4);cellMiercoles.setCellValue("E:" +fechaTexto(empleado, "E", 2) + " / S:" + fechaTexto(empleado, "S", 2));cellMiercoles.setCellStyle(styleBorder);
				Cell cellJueves = rowDetail.createCell(5);cellJueves.setCellValue("E:" +fechaTexto(empleado, "E", 3) + " / S:" + fechaTexto(empleado, "S", 3));cellJueves.setCellStyle(styleBorder);
				Cell cellViernes = rowDetail.createCell(6);cellViernes.setCellValue("E:" +fechaTexto(empleado, "E", 4) + " / S:" + fechaTexto(empleado, "S", 4));cellViernes.setCellStyle(styleBorder);
				Cell cellSabado = rowDetail.createCell(7);cellSabado.setCellValue("E:" +fechaTexto(empleado, "E", 5) + " / S:" + fechaTexto(empleado, "S", 5));cellSabado.setCellStyle(styleBorder);
				Cell cellDomingo = rowDetail.createCell(8);cellDomingo.setCellValue("E:" +fechaTexto(empleado, "E", 6) + " / S:" + fechaTexto(empleado, "S", 6));cellDomingo.setCellStyle(styleBorder);


				index++;

			}
		}
	

		for (int j = 0; j <= 8; j++) {
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

	public Date sumarDiasAFecha(Date fecha, int dias) {
		if (dias == 0)
			return fecha;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		Date date = calendar.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}

	public void iniciarEmpleadoLazy() {

		lstEmpeladoLazy = new LazyDataModel<Empleado>() {
			private List<Empleado> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Empleado getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Empleado empleado : datasource) {
					if (empleado.getId() == intRowKey) {
						return empleado;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Empleado empleado) {
				return String.valueOf(empleado.getId());
			}

			@Override
			public List<Empleado> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Sort sort = Sort.by("person.surnames").ascending();
				if (sortBy != null) {
					for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
						System.out.println(entry.getKey() + "/" + entry.getValue());
						if (entry.getValue().getOrder().isAscending()) {
							sort = Sort.by(entry.getKey()).descending();
						} else {
							sort = Sort.by(entry.getKey()).ascending();
						}
					}
				}

				Pageable pageable = PageRequest.of(first / pageSize, pageSize, sort);

				Page<Empleado> pageEmpleado = null;

				if(empleadoBusqueda==null && areaSelected==null) {
					pageEmpleado = empleadoService.findByEstadoAndSucursalEmpresa(true, navegacionBean.getSucursalLogin().getEmpresa(), pageable);
				}else if(empleadoBusqueda!=null && areaSelected==null) {
					pageEmpleado = empleadoService.findByPersonAndEstadoAndSucursalEmpresa(empleadoBusqueda.getPerson(), true, navegacionBean.getSucursalLogin().getEmpresa(), pageable);
				}else if(empleadoBusqueda==null && areaSelected!= null) {
					pageEmpleado = empleadoService.findByEstadoAndAreaAndSucursalEmpresa(true, areaSelected, navegacionBean.getSucursalLogin().getEmpresa(), pageable);
				}else if(empleadoBusqueda!=null && areaSelected!= null) {
					pageEmpleado = empleadoService.findByPersonAndEstadoAndAreaAndSucursalEmpresa(empleadoBusqueda.getPerson(), true, areaSelected, navegacionBean.getSucursalLogin().getEmpresa(), pageable);
				}

				setRowCount((int) pageEmpleado.getTotalElements());
				return datasource = pageEmpleado.getContent();
			}
		};
	}
	
	public void iniciarLazy() {

		lstAsistenciaLazy = new LazyDataModel<Asistencia>() {
			private List<Asistencia> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Asistencia getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Asistencia asistencia : datasource) {
					if (asistencia.getId() == intRowKey) {
						return asistencia;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Asistencia asistencia) {
				return String.valueOf(asistencia.getId());
			}

			@Override
			public List<Asistencia> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Sort sort = Sort.by("hora").ascending();
				if (sortBy != null) {
					for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
						System.out.println(entry.getKey() + "/" + entry.getValue());
						if (entry.getValue().getOrder().isAscending()) {
							sort = Sort.by(entry.getKey()).descending();
						} else {
							sort = Sort.by(entry.getKey()).ascending();
						}
					}
				}

				Pageable pageable = PageRequest.of(first / pageSize, pageSize, sort);

				Page<Asistencia> pageAsistencia = null;

				String dni = "%%";

				if (empleadoSelected != null) {
					dni = "%" + empleadoSelected.getPerson().getDni() + "%";
				}

				fechaIni.setHours(0);
				fechaIni.setMinutes(0);
				fechaIni.setSeconds(0);
				fechaFin.setHours(23);
				fechaFin.setMinutes(59);
				fechaFin.setSeconds(59);

				pageAsistencia = asistenciaService.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstado(dni, "%" + tipo + "%", fechaIni, fechaFin, estado, pageable);

				setRowCount((int) pageAsistencia.getTotalElements());
				return datasource = pageAsistencia.getContent();
			}
		};
	}


	public String convertirHora(Date hora) {
		String a = sdfFull.format(hora);
		return a;
	}

	public String convertirTipo(String tipo) {
		String valor = "";
		if (tipo.equals("E")) {
			valor = "ENTRADA";
		} else {
			valor = "SALIDA";
		}

		return valor;
	}

	public Converter getConversorEmpleado() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {
				if (value.trim().equals("") || value == null || value.trim().equals("null")) {
					return null;
				} else {
					Empleado c = null;
					for (Empleado si : lstEmpleado) {
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

	public List<Empleado> completeEmpleado(String query) {
		List<Empleado> lista = new ArrayList<>();
		for (Empleado empleado : lstEmpleado) {
			if (empleado.getPerson().getSurnames().toUpperCase().contains(query.toUpperCase())
					|| empleado.getPerson().getNames().toUpperCase().contains(query.toUpperCase())) {
				lista.add(empleado);
			}
		}
		return lista;
	}
	
	public Converter getConversorArea() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Area c = null;
                    for (Area si : lstArea) {
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
                    return ((Area) value).getId() + "";
                }
            }
        };
    }
	
	
	public List<Empleado> getLstEmpleado() {
		return lstEmpleado;
	}
	public void setLstEmpleado(List<Empleado> lstEmpleado) {
		this.lstEmpleado = lstEmpleado;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public Empleado getEmpleadoSelected() {
		return empleadoSelected;
	}
	public void setEmpleadoSelected(Empleado empleadoSelected) {
		this.empleadoSelected = empleadoSelected;
	}
	public Asistencia getAsistenciaSelected() {
		return asistenciaSelected;
	}
	public void setAsistenciaSelected(Asistencia asistenciaSelected) {
		this.asistenciaSelected = asistenciaSelected;
	}
	public LazyDataModel<Asistencia> getLstAsistenciaLazy() {
		return lstAsistenciaLazy;
	}
	public void setLstAsistenciaLazy(LazyDataModel<Asistencia> lstAsistenciaLazy) {
		this.lstAsistenciaLazy = lstAsistenciaLazy;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public AsistenciaService getAsistenciaService() {
		return asistenciaService;
	}
	public void setAsistenciaService(AsistenciaService asistenciaService) {
		this.asistenciaService = asistenciaService;
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
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public boolean isMostrarBoton() {
		return mostrarBoton;
	}
	public void setMostrarBoton(boolean mostrarBoton) {
		this.mostrarBoton = mostrarBoton;
	}
	public Empleado getEmpleadoBusqueda() {
		return empleadoBusqueda;
	}
	public void setEmpleadoBusqueda(Empleado empleadoBusqueda) {
		this.empleadoBusqueda = empleadoBusqueda;
	}
	public List<Area> getLstArea() {
		return lstArea;
	}
	public void setLstArea(List<Area> lstArea) {
		this.lstArea = lstArea;
	}
	public AreaService getAreaService() {
		return areaService;
	}
	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}
	public Area getAreaSelected() {
		return areaSelected;
	}
	public void setAreaSelected(Area areaSelected) {
		this.areaSelected = areaSelected;
	}
	public LazyDataModel<Empleado> getLstEmpeladoLazy() {
		return lstEmpeladoLazy;
	}
	public void setLstEmpeladoLazy(LazyDataModel<Empleado> lstEmpeladoLazy) {
		this.lstEmpeladoLazy = lstEmpeladoLazy;
	}
	public SemanaService getSemanaService() {
		return semanaService;
	}
	public void setSemanaService(SemanaService semanaService) {
		this.semanaService = semanaService;
	}
	public Semana getSemanaSelected() {
		return semanaSelected;
	}
	public void setSemanaSelected(Semana semanaSelected) {
		this.semanaSelected = semanaSelected;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}

}
