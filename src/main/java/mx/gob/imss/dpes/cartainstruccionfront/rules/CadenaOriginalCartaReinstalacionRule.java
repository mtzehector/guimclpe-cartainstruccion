/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.rules;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.common.enums.SelloElectronicoEnum;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.interfaces.prestamo.model.ResumenCartaInstruccion;
import mx.gob.imss.dpes.interfaces.serviciosdigitales.model.SelloElectronicoRequest;

import java.text.SimpleDateFormat;
import lombok.Getter;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class CadenaOriginalCartaReinstalacionRule extends BaseRule<CadenaOriginalCartaReinstalacionRule.Input, CadenaOriginalCartaReinstalacionRule.Output> {

    public static final String SEPARATOR = "|";
    public static final String TIPOCREDITO = "Nuevo";
    public static final String COLON = ":";

    @Override
    public Output apply(Input input) {
      StringBuilder sb = new StringBuilder();
      
      sb.append(SEPARATOR);
      sb.append(SEPARATOR);
      sb.append(SelloElectronicoEnum.NSS);
      sb.append(COLON);
      sb.append(input.cartaReinstalacion.getNss());
      sb.append(SEPARATOR);
      sb.append(SelloElectronicoEnum.FOLIOPRESTAMO);
      sb.append(COLON);
      sb.append(input.cartaReinstalacion.getFolio());
      sb.append(SEPARATOR);
      sb.append(SelloElectronicoEnum.FECSOLICITUDPRESTAMO);
      sb.append(COLON);
      sb.append(input.cartaReinstalacion.getFecha());
      sb.append(SEPARATOR);
      sb.append(SelloElectronicoEnum.TIPOCREDITO);
      sb.append(COLON);
      sb.append("REINSTALACIÓN");
      sb.append(SEPARATOR);
      sb.append(SEPARATOR);
      
      SelloElectronicoRequest request = new SelloElectronicoRequest();
      request.setCadenaOriginal(sb.toString());        
      return new Output(request);
    }
    
     public class Input extends BaseModel {

        @Getter private final transient ResumenCartaInstruccion cartaReinstalacion;

        public Input(ResumenCartaInstruccion cartaReinstalacion) {
            this.cartaReinstalacion = cartaReinstalacion;
        }
    }

    public class Output extends BaseModel {

        @Getter private final SelloElectronicoRequest request;

        public Output(SelloElectronicoRequest request) {
            this.request = request;
        }
    }

}

