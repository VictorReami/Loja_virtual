package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

    @Query(value = "SELECT a FROM VendaCompraLojaVirtual a WHERE a.id = ?1 AND a.excluido = false")
    VendaCompraLojaVirtual findByIdExclusao(Long id);

    @Query(value = "SELECT distinct(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.excluido = false AND i.produto.id = ?1 ")
    List<VendaCompraLojaVirtual> vendaPorProduto(Long idProduto);

    @Query(value = "SELECT distinct(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.excluido = false AND upper(trim(i.produto.nome)) like %?1%")
    List<VendaCompraLojaVirtual> vendaPorNomeProduto(String valor);

    @Query(value = "SELECT distinct(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.excluido = false AND upper(trim(i.vendaCompraLojaVirtual.pessoa.nome)) like %?1%")
    List<VendaCompraLojaVirtual> vendaPorNomeCliente(String nomepessoa);

    @Query(value="select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.enderecoCobranca.ruaLogradouro)) like %?1%")
    List<VendaCompraLojaVirtual> vendaPorEndereCobranca(String enderecocobranca);

    @Query(value="select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i  where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.ruaLogradouro)) like %?1%")
    List<VendaCompraLojaVirtual> vendaPorEnderecoEntrega(String enderecoentrega);

    @Query(value="select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
            + " where i.vendaCompraLojaVirtual.excluido = false "
            + " and i.vendaCompraLojaVirtual.dataVenda >= ?1 "
            + " and i.vendaCompraLojaVirtual.dataVenda <= ?2 ")
    List<VendaCompraLojaVirtual> consultaVendaFaixaData(Date data1, Date data2);

    @Query(value="select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
            + " where i.vendaCompraLojaVirtual.excluido = false and i.vendaCompraLojaVirtual.pessoa.id = ?1")
    List<VendaCompraLojaVirtual> vendaPorCliente(Long idCliente);

    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE vd_cp_loja_virt SET codigo_etiqueta =?1 WHERE id = ?2")
    void updateEtiqueta(String idEtiqueta, Long idVenda);

    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "UPDATE vd_cp_loja_virt SET url_imprime_etiqueta =?1 WHERE id = ?2")
    void updateURLEtiqueta(String urlEtiqueta, Long id);


}
