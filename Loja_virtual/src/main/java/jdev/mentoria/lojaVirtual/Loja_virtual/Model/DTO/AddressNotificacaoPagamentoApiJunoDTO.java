package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressNotificacaoPagamentoApiJunoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String street;
    private String number;
    private String complement;
    private String city;
    private String state;
    private String postCode;
    private String neighborhood;

}
