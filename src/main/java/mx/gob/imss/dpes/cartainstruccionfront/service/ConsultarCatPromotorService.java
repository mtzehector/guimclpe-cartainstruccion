package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.CatPromotorException;
import mx.gob.imss.dpes.cartainstruccionfront.exception.OfertaException;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.AmortizacionClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ConsultarCatPromotorService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private AmortizacionClient amortizacionClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request)
        throws BusinessException {

        try {
            //log.log(Level.INFO, "Buscando informacion del CAT promotor con IdSolicitud: {0}", request.getPayload().getSolicitud().getId());
            if (request.getPayload().getSolicitud().getCveOrigenSolicitud() == null) {
                return request;
            }
            if (request.getPayload().getSolicitud().getCveOrigenSolicitud().getId() == 4) {
                AmortizacionInsumos amortizacionInsumos = new AmortizacionInsumos();
                amortizacionInsumos.setCveSolicitud(request.getPayload().getSolicitud().getId());
                try {
                    Response catPromotor = amortizacionClient.obtenerCatPromotor(amortizacionInsumos);
                    if (catPromotor != null && catPromotor.getStatus() == 200) {
                        AmortizacionInsumos cat = catPromotor.readEntity(AmortizacionInsumos.class);
                        request.getPayload().setCatPromotor(cat.getCat());
                    }
                } catch (Exception e) {
                    log.log(Level.SEVERE, "ERROR ConsultarCatPromotorService.execute = {0}", e);
                    return response(null, ServiceStatusEnum.EXCEPCION, new CatPromotorException(), null);
                }
            }
            return request;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarCatPromotorService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new CatPromotorException(), null);
    }
}
