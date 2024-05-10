package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Usuario;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.UsuarioRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PessoaUsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final PessoaRepository pessoaRepository;

    private final JdbcTemplate jdbcTemplate;

    public PessoaUsuarioService(UsuarioRepository usuarioRepository, PessoaRepository pessoaRepository, JdbcTemplate jdbcTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaRepository = pessoaRepository;
        this.jdbcTemplate = jdbcTemplate;
    }



    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica){

        //pessoaJuridica = pessoaRepository.save(pessoaJuridica);

        for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++){
            pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);
            pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);
        }

        pessoaJuridica = pessoaRepository.save(pessoaJuridica);

        Usuario usuarioPJ = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

        if(usuarioPJ == null){
            String constraint = usuarioRepository.consultaContraintAcesso();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
            }

            usuarioPJ = new Usuario();
            usuarioPJ.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPJ.setEmpresa(pessoaJuridica);
            usuarioPJ.setPessoa(pessoaJuridica);
            usuarioPJ.setLogin(pessoaJuridica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuarioPJ.setSenha(senhaCript);

            usuarioPJ = usuarioRepository.save(usuarioPJ);

            usuarioRepository.insereAcessoUsuarioPJ(usuarioPJ.getId());

        }

        return pessoaJuridica;

    }

}
