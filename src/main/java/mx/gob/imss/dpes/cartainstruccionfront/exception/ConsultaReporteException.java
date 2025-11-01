package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class ConsultaReporteException extends BusinessException {

  private static final String KEY = "msg003";

  public ConsultaReporteException() {
    super(KEY);
  }
}
