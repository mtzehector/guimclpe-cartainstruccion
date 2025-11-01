/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model.consultacarta;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.model.UserProfile;

/**
 *
 * @author osiris.hernandez
 */
public class PromotorRequest extends UserProfile{

    @Getter @Setter private Long id;
    @Getter @Setter private String numEmpleado;
    @Getter @Setter private String cveCurp;
    @Getter @Setter private String cveEntidadFinanciera;
}
