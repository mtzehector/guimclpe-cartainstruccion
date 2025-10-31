/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaException;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class SolicitudEstadoService extends ServiceDefinition<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {

    @Inject
    @RestClient
    private SolicitudClient service;

    @Override
    public Message<RequestCreacionCartaInstruccion> execute(Message<RequestCreacionCartaInstruccion> request) throws BusinessException {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(request.getPayload().getPrestamo().getSolicitud());
        solicitud.setEstadoSolicitud(TipoEstadoSolicitudEnum.POR_AUTORIZAR);
        solicitud.setCveEstadoSolicitud(new EstadoSolicitud(TipoEstadoSolicitudEnum.POR_AUTORIZAR.toValue()));

        log.log(Level.WARNING, "Request Solicitud Back actualización Estado Solicitud {0}", solicitud);
        Response load = service.updateEstado(solicitud);
        if (load.getStatus() == 200) {
            Solicitud sol = load.readEntity(Solicitud.class);
            log.log(Level.WARNING, "Response Solicitud Back update folio {0}", sol);
            request.getPayload().setSolicitud(sol);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new CartaException(), null);
    }

}
