package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Produto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemVendaLojaDTO {

    private Double quantidade;

    private Produto produto;


}
