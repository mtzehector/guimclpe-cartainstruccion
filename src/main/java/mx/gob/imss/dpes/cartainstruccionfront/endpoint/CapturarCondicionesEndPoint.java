/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.endpoint;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCapturarCondicionesModel;
import mx.gob.imss.dpes.cartainstruccionfront.service.CapturarCondicionesService;
import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
/**
 *
 * @author osiris.hernandez
 */
@Slf4j
@Path("/capturarCondiciones")
@RequestScoped
public class CapturarCondicionesEndPoint extends BaseGUIEndPoint<RequestCapturarCondicionesModel, RequestCapturarCondicionesModel, RequestCapturarCondicionesModel> {

    @Inject
    private CapturarCondicionesService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(RequestCapturarCondicionesModel request) throws BusinessException {
        
        ServiceDefinition[] steps = {service};
        Message<RequestCapturarCondicionesModel> response = service.executeSteps(steps, new Message<>(request));
        return toResponse(response);
    }
}