package jdev.mentoria.lojaVirtual.Loja_virtual;

import jdev.mentoria.lojaVirtual.Loja_virtual.Controller.PessoaController;
import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.TipoEndereco;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Endereco;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaFisica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.PessoaUsuarioService;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Calendar;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

    @Autowired
    private PessoaController pessoaController;

    @Test
    public void testeCadPessoa() throws ExceptionMentoriaJava {


        PessoaJuridica pessoaJuridica = new PessoaJuridica();

        pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setNome("Victor Reamii");
        pessoaJuridica.setEmail("vitor.molina1@gmail.com");
        pessoaJuridica.setTelefone("1651651");
        pessoaJuridica.setInscEstadual("9.62.6262+");
        pessoaJuridica.setInscMunicipal("655665565");
        pessoaJuridica.setNomeFantasia("498asdasdasd");
        pessoaJuridica.setRazaoSocial("5555555");

        Endereco endereco1 = new Endereco();
        endereco1.setBairro("Pq novo mundo");
        endereco1.setCep("123123");
        endereco1.setComplemento("apto");
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setNumero("45");
        endereco1.setPessoa(pessoaJuridica);
        endereco1.setRuaLogradouro("Rua Itu");
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setUf("Sp");
        endereco1.setCidade("Americana");


        Endereco endereco2 = new Endereco();
        endereco2.setBairro("Vila Bertini");
        endereco2.setCep("123123333");
        endereco2.setComplemento("apto");
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setNumero("45asda");
        endereco2.setPessoa(pessoaJuridica);
        endereco2.setRuaLogradouro("Rua aaaaa");
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setUf("Sp");
        endereco2.setCidade("Americana");


        pessoaJuridica.getEnderecos().add(endereco2);
        pessoaJuridica.getEnderecos().add(endereco1);

        pessoaJuridica = pessoaController.salvarPJ(pessoaJuridica).getBody();

        assertEquals(true, pessoaJuridica.getId() > 0);

        for (Endereco endereco : pessoaJuridica.getEnderecos() ){
            assertEquals(true, endereco.getId() > 0);
        }

        assertEquals(2, pessoaJuridica.getEnderecos().size());

/*
        PessoaFisica pessoaFisica = new PessoaFisica();

        pessoaFisica.setCpf("898951951");
        pessoaFisica.setNome("Victor Reami");
        pessoaFisica.setEmail("Victor.mreami@gmail.com");
        pessoaFisica.setTelefone("1651651");
        pessoaFisica.setEmpresa();*/


    }
}
