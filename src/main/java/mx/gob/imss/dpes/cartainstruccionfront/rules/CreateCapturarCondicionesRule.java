/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.rules;

import java.util.logging.Level;
import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.RequestCapturarCondicionesModel;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.common.rule.CatRule;

/**
 *
 * @author osiris.hernandez
 */

@Provider
public class CreateCapturarCondicionesRule extends BaseRule<RequestCapturarCondicionesModel, RequestCapturarCondicionesModel> {

    private long toComparar( double valor){
      return Math.round( valor*100d );
    }
  
    @Override
    public RequestCapturarCondicionesModel apply(RequestCapturarCondicionesModel input) {
        CatRule.Input inputCat = new CatRule().new Input(input.getCondicionesPrestamo().getMonto(), input.getCondicionesPrestamo().getPlazo().getNumPlazo(), input.getCondicionesPrestamo().getDescuentoMensual());
        CatRule rule = new CatRule();
        log.log(Level.INFO,"Datos Entrada {0}", inputCat);
        CatRule.Output output = rule.apply(inputCat);
        log.log(Level.INFO,"Datos Salida {0}", output);
        
        double cat = output.getCat() * 100d;
        // La comparación de los CATs se hara con dos decimales, por lo que primero
        // se redondea antes de campararlo
        log.log(Level.INFO, "### RULE capturacondiciones {0}", cat );
      return input;
//        if (input.getCondicionesPrestamo().getDescuentoMensual() > input.getCapacidadCredito().getImpCapacidadFija()) {
//            input.setFlujo("err001");
//            return input;
//        } else if (input.getCondicionesPrestamo().getDescuentoMensual() > input.getCapacidadCredito().getImpCapacidadTotal()) {
//            input.setFlujo("err002");
//            return input;
//        } else if (toComparar( input.getCondicionesPrestamo().getCat() ) != toComparar( input.getCatEntidadFinanciera() ) ) {
//            input.setFlujo("err003");
//            return input;
//        } else if (toComparar( cat ) != toComparar( input.getCatEntidadFinanciera() ) ) {
//            input.setFlujo("err004");
//            return input;
//        } else {
//            return input;
//        }
    }

}
