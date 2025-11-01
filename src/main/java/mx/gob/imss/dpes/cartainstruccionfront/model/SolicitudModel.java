/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;

/**
 *
 * @author osiris.hernandez
 */
public class SolicitudModel extends BaseModel{
    @Getter @Setter private Long id;
    @Getter @Setter private Long estadoSolicitud;
}
