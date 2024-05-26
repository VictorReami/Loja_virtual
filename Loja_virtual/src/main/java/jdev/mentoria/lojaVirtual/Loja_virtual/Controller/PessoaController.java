package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaFisica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.PessoaUsuarioService;
import jdev.mentoria.lojaVirtual.Loja_virtual.Util.ValidaCNPJ;
import jdev.mentoria.lojaVirtual.Loja_virtual.Util.ValidaCPF;
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

        if (pessoaJuridica == null) {
            throw new ExceptionMentoriaJava("Pessoa juridica nao pode ser NULL");
        }

        if (pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
            throw new ExceptionMentoriaJava("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
        }


        if (pessoaJuridica.getId() == null && pessoaRepository.existeInsEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
            throw new ExceptionMentoriaJava("Já existe Inscrição estadual cadastrado com o número: " + pessoaJuridica.getInscEstadual());
        }


        if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
            throw new ExceptionMentoriaJava("Cnpj : " + pessoaJuridica.getCnpj() + " está inválido.");
        }

        pessoaJuridica = pessoaUsuarioService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
    }

    /*end-point é microsservicos é um API*/
    @ResponseBody
    @PostMapping(value = "/salvarPf")
    public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionMentoriaJava{

        if (pessoaFisica == null) {
            throw new ExceptionMentoriaJava("Pessoa fisica não pode ser NULL");
        }

        if (pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
            throw new ExceptionMentoriaJava("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());
        }


        if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
            throw new ExceptionMentoriaJava("CPF : " + pessoaFisica.getCpf() + " está inválido.");
        }

        pessoaFisica = pessoaUsuarioService.salvarPessoaFisica(pessoaFisica);

        return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
    }


}
