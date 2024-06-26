package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.NotaFiscalCompra;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.NotaFiscalVenda;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.NotaFiscalVendaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotaFiscalVendaController {

    private final NotaFiscalVendaRepository notaFiscalVendaRepository;

    public NotaFiscalVendaController(NotaFiscalVendaRepository notaFiscalVendaRepository) {
        this.notaFiscalVendaRepository = notaFiscalVendaRepository;
    }

    @ResponseBody
    @GetMapping(value = "/obterNotaFiscalVendaList/{idVenda}")
    public ResponseEntity<List<NotaFiscalVenda>> obterNotaFiscalVendaList(@PathVariable("idVenda") Long idVenda) throws ExceptionMentoriaJava {

        List<NotaFiscalVenda> notaFiscalVendaList = notaFiscalVendaRepository.buscaNotaFiscalPorVendaList(idVenda);

        if(notaFiscalVendaList == null){
            throw new ExceptionMentoriaJava("Não encontrou Nota fiscal compra com o código: " + idVenda);
        }

        return new ResponseEntity<List<NotaFiscalVenda>>(notaFiscalVendaList, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterNotaFiscalVenda/{idVenda}")
    public ResponseEntity<NotaFiscalVenda> obterNotaFiscalVenda(@PathVariable("idVenda") Long idVenda) throws ExceptionMentoriaJava {

        NotaFiscalVenda notaFiscalVendaList = notaFiscalVendaRepository.buscaNotaFiscalPorVenda(idVenda);

        if(notaFiscalVendaList == null){
            throw new ExceptionMentoriaJava("Não encontrou Nota fiscal compra com o código: " + idVenda);
        }

        return new ResponseEntity<NotaFiscalVenda>(notaFiscalVendaList, HttpStatus.OK);
    }



}
