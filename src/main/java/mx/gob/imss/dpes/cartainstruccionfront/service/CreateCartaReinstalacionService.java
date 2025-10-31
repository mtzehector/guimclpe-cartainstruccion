package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaException;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class CreateCartaReinstalacionService extends ServiceDefinition<CapacidadCreditoRequest, CapacidadCreditoRequest> {
    
    @Inject
    @RestClient
    private PrestamoClient prestamoClient;
    
    @Override
    public Message<CapacidadCreditoRequest> execute(Message<CapacidadCreditoRequest> request) throws BusinessException {
        RequestCartaInstruccion c = new RequestCartaInstruccion();
        c.setPersonaEf(request.getPayload().getPersonaEf());
        c.setPrestamo(request.getPayload().getPrestamo());
        c.setFlatPrestamoPromotor(request.getPayload().getFlatPrestamoPromotor());
        Pensionado pe = new Pensionado();
        pe.setCorreoElectronico(request.getPayload().getPensionado().getCorreoElectronico());
        pe.setTelefono(request.getPayload().getPensionado().getTelefono());
        pe.setNombre(request.getPayload().getPensionado().getNombre());
        pe.setPrimerApellido(request.getPayload().getPensionado().getPrimerApellido());
        pe.setSegundoApellido(request.getPayload().getPensionado().getSegundoApellido());
        c.setPensionado(pe);

        Response load = prestamoClient.createCartaIReinstalacion(c);
        if (load.getStatus() == 200) {
            return new Message<>(request.getPayload());
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new CartaException(), null);
    }
}
