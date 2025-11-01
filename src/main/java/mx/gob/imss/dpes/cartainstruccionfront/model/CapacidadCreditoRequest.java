/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;


import java.util.List;
import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Reporte;
import mx.gob.imss.dpes.common.personaef.model.PersonaEF;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;

/**
 *
 * @author osiris.hernandez
 */
@Data
public class CapacidadCreditoRequest extends BaseModel{
    private Prestamo prestamo;
    private ResumenCartaInstruccion resumenCarta = new ResumenCartaInstruccion();
    private Reporte<ResumenCartaInstruccion> reporte = new Reporte<>();
    private PersonaEF personaEf;
    private Pensionado pensionado;
    private Integer flatPrestamoPromotor;
    private List<PrestamoRecuperacion> prestamosRecuperacionArreglo;
    
}
