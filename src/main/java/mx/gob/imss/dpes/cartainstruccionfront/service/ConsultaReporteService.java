package mx.gob.imss.dpes.cartainstruccionfront.service;

import lombok.extern.slf4j.Slf4j;
import mx.gob.imss.dpes.cartainstruccionfront.exception.ConsultaReporteException;
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
public class ConsultaReporteService extends ServiceDefinition<Documento, Documento> {

  @Inject
  @RestClient
  private ReporteCartaPersistenceClient reporteCartaPersistenceClient;

  @Override
  public Message<Documento> execute(Message<Documento> request) throws BusinessException {

    Response load = reporteCartaPersistenceClient.load(request.getPayload().getCveSolicitud());

    if (load.getStatus() == 200) {
      return new Message<>(load.readEntity(Documento.class));
    }
    return response(null, ServiceStatusEnum.EXCEPCION, new ConsultaReporteException(), null);
  }
}
