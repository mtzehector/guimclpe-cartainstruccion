package mx.gob.imss.dpes.cartainstruccionfront.restclient;

import mx.gob.imss.dpes.cartainstruccionfront.model.SelloElectronicoRequest;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sellar")
@RegisterRestClient
public interface SelloElectronicoClient {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  Response create(SelloElectronicoRequest request);

}
