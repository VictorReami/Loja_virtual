package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PixDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String payloadInBase64;
    private String imageInBase64;
}
