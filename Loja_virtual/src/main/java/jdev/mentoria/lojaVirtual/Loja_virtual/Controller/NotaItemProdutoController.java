package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.MarcaProduto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.NotaItemProduto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.NotaItemProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class NotaItemProdutoController {

    private final NotaItemProdutoRepository notaItemProdutoRepository;

    public NotaItemProdutoController(NotaItemProdutoRepository notaItemProdutoRepository) {
        this.notaItemProdutoRepository = notaItemProdutoRepository;
    }

    @ResponseBody
    @PostMapping(value = "/salvarNotaItemProduto")
    public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto)throws ExceptionMentoriaJava {

        if(notaItemProduto.getId() == null) {

            if(notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0){
                throw new ExceptionMentoriaJava("O produto deve ser informado.");
            }

            if(notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0){
                throw new ExceptionMentoriaJava("A nota fiscal deve ser informada.");
            }

            if(notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0){
                throw new ExceptionMentoriaJava("A empresa deve ser informada.");
            }

            List<NotaItemProduto> notaItemProdutoList = notaItemProdutoRepository.buscaNotaItemPorProdutoNota(notaItemProduto.getId(), notaItemProduto.getNotaFiscalCompra().getId());
            if(notaItemProdutoList.size() != 0){
                throw new ExceptionMentoriaJava("Já existe este produto para esta nota.");
            }

        }

        if(notaItemProduto.getQuantidade() <= 0){
            throw new ExceptionMentoriaJava("Informe a quantidade do produto.");
        }

        NotaItemProduto notaItemProdutoSalvo = notaItemProdutoRepository.save(notaItemProduto);

        //Para retornar as informações qando salvo
        //notaItemProdutoSalvo = notaItemProdutoRepository.findById(notaItemProduto.getId()).get();

        return new ResponseEntity<NotaItemProduto>(notaItemProdutoSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/deleteNotaItemProduto")
    public ResponseEntity<String> deleteNotaItemProduto(@RequestBody NotaItemProduto notaItemProduto){
        notaItemProdutoRepository.deleteById(notaItemProduto.getId());

        return new ResponseEntity<String>("Nota Item produto Removido.", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteNotaItemProdutoPorId/{id}")
    public ResponseEntity<String> deleteNotaItemProdutoPorId(@PathVariable("id") Long id) {

        notaItemProdutoRepository.deleteById(id);

        return new ResponseEntity<String>("Nota Item produto Removido",HttpStatus.OK);
    }


}
