/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.SolicitudException;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.cartainstruccionfront.rules.SolicitudRule;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class ActualizaSolicitudReinstalacionService extends ServiceDefinition<CapacidadCreditoRequest, CapacidadCreditoRequest> {

    @Inject
    @RestClient
    private SolicitudClient service;

    @Inject
    private SolicitudRule rule;

    @Override
    public Message<CapacidadCreditoRequest> execute(Message<CapacidadCreditoRequest> request) throws BusinessException {
        Solicitud solicitud = rule.apply(request.getPayload());
        Response load = service.updateEstado(solicitud);
        if (load.getStatus() == 200) {
            return new Message<>(request.getPayload());
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new SolicitudException(), null);
    }

}
