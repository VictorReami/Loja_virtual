package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChargeNotificacaoPagamentoApiJunoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String code;
    private String amount;
    private String status;
    private String dueDate;

    private PayerNotificacaoPagamentoApiJunoDTO payerNotificacao = new PayerNotificacaoPagamentoApiJunoDTO();

}
