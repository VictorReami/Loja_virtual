package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FromEnvioEtiquetaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String email;
    private String document;
    private String company_document;
    private String state_register;
    private String address;
    private String complement;
    private String number;
    private String district;
    private String city;
    private String country_id;
    private String postal_code;
    private String note;

}
