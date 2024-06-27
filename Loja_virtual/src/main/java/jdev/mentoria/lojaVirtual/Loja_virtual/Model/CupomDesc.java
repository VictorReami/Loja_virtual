package jdev.mentoria.lojaVirtual.Loja_virtual.Model;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cupom_desc")
@SequenceGenerator(name = "seq_cupom_desc", sequenceName = "seq_cupom_desc", allocationSize = 1, initialValue = 1)
public class CupomDesc implements Serializable {
    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cupom_desc")
    private Long id;

    @NotEmpty(message = "Informe o codigo desconte.")
    @Column(nullable = false)
    private String codigoDesc;

    private BigDecimal valorRealDesc;

    private BigDecimal valorPorcentDesc;

    @NotEmpty(message = "Informe a data de validade do cupom.")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataValidadeCupom;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CupomDesc cupomDesc = (CupomDesc) o;
        return Objects.equals(getId(), cupomDesc.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
