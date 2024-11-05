package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AtributosNotificacaoPagamentoApiJunoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String createOn;
    private String date;
    private String releaseDate;
    private String amount;
    private String fee;
    private String status;
    private String type;

    private ChargeNotificacaoPagamentoApiJunoDTO charge = new ChargeNotificacaoPagamentoApiJunoDTO();

    private PixNotificacaoPagamentoApiJunoDTO pix = new PixNotificacaoPagamentoApiJunoDTO();

    private String digitalAccountId;
}
