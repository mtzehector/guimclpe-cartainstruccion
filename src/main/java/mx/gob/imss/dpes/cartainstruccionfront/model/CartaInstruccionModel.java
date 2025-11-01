/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author osiris.hernandez
 */
public class CartaInstruccionModel extends BaseModel{
  @Getter @Setter private Long cveSolicitud;
  @Getter @Setter private Long cveTipoDocumento;
  @Getter @Setter private String refSello;
  @Getter @Setter private String refDocumento;
}
