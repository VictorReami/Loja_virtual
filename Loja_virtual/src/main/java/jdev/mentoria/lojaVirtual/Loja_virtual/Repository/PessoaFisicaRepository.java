package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaFisica;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long> {

    @Query(value = "select pf from PessoaFisica pf where upper(trim(pf.nome)) like %?1%")
    public List<PessoaFisica> pesquisaPorNomePF(String nome);

    @Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
    public List<PessoaFisica> pesquisaPorCPF(String nome);
}
