package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    @Query(nativeQuery = true, value = "SELECT count(1) > 0 FROM categoria_produto WHERE trim(upper(nome_desc)) = trim(upper(?1))")
    public boolean existeCategoria(String nomeCategoria);

    @Query("select a from CategoriaProduto a where upper(trim(a.nomeDesc)) like %?1%")
    public List<CategoriaProduto> buscarCategoriaDes(String nomeDesc);



}
