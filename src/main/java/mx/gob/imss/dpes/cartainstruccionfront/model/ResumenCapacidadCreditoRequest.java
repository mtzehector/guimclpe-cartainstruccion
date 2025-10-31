/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Persona;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author salvador.pocteco
 */
public class ResumenCapacidadCreditoRequest extends BaseModel{
    
    @Getter @Setter private Solicitud solicitud;
    //@Getter @Setter private Persona pensionado;
    @Getter @Setter private Pensionado pensionado;
    @Getter @Setter private CapacidadCredito capacidadCredito;
}
