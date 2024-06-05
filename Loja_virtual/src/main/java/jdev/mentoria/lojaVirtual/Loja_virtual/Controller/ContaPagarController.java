package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.ContaPagar;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Produto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.ContaPagarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ContaPagarController {

    private final ContaPagarRepository contaPagarRepository;

    public ContaPagarController(ContaPagarRepository contaPagarRepository) {
        this.contaPagarRepository = contaPagarRepository;
    }


    @ResponseBody
    @PostMapping("/salvarContaPagar")
    public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar) throws ExceptionMentoriaJava {

        if (contaPagar.getEmpresa().getId() == null || contaPagar.getEmpresa().getId() <= 0) {
            throw new ExceptionMentoriaJava("Empresa responsável deve ser informada");
        }

        if(contaPagar.getPessoa_fornecedor().getId() == null || contaPagar.getPessoa_fornecedor().getId() <= 0) {
            throw new ExceptionMentoriaJava("Fornecedor responsável deve ser informada");
        }

        if(contaPagar.getId() == null){
            List<ContaPagar> contaPagarList = contaPagarRepository.buscaContaDescricao(contaPagar.getDescricao().toUpperCase().trim());
            if(!contaPagarList.isEmpty()){
                throw new ExceptionMentoriaJava("Já existe conta a pagar com a mesma descrição.");
            }
        }

        ContaPagar contaPagarSalvo = contaPagarRepository.save(contaPagar);

        return new ResponseEntity<ContaPagar>(contaPagarSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/deleteContaPagar")
    public ResponseEntity<String> deleteContaPagar(@RequestBody ContaPagar contaPagar){
        contaPagarRepository.deleteById(contaPagar.getId());

        return new ResponseEntity<String>("Conta pagar Removido.", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteContaPagarPorId/{id}")
    public ResponseEntity<String> deleteContaPagarPorId(@PathVariable("id") Long id) {

        contaPagarRepository.deleteById(id);

        return new ResponseEntity<String>("Conta Pagar Removido.", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterContaPagar/{id}")
    public ResponseEntity<ContaPagar> obterContaPagar(@PathVariable("id") Long id) throws ExceptionMentoriaJava {

        ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

        if(contaPagar == null){
            throw new ExceptionMentoriaJava("Não encontrou conta a pagar com o código: " + id);
        }

        return new ResponseEntity<ContaPagar>(contaPagar,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarContaPagarDesc/{desc}")
    public ResponseEntity<List<ContaPagar>> buscarContaPagarDesc(@PathVariable("desc") String desc) {

        List<ContaPagar> contaPagar = contaPagarRepository.buscaContaDescricao(desc.toUpperCase());

        return new ResponseEntity<List<ContaPagar>>(contaPagar,HttpStatus.OK);
    }
}
