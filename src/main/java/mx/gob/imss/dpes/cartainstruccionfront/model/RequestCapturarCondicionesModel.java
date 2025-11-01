/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.interfaces.capacidadcredito.model.CapacidadCredito;


/**
 *
 * @author osiris.hernandez
 */
public class RequestCapturarCondicionesModel extends BaseModel{

    @Getter @Setter CondicionesPrestamoModel condicionesPrestamo;
    @Getter @Setter CapacidadCredito capacidadCredito;
    @Getter @Setter double catEntidadFinanciera;
    @Getter @Setter String flujo;
}
