package com.model.aldasa.proyecto.bean;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
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

import com.model.aldasa.entity.Banco;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Simulador;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.BancoService;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.MetaSupervisorService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.NumeroALetra;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class LoteBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService; 
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService; 
	
//	@ManagedProperty(value = "#{comisionesService}")
//	private DetalleComisionesService comisionesService;
	
	@ManagedProperty(value = "#{metaSupervisorService}")
	private MetaSupervisorService metaSupervisorService;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	
	private List<Lote> lstLotes;
	private List<Person> lstPerson;
	private List<Empleado> lstEmpleado;
	
	private StreamedContent fileImg;
		
	private Lote loteSelected;
	private Team teamSelected;
	private Usuario usuarioLogin;
	
	
	
	private Project projectFilter;
	private Manzana manzanaFilter;
	private Manzana manzanaFilterMapeo;
	private Person personVenta;
	
	private String status = "";
	private String tituloDialog;
	private String nombreLoteSelected="";
	private int cantidadLotes=0;
	
	private boolean loteVendido = false;
	
	private LazyDataModel<Lote> lstLoteLazy;
	private List<Manzana> lstManzana = new ArrayList<>();
	private List<Project> lstProject = new ArrayList<>();
	private List<Team> lstTeam;
	private List<Person> lstPersonAsesor = new ArrayList<>();
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");  
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy"); 
	SimpleDateFormat sdfDay = new SimpleDateFormat("dd"); 
	
	@PostConstruct
	public void init() {
		usuarioLogin = navegacionBean.getUsuarioLogin();
		lstProject= projectService.findByStatusAndSucursalOrderByNameAsc(true, navegacionBean.getSucursalLogin());;
		lstManzana = manzanaService.findByStatusOrderByNameAsc(true);
		lstPerson=personService.findByStatus(true);

		iniciarLazy();
//		cargarAsesorPorEquipo();
		lstTeam=teamService.findByStatus(true);
		
	}	  
	
	public void newLote() {
		nombreLoteSelected="";
		tituloDialog="NUEVO LOTE";
		
		loteSelected=new Lote();
		loteSelected.setStatus("Disponible");
	}
	
	public void modifyLote( ) {
		tituloDialog="MODIFICAR LOTE";
		
		if(loteSelected.getStatus().equals(EstadoLote.VENDIDO.getName())) {
			loteVendido = true;
		}else {
			loteVendido = false;
		}
		
		nombreLoteSelected="Manzana " + loteSelected.getManzana().getName()+" / Lote: "+loteSelected.getNumberLote();
				
		personVenta = loteSelected.getPersonVenta();
		
		Person personAsesor = loteSelected.getPersonAssessor();
		Usuario usuarioAsesor = usuarioService.findByPerson(loteSelected.getPersonAssessor()); 
		if(usuarioAsesor != null) {
			teamSelected = usuarioAsesor.getTeam();
		}else {
			teamSelected = null;
		}
		cargarAsesorPorEquipo();
		loteSelected.setPersonAssessor(personAsesor);
				
		
	}
	
	
	public void calcularFechaVencimiento() {
		if(loteSelected.getFechaSeparacion() != null) {
			loteSelected.setFechaVencimiento(sumarDiasAFecha(loteSelected.getFechaSeparacion(), 7));
		}
	}
	
	public Date sumarDiasAFecha(Date fecha, int dias){
	      if (dias==0) return fecha;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      return calendar.getTime(); 
	}
	

	public void listarLotes(){	
		lstLotes = new ArrayList<>();
		if(!lstManzana.isEmpty()) {
			for(Manzana m:lstManzana) {
				List<Lote> lstLotesPorMz = loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(projectFilter, m, "%%");
				if(!lstLotesPorMz.isEmpty()) {
					lstLotes.addAll(lstLotesPorMz);
					Lote lot = new Lote();
					lot.setId(0);
					lot.setNumberLote("x");
					lstLotes.add(lot);
				}
			}
		}
//		lstLotes= loteService.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(projectFilter,manzanaFilterMapeo, "%%");
		cantidadLotes=0;
		
		if(!lstLotes.isEmpty()) {
			
			cantidadLotes = lstLotes.size();
			
		}
	}
	
	public void iniciarLazy() {
		lstLoteLazy = new LazyDataModel<Lote>() {
			private List<Lote> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Lote getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Lote lote : datasource) {
                    if (lote.getId() == intRowKey) {
                        return lote;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Lote lote) {
                return String.valueOf(lote.getId());
            }

			@Override
			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
				
				String numberLote="%"+ (filterBy.get("numberLote")!=null?filterBy.get("numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String manzana = "";
				if(manzanaFilter != null) {
					manzana = manzanaFilter.getName();
				}
				
				String proyecto = "%%";
				if(projectFilter != null) {
					proyecto = projectFilter.getName();
				}
				
                                
                Sort sort=Sort.by("numberLote").ascending();
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
                
				Page<Lote> pageLote;
//				if(projectFilter.equals("")) {
//					pageLote= loteService.findAllByNumberLoteLikeAndManzanaNameLikeAndStatusLike(numberLote,"%"+manzana+"%","%"+status+"%", pageable);
//				}else {
					pageLote= loteService.findAllByNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndStatusLikeAndProjectSucursal(numberLote, "%"+manzana+"%",proyecto,"%"+status+"%", navegacionBean.getSucursalLogin(), pageable);
				
//				}
				setRowCount((int) pageLote.getTotalElements());
				return datasource = pageLote.getContent();
			}
		};
	}
	
	public void fileDownloadView() {
        fileImg = DefaultStreamedContent.builder()
                .name("proyecto"+projectFilter.getId()+".jpg")
                .contentType("image/jpg")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/recursos/images/proyectos/proyecto"+projectFilter.getId()+".jpg"))
                .build();
    }
	
	public void saveLote() {	
		if(loteSelected.getNumberLote().equals("") || loteSelected.getNumberLote()==null) {
			addErrorMessage("Ingresar número de lote.");
			return ;
		}
		
		if(loteSelected.getManzana()==null) {
			addErrorMessage("Seleccionar una manzana.");
			return ;
		} 
		
		if(loteSelected.getProject()==null) {
			addErrorMessage("Seleccionar un proyecto.");
			return ;
		} 
		

		if(loteSelected.getStatus().equals(EstadoLote.SEPARADO.getName())) {
			if(loteSelected.getFechaSeparacion() == null) {
				addErrorMessage("Seleccionar una fecha separación.");
				return ;
			}
			
			if(loteSelected.getFechaVencimiento() == null) {
				addErrorMessage("Seleccionar una fecha vencimiento.");
				return ;
			}
			
			if(teamSelected == null) {
				addErrorMessage("Seleccionar equipo.");
				return ;
			}else if (teamSelected != null) {
				loteSelected.setPersonSupervisor(teamSelected.getPersonSupervisor());
			}
			
			if (loteSelected.getPersonAssessor() == null) {
				addErrorMessage("Ingresar asesor.");
				return ;
			}
		}

//		if(loteSelected.getStatus().equals(EstadoLote.VENDIDO.getName())) {
//			if(personVenta == null) {
//				addErrorMessage("Completar todos los datos de venta.");
//				return ;
//			}else if (personVenta != null) {
//				loteSelected.setPersonVenta(personVenta);
//			}
//			
//			if(teamSelected == null) {
//				addErrorMessage("Seleccionar un equipo");
//				return ;
//			}else if (teamSelected != null) {
//				loteSelected.setPersonSupervisor(teamSelected.getPersonSupervisor());
//			}
//			
//			if (loteSelected.getPersonAssessor() == null) {
//				addErrorMessage("Seleccionar un asesor.");
//				return ;
//			}
//			
//			if(loteSelected.getFechaVendido() == null) {
//				addErrorMessage("Completar todos los datos de venta.");
//				return ;
//			}
//			
//			if(loteSelected.getTipoPago().equals("Contado")) {
//				loteSelected.setMontoInicial(null);
//				loteSelected.setNumeroCuota(null);
//				if (loteSelected.getMontoVenta() == null) {
//					addErrorMessage("Completar todos los datos de venta.");
//					return ;
//				}
//			}
//			
//			if(loteSelected.getTipoPago().equals("Crédito")) {
//				if (loteSelected.getMontoVenta() == null || loteSelected.getMontoInicial() == null || loteSelected.getNumeroCuota() == null || loteSelected.getInteres() == null) {
//					addErrorMessage("Completar todos los datos de venta.");
//					return ;
//				}
//			}
//		} else {
//			loteSelected.setMontoVenta(null);
//			loteSelected.setTipoPago(null);
//		}
		//********************************
		
		if (tituloDialog.equals("NUEVO LOTE")) {
			Lote validarExistencia = loteService.findByNumberLoteAndManzanaAndProject(loteSelected.getNumberLote(), loteSelected.getManzana(), loteSelected.getProject());
			if (validarExistencia == null) {
				loteSelected.setRealizoContrato("N");
				Lote lote = loteService.save(loteSelected);
//				generarComision(lote);
				newLote();
				
				if(lote.getStatus().equals(EstadoLote.VENDIDO.getName())) {
					loteVendido=true;
				}else {
					loteVendido=false;
				}
				PrimeFaces.current().executeScript("PF('loteDialog').hide();");
				addInfoMessage("Se guardo correctamente.");
			} else { 
				addErrorMessage("El lote ya existe.");
			}
		} else {
			Lote validarExistencia = loteService.findByNumberLoteAndManzanaAndProjectException(loteSelected.getNumberLote(), loteSelected.getManzana().getId(), loteSelected.getProject().getId(), loteSelected.getId());
			if (validarExistencia == null) {
				Lote lote = loteService.save(loteSelected);
//				generarComision(lote);
				
				if(lote.getStatus().equals(EstadoLote.VENDIDO.getName())) {
					loteVendido=true;
				}else {
					loteVendido=false;
				}
				PrimeFaces.current().executeScript("PF('loteDialog').hide();");
				addInfoMessage("Se guardo correctamente.");
				nombreLoteSelected="Manzana " + loteSelected.getManzana().getName()+"/ lote: "+loteSelected.getNumberLote();
			} else { 
				addErrorMessage("El lote ya existe.");

			}
		}
	}
	
	
	public void generarComision(Lote lote) {
		
//		if (lote.getStatus().equals(EstadoLote.VENDIDO.getName())) {
//			
//			Comisiones validarExistencia = comisionesService.findByLote(lote);
//			Usuario usuarioAsesor = usuarioService.findByPerson(lote.getPersonAssessor());
//			Comision comision = comisionService.findByFechaIniLessThanEqualAndFechaCierreGreaterThanEqual(lote.getFechaVendido(), lote.getFechaVendido());
//		
//			if(comision != null){
//				
//				Comisiones comisiones = new Comisiones();
//				if(validarExistencia != null) {
//					comisiones.setId(validarExistencia.getId());
//				}
//				comisiones.setLote(lote);
//				if(usuarioAsesor.getTeam().getName().equals("ONLINE")) {
//					List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonAssessorDniAndTipoPagoAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), lote.getPersonAssessor().getDni(),lote.getTipoPago(), comision.getFechaIni(), comision.getFechaCierre());
//					if(lstLotesVendidos.size()==1) {
//						if (lote.getTipoPago().equals("Contado")) {
//							comisiones.setComisionAsesor(comision.getPrimeraVentaContadoOnline());
//						}else {
//							comisiones.setComisionAsesor(comision.getPrimeraVentaCreditoOnline());
//						}
//					}else {
//						if (lote.getTipoPago().equals("Contado")) {
//							comisiones.setComisionAsesor(comision.getBonoContadoOnline());
//						}else {
//							comisiones.setComisionAsesor(comision.getBonoCreditoOnline());
//						}
//					}
//					BigDecimal multiplica = lote.getMontoVenta().multiply(comision.getComisionSupervisorOnline());
//					comisiones.setComisionSupervisor(multiplica.divide(new BigDecimal(100)));
//					comisiones.setTipoEmpleado("O");
//					
//				}else if (usuarioAsesor.getTeam().getName().equals("INTERNOS") || usuarioAsesor.getTeam().getName().equals("EXTERNOS")) {
//					if (lote.getTipoPago().equals("Contado")) {
//						BigDecimal multiplica = lote.getMontoVenta().multiply(new BigDecimal(comision.getComisionContado()));
//						comisiones.setComisionAsesor(multiplica.divide(new BigDecimal(100)));
//					}else {
//						BigDecimal multiplica = lote.getMontoVenta().multiply(new BigDecimal(comision.getComisionCredito()));
//						comisiones.setComisionAsesor(multiplica.divide(new BigDecimal(100)));
//					}
//					comisiones.setComisionSupervisor(BigDecimal.ZERO);
//					comisiones.setTipoEmpleado(usuarioAsesor.getTeam().getName().equals("INTERNOS")?"I": "E");
//					
//				}else {
//					List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), lote.getPersonSupervisor(), "%%", comision.getFechaIni(), comision.getFechaCierre());
//					
//					MetaSupervisor metaSupervisor = metaSupervisorService.findByComisionAndEstadoAndPersonSupervisor(comision, true, lote.getPersonSupervisor());
//					
//					boolean alcanzaMeta = false;
//					if (metaSupervisor != null) {
//						int meta  = metaSupervisor.getMeta();
//						if(lstLotesVendidos.size() >= meta) {
//							alcanzaMeta = true;
//							for (Lote lt:lstLotesVendidos) {
//								Comisiones comConsulta = comisionesService.findByLote(lt);
//								if(comConsulta != null) {
//									BigDecimal multiplica = lt.getMontoVenta().multiply(new BigDecimal(comision.getComisionMetaSupervisor()));
//									comConsulta.setComisionSupervisor(multiplica.divide(new BigDecimal(100)));
//									comisionesService.save(comConsulta);
//
//								}
//							} 
//						}
//					}
//					
//					if (lote.getTipoPago().equals("Contado")) {
//						BigDecimal multiplica = lote.getMontoVenta().multiply(new BigDecimal(comision.getComisionContado()));
//						comisiones.setComisionAsesor(multiplica.divide(new BigDecimal(100)));
//					}else {
//						BigDecimal multiplica = lote.getMontoVenta().multiply(new BigDecimal(comision.getComisionCredito()));
//						comisiones.setComisionAsesor(multiplica.divide(new BigDecimal(100)));
//					}
//					
//					if(!alcanzaMeta) {
//						BigDecimal multiplica = lote.getMontoVenta().multiply(new BigDecimal(comision.getComisionSupervisor()));
//						comisiones.setComisionSupervisor(multiplica.divide(new BigDecimal(100)));
//
//					}else {
//						BigDecimal multiplica = lote.getMontoVenta().multiply(new BigDecimal(comision.getComisionMetaSupervisor()));
//						comisiones.setComisionSupervisor(multiplica.divide(new BigDecimal(100)));
//					}
//
//				}
//				BigDecimal multiplica = lote.getMontoVenta().multiply(new BigDecimal(comision.getSubgerente()));
//				comisiones.setComisionSubgerente(multiplica.divide(new BigDecimal(100)));
//				comisiones.setEstado(true);
//				comisionesService.save(comisiones);
//			}
//
//		}else {
//			Comisiones comisionesrelacionados = comisionesService.findByLote(lote);
//			
//			if (comisionesrelacionados != null) {
//				comisionesrelacionados.setEstado(false);
//				comisionesService.save(comisionesrelacionados);
//
//			}
//		}
	}
	
	public void calcularAreaPerimetro() {
		if(loteSelected.getMedidaFrontal() != null && loteSelected.getMedidaFrontal() >0) {
			if(loteSelected.getMedidaDerecha() != null && loteSelected.getMedidaDerecha() >0) {
				if(loteSelected.getMedidaIzquierda() != null && loteSelected.getMedidaIzquierda() >0) {
					if(loteSelected.getMedidaFondo() != null && loteSelected.getMedidaFondo() >0) {
						double area1 = (loteSelected.getMedidaFrontal()*loteSelected.getMedidaDerecha())/2;
						double area2 = (loteSelected.getMedidaIzquierda()*loteSelected.getMedidaFondo())/2;
						
						loteSelected.setArea(area1+area2);
						loteSelected.setPerimetro(loteSelected.getMedidaFrontal()+loteSelected.getMedidaDerecha()+loteSelected.getMedidaIzquierda()+loteSelected.getMedidaFondo());
					}
				}
			}
		}
	}
	
	public Converter getConversorTeam() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Team c = null;
                    for (Team si : lstTeam) {
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
                    return ((Team) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorManzana() {
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
	
	public Converter getConversorPerson() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Person c = null;
                    for (Person si : lstPerson) {
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
	
	public List<Manzana> completeManzana(String query) {
        List<Manzana> lista = new ArrayList<>();
        for (Manzana c : lstManzana) {
            if (c.getName().toUpperCase().contains(query.toUpperCase()) ) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	public List<Person> completePerson(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : getLstPerson()) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase()) || c.getDni().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	public void cargarAsesorPorEquipo() {
		lstPersonAsesor = new ArrayList<>();
//		List<Usuario> lstUsuarios = new ArrayList<>();
		
		loteSelected.setPersonAssessor(null);
		if(teamSelected!= null) {
//			lstUsuarios = usuarioService.findByTeam(teamSelected);
			lstEmpleado = empleadoService.findByEstadoAndTeam(true, teamSelected);
		}
		
		if(!lstEmpleado.isEmpty()){
			for(Empleado user : lstEmpleado) {
				lstPersonAsesor.add(user.getPerson());
			}
		}
	}
	
	public Converter getConversorPersonAsesor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Person c = null;
                    for (Person si : lstPersonAsesor) {
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
	
	public List<Person> completePersonAsesor(String query) {
        List<Person> lista = new ArrayList<>();
        for (Person c : lstPersonAsesor) {
            if (c.getSurnames().toUpperCase().contains(query.toUpperCase()) || c.getNames().toUpperCase().contains(query.toUpperCase())) {
                lista.add(c);
            }
        }
        return lista;
    }
	
	
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public LazyDataModel<Lote> getLstLoteLazy() {
		return lstLoteLazy;
	}
	public void setLstLoteLazy(LazyDataModel<Lote> lstLoteLazy) {
		this.lstLoteLazy = lstLoteLazy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Manzana> getLstManzana() {
		return lstManzana;
	}
	public void setLstManzana(List<Manzana> lstManzana) {
		this.lstManzana = lstManzana;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public List<Project> getLstProject() {
		return lstProject;
	}
	public void setLstProject(List<Project> lstProject) {
		this.lstProject = lstProject;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public Project getProjectFilter() {
		return projectFilter;
	}
	public void setProjectFilter(Project projectFilter) {
		this.projectFilter = projectFilter;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	
	public String getNombreLoteSelected() {
		return nombreLoteSelected;
	}
	public void setNombreLoteSelected(String nombreLoteSelected) {
		this.nombreLoteSelected = nombreLoteSelected;
	}
	public Manzana getManzanaFilter() {
		return manzanaFilter;
	}
	public void setManzanaFilter(Manzana manzanaFilter) {
		this.manzanaFilter = manzanaFilter;
	}
	public List<Lote> getLstLotes() {
		return lstLotes;
	}
	public void setLstLotes(List<Lote> lstLotes) {
		this.lstLotes = lstLotes;
	}
	public Manzana getManzanaFilterMapeo() {
		return manzanaFilterMapeo;
	}
	public void setManzanaFilterMapeo(Manzana manzanaFilterMapeo) {
		this.manzanaFilterMapeo = manzanaFilterMapeo;
	}
	public int getCantidadLotes() {
		return cantidadLotes;
	}
	public void setCantidadLotes(int cantidadLotes) {
		this.cantidadLotes = cantidadLotes;
	}
	public List<Person> getLstPerson() {
		return lstPerson;
	}
	public void setLstPerson(List<Person> lstPerson) {
		this.lstPerson = lstPerson;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public Person getPersonVenta() {
		return personVenta;
	}
	public void setPersonVenta(Person personVenta) {
		this.personVenta = personVenta;
	}
	public Team getTeamSelected() {
		return teamSelected;
	}
	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
	}
	public List<Team> getLstTeam() {
		return lstTeam;
	}
	public void setLstTeam(List<Team> lstTeam) {
		this.lstTeam = lstTeam;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public List<Person> getLstPersonAsesor() {
		return lstPersonAsesor;
	}
	public void setLstPersonAsesor(List<Person> lstPersonAsesor) {
		this.lstPersonAsesor = lstPersonAsesor;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
//	public DetalleComisionesService getComisionesService() {
//		return comisionesService;
//	}
//	public void setComisionesService(DetalleComisionesService comisionesService) {
//		this.comisionesService = comisionesService;
//	}
	public MetaSupervisorService getMetaSupervisorService() {
		return metaSupervisorService;
	}
	public void setMetaSupervisorService(MetaSupervisorService metaSupervisorService) {
		this.metaSupervisorService = metaSupervisorService;
	}
	public boolean isLoteVendido() {
		return loteVendido;
	}
	public void setLoteVendido(boolean loteVendido) {
		this.loteVendido = loteVendido;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public SimpleDateFormat getSdfM() {
		return sdfM;
	}
	public void setSdfM(SimpleDateFormat sdfM) {
		this.sdfM = sdfM;
	}
	public SimpleDateFormat getSdfY() {
		return sdfY;
	}
	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}
	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}
	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}
	public StreamedContent getFileImg() {
		return fileImg;
	}
	public void setFileImg(StreamedContent fileImg) {
		this.fileImg = fileImg;
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

	
}
