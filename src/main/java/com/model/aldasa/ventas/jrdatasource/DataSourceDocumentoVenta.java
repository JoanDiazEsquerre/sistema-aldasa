package com.model.aldasa.ventas.jrdatasource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.Lote;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Jhon
 */

public class DataSourceDocumentoVenta implements JRDataSource{
    private List<DetalleDocumentoVenta> listaDDV= new ArrayList<>();
    private int indice = -1;
    
    @Override
    public boolean next() throws JRException {
       return ++indice < listaDDV.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;  // busca el modelo de boleta el pdf

            if("DESCRIPCION".equals(jrf.getName())) { 
                valor = listaDDV.get(indice).getDescripcion();                        
            }else if ("NUMERO".equals(jrf.getName())){
                valor = listaDDV.get(indice).getDocumentoVenta().getSerie() + " - " + listaDDV.get(indice).getDocumentoVenta().getNumero(); 
            }else if ("AMORTIZACION".equals(jrf.getName())){
                valor = listaDDV.get(indice).getAmortizacion(); 
            }else if ("INTERES".equals(jrf.getName())){
                valor =listaDDV.get(indice).getInteres(); 
            }else if ("IMPORTEVENTA".equals(jrf.getName())){
                valor = listaDDV.get(indice).getImporteVenta(); 
            }else if ("NOMBREDOCUMENTO".equals(jrf.getName())){
                valor = listaDDV.get(indice).getDocumentoVenta().getTipoDocumento().getAbreviatura();
            }     
            return valor; 
    }
    
    public void addResumenDetalle(DetalleDocumentoVenta ddv){
        this.listaDDV.add(ddv);
    }

	


    
    
}
