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
import mx.gob.imss.dpes.cartainstruccionfront.model.EntidadFinancieraLogo;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
//import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.PromotorMclpe;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.PromotorRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.EntidadFinancieraFrontClient;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juan.garfias
 */

@Provider
public class ConsultarLogoEntidadFinancieraService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private EntidadFinancieraFrontClient entidadFinancieraFrontClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request) {
        try {
            //log.log(Level.INFO, "Servicio de consulta de promotor de carta de instrucción");
            PromotorRequest promotorRequest = new PromotorRequest();
            promotorRequest.setId(request.getPayload().getPrestamo().getPromotor());

            Response load = entidadFinancieraFrontClient.obtieneLogo(
                    request.getPayload().getSolicitud().getCveEntidadFinanciera()
            );

            if (load.getStatus() == 200) {

                EntidadFinancieraLogo efl = load.readEntity(
                        EntidadFinancieraLogo.class
                );

                request.getPayload().getPrestamo().getOferta().getEntidadFinanciera().setImgB64(
                        efl.getArchivo()
                );

                return request;

            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarLogoEntidadFinancieraService.execute = {0}", e);
        }

        //TODO: Cambiar por excepcion de infraestructura
        return response(null, ServiceStatusEnum.EXCEPCION,
            new CartaInstruccionException(CartaInstruccionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO), null);

    }
}
