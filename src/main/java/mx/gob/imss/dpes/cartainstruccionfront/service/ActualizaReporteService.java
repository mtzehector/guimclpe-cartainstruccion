package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.ReporteCartaInstruccionException;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ReporteCartaPersistenceClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

@Provider
public class ActualizaReporteService extends ServiceDefinition<Documento, Documento> {

  @Inject
  private ReporteCartaPersistenceClient persistenceClient;


  @Override
  public Message<Documento> execute(Message<Documento> request) throws BusinessException {

    Response load = persistenceClient.update(request.getPayload());
    if (load.getStatus() == 200) {
      return new Message<>(load.readEntity(Documento.class));
    }
    return response(null, ServiceStatusEnum.EXCEPCION, new ReporteCartaInstruccionException(), null);
  }
}
