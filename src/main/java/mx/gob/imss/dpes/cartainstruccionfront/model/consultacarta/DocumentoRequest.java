/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta;


import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author eduardo.loyo
 */
public class DocumentoRequest extends BaseModel {
       @Getter @Setter private String cveSolicitud;
       @Getter @Setter private List<Long> tiposDocumento;
}
