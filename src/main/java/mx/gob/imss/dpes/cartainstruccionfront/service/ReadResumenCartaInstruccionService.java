/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.service;

/**
 *
 * @author osiris.hernandez
 */
import java.text.DecimalFormat;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.ReporteCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.restclient.DocumentoClient;
import mx.gob.imss.dpes.common.enums.TipoDocumentoEnum;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.exception.RecursoNoExistenteException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.model.ServiceStatusEnum;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.documento.model.Documento;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Provider
public class ReadResumenCartaInstruccionService extends ServiceDefinition<ReporteCartaInstruccion, ReporteCartaInstruccion> {

    @Inject
    @RestClient
    private DocumentoClient client;

    @Override
    public Message<ReporteCartaInstruccion> execute(Message<ReporteCartaInstruccion> request) throws BusinessException {

        log.log(Level.INFO, "Buscando el resumen de la Carta de Libranza: {0}", request.getPayload().getIdSolicitud());

        Documento documento = new Documento();
        documento.setCveSolicitud(request.getPayload().getIdSolicitud());
        documento.setTipoDocumento(TipoDocumentoEnum.CARTA_INSTRUCCION);

        Response response = client.loadRefDocumento(documento);

        if (response.getStatus() == 200) {
            ResumenCartaInstruccion resumenCartaInstruccion = response.readEntity(ResumenCartaInstruccion.class);

            DecimalFormat formatter = new DecimalFormat("#,###.00");
            resumenCartaInstruccion.setMontoSolicitado(
                    formatter.format(Double.parseDouble(resumenCartaInstruccion.getMontoSolicitado()))
            );
            resumenCartaInstruccion.setImporteDescNomina(
                    formatter.format(Double.parseDouble(resumenCartaInstruccion.getImporteDescNomina()))
            );
            resumenCartaInstruccion.setTotalCredPagarInt(
                    formatter.format(Double.parseDouble(resumenCartaInstruccion.getTotalCredPagarInt()))
            );
            resumenCartaInstruccion.setImporteARecibir(
                    formatter.format(Double.parseDouble(resumenCartaInstruccion.getImporteARecibir())));

            resumenCartaInstruccion.setTipoCredito(resumenCartaInstruccion.getTipoCredito().toUpperCase());
            resumenCartaInstruccion.setTipoPension(resumenCartaInstruccion.getTipoPension().toUpperCase());
            resumenCartaInstruccion.setTipoTrabajador(resumenCartaInstruccion.getTipoTrabajador().toUpperCase());

            resumenCartaInstruccion.setCiudad(WordUtils.capitalize(resumenCartaInstruccion.getCiudad()));
            
            request.getPayload().setResumenCartaInstruccion(resumenCartaInstruccion);
            return request;
        }

        return response(null, ServiceStatusEnum.EXCEPCION, new RecursoNoExistenteException(), null);
    }

}
