package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.fileupload.RequestContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.model.aldasa.entity.ModuloSistema;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.RequerimientoSeparacionService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.EstadoRequerimientoSeparacionType;
import com.model.aldasa.util.Perfiles;

import org.primefaces.PrimeFaces;

@ManagedBean(name = "navegacionBean")
@SessionScoped
public class NavegacionBean implements Serializable  { 
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty(value = "#{requerimientoSeparacionService}")
	private RequerimientoSeparacionService requerimientoSeparacionService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	private String ruta, rutaLogo;
	private String username;                              
	private Usuario usuarioLogin = new Usuario();
	private Sucursal sucursalLogin;
	
	private int idAdministrador = Perfiles.ADMINISTRADOR.getId();
	private int idSupervisor = Perfiles.SUPERVISOR.getId();
	private int idAsesor = Perfiles.ASESOR.getId();
	private int idAsistentenAdmin = Perfiles.ASISTENTE_ADMINISTRATIVO.getId();
	private int idAsistencia = Perfiles.ASISTENCIA.getId();
	private int idRecursosHumanos = Perfiles.RECURSOS_HUMANOS.getId();
	private int idContabilidad = Perfiles.CONTABILIDAD.getId();
	private int idAsistenteVenta = Perfiles.ASISTENTE_VENTA.getId();

	
	private boolean menuProspeccion, menuProyecto, menuMantenimiento,menuReporte, menuAsistencia, menuVentas, menuAlmacen, menuRRHH, menucaja, menuCompra, menuContabilidad;
	private boolean subMenuEmpleado, subMenuPlanilla, subMenuComision, subMenuComisiones, subMenuManzanas, subMenuLotes, subMenuProspectos, subMenuProspeccion,subMenuAgenda, subMenuSimulador, subMenuPersonas,subMenuUsuarios,subMenuPerfiles, 
					subMenuProyectos,subMenuEquipos,subMenuCambiarContrasenia, subMenuReporteAcciones, subMenuAsistencia, subMenuReporteAsistencia, subMenuRequerimientoSeparacion, subMenuRankingVentas, subMenuContrato, subMenuDocumentoVentas, subMenuPlantillaVenta, 
					subMenuReporteDocumentoVenta, subMenuInventario, subMenuCliente, subMenuMapeoLote, subMenuVoucher, subMenuCaja, subMenuRequerimientoCompra, subMenuDocumentoCompra, subMenuReporteDeuda, subMenuMovimientosBanc;
	
	private int[] permisoProspectos,permisoProspeccion,permisoAgenda,permisoSimulador,permisoPersonas,permisoUsuarios,permisoPerfiles,permisoProyectos,permisoEquipos, permisoCambiarConstrasenia,
					permisoReporteAcciones,permisoManzanas,permisoLotes,permisoComisiones,permisoComision,permisoEmpleado,permisoAsistencia,permisoReporteAsistencia,permisoRequerimientoSeparacion,
					permisoRankingVentas,permisoContrato,permisoReporteDocumentoVentas,permisoDocumentoVenta,permisoInventario,permisoCliente, permisoPlantillaVenta, permisoMapeoLote, permisoVoucher,
					permisoPlanilla, permisoCaja, permisoRequerimientoCompra, permisoDocumentoCompra, permisoReporteDeuda, permisoMovimientoBanc;

	@PostConstruct
	public void init() {
		sucursalLogin = (Sucursal)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sucursalLog"); 
		if(sucursalLogin==null) {
			cerrarSesion();
			return;
			
		}
		
		
		ruta = "modulos/general/mantenimientos/inicio.xhtml";
		username = SecurityContextHolder.getContext().getAuthentication().getName();
		usuarioLogin = usuarioService.findByUsername(username);
		
		permisoProspectos= obtenerPermisosPorPerfil(1);
		permisoProspeccion= obtenerPermisosPorPerfil(2);
		permisoAgenda= obtenerPermisosPorPerfil(3);
		permisoSimulador= obtenerPermisosPorPerfil(4);
		permisoRequerimientoSeparacion= obtenerPermisosPorPerfil(5);
		permisoProyectos= obtenerPermisosPorPerfil(6);
		permisoManzanas= obtenerPermisosPorPerfil(7);
		permisoLotes= obtenerPermisosPorPerfil(8);
		permisoComisiones= obtenerPermisosPorPerfil(9);
		permisoContrato= obtenerPermisosPorPerfil(10);
		permisoRankingVentas= obtenerPermisosPorPerfil(11);
		permisoDocumentoVenta= obtenerPermisosPorPerfil(12);
		permisoPersonas= obtenerPermisosPorPerfil(13);
		permisoUsuarios= obtenerPermisosPorPerfil(14);
		permisoPerfiles= obtenerPermisosPorPerfil(15);
		permisoEquipos= obtenerPermisosPorPerfil(16);
		permisoEmpleado= obtenerPermisosPorPerfil(17);
		permisoComision= obtenerPermisosPorPerfil(18);
		permisoCambiarConstrasenia= obtenerPermisosPorPerfil(19);
		permisoReporteAcciones= obtenerPermisosPorPerfil(20);
		permisoReporteAsistencia= obtenerPermisosPorPerfil(22);
		permisoReporteDocumentoVentas= obtenerPermisosPorPerfil(23);
		permisoAsistencia= obtenerPermisosPorPerfil(24);
		permisoInventario= obtenerPermisosPorPerfil(25);
		permisoCliente= obtenerPermisosPorPerfil(26);
		permisoPlantillaVenta=obtenerPermisosPorPerfil(27);
		permisoMapeoLote = obtenerPermisosPorPerfil(28);
		permisoVoucher = obtenerPermisosPorPerfil(29);
		permisoPlanilla = obtenerPermisosPorPerfil(30);
		permisoCaja = obtenerPermisosPorPerfil(31);
		permisoRequerimientoCompra = obtenerPermisosPorPerfil(32);
		permisoDocumentoCompra = obtenerPermisosPorPerfil(33);
		permisoReporteDeuda = obtenerPermisosPorPerfil(34);
		permisoMovimientoBanc = obtenerPermisosPorPerfil(35);

		
		permisoPantallas();
		
		if(sucursalLogin.getId().toString().equals("1")) {
			rutaLogo = "/recursos/images/LOGO.png";
		}else if(sucursalLogin.getId().toString().equals("2")){
			rutaLogo = "/recursos/images/LOGO_ABARCA.png";
		}else if(sucursalLogin.getId().toString().equals("3")) {
			rutaLogo = "/recursos/images/LOGO_CONSORCIO.png";
		}else {
			rutaLogo = "/recursos/images/LOGO_ALDASA_BIENES_RAICES.png";
		}
		
		if(usuarioLogin.getProfile().getId().equals(20)) {
			ruta = "modulos/proyecto/mantenimientos/mapeoLote.xhtml";
		}
		
		
//		try {
//			actualizarPlantillaSeparacion();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
//	public void actualizarPlantillaSeparacion() throws ParseException {
//		List<RequerimientoSeparacion> lstSeparacionesPendientes = requerimientoSeparacionService.findByEstado(EstadoRequerimientoSeparacionType.EN_PROCESO.getDescripcion());
//		
//		SimpleDateFormat sdfQuery = new SimpleDateFormat("yyyy-MM-dd");  
//		String fechaHoyText = sdfQuery.format(new Date());
//		
//		Date fechaHoy = sdfQuery.parse(fechaHoyText);
//		
//		
//		for(RequerimientoSeparacion req : lstSeparacionesPendientes) {
//			String fechaReqText = sdfQuery.format(req.getFechaVencimiento());
//			Date fechaReq = sdfQuery.parse(fechaReqText);
//			
//			if(fechaHoy.after(fechaReq)) {
//				req.setEstado(EstadoRequerimientoSeparacionType.VENCIDO.getDescripcion());
//				requerimientoSeparacionService.save(req);
//				
//				req.getLote().setStatus(EstadoLote.DISPONIBLE.getName());
//				loteService.save(req.getLote()); 
//				
//			}
//			
//		}
//	}
	
	public int[] obtenerPermisosPorPerfil(int idModuloSistema) {
		int[] ids= {};
		
		if(!usuarioLogin.getProfile().getPermiso().equals("")) {
			String[] idPermisos = usuarioLogin.getProfile().getPermiso().split(",");
			int recorre=0;
			for(String s : idPermisos) {
				String modulo = idModuloSistema+"";
				if(modulo.equals(s)) {
					recorre++;
				}
			}
			
			if(recorre>0) {
				ids = new int[recorre];
				recorre = 0;
				for(String s : idPermisos) {
					String modulo = idModuloSistema+"";
					if(modulo.equals(s)) {
						ids[recorre]= usuarioLogin.getProfile().getId();
						recorre++;
					}
				}
			}
			
		}
		
		return ids;
	}
	

	
	public void permisoPantallas() {
		menuProspeccion=false; menuProyecto=false; menuMantenimiento=false; menuReporte=false; menuAsistencia=false;menuVentas=false; menuAlmacen=false; menuRRHH = false; menuContabilidad = false;
		//*******************************************************************************
		subMenuProspectos = validaPermiso(permisoProspectos);
		subMenuProspeccion= validaPermiso(permisoProspeccion);
		subMenuAgenda= validaPermiso(permisoAgenda);
		subMenuSimulador= validaPermiso(permisoSimulador);
		subMenuRequerimientoSeparacion= validaPermiso(permisoRequerimientoSeparacion);
	
		if(subMenuProspectos || subMenuProspeccion || subMenuAgenda || subMenuSimulador || subMenuRequerimientoSeparacion) {
			menuProspeccion=true;
		}
		//*******************************************************************************
		subMenuProyectos= validaPermiso(permisoProyectos);
		subMenuManzanas = validaPermiso(permisoManzanas);
		subMenuLotes= validaPermiso(permisoLotes);
		subMenuComisiones= validaPermiso(permisoComisiones);
		subMenuContrato = validaPermiso(permisoContrato);
		subMenuMapeoLote = validaPermiso(permisoMapeoLote);
				
		if(subMenuManzanas || subMenuLotes || subMenuProyectos || subMenuComisiones || subMenuContrato || subMenuMapeoLote){
			menuProyecto=true;
		}
		
		//******************************************************************************
		subMenuRankingVentas= validaPermiso(permisoRankingVentas);
		subMenuDocumentoVentas=validaPermiso(permisoDocumentoVenta);
		subMenuPlantillaVenta = validaPermiso(permisoPlantillaVenta);
				
		if(subMenuRankingVentas || subMenuDocumentoVentas || subMenuPlantillaVenta){
			menuVentas=true;
		}
		
		//******************************************************************************
		
		subMenuRequerimientoCompra= validaPermiso(permisoRequerimientoCompra);
		subMenuDocumentoCompra=validaPermiso(permisoDocumentoCompra);
				
		if(subMenuRequerimientoCompra || subMenuDocumentoCompra){
			menuCompra=true;
		}
		
		//******************************************************************************
		subMenuPersonas = validaPermiso(permisoPersonas);
		subMenuUsuarios= validaPermiso(permisoUsuarios);
		subMenuPerfiles= validaPermiso(permisoPerfiles);
		subMenuEquipos= validaPermiso(permisoEquipos);
		subMenuCliente= validaPermiso(permisoCliente);
		subMenuComision= validaPermiso(permisoComision);
		subMenuCambiarContrasenia= validaPermiso(permisoCambiarConstrasenia);
		subMenuVoucher = validaPermiso(permisoVoucher);
		
		if(subMenuPersonas || subMenuUsuarios || subMenuPerfiles || subMenuEquipos || subMenuCliente || subMenuComision || subMenuCambiarContrasenia || subMenuVoucher) {
			menuMantenimiento=true;
		}
		
		//******************************************************************************
		
		subMenuCaja= validaPermiso(permisoCaja);
		
		if(subMenuCaja) {
			menucaja=true;
		}
		
		//*******************************************************************************
		subMenuEmpleado= validaPermiso(permisoEmpleado);
		subMenuPlanilla = validaPermiso(permisoPlanilla);
		
		if(subMenuPlanilla || subMenuEmpleado) {
			menuRRHH=true;
		}
		
		//*******************************************************************************
		subMenuReporteAcciones = validaPermiso(permisoReporteAcciones);
		subMenuReporteAsistencia = validaPermiso(permisoReporteAsistencia);
		subMenuReporteDocumentoVenta=validaPermiso(permisoReporteDocumentoVentas);
		subMenuReporteDeuda = validaPermiso(permisoReporteDeuda);

		
		if(subMenuReporteAcciones || subMenuReporteAsistencia || subMenuReporteDocumentoVenta || subMenuReporteDeuda) {
			menuReporte=true;
		}
		//*******************************************************************************
		
		subMenuAsistencia = validaPermiso(permisoAsistencia);

		if(subMenuAsistencia) {
			menuAsistencia=true;
		}
		//*******************************************************************************
		
		subMenuInventario = validaPermiso(permisoInventario);

		if(subMenuInventario) {
			menuAlmacen=true;
		}
		//*******************************************************************************
		
		subMenuMovimientosBanc = validaPermiso(permisoMovimientoBanc);

		if(subMenuMovimientosBanc) {
			menuContabilidad=true;
		}
		//*******************************************************************************
		
	}			
	
	
	public boolean validaPermiso(int[] permiso) {
		boolean valida = false;
		for(int per:permiso) {
			if(per == usuarioLogin.getProfile().getId()) {
				valida= true;
			}
		}
		
		return valida;
	}
	
	
	public void getProcesoProspeccionPage() {
		ruta = "modulos/prospeccion/procesos/prospeccion.xhtml";
	}

	public void getProcesoSimuladorPage() {
		ruta = "modulos/prospeccion/procesos/simulador.xhtml";
	}

	public void getProcesoAgendaPage() {
		ruta = "modulos/prospeccion/procesos/agenda.xhtml";
	}

	public void getProcesoRequerimientoSeparacionPage() {
		ruta = "modulos/prospeccion/procesos/requerimientoSeparacion.xhtml";
	}

	public void getProspectosPage() {
		ruta = "modulos/prospeccion/mantenimientos/prospecto.xhtml";
	}

	public void getProcesoReporteProspeccionPage() {
		ruta = "modulos/prospeccion/procesos/reporteProspeccion.xhtml";
	}

	public void getMantenimientoProjectPage() {
		ruta = "modulos/proyecto/mantenimientos/project.xhtml";
	}

	public void getProyectoManzanasPage() {
		ruta = "modulos/proyecto/mantenimientos/manzana.xhtml";
	}

	public void getProyectoLotesPage() {
		ruta = "modulos/proyecto/mantenimientos/lote.xhtml";
	}
	
	public void getProyectoMapeoLotesPage() {
		ruta = "modulos/proyecto/mantenimientos/mapeoLote.xhtml";
	}

	public void getProyectoComisionesPage() {
		ruta = "modulos/proyecto/procesos/comisiones.xhtml";
	}

	public void getMantenimientoPersonasPage() {
		ruta = "modulos/general/mantenimientos/personas.xhtml";
	}

	public void getMantenimientoUsersPage() {
		ruta = "modulos/general/mantenimientos/users.xhtml";
	}

	public void getMantenimientoProfilePage() {
		ruta = "modulos/general/mantenimientos/profile.xhtml";
	}

	public void getMantenimientoTeamPage() {
		ruta = "modulos/general/mantenimientos/team.xhtml";
	}

	public void getRRHHEmpleadoPage() {
		ruta = "modulos/rrhh/mantenimientos/empleado.xhtml";
	}
	
	public void getRRHHPlanillaPage() {
		ruta = "modulos/rrhh/procesos/planilla.xhtml";
	}
	
	public void getMantenimientoClientePage() {
		ruta = "modulos/general/mantenimientos/cliente.xhtml";
	}

	public void getMantenimientoComisionPage() {
		ruta = "modulos/general/mantenimientos/comision.xhtml";
	}
	
	public void getMantenimientoVoucherPage() {
		ruta = "modulos/general/mantenimientos/voucher.xhtml";
	}

	public void getMantenimientoPasswordchangePage() {
		ruta = "modulos/general/mantenimientos/passwordchange.xhtml";
	}

	public void getAsistenciaAsistenciaPage() {
		ruta = "modulos/asistencia/mantenimientos/asistencia.xhtml";
	}

	public void getAsistenciaReporteAsistenciaPage() {
		ruta = "modulos/asistencia/procesos/reporteAsistencia.xhtml";
	}

	public void getVentasRankingVentasPage() {
		ruta = "modulos/ventas/mantenimientos/rankingVentas.xhtml";
	}
	
	public void getVentasDocumentoVentasPage() {
		ruta = "modulos/ventas/procesos/documentoVenta.xhtml";
	}
	
	public void getVentasPlantillaVentaPage() {
		ruta = "modulos/ventas/procesos/plantillaVenta.xhtml";
	}
	
	public void getVentasReporteDocumentoVentaPage() {
		ruta = "modulos/ventas/reportes/reporteDocumentoVenta.xhtml";
	}
	public void getVentasReporteDeudaPage() {
		ruta = "modulos/proyecto/reportes/reporteDeuda.xhtml";
	}
	public void getAlmacenInventarioPage() {
		ruta = "modulos/almacen/mantenimientos/inventario.xhtml";
	}

	public void getContratosPage() {
		ruta = "modulos/proyecto/mantenimientos/contrato.xhtml";
	}
	
	public void getProcesoCajaPage() {
		ruta = "modulos/caja/procesos/caja.xhtml";
	}
	
	public void getProcesoRequerimientoComprasPage() {
		ruta = "modulos/compra/procesos/requerimientoCompra.xhtml";
	}
	
	public void getProcesoDocumentoCompraPage() {
		ruta = "modulos/compra/procesos/documentoCompra.xhtml";
	}
	
	public void getProcesoMovBancarioPage() {
		ruta = "modulos/contabilidad/procesos/movimientosBanc.xhtml";
	}
	
	
	
	public void cerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
	

	
	
	
	
	

	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public int getIdAdministrador() {
		return idAdministrador;
	}
	public void setIdAdministrador(int idAdministrador) {
		this.idAdministrador = idAdministrador;
	}
	public int getIdSupervisor() {
		return idSupervisor;
	}
	public void setIdSupervisor(int idSupervisor) {
		this.idSupervisor = idSupervisor;
	}
	public int getIdAsesor() {
		return idAsesor;
	}
	public void setIdAsesor(int idAsesor) {
		this.idAsesor = idAsesor;
	}
	public boolean isMenuProspeccion() {
		return menuProspeccion;
	}
	public void setMenuProspeccion(boolean menuProspeccion) {
		this.menuProspeccion = menuProspeccion;
	}
	public boolean isMenuMantenimiento() {
		return menuMantenimiento;
	}
	public void setMenuMantenimiento(boolean menuMantenimiento) {
		this.menuMantenimiento = menuMantenimiento;
	}
	public boolean isMenuReporte() {
		return menuReporte;
	}
	public void setMenuReporte(boolean menuReporte) {
		this.menuReporte = menuReporte;
	}
	public boolean isSubMenuProspectos() {
		return subMenuProspectos;
	}
	public void setSubMenuProspectos(boolean subMenuProspectos) {
		this.subMenuProspectos = subMenuProspectos;
	}
	public boolean isSubMenuProspeccion() {
		return subMenuProspeccion;
	}
	public void setSubMenuProspeccion(boolean subMenuProspeccion) {
		this.subMenuProspeccion = subMenuProspeccion;
	}
	public boolean isSubMenuAgenda() {
		return subMenuAgenda;
	}
	public void setSubMenuAgenda(boolean subMenuAgenda) {
		this.subMenuAgenda = subMenuAgenda;
	}
	public boolean isSubMenuPersonas() {
		return subMenuPersonas;
	}
	public void setSubMenuPersonas(boolean subMenuPersonas) {
		this.subMenuPersonas = subMenuPersonas;
	}
	public boolean isSubMenuUsuarios() {
		return subMenuUsuarios;
	}
	public void setSubMenuUsuarios(boolean subMenuUsuarios) {
		this.subMenuUsuarios = subMenuUsuarios;
	}
	public boolean isSubMenuPerfiles() {
		return subMenuPerfiles;
	}
	public void setSubMenuPerfiles(boolean subMenuPerfiles) {
		this.subMenuPerfiles = subMenuPerfiles;
	}
	public boolean isSubMenuProyectos() {
		return subMenuProyectos;
	}
	public void setSubMenuProyectos(boolean subMenuProyectos) {
		this.subMenuProyectos = subMenuProyectos;
	}
	public boolean isSubMenuEquipos() {
		return subMenuEquipos;
	}
	public void setSubMenuEquipos(boolean subMenuEquipos) {
		this.subMenuEquipos = subMenuEquipos;
	}
	public boolean isSubMenuCambiarContrasenia() {
		return subMenuCambiarContrasenia;
	}
	public void setSubMenuCambiarContrasenia(boolean subMenuCambiarContrasenia) {
		this.subMenuCambiarContrasenia = subMenuCambiarContrasenia;
	}
	public boolean isSubMenuReporteAcciones() {
		return subMenuReporteAcciones;
	}
	public void setSubMenuReporteAcciones(boolean subMenuReporteAcciones) {
		this.subMenuReporteAcciones = subMenuReporteAcciones;
	}
	public int[] getPermisoProspectos() {
		return permisoProspectos;
	}
	public void setPermisoProspectos(int[] permisoProspectos) {
		this.permisoProspectos = permisoProspectos;
	}
	public int[] getPermisoProspeccion() {
		return permisoProspeccion;
	}
	public void setPermisoProspeccion(int[] permisoProspeccion) {
		this.permisoProspeccion = permisoProspeccion;
	}
	public int[] getPermisoAgenda() {
		return permisoAgenda;
	}
	public void setPermisoAgenda(int[] permisoAgenda) {
		this.permisoAgenda = permisoAgenda;
	}
	public int[] getPermisoPersonas() {
		return permisoPersonas;
	}
	public void setPermisoPersonas(int[] permisoPersonas) {
		this.permisoPersonas = permisoPersonas;
	}
	public int[] getPermisoUsuarios() {
		return permisoUsuarios;
	}
	public void setPermisoUsuarios(int[] permisoUsuarios) {
		this.permisoUsuarios = permisoUsuarios;
	}
	public int[] getPermisoPerfiles() {
		return permisoPerfiles;
	}
	public void setPermisoPerfiles(int[] permisoPerfiles) {
		this.permisoPerfiles = permisoPerfiles;
	}
	public int[] getPermisoProyectos() {
		return permisoProyectos;
	}
	public void setPermisoProyectos(int[] permisoProyectos) {
		this.permisoProyectos = permisoProyectos;
	}
	public int[] getPermisoEquipos() {
		return permisoEquipos;
	}
	public void setPermisoEquipos(int[] permisoEquipos) {
		this.permisoEquipos = permisoEquipos;
	}
	public int[] getPermisoCambiarConstrasenia() {
		return permisoCambiarConstrasenia;
	}
	public void setPermisoCambiarConstrasenia(int[] permisoCambiarConstrasenia) {
		this.permisoCambiarConstrasenia = permisoCambiarConstrasenia;
	}
	public int[] getPermisoReporteAcciones() {
		return permisoReporteAcciones;
	}
	public void setPermisoReporteAcciones(int[] permisoReporteAcciones) {
		this.permisoReporteAcciones = permisoReporteAcciones;
	}
	public boolean isSubMenuSimulador() {
		return subMenuSimulador;
	}
	public void setSubMenuSimulador(boolean subMenuSimulador) {
		this.subMenuSimulador = subMenuSimulador;
	}
	public boolean isMenuProyecto() {
		return menuProyecto;
	}
	public void setMenuProyecto(boolean menuProyecto) {
		this.menuProyecto = menuProyecto;
	}
	public int[] getPermisoSimulador() {
		return permisoSimulador;
	}
	public void setPermisoSimulador(int[] permisoSimulador) {
		this.permisoSimulador = permisoSimulador;
	}
	public boolean isSubMenuManzanas() {
		return subMenuManzanas;
	}
	public void setSubMenuManzanas(boolean subMenuManzanas) {
		this.subMenuManzanas = subMenuManzanas;
	}
	public boolean isSubMenuLotes() {
		return subMenuLotes;
	}
	public void setSubMenuLotes(boolean subMenuLotes) {
		this.subMenuLotes = subMenuLotes;
	}
	public int[] getPermisoManzanas() {
		return permisoManzanas;
	}
	public void setPermisoManzanas(int[] permisoManzanas) {
		this.permisoManzanas = permisoManzanas;
	}
	public int[] getPermisoLotes() {
		return permisoLotes;
	}
	public void setPermisoLotes(int[] permisoLotes) {
		this.permisoLotes = permisoLotes;
	}
	public int getIdAsistentenAdmin() {
		return idAsistentenAdmin;
	}
	public void setIdAsistentenAdmin(int idAsistentenAdmin) {
		this.idAsistentenAdmin = idAsistentenAdmin;
	}
	public boolean isSubMenuComisiones() {
		return subMenuComisiones;
	}
	public void setSubMenuComisiones(boolean subMenuComisiones) {
		this.subMenuComisiones = subMenuComisiones;
	}
	public int[] getPermisoComisiones() {
		return permisoComisiones;
	}
	public void setPermisoComisiones(int[] permisoComisiones) {
		this.permisoComisiones = permisoComisiones;
	}
	public boolean isSubMenuComision() {
		return subMenuComision;
	}
	public void setSubMenuComision(boolean subMenuComision) {
		this.subMenuComision = subMenuComision;
	}
	public int[] getPermisoComision() {
		return permisoComision;
	}
	public void setPermisoComision(int[] permisoComision) {
		this.permisoComision = permisoComision;
	}
	public boolean isSubMenuEmpleado() {
		return subMenuEmpleado;
	}
	public void setSubMenuEmpleado(boolean subMenuEmpleado) {
		this.subMenuEmpleado = subMenuEmpleado;
	}
	public int[] getPermisoEmpleado() {
		return permisoEmpleado;
	}
	public void setPermisoEmpleado(int[] permisoEmpleado) {
		this.permisoEmpleado = permisoEmpleado;
	}
	public boolean isMenuAsistencia() {
		return menuAsistencia;
	}
	public void setMenuAsistencia(boolean menuAsistencia) {
		this.menuAsistencia = menuAsistencia;
	}
	public boolean isSubMenuAsistencia() {
		return subMenuAsistencia;
	}
	public void setSubMenuAsistencia(boolean subMenuAsistencia) {
		this.subMenuAsistencia = subMenuAsistencia;
	}
	public int[] getPermisoAsistencia() {
		return permisoAsistencia;
	}
	public void setPermisoAsistencia(int[] permisoAsistencia) {
		this.permisoAsistencia = permisoAsistencia;
	}
	public boolean isSubMenuReporteAsistencia() {
		return subMenuReporteAsistencia;
	}
	public void setSubMenuReporteAsistencia(boolean subMenuReporteAsistencia) {
		this.subMenuReporteAsistencia = subMenuReporteAsistencia;
	}
	public int[] getPermisoReporteAsistencia() {
		return permisoReporteAsistencia;
	}
	public void setPermisoReporteAsistencia(int[] permisoReporteAsistencia) {
		this.permisoReporteAsistencia = permisoReporteAsistencia;
	}
	public int getIdAsistencia() {
		return idAsistencia;
	}
	public void setIdAsistencia(int idAsistencia) {
		this.idAsistencia = idAsistencia;
	}
	public int getIdRecursosHumanos() {
		return idRecursosHumanos;
	}
	public void setIdRecursosHumanos(int idRecursosHumanos) {
		this.idRecursosHumanos = idRecursosHumanos;
	}
	public boolean isSubMenuRequerimientoSeparacion() {
		return subMenuRequerimientoSeparacion;
	}
	public void setSubMenuRequerimientoSeparacion(boolean subMenuRequerimientoSeparacion) {
		this.subMenuRequerimientoSeparacion = subMenuRequerimientoSeparacion;
	}
	public int[] getPermisoRequerimientoSeparacion() {
		return permisoRequerimientoSeparacion;
	}
	public void setPermisoRequerimientoSeparacion(int[] permisoRequerimientoSeparacion) {
		this.permisoRequerimientoSeparacion = permisoRequerimientoSeparacion;
	}
	public boolean isMenuVentas() {
		return menuVentas;
	}
	public void setMenuVentas(boolean menuVentas) {
		this.menuVentas = menuVentas;
	}
	public boolean isSubMenuRankingVentas() {
		return subMenuRankingVentas;
	}
	public void setSubMenuRankingVentas(boolean subMenuRankingVentas) {
		this.subMenuRankingVentas = subMenuRankingVentas;
	}
	public int[] getPermisoRankingVentas() {
		return permisoRankingVentas;
	}
	public void setPermisoRankingVentas(int[] permisoRankingVentas) {
		this.permisoRankingVentas = permisoRankingVentas;
	}
	public int getIdContabilidad() {
		return idContabilidad;
	}
	public void setIdContabilidad(int idContabilidad) {
		this.idContabilidad = idContabilidad;
	}
	public boolean isSubMenuContrato() {
		return subMenuContrato;
	}
	public void setSubMenuContrato(boolean subMenuContrato) {
		this.subMenuContrato = subMenuContrato;
	}
	public Sucursal getSucursalLogin() {
		return sucursalLogin;
	}
	public void setSucursalLogin(Sucursal sucursalLogin) {
		this.sucursalLogin = sucursalLogin;
	}
	public int getIdAsistenteVenta() {
		return idAsistenteVenta;
	}
	public void setIdAsistenteVenta(int idAsistenteVenta) {
		this.idAsistenteVenta = idAsistenteVenta;
	}
	public int[] getPermisoContrato() {
		return permisoContrato;
	}
	public void setPermisoContrato(int[] permisoContrato) {
		this.permisoContrato = permisoContrato;
	}
	public boolean isSubMenuDocumentoVentas() {
		return subMenuDocumentoVentas;
	}
	public void setSubMenuDocumentoVentas(boolean subMenuDocumentoVentas) {
		this.subMenuDocumentoVentas = subMenuDocumentoVentas;
	}
	public int[] getPermisoReporteDocumentoVentas() {
		return permisoReporteDocumentoVentas;
	}
	public void setPermisoReporteDocumentoVentas(int[] permisoReporteDocumentoVentas) {
		this.permisoReporteDocumentoVentas = permisoReporteDocumentoVentas;
	}

	public boolean isSubMenuReporteDocumentoVenta() {
		return subMenuReporteDocumentoVenta;
	}
	public void setSubMenuReporteDocumentoVenta(boolean subMenuReporteDocumentoVenta) {
		this.subMenuReporteDocumentoVenta = subMenuReporteDocumentoVenta;
	}
	public int[] getPermisoDocumentoVenta() {
		return permisoDocumentoVenta;
	}
	public void setPermisoDocumentoVenta(int[] permisoDocumentoVenta) {
		this.permisoDocumentoVenta = permisoDocumentoVenta;
	}
	public boolean isMenuAlmacen() {
		return menuAlmacen;
	}
	public void setMenuAlmacen(boolean menuAlmacen) {
		this.menuAlmacen = menuAlmacen;
	}
	public boolean isSubMenuInventario() {
		return subMenuInventario;
	}
	public void setSubMenuInventario(boolean subMenuInventario) {
		this.subMenuInventario = subMenuInventario;
	}
	public int[] getPermisoInventario() {
		return permisoInventario;
	}
	public void setPermisoInventario(int[] permisoInventario) {
		this.permisoInventario = permisoInventario;
	}
	public boolean isSubMenuCliente() {
		return subMenuCliente;
	}
	public void setSubMenuCliente(boolean subMenuCliente) {
		this.subMenuCliente = subMenuCliente;
	}
	public int[] getPermisoCliente() {
		return permisoCliente;
	}
	public void setPermisoCliente(int[] permisoCliente) {
		this.permisoCliente = permisoCliente;
	}
	public String getRutaLogo() {
		return rutaLogo;
	}
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}
	public boolean isSubMenuPlantillaVenta() {
		return subMenuPlantillaVenta;
	}
	public void setSubMenuPlantillaVenta(boolean subMenuPlantillaVenta) {
		this.subMenuPlantillaVenta = subMenuPlantillaVenta;
	}
	public int[] getPermisoPlantillaVenta() {
		return permisoPlantillaVenta;
	}
	public void setPermisoPlantillaVenta(int[] permisoPlantillaVenta) {
		this.permisoPlantillaVenta = permisoPlantillaVenta;
	}
	public boolean isSubMenuMapeoLote() {
		return subMenuMapeoLote;
	}
	public void setSubMenuMapeoLote(boolean subMenuMapeoLote) {
		this.subMenuMapeoLote = subMenuMapeoLote;
	}
	public boolean isSubMenuVoucher() {
		return subMenuVoucher;
	}
	public void setSubMenuVoucher(boolean subMenuVoucher) {
		this.subMenuVoucher = subMenuVoucher;
	}
	public int[] getPermisoMapeoLote() {
		return permisoMapeoLote;
	}
	public void setPermisoMapeoLote(int[] permisoMapeoLote) {
		this.permisoMapeoLote = permisoMapeoLote;
	}
	public int[] getPermisoVoucher() {
		return permisoVoucher;
	}
	public void setPermisoVoucher(int[] permisoVoucher) {
		this.permisoVoucher = permisoVoucher;
	}
	public boolean isMenuRRHH() {
		return menuRRHH;
	}
	public void setMenuRRHH(boolean menuRRHH) {
		this.menuRRHH = menuRRHH;
	}
	public boolean isSubMenuPlanilla() {
		return subMenuPlanilla;
	}
	public void setSubMenuPlanilla(boolean subMenuPlanilla) {
		this.subMenuPlanilla = subMenuPlanilla;
	}
	public boolean isMenucaja() {
		return menucaja;
	}
	public void setMenucaja(boolean menucaja) {
		this.menucaja = menucaja;
	}
	public boolean isSubMenuCaja() {
		return subMenuCaja;
	}
	public void setSubMenuCaja(boolean subMenuCaja) {
		this.subMenuCaja = subMenuCaja;
	}
	public int[] getPermisoPlanilla() {
		return permisoPlanilla;
	}
	public void setPermisoPlanilla(int[] permisoPlanilla) {
		this.permisoPlanilla = permisoPlanilla;
	}
	public int[] getPermisoCaja() {
		return permisoCaja;
	}
	public void setPermisoCaja(int[] permisoCaja) {
		this.permisoCaja = permisoCaja;
	}
	public int[] getPermisoRequerimientoCompra() {
		return permisoRequerimientoCompra;
	}
	public void setPermisoRequerimientoCompra(int[] permisoRequerimientoCompra) {
		this.permisoRequerimientoCompra = permisoRequerimientoCompra;
	}
	public boolean isMenuCompra() {
		return menuCompra;
	}
	public void setMenuCompra(boolean menuCompra) {
		this.menuCompra = menuCompra;
	}
	public boolean isSubMenuRequerimientoCompra() {
		return subMenuRequerimientoCompra;
	}
	public void setSubMenuRequerimientoCompra(boolean subMenuRequerimientoCompra) {
		this.subMenuRequerimientoCompra = subMenuRequerimientoCompra;
	}
	public boolean isSubMenuDocumentoCompra() {
		return subMenuDocumentoCompra;
	}
	public void setSubMenuDocumentoCompra(boolean subMenuDocumentoCompra) {
		this.subMenuDocumentoCompra = subMenuDocumentoCompra;
	}
	public int[] getPermisoDocumentoCompra() {
		return permisoDocumentoCompra;
	}
	public void setPermisoDocumentoCompra(int[] permisoDocumentoCompra) {
		this.permisoDocumentoCompra = permisoDocumentoCompra;
	}
	public boolean isSubMenuReporteDeuda() {
		return subMenuReporteDeuda;
	}
	public void setSubMenuReporteDeuda(boolean subMenuReporteDeuda) {
		this.subMenuReporteDeuda = subMenuReporteDeuda;
	}
	public boolean isMenuContabilidad() {
		return menuContabilidad;
	}
	public void setMenuContabilidad(boolean menuContabilidad) {
		this.menuContabilidad = menuContabilidad;
	}
	public boolean isSubMenuMovimientosBanc() {
		return subMenuMovimientosBanc;
	}
	public void setSubMenuMovimientosBanc(boolean subMenuMovimientosBanc) {
		this.subMenuMovimientosBanc = subMenuMovimientosBanc;
	}
	public int[] getPermisoReporteDeuda() {
		return permisoReporteDeuda;
	}
	public void setPermisoReporteDeuda(int[] permisoReporteDeuda) {
		this.permisoReporteDeuda = permisoReporteDeuda;
	}
	public int[] getPermisoMovimientoBanc() {
		return permisoMovimientoBanc;
	}
	public void setPermisoMovimientoBanc(int[] permisoMovimientoBanc) {
		this.permisoMovimientoBanc = permisoMovimientoBanc;
	}
	public RequerimientoSeparacionService getRequerimientoSeparacionService() {
		return requerimientoSeparacionService;
	}
	public void setRequerimientoSeparacionService(RequerimientoSeparacionService requerimientoSeparacionService) {
		this.requerimientoSeparacionService = requerimientoSeparacionService;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}

	
	
}
