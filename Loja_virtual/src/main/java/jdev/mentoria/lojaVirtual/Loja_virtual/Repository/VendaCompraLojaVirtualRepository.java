package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

    @Query(value = "SELECT a FROM VendaCompraLojaVirtual a WHERE a.id = ?1 AND a.excluido = false")
    VendaCompraLojaVirtual findByIdExclusao(Long id);

    @Query(value = "SELECT i.vendaCompraLojaVirtual FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.excluido = false AND i.produto.id = ?1 ")
    List<VendaCompraLojaVirtual> vendaPorProduto(Long idProduto);



}
