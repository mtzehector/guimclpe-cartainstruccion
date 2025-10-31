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
import mx.gob.imss.dpes.cartainstruccionfront.restclient.PrestamoRecuperacionClient;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.entidadfinanciera.model.EntidadFinanciera;
import mx.gob.imss.dpes.interfaces.prestamo.model.AmortizacionInsumos;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author edgar.arenas
 */
@Provider
public class ConsultaPrestamosRecuperacionService extends ServiceDefinition<ConsultaCartaInstruccionModel, ConsultaCartaInstruccionModel> {

    @Inject
    @RestClient
    private PrestamoRecuperacionClient consultaPrestamoRecClient;

    @Inject
    private ConsultarNombreComercialSipre consultarNombreComercialSipre;

    @Inject
    private AmortizacionClient client;

    @Override
    public Message<ConsultaCartaInstruccionModel> execute(Message<ConsultaCartaInstruccionModel> request)
        throws BusinessException {

        try {
            //log.log(Level.INFO, ">>>cartaInstruccionFront|ConsultaPrestamosRecuperacionService|execute {0}", request);

            PrestamoRecuperacion recuperacion = new PrestamoRecuperacion();
            recuperacion.setSolicitud(request.getPayload().getSolicitud().getId());

            Response respuesta = consultaPrestamoRecClient.consultaPrestamosPorSolicitud(request.getPayload().getSolicitud().getId());

            if (respuesta.getStatus() == 200) {
                PrestamoEnRecuperacionRs rs = respuesta.readEntity(PrestamoEnRecuperacionRs.class);
                for (PrestamoRecuperacion p : rs.getPrestamosEnRecuperacion()) {
                    //Obtener Nombre Comercial EF
                    String a = p.getNumEntidadFinanciera().replace(" ", "");
                    EntidadFinanciera e = new EntidadFinanciera();
                    e.setIdSipre(a);
                    //log.log(Level.INFO, ">>>ID EF SIPRE {0}: " + e.getIdSipre());
                    Message<EntidadFinanciera> es = consultarNombreComercialSipre.execute(new Message<>(e));
                    if (es != null) {
                        //p.setNombreComercial(es.getPayload().getNombreComercial());
                        //p.setCveEntidadFinanciera(es.getPayload().getId());
                        p.setClabe(es.getPayload().getClabe());
                    }
                    //Obtener Saldo Capital
                    AmortizacionInsumos saldoCapital = new AmortizacionInsumos();
                    saldoCapital.setFolioSipre(p.getNumSolicitudSipre());
                    saldoCapital.setNumMensualidad(p.getNumMesRecuperado());
                    Response sc = client.obtenerSaldoCapital(saldoCapital);
                    AmortizacionInsumos res = sc.readEntity(AmortizacionInsumos.class);
                    p.setSaldoCapital(res.getSaldoCapital());
                    //log.log(Level.INFO, ">>>SALDO CAPITAL {0}: " + p);
                }
                request.getPayload().setListPrestamoRecuperacion(rs);

                double suma = 0d;
                for(PrestamoRecuperacion p : request.getPayload().getListPrestamoRecuperacion().getPrestamosEnRecuperacion()) {
                    if (p.getSaldoCapital() != null) {
                        if (p.getCanMontoSol() != null) {
                            suma += p.getCanMontoSol();
                        } else {
                            suma += p.getSaldoCapital();
                        }
                    }
                }
                double monto = request.getPayload().getPrestamo().getMonto();
                double apagar = monto-suma;
                request.getPayload().getPrestamo().setImporteARecibir(apagar);
    
                //log.log(Level.INFO, ">>>LIST PRESTAMOS REC {0}: " + request.getPayload().getPrestamoRecuperacion().getSaldoCapital());
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "ERROR ConsultaPrestamosRecuperacionService.execute = {0}", e);
            request.getPayload().setPrestamoRecuperacion(null);
        } finally {
            return request;
        }
    }
}
