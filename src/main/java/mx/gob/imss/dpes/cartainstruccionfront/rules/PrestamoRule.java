/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.cartainstruccionfront.rules;

import javax.ws.rs.ext.Provider;
import mx.gob.imss.dpes.cartainstruccionfront.model.CapacidadCreditoRequest;
import mx.gob.imss.dpes.common.enums.OrigenSolicitudEnum;
import mx.gob.imss.dpes.common.enums.TipoCreditoEnum;
import mx.gob.imss.dpes.common.enums.TipoEstadoSolicitudEnum;
import mx.gob.imss.dpes.common.rule.BaseRule;
import mx.gob.imss.dpes.interfaces.prestamo.model.Prestamo;

/**
 *
 * @author osiris.hernandez
 */
@Provider
public class PrestamoRule extends BaseRule<CapacidadCreditoRequest, Prestamo> {

    @Override
    public Prestamo apply(CapacidadCreditoRequest input) {
        Prestamo prestamo = new Prestamo();
        prestamo.setMonto(input.getPrestamo().getMonto());
        prestamo.setImpDescNomina(input.getPrestamo().getImpDescNomina());
        prestamo.setImpTotalPagar(input.getPrestamo().getImpTotalPagar());
        prestamo.setPrimerDescuento(input.getPrestamo().getPrimerDescuento());
        prestamo.setNumPeriodoNomina(input.getPrestamo().getNumPeriodoNomina());
        prestamo.setRefCuentaClabe(input.getPrestamo().getRefCuentaClabe());
        prestamo.setIdOferta(input.getPrestamo().getIdOferta());
        prestamo.setTipoCredito(TipoCreditoEnum.NUEVO);
        prestamo.setSolicitud(input.getPrestamo().getSolicitud());
        prestamo.setTipoSimulacion(input.getPrestamo().getTipoSimulacion());
        prestamo.setPromotor(input.getPrestamo().getPromotor());
        return prestamo;
    }
}
