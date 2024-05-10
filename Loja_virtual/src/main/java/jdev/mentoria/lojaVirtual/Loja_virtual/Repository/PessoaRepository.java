package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {

    @Query(value = "SELECT pj FROM PessoaJuridica pj WHERE pj.cnpj = ?1")
    public PessoaJuridica existeCnpjCadastrado(String cnpj);


}
