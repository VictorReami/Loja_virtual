package jdev.mentoria.lojaVirtual.Loja_virtual.Enums;

import lombok.Getter;

@Getter
public enum TipoPessoa {

    JURIDICA("Jurídica"),
    JURIDICA_FORNECEDOR("Jurídica e Fornecedor"),
    FISICA("Física");

    private String descricao;

    private TipoPessoa(String descricao){
        this.descricao = descricao;
    }

    @Override
    public String toString(){
        return this.descricao;
    }

}
