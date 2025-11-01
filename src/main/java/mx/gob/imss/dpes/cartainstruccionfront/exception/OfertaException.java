package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class OfertaException extends BusinessException {
    private final static String KEY = "err005";

    public OfertaException() {
        super(KEY);
    }
}
