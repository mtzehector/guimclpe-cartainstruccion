package mx.gob.imss.dpes.cartainstruccionfront.model;

import lombok.Data;
import mx.gob.imss.dpes.common.model.BaseModel;

@Data
public class SelloElectronicoRequest extends BaseModel {

  private String nss;
  private String folioNegocio;
  private String fecCreacion;
  private String tipoCredito;
}
