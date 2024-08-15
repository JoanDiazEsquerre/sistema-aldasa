package com.model.aldasa.proyecto.jrdatasource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.model.aldasa.entity.Cuota;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DataSourceCronogramaPago implements JRDataSource{
 	private List<Cuota> lstCuota= new ArrayList<>();
    private int indice = -1;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    @Override
    public boolean next() throws JRException {
       return ++indice < lstCuota.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;  // busca el modelo de boleta el pdf

            if("NRO_CUOTA".equals(jrf.getName())) { 
            	valor = lstCuota.get(indice).isPrepago()==true?"PREPAGO" : lstCuota.get(indice).getNroCuota();
            }else if ("PERIODO".equals(jrf.getName())){
                valor = sdf.format(lstCuota.get(indice).getFechaPago()) ; 
            }else if ("CUOTASI".equals(jrf.getName())){
                valor = lstCuota.get(indice).getCuotaSI(); 
            }else if ("INTERES".equals(jrf.getName())){
                valor =lstCuota.get(indice).getInteres(); 
            }else if ("CUOTATOTAL".equals(jrf.getName())){
            	valor = lstCuota.get(indice).getCuotaTotal().subtract(lstCuota.get(indice).getAdelanto()); 
            } else if ("ESTADOPAGO".equals(jrf.getName())){
                valor = lstCuota.get(indice).isPrepago()==true?"PREPAGO" : lstCuota.get(indice).getPagoTotal().equals("S")?"PAGADO":"PENDIENTE";
            } else if ("ADELANTO".equals(jrf.getName())){
                valor = lstCuota.get(indice).getAdelanto(); 
            }
            return valor; 
    }
    
    public void addResumenDetalle(Cuota ddv){
        this.lstCuota.add(ddv);
    }

}
