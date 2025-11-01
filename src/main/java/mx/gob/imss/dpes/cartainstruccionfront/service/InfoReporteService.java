/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.model.ReporteCartaInstruccion;
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

/**
 *
 * @author edgar.arenas
 */
@Provider
public class InfoReporteService extends ServiceDefinition<CapacidadCreditoRequest, CapacidadCreditoRequest> {
    
    @Inject
    @RestClient    
    private DocumentoClient client;
    
    @Override
    public Message<CapacidadCreditoRequest> execute(Message<CapacidadCreditoRequest> request) throws BusinessException {
        
        log.log( Level.INFO, "Buscando el resumen de la Carta de Libranza: {0}", request.getPayload().getPrestamo().getSolicitud() );
        
        Documento documento = new Documento();
        documento.setCveSolicitud( request.getPayload().getPrestamo().getSolicitud() );
        documento.setTipoDocumento( TipoDocumentoEnum.CARTA_INSTRUCCION );
        
        Response response = client.loadRefDocumento(documento);
        
        if( response.getStatus() == 200 ){
          ResumenCartaInstruccion resumenCartaInstruccion = response.readEntity( ResumenCartaInstruccion.class );
          request.getPayload().setResumenCarta(resumenCartaInstruccion);
          return request;          
        }
        
        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }
}
