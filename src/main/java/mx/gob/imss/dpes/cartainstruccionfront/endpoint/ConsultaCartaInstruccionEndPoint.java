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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaInstruccionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionRequest;
import mx.gob.imss.dpes.cartainstruccionfront.service.*;
//import mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarCondicionOfertaService;

import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.PartialContentFlowException;
import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/cartaInstruccion")
@RequestScoped
public class ConsultaCartaInstruccionEndPoint extends BaseGUIEndPoint<ConsultaCartaInstruccionRequest, ConsultaCartaInstruccionRequest, ConsultaCartaInstruccionRequest> {

    @Inject
    ConsultarPensionadoService consultarPensionadoService;

    @Inject
    ConsultarPrestamoService consultarPrestamoService;

    //@Inject
    //ConsultarCondicionOfertaService consultarCondicionOfertaService;

    @Inject
    ConsultarSolicitudService consultarSolicitudService;

    @Inject
    ConsultarPromotorService consultarPromotorService;

    @Inject
    ConsultarDocumentosService consultarDocumentosService;

    @Inject
    ConsultaPersonaService consultaPersona;

    @Inject
    ConsultaCapacidadCreditoService consultaCapCredito;

    @Inject
    ConsultaPrestamosRecuperacionService consultaPrestamosRec;

    @Inject
    ConsultarLogoEntidadFinancieraService consultarLogoEfService;

    @Inject
    PrestamosMontoLiquidarService prestamosMontoLiquidarService;

    @Inject
    private ConsultarOfertaService consultarOfertaService;

    @Inject
    ConsultarCatPromotorService consultarCatPromotorService;

    @Inject
    ComplementaPrestamosRecuperacionService complementaPrestamosRecuperacionService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener los datos de una carta de instrucción",
            description = "Obtener los datos de una carta de instrucción")
    @Override
    public Response create(ConsultaCartaInstruccionRequest request) {
        try {
            ConsultaCartaInstruccionModel consultaCartaInstruccionModel = new ConsultaCartaInstruccionModel();

            consultaCartaInstruccionModel.setClaveSolicitud(request.getSolicitud().getId().toString());

            ServiceDefinition[] steps = {
                    consultarSolicitudService,
                    consultarPrestamoService,
                    consultarPensionadoService,
                    //consultarCondicionOfertaService,
                    consultarOfertaService,
                    consultarCatPromotorService,
                    consultarPromotorService,
                    consultarDocumentosService,
                    consultaPrestamosRec,
                    complementaPrestamosRecuperacionService
            };

            Message<ConsultaCartaInstruccionModel> response
                    = consultarSolicitudService.executeSteps(steps, new Message<>(consultaCartaInstruccionModel));

            //log.log(Level.INFO, ">>>El request de la consulta de la Carta de Libranza : {0}", response.getPayload());
            return toResponse(response);
        } catch (VariableMessageException e) {
            return toResponse(new Message(null, ServiceStatusEnum.PARTIAL_CONTENT,
                new PartialContentFlowException(e.getMessage()), null));
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultaCartaInstruccionEndPoint.create = {0}", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                new CartaInstruccionException(CartaInstruccionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO), null));
        }
    }

    @POST
    @Path("/consulta")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInfoSolicitud(ConsultaCartaInstruccionRequest request) {
        try {
            ConsultaCartaInstruccionModel consultaCartaInstruccionModel = new ConsultaCartaInstruccionModel();
            consultaCartaInstruccionModel.setClaveSolicitud(request.getSolicitud().getId().toString());
            consultaCartaInstruccionModel.setCveEFOperador(request.getCveEFOperador());

            ServiceDefinition[] steps = {
                    consultarSolicitudService,
                    consultarPrestamoService,
                    consultarPensionadoService,
                    //consultarCondicionOfertaService,
                    consultarOfertaService,
                    consultarCatPromotorService,
                    consultarDocumentosService,
                    consultaPersona,
                    prestamosMontoLiquidarService,
                    consultarLogoEfService
            };

            Message<ConsultaCartaInstruccionModel> response
                    = consultarSolicitudService.executeSteps(steps, new Message<>(consultaCartaInstruccionModel));
            //log.log(Level.INFO, "El request de la consulta de la Carta de Libranza : {0}", response.getPayload());
            return toResponse(response);
        } catch (VariableMessageException e) {
            return toResponse(new Message(null, ServiceStatusEnum.PARTIAL_CONTENT,
                new PartialContentFlowException(e.getMessage()), null));
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultaCartaInstruccionEndPoint.obtenerInfoSolicitud = {0}", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new CartaInstruccionException(CartaInstruccionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO), null));
        }
    }

    @POST
    @Path("/consultaMLCapacidad")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerInfoSolicitudCapacidad(ConsultaCartaInstruccionRequest request) {
        try {
            ConsultaCartaInstruccionModel consultaCartaInstruccionModel
                    = new ConsultaCartaInstruccionModel();
            consultaCartaInstruccionModel.setClaveSolicitud(request.getSolicitud().getId().toString());

            ServiceDefinition[] steps = {consultarSolicitudService, consultarPensionadoService,
                    consultaPersona, consultaPrestamosRec, consultaCapCredito};
            Message<ConsultaCartaInstruccionModel> response
                    = consultarSolicitudService.executeSteps(steps, new Message<>(consultaCartaInstruccionModel));
            //log.log(Level.INFO, "El request de la consulta de la Carta de Libranza : {0}", response.getPayload());
            return toResponse(response);
        } catch (VariableMessageException e) {
            return toResponse(new Message(null, ServiceStatusEnum.PARTIAL_CONTENT,
                new PartialContentFlowException(e.getMessage()), null));
        } catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultaCartaInstruccionEndPoint.obtenerInfoSolicitudCapacidad = {0}", e);
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION,
                    new CartaInstruccionException(CartaInstruccionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO), null));
        }
    }
}
