/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.restclient;

/**
 *
 * @author antonio
 */
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.CondicionOfertaRequest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/condicionOferta")
@RegisterRestClient
public interface ConsultaCondicionOfertaClient{

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response load(CondicionOfertaRequest request);

}
