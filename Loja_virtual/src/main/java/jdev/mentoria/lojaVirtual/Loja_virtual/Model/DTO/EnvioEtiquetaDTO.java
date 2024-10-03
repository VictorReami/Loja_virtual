package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EnvioEtiquetaDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    private String service;
    private String agency;


    private FromEnvioEtiquetaDTO from = new FromEnvioEtiquetaDTO();


    private ToEnvioEtiquetaDTO to = new ToEnvioEtiquetaDTO();


    private List<ProductsEnvioEtiquetaDTO> products = new ArrayList<ProductsEnvioEtiquetaDTO>();


    private List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();

    private OptionsEnvioEtiquetaDTO options = new OptionsEnvioEtiquetaDTO();

}
