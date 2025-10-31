package mx.gob.imss.dpes.cartainstruccionfront.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/prestamo")
@RegisterRestClient
public interface SistrapPrestamoClient {
    @GET
    @Path("/descuentoNoAplicado/{idSolPrestamo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerPrestamoDescuentoNoAplicado(@PathParam("idSolPrestamo") String idSolPrestamo);
}
