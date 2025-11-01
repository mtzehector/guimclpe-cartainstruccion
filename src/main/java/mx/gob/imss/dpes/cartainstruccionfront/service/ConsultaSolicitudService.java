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
import mx.gob.imss.dpes.cartainstruccionfront.exception.ResumenCapacidadCreditoException;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author salvador.pocteco
 */
@Provider
public class ConsultaSolicitudService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private SolicitudClient solicitudClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request) throws BusinessException {
        log.log(Level.INFO, "El request de la solicitud es : {0}", request.getPayload().getSolicitud().getId());
        Response respuesta = solicitudClient.consultaSolicitud(request.getPayload().getSolicitud().getId());
        
        if (respuesta.getStatus() == 200) {
            Solicitud solicitudEntity = respuesta.readEntity(Solicitud.class);
            request.getPayload().setSolicitud(solicitudEntity);
            
            log.log(Level.INFO, "Los datos de la Solicitud son : {0}", request.getPayload());
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenCapacidadCreditoException(), null);
    }

}
