package mx.gob.imss.dpes.cartainstruccionfront.endpoint;

import mx.gob.imss.dpes.cartainstruccionfront.exception.ReporteCartaReinstalacionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.ReporteCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.service.CrearCartaReinstalacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.ObtenerDatosReporteService;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

@Path("/reporteCartaReinstalacion")
@RequestScoped
public class ReporteCartaReinstalacionEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    @Inject
    private ObtenerDatosReporteService datosService;

    @Inject
    private CrearCartaReinstalacionService cartaReinstalacionService;

    @GET
    @Path("/{idSolicitud}")
    @Produces("application/pdf")
    public Response crearCartaReinstalacionPromotor(@PathParam("idSolicitud") Long idSolicitud) throws BusinessException{
        try{
            ReporteCartaInstruccion request = new ReporteCartaInstruccion();
            request.setIdSolicitud(idSolicitud);

            ServiceDefinition[] steps = {datosService, cartaReinstalacionService};
            Message<ReporteCartaInstruccion> response = datosService.executeSteps(steps, new Message<>(request));

            return Response.ok(
                    response.getPayload().getReporte().getPdf()
            ).header(
                    "Content-Disposition",
                    "attachment; filename=ReporteCartaReinstalacion.pdf"
            ).build();
        }catch (BusinessException e) {
            return toResponse(new Message(null, ServiceStatusEnum.EXCEPCION, e, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ReporteCartaReinstalacionEndPoint.crearCartaReinstalacionPromotor() ", e);
            return toResponse(new Message(
                    null,
                    ServiceStatusEnum.EXCEPCION,
                    new ReporteCartaReinstalacionException(ReporteCartaReinstalacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            ));
        }

    }

}
