package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class CartaReinstalacionException extends BusinessException {

    public static final String ERROR_DESCONOCIDO_EN_EL_SERVICIO ="msg010";

    public static final String ERROR_AL_GENERAR_REPORTE_CARTA_REINSTALACION = "msg011";

    public CartaReinstalacionException(String messageKey) {
        super(messageKey);
    }
}
