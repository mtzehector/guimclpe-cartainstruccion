package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.OfertaException;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ConsultaCondicionOfertaClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoInsumoTaClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.CondicionOfertaRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoInsumoTa;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ConsultarOfertaService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private ConsultaCondicionOfertaClient ofertaClient;

    @Inject
    @RestClient
    private PrestamoInsumoTaClient prestamoInsumoTaClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request)
        throws BusinessException {

        try {
            CondicionOfertaRequest ofertaRequest = new CondicionOfertaRequest();
            ofertaRequest.setClave(request.getPayload().getPrestamo().getIdOferta().toString());

            Response responseOferta = ofertaClient.load(ofertaRequest);
            if (responseOferta != null && responseOferta.getStatus() == 200){
                Oferta oferta = responseOferta.readEntity(Oferta.class);
                Response responsePrestamoInsumo = prestamoInsumoTaClient.obtenerPrestamoInsumoPorCveSolicitud(
                        request.getPayload().getSolicitud().getId()
                );
                if (responsePrestamoInsumo != null && responsePrestamoInsumo.getStatus() == 200){
                    PrestamoInsumoTa prestamoInsumo = responsePrestamoInsumo.readEntity(PrestamoInsumoTa.class);
                    oferta.setCat(prestamoInsumo.getCat() == null ? 0.0 : prestamoInsumo.getCat().doubleValue());
                    request.getPayload().getPrestamo().setOferta(oferta);
                    return request;
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarOfertaService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new OfertaException(), null);
    }
}
