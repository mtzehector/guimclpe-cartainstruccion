/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.endpoint;

import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.service.*;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.IdentityBaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.Notice;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author osiris.hernandez
 */
@Path("/capacidadCredito")
@RequestScoped
public class CapacidadCreditoEndPoint extends BaseGUIEndPoint<CapacidadCreditoRequest, CapacidadCreditoRequest, CapacidadCreditoRequest> {

    @Inject
    PrestamoService prestamoService;
    @Inject
    SolicitudService solicitudService;
    @Inject
    CreateEventService createEventService;
    @Inject
    InfoReporteService infoReporte;
    @Inject
    GenerarReportePrestamoPromotor generaReporte;
    @Inject
    CorreoService enviarReporte;
    @Inject
    private CreateCartaInstruccionCapacidadService crearCarta;
    @Inject
    CreateCartaReinstalacionService createCartaReinstalacionService;
    @Inject
    ConsultaCartaReinstalacionService consultaCartaReinstalacionService;
    @Inject
    GenerarReporteReinstalacionPrestamo generarReporteReinstalacionPrestamo;
    @Inject
    CorreoCartaReinstalacionService correoCartaReinstalacionService;
    @Inject
    ActualizaSolicitudReinstalacionService actualizaSolicitudReinstalacionService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generar una Carta de Libranza de capacidad de credito",
            description = "Generar una Carta de Libranza de capacidad de credito")
    @Override
    public Response create(CapacidadCreditoRequest request) throws BusinessException {

        log.log(Level.INFO, "Inicia generacion de Carta de Libranza capacidad de credito {0}", request);
        ServiceDefinition[] steps = {prestamoService, crearCarta, solicitudService, infoReporte, generaReporte, enviarReporte};
        Message<CapacidadCreditoRequest> response = prestamoService.executeSteps(steps, new Message<>(request));

//        if (!Message.isException(response)) {
//            IdentityBaseModel<Long> model = new IdentityBaseModel<>();
//            model.setId(response.getPayload().getPrestamo().getSolicitud());
//            createEventService.execute(new Message<>(model));
//        }
        if (response.getPayload().getPrestamo().getImpDescNomina() > response.getPayload().getPrestamo().getMonto()) {
            response.getHeader().addNotice(new Notice("El descuento mensual ingresado es mayor a la capacidad de crédito total del pensionado. Favor de verificar el descuento mensual."));
        }

        return toResponse(response);
    }

    @POST
    @Path("/crear/carta/prestamopromotor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generar una Carta de Libranza de capacidad de credito",
            description = "Generar una Carta de Libranza de capacidad de credito")
    public Response crearCartaPrestamoPromotor(CapacidadCreditoRequest request) throws BusinessException {
        
        log.log(Level.INFO, ">>>cartaInstruccionFront|CapacidadCreditoEndPoint|crearCartaPrestamoPromotor: {0}", request);
        ServiceDefinition[] steps = {crearCarta, infoReporte, generaReporte, enviarReporte, solicitudService};
        Message<CapacidadCreditoRequest> response = crearCarta.executeSteps(steps, new Message<>(request));

//        if (!Message.isException(response)) {
//            IdentityBaseModel<Long> model = new IdentityBaseModel<>();
//            model.setId(response.getPayload().getPrestamo().getSolicitud());
//            createEventService.execute(new Message<>(model));
//        }
        return toResponse(response);
    }

    @POST
    @Path("/crear/carta/reinstalacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generar una Carta de Reinstalacion de credito",
            description = "Generar una Carta de Reinstalacion de credito")
    public Response crearCartaReinstalacion(CapacidadCreditoRequest request) throws BusinessException {
        ServiceDefinition[] steps = {
                createCartaReinstalacionService,
                consultaCartaReinstalacionService,
                generarReporteReinstalacionPrestamo,
                correoCartaReinstalacionService,
                actualizaSolicitudReinstalacionService
        };
        Message<CapacidadCreditoRequest> response = createCartaReinstalacionService.executeSteps(steps, new Message<>(request));

        return toResponse(response);
    }
}
