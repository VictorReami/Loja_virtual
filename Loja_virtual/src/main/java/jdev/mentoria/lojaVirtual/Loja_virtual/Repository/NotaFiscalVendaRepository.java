package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.NotaFiscalVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

    @Query(value = "select n from NotaFiscalVenda n where n.vendaCompraLojaVirtual.id = ?1")
    List<NotaFiscalVenda> buscaNotaFiscalPorVendaList(Long idVenda);

    @Query(value = "select n from NotaFiscalVenda n where n.vendaCompraLojaVirtual.id = ?1")
    NotaFiscalVenda buscaNotaFiscalPorVenda(Long idVenda);



}
