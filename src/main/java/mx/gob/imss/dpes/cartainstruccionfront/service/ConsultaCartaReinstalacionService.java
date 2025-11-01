/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.DocumentoClient;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ConsultaCartaReinstalacionService extends ServiceDefinition<CapacidadCreditoRequest, CapacidadCreditoRequest> {
    
    @Inject
    @RestClient    
    private DocumentoClient client;
    
    @Override
    public Message<CapacidadCreditoRequest> execute(Message<CapacidadCreditoRequest> request) throws BusinessException {
        Documento documento = new Documento();
        documento.setCveSolicitud( request.getPayload().getPrestamo().getSolicitud() );
        documento.setTipoDocumento( TipoDocumentoEnum.CARTA_REINSTALACION );
        
        Response response = client.loadRefDocumento(documento);
        
        if( response.getStatus() == 200 ){
          ResumenCartaInstruccion resumenCartaInstruccion = response.readEntity( ResumenCartaInstruccion.class );
          request.getPayload().setResumenCarta(resumenCartaInstruccion);
          return request;          
        }
        
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }
}
