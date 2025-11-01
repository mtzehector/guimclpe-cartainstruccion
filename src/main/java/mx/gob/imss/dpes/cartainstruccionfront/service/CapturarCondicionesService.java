/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.service;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.exception.CapturarCondicionesException;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCapturarCondicionesModel;
import mx.gob.imss.dpes.cartainstruccionfront.rules.CreateCapturarCondicionesRule;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class CapturarCondicionesService extends ServiceDefinition<RequestCapturarCondicionesModel, RequestCapturarCondicionesModel>{
    
    @Inject
    private CreateCapturarCondicionesRule rule;
      
    @Override
    public Message<RequestCapturarCondicionesModel> execute(Message<RequestCapturarCondicionesModel> request) throws BusinessException{
        
        request.getPayload().setFlujo("1");
        RequestCapturarCondicionesModel response = rule.apply(request.getPayload());
        
        
        if( !"1".equals( response.getFlujo() ) ){
            return response(null, ServiceStatusEnum.EXCEPCION, new CapturarCondicionesException(response.getFlujo()), null);
        }   
        return new Message<>(response);
    }    
}
