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

import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.PrestamoModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ConsultaPrestamoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author salvador.pocteco
 */
@Provider
public class ConsultarPrestamoService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private ConsultaPrestamoClient consultaPrestamoClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request)
        throws BusinessException {

        //log.log(Level.INFO, "Servicio de consulta de prestamo de carta de instrucción");
        try {
            Response respuesta = consultaPrestamoClient.load(request.getPayload().getSolicitud().getId().toString());
            if (respuesta.getStatus() == 200) {

                request.getPayload().setPrestamo(respuesta.readEntity(PrestamoModel.class));

                return request;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarPrestamoService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new CartaInstruccionException(), null);
    }

}
