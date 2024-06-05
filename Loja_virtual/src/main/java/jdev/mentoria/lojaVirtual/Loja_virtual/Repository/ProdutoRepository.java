package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.CategoriaProduto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Produto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(nativeQuery = true, value = "SELECT count(1) > 0 FROM Produto WHERE TRIM(UPPER(nome)) = TRIM(UPPER(?1))")
    public boolean existeProduto(String nome);

   /* @Query(nativeQuery = true, value = "SELECT count(1) > 0 FROM Produto WHERE TRIM(UPPER(nome)) = TRIM(UPPER(?1)) AND empresa_id = ?2")
    public boolean existeProduto(String nome, Long idEmpresa);*/

    @Query("SELECT a FROM Produto a WHERE UPPER(TRIM(a.nome)) like %?1%")
    public List<Produto> buscarProdutoNome(String nome);

    @Query("SELECT a FROM Produto a WHERE UPPER(TRIM(a.nome)) like %?1% AND a.empresa.id = ?2")
    public List<Produto> buscarProdutoNome(String nome, Long idEmpresa);
}
