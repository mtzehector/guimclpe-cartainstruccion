package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class ReporteCartaInstruccionException extends BusinessException {

  private static final String KEY = "msg002";

  public ReporteCartaInstruccionException() {super(KEY);}
}
