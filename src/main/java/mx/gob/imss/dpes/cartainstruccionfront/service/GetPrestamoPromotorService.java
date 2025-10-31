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
public class GetPrestamoPromotorService extends ServiceDefinition<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {

    @Inject
    @RestClient
    private PrestamoClient service;

    @Override
    public Message<RequestCreacionCartaInstruccion> execute(Message<RequestCreacionCartaInstruccion> request) throws BusinessException {
        log.log(Level.INFO, "Step 1");
        log.log(Level.INFO, "Request Asegurandonos que existe el prestamo {0}", request);
        Long promotor = request.getPayload().getPrestamo().getPromotor();
        Response load = service.load(request.getPayload().getPrestamo().getSolicitud());
        if (load.getStatus() == 200) {
            Prestamo prestamoOut = load.readEntity(Prestamo.class);
            prestamoOut.setPromotor(promotor);
            log.log(Level.WARNING, "Response prestamo Back Obtener promotor {0}", prestamoOut);
            request.getPayload().setPrestamo(prestamoOut);
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new CartaException(), null);
    }

}
