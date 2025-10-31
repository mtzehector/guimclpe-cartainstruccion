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
import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaInstruccionException;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.EventoClient;
import mx.gob.imss.dpes.common.enums.EventEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.IdentityBaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.evento.model.Evento;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author antonio
 */
@Provider
public class CreateEventService extends ServiceDefinition<IdentityBaseModel<Long>, IdentityBaseModel<Long>> {

    @Inject
    @RestClient
    private EventoClient client;

    @Override
    public Message<IdentityBaseModel<Long>> execute(Message<IdentityBaseModel<Long>> request) throws BusinessException {
        log.log(Level.INFO,"Events");
        log.log( Level.INFO, "Solicitando el Evento: {0}", request.getPayload());
        Evento evento = new Evento();
        evento.setId( request.getPayload().getId() );
        evento.setEvent(EventEnum.CREAR_CARTA_INSTRUCCION);
        Response event = client.create(evento);
        if (event.getStatus() == 200) {
          return request;
        }
        
        return response(null, ServiceStatusEnum.EXCEPCION, new CartaInstruccionException(), null);
    }

}
