package mx.gob.imss.dpes.cartainstruccionfront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

public class ReporteCartaReinstalacionException extends BusinessException {

    public final static String ERROR_DESCONOCIDO_EN_EL_SERVICIO = "msg010";
    public final static String ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO = "msg012";
    public final static String ERROR_AL_GENERAR_CARTA_REINSTALACION = "msg011";
    public final static String ERROR_AL_INVOCAR_SERVICIO_SALDO_CAPITAL = "msg013";

    public ReporteCartaReinstalacionException(String messageKey) {
        super(messageKey);
    }
}
