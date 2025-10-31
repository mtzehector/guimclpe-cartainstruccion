package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.basereport.service.ReporteService;
import mx.gob.imss.dpes.cartainstruccionfront.exception.ReporteCartaReinstalacionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.PrestamoRecuperacionIdRq;
import mx.gob.imss.dpes.cartainstruccionfront.model.PrestamosEnRecuperacionRequest;
import mx.gob.imss.dpes.cartainstruccionfront.model.ReporteCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.ConsultaTAClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoBackClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoFrontClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.Reporte;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionPorDescuento;
import mx.gob.imss.dpes.interfaces.prestamo.model.CapitalInsolutoRQ;
import mx.gob.imss.dpes.interfaces.prestamo.model.CapitalInsolutoRS;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;

@Provider
public class CrearCartaReinstalacionService extends ServiceDefinition<ReporteCartaInstruccion, ReporteCartaInstruccion> {

    @Inject
    private ReporteService service;

    @Inject
    @RestClient
    private ConsultaTAClient client;

    @Inject
    @RestClient
    private PrestamoFrontClient prestamoFrontClient;

    @Inject
    @RestClient
    private PrestamoBackClient backClient;


    @Override
    public Message<ReporteCartaInstruccion> execute(Message<ReporteCartaInstruccion> request) throws BusinessException {
        try {
            Reporte<ResumenCartaInstruccion> reporte = request.getPayload().getReporte();
            reporte.setRuta("/reports/CartaReinstalacion.jasper");

            AmortizacionInsumos amortizacionInsumos = new AmortizacionInsumos();
            amortizacionInsumos.setCveSolicitud(request.getPayload().getIdSolicitud());
            Response load = client.load(amortizacionInsumos);

            if (load.getStatus() == 200) {

                List<AmortizacionPorDescuento> tablaAmortAux = load.readEntity(List.class);
                JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(tablaAmortAux);

                List<PrestamoRecuperacion> prestamosPorLiquidar = obtenerPrestamos(request.getPayload().getIdSolicitud());
                JRBeanCollectionDataSource jrbcds2 = new JRBeanCollectionDataSource(prestamosPorLiquidar);

                request.getPayload().getResumenCartaInstruccion().setPrestamosPorLiquidar(jrbcds2);
                request.getPayload().getResumenCartaInstruccion().setTablaAmort(jrbcds);

                reporte.getBeans().add(request.getPayload().getResumenCartaInstruccion());

                Message<Reporte> response = service.execute(new Message(reporte));

                if (!Message.isException(response)) {
                    reporte.setPdf(response.getPayload().getPdf());
                    return request;
                } else {
                    return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
                }
            }
        }catch (BusinessException e) {
            log.log(Level.SEVERE, "ERROR CrearCartaReinstalacionService.execute()", e);
            throw e;
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR CrearCartaReinstalacionService.execute()", e);
        }
        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new ReporteCartaReinstalacionException(ReporteCartaReinstalacionException.ERROR_AL_GENERAR_CARTA_REINSTALACION),
                null
        );
    }

    public List<PrestamoRecuperacion> obtenerPrestamos(Long id) throws BusinessException {

        PrestamosEnRecuperacionRequest rq = new PrestamosEnRecuperacionRequest();
        PrestamoRecuperacionIdRq rqIn = new PrestamoRecuperacionIdRq();
        rqIn.setId(id);
        rq.setRequest(rqIn);

        log.log(Level.INFO, "Prestamos en recuperacion para Reporte RQ: {0}", rq);
        Response load = prestamoFrontClient.prestamosEnRecuperacion(rq);

        PrestamosEnRecuperacionRequest rq2 = load.readEntity(PrestamosEnRecuperacionRequest.class);
        log.log(Level.INFO, "Prestamos en recuperacion para Reporte RS: {0}", rq2.getResponse().getPrestamosEnRecuperacion());

        for (PrestamoRecuperacion p : rq2.getResponse().getPrestamosEnRecuperacion()) {
            if (p.getCanMontoSol() != null) {
                p.setSaldoCapital(p.getCanMontoSol());
            } else {
                CapitalInsolutoRQ caprq = new CapitalInsolutoRQ();
                caprq.setFolioSipre(p.getNumSolicitudSipre());
                caprq.setNumMensualidad(p.getNumMesRecuperado());
                caprq.setNumFolioSolicitud(p.getNumFolioSolicitud());
                try {
                    Response loadSalCap = backClient.capitalinsoluto(caprq);

                    if (loadSalCap.getStatus() == 200) {
                        CapitalInsolutoRS rs = loadSalCap.readEntity(CapitalInsolutoRS.class);
                        p.setSaldoCapital(rs.getSaldoCapital());
                    }
                } catch (Exception e) {
                    log.log(Level.SEVERE, "ERROR CrearCartaReinstalacionService.obtenerPrestamos()", e);
                    throw new ReporteCartaReinstalacionException(ReporteCartaReinstalacionException.ERROR_AL_INVOCAR_SERVICIO_SALDO_CAPITAL);
                }
            }
        }

        if (!rq2.getResponse().getPrestamosEnRecuperacion().isEmpty()) {
            return rq2.getResponse().getPrestamosEnRecuperacion();
        } else {
            return null;
        }
    }
}
