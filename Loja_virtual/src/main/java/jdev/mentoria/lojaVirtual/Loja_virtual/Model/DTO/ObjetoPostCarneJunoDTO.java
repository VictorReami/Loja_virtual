package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ObjetoPostCarneJunoDTO implements Serializable {

    private static final long SerialVersionUID = 1L;

    /*Descrição da cobrança*/
    private String description;

    /*Nome do comprador ou cliente*/
    private String payerName;

    /*Telefone do cliente comprador*/
    private String payerPhone;

    /*Valor da compra ou parcela*/
    private String totalAmount;

    /*Quantidade de parcelas*/
    private String installments;

    /*Referencia para o produto da loja ou Código do produto*/
    private String reference;

    private Integer recurrency = 0;

    private String payerCpfCnpj;

    private String payerEmail;

    private Long idVenda;


}
