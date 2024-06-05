package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import Model.DTO.CepDTO;
import Model.DTO.ConsultaCnpjDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaFisica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Usuario;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaFisicaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaJuridicaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.UsuarioRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

@Service
public class PessoaUsuarioService {
    private final UsuarioRepository usuarioRepository;

    private final PessoaJuridicaRepository pessoaJuridicaRepository;


    private final JdbcTemplate jdbcTemplate;

    private final ServiceSendEmail serviceSendEmail;

    private final PessoaFisicaRepository pessoaFisicaRepository;

    public PessoaUsuarioService(UsuarioRepository usuarioRepository, PessoaJuridicaRepository pessoaJuridicaRepository, JdbcTemplate jdbcTemplate, ServiceSendEmail serviceSendEmail, PessoaFisicaRepository pessoaFisicaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.serviceSendEmail = serviceSendEmail;
        this.pessoaFisicaRepository = pessoaFisicaRepository;

    }



    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica) {

        //juridica = pesssoaRepository.save(juridica);

        for (int i = 0; i< juridica.getEnderecos().size(); i++) {
            juridica.getEnderecos().get(i).setPessoa(juridica);
            juridica.getEnderecos().get(i).setEmpresa(juridica);
        }

        juridica = pessoaJuridicaRepository.save(juridica);

        Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());

        if (usuarioPj == null) {

            String constraint = usuarioRepository.consultaConstraintAcesso();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
            }

            usuarioPj = new Usuario();
            usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPj.setEmpresa(juridica);
            usuarioPj.setPessoa(juridica);
            usuarioPj.setLogin(juridica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuarioPj.setSenha(senhaCript);

            usuarioPj = usuarioRepository.save(usuarioPj);

            usuarioRepository.insereAcessoUser(usuarioPj.getId());
            usuarioRepository.insereAcessoUsuarioPJ(usuarioPj.getId(), "ROLE_ADMIN");

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br/>");
            menssagemHtml.append("<b>Login: </b>"+juridica.getEmail()+"<br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");

            try {
                serviceSendEmail.enviaremailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString() , juridica.getEmail());
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        return juridica;

    }


    public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
        //juridica = pesssoaRepository.save(juridica);

        for (int i = 0; i< pessoaFisica.getEnderecos().size(); i++) {
            pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
            //pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
        }

        pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

        Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

        if (usuarioPj == null) {

            String constraint = usuarioRepository.consultaConstraintAcesso();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
            }

            usuarioPj = new Usuario();
            usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPj.setEmpresa(pessoaFisica.getEmpresa());
            usuarioPj.setPessoa(pessoaFisica);
            usuarioPj.setLogin(pessoaFisica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuarioPj.setSenha(senhaCript);

            usuarioPj = usuarioRepository.save(usuarioPj);

            usuarioRepository.insereAcessoUser(usuarioPj.getId());

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br/>");
            menssagemHtml.append("<b>Login: </b>"+pessoaFisica.getEmail()+"<br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");

            try {
                serviceSendEmail.enviaremailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString() , pessoaFisica.getEmail());
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        return pessoaFisica;
    }

    //Api de consulta de CEP
    public CepDTO consultaCEP(String cep) {
        return new RestTemplate().getForEntity("https://viacep.com.br/ws/" + cep + "/json", CepDTO.class).getBody();
    }

    public ConsultaCnpjDTO consultaCnpjReceitaWS(String cnpj){
        return new RestTemplate().getForEntity("http://receitaws.com.br/v1/cnpj/"+ cnpj, ConsultaCnpjDTO.class).getBody();

    }

}
