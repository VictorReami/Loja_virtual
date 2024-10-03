package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class VolumesEnvioEtiquetaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String height;
    private String width;
    private String length;
    private String weight;

}
