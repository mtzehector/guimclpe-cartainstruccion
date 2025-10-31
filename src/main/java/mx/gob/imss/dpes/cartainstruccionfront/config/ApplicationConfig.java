package mx.gob.imss.dpes.cartainstruccionfront.config;

import mx.gob.imss.dpes.cartainstruccionfront.service.ActualizaSolicitudReinstalacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaCartaReinstalacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.GeneraCadenaYSelloCartaReinstalacionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.GenerarReporteReinstalacionPrestamo;

import javax.ws.rs.core.Application;
import java.util.Set;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {

        resources.add(mx.gob.imss.dpes.basereport.service.ReporteService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.CapacidadCreditoEndPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.CapturarCondicionesEndPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.CartaInstruccionEndPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.CartaReciboEndPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.ConsultaCartaInstruccionEndPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.ReporteCartaInstruccionEndPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.ResumenCapacidadCreditoEnPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.CartaReinstalacionEndPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.endpoint.ReporteCartaReinstalacionEndPoint.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.rules.CartaInstruccionRule.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.rules.CreateCapturarCondicionesRule.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.rules.PrestamoRule.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.rules.SolicitudRule.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.rules.CadenaOriginalCartaReinstalacionRule.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ActualizaReporteService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ActualizaSolicitudReinstalacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CapturarCondicionesService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ComplementaPrestamosRecuperacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaCapacidadCreditoService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaCartaReinstalacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaPersonaService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaPrestamosRecuperacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaReporteService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultaSolicitudService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarCondicionOfertaService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarDocumentosService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarLogoEntidadFinancieraService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarNombreComercialSipre.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarPensionadoService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarPrestamoService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarPromotorService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarSolicitudService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CorreoService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CorreoSimulacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CreateCartaInstruccionCapacidadService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CreateCartaInstruccionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CreateCartaReinstalacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CreateEventService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CreateReporteCartaInstruccionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.GeneraCartaReciboService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.GenerarReportePrestamoPromotor.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.GenerarReporteReinstalacionPrestamo.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.GenerarReporteSimulacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.GetPrestamoPromotorService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.GuardaReporteService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.InfoReporteService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.InfoReporteSimulacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ObtenerSelloElectronicoService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.PrestamoPromotorService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.PrestamoService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.PrestamosMontoLiquidarService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ReadResumenCartaInstruccionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.SolicitudEstadoService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.SolicitudService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarOfertaService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ConsultarCatPromotorService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.GeneraCartaReinstalacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.InformacionCartaReinstalacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.GeneraCadenaYSelloCartaReinstalacionService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.ObtenerDatosReporteService.class);
        resources.add(mx.gob.imss.dpes.cartainstruccionfront.service.CrearCartaReinstalacionService.class);
        resources.add(mx.gob.imss.dpes.common.exception.AlternateFlowMapper.class);
        resources.add(mx.gob.imss.dpes.common.exception.BusinessMapper.class);
        resources.add(mx.gob.imss.dpes.common.rule.MontoTotalRule.class);
        resources.add(mx.gob.imss.dpes.common.rule.PagoMensualRule.class);

    }
}
