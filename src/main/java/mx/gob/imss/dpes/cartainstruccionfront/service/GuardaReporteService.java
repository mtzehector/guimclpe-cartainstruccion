package mx.gob.imss.dpes.cartainstruccionfront.service;

import java.util.logging.Level;
import mx.gob.imss.dpes.cartainstruccionfront.exception.ReporteCartaInstruccionException;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ReporteCartaPersistenceClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

@Provider
public class GuardaReporteService extends ServiceDefinition<Documento, Documento> {

  @Inject
  @RestClient
  private ReporteCartaPersistenceClient reporteCartaPersistenceClient;

  @Override
  public Message<Documento> execute(Message<Documento> request) throws BusinessException {
    log.log( Level.INFO, "### Intentado la llamada al servicio de persistencia el reporte con el siguiente request {0}", request.getPayload().toString());

    Response load = reporteCartaPersistenceClient.create(request.getPayload());

    if (load.getStatus() == 200) {
      return new Message<>(load.readEntity(Documento.class));
    }
    return response(null, ServiceStatusEnum.EXCEPCION, new ReporteCartaInstruccionException(), null);
  }


}
