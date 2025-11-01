package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.model.PrestamoEnRecuperacionRs;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.SistrapPrestamoClient;
import mx.gob.imss.dpes.common.enums.OrigenSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.sipre.model.PrestamoDescuentoNoAplicado;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class ComplementaPrestamosRecuperacionService extends
        ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private SistrapPrestamoClient sistrapPrestamoClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request)
        throws BusinessException {

        try {
            Solicitud solicitud = request.getPayload().getSolicitud();

            if (solicitud.getCveOrigenSolicitud().getId() != OrigenSolicitudEnum.REINSTALACION.getId())
                return request;

            PrestamoEnRecuperacionRs prestamoEnRecuperacionRs = request.getPayload().getListPrestamoRecuperacion();

            if(!(prestamoEnRecuperacionRs != null && prestamoEnRecuperacionRs.getPrestamosEnRecuperacion() != null &&
                !prestamoEnRecuperacionRs.getPrestamosEnRecuperacion().isEmpty()))
                return request;

            Response respuesta = null;
            PrestamoDescuentoNoAplicado prestamoDescuentoNoAplicado = null;
            for (PrestamoRecuperacion prestamoRecuperacion : prestamoEnRecuperacionRs.getPrestamosEnRecuperacion()) {
                respuesta = sistrapPrestamoClient.obtenerPrestamoDescuentoNoAplicado(
                    prestamoRecuperacion.getNumSolicitudSipre());

                if (respuesta.getStatus() == 200) {
                    prestamoDescuentoNoAplicado = respuesta.readEntity(PrestamoDescuentoNoAplicado.class);

                    prestamoRecuperacion.setNumMesesConsecutivos(prestamoDescuentoNoAplicado.getNumMesesConsecutivos());
                }
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ComplementaPrestamosRecuperacionService.execute = [" + request + "]", e);
        } finally {
            return request;
        }
    }
}
