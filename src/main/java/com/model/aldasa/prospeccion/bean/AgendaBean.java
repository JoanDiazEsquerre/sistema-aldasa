package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.util.ObjectUtils;

import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.ProspectionService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.EstadoProspeccion;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class AgendaBean  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(value = "#{prospectionDetailService}")
	private ProspectionDetailService prospectionDetailService;
	
	@ManagedProperty(value = "#{prospectionService}")
	private ProspectionService prospectionService;
	
	private Usuario usuarioLogin = new Usuario();
	private List<ProspectionDetail> lstProspectionDetailAgenda = new ArrayList<>();
	
	private ScheduleModel eventModel= new DefaultScheduleModel();
  
    private ProspectionDetail prospectionDetailSelected;
    
    private ScheduleModel lazyEventModel;
 
    SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy");
	
	@PostConstruct
	public void init() {
		
usuarioLogin = usuarioService.findByUsername(navegacionBean.getUsername());		
		
		
		eventModel= new DefaultScheduleModel();
		
		lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(LocalDateTime start, LocalDateTime end) {
            	
            	Instant instant1 = start.atZone(ZoneId.systemDefault()).toInstant();
            	Date dateStart = Date.from(instant1);
            	
            	Instant instant2 = end.atZone(ZoneId.systemDefault()).toInstant();
            	Date dateFinish = Date.from(instant2);
            	
            	
            	if (Perfiles.ADMINISTRADOR.getName().equals(usuarioLogin.getProfile().getName()) ) {
        			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionStatusAndScheduledAndDateBetween(EstadoProspeccion.EN_SEGUIMIENTO.getName(),true,dateStart,dateFinish);
        		}else if(Perfiles.ASESOR.getName().equals(usuarioLogin.getProfile().getName())) {	
        			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionPersonAssessorAndProspectionStatusAndScheduledAndDateBetween(usuarioLogin.getPerson(),EstadoProspeccion.EN_SEGUIMIENTO.getName(),true,dateStart,dateFinish);
        		} else if (Perfiles.SUPERVISOR.getName().equals(usuarioLogin.getProfile().getName())) {
        			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionPersonSupervisorAndProspectionStatusAndScheduledAndDateBetween(usuarioLogin.getPerson(),EstadoProspeccion.EN_SEGUIMIENTO.getName(),true,dateStart,dateFinish);
        		}
        		
            
            	
        		if(!ObjectUtils.isEmpty(lstProspectionDetailAgenda)) {
	    			for(ProspectionDetail detalle : lstProspectionDetailAgenda) {
	    				Date input = detalle.getDate();
	    				Instant instant = input.toInstant();
	    				ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
	    				LocalDateTime date = zdt.toLocalDateTime();
	    				
	    				String color="red";
	    				if(detalle.getDate().after(new Date())) {
	    					color="#27AE60";
	    				}else if(detalle.getDate().before(new Date())) {
	    					color="orange";
	    				}
	    					    			
	    				DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
	    						
	    						.title(detalle.getAction().getDescription())
	    						.startDate(date)
	    						.data(detalle)
	    						.endDate(date)
	    						.description(detalle.getProspection().getProspect().getPerson().getSurnames()+" "+detalle.getProspection().getProspect().getPerson().getNames()+""+(detalle.getComment().equals("")?"":" / "+detalle.getComment()))
	    						.overlapAllowed(true)
	    						.id(detalle.getId()+"")
	    						.borderColor(color).build();
	    				addEvent(event);
	    			}
	    		}
            	
            }
        };
		
//		
//		DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
//	                .title(detalle.getAction().getDescription())
//	                .startDate(date)
//	                .endDate(date)
//	                .description("Aqui la descripcion")
//	                .build();
//		eventModel.addEvent(event);
//	}
//}
		
		
//		DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder().title("Champions League Match")
//				.startDate(previousDay8Pm(date)).endDate(previousDay11Pm(date)).description("Team A vs. Team B")
//				.url("https://www.uefa.com/uefachampionsleague/").borderColor("orange").build();
//		eventModel.addEvent(event);
//
//		DefaultScheduleEvent<?> event1 = DefaultScheduleEvent.builder().startDate(date.minusDays(6)).endDate(date.minusDays(3))
//				.overlapAllowed(true).editable(false).resizable(false).backgroundColor("lightgreen").build();
//		eventModel.addEvent(event1);
//
//		
//
//		DefaultScheduleEvent<?> event2event = DefaultScheduleEvent.builder().title("Breakfast at Tiffanys (always resizable)")
//				.startDate(nextDay9Am(date)).endDate(nextDay11Am(date)).description("all you can eat")
//				.overlapAllowed(true).resizable(true).borderColor("#27AE60").build();
//		eventModel.addEvent(event2event);
//
//		event = DefaultScheduleEvent.builder().title("Plant the new garden stuff (always draggable)")
//				.startDate(theDayAfter3Pm(date)).endDate(fourDaysLater3pm(date)).description("Trees, flowers, ...")
//				.draggable(true).borderColor("#27AE60").build();
//		eventModel.addEvent(event);
//
//		DefaultScheduleEvent<?> scheduleEventAllDay = DefaultScheduleEvent.builder().title("Holidays (AllDay)")
//				.startDate(sevenDaysLater0am(date)).endDate(eightDaysLater0am(date))
//				.description("sleep as long as you want").borderColor("#27AE60").allDay(true).build();
//		eventModel.addEvent(scheduleEventAllDay);
		
   	}
	
	public LocalDateTime getRandomDateTime(LocalDateTime base) {
        LocalDateTime dateTime = base.withMinute(0).withSecond(0).withNano(0);
        return dateTime.plusDays(((int) (Math.random() * 30)));
    }
	   
	
    public void realizarAction() {
    	String dateNow = sdfFull.format(new Date());
    	String dateDetail = sdfFull.format(prospectionDetailSelected.getDate());
    	if(dateNow.equals(dateDetail)) {
    		prospectionDetailSelected.setScheduled(false);
    		prospectionDetailService.save(prospectionDetailSelected);
    		
    		
    		if(prospectionDetailSelected.getAction().getPorcentage()> prospectionDetailSelected.getProspection().getPorcentage()) {
    			Prospection prospection = prospectionDetailSelected.getProspection();
    			prospection.setPorcentage(prospectionDetailSelected.getAction().getPorcentage());
				prospectionService.save(prospection);
			}
    		
    		mensajeINFO("Se realizó correctamente la acción");
    	}else {
    		mensajeERROR("La fecha agandada no es igual a la fecha actual."); 
    	}
    }
    
    public void mensajeERROR(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	public void mensajeINFO(String mensaje) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", mensaje);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

    
   public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
	   ScheduleEvent event = selectEvent.getObject();
	   prospectionDetailSelected = (ProspectionDetail) event.getData();
	   
   }  
 
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    public LocalDate getInitialDate() {
        return LocalDate.now().plusDays(1);
    }

     



    
    
	public ProspectionDetail getProspectionDetailSelected() {
		return prospectionDetailSelected;
	}

	public void setProspectionDetailSelected(ProspectionDetail prospectionDetailSelected) {
		this.prospectionDetailSelected = prospectionDetailSelected;
	}

	public ProspectionDetailService getProspectionDetailService() {
		return prospectionDetailService;
	}

	public void setProspectionDetailService(ProspectionDetailService prospectionDetailService) {
		this.prospectionDetailService = prospectionDetailService;
	}

	public List<ProspectionDetail> getLstProspectionDetailAgenda() {
		return lstProspectionDetailAgenda;
	}

	public void setLstProspectionDetailAgenda(List<ProspectionDetail> lstProspectionDetailAgenda) {
		this.lstProspectionDetailAgenda = lstProspectionDetailAgenda;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
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

	public ScheduleModel getLazyEventModel() {
		return lazyEventModel;
	}

	public void setLazyEventModel(ScheduleModel lazyEventModel) {
		this.lazyEventModel = lazyEventModel;
	}

	public ProspectionService getProspectionService() {
		return prospectionService;
	}

	public void setProspectionService(ProspectionService prospectionService) {
		this.prospectionService = prospectionService;
	}

	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}

	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	
}
