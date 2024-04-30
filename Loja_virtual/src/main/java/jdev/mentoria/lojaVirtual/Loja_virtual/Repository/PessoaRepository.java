package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {
}
