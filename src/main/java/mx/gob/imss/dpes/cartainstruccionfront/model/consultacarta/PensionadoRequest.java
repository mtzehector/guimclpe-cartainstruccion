/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author cesar.leon
 */
public class PensionadoRequest extends BaseModel {
     @Getter @Setter private String nss; 
     @Getter @Setter private String grupoFamiliar;
}
