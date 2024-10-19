package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoletoGeradoApiJunoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<LinksDTO> _links = new ArrayList<LinksDTO>();

    private EmbeddedDTO embedded = new EmbeddedDTO();


}
