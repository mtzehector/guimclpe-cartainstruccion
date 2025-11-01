package mx.gob.imss.dpes.cartainstruccionfront.endpoint;

import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaReinstalacionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.service.GeneraCadenaYSelloCartaReinstalacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.GeneraCartaReinstalacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.InformacionCartaReinstalacionService;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

@Path("/cartaReinstalacion")
public class CartaReinstalacionEndPoint extends BaseGUIEndPoint<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {

    @Inject
    private InformacionCartaReinstalacionService informacionReinstalacionService;
    @Inject
    private GeneraCadenaYSelloCartaReinstalacionService selloService;
    @Inject
    private GeneraCartaReinstalacionService generaCartaService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cartaReinstalacion(RequestCreacionCartaInstruccion request) throws BusinessException{
        try{
            ServiceDefinition[] steps = {
                    informacionReinstalacionService,
                    selloService,
                    generaCartaService
            };
            Message<RequestCreacionCartaInstruccion> response = generaCartaService.executeSteps(steps, new Message<>(request));
            return Response.ok("cartaReinstalacion").build();
        }catch (BusinessException e){
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR CartaReinstalacionEndPoint.cartaReinstalacion() ", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new CartaReinstalacionException(CartaReinstalacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }

    }

}
