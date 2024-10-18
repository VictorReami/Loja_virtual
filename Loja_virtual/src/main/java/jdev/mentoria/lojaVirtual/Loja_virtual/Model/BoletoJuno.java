package jdev.mentoria.lojaVirtual.Loja_virtual.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boleto_juno")
@SequenceGenerator(name = "seq_boleto_juno", sequenceName = "seq_boleto_juno", allocationSize = 1, initialValue = 1)
public class BoletoJuno implements Serializable {
    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_boleto_juno")
    private Long id;

    /*Código de controle do boleto*/
    private String code = "";

    /*Imprime o boleto completo com todas as parcelar*/
    private String Link = "";

    /*Mostra um telinha de checkout da Juno com os boleto, pix e cartão pagos ou vencidos*/
    private String checkoutUrl = "";

    private boolean quitado = false;

    private String dataVencimento= "";

    private String dataPagamento= "";

    private BigDecimal valor = BigDecimal.ZERO;

    private Integer recorrencia = 0;

    /*ID controle do boleto para poder cancelar pela API*/
    private String idChrBoleto= "";

    /*Link da parcela do boleto*/
    private String installmentLink= "";

    private String IdPix= "";

    @Column(columnDefinition = "TEXT")
    private String payloadInBase64= "";

    @Column(columnDefinition = "TEXT")
    private String imageInBase64 = "";

    private String chargeICartao = "";

    @ManyToOne
    @JoinColumn(name = "venda_compra_loja_virt_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "venda_compra_loja_virt_fk"))
    private VendaCompraLojaVirtual vendaCompraLojaVirtual;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoletoJuno that = (BoletoJuno) o;
        return isQuitado() == that.isQuitado() && Objects.equals(getId(), that.getId()) && Objects.equals(getCode(), that.getCode()) && Objects.equals(getLink(), that.getLink()) && Objects.equals(getCheckoutUrl(), that.getCheckoutUrl()) && Objects.equals(getDataVencimento(), that.getDataVencimento()) && Objects.equals(getDataPagamento(), that.getDataPagamento()) && Objects.equals(getValor(), that.getValor()) && Objects.equals(getRecorrencia(), that.getRecorrencia()) && Objects.equals(getIdChrBoleto(), that.getIdChrBoleto()) && Objects.equals(getInstallmentLink(), that.getInstallmentLink()) && Objects.equals(getIdPix(), that.getIdPix()) && Objects.equals(getPayloadInBase64(), that.getPayloadInBase64()) && Objects.equals(getImageInBase64(), that.getImageInBase64()) && Objects.equals(getChargeICartao(), that.getChargeICartao()) && Objects.equals(getVendaCompraLojaVirtual(), that.getVendaCompraLojaVirtual()) && Objects.equals(getEmpresa(), that.getEmpresa());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getLink(), getCheckoutUrl(), isQuitado(), getDataVencimento(), getDataPagamento(), getValor(), getRecorrencia(), getIdChrBoleto(), getInstallmentLink(), getIdPix(), getPayloadInBase64(), getImageInBase64(), getChargeICartao(), getVendaCompraLojaVirtual(), getEmpresa());
    }
}
