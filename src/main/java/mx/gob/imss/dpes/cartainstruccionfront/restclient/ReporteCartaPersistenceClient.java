package mx.gob.imss.dpes.cartainstruccionfront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

@Path("/reporte")
@RegisterRestClient
public interface ReporteCartaPersistenceClient {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response create(Documento documento);

  @GET
  @Path("/{cveSolicitud}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response load(@PathParam("cveSolicitud") Long cveSolicitud);

  @PUT
  @Path("/actualizar")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response update(Documento documento);
}
