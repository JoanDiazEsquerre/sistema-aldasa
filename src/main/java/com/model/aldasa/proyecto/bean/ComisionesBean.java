package com.model.aldasa.proyecto.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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

import com.model.aldasa.entity.ComisionSupervisor;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
//import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.service.CargoService;
import com.model.aldasa.service.ComisionSupervisorService;
import com.model.aldasa.service.ComisionesService;
import com.model.aldasa.service.ConfiguracionComisionService;
import com.model.aldasa.service.DetalleComisionesService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.MetaSupervisorService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.Perfiles;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class ComisionesBean extends BaseBean implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService; 
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService; 
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{configuracionComisionService}")
	private ConfiguracionComisionService configuracionComisionService;
	
	@ManagedProperty(value = "#{comisionesService}")
	private ComisionesService comisionesService;
	
	@ManagedProperty(value = "#{detalleComisionesService}")
	private DetalleComisionesService detalleComisionesService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{metaSupervisorService}")
	private MetaSupervisorService metaSupervisorService;
	
	@ManagedProperty(value = "#{cargoService}")
	private CargoService cargoService;
	
	@ManagedProperty(value = "#{comisionSupervisorService}")
	private ComisionSupervisorService comisionSupervisorService; 
	
	private LazyDataModel<ComisionSupervisor> lstComisionSupervisorLazy;
	private LazyDataModel<Comisiones> lstComisionesLazy;
	
	private ConfiguracionComision configuracionComisionSelected;
	private ComisionSupervisor comisionSupervisorSelected;
	private Comisiones comisionesSelected;
	
	private BigDecimal porcentajeGeneral;
	
	private List<ConfiguracionComision> lstConfiguracionComision;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	
	private StreamedContent fileDes;
	
	@PostConstruct
	public void init() {
		cargarListaConfiguracion();
		
		iniciarLazyComisionSupervisor();
		iniciarComisionesLazy();
	}
	
	public void procesarExcel() {
		PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').show();"); 
		
		String nombreArchivo= "Reporte de comisiones "+comisionSupervisorSelected.getPersonaSupervisor().getNames()+".xlsx";
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Documento Venta");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("ASESOR");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("EXTERNO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("CLIENTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("DNI CLIENTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("PROYECTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("MANZANA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("LOTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("TIPO DE PAGO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("PRECIO LOTE ");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("INICIAL LOTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("COMISION POR LOTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue("TOTAL COMISION ASESOR");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("BONO ASESOR");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue("COMISION SUPERVISOR");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue("COMISION JEFE VENTA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue("COMISION SUBGERENTE");cellSub1.setCellStyle(styleTitulo);

		
		
		List<Comisiones> lstComisiones = comisionesService.findByEstadoAndComisionSupervisor(true, comisionSupervisorSelected);
		
		int index = 1;
		BigDecimal totalAsesor = BigDecimal.ZERO;
		BigDecimal totalBonoAsesor = BigDecimal.ZERO;
		BigDecimal totalSupervisor = BigDecimal.ZERO;
		BigDecimal totalJefeVenta = BigDecimal.ZERO;
		BigDecimal totalSubgerente = BigDecimal.ZERO;
		BigDecimal totalInicial = BigDecimal.ZERO;
		BigDecimal totalPrecioLote = BigDecimal.ZERO;
		
		
		
		if (!lstComisiones.isEmpty()) {
			for (Comisiones comision : lstComisiones) {
				List<DetalleComisiones> lstDet = detalleComisionesService.findByEstadoAndComisiones(true, comision);
				if(!lstDet.isEmpty()) {
					totalBonoAsesor = totalBonoAsesor.add(comision.getBono());
					int indexUltimo = index;
					BigDecimal totalComisionAsesor = BigDecimal.ZERO;
					for (DetalleComisiones dc : lstDet) {
						totalComisionAsesor = totalComisionAsesor.add(dc.getMontoComision());
								
					}
					
					for (DetalleComisiones dc : lstDet) {
						rowSubTitulo = sheet.createRow(index);
						cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(dc.getComisiones().getPersonaAsesor().getNames()+" "+dc.getComisiones().getPersonaAsesor().getSurnames());cellSub1.setCellStyle(styleBorder);
						
						Empleado empleado = empleadoService.findByPersonId(dc.getComisiones().getPersonaAsesor().getId());
						if(empleado!=null) {
							cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(empleado.isExterno()?"SI":"NO");cellSub1.setCellStyle(styleBorder);

						}else {
							cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("SIN DEFINIR");cellSub1.setCellStyle(styleBorder);
						}
								

						cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(dc.getPlantillaVenta().getPerson().getNames()+" "+dc.getPlantillaVenta().getPerson().getSurnames());cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(dc.getPlantillaVenta().getPerson().getDni());cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(dc.getPlantillaVenta().getLote().getProject().getName());cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(dc.getPlantillaVenta().getLote().getManzana().getName());cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(dc.getPlantillaVenta().getLote().getNumberLote());cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(dc.getPlantillaVenta().getTipoPago());cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(dc.getPlantillaVenta().getMontoVenta()+"");cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(dc.getPlantillaVenta().getMontoInicial() ==null?"0" : dc.getPlantillaVenta().getMontoInicial()+"");cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue(dc.getMontoComision()+"");cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue(totalComisionAsesor+"");cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue(comision.getBono()+"");cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue(dc.getMontoComisionSupervisor()+"");cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue(dc.getMontoComisionJefeVenta()+"");cellSub1.setCellStyle(styleBorder);
						cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue(dc.getMontoComisionSubgerente()+"");cellSub1.setCellStyle(styleBorder);
						
						totalAsesor = totalAsesor.add(dc.getMontoComision());
						totalSupervisor = totalSupervisor.add(dc.getMontoComisionSupervisor());
						totalJefeVenta = totalJefeVenta.add(dc.getMontoComisionJefeVenta());
						totalSubgerente = totalSubgerente.add(dc.getMontoComisionSubgerente());
						totalPrecioLote = totalPrecioLote.add(dc.getPlantillaVenta().getMontoVenta());
						if(dc.getPlantillaVenta().getMontoInicial()!=null) {
							totalInicial = totalInicial.add(dc.getPlantillaVenta().getMontoInicial());
						}
						
						
						index++;
					}
					
					if(lstDet.size()>1) {
						sheet.addMergedRegion(new CellRangeAddress(indexUltimo,index-1, 0, 0));
						sheet.addMergedRegion(new CellRangeAddress(indexUltimo,index-1, 1, 1));
						sheet.addMergedRegion(new CellRangeAddress(indexUltimo,index-1, 11, 11));
						sheet.addMergedRegion(new CellRangeAddress(indexUltimo,index-1, 12, 12));
					}

				}
			
			
//				
//				if(d.getTipoDocumento().getCodigo().equals("03") ||d.getTipoDocumento().getCodigo().equals("01")) {
//					total = total.add(d.getTotal());
//				}
//				
//				index++;
			}
		}
		
		rowSubTitulo = sheet.createRow(index);
		sheet.addMergedRegion(new CellRangeAddress(index,index, 0, 7));
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(totalPrecioLote+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(totalInicial+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue(totalAsesor+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue(totalAsesor+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue(totalBonoAsesor+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(13);cellSub1.setCellValue(totalSupervisor+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(14);cellSub1.setCellValue(totalJefeVenta+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(15);cellSub1.setCellValue(totalSubgerente+"");cellSub1.setCellStyle(styleBorder);
		
		
		index = index+5;
		
		rowSubTitulo = sheet.createRow(index);
		sheet.addMergedRegion(new CellRangeAddress(index,index, 0, 3));
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("RESUMEN");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("TOTALES");cellSub1.setCellStyle(styleBorder);
		
		index++;

		rowSubTitulo = sheet.createRow(index);
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("SUPERVISOR");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("COMISION");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("BONO");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("LOTE VENDIDOS");cellSub1.setCellStyle(styleBorder);
		
		index++;

		rowSubTitulo = sheet.createRow(index);
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(comisionSupervisorSelected.getPersonaSupervisor().getNames()+" "+comisionSupervisorSelected.getPersonaSupervisor().getSurnames());cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(comisionSupervisorSelected.getMontoComision()+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(comisionSupervisorSelected.getBono()+"");cellSub1.setCellStyle(styleBorder);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(comisionSupervisorSelected.getNumVendido());cellSub1.setCellStyle(styleBorder);
		
		
		for (int j = 0; j <= 15; j++) {
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
			
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 


		} catch (FileNotFoundException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		} catch (IOException e) {
			PrimeFaces.current().executeScript("PF('blockUIWidgetGeneral').hide();"); 

			e.printStackTrace();
		}
	}
	
	public void cargarListaConfiguracion() {
		lstConfiguracionComision = configuracionComisionService.findByEstadoOrderByCodigoDesc(true);
		configuracionComisionSelected = lstConfiguracionComision.get(0);
		
		calcularPorcentajeGeneral();
	}
	
	public BigDecimal calcularPorcentaje(ComisionSupervisor comisionSupervisor) {
		BigDecimal calculo = BigDecimal.ZERO;
		int num = comisionSupervisor.getNumVendido();
		
		if(num !=0) {
			BigDecimal multiplicado = new BigDecimal(num).multiply(new BigDecimal(100));    
			calculo = multiplicado.divide(new BigDecimal(comisionSupervisor.getMeta()), 2, RoundingMode.HALF_UP);
		}
		return calculo;
	}
	
	public void calcularPorcentajeGeneral() {
		porcentajeGeneral = BigDecimal.ZERO;
		int num = configuracionComisionSelected.getNumVendidojv();
		
		if(num !=0) {
			BigDecimal multiplicado = new BigDecimal(num).multiply(new BigDecimal(100));    
			porcentajeGeneral = multiplicado.divide(new BigDecimal(configuracionComisionSelected.getMetajv()), 2, RoundingMode.HALF_UP);
		}
	}
	
	public List<DetalleComisiones> getDetalleComisiones(Comisiones comisiones) {
		return detalleComisionesService.findByEstadoAndComisiones(true, comisiones);
	}
	
	public void iniciarLazyComisionSupervisor() {
		lstComisionSupervisorLazy = new LazyDataModel<ComisionSupervisor>() {
			private List<ComisionSupervisor> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public ComisionSupervisor getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (ComisionSupervisor comisiones : datasource) {
                    if (comisiones.getId() == intRowKey) {
                        return comisiones;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(ComisionSupervisor comisiones) {
                return String.valueOf(comisiones.getId());
            }

			@Override
			public List<ComisionSupervisor> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {				
				
                Sort sort=Sort.by("id").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	    System.out.println(entry.getKey() + "/" + entry.getValue());
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                	   }
                	}
                }
                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
				Page<ComisionSupervisor> pageComisiones = comisionSupervisorService.findByEstadoAndConfiguracionComision(true, configuracionComisionSelected, pageable);
			
					
				setRowCount((int) pageComisiones.getTotalElements());
				return datasource = pageComisiones.getContent();
			}
		};
	}
	
	public void iniciarComisionesLazy() {
		lstComisionesLazy = new LazyDataModel<Comisiones>() {
			private List<Comisiones> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Comisiones getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Comisiones plantillaVenta : datasource) {
                    if (plantillaVenta.getId() == intRowKey) {
                        return plantillaVenta;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Comisiones plantillaVenta) {
                return String.valueOf(plantillaVenta.getId());
            }

			@Override
			public List<Comisiones> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura

				
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

				Page<Comisiones> pagePlantillaVenta= comisionesService.findByEstadoAndComisionSupervisor(true, comisionSupervisorSelected, pageable);		
				
				setRowCount((int) pagePlantillaVenta.getTotalElements());
				return datasource = pagePlantillaVenta.getContent();
			}
		};
	}
	
	public void cambiarComision() {
		calcularPorcentajeGeneral();
//		lstComisiones = comisionesService.findByEstadoAndConfiguracionComision(true, comisionSelected);
//		lstPersonSupervisor = new ArrayList<>();
//		personSupervisorSelected=null;
//		
//		if(!lstComisiones.isEmpty()) {
//			for(Comisiones c : lstComisiones) {
//				// para listar supervisores
//				if(lstPersonSupervisor.isEmpty()) {
//					lstPersonSupervisor.add(c.getPersonSupervisor());
//				}else {
//					boolean encuentra=false;
//					for(Person p:lstPersonSupervisor) {
//						
//						if(p.getId().equals(c.getPersonSupervisor().getId())) { 
//							encuentra=true;
//						}
//					}
//					if(!encuentra) {
//						lstPersonSupervisor.add(c.getPersonSupervisor());
//					}
//				}
//			}
//			
//		}
//		
//		
//		for(Team equipo :lstTeam) {
//
//			if(lstPersonSupervisor.isEmpty()) { 
//				lstPersonSupervisor.add(equipo.getPersonSupervisor());
//			}else {
//				boolean encuentra=false;
//				for(Person p:lstPersonSupervisor) {
//					
//					if(p.getId().equals(equipo.getPersonSupervisor().getId())) { 
//						encuentra=true;
//					}
//				}
//				if(!encuentra) {
//					lstPersonSupervisor.add(equipo.getPersonSupervisor());
//				}
//			}
//			
//			
//		}
			
		
		
		
	}
	
	
	public Converter getConversorConfiguracionComision() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	ConfiguracionComision c = null;
                    for (ConfiguracionComision si : lstConfiguracionComision) {
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
                    return ((ConfiguracionComision) value).getId() + "";
                }
            }
        };
    }
	
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public ConfiguracionComisionService getConfiguracionComisionService() {
		return configuracionComisionService;
	}
	public void setConfiguracionComisionService(ConfiguracionComisionService configuracionComisionService) {
		this.configuracionComisionService = configuracionComisionService;
	}
//	public DetalleComisionesService getComisionesService() {
//		return comisionesService;
//	}
//	public void setComisionesService(DetalleComisionesService comisionesService) {
//		this.comisionesService = comisionesService;
//	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public MetaSupervisorService getMetaSupervisorService() {
		return metaSupervisorService;
	}
	public void setMetaSupervisorService(MetaSupervisorService metaSupervisorService) {
		this.metaSupervisorService = metaSupervisorService;
	}
	public CargoService getCargoService() {
		return cargoService;
	}
	public void setCargoService(CargoService cargoService) {
		this.cargoService = cargoService;
	}
	public ConfiguracionComision getConfiguracionComisionSelected() {
		return configuracionComisionSelected;
	}
	public void setConfiguracionComisionSelected(ConfiguracionComision configuracionComisionSelected) {
		this.configuracionComisionSelected = configuracionComisionSelected;
	}
	public List<ConfiguracionComision> getLstConfiguracionComision() {
		return lstConfiguracionComision;
	}
	public void setLstConfiguracionComision(List<ConfiguracionComision> lstConfiguracionComision) {
		this.lstConfiguracionComision = lstConfiguracionComision;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public ComisionSupervisorService getComisionSupervisorService() {
		return comisionSupervisorService;
	}
	public void setComisionSupervisorService(ComisionSupervisorService comisionSupervisorService) {
		this.comisionSupervisorService = comisionSupervisorService;
	}
	public LazyDataModel<ComisionSupervisor> getLstComisionSupervisorLazy() {
		return lstComisionSupervisorLazy;
	}
	public void setLstComisionSupervisorLazy(LazyDataModel<ComisionSupervisor> lstComisionSupervisorLazy) {
		this.lstComisionSupervisorLazy = lstComisionSupervisorLazy;
	}
	public ComisionSupervisor getComisionSupervisorSelected() {
		return comisionSupervisorSelected;
	}
	public void setComisionSupervisorSelected(ComisionSupervisor comisionSupervisorSelected) {
		this.comisionSupervisorSelected = comisionSupervisorSelected;
	}
	public ComisionesService getComisionesService() {
		return comisionesService;
	}
	public void setComisionesService(ComisionesService comisionesService) {
		this.comisionesService = comisionesService;
	}
	public LazyDataModel<Comisiones> getLstComisionesLazy() {
		return lstComisionesLazy;
	}
	public void setLstComisionesLazy(LazyDataModel<Comisiones> lstComisionesLazy) {
		this.lstComisionesLazy = lstComisionesLazy;
	}
	public DetalleComisionesService getDetalleComisionesService() {
		return detalleComisionesService;
	}
	public void setDetalleComisionesService(DetalleComisionesService detalleComisionesService) {
		this.detalleComisionesService = detalleComisionesService;
	}
	public Comisiones getComisionesSelected() {
		return comisionesSelected;
	}
	public void setComisionesSelected(Comisiones comisionesSelected) {
		this.comisionesSelected = comisionesSelected;
	}
	public BigDecimal getPorcentajeGeneral() {
		return porcentajeGeneral;
	}
	public void setPorcentajeGeneral(BigDecimal porcentajeGeneral) {
		this.porcentajeGeneral = porcentajeGeneral;
	}
	public StreamedContent getFileDes() {
		return fileDes;
	}
	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	
	
		
}
