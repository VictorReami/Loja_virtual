package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DataNotificacaoPagamentoApiJunoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String entityId;
    private String entityType;

    private AtributosNotificacaoPagamentoApiJunoDTO atributos = new AtributosNotificacaoPagamentoApiJunoDTO();

}
