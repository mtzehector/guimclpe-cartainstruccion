package mx.gob.imss.dpes.cartainstruccionfront.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaInstruccionParseException;
import mx.gob.imss.dpes.cartainstruccionfront.exception.SelloElectronicoException;
import mx.gob.imss.dpes.cartainstruccionfront.model.ReporteCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.model.SelloElectronicoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.SelloElectronicoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Level;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;

@Provider
public class ObtenerSelloElectronicoService extends ServiceDefinition<Documento, Documento> {

  @Inject
  @RestClient
  private SelloElectronicoClient selloElectronicoClient;

  @Override
  public Message<Documento> execute(Message<Documento> request) throws BusinessException {

    SelloElectronicoRequest peticion = new SelloElectronicoRequest();
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      ReporteCartaInstruccion cartaInstruccion = objectMapper.readValue(request.getPayload().getRefDocumento(), ReporteCartaInstruccion.class);
      peticion.setNss(cartaInstruccion.getResumenCartaInstruccion().getNss());
      peticion.setFolioNegocio(cartaInstruccion.getResumenCartaInstruccion().getFolio());
      peticion.setFecCreacion(cartaInstruccion.getResumenCartaInstruccion().getFecha());
      peticion.setTipoCredito(cartaInstruccion.getResumenCartaInstruccion().getTipoCredito());
    } catch (IOException e) {
      log.log( Level.SEVERE, "Ocurrio una excepción IO", e);
      return response(null, ServiceStatusEnum.EXCEPCION, new CartaInstruccionParseException(), null);
    }

    Response sellar = selloElectronicoClient.create(peticion);

    if (sellar.getStatus() == 200) {
      request.getPayload().setRefSello(sellar.readEntity(String.class));
      return response(request.getPayload(), ServiceStatusEnum.EXITOSO, null, null);
    } else {
      return response(null, ServiceStatusEnum.EXCEPCION, new SelloElectronicoException(), null);
    }
  }
}
