/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.rules;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.CartaInstruccionModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.PrestamoModel;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.common.rule.BaseRule;
/**
 *
 * @author osiris.hernandez
 */
@Provider
public class CartaInstruccionRule extends BaseRule<CapacidadCreditoRequest, CartaInstruccionModel>{


  @Override
  public CartaInstruccionModel apply(CapacidadCreditoRequest input) {
    CartaInstruccionModel cartaInstruccion = new CartaInstruccionModel();
    cartaInstruccion.setCveSolicitud(input.getPrestamo().getSolicitud());
    return cartaInstruccion;
  }
  
}
