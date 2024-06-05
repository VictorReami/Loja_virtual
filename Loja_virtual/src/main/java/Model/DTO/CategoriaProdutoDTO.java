package Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoriaProdutoDTO implements Serializable {

    private static final long SerialVersionUID = 1L;

    private Long id;
    private String nomeDesc;
    private String empresa;

}
