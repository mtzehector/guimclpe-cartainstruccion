/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author osiris.hernandez
 */
public class CartaInstruccionException extends BusinessException {

  public static final String KEY ="msg007";
  public static final String ERROR_SERVICIO_CONSULTAR_PENSIONADO_SERVICE ="msg009";
  public static final String ERROR_DESCONOCIDO_EN_EL_SERVICIO ="msg010";

  public CartaInstruccionException() {
    super(KEY);
  }

  public CartaInstruccionException(String msg) {
    super(msg);
  }
}
