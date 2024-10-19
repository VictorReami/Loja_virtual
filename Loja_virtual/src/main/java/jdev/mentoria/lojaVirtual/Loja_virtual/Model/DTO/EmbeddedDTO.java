package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ConteudoBoletoJunoDTO> charges = new ArrayList<ConteudoBoletoJunoDTO>();
}
