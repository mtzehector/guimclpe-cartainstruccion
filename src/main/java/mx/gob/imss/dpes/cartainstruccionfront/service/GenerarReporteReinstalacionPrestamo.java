package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.basereport.service.ReporteService;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.cartainstruccionfront.model.PrestamoRecuperacionIdRq;
import mx.gob.imss.dpes.cartainstruccionfront.model.PrestamosEnRecuperacionRequest;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoBackClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoFrontClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.Reporte;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;

@Provider
public class GenerarReporteReinstalacionPrestamo extends ServiceDefinition<CapacidadCreditoRequest, CapacidadCreditoRequest> {
    
    @Inject    
    private ReporteService service;
    
    @Inject
    @RestClient
    private PrestamoFrontClient prestamoFrontClient;

    @Inject
    @RestClient
    private PrestamoBackClient backClient;

    @Override
    public Message<CapacidadCreditoRequest> execute(Message<CapacidadCreditoRequest> request) throws BusinessException {
        
        Reporte<ResumenCartaInstruccion> reporte = request.getPayload().getReporte();
        reporte.setRuta("/reports/CartaReinstalacion.jasper");
         
        // Obtiene los prestamos en Recuperacion
        List<PrestamoRecuperacion> prestamosPorLiquidar = obtenerPrestamos(request.getPayload().getPrestamo().getSolicitud());
        JRBeanCollectionDataSource jrbcds2 = new JRBeanCollectionDataSource(prestamosPorLiquidar);

        request.getPayload().getResumenCarta().setPrestamosPorLiquidar(jrbcds2);
        //request.getPayload().getResumenCarta().setTablaAmort(jrbcds);

        DecimalFormat formatter = new DecimalFormat("#,###.00");
        request.getPayload().getResumenCarta().setMontoSolicitado(formatter.format(Double.parseDouble(request.getPayload().getResumenCarta().getMontoSolicitado())));
        request.getPayload().getResumenCarta().setImporteDescNomina(formatter.format(Double.parseDouble(request.getPayload().getResumenCarta().getImporteDescNomina())));
        request.getPayload().getResumenCarta().setTotalCredPagarInt(formatter.format(Double.parseDouble(request.getPayload().getResumenCarta().getTotalCredPagarInt())));
        request.getPayload().getResumenCarta().setImporteARecibir(formatter.format(Double.parseDouble(request.getPayload().getResumenCarta().getImporteARecibir())));

        reporte.getBeans().add(request.getPayload().getResumenCarta());

        Message<Reporte> response = service.execute(new Message(reporte));

        if (!Message.isException(response)) {
            reporte.setPdf(response.getPayload().getPdf());
            return request;
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }
    
    public List<PrestamoRecuperacion> obtenerPrestamos(Long id) {

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
