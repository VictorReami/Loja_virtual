package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConteudoBoletoJunoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String code;
    private String reference;
    private String dueDate;
    private String link;
    private String installmentLink;
    private String payNumber;
    private String amount;
    private String status;
    private String checkoutUrl;

    private BilletDetailsDTO billetDetails = new BilletDetailsDTO();

    private List<PaymentDTO> payment = new ArrayList<PaymentDTO>();

    private PixDTO pix = new PixDTO();

    private List<LinksDTO> _links = new ArrayList<LinksDTO>();

}
