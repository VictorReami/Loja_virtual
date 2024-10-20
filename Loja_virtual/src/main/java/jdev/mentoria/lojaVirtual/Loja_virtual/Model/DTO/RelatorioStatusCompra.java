package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class RelatorioStatusCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "Informa a data inicial")
    private String dataInicial;

    @NotEmpty(message = "Informa a data final")
    private String dataFinal;

    private String codigoProduto ="";
    private String nomeProduto ="";
    private String emailCliente ="";
    private String foneCliente ="";
    private String valorVendaProduto ="";
    private String codigoCliente ="";
    private String nomeCliente ="";
    private String qtdEstoque ="";
    private String codigoVenda ="";
    private String statusVenda ="";
}
