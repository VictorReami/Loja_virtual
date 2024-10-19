package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String chargeId;
    private String date;
    private String releaseDate;
    private String amount;
    private String fee;
    private String type;
    private String status;
    private String transactionId;
    private String failReason;
}
