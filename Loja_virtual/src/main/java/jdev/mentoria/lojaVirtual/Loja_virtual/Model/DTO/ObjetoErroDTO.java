package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ObjetoErroDTO implements Serializable {

    private static final long SerialVersionUID = 1L;
    private String error;
    private String code;

}
