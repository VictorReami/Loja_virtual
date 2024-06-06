package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.NotaFiscalCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Max;
import java.util.List;

@Transactional
@Repository
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {


    @Query("SELECT a FROM NotaFiscalCompra a WHERE UPPER(TRIM(a.descricaoObs)) LIKE %?1%")
    List<NotaFiscalCompra> buscaNotaFiscalCompraDescicao(String desc);

    @Query("SELECT a FROM NotaFiscalCompra a WHERE a.pessoa = ?1 ")
    List<NotaFiscalCompra> buscaNotaFiscalCompraPorPessoa(Long idPessoa);

    @Query("SELECT a FROM NotaFiscalCompra a WHERE a.contaPagar.id = ?1")
    List<NotaFiscalCompra> buscaNotaFiscalCompraPorContaPagar(Long idContaPagar);


    @Query("SELECT a FROM NotaFiscalCompra a WHERE a.empresa.id = ?1")
    List<NotaFiscalCompra> buscaNotaFiscalCompraPorEmpresa(Long idEmpresa);

}
