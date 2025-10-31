package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.CorreoClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Adjunto;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.Correo;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class CorreoCartaReinstalacionService extends ServiceDefinition<CapacidadCreditoRequest, CapacidadCreditoRequest>{
    
    @Inject
    @RestClient
    CorreoClient correoClient;
    
    @Inject
    private Config config;
    
    @Override
    public Message<CapacidadCreditoRequest> execute(Message<CapacidadCreditoRequest> request) throws BusinessException {
        
        log.log(Level.INFO,">>>INICIA STEP ENVIAR CORREO Carta de Libranza: {0}", request.getPayload().getPensionado().getCorreoElectronico());
        log.log(Level.INFO,">>>INICIA STEP ENVIAR CORREO Carta de Libranza: {0}", request.getPayload().getPersonaEf().getEntidadFinanciera().getCorreoAdminEF());
        String entidadFinanciera = request.getPayload().getPersonaEf().getEntidadFinanciera().getRazonSocial();
        String plantilla = String.format(config.getValue("plantillaSolicitudReinstalacion", String.class),
                           entidadFinanciera);
        
        Correo correo = new Correo();
        
        try {
            correo.setCuerpoCorreo(new String(plantilla.getBytes(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CorreoCartaReinstalacionService.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        correo.setAsunto("Carta de Reinstalación");
        ArrayList<String> correos = new ArrayList<>();
        correos.add(request.getPayload().getPersonaEf().getEntidadFinanciera().getCorreoAdminEF());
        correos.add(request.getPayload().getPensionado().getCorreoElectronico());
        correo.setCorreoPara(correos);
        ArrayList<Adjunto> adjuntos = new ArrayList<>();
        Adjunto adjunto = new Adjunto();
        adjunto.setNombreAdjunto("CartaReinstalacion.pdf");
        adjunto.setAdjuntoBase64(request.getPayload().getReporte().getPdf());
        adjuntos.add(adjunto);
        correo.setAdjuntos(adjuntos);
                
        Response response = correoClient.enviaCorreo(correo);
                
        if(response.getStatus() == 200 || response.getStatus() == 204) {
            log.log(Level.INFO, ">>>>>>SE ENVIO CORREO Carta de Reinstalación={0}", response.getStatus());
            return request;
        }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
