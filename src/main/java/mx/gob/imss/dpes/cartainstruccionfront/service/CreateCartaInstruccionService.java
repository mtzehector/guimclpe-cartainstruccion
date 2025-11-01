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
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoClient;
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
public class CreateCartaInstruccionService extends ServiceDefinition<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {
    
    
    @Inject
    @RestClient
    private PrestamoClient service;
    
    @Override
    public Message<RequestCreacionCartaInstruccion> execute(Message<RequestCreacionCartaInstruccion> request) throws BusinessException {
        log.log(Level.INFO,">>>cartaInstruccionFront|CreateCartaInstruccionService|execute: {0}", request);
        RequestCartaInstruccion c = new RequestCartaInstruccion();
        c.setPersonaEf(request.getPayload().getPersonaEf());
        c.setPrestamo(request.getPayload().getPrestamo());
        c.setFlatPrestamoPromotor(0);
        Pensionado pe = new Pensionado();
        pe.setCorreoElectronico(request.getPayload().getPensionado().getCorreoElectronico());
        pe.setTelefono(request.getPayload().getPensionado().getTelefono());
        pe.setNombre(request.getPayload().getPensionado().getNombre());
        pe.setPrimerApellido(request.getPayload().getPensionado().getPrimerApellido());
        pe.setSegundoApellido(request.getPayload().getPensionado().getSegundoApellido());
        c.setPensionado(pe);
        
        log.log(Level.INFO,">>>>REQUEST CREAR Carta de Libranza<<<< {0}", c);
        Response load = service.crearCartaInstruccion(c);
        if (load.getStatus() == 200) {
            
            log.log(Level.WARNING, "FIN Carta de Libranza {0}");
            
            return new Message<>(request.getPayload());
        }
        return response(null, ServiceStatusEnum.EXCEPCION, new CartaException(), null);
    }
}
