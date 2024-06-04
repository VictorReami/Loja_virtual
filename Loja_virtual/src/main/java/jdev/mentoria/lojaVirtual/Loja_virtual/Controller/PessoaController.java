package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import Model.DTO.CepDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Endereco;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaFisica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.EnderecoRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.PessoaUsuarioService;
import jdev.mentoria.lojaVirtual.Loja_virtual.Util.ValidaCNPJ;
import jdev.mentoria.lojaVirtual.Loja_virtual.Util.ValidaCPF;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RestController
public class PessoaController {

    private final PessoaRepository pessoaRepository;
    private final PessoaUsuarioService pessoaUsuarioService;
    private final EnderecoRepository enderecoRepository;

    public PessoaController(PessoaRepository pessoaRepository, PessoaUsuarioService pessoaUsuarioService, EnderecoRepository enderecoRepository) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaUsuarioService = pessoaUsuarioService;
        this.enderecoRepository = enderecoRepository;
    }

    @ResponseBody
    @GetMapping(value = "/consultaCep/{cep}")
    public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
        CepDTO cepDTO = pessoaUsuarioService.consultaCEP(cep);

        return new ResponseEntity<CepDTO>(cepDTO, HttpStatus.OK);

    }

    @ResponseBody
    @PostMapping("/salvarPJ")
    public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava {

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

        if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
                CepDTO cepDTO = pessoaUsuarioService.consultaCEP(pessoaJuridica.getEnderecos().get(p).getCep());

                pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
                pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
                pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
                pessoaJuridica.getEnderecos().get(p).setRuaLogradouro(cepDTO.getLogradouro());
                pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
            }
        } else {
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
                Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

                if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
                    CepDTO cepDTO = pessoaUsuarioService.consultaCEP(pessoaJuridica.getEnderecos().get(p).getCep());

                    pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
                    pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
                    pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
                    pessoaJuridica.getEnderecos().get(p).setRuaLogradouro(cepDTO.getLogradouro());
                    pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

                }
            }
        }

        pessoaJuridica = pessoaUsuarioService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
    }

    /*end-point é microsservicos é um API*/
    @ResponseBody
    @PostMapping(value = "/salvarCpf")
    public ResponseEntity<PessoaFisica> salvarCpf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionMentoriaJava{

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
