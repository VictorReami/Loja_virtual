package jdev.mentoria.lojaVirtual.Loja_virtual.Model;

//import jakarta.persistence.*;
import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.StatusContaPagar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    private BigDecimal valorDesconto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusContaPagar status;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtVencimento;

    @Temporal(TemporalType.DATE)
    private Date dtPagamento;

    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
    private Pessoa pessoa;

    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "pessoa_fornecedor_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fornecedor_fk"))
    private Pessoa pessoa_fornecedor;

    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private Pessoa empresa;

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
