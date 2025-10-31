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
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.service.CorreoSimulacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.CreateCartaInstruccionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.CreateEventService;
import mx.gob.imss.dpes.cartainstruccionfront.service.CreateReporteCartaInstruccionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.GenerarReporteSimulacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.GetPrestamoPromotorService;
import mx.gob.imss.dpes.cartainstruccionfront.service.InfoReporteSimulacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.PrestamoPromotorService;
import mx.gob.imss.dpes.cartainstruccionfront.service.SolicitudEstadoService;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author eduardo.loyo
 */
@Path("/carta")
@RequestScoped
public class CartaInstruccionEndPoint extends BaseGUIEndPoint<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {

    @Inject
    private GetPrestamoPromotorService getPrestamo; //Step 1
    @Inject
    private PrestamoPromotorService prestamo; //Step 2

    @Inject
    private SolicitudEstadoService solicitud; //Step 4
    
    @Inject
    private CreateCartaInstruccionService crearCarta; //Step3
    
    @Inject
    private CreateEventService eventService;
    
    @Inject
    InfoReporteSimulacionService infoReporte;
    @Inject 
    GenerarReporteSimulacionService  generaReporte;
    @Inject
    CorreoSimulacionService enviarReporte;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crea carta y actualiza estado de solicitud",
            description = "Crea carta y solicitud")
    @Override
    public Response create(RequestCreacionCartaInstruccion request) throws BusinessException {
        log.log(Level.INFO, "Inician los pasos");
        log.log(Level.INFO, "Request inicial: {0}", request);
        ServiceDefinition[] steps = {getPrestamo, prestamo, crearCarta, solicitud, infoReporte, generaReporte, enviarReporte};
        Message<RequestCreacionCartaInstruccion> response = getPrestamo.executeSteps(steps, new Message<>(request));
       
//        if (!Message.isException(response)) {
//            IdentityBaseModel<Long> model = new IdentityBaseModel<>();
//            model.setId(response.getPayload().getSolicitud().getId());
//            eventService.execute(new Message<>(model));
//        }
        
        
        return toResponse(response);
    }
}
