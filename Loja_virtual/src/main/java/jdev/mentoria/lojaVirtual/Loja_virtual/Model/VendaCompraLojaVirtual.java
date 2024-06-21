package jdev.mentoria.lojaVirtual.Loja_virtual.Model;

//import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
@Table(name = "vd_cp_loja_virt")
@SequenceGenerator(name = "seq_vd_cp_loja_virt", sequenceName = "seq_vd_cp_loja_virt", allocationSize = 1, initialValue = 1)
public class VendaCompraLojaVirtual implements Serializable {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vd_cp_loja_virt")
    private Long id;

    @NotNull(message = "A pessoa compradora deve ser informada")
    @ManyToOne(targetEntity = PessoaFisica.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
    private PessoaFisica pessoa;

    @NotNull(message = "O endereco de entrega deve ser informado")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_entrega_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "endereco_entrega_fk"))
    private Endereco enderecoEntrega;

    @NotNull(message = "O endereco de cobranca deve ser informado")
    @ManyToOne
    @JoinColumn(name = "endereco_cobranca_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "endereco_cobranca_fk"))
    private Endereco enderecoCobranca;

    @Min(value = 1, message = "Valor total da venda deve ser maior que 1")
    @Column(nullable = false)
    private BigDecimal valorTotal;

    private BigDecimal valorDesconto;

    @NotNull(message = "A forma de pagamento deve ser informado")
    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "forma_pagamento_fk"))
    private FormaPagamento formaPagamento;

    @JsonIgnoreProperties(allowGetters = true)
    @NotNull(message = "A nota fiscal deve ser informada")
    @OneToOne
    @JoinColumn(name = "nota_fiscal_venda_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "nota_fiscal_venda_fk"))
    private NotaFiscalVenda notaFiscalVenda;

    @ManyToOne
    @JoinColumn(name = "cupom_desc_id",  foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "cupom_desc_fk"))
    private CupomDesc cupomDesc;

    @Min(value = 5, message = "valor do frete Invalido")
    @NotNull(message = "O valor do frete deve ser informado")
    @Column(nullable = false)
    private BigDecimal valorFrete;

    @Min(value = 1, message = "É preciso informar o dia da entrega")
    @Column(nullable = false)
    private Integer diaEntrega;

    @NotNull(message = "A data da venda deve ser informada")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataVenda;

    @NotNull(message = "A data da entrega deve ser informada")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataEntrega;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendaCompraLojaVirtual that = (VendaCompraLojaVirtual) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
