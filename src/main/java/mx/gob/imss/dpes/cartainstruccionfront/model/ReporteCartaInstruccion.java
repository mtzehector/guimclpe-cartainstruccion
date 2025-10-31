package mx.gob.imss.dpes.cartainstruccionfront.model;


import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.Reporte;

import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;



public class ReporteCartaInstruccion extends BaseModel {

  @Getter @Setter ResumenCartaInstruccion resumenCartaInstruccion = new ResumenCartaInstruccion();
  @Getter @Setter Reporte<ResumenCartaInstruccion> reporte = new Reporte<>();
  @Getter @Setter Long idSolicitud;
}
