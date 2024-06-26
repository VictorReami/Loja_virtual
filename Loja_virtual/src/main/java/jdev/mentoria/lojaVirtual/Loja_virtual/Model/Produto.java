package jdev.mentoria.lojaVirtual.Loja_virtual.Model;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produto")
@SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")
    private Long id;

    @NotNull(message = "O tipo da unidade deve ser informada.")
    @Column(nullable = false)
    private String tipoUnidade;

    @Size(min = 10, message = "Nome do produto deve ter mais de 10 letras")
    @NotNull(message = "Nome do produto deve ser informado.")
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @NotNull(message = "Descrição do produto deve ser informada.")
    @Column(columnDefinition = "text", length = 2000, nullable = false)
    private String descricao;

    /* Nota item notaproduto - ASSOCIAR */

    @NotNull(message = "Peso deve ser informado.")
    @Column(nullable = false)
    private Double peso; /* EX: 1000.55*/

    @NotNull(message = "Largura deve ser informado")
    @Column(nullable = false)
    private Double largura;

    @NotNull(message = "Altura deve ser informado")
    @Column(nullable = false)
    private Double altura;

    @NotNull(message = "Profundidade")
    @Column(nullable = false)
    private Double profundidade;

    @NotNull(message = "Valor de venda deve ser informado")
    @Column(nullable = false)
    private BigDecimal valorVenda = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer qtdeEstoque = 0;

    private Integer qtdeAlertaEstoque = 0;

    private String linkYoutube;

    private Boolean alertaQtdeEstoque = Boolean.FALSE;

    private Integer qtdeClick = 0;

    @NotNull(message = "A empresa responsável deve ser informada")
    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    @NotNull(message = "A Categoria do Produto deve ser informada")
    @ManyToOne(targetEntity = CategoriaProduto.class)
    @JoinColumn(name = "categoria_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "categoria_produto_id_fk"))
    private CategoriaProduto categoriaProduto = new CategoriaProduto();

    @NotNull(message = "A Marca do Produto deve ser informada")
    @ManyToOne(targetEntity = MarcaProduto.class)
    @JoinColumn(name = "marca_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "marca_produto_id_fk"))
    private MarcaProduto marcaProduto = new MarcaProduto();

    @OneToMany(mappedBy = "produto", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImagemProduto> imagemProduto = new ArrayList<ImagemProduto>();

   /* @NotNull(message = "A Nota Item do Produto deve ser informada")
    @ManyToOne(targetEntity = NotaItemProduto.class)
    @JoinColumn(name = "nota_item_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "nota_item_produto_id_fk"))
    private NotaItemProduto notaItemProduto = new NotaItemProduto();*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(getId(), produto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
