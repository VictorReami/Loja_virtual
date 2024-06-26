package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.StatusRastreio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {

    @Query(value = "SELECT s FROM StatusRastreio s WHERE s.vendaCompraLojaVirtual.id = ?1 order by s.id")
    public List<StatusRastreio> listaRastreiovenda(Long idVenda);




}
