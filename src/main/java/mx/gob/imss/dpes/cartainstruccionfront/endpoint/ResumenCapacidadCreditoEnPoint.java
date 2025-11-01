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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaCapacidadCreditoService;
import mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaPersonaService;
import mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaPrestamosRecuperacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaSolicitudService;
import mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarPensionadoService;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author salvador.pocteco
 */
@Path("/resumenCapacidadCredito")
@RequestScoped
public class ResumenCapacidadCreditoEnPoint extends BaseGUIEndPoint<BaseModel, ConsultaCartaInstruccionModel, BaseModel> {

    @Inject
    private ConsultaSolicitudService consultaSolicitudService;
    
     @Inject
    private ConsultarPensionadoService consultarPensionadoService;
        @Inject
    ConsultaPrestamosRecuperacionService consultaPrestamosRec;
    
    @Inject
    private ConsultaCapacidadCreditoService consultaCapacidadCreditoService;
    
    @Inject
    private ConsultaPersonaService personaService;

    @GET
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener el resumen de la simulacion del credito",
            description = "Obtener el resumen de la simulacion del credito")
    @Override
    public Response load(ConsultaCartaInstruccionModel resumenCapacidadCreditoRequest) throws BusinessException {
        log.log(Level.INFO,">>>>>ResumenCapacidadCreditoEnPoint: {0}");
        ServiceDefinition[] steps = {consultaSolicitudService, consultarPensionadoService, consultaCapacidadCreditoService,consultaPrestamosRec, personaService};
        Message<ConsultaCartaInstruccionModel> response = consultaSolicitudService.executeSteps(steps, new Message<>(resumenCapacidadCreditoRequest));

        return toResponse(response);
    }
}
