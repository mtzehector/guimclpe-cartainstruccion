/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.CorreoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class CorreoSimulacionService extends ServiceDefinition<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion>{
    
     @Inject
    @RestClient
    CorreoClient correoClient;
    
    @Inject
    private Config config;
    
    @Override
    public Message<RequestCreacionCartaInstruccion> execute(Message<RequestCreacionCartaInstruccion> request) throws BusinessException {
        
        log.log(Level.INFO,">>>INICIA STEP ENVIAR CORREO Carta de Libranza: {0}", request.getPayload());
        
        String entidadFinanciera = request.getPayload().getPersonaEf().getEntidadFinanciera().getRazonSocial();
        String plantilla = String.format(config.getValue("plantillaSolicitudPrestamo", String.class),
                           entidadFinanciera);
        
        Correo correo = new Correo();
        
        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CorreoService.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        correo.setAsunto("Carta de Libranza");
        ArrayList<String> correos = new ArrayList<>();
        correos.add(request.getPayload().getPersonaEf().getEntidadFinanciera().getCorreoAdminEF());
        correos.add(request.getPayload().getPensionado().getCorreoElectronico());
        correo.setCorreoPara(correos);
        ArrayList<Adjunto> adjuntos = new ArrayList<>();
        Adjunto adjunto = new Adjunto();
        adjunto.setNombreAdjunto("CartaLibranza.pdf");
        adjunto.setAdjuntoBase64(request.getPayload().getReporte().getPdf());
        adjuntos.add(adjunto);
        correo.setAdjuntos(adjuntos);
                
        Response response = correoClient.enviaCorreo(correo);
                
        if(response.getStatus() == 200 || response.getStatus() == 204) {
            log.log(Level.INFO, ">>>>>>SE ENVIO CORREO Carta de Libranza={0}", response.getStatus());
            return request;
        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
