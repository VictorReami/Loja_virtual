package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.CupomDesc;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.CupomDescRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CupomDescController {
    private final CupomDescRepository cupomDescRepository;

    public CupomDescController(CupomDescRepository cupomDescRepository) {
        this.cupomDescRepository = cupomDescRepository;
    }

    @ResponseBody
    @PostMapping("/salvarCupomDesc")
    public ResponseEntity<CupomDesc> salvarCupomDesc(@RequestBody @Valid CupomDesc cupomDesc) throws ExceptionMentoriaJava {

        CupomDesc CupomDescSalvo = this.cupomDescRepository.save(cupomDesc);

        return new ResponseEntity<CupomDesc>(CupomDescSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteCupomDescPorId/{id}")
    public ResponseEntity<?> deleteCupomDescPorId(@PathVariable("id")  Long id) {

        cupomDescRepository.deleteById(id);

        return new ResponseEntity("Cupom desconto Removido.",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterCupomDesc/{id}")
    public ResponseEntity<CupomDesc> obterCupomDesc(@PathVariable("id") Long id) throws ExceptionMentoriaJava {

        CupomDesc cupomDesc = cupomDescRepository.findById(id).orElse(null);

        if(cupomDesc == null){
            throw new ExceptionMentoriaJava("Não encontrou o cupom desconto com o código: " + id);
        }

        return new ResponseEntity<CupomDesc>(cupomDesc,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/cupomDescontoList")
    public ResponseEntity<List<CupomDesc>> cupomDescontoList(){

        List<CupomDesc> cupomDescList = cupomDescRepository.findAll();

        return new ResponseEntity<List<CupomDesc>>(cupomDescList, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "/buscaCupomDescontoPorEmpresa/{idEmpresa}")
    public ResponseEntity<List<CupomDesc>> buscaCupomDescontoPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa){

        List<CupomDesc> cupomDescList = cupomDescRepository.buscaCupomDescontoPorEmpresa(idEmpresa);

        return new ResponseEntity<List<CupomDesc>>(cupomDescList, HttpStatus.OK);
    }

}
