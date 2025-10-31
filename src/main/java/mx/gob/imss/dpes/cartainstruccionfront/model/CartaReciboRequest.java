/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author juanf.barragan
 */
public class CartaReciboRequest extends BaseModel{
    
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private Long cveEntFin;
    @Getter
    @Setter
    private String periodo;
    @Getter
    @Setter
    private String proveedor;
    @Getter
    @Setter
    private String rfc;
    @Getter
    @Setter
    private String monto;
    @Getter
    @Setter
    private String montoLetra;
    @Getter
    @Setter
    private String mes;
    @Getter
    @Setter
    private String anio;
    @Getter
    @Setter
    private String primas;
    @Getter
    @Setter
    private String tasa;
    @Getter
    @Setter
    private String iva;
    @Getter
    @Setter
    private String demasia;
    @Getter
    @Setter
    private String neto;
    @Getter
    @Setter
    private String firma;
    @Getter
    @Setter
    private String refDocBoveda;
    
    
    
    
}
