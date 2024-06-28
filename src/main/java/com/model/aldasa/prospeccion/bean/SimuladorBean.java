package com.model.aldasa.prospeccion.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.model.aldasa.entity.Simulador;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class SimuladorBean extends BaseBean  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private BigDecimal montoTotal,montoInteres;
	private BigDecimal montoInicial;
	private BigDecimal montoDeuda=BigDecimal.ZERO;
	private Integer numeroCuotas;
	private Integer porcentaje=0; 
	private List<Simulador> lstSimulador = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		limpiar();
	}
	
	public void limpiar() {
		
		montoTotal=null;
		montoInicial=null;
		montoDeuda=BigDecimal.ZERO;
		porcentaje=0; 
		numeroCuotas=6;
		lstSimulador.clear();
	}

	public void calcularPorcentaje() {
		switch (numeroCuotas) {
		case 6:
			porcentaje=0;
			break;
		case 12:
			porcentaje=6;
			break;
		case 18:
			porcentaje=9;
			break;
		case 24:
			porcentaje=12;
			break;
		case 36:
			porcentaje=18;
			break;
		case 48:
			porcentaje=24;
			break;
		case 60:
			porcentaje=30;
			break;

		}
	}
	
	public void calcularMontoDeuda() {
		lstSimulador.clear();

		if (montoTotal == null || montoInicial == null) {
			montoDeuda = BigDecimal.ZERO;
		} else{
			montoDeuda = montoTotal.subtract(montoInicial);
		}
		
	}
	
	public void simularCuotas () {
		lstSimulador.clear();
		
		if(montoTotal==null) {
			addErrorMessage("Ingresar monto total.");
			return;
		}else if(montoTotal==BigDecimal.ZERO) {
			addErrorMessage("El monto total debe ser mayor a 0.");
			return;
		}
		
		if(montoInicial==null) {
			addErrorMessage("Ingresar monto inicial.");
			return;
		}else if(montoInicial==BigDecimal.ZERO) {
			addErrorMessage("El monto inicial debe ser mayor a 0.");
			return;
		}
		
		if(porcentaje==null) {
           addErrorMessage("Ingrese el porcentaje de interés");
            return;
        }
		
		
		Simulador filaInicio = new Simulador();
		filaInicio.setNroCuota("0");
		filaInicio.setInicial(montoInicial);
		filaInicio.setCuotaSI(BigDecimal.ZERO);
		filaInicio.setInteres(BigDecimal.ZERO);
		filaInicio.setCuotaTotal(BigDecimal.ZERO);
		lstSimulador.add(filaInicio);
		
		if(porcentaje==0) {
			montoInteres=BigDecimal.ZERO;
			
			BigDecimal cuota = montoDeuda.divide(new BigDecimal(numeroCuotas), 2, RoundingMode.HALF_UP);
			
			
			BigDecimal sumaTotal=BigDecimal.ZERO;
			List<Simulador> listaPrevia = new ArrayList<>();
			BigDecimal sumaDecimales = BigDecimal.ZERO;
			BigDecimal sumaCuotaSI = BigDecimal.ZERO;
			
			for(int i=0; i<numeroCuotas-1;i++) {
				Simulador filaCouta = new Simulador();
				filaCouta.setNroCuota((i+2)+"");
				filaCouta.setInicial(BigDecimal.ZERO);
				filaCouta.setCuotaSI(cuota);
				filaCouta.setInteres(BigDecimal.ZERO);
				filaCouta.setCuotaTotal(cuota);
								
				
				sumaTotal = sumaTotal.add(filaCouta.getCuotaSI());
				
				String decimalCuotaTotal = filaCouta.getCuotaTotal().toString();
				String separador = Pattern.quote(".");
				String[] partes = decimalCuotaTotal.split(separador);
				if(partes.length>1) {
					String enteroTexto = partes[0];
					BigDecimal entero = new  BigDecimal(enteroTexto);
					BigDecimal decimal = cuota.subtract(entero);
					sumaDecimales = sumaDecimales.add(decimal);
					
					filaCouta.setCuotaSI(filaCouta.getCuotaSI().subtract(decimal));
					filaCouta.setCuotaTotal(filaCouta.getCuotaSI());
				}
				sumaCuotaSI = sumaCuotaSI.add(filaCouta.getCuotaSI());
				listaPrevia.add(filaCouta);
			}
			
			Simulador filaPrimeraCuota = new Simulador();
			filaPrimeraCuota.setNroCuota("1");
			filaPrimeraCuota.setInicial(BigDecimal.ZERO);
			filaPrimeraCuota.setCuotaSI(cuota.add(sumaDecimales));
			filaPrimeraCuota.setInteres(BigDecimal.ZERO);
			filaPrimeraCuota.setCuotaTotal(cuota.add(sumaDecimales));
			
			sumaCuotaSI=sumaCuotaSI.add(filaPrimeraCuota.getCuotaSI());
			
			BigDecimal decimales = sumaCuotaSI.subtract(montoDeuda);
			filaPrimeraCuota.setCuotaSI(filaPrimeraCuota.getCuotaSI().subtract(decimales));
			filaPrimeraCuota.setCuotaTotal(filaPrimeraCuota.getCuotaSI());
			
			lstSimulador.add(filaPrimeraCuota);
			
			
			for(Simulador sim:listaPrevia) {
				lstSimulador.add(sim);
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(montoInicial);
			filaTotal.setCuotaSI(montoDeuda);
			filaTotal.setInteres(BigDecimal.ZERO);
			filaTotal.setCuotaTotal(montoDeuda);
			lstSimulador.add(filaTotal);
			
		}else {
			
			BigDecimal porc= new BigDecimal(porcentaje);
			BigDecimal porcMin= porc.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
			
			BigDecimal cuota = montoDeuda.divide(new BigDecimal(numeroCuotas), 2, RoundingMode.HALF_UP);
			BigDecimal interesCuota = cuota.multiply(porcMin);
			List<Simulador> listaPrevia = new ArrayList<>();
			BigDecimal sumaDecimales = BigDecimal.ZERO;
			BigDecimal sumaCuotaSI = BigDecimal.ZERO;
			
			for(int i=0; i<numeroCuotas-1;i++) {
				Simulador filaCouta = new Simulador();
				filaCouta.setNroCuota((i+2)+"");
				filaCouta.setInicial(BigDecimal.ZERO);
				filaCouta.setCuotaSI(cuota);
				filaCouta.setInteres(interesCuota);
				filaCouta.setCuotaTotal(filaCouta.getCuotaSI().add(filaCouta.getInteres()));
								
				String decimalCuotaTotal = filaCouta.getCuotaTotal().toString();
				String separador = Pattern.quote(".");
				String[] partes = decimalCuotaTotal.split(separador);
				if(partes.length>1) {
					String enteroTexto = partes[0];
					BigDecimal entero = new  BigDecimal(enteroTexto);
					BigDecimal decimal = cuota.add(interesCuota).subtract(entero);
					sumaDecimales = sumaDecimales.add(decimal);
					
					filaCouta.setCuotaSI(filaCouta.getCuotaSI().subtract(decimal));
					filaCouta.setCuotaTotal(filaCouta.getCuotaSI().add(filaCouta.getInteres()));
				}
				sumaCuotaSI = sumaCuotaSI.add(filaCouta.getCuotaSI());
				listaPrevia.add(filaCouta);
			}
			
			Simulador filaPrimeraCuota = new Simulador();
			filaPrimeraCuota.setNroCuota("1");
			filaPrimeraCuota.setInicial(BigDecimal.ZERO);
			filaPrimeraCuota.setCuotaSI(montoDeuda.subtract(sumaCuotaSI));
			filaPrimeraCuota.setInteres(interesCuota);
			filaPrimeraCuota.setCuotaTotal(filaPrimeraCuota.getCuotaSI().add(filaPrimeraCuota.getInteres()));
			lstSimulador.add(filaPrimeraCuota);
			
			for(Simulador sim:listaPrevia) {
				lstSimulador.add(sim);
			}
			
			BigDecimal sumaInicial = BigDecimal.ZERO;
			BigDecimal sumaSI = BigDecimal.ZERO;
			BigDecimal sumaInteres = BigDecimal.ZERO;
			BigDecimal sumaTotal = BigDecimal.ZERO;
			for(Simulador sim:lstSimulador) {
				sumaInicial=sumaInicial.add(sim.getInicial());
				sumaSI=sumaSI.add(sim.getCuotaSI());
				sumaInteres=sumaInteres.add(sim.getInteres());
				sumaTotal=sumaTotal.add(sim.getCuotaTotal());
			}
			
			Simulador filaTotal = new Simulador();
			filaTotal.setNroCuota("TOTAL");
			filaTotal.setInicial(sumaInicial);
			filaTotal.setCuotaSI(sumaSI);
			filaTotal.setInteres(sumaInteres);
			filaTotal.setCuotaTotal(sumaTotal);
			lstSimulador.add(filaTotal);
			
		}	
	}
	
	
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}
	public BigDecimal getMontoInteres() {
		return montoInteres;
	}
	public void setMontoInteres(BigDecimal montoInteres) {
		this.montoInteres = montoInteres;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public BigDecimal getMontoDeuda() {
		return montoDeuda;
	}
	public void setMontoDeuda(BigDecimal montoDeuda) {
		this.montoDeuda = montoDeuda;
	}
	public Integer getNumeroCuotas() {
		return numeroCuotas;
	}
	public void setNumeroCuotas(Integer numeroCuotas) {
		this.numeroCuotas = numeroCuotas;
	}
	public Integer getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Integer porcentaje) {
		this.porcentaje = porcentaje;
	}
	public List<Simulador> getLstSimulador() {
		return lstSimulador;
	}
	public void setLstSimulador(List<Simulador> lstSimulador) {
		this.lstSimulador = lstSimulador;
	}
	

}
