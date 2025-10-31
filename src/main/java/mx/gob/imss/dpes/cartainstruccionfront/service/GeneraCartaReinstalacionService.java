package mx.gob.imss.dpes.cartainstruccionfront.service;

import mx.gob.imss.dpes.cartainstruccionfront.exception.CartaReinstalacionException;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCreacionCartaInstruccion;
import mx.gob.imss.dpes.common.exception.BusinessException;
import mx.gob.imss.dpes.common.model.Message;
import mx.gob.imss.dpes.common.service.ServiceDefinition;
import mx.gob.imss.dpes.interfaces.prestamo.model.PrestamoRecuperacion;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Provider
public class GeneraCartaReinstalacionService extends ServiceDefinition<RequestCreacionCartaInstruccion, RequestCreacionCartaInstruccion> {

    @Override
    public Message<RequestCreacionCartaInstruccion> execute(Message<RequestCreacionCartaInstruccion> request) throws BusinessException {
        try{
            Map<String, Object> parametros = new HashMap<>();
            List<ResumenCartaInstruccion> listDataSource = new ArrayList<>();
            listDataSource.add(request.getPayload().getResumenCarta());
            JasperReport report = (JasperReport) JRLoader.loadObject(new ClassPathResource("/reports/CartaReinstalacion.jasper").getInputStream());
            JasperPrint print = JasperFillManager.fillReport(report, parametros, new JRBeanArrayDataSource(listDataSource.toArray()));
        }catch (Exception e) {
            log.log(Level.SEVERE, "ERROR GeneraCartaReinstalacionService.execute()", e);
        }
        throw new CartaReinstalacionException(CartaReinstalacionException.ERROR_AL_GENERAR_REPORTE_CARTA_REINSTALACION);
    }

}
