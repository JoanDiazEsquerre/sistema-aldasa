package com.model.aldasa.prospeccion.bean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.StreamedContent;

import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.DocumentoVentaService;

@ManagedBean
public class LoadImagePlantillaBean {
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	public StreamedContent graphicImage;
	 public String nombreArchivo = "";

	 public byte[] getImage() throws IOException {
		 String ruta = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+"0.png";
//		 String ruta = "/home/imagen/voucher_separaciones/0.png"; 
		 if(!nombreArchivo.equals("")) {
			 ruta = navegacionBean.getSucursalLogin().getEmpresa().getRutaPlantillaVenta()+nombreArchivo;
//			 ruta = "/home/imagen/voucher_separaciones/"+nombreArchivo;
		 }
		 
		 System.out.println(ruta);

		  File file = new File(ruta);
		  byte[] byteImage = Files.readAllBytes(file.toPath());     
		  return byteImage;
     
   }
	
	@PostConstruct
	public void init() {

           
	}

	public StreamedContent getGraphicImage() {
		return graphicImage;
	}

	public void setGraphicImage(StreamedContent graphicImage) {
		this.graphicImage = graphicImage;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}

	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}	
	
	
}
