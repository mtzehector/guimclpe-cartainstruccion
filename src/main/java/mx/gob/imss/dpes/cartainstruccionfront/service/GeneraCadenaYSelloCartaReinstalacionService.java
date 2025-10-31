package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaReinstalacionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.model.SelloElectronicoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.SelloElectronicoClient;
import mx.gob.imss.dpes.cartainstruccionfront.rules.CadenaOriginalCartaReinstalacionRule;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class GeneraCadenaYSelloCartaReinstalacionService extends ServiceDefinition<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {

    @Inject
    private CadenaOriginalCartaReinstalacionRule cadenaOriginalRule;

    @Inject
    private SelloElectronicoClient selloElectronicoClient;

    @Override
    public Message<RequestCreacionCartaInstruccion> execute(Message<RequestCreacionCartaInstruccion> request) throws BusinessException {
        try {
            CadenaOriginalCartaReinstalacionRule.Output cadena = cadenaOriginalRule.apply( cadenaOriginalRule.new Input(request.getPayload().getResumenCarta()));
            request.getPayload().getResumenCarta().setCadenaOriginal(cadena.getRequest().getCadenaOriginal());

            SelloElectronicoRequest selloRequest = new SelloElectronicoRequest();
            selloRequest.setNss(request.getPayload().getResumenCarta().getNss());
            selloRequest.setFecCreacion(request.getPayload().getResumenCarta().getFecha());
            selloRequest.setFolioNegocio(request.getPayload().getResumenCarta().getFolio());
            selloRequest.setTipoCredito("Reinstalación");
            Response sello = selloElectronicoClient.create(selloRequest);
            if (sello.getStatus() == 200) {
                request.getPayload().getResumenCarta().setSelloDigital(sello.readEntity(String.class));
                return request;
            }
        }catch (Exception e){
            log.log(Level.SEVERE, "ERROR GeneraCadenaYSelloCartaReinstalacionService.execute = {0}", e);
        }
        return response(null,
                ServiceStatusEnum.EXCEPCION,
                new CartaReinstalacionException(CartaReinstalacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                null
        );

    }
}
