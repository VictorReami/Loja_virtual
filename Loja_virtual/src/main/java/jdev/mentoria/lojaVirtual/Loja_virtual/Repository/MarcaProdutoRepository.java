package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.MarcaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {

    @Query("select a from MarcaProduto a where upper(trim(a.nomeDesc)) like %?1%")
    List<MarcaProduto> buscarMarcaDesc(String desc);
}
