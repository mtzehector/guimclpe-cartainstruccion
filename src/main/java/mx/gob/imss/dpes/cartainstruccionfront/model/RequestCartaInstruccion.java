/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.personaef.model.PersonaEF;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;

/**
 *
 * @author edgar.arenas
 */
public class RequestCartaInstruccion extends BaseModel {
    
    @Getter @Setter private Prestamo prestamo = new Prestamo(); 
    @Getter @Setter private PersonaEF personaEf;
    @Getter @Setter private Integer flatPrestamoPromotor;
    @Getter @Setter private Pensionado pensionado;
    
}
