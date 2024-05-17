package jdev.mentoria.lojaVirtual.Loja_virtual.Repository;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    @Query(value = "SELECT u FROM Usuario u WHERE u.dataAtualSenha <= current_date - 6")
    List<Usuario> usuarioSenhvencida();

    @Query(value = "select u from Usuario u where u.login = ?1")
    Usuario findUserByLogin(String login);

    @Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login =?2")
    Usuario findUserByPessoa(Long id, String email);

    @Query(value = "SELECT CONSTRAINT_NAME\n" +
                    "FROM information_schema.constraint_column_usage\n" +
                    "WHERE table_name = 'usuarios_acesso'\n" +
                    "AND column_name = 'acesso_id'\n" +
                    "AND constraint_name != 'unique_acesso_user';", nativeQuery = true)
    String consultaContraintAcesso();

/*
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO usuarios_acesso (usuario_id, acesso_id)VALUES(?1, (SELECT id FROM acesso WHERE descricao = 'ROLE_USER'))")
    void insereAcessoUsuarioPJ(Long id);*/


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO usuarios_acesso (usuario_id, acesso_id)VALUES(?1, (SELECT id FROM acesso WHERE descricao = ?2 limit 1))")
    void insereAcessoUsuarioPJ(Long id,String acesso);
}
