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
import mx.gob.imss.dpes.basereport.service.ReporteService;
import mx.gob.imss.dpes.cartainstruccionfront.model.CartaRecibo;
import mx.gob.imss.dpes.cartainstruccionfront.model.CartaReciboRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.CartaReciboClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.Reporte;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author juanf.barragan
 */
@Provider
public class GeneraCartaReciboService extends ServiceDefinition<CartaRecibo,CartaRecibo>{
    
    @Inject    
    private ReporteService service;
    
    @Inject
    @RestClient
    private CartaReciboClient client;
    

    @Override
    public Message<CartaRecibo> execute(Message<CartaRecibo> request) throws BusinessException {
        
         log.log( Level.INFO, "Armando el reporte PDF: {0}", request.getPayload());
         
         Reporte<CartaReciboRequest> reporte = request.getPayload().getReporte();
         
         reporte.setRuta("/reports/CartaRecibo.jasper");
         
         reporte.getBeans().add(request.getPayload().getCartaRecibo());
         
          Message<Reporte> response = service.execute( new Message( reporte ) );
          
          if ( !Message.isException(response) ) {
            reporte.setPdf( response.getPayload().getPdf() );
            
            return request;
          }
          
         return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
        
    }
    
    public CartaReciboRequest crearCarta (CartaReciboRequest request)  throws BusinessException {
        
        log.log( Level.INFO, "Inicia la creación del registro de la carta ", request);
        
        Response response = client.crearCartaRecibo(request);
        
        if(response.getStatus()== 200){
            CartaReciboRequest recibo = response.readEntity(CartaReciboRequest.class);
            return recibo;
        }
        
        return null;
    }
    
    public CartaReciboRequest recuperaCarta(Long id) throws BusinessException{
        
        log.log( Level.INFO, "Inicia la recuperacion del registro de la carta ", id);
        
        Response response = client.obtenerCartaRecibo(id);
        
        if(response.getStatus()== 200){
            CartaReciboRequest recibo = response.readEntity(CartaReciboRequest.class);
            return recibo;
        }
        
        return null;
    }
    
}
