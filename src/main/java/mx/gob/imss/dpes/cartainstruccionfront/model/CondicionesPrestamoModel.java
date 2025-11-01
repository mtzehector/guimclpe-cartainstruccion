/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Plazo;

/**
 *
 * @author osiris.hernandez
 */
public class CondicionesPrestamoModel extends BaseModel{
    @Getter @Setter double monto;   
    @Getter @Setter double montoTotalPagar;
    @Getter @Setter double descuentoMensual;
    @Getter @Setter double cat;
    @Getter @Setter Plazo plazo;
    
}
