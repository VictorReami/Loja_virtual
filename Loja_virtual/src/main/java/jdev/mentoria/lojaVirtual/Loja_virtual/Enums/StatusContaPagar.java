package jdev.mentoria.lojaVirtual.Loja_virtual.Enums;

public enum StatusContaPagar {
    COBRANCA("Pagar"),
    VENCIDA("Vencia"),
    ABERTA("Aberta"),
    ALUGUEL("Aluguel"),
    FUNCIONARIO("funcionario"),
    QUITADA("Quitada"),
    NEGOCIADA("Renegociada");

    private String descricao;

    private StatusContaPagar(String descricao){
        this.descricao = descricao;
    }
    @Override
    public String toString(){
        return this.descricao;
    }


}
