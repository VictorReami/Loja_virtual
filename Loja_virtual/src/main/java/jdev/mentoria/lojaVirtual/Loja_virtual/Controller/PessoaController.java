package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.PessoaUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    private final PessoaUsuarioService pessoaUsuarioService;

    public PessoaController(PessoaRepository pessoaRepository, PessoaUsuarioService pessoaUsuarioService) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaUsuarioService = pessoaUsuarioService;
    }

    @ResponseBody
    @PostMapping("/salvarPJ")
    public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava {

        if(pessoaJuridica == null){
            throw new ExceptionMentoriaJava("Pessoa juridica não pode ser NULL");
        } else if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
            throw new ExceptionMentoriaJava("Já existe CNPJ cadastrado com o número " + pessoaJuridica.getCnpj());
        }

        pessoaJuridica = pessoaUsuarioService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
    }


}
