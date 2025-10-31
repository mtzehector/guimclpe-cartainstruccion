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
import mx.gob.imss.dpes.cartainstruccionfront.model.PersonaModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ConsultaPersonaClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ConsultaPersonaService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {
    
    @Inject
    @RestClient
    private ConsultaPersonaClient client;
   
    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request) throws BusinessException {

        try {
            //log.log(Level.INFO,">>>INICIA STEP PERSONA: {0}", request.getPayload());
            Pensionado pe = new Pensionado();
            pe.setNss(request.getPayload().getSolicitud().getNss());
            pe.setCurp(request.getPayload().getSolicitud().getCurp());

            Response load = client.load(pe);
            if (load.getStatus() == 200) {
                 PersonaModel res = load.readEntity(PersonaModel.class);
                 request.getPayload().setPersona(res);
                 return request;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultaPersonaService.execute = {0}", e);
        }

        return response(null, ServiceStatusEnum.EXCEPCION,
            new CartaInstruccionException(CartaInstruccionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO), null);
        
    }
}
