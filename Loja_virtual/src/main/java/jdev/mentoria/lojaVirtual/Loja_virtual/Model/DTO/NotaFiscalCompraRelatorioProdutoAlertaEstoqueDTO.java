package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String nomeProduto = "";

    @NotEmpty(message = "Informar a data inicial")
    private String dataInicial = "";

    @NotEmpty(message = "Informar a data final")
    private String dataFinal = "";

    private String codigoNota = "";
    private String codigoProduto = "";
    private String valorvendaProduto = "";
    private String quantidadeCompra = "";
    private String codigoFornecedor = "";
    private String nomeFornecedor = "";
    private String dataCompra = "";
    private String qtdeEstoque = "";
    private String qtdeAlertaEstoque = "";
}
