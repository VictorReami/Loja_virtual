package jdev.mentoria.lojaVirtual.Loja_virtual.Enums;

import lombok.Getter;

@Getter
public enum StatusVendaLojaVirtual {

    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada"),
    ABANDONOU_CARRINHO("Abandonou Carrinho");

    private String descricao = "";

    private StatusVendaLojaVirtual(String descricao){
        this.descricao = descricao;
    }

    @Override
    public String toString(){
        return this.descricao;
    }
}
