/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mx.gob.imss.dpes.cartainstruccionfront.model.PrestamoModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCartaInstruccion;
import mx.gob.imss.dpes.common.enums.EventEnum;
import mx.gob.imss.dpes.common.personaef.model.PersonaEF;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author osiris.hernandez
 */
@Path("/prestamo")
@RegisterRestClient
public interface PrestamoClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Prestamo request);

    @GET
    @Path("/{idSolicitud}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response load(@PathParam("idSolicitud") Long idSolicitud);

    @POST
    @Path("/event")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response event(Long idSolicitud, EventEnum event);
    
    @POST
    @Path("/crear/cartainstruccion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response crearCartaInstruccion(RequestCartaInstruccion request);

    @POST
    @Path("/crear/cartaReinstalacion")
    @Consumes(MediaType.APPLICATION_JSON)
    Response createCartaIReinstalacion(RequestCartaInstruccion request);
    
}
