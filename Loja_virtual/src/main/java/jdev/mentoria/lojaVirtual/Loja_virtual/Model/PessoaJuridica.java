package jdev.mentoria.lojaVirtual.Loja_virtual.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pessoa_juridica")
@PrimaryKeyJoinColumn(name = "id")
public class PessoaJuridica extends Pessoa{

    private static final long SerialVersionUID = 1L;

    private String cnpj;

    private String inscEstadual;

    private String inscMunicipal;

    private String nomeFantasia;

    private String razaoSocial;

    private String categoria;



}
