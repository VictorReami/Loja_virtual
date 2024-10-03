package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TagsEnvioDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tag;
    private String url;

}
