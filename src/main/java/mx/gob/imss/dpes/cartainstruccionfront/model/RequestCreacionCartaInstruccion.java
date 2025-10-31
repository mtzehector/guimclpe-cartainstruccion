/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Reporte;
import mx.gob.imss.dpes.common.personaef.model.PersonaEF;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author eduardo.loyo
 */
public class RequestCreacionCartaInstruccion extends BaseModel {

    @Getter
    @Setter
    private Prestamo prestamo = new Prestamo();
    @Getter
    @Setter
    private Solicitud solicitud = new Solicitud();
    @Getter
    @Setter
    private ResumenCartaInstruccion resumenCarta = new ResumenCartaInstruccion();
    @Getter
    @Setter
    private Reporte<ResumenCartaInstruccion> reporte = new Reporte<>();
    @Getter
    @Setter
    private PersonaEF personaEf;
    @Getter
    @Setter
    private Pensionado pensionado;
    @Getter
    @Setter
    private Integer flatPrestamoPromotor;
}
