package jdev.mentoria.lojaVirtual.Loja_virtual.Enums;

import lombok.Getter;

@Getter
public enum StatusContaReceber {
    COBRANCA("Pagar"),
    VENCIDA("Vencia"),
    ABERTA("Aberta"),
    QUITADA("Quitada");

    private String descricao;

    private StatusContaReceber(String descricao){
        this.descricao = descricao;
    }

    @Override
    public String toString(){
        return this.descricao;
    }


}
