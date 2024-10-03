package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OptionsEnvioEtiquetaDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String insurance_value;


    private boolean receipt;
    private boolean own_hand;
    private boolean reverse;
    private boolean non_commercial;

    private InvoiceEnvioDTO invoice = new InvoiceEnvioDTO();

    private String platform;

    private List<TagsEnvioDto> tags = new ArrayList<TagsEnvioDto>();

}
