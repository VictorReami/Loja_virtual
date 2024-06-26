package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.CupomDesc;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.CupomDescRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CupomDescController {
    private final CupomDescRepository cupomDescRepository;

    public CupomDescController(CupomDescRepository cupomDescRepository) {
        this.cupomDescRepository = cupomDescRepository;
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
