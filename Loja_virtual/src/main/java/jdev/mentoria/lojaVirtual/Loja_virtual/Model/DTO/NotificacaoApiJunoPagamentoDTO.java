package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NotificacaoApiJunoPagamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String eventId;
    private String eventType;
    private String timestmp;

    private List<DataNotificacaoPagamentoApiJunoDTO> data = new ArrayList<DataNotificacaoPagamentoApiJunoDTO>();

}
