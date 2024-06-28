package com.model.aldasa.general.bean;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DetalleImagenLote;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.DetalleDocumentoVentaService;
import com.model.aldasa.service.DetalleImagenLoteService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.UtilXls;
import com.model.aldasa.ventas.bean.LoadImageDocumentoBean;

@ManagedBean
@ViewScoped
public class VoucherBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{sucursalService}")
	private SucursalService sucursalService;
	
	@ManagedProperty(value = "#{detalleDocumentoVentaService}")
	private DetalleDocumentoVentaService detalleDocumentoVentaService;
	
	@ManagedProperty(value = "#{loadImageDocumentoBean}")
	private LoadImageDocumentoBean loadImageDocumentoBean;
	
	@ManagedProperty(value = "#{detalleImagenLoteService}")
	private DetalleImagenLoteService detalleImagenLoteService;
	
	private String nombreArchivo = "Reporte de Voucher.xlsx";
	
	private boolean estado, validarExistenciaImagen, busquedaSinFecha;
	private String tituloDialog, porRegularizar;
	
	private Sucursal sucursal, sucursalDialog;
	private Imagen imagenSelected;
	private CuentaBancaria ctaBanFilter;
	
	private List<Sucursal> lstSucursal;
	private List<CuentaBancaria> lstCuentaBancaria;
	private List<CuentaBancaria> lstCuentaBancariaFilter;
	
	private List<Project> lstProject;
	private List<Manzana> lstManzana1, lstManzana2, lstManzana3, lstManzana4, lstManzana5, lstManzana6, lstManzana7, lstManzana8, lstManzana9, lstManzana10;
	private List<Lote> lstLote1, lstLote2, lstLote3, lstLote4, lstLote5, lstLote6, lstLote7, lstLote8, lstLote9, lstLote10;
	
	private Project proyecto1, proyecto2, proyecto3, proyecto4, proyecto5, proyecto6, proyecto7, proyecto8, proyecto9, proyecto10;	
	private Manzana manzana1, manzana2, manzana3, manzana4, manzana5, manzana6, manzana7, manzana8, manzana9, manzana10;
	private Lote lote1, lote2, lote3, lote4, lote5, lote6, lote7, lote8, lote9, lote10 ;
	
	private LazyDataModel<Imagen> lstImagenLazy;
	
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	SimpleDateFormat sdfFull2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private Date fechaIni, fechaFin;
	
	private StreamedContent fileDes;
	
	private UploadedFile file;
	
	@PostConstruct
	public void init() {
		porRegularizar = "%%";
		busquedaSinFecha= false;
		estado=true;
		lstSucursal =sucursalService.findByEstado(true);
		
		lstProject=projectService.findByStatusOrderByNameAsc(true);
		
		fechaIni = new Date();
		fechaFin = new Date();
		iniciarLazy();
	}
	
	public void eliminarArchivo() {
        File archivo = new File(navegacionBean.getSucursalLogin().getEmpresa().getRutaDocumentoVenta() + imagenSelected.getNombre());
        
        if (archivo.exists()) {
            if (archivo.delete()) {
            	modifyVoucher();
            	addInfoMessage("La imagen ha sido eliminada correctamente."); 
            	
            } else {
            	addErrorMessage("No se pudo eliminar la imagen.");
            }
        } else {
        	addErrorMessage("La imagen no existe.");
        }
    }
	
	
	public void modifyVoucher() {

		loadImageDocumentoBean.setNombreArchivo(imagenSelected.getNombre());
		validarExistenciaImagen= false;
		
		 if(!imagenSelected.getNombre().equals("") && !imagenSelected.getNombre().equals("-")) {
			 String ruta = navegacionBean.getSucursalLogin().getEmpresa().getRutaDocumentoVenta()+imagenSelected.getNombre();
//			 
			 File file = new File(ruta);
			  if (file.exists()) {
				  validarExistenciaImagen = true;
			  }
		 }

		tituloDialog = "MODIFICAR VOUCHER";
		file = null;

		CuentaBancaria cta = imagenSelected.getCuentaBancaria();
		sucursalDialog = imagenSelected.getCuentaBancaria().getSucursal();
		listarCuentaBancaria();
		imagenSelected.setCuentaBancaria(cta);
		
		proyecto1=null;proyecto2=null;proyecto3=null;proyecto4=null;proyecto5=null;proyecto6=null;proyecto7=null;proyecto8=null;proyecto9=null;proyecto10=null;
		manzana1=null;manzana2=null;manzana3=null;manzana4=null;manzana5=null;manzana6=null;manzana7=null;manzana8=null;manzana9=null;manzana10=null;
		lote1=null;lote2=null;lote3=null;lote4=null;lote5=null;lote6=null;lote7=null;lote8=null;lote9=null;lote10=null;

		List<DetalleImagenLote> lstImagenLote = detalleImagenLoteService.findByEstadoAndImagen(true, imagenSelected);
		if (!lstImagenLote.isEmpty()) {
			int cont = 1;
			for (DetalleImagenLote dt : lstImagenLote) {

				switch (cont) {
				case 1:
					proyecto1 = dt.getLote().getProject();
					listarManzanas(1);
					manzana1 = dt.getLote().getManzana();
					listarLotes(1);
					lote1 = dt.getLote();
					break;
				case 2:
					proyecto2 = dt.getLote().getProject();
					listarManzanas(2);
					manzana2 = dt.getLote().getManzana();
					listarLotes(2);
					lote2 = dt.getLote();
					break;
				case 3:
					proyecto3 = dt.getLote().getProject();
					listarManzanas(3);
					manzana3 = dt.getLote().getManzana();
					listarLotes(3);
					lote3 = dt.getLote();
					break;
				case 4:
					proyecto4 = dt.getLote().getProject();
					listarManzanas(4);
					manzana4 = dt.getLote().getManzana();
					listarLotes(4);
					lote4 = dt.getLote();
					break;
				case 5:
					proyecto5 = dt.getLote().getProject();
					listarManzanas(5);
					manzana5 = dt.getLote().getManzana();
					listarLotes(5);
					lote5 = dt.getLote();
					break;
				case 6:
					proyecto6 = dt.getLote().getProject();
					listarManzanas(6);
					manzana6 = dt.getLote().getManzana();
					listarLotes(6);
					lote6 = dt.getLote();
					break;
				case 7:
					proyecto7 = dt.getLote().getProject();
					listarManzanas(7);
					manzana7 = dt.getLote().getManzana();
					listarLotes(7);
					lote7 = dt.getLote();
					break;
				case 8:
					proyecto8 = dt.getLote().getProject();
					listarManzanas(8);
					manzana8 = dt.getLote().getManzana();
					listarLotes(8);
					lote8 = dt.getLote();
					break;
				case 9:
					proyecto9 = dt.getLote().getProject();
					listarManzanas(9);
					manzana9 = dt.getLote().getManzana();
					listarLotes(9);
					lote9 = dt.getLote();
					break;
				case 10:
					proyecto10 = dt.getLote().getProject();
					listarManzanas(10);
					manzana10 = dt.getLote().getManzana();
					listarLotes(10);
					lote10 = dt.getLote();
					break;
				}

				
				cont++;
			}

		}
		
//		if(imagenSelected.getLote()!=null) {
//			Lote lote = imagenSelected.getLote();
//			
//			proyectoPlantilla = lote.getProject();
//			listarManzanas(1);
//			manzanaPlantilla = lote.getManzana();
//			listarLotesPlantilla();
//			imagenSelected.setLote(lote);
//		}
		PrimeFaces.current().executeScript("PF('imagenDialog').show();");
	}
	
	public void newImagen() {
		loadImageDocumentoBean.setNombreArchivo("-");
		validarExistenciaImagen=false;
		
		tituloDialog="NUEVO VOUCHER";
		sucursalDialog=null;
		lstCuentaBancaria = new ArrayList<>();
		imagenSelected = new Imagen();
		imagenSelected.setNombre("-");
		imagenSelected.setCarpeta("IMG-DOCUMENTO-VENTA");
		imagenSelected.setEstado(true);
		imagenSelected.setResuelto(false); 
		imagenSelected.setTipoTransaccion("DEPOSITO EN EFECTIVO");
		imagenSelected.setLote(null);
		imagenSelected.setPorRegularizar("NO");
		file=null;
		
		
		proyecto1=null;proyecto2=null;proyecto3=null;proyecto4=null;proyecto5=null;proyecto6=null;proyecto7=null;proyecto8=null;proyecto9=null;proyecto10=null;
		manzana1=null;manzana2=null;manzana3=null;manzana4=null;manzana5=null;manzana6=null;manzana7=null;manzana8=null;manzana9=null;manzana10=null;
		lote1=null;lote2=null;lote3=null;lote4=null;lote5=null;lote6=null;lote7=null;lote8=null;lote9=null;lote10=null;
		
		PrimeFaces.current().executeScript("PF('imagenDialog').show();");
		
	}
	
	public void listarManzanas(int num) {
		switch (num) {
		case 1:
			manzana1=null;
			lstLote1 = new ArrayList<>();
			if(proyecto1 != null) {
				lstManzana1 = manzanaService.findByProject(proyecto1.getId());
			}else {
				lstManzana1 = new ArrayList<>();
			}

		case 2:
			manzana2=null;
			lstLote2 = new ArrayList<>();
			if(proyecto2 != null) {
				lstManzana2 = manzanaService.findByProject(proyecto2.getId());
			}else {
				lstManzana2 = new ArrayList<>();
			}

		case 3:
			manzana3=null;
			lstLote3 = new ArrayList<>();
			if(proyecto3 != null) {
				lstManzana3 = manzanaService.findByProject(proyecto3.getId());
			}else {
				lstManzana3 = new ArrayList<>();
			}

		case 4:
			manzana4=null;
			lstLote4 = new ArrayList<>();
			if(proyecto4 != null) {
				lstManzana4 = manzanaService.findByProject(proyecto4.getId());
			}else {
				lstManzana4 = new ArrayList<>();
			}

		case 5:
			manzana5=null;
			lstLote5 = new ArrayList<>();
			if(proyecto5 != null) {
				lstManzana5 = manzanaService.findByProject(proyecto5.getId());
			}else {
				lstManzana5 = new ArrayList<>();
			}

		case 6:
			manzana6=null;
			lstLote6 = new ArrayList<>();
			if(proyecto6 != null) {
				lstManzana6 = manzanaService.findByProject(proyecto6.getId());
			}else {
				lstManzana6 = new ArrayList<>();
			}

		case 7:
			manzana7=null;
			lstLote7 = new ArrayList<>();
			if(proyecto7 != null) {
				lstManzana7 = manzanaService.findByProject(proyecto7.getId());
			}else {
				lstManzana7 = new ArrayList<>();
			}

		case 8:
			manzana8=null;
			lstLote8 = new ArrayList<>();
			if(proyecto8 != null) {
				lstManzana8 = manzanaService.findByProject(proyecto8.getId());
			}else {
				lstManzana8 = new ArrayList<>();
			}

		case 9:
			manzana9=null;
			lstLote9 = new ArrayList<>();
			if(proyecto9 != null) {
				lstManzana9 = manzanaService.findByProject(proyecto9.getId());
			}else {
				lstManzana9 = new ArrayList<>();
			}

		case 10:
			manzana10=null;
			lstLote10 = new ArrayList<>();
			if(proyecto10 != null) {
				lstManzana10 = manzanaService.findByProject(proyecto10.getId());
			}else {
				lstManzana10 = new ArrayList<>();
			}
			
		}
		
	}
	
	public void listarLotes(int num) {
		switch (num) {
		case 1:
			if(manzana1 != null) {
				lstLote1 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto1, manzana1, "%%");
			}else {
				lstLote1 = new ArrayList<>();
			}

		case 2:
			if(manzana2 != null) {
				lstLote2 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto2, manzana2, "%%");
			}else {
				lstLote2 = new ArrayList<>();
			}

		case 3:
			if(manzana3 != null) {
				lstLote3 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto3, manzana3, "%%");
			}else {
				lstLote3 = new ArrayList<>();
			}

		case 4:
			if(manzana4 != null) {
				lstLote4 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto4, manzana4, "%%");
			}else {
				lstLote4 = new ArrayList<>();
			}

		case 5:
			if(manzana5 != null) {
				lstLote5 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto5, manzana5, "%%");
			}else {
				lstLote5 = new ArrayList<>();
			}

		case 6:
			if(manzana6 != null) {
				lstLote6 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto6, manzana6, "%%");
			}else {
				lstLote6 = new ArrayList<>();
			}

		case 7:
			if(manzana7 != null) {
				lstLote7 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto7, manzana7, "%%");
			}else {
				lstLote7 = new ArrayList<>();
			}

		case 8:
			if(manzana8 != null) {
				lstLote8 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto8, manzana8, "%%");
			}else {
				lstLote8 = new ArrayList<>();
			}

		case 9:
			if(manzana9 != null) {
				lstLote9 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto9, manzana9, "%%");
			}else {
				lstLote9 = new ArrayList<>();
			}

		case 10:
			if(manzana10 != null) {
				lstLote10 = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(proyecto10, manzana10, "%%");
			}else {
				lstLote10 = new ArrayList<>();
			}
			
		}

	}
	
	public void procesarExcel() {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Reporte Voucher");

		CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
		CellStyle styleTitulo = UtilXls.styleCell(workbook, 'A');
		
		Row rowSubTitulo = sheet.createRow(0);
		Cell cellSub1 = null;
		cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue("FECHA Y HORA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue("MONTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue("NRO OPERACION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue("CUENTA BANCARIA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue("TIPO TRANSACCION");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue("BOLETA / FACTURA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue("COMENTARIO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue("TIPO PRODUCTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("PROYECTO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("MANZANA");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("LOTE");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue("USUARIO");cellSub1.setCellStyle(styleTitulo);
		cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue("SUCURSAL");cellSub1.setCellStyle(styleTitulo);


		fechaIni.setHours(0);
        fechaIni.setMinutes(0);
        fechaIni.setSeconds(0);
        fechaFin.setHours(23);
        fechaFin.setMinutes(59);
        fechaFin.setSeconds(59);
		
        List<Imagen> lstVoucher = new ArrayList<>();
        
        String sucursalName="%%";
        if(sucursal!=null) {
        	sucursalName="%"+sucursal.getRazonSocial()+"%";
        }
        
        if(ctaBanFilter != null) {
        	lstVoucher =  imagenService.findByEstadoAndCuentaBancariaAndFechaBetweenOrderByFechaDesc(estado, ctaBanFilter, fechaIni, fechaFin);

        }else {
        	lstVoucher =  imagenService.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndFechaBetweenOrderByFechaDesc(estado, sucursalName, fechaIni, fechaFin);
        }
        
		
		int index = 1;
		BigDecimal total = BigDecimal.ZERO;
		
		for (Imagen imagen : lstVoucher) {
//			List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(d, true);	
			
			rowSubTitulo = sheet.createRow(index);
			cellSub1 = rowSubTitulo.createCell(0);cellSub1.setCellValue(convertirHora(imagen.getFecha()));cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(imagen.getMonto().doubleValue());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(2);cellSub1.setCellValue(imagen.getNumeroOperacion());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(3);cellSub1.setCellValue(imagen.getCuentaBancaria().getBanco().getNombre()+ " : " +imagen.getCuentaBancaria().getNumero()+" ("+ (imagen.getCuentaBancaria().getMoneda().equals("S") ? "SOLES)": "DÓLARES)"));cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(4);cellSub1.setCellValue(imagen.getTipoTransaccion());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(5);cellSub1.setCellValue(imagen.getDocumentoVenta()==null?"": imagen.getDocumentoVenta().getSerie()+"-"+imagen.getDocumentoVenta().getNumero());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(6);cellSub1.setCellValue(imagen.getDocumentoVenta()==null? imagen.getComentario() : obtenerDetalleBoleta(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);
			
			cellSub1 = rowSubTitulo.createCell(7);cellSub1.setCellValue(imagen.getDocumentoVenta()==null?"": obtenerDetalleBoletaTipoProducto(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);

			if(imagen.getDocumentoVenta()!=null) {
				cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(obtenerDetalleBoletaProyecto(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(obtenerDetalleBoletaManzana(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue(obtenerDetalleBoletaLote(imagen.getDocumentoVenta()));cellSub1.setCellStyle(styleBorder);
				
			}else if(imagen.getLote()!=null){
				
				cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue(imagen.getLote().getProject().getName());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue(imagen.getLote().getManzana().getName());cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue(imagen.getLote().getNumberLote());cellSub1.setCellStyle(styleBorder);
				
			}else {
				cellSub1 = rowSubTitulo.createCell(8);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(9);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				cellSub1 = rowSubTitulo.createCell(10);cellSub1.setCellValue("");cellSub1.setCellStyle(styleBorder);
				
			}
			
			
			cellSub1 = rowSubTitulo.createCell(11);cellSub1.setCellValue(imagen.getUsuario()==null?"": imagen.getUsuario().getUsername());cellSub1.setCellStyle(styleBorder);
			cellSub1 = rowSubTitulo.createCell(12);cellSub1.setCellValue(imagen.getCuentaBancaria().getSucursal().getRazonSocial());cellSub1.setCellStyle(styleBorder);
			
			total = total.add(imagen.getMonto());
			
			index++;
		}
		
		
		rowSubTitulo = sheet.createRow(index);
		cellSub1 = rowSubTitulo.createCell(1);cellSub1.setCellValue(total.doubleValue());cellSub1.setCellStyle(styleBorder);
		
		
		for (int j = 0; j <= 12; j++) {
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
	
	public String obtenerDetalleBoleta(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			detalle = detalle + det.getDescripcion()+".";
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleLote(Imagen imagen) {
		String detalle="";
		List<DetalleImagenLote> lstDet = detalleImagenLoteService.findByEstadoAndImagen(true, imagen);
		
		int cont = 0;
		for(DetalleImagenLote det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			detalle = detalle + det.getLote().getProject().getName()+", MZ "+ det.getLote().getManzana().getName()+", LT " + det.getLote().getNumberLote()+" ** ";
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleBoletaProyecto(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		List<String> lstProyectos = new ArrayList<>();
		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			if(det.getCuota()!=null) {
				String nombre =  det.getCuota().getContrato().getLote().getProject().getName();
				if(lstProyectos.isEmpty()) {
					lstProyectos.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstProyectos, nombre);
					if(!busqueda) {
						lstProyectos.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getRequerimientoSeparacion()!=null) {
				String nombre =  det.getRequerimientoSeparacion().getLote().getProject().getName();
				if(lstProyectos.isEmpty()) {
					lstProyectos.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstProyectos, nombre);
					if(!busqueda) {
						lstProyectos.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getCuotaPrepago()!=null) {			
				String nombre =  det.getCuotaPrepago().getContrato().getLote().getProject().getName();
				if(lstProyectos.isEmpty()) {
					lstProyectos.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstProyectos, nombre);
					if(!busqueda) {
						lstProyectos.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleBoletaManzana(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		List<String> lstManzana = new ArrayList<>();
		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			if(det.getCuota()!=null) {
				String nombre =  det.getCuota().getContrato().getLote().getManzana().getName();
				if(lstManzana.isEmpty()) {
					lstManzana.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstManzana, nombre);
					if(!busqueda) {
						lstManzana.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getRequerimientoSeparacion()!=null) {
				String nombre =  det.getRequerimientoSeparacion().getLote().getManzana().getName();
				if(lstManzana.isEmpty()) {
					lstManzana.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstManzana, nombre);
					if(!busqueda) {
						lstManzana.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getCuotaPrepago()!=null) {
				String nombre =  det.getCuotaPrepago().getContrato().getLote().getManzana().getName();
				if(lstManzana.isEmpty()) {
					lstManzana.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstManzana, nombre);
					if(!busqueda) {
						lstManzana.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}			
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleBoletaLote(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		List<String> lstLote = new ArrayList<>();

		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			if(det.getCuota()!=null) {
				String nombre =  det.getCuota().getContrato().getLote().getNumberLote();
				if(lstLote.isEmpty()) {
					lstLote.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstLote, nombre);
					if(!busqueda) {
						lstLote.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			if(det.getRequerimientoSeparacion()!=null) {
				String nombre =  det.getRequerimientoSeparacion().getLote().getNumberLote();
				if(lstLote.isEmpty()) {
					lstLote.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstLote, nombre);
					if(!busqueda) {
						lstLote.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			
			if(det.getCuotaPrepago()!=null) {
				String nombre =  det.getCuotaPrepago().getContrato().getLote().getNumberLote();
				if(lstLote.isEmpty()) {
					lstLote.add(nombre);
					detalle = detalle + nombre;
				}else {
					boolean busqueda = buscarPalabra(lstLote, nombre);
					if(!busqueda) {
						lstLote.add(nombre);
						detalle = detalle + nombre;
					}
				}
			}
			cont++;
		}
		
		
		return detalle;
	}
	
	public String obtenerDetalleBoletaTipoProducto(DocumentoVenta documentoVenta) {
		String detalle="";
		List<DetalleDocumentoVenta> lstDet = detalleDocumentoVentaService.findByDocumentoVentaAndEstado(documentoVenta, true);
		List<String> lstProducto = new ArrayList<>();

		
		int cont = 0;
		for(DetalleDocumentoVenta det: lstDet) {
			if(cont !=0) {
				detalle = detalle+ " \n";
			}
			
			String nombre =  det.getProducto().getDescripcion();
			if(lstProducto.isEmpty()) {
				lstProducto.add(nombre);
				detalle = detalle + nombre;
			}else {
				boolean busqueda = buscarPalabra(lstProducto, nombre);
				if(!busqueda) {
					lstProducto.add(nombre);
					detalle = detalle + nombre;
				}
			}
			cont++;
		}
		
		
		return detalle;
	}
	
	public boolean buscarPalabra(List<String> lstPalabra, String busqueda) {
		boolean encuentra = false;
		for(String nom : lstPalabra) {
			if(nom.equals(busqueda)) {
				encuentra= true;
			}
		}
		return encuentra;
	}
	
	public void saveVoucher() {
//		if(imagenSelected.getFecha() == null) {
//			addErrorMessage("Debe ingresar una fecha.");
//			return;
//		}
		if(imagenSelected.getMonto() == null) {
			addErrorMessage("Debe ingresar un monto.");
			return;
		}else {
			if(imagenSelected.getMonto().compareTo(BigDecimal.ZERO) < 1) {
				addErrorMessage("El monto debe ser mayor que 0.");
				return;
			}
		}
		if(imagenSelected.getNumeroOperacion().equals("")) {
			addErrorMessage("Debe ingresar número de operación.");
			return;
		}
		
		if(imagenSelected.getCuentaBancaria()==null) {
			addErrorMessage("Debe seleccionar una cuenta bancaria.");
			return;
		}
		
		
		imagenSelected.setUsuario(navegacionBean.getUsuarioLogin());
		imagenSelected.setFechaRegistro(new Date());
		
		
		if(imagenSelected.getId() ==null) {
			if(imagenSelected.getFecha()!=null) {
				List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancaria(true, imagenSelected.getFecha(), imagenSelected.getMonto(), imagenSelected.getNumeroOperacion(), imagenSelected.getCuentaBancaria());
				if(!buscarImagen.isEmpty()) {
					addErrorMessage("Ya existe el voucher");
					return;
				}
			}else {
				List<Imagen> buscarImagen = imagenService.findByEstadoAndMontoAndNumeroOperacionAndCuentaBancaria(true, imagenSelected.getMonto(), imagenSelected.getNumeroOperacion(), imagenSelected.getCuentaBancaria());
				if(!buscarImagen.isEmpty()) {
					addErrorMessage("Ya existe el voucher");
					return;
				}
			}	
			
		}else {
			if(imagenSelected.getFecha()!=null) {
				String fecha = sdfFull2.format(imagenSelected.getFecha());
				
				List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(true,fecha , imagenSelected.getMonto(), imagenSelected.getNumeroOperacion(), imagenSelected.getCuentaBancaria().getId(), imagenSelected.getId());
				if(!buscarImagen.isEmpty()) {
					addErrorMessage("Ya existe el voucher");
					return;
				}
			}else {
				List<Imagen> buscarImagen = imagenService.findByEstadoAndMontoAndNumeroOperacionAndCuentaBancariaAndIdException(true , imagenSelected.getMonto(), imagenSelected.getNumeroOperacion(), imagenSelected.getCuentaBancaria().getId(), imagenSelected.getId());
				if(!buscarImagen.isEmpty()) {
					addErrorMessage("Ya existe el voucher");
					return;
				}
			}
		}
		
		List<Lote> lstLotes = new ArrayList<>();
		if(lote1 !=null) {
			lstLotes.add(lote1);
		}
		if(lote2 !=null) {
			lstLotes.add(lote2);
		}
		if(lote3 !=null) {
			lstLotes.add(lote3);
		}
		if(lote4 !=null) {
			lstLotes.add(lote4);
		}
		if(lote5 !=null) {
			lstLotes.add(lote5);
		}
		if(lote6 !=null) {
			lstLotes.add(lote6);
		}
		if(lote7 !=null) {
			lstLotes.add(lote7);
		}
		if(lote8 !=null) {
			lstLotes.add(lote8);
		}
		if(lote9 !=null) {
			lstLotes.add(lote9);
		}
		if(lote10 !=null) {
			lstLotes.add(lote10);
		}
		
		
		if(imagenSelected.getDocumentoVenta()!=null) {
			if(imagenSelected.getDocumentoVenta().getSucursal().getId().equals(imagenSelected.getCuentaBancaria().getSucursal().getId())) {
				imagenSelected.setPorRegularizar("NO");
			}else {
				imagenSelected.setPorRegularizar("SI");
			}
			
		}
		
		Imagen imagen = imagenService.save(imagenSelected, lstLotes);
//		Integer id = imagenSelected.getId();
		if(imagen != null) {
			if(file!=null) {
				if(imagen.getNombre().equals("-")) { 
					imagen.setNombre(imagen.getId()+ "." + getExtension(file.getFileName()));
					imagenService.save(imagen);
					subirArchivo(imagen.getNombre(), file);
				}else {
					
					String str = imagen.getNombre();
			        String[] parts = str.split("\\.");
					String part1 = parts[0]; // 123
					imagen.setNombre(part1+ "." + getExtension(file.getFileName()));
					imagenService.save(imagen);
					subirArchivo(imagen.getNombre(), file);
				}
				
			}
			
			addInfoMessage("Se registró correctamente el voucher.");
			PrimeFaces.current().executeScript("PF('imagenDialog').hide();"); 

		}else {
			addErrorMessage("No se pudo guardar el voucher"); 
		}
	}
	
	public void subirArchivo(String nombre, UploadedFile file) {
		//  File result = new File("/home/imagen/IMG-DOCUMENTO-VENTA/" + nombre);
		//  File result = new File("C:\\IMG-DOCUMENTO-VENTA\\" + nombre);
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
	
	public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
	
	public void listarCuentaBancaria() {
		lstCuentaBancaria = new ArrayList<>();
		
		if(sucursalDialog!= null) {
			lstCuentaBancaria=cuentaBancariaService.findByEstadoAndSucursal(true, sucursalDialog);
			if(!lstCuentaBancaria.isEmpty()) {
				imagenSelected.setCuentaBancaria(lstCuentaBancaria.get(0));
			}
		}
		
	}
	
	public void listarCuentaBancariaFilter() {
		lstCuentaBancariaFilter = new ArrayList<>();
		
		ctaBanFilter = null;
		
		if(sucursal != null) {
			lstCuentaBancariaFilter=cuentaBancariaService.findByEstado(true);
		}
		
	}
	
	public void anularVoucher() {
		imagenSelected.setEstado(false);
		imagenService.save(imagenSelected);
		addInfoMessage("Se anuló correctamente el voucher.");
	}
	
	public String convertirHora(Date hora) {
		String a = "";
		if(hora != null) {
			a = sdfFull.format(hora);
		}
		
		return a;
	}
	
	public void iniciarLazy() {

		lstImagenLazy = new LazyDataModel<Imagen>() {
			private List<Imagen> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Imagen getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Imagen i : datasource) {
                    if (i.getId() == intRowKey) {
                        return i;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Imagen imagen) {
                return String.valueOf(imagen.getId());
            }

			@Override
			public List<Imagen> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String monto = filterBy.get("monto") != null ? filterBy.get("monto").getFilterValue().toString().trim().replaceAll(" ", "%") : "";
				BigDecimal montoBigDecimal = BigDecimal.ZERO;
				if(!monto.equals("")) {
					montoBigDecimal = new BigDecimal(monto);
				}
				
				String numeroOp = "%" + (filterBy.get("numeroOperacion") != null ? filterBy.get("numeroOperacion").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String tipoTransaccion = "%" + (filterBy.get("tipoTransaccion") != null ? filterBy.get("tipoTransaccion").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String comentario = "%" + (filterBy.get("comentario") != null ? filterBy.get("comentario").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
				
				
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
               
                Page<Imagen> page=null;
                String sucursalName="%%";
                if(sucursal!=null) {
                	sucursalName="%"+sucursal.getRazonSocial()+"%";
                }
                
                String numCuenta="%%";
                if(ctaBanFilter!=null) {
                	numCuenta="%"+ctaBanFilter.getNumero()+"%"; 
                }
               
                fechaIni.setHours(0);
                fechaIni.setMinutes(0);
                fechaIni.setSeconds(0);
                fechaFin.setHours(23);
                fechaFin.setMinutes(59);
                fechaFin.setSeconds(59);
                
                if(busquedaSinFecha) {
                	if(montoBigDecimal.compareTo(BigDecimal.ZERO)==0) {
			    		page =  imagenService.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeAndFechaIsNullOrderByIdDesc
	                			(estado, sucursalName, numCuenta, porRegularizar, numeroOp, tipoTransaccion, comentario, pageable);
	                    
			    	}else {
			    		page =  imagenService.findByEstadoAndMontoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeAndFechaIsNullOrderByIdDesc
	                			(estado, montoBigDecimal, sucursalName, numCuenta, porRegularizar, numeroOp, tipoTransaccion, comentario, pageable);
			    	}
                }else {
                	
                
			    	if(montoBigDecimal.compareTo(BigDecimal.ZERO)==0) {
			    		page =  imagenService.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndFechaBetweenAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeOrderByIdDesc
	                			(estado, sucursalName, numCuenta, fechaIni, fechaFin, porRegularizar, numeroOp, tipoTransaccion, comentario, pageable);
	                    
			    	}else {
			    		page =  imagenService.findByEstadoAndMontoAndCuentaBancariaSucursalRazonSocialLikeAndCuentaBancariaNumeroLikeAndFechaBetweenAndPorRegularizarLikeAndNumeroOperacionLikeAndTipoTransaccionLikeAndComentarioLikeOrderByIdDesc
	                			(estado, montoBigDecimal, sucursalName, numCuenta, fechaIni, fechaFin, porRegularizar, numeroOp, tipoTransaccion, comentario, pageable);
			    	}
				    	
				  
                }
                
            	
                
                setRowCount((int) page.getTotalElements());
                return datasource = page.getContent();
            }
		};
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
	
	public Converter getConversorCuentaBancariaFilter() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	CuentaBancaria c = null;
                    for (CuentaBancaria si : lstCuentaBancariaFilter) {
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
	
	public Converter getConversorManzana(List<Manzana> lstManzana) {
		if(lstManzana != null) {
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
		}else {
			return null;
		}
       
    }
	
	public Converter getConversorLote(List<Lote> lstLote) {
		if(lstLote != null) {
			return new Converter() {
	            @Override
	            public Object getAsObject(FacesContext context, UIComponent component, String value) {
	                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
	                    return null;
	                } else {
	                	Lote c = null;
	                    for (Lote si : lstLote) {
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
		}else{
			return null;
		}
        
    }
	

	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
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
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
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
	public Imagen getImagenSelected() {
		return imagenSelected;
	}
	public void setImagenSelected(Imagen imagenSelected) {
		this.imagenSelected = imagenSelected;
	}
	public LazyDataModel<Imagen> getLstImagenLazy() {
		return lstImagenLazy;
	}
	public void setLstImagenLazy(LazyDataModel<Imagen> lstImagenLazy) {
		this.lstImagenLazy = lstImagenLazy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public Sucursal getSucursalDialog() {
		return sucursalDialog;
	}
	public void setSucursalDialog(Sucursal sucursalDialog) {
		this.sucursalDialog = sucursalDialog;
	}
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public DetalleDocumentoVentaService getDetalleDocumentoVentaService() {
		return detalleDocumentoVentaService;
	}
	public void setDetalleDocumentoVentaService(DetalleDocumentoVentaService detalleDocumentoVentaService) {
		this.detalleDocumentoVentaService = detalleDocumentoVentaService;
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
	public CuentaBancaria getCtaBanFilter() {
		return ctaBanFilter;
	}
	public void setCtaBanFilter(CuentaBancaria ctaBanFilter) {
		this.ctaBanFilter = ctaBanFilter;
	}
	public List<CuentaBancaria> getLstCuentaBancariaFilter() {
		return lstCuentaBancariaFilter;
	}
	public void setLstCuentaBancariaFilter(List<CuentaBancaria> lstCuentaBancariaFilter) {
		this.lstCuentaBancariaFilter = lstCuentaBancariaFilter;
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
	public List<Project> getLstProject() {
		return lstProject;
	}
	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}
	public UploadedFile getFile() {
		return file;
	}
	public void setFile(UploadedFile file) {
		this.file = file;
	}
	public LoadImageDocumentoBean getLoadImageDocumentoBean() {
		return loadImageDocumentoBean;
	}
	public void setLoadImageDocumentoBean(LoadImageDocumentoBean loadImageDocumentoBean) {
		this.loadImageDocumentoBean = loadImageDocumentoBean;
	}
	public String getPorRegularizar() {
		return porRegularizar;
	}
	public void setPorRegularizar(String porRegularizar) {
		this.porRegularizar = porRegularizar;
	}
	public SimpleDateFormat getSdfFull2() {
		return sdfFull2;
	}
	public void setSdfFull2(SimpleDateFormat sdfFull2) {
		this.sdfFull2 = sdfFull2;
	}
	public Project getProyecto1() {
		return proyecto1;
	}
	public void setProyecto1(Project proyecto1) {
		this.proyecto1 = proyecto1;
	}
	public Project getProyecto2() {
		return proyecto2;
	}
	public void setProyecto2(Project proyecto2) {
		this.proyecto2 = proyecto2;
	}
	
	public Project getProyecto3() {
		return proyecto3;
	}
	public void setProyecto3(Project proyecto3) {
		this.proyecto3 = proyecto3;
	}
	public Project getProyecto4() {
		return proyecto4;
	}
	public void setProyecto4(Project proyecto4) {
		this.proyecto4 = proyecto4;
	}
	public Project getProyecto5() {
		return proyecto5;
	}
	public void setProyecto5(Project proyecto5) {
		this.proyecto5 = proyecto5;
	}
	public Project getProyecto6() {
		return proyecto6;
	}
	public void setProyecto6(Project proyecto6) {
		this.proyecto6 = proyecto6;
	}
	public Project getProyecto7() {
		return proyecto7;
	}
	public void setProyecto7(Project proyecto7) {
		this.proyecto7 = proyecto7;
	}
	public Project getProyecto8() {
		return proyecto8;
	}
	public void setProyecto8(Project proyecto8) {
		this.proyecto8 = proyecto8;
	}
	public Project getProyecto9() {
		return proyecto9;
	}
	public void setProyecto9(Project proyecto9) {
		this.proyecto9 = proyecto9;
	}
	public Project getProyecto10() {
		return proyecto10;
	}
	public void setProyecto10(Project proyecto10) {
		this.proyecto10 = proyecto10;
	}
	public Manzana getManzana1() {
		return manzana1;
	}
	public void setManzana1(Manzana manzana1) {
		this.manzana1 = manzana1;
	}
	public Manzana getManzana2() {
		return manzana2;
	}
	public void setManzana2(Manzana manzana2) {
		this.manzana2 = manzana2;
	}
	public Manzana getManzana3() {
		return manzana3;
	}
	public void setManzana3(Manzana manzana3) {
		this.manzana3 = manzana3;
	}
	public Manzana getManzana4() {
		return manzana4;
	}
	public void setManzana4(Manzana manzana4) {
		this.manzana4 = manzana4;
	}
	public Manzana getManzana5() {
		return manzana5;
	}
	public void setManzana5(Manzana manzana5) {
		this.manzana5 = manzana5;
	}
	public Manzana getManzana6() {
		return manzana6;
	}
	public void setManzana6(Manzana manzana6) {
		this.manzana6 = manzana6;
	}
	public Manzana getManzana7() {
		return manzana7;
	}
	public void setManzana7(Manzana manzana7) {
		this.manzana7 = manzana7;
	}
	public Manzana getManzana8() {
		return manzana8;
	}
	public void setManzana8(Manzana manzana8) {
		this.manzana8 = manzana8;
	}
	public Manzana getManzana9() {
		return manzana9;
	}
	public void setManzana9(Manzana manzana9) {
		this.manzana9 = manzana9;
	}
	public Manzana getManzana10() {
		return manzana10;
	}
	public void setManzana10(Manzana manzana10) {
		this.manzana10 = manzana10;
	}
	public Lote getLote1() {
		return lote1;
	}
	public void setLote1(Lote lote1) {
		this.lote1 = lote1;
	}
	public Lote getLote2() {
		return lote2;
	}
	public void setLote2(Lote lote2) {
		this.lote2 = lote2;
	}
	public Lote getLote3() {
		return lote3;
	}
	public void setLote3(Lote lote3) {
		this.lote3 = lote3;
	}
	public Lote getLote4() {
		return lote4;
	}
	public void setLote4(Lote lote4) {
		this.lote4 = lote4;
	}
	public Lote getLote5() {
		return lote5;
	}
	public void setLote5(Lote lote5) {
		this.lote5 = lote5;
	}
	public Lote getLote6() {
		return lote6;
	}
	public void setLote6(Lote lote6) {
		this.lote6 = lote6;
	}
	public Lote getLote7() {
		return lote7;
	}
	public void setLote7(Lote lote7) {
		this.lote7 = lote7;
	}
	public Lote getLote8() {
		return lote8;
	}
	public void setLote8(Lote lote8) {
		this.lote8 = lote8;
	}
	public Lote getLote9() {
		return lote9;
	}
	public void setLote9(Lote lote9) {
		this.lote9 = lote9;
	}
	public Lote getLote10() {
		return lote10;
	}
	public void setLote10(Lote lote10) {
		this.lote10 = lote10;
	}
	public List<Manzana> getLstManzana1() {
		return lstManzana1;
	}
	public void setLstManzana1(List<Manzana> lstManzana1) {
		this.lstManzana1 = lstManzana1;
	}
	public List<Manzana> getLstManzana2() {
		return lstManzana2;
	}
	public void setLstManzana2(List<Manzana> lstManzana2) {
		this.lstManzana2 = lstManzana2;
	}
	public List<Manzana> getLstManzana3() {
		return lstManzana3;
	}
	public void setLstManzana3(List<Manzana> lstManzana3) {
		this.lstManzana3 = lstManzana3;
	}
	public List<Manzana> getLstManzana4() {
		return lstManzana4;
	}
	public void setLstManzana4(List<Manzana> lstManzana4) {
		this.lstManzana4 = lstManzana4;
	}
	public List<Manzana> getLstManzana5() {
		return lstManzana5;
	}
	public void setLstManzana5(List<Manzana> lstManzana5) {
		this.lstManzana5 = lstManzana5;
	}
	public List<Manzana> getLstManzana6() {
		return lstManzana6;
	}
	public void setLstManzana6(List<Manzana> lstManzana6) {
		this.lstManzana6 = lstManzana6;
	}
	public List<Manzana> getLstManzana7() {
		return lstManzana7;
	}
	public void setLstManzana7(List<Manzana> lstManzana7) {
		this.lstManzana7 = lstManzana7;
	}
	public List<Manzana> getLstManzana8() {
		return lstManzana8;
	}
	public void setLstManzana8(List<Manzana> lstManzana8) {
		this.lstManzana8 = lstManzana8;
	}
	public List<Manzana> getLstManzana9() {
		return lstManzana9;
	}
	public void setLstManzana9(List<Manzana> lstManzana9) {
		this.lstManzana9 = lstManzana9;
	}
	public List<Manzana> getLstManzana10() {
		return lstManzana10;
	}
	public void setLstManzana10(List<Manzana> lstManzana10) {
		this.lstManzana10 = lstManzana10;
	}
	public List<Lote> getLstLote1() {
		return lstLote1;
	}
	public void setLstLote1(List<Lote> lstLote1) {
		this.lstLote1 = lstLote1;
	}
	public List<Lote> getLstLote2() {
		return lstLote2;
	}
	public void setLstLote2(List<Lote> lstLote2) {
		this.lstLote2 = lstLote2;
	}
	public List<Lote> getLstLote3() {
		return lstLote3;
	}
	public void setLstLote3(List<Lote> lstLote3) {
		this.lstLote3 = lstLote3;
	}
	public List<Lote> getLstLote4() {
		return lstLote4;
	}
	public void setLstLote4(List<Lote> lstLote4) {
		this.lstLote4 = lstLote4;
	}
	public List<Lote> getLstLote5() {
		return lstLote5;
	}
	public void setLstLote5(List<Lote> lstLote5) {
		this.lstLote5 = lstLote5;
	}
	public List<Lote> getLstLote6() {
		return lstLote6;
	}
	public void setLstLote6(List<Lote> lstLote6) {
		this.lstLote6 = lstLote6;
	}
	public List<Lote> getLstLote7() {
		return lstLote7;
	}
	public void setLstLote7(List<Lote> lstLote7) {
		this.lstLote7 = lstLote7;
	}
	public List<Lote> getLstLote8() {
		return lstLote8;
	}
	public void setLstLote8(List<Lote> lstLote8) {
		this.lstLote8 = lstLote8;
	}
	public List<Lote> getLstLote9() {
		return lstLote9;
	}
	public void setLstLote9(List<Lote> lstLote9) {
		this.lstLote9 = lstLote9;
	}
	public List<Lote> getLstLote10() {
		return lstLote10;
	}
	public void setLstLote10(List<Lote> lstLote10) {
		this.lstLote10 = lstLote10;
	}
	public DetalleImagenLoteService getDetalleImagenLoteService() {
		return detalleImagenLoteService;
	}
	public void setDetalleImagenLoteService(DetalleImagenLoteService detalleImagenLoteService) {
		this.detalleImagenLoteService = detalleImagenLoteService;
	}

	public boolean isValidarExistenciaImagen() {
		return validarExistenciaImagen;
	}

	public void setValidarExistenciaImagen(boolean validarExistenciaImagen) {
		this.validarExistenciaImagen = validarExistenciaImagen;
	}

	public boolean isBusquedaSinFecha() {
		return busquedaSinFecha;
	}

	public void setBusquedaSinFecha(boolean busquedaSinFecha) {
		this.busquedaSinFecha = busquedaSinFecha;
	}

	
	
	
		
}
