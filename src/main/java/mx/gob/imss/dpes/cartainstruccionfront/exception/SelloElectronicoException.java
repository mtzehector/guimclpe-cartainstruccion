package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class SelloElectronicoException extends BusinessException {
  private static final String KEY = "msg001";

  public SelloElectronicoException() {super(KEY);}
}
