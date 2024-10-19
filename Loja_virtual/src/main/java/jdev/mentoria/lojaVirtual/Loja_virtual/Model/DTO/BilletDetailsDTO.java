package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BilletDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String banckAccount;
    private String ourNumber;
    private String barcodeNumber;
    private String portifolio;


}
