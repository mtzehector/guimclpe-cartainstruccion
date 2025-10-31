package mx.gob.imss.dpes.cartainstruccionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.AmortizacionClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ConsultaCondicionOfertaClient;
import mx.gob.imss.dpes.common.model.Message;

import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.CondicionOfertaRequest;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.Oferta;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@Provider
public class ConsultarCondicionOfertaService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private ConsultaCondicionOfertaClient consultaCondicionOfertaClient;
    
    @Inject
    @RestClient
    private AmortizacionClient client;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request) {

        log.log(Level.INFO, "Servicio de consulta de Oferta de carta de instrucción");
        CondicionOfertaRequest condicionOfertaMclpeRequest = new CondicionOfertaRequest();
        condicionOfertaMclpeRequest.setClave(request.getPayload().getPrestamo().getIdOferta().toString());
        Response load = consultaCondicionOfertaClient.load(condicionOfertaMclpeRequest);

        if (load.getStatus() == 200) {
            request.getPayload().getPrestamo().setOferta(load.readEntity(Oferta.class));
            if (request.getPayload().getSolicitud().getCveOrigenSolicitud() != null) {
                if (request.getPayload().getSolicitud().getCveOrigenSolicitud().getId() == 4) {
                    AmortizacionInsumos amortizacionInsumos = new AmortizacionInsumos();
                    amortizacionInsumos.setCveSolicitud(request.getPayload().getSolicitud().getId());
                    Response catPromotor = client.obtenerCatPromotor(amortizacionInsumos);
                    if (catPromotor.getStatus() == 200) {
                        AmortizacionInsumos cat = catPromotor.readEntity(AmortizacionInsumos.class);
                        request.getPayload().setCatPromotor(cat.getCat());
                    }
                }
            }

        }

        return request;
    }

}
