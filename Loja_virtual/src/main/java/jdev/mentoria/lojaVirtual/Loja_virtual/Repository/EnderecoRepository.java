package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

   /* @Query(value = "SELECT e FROM endereco e WHERE e.empresa.id = ?1")
    public List<Endereco> enderecoPJ(Long idempresa);*/

}
