package mx.gob.imss.dpes.cartainstruccionfront.endpoint;


import mx.gob.imss.dpes.common.endpoint.BaseGUIEndPoint;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import mx.gob.imss.dpes.cartainstruccionfront.model.ReporteCartaInstruccion;
import mx.gob.imss.dpes.cartainstruccionfront.service.ReadResumenCartaInstruccionService;
import mx.gob.imss.dpes.cartainstruccionfront.service.CreateReporteCartaInstruccionService;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.io.StringWriter;
import java.util.logging.Level;

@Path("/reporteResumenCartaInstruccion")
@RequestScoped
public class ReporteCartaInstruccionEndPoint extends BaseGUIEndPoint<BaseModel, BaseModel, BaseModel> {

    ReporteCartaInstruccion request = new ReporteCartaInstruccion();

    @Inject
    ReadResumenCartaInstruccionService readResumenCartaInstruccionService;
    @Inject
    CreateReporteCartaInstruccionService createReporteCartaInstruccionService;

    @GET
    @Path("/{idSolicitud}")
    @Produces("application/pdf")
    @Operation(summary = "Generar el reporte de resumen de Carta de Libranza",
            description = "Generar el reporte de resumen de Carta de Libranza")
    public Response create(@PathParam("idSolicitud") Long idSolicitud) throws BusinessException {

        request.setIdSolicitud(idSolicitud);

        ServiceDefinition[] steps = {readResumenCartaInstruccionService, createReporteCartaInstruccionService};
        Message<ReporteCartaInstruccion> response = createReporteCartaInstruccionService.executeSteps(steps, new Message<>(request));

        if (!Message.isException(response)) {
            return Response.ok(response.getPayload().getReporte().getPdf()).header("Content-Disposition",
                    "attachment; filename=ReporteResumenCartaInstruccion.pdf").build();
        }
        return toResponse(response);

    }

    @GET
    @Path("/xml/{idSolicitud}")
    @Produces("application/xml")
    @Operation(summary = "Generar el reporte de resumen de Carta de Libranza en formato XML",
            description = "Generar el reporte de resumen de Carta de Libranza en formato XML")
    public Response createXML(@PathParam("idSolicitud") Long idSolicitud) throws BusinessException {

        request.setIdSolicitud(idSolicitud);

        ServiceDefinition[] steps = {readResumenCartaInstruccionService};
        Message<ReporteCartaInstruccion> response = readResumenCartaInstruccionService.executeSteps(steps, new Message<>(request));

        if (!Message.isException(response)) {
            String xml = obtenerXML(response.getPayload().getResumenCartaInstruccion());
            return Response.ok(xml.getBytes()).header("Content-Disposition",
                    "attachment; filename=ReporteResumenCartaInstruccion.xml").build();
        }
        return toResponse(response);
    }

    private String obtenerXML(ResumenCartaInstruccion resumenCartaInstruccion) {
        String xml = "";
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ResumenCartaInstruccion.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(resumenCartaInstruccion, sw);
            xml = sw.toString();

        } catch (JAXBException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return xml;
    }

}
