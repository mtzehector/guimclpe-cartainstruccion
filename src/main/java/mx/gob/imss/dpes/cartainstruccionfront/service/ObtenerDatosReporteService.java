package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.ReporteCartaReinstalacionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.ReporteCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.DocumentoClient;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.text.DecimalFormat;
import java.util.logging.Level;

@Provider
public class ObtenerDatosReporteService extends ServiceDefinition<ReporteCartaInstruccion, ReporteCartaInstruccion> {

    @Inject
    @RestClient
    private DocumentoClient documentoClient;

    @Override
    public Message<ReporteCartaInstruccion> execute(Message<ReporteCartaInstruccion> request) throws BusinessException {
        try {
            Documento documento = new Documento();
            documento.setCveSolicitud(request.getPayload().getIdSolicitud());
            documento.setTipoDocumento(TipoDocumentoEnum.CARTA_REINSTALACION);
            Response response = documentoClient.loadRefDocumento(documento);

            if (response != null && response.getStatus() == 200) {

                DecimalFormat formatter = new DecimalFormat("#,###.00");
                ResumenCartaInstruccion resumenCartaInstruccion = response.readEntity(ResumenCartaInstruccion.class);

                resumenCartaInstruccion.setMontoSolicitado(resumenCartaInstruccion.getMontoSolicitado() == null? "0.0" :
                        formatter.format(Double.parseDouble(resumenCartaInstruccion.getMontoSolicitado()))
                );
                resumenCartaInstruccion.setImporteDescNomina(resumenCartaInstruccion.getImporteDescNomina() == null? "0.0" :
                        formatter.format(Double.parseDouble(resumenCartaInstruccion.getImporteDescNomina()))
                );
                resumenCartaInstruccion.setTotalCredPagarInt(resumenCartaInstruccion.getTotalCredPagarInt() == null? "0.0" :
                        formatter.format(Double.parseDouble(resumenCartaInstruccion.getTotalCredPagarInt()))
                );
                resumenCartaInstruccion.setImporteARecibir(resumenCartaInstruccion.getImporteARecibir() == null? "0.0" :
                        formatter.format(Double.parseDouble(resumenCartaInstruccion.getImporteARecibir()))
                );
                if (resumenCartaInstruccion.getTipoCredito() != null && !resumenCartaInstruccion.getTipoCredito().isEmpty())
                    resumenCartaInstruccion.setTipoCredito(resumenCartaInstruccion.getTipoCredito().toUpperCase());
                if (resumenCartaInstruccion.getTipoPension() != null && !resumenCartaInstruccion.getTipoPension().isEmpty())
                    resumenCartaInstruccion.setTipoPension(resumenCartaInstruccion.getTipoPension().toUpperCase());
                if (resumenCartaInstruccion.getTipoTrabajador() != null && !resumenCartaInstruccion.getTipoTrabajador().isEmpty())
                    resumenCartaInstruccion.setTipoTrabajador(resumenCartaInstruccion.getTipoTrabajador().toUpperCase());
                if (resumenCartaInstruccion.getCiudad() != null && !resumenCartaInstruccion.getCiudad().isEmpty())
                    resumenCartaInstruccion.setCiudad(WordUtils.capitalize(resumenCartaInstruccion.getCiudad()));

                request.getPayload().setResumenCartaInstruccion(resumenCartaInstruccion);
                return request;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "ERROR ObtenerDatosReporteService.execute() = {0}", e);
        }
        return response(
                null,
                ServiceStatusEnum.EXCEPCION,
                new ReporteCartaReinstalacionException(ReporteCartaReinstalacionException.ERROR_AL_INVOCAR_SERVICIO_DE_DOCUMENTO),
                null
        );
    }
}
