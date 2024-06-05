package jdev.mentoria.lojaVirtual.Loja_virtual.Enums;

import lombok.Getter;

@Getter
public enum TipoEndereco {

    COBRANCA("Cobrança"),
    ENTREGA("Entrega");

    private String descricao;

    private TipoEndereco(String descricao){
        this.descricao = descricao;
    }

    @Override
    public String toString(){
        return this.descricao;
    }






}
