package mx.gob.imss.dpes.cartainstruccionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaInstruccionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.EntidadFinancieraLogo;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.PromotorMclpe;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.PromotorRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.EntidadFinancieraFrontClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PromotorClient;
import mx.gob.imss.dpes.common.model.Message;

import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

@Provider
public class ConsultarPromotorService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private PromotorClient promotorClient;

    @Inject
    @RestClient
    private EntidadFinancieraFrontClient entidadFinancieraFrontClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request) {
        try {
            //log.log(Level.INFO, "Servicio de consulta de promotor de carta de instrucción");
            if (request.getPayload().getSolicitud().getCveOrigenSolicitud() != null && request.getPayload().getSolicitud().getCveOrigenSolicitud().getId() == 5)
                return request;

            PromotorRequest promotorRequest = new PromotorRequest();
            promotorRequest.setId(request.getPayload().getPrestamo().getPromotor());

            Response load = promotorClient.load(promotorRequest);
            if (load.getStatus() == 200) {

                PromotorMclpe promotor = load.readEntity(PromotorMclpe.class);
                request.getPayload().setPersonaEf(promotor);

                try {
                    Response imgEF = entidadFinancieraFrontClient.obtieneLogo(promotor.getCveEntidadFinanciera());

                    if (imgEF.getStatus() == 200) {
                        EntidadFinancieraLogo efl = imgEF.readEntity(EntidadFinancieraLogo.class);

                        //log.log(Level.INFO, "Img EF JGV : {0}" + request.getPayload().getPromotor() );

                        request.getPayload().getPersonaEf().getEntidadFinanciera().setImgB64(
                                efl.getArchivo());
                    }
                } catch(Exception e) {
                      log.log(Level.WARNING, "ConsultarPromotorService.execute - No se econtraro el LOGO = {0}", e);
                       EntidadFinancieraLogo efl = new EntidadFinancieraLogo();
                       request.getPayload().getPersonaEf().getEntidadFinanciera().setImgB64(
                            efl.getArchivo());
                }

                return request;
            }
        } catch(Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarPromotorService.execute = {0}", e);
        }
      
        return response(null, ServiceStatusEnum.EXCEPCION, new CartaInstruccionException(), null);
    }

}
