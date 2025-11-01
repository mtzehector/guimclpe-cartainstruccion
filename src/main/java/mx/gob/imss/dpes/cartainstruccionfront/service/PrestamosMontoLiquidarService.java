/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.service;

import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.PrestamoEnRecuperacionRs;
import mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta.ConsultaCartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.AmortizacionClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.EntidadFinancieraBackClient;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoRecuperacionClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.solicitud.model.Solicitud;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class PrestamosMontoLiquidarService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private PrestamoRecuperacionClient consultaPrestamoRecClient;

    @Inject
    @RestClient
    private EntidadFinancieraBackClient entidadFClient;
    
    @Inject
    private AmortizacionClient client;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request) throws BusinessException {
        Response response = entidadFClient.obtenerEntidadFinanciera(request.getPayload().getCveEFOperador());
        EntidadFinanciera ef = response.readEntity(EntidadFinanciera.class);
        //log.log(Level.INFO, ">>>cartaInstruccionFront|PrestamosMontoLiquidarService|execute {0}", ef.getCveEntidadFinancieraSipre());

        try {
            Response responsePrestamos = consultaPrestamoRecClient.consultaPrestamosMontoLiquidar(request.getPayload().getSolicitud().getId(), ef.getCveEntidadFinancieraSipre());
            if (responsePrestamos.getStatus() == 200) {
                PrestamoEnRecuperacionRs rs = responsePrestamos.readEntity(PrestamoEnRecuperacionRs.class);
                for (PrestamoRecuperacion p : rs.getPrestamosEnRecuperacion()) {
                    AmortizacionInsumos saldoCapital = new AmortizacionInsumos();
                    saldoCapital.setFolioSipre(p.getNumSolicitudSipre());
                    saldoCapital.setNumMensualidad(p.getNumMesRecuperado());
                    saldoCapital.setNumFolioSolicitud(p.getNumFolioSolicitud());
                    Response sc = client.obtenerSaldoCapital(saldoCapital);
                    AmortizacionInsumos res = sc.readEntity(AmortizacionInsumos.class);
                    p.setSaldoCapital(res.getSaldoCapital());
                    //log.log(Level.INFO, ">>>SALDO CAPITAL {0}: " + p);
                }
                request.getPayload().setListPrestamoRecuperacion(rs);
                //log.log(Level.INFO, ">>>LIST PRESTAMOS REC {0}: " + request.getPayload().getPrestamoRecuperacion().getSaldoCapital());
            }
        } catch (Exception e) {
            request.getPayload().setPrestamoRecuperacion(null);
            log.log(Level.WARNING, e.toString());
            log.log(Level.WARNING, "ERROR PrestamosMontoLiquidarService.execute = {0}", e);
        } finally {
            return request;
        }
    }
}
