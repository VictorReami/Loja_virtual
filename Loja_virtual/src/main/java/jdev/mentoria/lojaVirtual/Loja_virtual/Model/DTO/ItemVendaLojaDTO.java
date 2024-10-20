package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Produto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ItemVendaLojaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double quantidade;

    private Produto produto;


}
