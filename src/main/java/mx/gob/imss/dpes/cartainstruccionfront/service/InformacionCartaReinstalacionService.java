package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaInstruccionException;
import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaReinstalacionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.pensionado.model.Pensionado;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@Provider
public class InformacionCartaReinstalacionService extends ServiceDefinition<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {

    @Override
    public Message<RequestCreacionCartaInstruccion> execute(Message<RequestCreacionCartaInstruccion> req) throws BusinessException {
        try {
            Solicitud solicitud = req.getPayload().getSolicitud();
            Pensionado pensionado = req.getPayload().getPensionado();
            Prestamo prestamo = req.getPayload().getPrestamo();
            PrestamoRecuperacion prestamoRecuperacion = new PrestamoRecuperacion();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            ResumenCartaInstruccion cartaReinstalacion = new ResumenCartaInstruccion();
            cartaReinstalacion.setFecha(simpleDateFormat.format(new Date()));
            if (solicitud.getNumFolioSolicitud() != null && !solicitud.getNumFolioSolicitud().isEmpty()) {
                cartaReinstalacion.setFolio(solicitud.getNumFolioSolicitud());
            }

            cartaReinstalacion.setNombre(pensionado.getNombre());
            cartaReinstalacion.setPrimerApe(pensionado.getPrimerApellido());
            cartaReinstalacion.setSegundoApe(pensionado.getSegundoApellido());
            cartaReinstalacion.setCurp(pensionado.getCurp());
            cartaReinstalacion.setNss(pensionado.getNss());
            cartaReinstalacion.setTelefono(pensionado.getTelefonoCelular());
            cartaReinstalacion.setEmail(pensionado.getCorreoElectronico());
            cartaReinstalacion.setDelegacionDesc(solicitud.getDelegacion());

            if (solicitud.getEntidadFinanciera() != null) {
                cartaReinstalacion.setNombreComercial(solicitud.getEntidadFinanciera().getNombreComercial());
                cartaReinstalacion.setTelefonoBanco(solicitud.getEntidadFinanciera().getTelefonoAtencionClientes());
                cartaReinstalacion.setCorreoElectronico(solicitud.getEntidadFinanciera().getCorreoElectronico());
                prestamoRecuperacion.setNombreComercial(solicitud.getEntidadFinanciera().getNombreComercial());
            }

            if (solicitud.getPrestamo() != null ) {
                cartaReinstalacion.setCat(solicitud.getPrestamo().getCatPrestamoPromotor() == null? "0.0" : solicitud.getPrestamo().getCatPrestamoPromotor());
                cartaReinstalacion.setMontoSolicitado(solicitud.getPrestamo().getImpMontoSol() == null? "0.0" : solicitud.getPrestamo().getImpMontoSol().toString());
                cartaReinstalacion.setImporteDescNomina(solicitud.getPrestamo().getImpDescNomina() == null? "0.0" : solicitud.getPrestamo().getImpDescNomina().toString());
                if (solicitud.getPrestamo().getOferta() != null && solicitud.getPrestamo().getOferta().getPlazo() != null) {
                    cartaReinstalacion.setPlazo(solicitud.getPrestamo().getOferta().getPlazo().getNumPlazo() == null? "0" : solicitud.getPrestamo().getOferta().getPlazo().getNumPlazo().toString());
                }
                cartaReinstalacion.setTotalCredPagarInt(prestamo.getImpTotalPagar() == null? "0.0" : prestamo.getImpTotalPagar().toString());
            }
            cartaReinstalacion.setNominaPrimerDesc(simpleDateFormat.format(solicitud.getAltaRegistro()));
            cartaReinstalacion.setFechaVigFolio("");
            cartaReinstalacion.setImporteARecibir("");

            prestamoRecuperacion.setCanCatPrestamo(0.5);
            prestamoRecuperacion.setImpRealPrestamo(0.5);
            prestamoRecuperacion.setCanDescuentoMensual(0.5);
            prestamoRecuperacion.setNumMesRecuperado(90);
            prestamoRecuperacion.setSaldoCapital(0.5);
            List<PrestamoRecuperacion> prestamosPorLiquidar = new ArrayList<>();
            prestamosPorLiquidar.add(prestamoRecuperacion);
            cartaReinstalacion.setPrestamosPorLiquidar(new JRBeanCollectionDataSource(prestamosPorLiquidar));
            req.getPayload().setResumenCarta(cartaReinstalacion);
            return req;
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR InformacionCartaReinstalacionService.execute = {0}", e);
            return response(null,
                    ServiceStatusEnum.EXCEPCION,
                    new CartaReinstalacionException(CartaReinstalacionException.ERROR_DESCONOCIDO_EN_EL_SERVICIO),
                    null
            );
        }

    }
}
