/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.rules;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class SolicitudRule extends BaseRule<CapacidadCreditoRequest, Solicitud> {

    @Override
    public Solicitud apply(CapacidadCreditoRequest input) {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(input.getPrestamo().getSolicitud());
        solicitud.setEstadoSolicitud(TipoEstadoSolicitudEnum.POR_AUTORIZAR);
        solicitud.setCveEstadoSolicitud(new EstadoSolicitud(
                TipoEstadoSolicitudEnum.POR_AUTORIZAR.getTipo()));
        return solicitud;
    }
}
