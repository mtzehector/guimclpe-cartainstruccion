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
import mx.gob.imss.dpes.cartainstruccionfront.model.ResumenCapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.CapacidadCreditoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author salvador.pocteco
 */
@Provider
public class ConsultaCapacidadCreditoService  extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private CapacidadCreditoClient capacidadCreditoClient;
    
    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request) throws BusinessException {
        try {
            //log.log(Level.INFO, "El request de la solicitud capacidad es : {0}", request.getPayload().getSolicitud().getId());
            Response respuesta = capacidadCreditoClient.consultaCapacidad(request.getPayload().getSolicitud().getId());

            if (respuesta.getStatus() == 200) {
                CapacidadCredito capacidadCredito = respuesta.readEntity(CapacidadCredito.class);
                request.getPayload().setCapacidadCredito(capacidadCredito);

                //log.log(Level.INFO, "Los datos de la capacidad son : {0}", request.getPayload());
                return new Message<>(request.getPayload());
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultaCapacidadCreditoService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new ResumenCapacidadCreditoException(), null);
    }
    
}
