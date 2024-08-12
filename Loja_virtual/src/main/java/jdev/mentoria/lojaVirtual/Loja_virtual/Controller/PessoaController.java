package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.Enums.TipoPessoa;
import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.CepDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.ConsultaCnpjDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Endereco;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaFisica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.PessoaJuridica;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.EnderecoRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaFisicaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.PessoaJuridicaRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.PessoaUsuarioService;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.ServiceContagemAcessoAPI;
import jdev.mentoria.lojaVirtual.Loja_virtual.Util.ValidaCNPJ;
import jdev.mentoria.lojaVirtual.Loja_virtual.Util.ValidaCPF;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
public class PessoaController {

    private final PessoaJuridicaRepository pessoaJuridicaRepository;
    private final PessoaUsuarioService pessoaUsuarioService;
    private final EnderecoRepository enderecoRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final ServiceContagemAcessoAPI serviceContagemAcessoAPI;

    public PessoaController(PessoaJuridicaRepository pessoaJuridicaRepository, PessoaUsuarioService pessoaUsuarioService, EnderecoRepository enderecoRepository, PessoaFisicaRepository pessoaFisicaRepository, ServiceContagemAcessoAPI serviceContagemAcessoAPI) {
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.pessoaUsuarioService = pessoaUsuarioService;
        this.enderecoRepository = enderecoRepository;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.serviceContagemAcessoAPI = serviceContagemAcessoAPI;
    }

    @ResponseBody
    @GetMapping(value = "consultaNomePessoaFisica/{nome}")
    public ResponseEntity<List<PessoaFisica>> consultaNomePessoaFisica(@PathVariable("nome") String nome){
        List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());

        serviceContagemAcessoAPI.atualizaAcessoEndPointPF();

        return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "consultaCPF/{cpf}")
    public ResponseEntity<List<PessoaFisica>> consultaCPF(@PathVariable("cpf") String cpf){
        List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.pesquisaPorCPF(cpf.trim().toUpperCase());

        return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "consultaNomePJ/{nome}")
    public ResponseEntity<List<PessoaJuridica>> consultaNomePJ(@PathVariable("nome") String nome){
        List<PessoaJuridica> pessoaJuridicas = pessoaJuridicaRepository.pesquisaPorNomePJ(nome.trim().toUpperCase());

        return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "consultaCNPJ/{cnpj}")
    public ResponseEntity<List<PessoaJuridica>> consultaCNPJ(@PathVariable("cnpj") String cnpj){
        List<PessoaJuridica> pessoaJuridicas = pessoaJuridicaRepository.existeCnpjCadastradoList(cnpj.trim().toUpperCase());

        return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/consultaCep/{cep}")
    public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
        CepDTO cepDTO = pessoaUsuarioService.consultaCEP(cep);

        return new ResponseEntity<CepDTO>(cepDTO, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/consultaCnpjReceitaWs/{cnpj}")
    public ResponseEntity<ConsultaCnpjDTO> consultaCnpjReceitaWs(@PathVariable("cnpj") String cnpj){
        ConsultaCnpjDTO ConsultaCnpjDTO = pessoaUsuarioService.consultaCnpjReceitaWS(cnpj);

        return new ResponseEntity<ConsultaCnpjDTO>(ConsultaCnpjDTO, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/salvarPJ")
    public ResponseEntity<PessoaJuridica> salvarPJ(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava {

        if (pessoaJuridica == null) {
            throw new ExceptionMentoriaJava("Pessoa juridica nao pode ser NULL");
        }

        if(pessoaJuridica.getTipoPessoa() == null){
            throw new ExceptionMentoriaJava("Informe o tipo Jurídico ou Fornecedor da loja.");
        }

        if (pessoaJuridica.getId() == null && pessoaJuridicaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
            throw new ExceptionMentoriaJava("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
        }


        if (pessoaJuridica.getId() == null && pessoaJuridicaRepository.existeInsEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
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

        if(pessoaFisica.getTipoPessoa() == null){
            pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
        }

        if (pessoaFisica.getId() == null && pessoaJuridicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
            throw new ExceptionMentoriaJava("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());
        }


        if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
            throw new ExceptionMentoriaJava("CPF : " + pessoaFisica.getCpf() + " está inválido.");
        }

        pessoaFisica = pessoaUsuarioService.salvarPessoaFisica(pessoaFisica);

        return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
    }


}
