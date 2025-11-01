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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.cartainstruccionfront.model.CartaRecibo;
import mx.gob.imss.dpes.cartainstruccionfront.model.CartaReciboRequest;
import mx.gob.imss.dpes.cartainstruccionfront.service.GeneraCartaReciboService;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import org.eclipse.microprofile.openapi.annotations.Operation;

/**
 *
 * @author juanf.barragan
 */
@Path("/cartaRecibo")
@RequestScoped
public class CartaReciboEndPoint extends BaseGUIEndPoint<CartaRecibo,CartaRecibo,CartaRecibo>{
    @Inject
    private GeneraCartaReciboService generarCarta;
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crea la carta recibo")
    public Response crearCarta(CartaReciboRequest request) throws BusinessException{

        log.log( Level.INFO, "Inicia la creacion de la carta ", request);
        
        CartaReciboRequest response = generarCarta.crearCarta(request);
        
        log.log( Level.INFO, "La carta guardada es  ", response);
        
        return toResponse(new Message<>(response));
    }
    
     @GET
    @Path("/{id}")
    @Produces("application/pdf")
    @Operation(summary = "Generar el reporte de Carta Rrecibo",
            description = "Generar el reporte de Carta Rrecibo")
     public Response create(@PathParam("id") Long id) throws BusinessException{
         
         CartaReciboRequest response = generarCarta.recuperaCarta(id);
         
         CartaRecibo carta = new CartaRecibo();
         
         carta.setCartaRecibo(response);
         
         Message<CartaRecibo> reporte = generarCarta.execute(new Message<>(carta));
         
         if (!Message.isException(reporte)) {
            return Response.ok(reporte.getPayload().getReporte().getPdf()).header("Content-Disposition",
                    "attachment; filename=ReporteResumenCartaInstruccion.pdf").build();
        }
        return toResponse(reporte);

     }
    
}
