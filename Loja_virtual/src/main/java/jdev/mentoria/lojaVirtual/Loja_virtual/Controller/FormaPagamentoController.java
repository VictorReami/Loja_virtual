package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.FormaPagamento;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.FormaPagamentoRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FormaPagamentoController {

    private final FormaPagamentoRepository formaPagamentoRepository;

    public FormaPagamentoController(FormaPagamentoRepository formaPagamentoRepository) {
        this.formaPagamentoRepository = formaPagamentoRepository;
    }

    @ResponseBody
    @PostMapping(value = "/salvarFormaPagamento")
    public ResponseEntity<FormaPagamento> salvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) throws ExceptionMentoriaJava {

        formaPagamento = formaPagamentoRepository.save(formaPagamento);

        return new ResponseEntity<FormaPagamento>(formaPagamento, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping(value = "/formaPagamentoList")
    public ResponseEntity<List<FormaPagamento>> formaPagamentoList(){

        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();

        return new ResponseEntity<List<FormaPagamento>>(formaPagamentoList ,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/formaPagamentoIdEmpresa/{idempresa}")
    public ResponseEntity<List<FormaPagamento>> formaPagamentoIdEmpresa(@PathVariable("idempresa") Long idempresa){

        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.buscaPagamentosPorEmpresa(idempresa);

        return new ResponseEntity<List<FormaPagamento>>(formaPagamentoList ,HttpStatus.OK);
    }




}
