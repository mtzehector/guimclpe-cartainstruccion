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
import mx.gob.imss.dpes.cartainstruccionfront.exception.SolicitudException;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.model.SolicitudModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.SolicitudClient;
import mx.gob.imss.dpes.cartainstruccionfront.rules.SolicitudRule;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.EstadoSolicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class SolicitudService extends ServiceDefinition<CapacidadCreditoRequest, CapacidadCreditoRequest> {

    @Inject
    @RestClient
    private SolicitudClient service;

    @Inject
    private SolicitudRule rule;

    @Override
    public Message<CapacidadCreditoRequest> execute(Message<CapacidadCreditoRequest> request) throws BusinessException {

        log.log(Level.INFO, ">>>cartainstruccionfront|SolicitudService: {0}", request.getPayload());
        Boolean flatMejorOferta = false;
        if (request.getPayload().getPrestamosRecuperacionArreglo() == null || request.getPayload().getPrestamosRecuperacionArreglo().isEmpty()) {
            Solicitud solicitud = rule.apply(request.getPayload());
            log.log(Level.INFO, "Request hacia Solicitud: {0}", solicitud);
            Response load = service.updateEstado(solicitud);
            if (load.getStatus() == 200) {
                return new Message<>(request.getPayload());
            }
            return response(null, ServiceStatusEnum.EXCEPCION, new SolicitudException(), null);
        } else {
            if(request.getPayload().getPrestamo().getTipoCreditoId() == 2 || request.getPayload().getPrestamo().getTipoCreditoId() == 1){
                flatMejorOferta = false;
            }else{
                flatMejorOferta = true;
                for (PrestamoRecuperacion p : request.getPayload().getPrestamosRecuperacionArreglo()) {
                    if (p.getMejorOferta() == 1) {
                        flatMejorOferta = true;
                    }
                }
            }
            if (!flatMejorOferta) {
                Solicitud solicitud = rule.apply(request.getPayload());
                log.log(Level.INFO, "Request hacia Solicitud: {0}", solicitud);
                Response load = service.updateEstado(solicitud);
                if (load.getStatus() == 200) {
                    return new Message<>(request.getPayload());
                }
                return response(null, ServiceStatusEnum.EXCEPCION, new SolicitudException(), null);
            }else{
                Solicitud solicitud = new Solicitud();
                solicitud.setId(request.getPayload().getPrestamo().getSolicitud());
                solicitud.setEstadoSolicitud(TipoEstadoSolicitudEnum.PENDIENTE_MONTO_LIQUIDAR);
                solicitud.setCveEstadoSolicitud(new EstadoSolicitud(
                TipoEstadoSolicitudEnum.PENDIENTE_MONTO_LIQUIDAR.getTipo()));
                Response load = service.updateEstado(solicitud);
                if (load.getStatus() == 200) {
                    return new Message<>(request.getPayload());
                }
            }
            return new Message<>(request.getPayload());
        }
    }

}
