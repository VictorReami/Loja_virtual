package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.StatusRastreio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {

    @Query(value = "SELECT s FROM StatusRastreio s WHERE s.vendaCompraLojaVirtual.id = ?1 order by s.id")
    public List<StatusRastreio> listaRastreiovenda(Long idVenda);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE status_rastreio SET url_rastreio = ?1 WHERE venda_compra_loja_virt_id = ?2")
    void salvaUrlRastreio(String urlRastreio, Long idVenda);





}
