/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author salvador.pocteco
 */
public class ResumenCapacidadCreditoException extends BusinessException {
    
    private static final String KEY = "msg008";
    
    public ResumenCapacidadCreditoException() {
        super(KEY);
    }
    
}
