package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;


import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Endereco;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.ItemVendaLoja;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Pessoa;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaFisica;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VendaCompraLojaVirtualDTO {

    private Long id;

    private BigDecimal valorTotal;

    private BigDecimal valorDesconto;

    private BigDecimal valorFrete;

    private Pessoa pessoa;

    private Endereco enderecoEntrega;

    private Endereco enderecoCobranca;

    private List<ItemVendaLojaDTO> itemVendaLoja = new ArrayList<ItemVendaLojaDTO>();


}
