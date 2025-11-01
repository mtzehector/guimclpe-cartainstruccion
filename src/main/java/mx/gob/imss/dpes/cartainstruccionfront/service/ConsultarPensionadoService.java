package mx.gob.imss.dpes.cartainstruccionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaInstruccionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.PersonaModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.PensionadoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ConsultaPensionadoClient;
//import mx.gob.imss.dpes.cartainstruccionfront.restclient.ConsultaPersonaClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PensionadoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.VariableMessageException;
import mx.gob.imss.dpes.common.model.ErrorInfo;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;

import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

@Provider
public class ConsultarPensionadoService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private PensionadoClient pensionadoSistrapClient;

    @Inject
    @RestClient
    private ConsultaPensionadoClient pensionadoClient;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request)
        throws BusinessException {
        //log.log(Level.INFO, "Servicio de consulta de pensionado de carta de instrucción");

        Response response = null;

        try {
            PensionadoRequest pensionadoRequest = new PensionadoRequest();
            pensionadoRequest.setNss(request.getPayload().getSolicitud().getNss());
            pensionadoRequest.setGrupoFamiliar(request.getPayload().getSolicitud().getGrupoFamiliar());

            response = pensionadoSistrapClient.load(pensionadoRequest);

            if (response.getStatus() == Response.Status.PARTIAL_CONTENT.getStatusCode())
                throw new VariableMessageException((response.readEntity(ErrorInfo.class)).getMessage());

            if (response.getStatus() != Response.Status.OK.getStatusCode())
                throw new CartaInstruccionException(
                    CartaInstruccionException.ERROR_SERVICIO_CONSULTAR_PENSIONADO_SERVICE);

            request.getPayload().setPensionado(response.readEntity(Pensionado.class));
        } catch (VariableMessageException e) {
            log.log(Level.SEVERE, "ERROR ConsultarPensionadoService.execute = {0}", e);
            throw e;
        } catch (BusinessException e) {
            log.log(Level.SEVERE, "ERROR ConsultarPensionadoService.execute = {0}", e);
            return response(null, ServiceStatusEnum.EXCEPCION, e, null);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarPensionadoService.execute = {0}", e);
            return response(null, ServiceStatusEnum.EXCEPCION,
                new CartaInstruccionException(CartaInstruccionException.ERROR_SERVICIO_CONSULTAR_PENSIONADO_SERVICE),
                null);
        }

        try {
            response = pensionadoClient.load(request.getPayload().getSolicitud().getCurp());

            if (response.getStatus() != Response.Status.OK.getStatusCode())
                throw new CartaInstruccionException(
                    CartaInstruccionException.ERROR_SERVICIO_CONSULTAR_PENSIONADO_SERVICE);

            PersonaModel pensionado = response.readEntity(PersonaModel.class);
            request.getPayload().getPensionado().setPrimerApellido(pensionado.getPrimerApellido());
            request.getPayload().getPensionado().setSegundoApellido(pensionado.getSegundoApellido());
            request.getPayload().getPensionado().setNombre(pensionado.getNombre());
            request.getPayload().getPensionado().setNss(request.getPayload().getSolicitud().getNss());
            request.getPayload().getPensionado().setCurp(request.getPayload().getSolicitud().getCurp());
            request.getPayload().getPensionado().setGrupoFamiliar(request.getPayload().getSolicitud().getGrupoFamiliar());
            request.getPayload().getPensionado().setCuentaClabe(request.getPayload().getPrestamo().getRefCuentaClabe());
            request.getPayload().setPersona(pensionado);

            return request;
        } catch (BusinessException e) {
            log.log(Level.SEVERE, "ERROR ConsultarPensionadoService.execute = {0}", e);
            return response(null, ServiceStatusEnum.EXCEPCION, e, null);
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ConsultarPensionadoService.execute = {0}", e);
            return response(null, ServiceStatusEnum.EXCEPCION,
                new CartaInstruccionException(CartaInstruccionException.ERROR_SERVICIO_CONSULTAR_PENSIONADO_SERVICE),
                null);
        }
    }

}
