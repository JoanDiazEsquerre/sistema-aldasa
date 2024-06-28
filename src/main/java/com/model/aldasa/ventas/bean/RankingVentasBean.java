package com.model.aldasa.ventas.bean;

import java.io.Serializable;
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

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.EstadoLote;

@ManagedBean
@ViewScoped
public class RankingVentasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
		
//	@ManagedProperty(value = "#{comisionService}")
//	private ComisionService comisionService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
		
	private List<Usuario> lstUsuario;	
	
	private Usuario userSelected;
	
	private String anio;
	
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdfY = new SimpleDateFormat("yy");
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");

	
	@PostConstruct
	public void init() {
	anio=sdfY.format(new Date());
	listarUsuario();
	}
	
	public void listarUsuario() {
		lstUsuario = new ArrayList<>();
		List<Usuario> lstusu = usuarioService.findByStatus(true);
		for (Usuario a : lstusu) {
			Empleado empleado = empleadoService.findByPersonId(a.getPerson().getId());
			if(empleado != null) {
				Date fechaActual = sumarDiasAFecha(new Date(), -1);
				Date fechaFin = empleado.getFechaSalida();
				
				if(fechaFin.after(fechaActual)) {
					lstUsuario.add(a);
				}
				
			}
		}
	}
	
	public  Date sumarDiasAFecha(Date fecha, int dias){
	      if (dias==0) return fecha;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      Date date= calendar.getTime(); 
	      date.setHours(0);
	      date.setMinutes(0); 
	      date.setSeconds(0);
	      return date; 
	}
	
	public int lotesVendidoMes (String mes,String anioselected, Usuario usuario) {
//		String codigo = mes+anioselected;
//		Comision comision = comisionService.findByEstadoAndCodigo(true, codigo);
//		if (comision != null) {
//			List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(),usuario.getPerson().getDni() , comision.getFechaIni(), comision.getFechaCierre());
//			if(!lstLotesVendidos.isEmpty()) {
//				return lstLotesVendidos.size();
//			}
//		}
		return 0;
	}
	
	public int totalLotesVendido(Usuario usuario) {
		int lot = 0;
		Empleado empleado = empleadoService.findByPersonId(usuario.getPerson().getId());
		if(empleado!=null) {
			List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(),usuario.getPerson().getDni() ,empleado.getFechaIngreso(), new Date());
			if(!lstLotesVendidos.isEmpty()) {
				return lstLotesVendidos.size();
			}
		}
		return lot;
	}
	
	public int mesesTrabajados(Usuario usuario) {
		int meses = 0;
		Empleado empleado = empleadoService.findByPersonId(usuario.getPerson().getId());
		if(empleado != null) {
			int anioIngreso = empleado.getFechaIngreso().getYear();
			int anioSalida = empleado.getFechaSalida().getYear();
			int anioActual= new Date().getYear(); 
			int aniosDiferencia = anioActual-anioIngreso;
			
			int mesIngreso=empleado.getFechaIngreso().getMonth();
			int mesSalida = empleado.getFechaSalida().getMonth();
			int mesActual=new Date().getMonth();
			int mesDiferencia=0;
			if(mesIngreso> mesActual) {
				mesDiferencia = mesIngreso-mesActual;
			}else if (mesActual>mesIngreso) {
				mesDiferencia = mesActual-mesIngreso;
			} 
			if (mesActual>mesSalida) {
				return 0;
			}
			
			
			if(aniosDiferencia != 0) {
					meses = (aniosDiferencia*12)-mesDiferencia;
				 if (mesActual>mesIngreso) {
					meses = (aniosDiferencia*12)+mesDiferencia;
				}
				 if(anioActual>anioSalida) {
						return 0;
					}
				
			}else  {
				meses = mesDiferencia;
				
			}
			meses++;
		}
		
		return meses;
	}
	
	public boolean validarMesNoTrabajado(Usuario usuario, String mes, String anio) {
		boolean valor = false;
		Empleado empleado = empleadoService.findByPersonId(usuario.getPerson().getId());
		
		if(empleado!=null) {
			String anioMesEnviadoText = anio+mes;
			int anioMesEnviado = Integer.parseInt(anioMesEnviadoText);

			String anioMesIngresoText  =sdfY.format(empleado.getFechaIngreso())+ sdfM.format(empleado.getFechaIngreso()) ;
			int anioMesIngreso =Integer.parseInt(anioMesIngresoText);
			
			if(anioMesEnviado < anioMesIngreso ) {
				return true;
			}
			
		}	
		return valor;
	}
	
	public int mesesQueVendio(Usuario usuario) {
		int mesesVenta = 0;
		Empleado empleado = empleadoService.findByPersonId(usuario.getPerson().getId());
		if(empleado!=null) {
			Date fechaIngreso = empleado.getFechaIngreso();
			fechaIngreso.setDate(1);
			
			int mesesTrabajados = mesesTrabajados(usuario);
			mesesTrabajados++;
			
			for(int i = 0; i<mesesTrabajados;i++) {
				if(i==0) {
					String mes = sdfM.format(fechaIngreso);
					String anio = sdfY.format(fechaIngreso);
					
					int lotesVendidos = lotesVendidoMes(mes, anio, usuario);
					if(lotesVendidos>0) {
						mesesVenta++;
					}
				}else {
					fechaIngreso = sumaRestaMeses(fechaIngreso, 1);
					String mes = sdfM.format(fechaIngreso);
					String anio = sdfY.format(fechaIngreso);
					
					int lotesVendidos = lotesVendidoMes(mes, anio, usuario);
					if(lotesVendidos>0) {
						mesesVenta++;
					}
				}
			}
		}
		return mesesVenta;
	}
	
	public double promedioMensualVentas(Usuario usuario) {
		double prom = 0.0;
		Empleado empleado = empleadoService.findByPersonId(usuario.getPerson().getId());
		if(totalLotesVendido(usuario)>0) {
			double tlv = totalLotesVendido(usuario) ;
			double mt = mesesTrabajados(usuario);
			return Math.round(tlv/mt * 100.0) / 100.0;
		}
		
		return prom;
	}
	
	public double promedio3UltimosMenses(Usuario usuario) {
		double prom = 0.0;

		Date fechaActual= new Date();
		fechaActual.setDate(1);
		
		String mesActual=sdfM.format(fechaActual);
		String anioActual=sdfY.format(fechaActual);
		int ventaMesActual=lotesVendidoMes(mesActual, anioActual, usuario);

		Date mesAnterior = sumaRestaMeses(fechaActual,-1);
		
		String mesPasado=sdfM.format(mesAnterior);
		String anioMesPasado=sdfY.format(mesAnterior);
		
		int ventaMesAnterior= lotesVendidoMes(mesPasado, anioMesPasado, usuario);
		
		Date mesAnterior2 = sumaRestaMeses(fechaActual,-2);
		
		String mesAntePasado=sdfM.format(mesAnterior2);
		String anioMesAntePasado=sdfY.format(mesAnterior2);
		
		int ventaMesAnteAnterior= lotesVendidoMes(mesAntePasado, anioMesAntePasado, usuario);
		
		double suma3UltimosMeses = ventaMesActual + ventaMesAnterior + ventaMesAnteAnterior;
		if(suma3UltimosMeses != 0) {
			return Math.round(suma3UltimosMeses/3 * 100.0) / 100.0;
		}
		
		return prom;
	}
	
	public Date sumaRestaMeses(Date fecha, int mes) {
		
		Calendar calendar = Calendar.getInstance(); 
		calendar.setLenient(false);

		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		
		
		calendar.add(calendar.MONTH, mes);  
		return calendar.getTime();
	}
	
	public String rendimiento (Usuario usuario) {
		String a = "";
		double promedio = promedio3UltimosMenses(usuario);
		
		if (promedio >= 5.0) {
			String alto = String.format("ALTO", promedio);
			return alto;
		}
		if (promedio >= 1.5) {
			String medio = String.format("MEDIO", promedio);
			return medio;
		}
		if (promedio >= 0.0) {
			String bajo = String.format("BAJO", promedio);
			return bajo;
		}
		
		return a;
	}
	
	
	
	

	
	
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public SimpleDateFormat getSdf2() {
		return sdf2;
	}
	public void setSdf2(SimpleDateFormat sdf2) {
		this.sdf2 = sdf2;
	}
	public SimpleDateFormat getSdfY() {
		return sdfY;
	}
	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public SimpleDateFormat getSdfM() {
		return sdfM;
	}
	public void setSdfM(SimpleDateFormat sdfM) {
		this.sdfM = sdfM;
	}
	public Usuario getUserSelected() {
		return userSelected;
	}
	public void setUserSelected(Usuario userSelected) {
		this.userSelected = userSelected;
	}
	public List<Usuario> getLstUsuario() {
		return lstUsuario;
	}
	public void setLstUsuario(List<Usuario> lstUsuario) {
		this.lstUsuario = lstUsuario;
	}
	
}
