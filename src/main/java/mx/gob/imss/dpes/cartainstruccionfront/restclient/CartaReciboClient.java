/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.cartainstruccionfront.model.CartaReciboRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author juanf.barragan
 */

@Path("/cartaRecibo")
@RegisterRestClient
public interface CartaReciboClient {
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crearCartaRecibo(CartaReciboRequest carta);
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response obtenerCartaRecibo (@PathParam("id") Long id);
    
}
