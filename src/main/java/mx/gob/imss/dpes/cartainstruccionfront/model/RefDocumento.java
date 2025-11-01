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
 * @author eduardo.loyo
 */
public class RefDocumento extends BaseModel {
    @Getter @Setter private String nss; //"1098234750\",\"
    @Getter @Setter private String curp;////:\"PEGJ560626HDFEON09\",\"
    @Getter @Setter private String nombre;//":\"Juan\",\""
    @Getter @Setter private String primerAp;//":\"Pérez\",\""
    @Getter @Setter private String segundoAp;//":\"González\",\""
    @Getter @Setter private String delegacion;//":\"Benito Juárez\",\""
    @Getter @Setter private String telefono;//":\"1122334455\",\""
    @Getter @Setter private String email;//":\"some@mail.com\",\""
    @Getter @Setter private String tipoCredito;//":\"Nuevo\",\""
    @Getter @Setter private String tipoPension;//":\"Pensión\",\""
    @Getter @Setter private String tipoTrabajador;//":\"Pensionado\",\""
    @Getter @Setter private String nombreComercialEF;//":\"Banca Mifel\",\""
    @Getter @Setter private String razonSocialEF;//":\"Banca Mifel\",\""
    @Getter @Setter private String telefonoEF;//":\"5544332211\",\""
    @Getter @Setter private String paginaWebEF;//":\"www.mifel.com\",\""
    @Getter @Setter private String tasaAnual;//":\"10%\",\""
    @Getter @Setter private String cat;//":\"37%\",\""
    @Getter @Setter private String montoSolicitado;//":\"25000\",\""
    @Getter @Setter private String importeDescNomina;//":\"150\",\""
    @Getter @Setter private String totalDescAplicar;//":\"1500\",\""
    @Getter @Setter private String plazo;//":\"12\",\""
    @Getter @Setter private String totalCreditoPagarInteres;//":\"30000\",\""
    @Getter @Setter private String nominaPrimerDesc;//":\"Noviembre\",\""
    @Getter @Setter private String fecVigenciaFolio;//":\"Enero\",\""
    @Getter @Setter private String sello;//":\"SelloDigitalGoesHere\"}"
}
