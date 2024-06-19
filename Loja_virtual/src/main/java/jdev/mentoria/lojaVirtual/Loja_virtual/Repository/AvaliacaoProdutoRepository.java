package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.AvaliacaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

    @Query(value = "SELECT a FROM AvaliacaoProduto a WHERE a.produto.id = ?1 ")
    List<AvaliacaoProduto> avalicaoProduto(Long idProduto);

    @Query(value = "SELECT a FROM AvaliacaoProduto a WHERE a.produto.id = ?1 AND a.pessoa.id =?2")
    List<AvaliacaoProduto> avalicaoProdutoPessoa(Long idProduto, Long idPessoa);

    @Query(value = "SELECT a FROM AvaliacaoProduto a WHERE a.pessoa.id =?1")
    List<AvaliacaoProduto> avalicaoPessoa(Long idPessoa);

}
