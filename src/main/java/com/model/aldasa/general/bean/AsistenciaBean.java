package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.service.AsistenciaService;
import com.model.aldasa.service.EmpleadoService;

@ManagedBean
@ViewScoped
public class AsistenciaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{asistenciaService}")
	private AsistenciaService asistenciaService;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	private String textoIngresado;
	
	@PostConstruct
	public void init() {
		textoIngresado="";
		
	}
	
	public void boton1() {
		textoIngresado=textoIngresado + 1;
	}
	public void boton2() {
		textoIngresado=textoIngresado + 2;
	}
	public void boton3() {
		textoIngresado=textoIngresado + 3;
	}
	public void boton4() {
		textoIngresado=textoIngresado + 4;
	}
	public void boton5() {
		textoIngresado=textoIngresado + 5;
	}
	public void boton6() {
		textoIngresado=textoIngresado + 6;
	}
	public void boton7() {
		textoIngresado=textoIngresado + 7;
	}
	public void boton8() {
		textoIngresado=textoIngresado + 8;
	}
	public void boton9() {
		textoIngresado=textoIngresado + 9;
	}
	public void boton0() {
		textoIngresado=textoIngresado + 0;
	}
	
	public void limpiar() {
		textoIngresado="";
	}
	
	public void borrar() {
		if(!textoIngresado.equals("")) {
			textoIngresado=textoIngresado.substring(0, textoIngresado.length() - 1);
		}
	}
	
	public void aceptar() {
		Empleado empleado = empleadoService.findByPersonDniAndEstado(textoIngresado, true);
		if(empleado == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "DNI incorrecto");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}else {
			Date horaIni = new Date();
			horaIni.setHours(00);
			horaIni.setMinutes(00);
			horaIni.setSeconds(00);
			Date horaFin = new Date();
			horaFin.setHours(23);
			horaFin.setMinutes(59);
			horaFin.setSeconds(59);
			List<Asistencia> lstAsistencia = asistenciaService.findByEmpleadoAndHoraBetweenAndEstadoOrderByHoraAsc(empleado, horaIni, horaFin, true);
			if (lstAsistencia.isEmpty()) {
				Asistencia asistencia = new Asistencia();
				asistencia.setEmpleado(empleado);
				asistencia.setTipo("E");
				asistencia.setHora(new Date());
				asistencia.setEstado(true);
				Asistencia guarda = asistenciaService.save(asistencia);
				if(guarda == null) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar");
					PrimeFaces.current().dialog().showMessageDynamic(message);
					return;
				}else {
					
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación ", "Se guardo su ENTRADA correctamente, empleado " + empleado.getPerson().getSurnames() + " " + empleado.getPerson().getNames() + " y se ha tomado una foto automaticamente para verificar que seas la verdadera persona.");
					PrimeFaces.current().dialog().showMessageDynamic(message);
					textoIngresado="";
					return;
				}
				
				
				
			}else {
				if(lstAsistencia.size()==4) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo registrar, ya tiene completado los registros del día.");
					PrimeFaces.current().dialog().showMessageDynamic(message);
					return;
				}
				
				
				String tipo = "";
				for (Asistencia asist:lstAsistencia) {
					tipo = asist.getTipo();
				}
				if(tipo.equals("E")) {
					Asistencia asistencia = new Asistencia();
					asistencia.setEmpleado(empleado);
					asistencia.setTipo("S");
					asistencia.setHora(new Date());
					asistencia.setEstado(true);
					Asistencia guarda = asistenciaService.save(asistencia);
					if(guarda == null) {
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar");
						PrimeFaces.current().dialog().showMessageDynamic(message);
						return;
					}else {
						
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación ", "Se guardo su SALIDA correctamente, empleado " + empleado.getPerson().getSurnames() + " " + empleado.getPerson().getNames() + " y se ha tomado una foto automaticamente para verificar que seas la verdadera persona.");
						PrimeFaces.current().dialog().showMessageDynamic(message);
						textoIngresado="";
						return;
					}
				} else {
					Asistencia asistencia = new Asistencia();
					asistencia.setEmpleado(empleado);
					asistencia.setTipo("E");
					asistencia.setHora(new Date());
					asistencia.setEstado(true);
					Asistencia guarda = asistenciaService.save(asistencia);
					if(guarda == null) {
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar");
						PrimeFaces.current().dialog().showMessageDynamic(message);
						return;
					}else {
						
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación ", "Se guardo su ENTRADA correctamente, empleado " + empleado.getPerson().getSurnames() + " " + empleado.getPerson().getNames() + " y se ha tomado una foto automaticamente para verificar que seas la verdadera persona.");
						PrimeFaces.current().dialog().showMessageDynamic(message);
						textoIngresado="";
						return;
					}
				}
			}
	
		}
		
	}
		
	

	
	public String getTextoIngresado() {
		return textoIngresado;
	}
	public void setTextoIngresado(String textoIngresado) {
		this.textoIngresado = textoIngresado;
	}
	public AsistenciaService getAsistenciaService() {
		return asistenciaService;
	}
	public void setAsistenciaService(AsistenciaService asistenciaService) {
		this.asistenciaService = asistenciaService;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	

}
