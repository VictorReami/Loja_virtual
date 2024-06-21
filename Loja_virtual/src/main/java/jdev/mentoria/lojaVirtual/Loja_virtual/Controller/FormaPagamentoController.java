package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.FormaPagamento;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.FormaPagamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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




}
