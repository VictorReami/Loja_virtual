package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.CupomDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CupomDescRepository  extends JpaRepository<CupomDesc, Long > {

    //@Query(value = "SELECT a FROM CupomDesc a.empresa.id = ?1")
    @Query(value = "select c from CupomDesc c where c.empresa.id = ?1")
    public List<CupomDesc>  buscaCupomDescontoPorEmpresa(Long idEmpresa);

}
