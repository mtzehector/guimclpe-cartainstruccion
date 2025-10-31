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
import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaException;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author eduardo.loyo
 */
@Provider
public class PrestamoPromotorService extends ServiceDefinition<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {

    @Inject
    @RestClient
    private PrestamoClient service;

    @Override
    public Message<RequestCreacionCartaInstruccion> execute(Message<RequestCreacionCartaInstruccion> request) throws BusinessException {
        log.log(Level.INFO,"Step 2");
        log.log(Level.INFO, "Request prestamo persistiendo el promotor {0}", request);
        Response load = service.create(request.getPayload().getPrestamo());
        if (load.getStatus() == 200) {
            Prestamo pretamoOut = load.readEntity(Prestamo.class);
            log.log(Level.WARNING, "Response prestamo Back update promotor {0}", pretamoOut);
            request.getPayload().setPrestamo(pretamoOut);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new CartaException(), null);
    }

}
