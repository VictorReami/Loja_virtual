package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.NotaItemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "DELETE FROM nota_item_produto WHERE nota_fiscal_compra_id = ?1")
    void deleteItemNotaFiscalCompra(Long idNotaCompra);

    @Query("SELECT a FROM NotaItemProduto a WHERE a.produto.id = ?1 AND a.notaFiscalCompra.id = ?2")
    List<NotaItemProduto> buscaNotaItemPorProdutoNota(Long idProduto, Long idNotaFiscal);

    @Query("SELECT a FROM NotaItemProduto a WHERE a.produto.id = ?1")
    List<NotaItemProduto> buscaNotaItemPorProduto(Long idProduto);

    @Query("SELECT a FROM NotaItemProduto a WHERE a.notaFiscalCompra.id = ?1")
    List<NotaItemProduto> buscaNotaItemPorNotaFiscal(Long idNotaFiscal);

    @Query("SELECT a FROM NotaItemProduto a WHERE a.empresa.id = ?1")
    List<NotaItemProduto> buscaNotaItemPorEmpresa(Long idEmpresa);
}
