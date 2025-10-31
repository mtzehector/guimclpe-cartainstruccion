package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class CartaInstruccionParseException extends BusinessException {

  private static final String KEY ="msg004";

  public CartaInstruccionParseException() {
    super(KEY);
  }
}
