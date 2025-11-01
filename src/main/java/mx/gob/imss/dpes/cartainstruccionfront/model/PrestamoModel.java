/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mx.gob.imss.dpes.common.model.BaseModel;
import mx.gob.imss.dpes.support.config.CustomDateDeserializer;
import mx.gob.imss.dpes.support.config.CustomDateSerializer;

/**
 *
 * @author osiris.hernandez
 */
public class PrestamoModel extends BaseModel{
    
    @Getter @Setter private BigDecimal monto;
    @Getter @Setter private BigDecimal impDescuentoNomina;
    @Getter @Setter private BigDecimal impTotalPagar;
    @JsonDeserialize( using = CustomDateDeserializer.class )
    @JsonSerialize(using = CustomDateSerializer.class) @Getter @Setter private Date fecPrimerDescuento;
    @Getter @Setter private Integer numPeriodoNomina;
    @Getter @Setter private String refCuentaClabe;
    @Getter @Setter private Long condicionOferta;
    @Getter @Setter private Long promotor;
    @Getter @Setter private Long simulacion;
    @Getter @Setter private Long credito;
    @Getter @Setter private Long solicitud;
}
