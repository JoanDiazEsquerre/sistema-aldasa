package com.model.aldasa.prospeccion.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.servlet.ServletContext;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.model.aldasa.entity.Action;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ActionService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.EstadoProspeccion;
import com.model.aldasa.util.Perfiles;
import com.model.aldasa.util.UtilXls;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@ManagedBean
@ViewScoped
public class ReporteProspeccionBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(value = "#{prospectService}")
	private ProspectService prospectService;
	
	@ManagedProperty(value = "#{actionService}")
	private ActionService actionService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{prospectionDetailService}")
	private ProspectionDetailService prospectionDetailService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	private Usuario usuarioLogin = new Usuario();
	
	private List<SelectItem> countriesGroup;
	private List<Person> lstPersonAssessor;
	private List<Action> lstActions;
	private List<Project> lstProject;
	private List<ProspectionDetail> lstProspectionDetailReporte;
	
	private Date fechaIni,fechaFin;
	private String origenContactoSelected,prospectSurnames="";
	private String nombreArchivo = "Reporte de Acciones.xlsx";
	
	private boolean permisoSepararVender = false;
	
	private Prospect prospectSelected;
	private Person personAssessorSelected;
	private Action actionSelected;
	private Project projectSelected;
	private ProspectionDetail prospectionDetailSelected;
	
	private StreamedContent fileDes;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
	
	@PostConstruct
	public void init() {
		usuarioLogin = usuarioService.findByUsername(navegacionBean.getUsername());
		listarPersonasAssessor();
		
		fechaIni = new Date() ;
		fechaFin = new Date() ;

		countriesGroup = new ArrayList<>();
        SelectItemGroup europeCountries = new SelectItemGroup("Redes Sociales");
        europeCountries.setSelectItems(new SelectItem[]{
            new SelectItem("WhatsAap", "WhatsApp"),
            new SelectItem("Facebook", "Facebook"),
            new SelectItem("Instagram", "Instagram"),
            new SelectItem("Google/SEM", "Google/SEM"),
            new SelectItem("Google/SEO", "Google/SEO")
        });
        countriesGroup.add(europeCountries);
        
        listarActions();
        listarProject();
        
        if(usuarioLogin.getProfile().getId() == Perfiles.ADMINISTRADOR.getId() || usuarioLogin.getProfile().getId() == Perfiles.ASISTENTE_ADMINISTRATIVO.getId()) {
        	permisoSepararVender = true;
        }
	}
	
	public void separarLote() {
		Lote lote = loteService.findById(prospectionDetailSelected.getLote().getId());
		if(lote.getStatus().equals(EstadoLote.SEPARADO.getName())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se completó la accion, el lote se encuentra separado.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}else if(lote.getStatus().equals(EstadoLote.VENDIDO.getName())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se completó la accion, el lote se encuentra Vendido.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}else if(lote.getStatus().equals(EstadoLote.INACTIVO.getName())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se completó la accion, el lote se encuentra Inactivo.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}else {
			lote.setStatus(EstadoLote.SEPARADO.getName());
			lote.setFechaSeparacion(new Date());
			lote.setFechaVencimiento(sumarDiasAFecha(new Date(), 7));
			lote.setPersonVenta(prospectionDetailSelected.getProspection().getProspect().getPerson());
			lote.setPersonAssessor(prospectionDetailSelected.getProspection().getPersonAssessor());
			lote.setPersonSupervisor(prospectionDetailSelected.getProspection().getPersonSupervisor());
			
			lote = loteService.save(lote);
			if(lote != null) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", "Lote separado correctamente");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo separar el lote");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
	}
	
	public void venderLote() {
		Lote lote = loteService.findById(prospectionDetailSelected.getLote().getId());
		
		if(lote.getStatus().equals(EstadoLote.VENDIDO.getName())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se completó la accion, el lote se encuentra Vendido.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}else if(lote.getStatus().equals(EstadoLote.INACTIVO.getName())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se completó la accion, el lote se encuentra Inactivo.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		} else if(lote.getStatus().equals(EstadoLote.SEPARADO.getName()) && lote.getPersonVenta().getId() != prospectionDetailSelected.getProspection().getProspect().getPerson().getId()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se completó la accion, el lote se encuentra Separado por otra persona.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}else {
			lote.setStatus(EstadoLote.VENDIDO.getName());
			lote.setFechaVendido(prospectionDetailSelected.getDate());
			lote.setPersonVenta(prospectionDetailSelected.getProspection().getProspect().getPerson());
			
			lote = loteService.save(lote);
			if(lote != null) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", "Lote pasó a vendido correctamente");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo vender el lote");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
	}
	
	public Date sumarDiasAFecha(Date fecha, int dias){
	      if (dias==0) return fecha;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      return calendar.getTime(); 
	}
	
	
	public void procesarExcel() {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Acciones");

        CellStyle styleBorder = UtilXls.styleCell(workbook, 'B');
        CellStyle styleTitulo =UtilXls.styleCell(workbook,'A');
        //CellStyle styleSumaTotal = UtilsXls.styleCell(workbook,'Z');

//        Row rowTituloHoja = sheet.createRow(0);
//        Cell cellTituloHoja = rowTituloHoja.createCell(0);
//        cellTituloHoja.setCellValue("Reporte de Acciones");
//        cellTituloHoja.setCellStyle(styleBorder);
//        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11)); //combinar Celdas para titulo

        Row rowSubTitulo = sheet.createRow(0);
        Cell cellSubIndex = rowSubTitulo.createCell(0);cellSubIndex.setCellValue("PROSPECTO");cellSubIndex.setCellStyle(styleTitulo);
        Cell cellSubDoc = rowSubTitulo.createCell(1);cellSubDoc.setCellValue("DNI PROSPECTO");cellSubDoc.setCellStyle(styleTitulo);
        Cell cellSubSerie = rowSubTitulo.createCell(2);cellSubSerie.setCellValue("TELEFONO");cellSubSerie.setCellStyle(styleTitulo);
        Cell cellSubFechaEmision = rowSubTitulo.createCell(3);cellSubFechaEmision.setCellValue("CELULAR");cellSubFechaEmision.setCellStyle(styleTitulo);
        Cell cellSubFechaVen = rowSubTitulo.createCell(4);cellSubFechaVen.setCellValue("OCUPACION");cellSubFechaVen.setCellStyle(styleTitulo);
        Cell cellSubTipo = rowSubTitulo.createCell(5);cellSubTipo.setCellValue("ACCION");cellSubTipo.setCellStyle(styleTitulo);
        Cell cellSubTipoProv = rowSubTitulo.createCell(6);cellSubTipoProv.setCellValue("FECHA Y HORA");cellSubTipoProv.setCellStyle(styleTitulo);
        Cell cellSubMoneda = rowSubTitulo.createCell(7);cellSubMoneda.setCellValue("ASESOR");cellSubMoneda.setCellStyle(styleTitulo);
        Cell cellSubTotal = rowSubTitulo.createCell(8);cellSubTotal.setCellValue("SUPERVISOR");cellSubTotal.setCellStyle(styleTitulo);
        Cell cellSubNeto = rowSubTitulo.createCell(9);cellSubNeto.setCellValue("ORIGEN CONTACTO");cellSubNeto.setCellStyle(styleTitulo);
        Cell cellSubCondicion = rowSubTitulo.createCell(10);cellSubCondicion.setCellValue("PROYECTO");cellSubCondicion.setCellStyle(styleTitulo);
        Cell cellSubTrans = rowSubTitulo.createCell(11);cellSubTrans.setCellValue("RESULTADO");cellSubTrans.setCellStyle(styleTitulo);
      
        if(lstProspectionDetailReporte != null) {
        	int index = 1;
        	for(ProspectionDetail detail :lstProspectionDetailReporte) {
        		Row rowDetail = sheet.createRow(index);
        	    Cell cellNomPros = rowDetail.createCell(0);cellNomPros.setCellValue(detail.getProspection().getProspect().getPerson().getSurnames()+" "+detail.getProspection().getProspect().getPerson().getNames());cellNomPros.setCellStyle(styleBorder);
        	    Cell cellDniPros = rowDetail.createCell(1);cellDniPros.setCellValue(detail.getProspection().getProspect().getPerson().getDni());cellDniPros.setCellStyle(styleBorder);
        	    Cell cellTlf = rowDetail.createCell(2);cellTlf.setCellValue(detail.getProspection().getProspect().getPerson().getPhone());cellTlf.setCellStyle(styleBorder);
        	    Cell cellCellPhone = rowDetail.createCell(3);cellCellPhone.setCellValue(detail.getProspection().getProspect().getPerson().getCellphone());cellCellPhone.setCellStyle(styleBorder);
        	    Cell cellOccu = rowDetail.createCell(4);cellOccu.setCellValue(detail.getProspection().getProspect().getPerson().getOccupation());cellOccu.setCellStyle(styleBorder);
        	    Cell cellAction = rowDetail.createCell(5);cellAction.setCellValue(detail.getAction().getDescription());cellAction.setCellStyle(styleBorder);
        	    Cell cellDate = rowDetail.createCell(6);cellDate.setCellValue(sdfFull.format(detail.getDate()));cellDate.setCellStyle(styleBorder);
        	    Cell cellAse = rowDetail.createCell(7);cellAse.setCellValue(detail.getProspection().getPersonAssessor().getSurnames()+" "+detail.getProspection().getPersonAssessor().getNames());cellAse.setCellStyle(styleBorder);
        	    Cell cellSup = rowDetail.createCell(8);cellSup.setCellValue(detail.getProspection().getPersonSupervisor().getSurnames()+" "+detail.getProspection().getPersonSupervisor().getNames());cellSup.setCellStyle(styleBorder);
        	    Cell cellOri = rowDetail.createCell(9);cellOri.setCellValue(detail.getProspection().getOriginContact());cellOri.setCellStyle(styleBorder);
        	    Cell cellProy = rowDetail.createCell(10);cellProy.setCellValue(detail.getProspection().getProject().getName());cellProy.setCellStyle(styleBorder);
        	    Cell cellRes = rowDetail.createCell(11);cellRes.setCellValue(detail.getProspection().getResult());cellRes.setCellStyle(styleBorder);
        	    
        	    index++;
        	}
        }
     

        for (int j = 0; j <= 11; j++) {
            sheet.autoSizeColumn(j);
        }
        try {
            ServletContext scontext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String filePath = scontext.getRealPath("/WEB-INF/fileAttachments/"+nombreArchivo);
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
            fileDes = DefaultStreamedContent.builder()
                    .name(nombreArchivo)
                    .contentType("aplication/xls")
                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/fileAttachments/"+nombreArchivo))
                    .build();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
   

    }
	
//	public void descargarArchivo(String rutaArchivo,String nombreArchivo) {
//		   System.out.println("Inicio descargarArchivo");
//		   InputStream is = null;
//		   try {
//		      is = new FileInputStream(rutaArchivo);
//		      Date hoy=new Date();
//		      if (is != null)Filedownload.save(is, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", nombreArchivo);
//		        else alert("d");
//		   } catch (FileNotFoundException e) {
//		      e.printStackTrace();
//		   }
//		   System.out.println("Fin descargarArchivo");
//		}
	

	public void buscarReporte() {
		lstProspectionDetailReporte = new ArrayList<ProspectionDetail>();
		String personSurnames="%"+prospectSurnames+"%";
		String assessorDni="%%";
		String action="%%";
		String originContact="%"+origenContactoSelected+"%";
		String project="%%";
		
		
		if(personAssessorSelected!=null) {
			assessorDni="%"+personAssessorSelected.getDni()+"%";
		}
		
		if(actionSelected!=null) {
			action= "%"+actionSelected.getDescription()+"%";
		}
		
		if(projectSelected!=null) {
			project="%"+ projectSelected.getName()+"%";
		}
		
		fechaFin.setHours(23);
		fechaFin.setMinutes(59);
		
		if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
			lstProspectionDetailReporte = prospectionDetailService.findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(false, fechaIni,fechaFin,personSurnames,"%"+usuarioLogin.getPerson().getDni()+"%","%%",action,originContact,project);
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
			lstProspectionDetailReporte = prospectionDetailService.findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(false, fechaIni,fechaFin,personSurnames,assessorDni,"%"+usuarioLogin.getPerson().getDni()+"%",action,originContact,project);
		}else {
			lstProspectionDetailReporte = prospectionDetailService.findByScheduledAndDateBetweenAndProspectionProspectPersonSurnamesLikeAndProspectionPersonAssessorDniLikeAndProspectionPersonSupervisorDniLikeAndActionDescriptionLikeAndProspectionOriginContactLikeAndProspectionProjectNameLike(false, fechaIni,fechaFin,personSurnames,assessorDni,"%%",action,originContact,project);
		}
		
		if(lstProspectionDetailReporte.isEmpty()) {
			addWarnMessage("No se encontraron registros.");
		}
	}
	
	
	
	public void listarActions() {
		lstActions=actionService.findByStatus(true);
	}
	
	public void listarProject() {
		lstProject=projectService.findByStatusOrderByNameAsc(true);
	}
	
	public void mensajeERROR(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void mensajeINFO(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void listarPersonasAssessor() {
		List<Usuario> lstUsersAssesor = new ArrayList<>();
		if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
			lstUsersAssesor = usuarioService.findByProfileIdAndStatus(Perfiles.ASESOR.getId(), true);
		}else if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {	
			lstUsersAssesor.add(usuarioLogin);
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
			lstUsersAssesor = usuarioService.findByTeamPersonSupervisorAndStatus(usuarioLogin.getPerson(), true);
		}
		
		lstPersonAssessor = new ArrayList<>();
		
		if(lstUsersAssesor!=null) {
			if(!lstUsersAssesor.isEmpty()) {
				for(Usuario ase :lstUsersAssesor ) {
					lstPersonAssessor.add(ase.getPerson());
				}
			}
		}
	}
	
	public Converter getConversorPersonAssessor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Person c = null;
                    for (Person si : lstPersonAssessor) {
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
	
	public Converter getConversorAction() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Action c = null;
                    for (Action si : lstActions) {
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
                    return ((Action) value).getId() + "";
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
	
	public List<Person> completePersonAssesor(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPersonAssessor) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	
	
	
	
	
	

	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	public List<SelectItem> getCountriesGroup() {
		return countriesGroup;
	}
	public void setCountriesGroup(List<SelectItem> countriesGroup) {
		this.countriesGroup = countriesGroup;
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
	public ProspectService getProspectService() {
		return prospectService;
	}
	public void setProspectService(ProspectService prospectService) {
		this.prospectService = prospectService;
	}
	public Prospect getProspectSelected() {
		return prospectSelected;
	}
	public void setProspectSelected(Prospect prospectSelected) {
		this.prospectSelected = prospectSelected;
	}
	public List<Person> getLstPersonAssessor() {
		return lstPersonAssessor;
	}
	public void setLstPersonAssessor(List<Person> lstPersonAssessor) {
		this.lstPersonAssessor = lstPersonAssessor;
	}
	public Person getPersonAssessorSelected() {
		return personAssessorSelected;
	}
	public void setPersonAssessorSelected(Person personAssessorSelected) {
		this.personAssessorSelected = personAssessorSelected;
	}
	public Action getActionSelected() {
		return actionSelected;
	}
	public void setActionSelected(Action actionSelected) {
		this.actionSelected = actionSelected;
	}
	public ActionService getActionService() {
		return actionService;
	}
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}
	public List<Action> getLstActions() {
		return lstActions;
	}
	public void setLstActions(List<Action> lstActions) {
		this.lstActions = lstActions;
	}
	public String getOrigenContactoSelected() {
		return origenContactoSelected;
	}
	public void setOrigenContactoSelected(String origenContactoSelected) {
		this.origenContactoSelected = origenContactoSelected;
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
	public Project getProjectSelected() {
		return projectSelected;
	}
	public void setProjectSelected(Project projectSelected) {
		this.projectSelected = projectSelected;
	}
	public List<ProspectionDetail> getLstProspectionDetailReporte() {
		return lstProspectionDetailReporte;
	}
	public void setLstProspectionDetailReporte(List<ProspectionDetail> lstProspectionDetailReporte) {
		this.lstProspectionDetailReporte = lstProspectionDetailReporte;
	}
	public ProspectionDetailService getProspectionDetailService() {
		return prospectionDetailService;
	}
	public void setProspectionDetailService(ProspectionDetailService prospectionDetailService) {
		this.prospectionDetailService = prospectionDetailService;
	}
	public String getProspectSurnames() {
		return prospectSurnames;
	}
	public void setProspectSurnames(String prospectSurnames) {
		this.prospectSurnames = prospectSurnames;
	}
	public StreamedContent getFileDes() {
		return fileDes;
	}
	public void setFileDes(StreamedContent fileDes) {
		this.fileDes = fileDes;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public ProspectionDetail getProspectionDetailSelected() {
		return prospectionDetailSelected;
	}
	public void setProspectionDetailSelected(ProspectionDetail prospectionDetailSelected) {
		this.prospectionDetailSelected = prospectionDetailSelected;
	}
	public boolean isPermisoSepararVender() {
		return permisoSepararVender;
	}
	public void setPermisoSepararVender(boolean permisoSepararVender) {
		this.permisoSepararVender = permisoSepararVender;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	
	
}
