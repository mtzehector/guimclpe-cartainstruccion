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
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestPromotorModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ConsultaSolicitudClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PromotorFrontClient;
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
public class ConsultarSolicitudService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private ConsultaSolicitudClient consultaSolicitudClient;

    @Inject
    @RestClient
    private PromotorFrontClient promotorClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request)
        throws BusinessException {

        Solicitud solicitudEntity = null;

        try {
            //log.log(Level.INFO, "Servicio de consulta de solicitud de carta de instrucción: {0}", request.getPayload());
            //log.log(Level.INFO, "El request de la solicitud es : {0}", request.getPayload().getClaveSolicitud());

            Response respuesta = consultaSolicitudClient.load(request.getPayload().getClaveSolicitud());

            if (respuesta.getStatus() == 200) {
                solicitudEntity = respuesta.readEntity(Solicitud.class);
                request.getPayload().setSolicitud(solicitudEntity);
                //log.log(Level.INFO, "Servicio de consulta de solicitud de carta de instrucción: {0}", request.getPayload().getSolicitud());

                if (solicitudEntity.getCvePromotor() != null) {
                    Response responsePromotorClient = promotorClient.obtenerPorClave(solicitudEntity.getCvePromotor());
                    RequestPromotorModel promotor = responsePromotorClient.readEntity(RequestPromotorModel.class);
                    request.getPayload().setPromotor(promotor);
                } else
                    request.getPayload().setPromotor(new RequestPromotorModel());

                return request;
            }
        } catch (Exception e) {
            if(solicitudEntity != null && solicitudEntity.getCveEstadoSolicitud() != null &&
                solicitudEntity.getCveEstadoSolicitud().getId() != 1 &&
                solicitudEntity.getCvePromotor() != null) {
                request.getPayload().setPromotor(new RequestPromotorModel());
                return request;
            }

            log.log(Level.SEVERE, "ERROR ConsultarSolicitudService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new CartaInstruccionException(), null);
    }

}
