package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import java.util.List;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {



    //@Query(nativeQuery = true, value = "select * from imagem_produto where produto_id = ?1 ")
    @Query("select a from ImagemProduto a where a.produto.id = ?1")
    List<ImagemProduto> buscaImagemProduto(Long idProduto);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "delete from imagem_produto where produto_id = ?1")
    void deleteImagem(Long idProduto);



}
