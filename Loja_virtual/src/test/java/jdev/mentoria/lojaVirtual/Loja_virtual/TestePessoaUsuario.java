package jdev.mentoria.lojaVirtual.Loja_virtual;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaFisica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.PessoaUsuarioService;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

    @Autowired
    private  PessoaUsuarioService pessoaUsuarioService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testeCadPessoa(){


        PessoaJuridica pessoaJuridica = new PessoaJuridica();

        pessoaJuridica.setCnpj("898951951");
        pessoaJuridica.setNome("Victor Reami");
        pessoaJuridica.setEmail("Victor.mreami@gmail.com");
        pessoaJuridica.setTelefone("1651651");
        pessoaJuridica.setInscEstadual("9.62.6262+");
        pessoaJuridica.setInscMunicipal("655665565");
        pessoaJuridica.setNomeFantasia("498asdasdasd");
        pessoaJuridica.setRazaoSocial("5555555");

        pessoaRepository.save(pessoaJuridica);

/*
        PessoaFisica pessoaFisica = new PessoaFisica();

        pessoaFisica.setCpf("898951951");
        pessoaFisica.setNome("Victor Reami");
        pessoaFisica.setEmail("Victor.mreami@gmail.com");
        pessoaFisica.setTelefone("1651651");
        pessoaFisica.setEmpresa();*/


    }




}
