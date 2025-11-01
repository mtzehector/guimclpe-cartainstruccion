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
import mx.gob.imss.dpes.cartainstruccionfront.exception.PrestamoException;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoClient;
import mx.gob.imss.dpes.cartainstruccionfront.rules.PrestamoRule;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class PrestamoService extends ServiceDefinition<CapacidadCreditoRequest, CapacidadCreditoRequest> {

    @Inject
    @RestClient
    private PrestamoClient service;

    @Inject
    private PrestamoRule rule;

    @Override
    public Message<CapacidadCreditoRequest> execute(Message<CapacidadCreditoRequest> request) throws BusinessException {
        log.log(Level.INFO, "Step 1");
        Prestamo prestamo = rule.apply(request.getPayload());
        log.log(Level.INFO, "Request hacia prestamo: {0}", prestamo);
        Response load = service.create(prestamo);
        if (load.getStatus() == 200) {
            Prestamo sol = load.readEntity(Prestamo.class);
            request.getPayload().setPrestamo(sol);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new PrestamoException(), null);
    }
}
