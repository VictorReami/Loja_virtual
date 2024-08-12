package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
public class ImagemProdutoDTO implements Serializable {

    private Long id;

    private String imagemOriginal;

    private String imagemMiniatura;

    private Long produto;

    private Long empresa;
}
