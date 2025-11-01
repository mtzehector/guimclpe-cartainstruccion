package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class CatPromotorException extends BusinessException {

    private final static String KEY = "err006";

    public CatPromotorException() {
        super(KEY);
    }
}
