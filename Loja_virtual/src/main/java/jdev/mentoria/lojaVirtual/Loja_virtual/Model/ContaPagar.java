package jdev.mentoria.lojaVirtual.Loja_virtual.Model;

//import jakarta.persistence.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.StatusContaPagar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conta_pagar")
@SequenceGenerator(name = "seq_conta_pagar", sequenceName = "seq_conta_pagar", allocationSize = 1, initialValue = 1)
public class ContaPagar implements Serializable {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_conta_pagar")
    private Long id;

   // @NotEmpty(message = "Informe o campo descrição da conta a pagar.")
    @NotNull(message = "Informe o campo descrição da conta a pagar.")
    @Column(nullable = false)
    private String descricao;

   // @NotEmpty(message = "Informe o valor total da conta a pagar.")
    @NotNull(message = "Informe o valor total da conta a pagar.")
    @Column(nullable = false)
    private BigDecimal valorTotal;

    private BigDecimal valorDesconto;

    @NotNull(message = "Informe o status da conta a pagar.")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusContaPagar status;

    //@NotEmpty(message = "Informe a data de vencimento da conta a pagar.")
    @NotNull(message = "Informe a data de vencimento da conta a pagar.")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtVencimento;

    @Temporal(TemporalType.DATE)
    private Date dtPagamento;

    @ManyToOne(targetEntity = PessoaFisica.class)
    @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
    private PessoaFisica pessoaFisica;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "pessoa_fornecedor_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fornecedor_fk"))
    private PessoaJuridica pessoa_fornecedor;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContaPagar that = (ContaPagar) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
