package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BillingJunoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String document;
    private String email;
    private String phone;
    private boolean notify = true;


}
