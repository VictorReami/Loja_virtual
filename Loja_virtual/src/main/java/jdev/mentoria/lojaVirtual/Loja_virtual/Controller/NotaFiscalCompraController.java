package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.NotaFiscalCompraRelatorioDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.RelatorioStatusCompra;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.MarcaProduto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.NotaFiscalCompra;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.NotaFiscalCompraRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.NotaItemProdutoRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.NotaFiscalCompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NotaFiscalCompraController {
    private final NotaFiscalCompraRepository notaFiscalCompraRepository;
    private final NotaItemProdutoRepository notaItemProdutoRepository;
    private final NotaFiscalCompraService notaFiscalCompraService;

    public NotaFiscalCompraController(NotaFiscalCompraRepository notaFiscalCompraRepository, NotaItemProdutoRepository notaItemProdutoRepository, NotaFiscalCompraService notaFiscalCompraService) {
        this.notaFiscalCompraRepository = notaFiscalCompraRepository;
        this.notaItemProdutoRepository = notaItemProdutoRepository;
        this.notaFiscalCompraService = notaFiscalCompraService;
    }

    @ResponseBody
    @PostMapping(value = "/relatorioStatusCompra")
    public ResponseEntity<List<RelatorioStatusCompra>> relatorioStatusCompra (@Valid @RequestBody  RelatorioStatusCompra relatorioStatusCompra){

        List<RelatorioStatusCompra> retorno = new ArrayList<RelatorioStatusCompra>();

        retorno = notaFiscalCompraService.relatorioStatusVendaLojaVirtual(relatorioStatusCompra);

        return new ResponseEntity<List<RelatorioStatusCompra>>(retorno, HttpStatus.OK);

    }



    @ResponseBody
    @GetMapping(value = "/relatorioNotaFiscalCompraProduto")
    public ResponseEntity<List<NotaFiscalCompraRelatorioDTO>> relatorioNotaFiscalCompraProduto(@RequestBody @Valid NotaFiscalCompraRelatorioDTO notaFiscalCompraRelatorioDTO){

        List<NotaFiscalCompraRelatorioDTO> notaFiscalCompraRelatorioDTOList = new ArrayList<NotaFiscalCompraRelatorioDTO>();

        notaFiscalCompraRelatorioDTOList = notaFiscalCompraService.geraRelatorioNotaFiscalCompraProduto(notaFiscalCompraRelatorioDTO);


        return new ResponseEntity<List<NotaFiscalCompraRelatorioDTO>>(notaFiscalCompraRelatorioDTOList, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO")
    public ResponseEntity<List<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO>> relatorioNotaFiscalCompraProduto(@RequestBody @Valid NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO){

        List<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO> notaFiscalCompraRelatorioProdutoAlertaEstoqueDTOList = new ArrayList<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO>();

        notaFiscalCompraRelatorioProdutoAlertaEstoqueDTOList = notaFiscalCompraService.gerarRelatorioAlertaEstoque(notaFiscalCompraRelatorioProdutoAlertaEstoqueDTO);


        return new ResponseEntity<List<NotaFiscalCompraRelatorioProdutoAlertaEstoqueDTO>>(notaFiscalCompraRelatorioProdutoAlertaEstoqueDTOList, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/salvarNotaFiscalCompra")
    public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionMentoriaJava {

        if(notaFiscalCompra.getId() == null){
            if(notaFiscalCompra.getDescricaoObs() != null){
                List<NotaFiscalCompra> notaFiscalCompraList = notaFiscalCompraRepository.buscaNotaFiscalCompraDescicao(notaFiscalCompra.getDescricaoObs().toUpperCase().trim());

                if(notaFiscalCompraList.size() != 0){
                    throw new ExceptionMentoriaJava("Já existe Nota de compra com essa mesma descrição: " + notaFiscalCompra.getDescricaoObs() + " .");
                }
            }
        }

        if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0){
            throw new ExceptionMentoriaJava("A Pessoa Fisica da nota fiscal deve ser informada.");
        }

        if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0){
            throw new ExceptionMentoriaJava("A empresa responsável deve ser informada.");
        }

        if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0){
            throw new ExceptionMentoriaJava("A conta a pagar da nota fiscal deve ser informada.");
        }

        NotaFiscalCompra notaFiscalCompraSalvo = notaFiscalCompraRepository.save(notaFiscalCompra);

        return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/deleteNotaFiscalCompra")
    public ResponseEntity<String> deleteNotaFiscalCompra(@RequestBody NotaFiscalCompra notaFiscalCompra){

        //Deleta os ITENS da Nota fiscal de compra.
        notaItemProdutoRepository.deleteItemNotaFiscalCompra(notaFiscalCompra.getId());

        //Deleta o HEADER da nota fiscal.
        notaFiscalCompraRepository.deleteById(notaFiscalCompra.getId());

        return new ResponseEntity<String>("Nota fiscal compra Removido.",HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteNotaFiscalCompraPorId/{id}")
    public ResponseEntity<String> deleteNotaFiscalCompraPorId(@PathVariable("id") Long id) {

        //Deleta os ITENS da Nota fiscal de compra.
        notaItemProdutoRepository.deleteItemNotaFiscalCompra(id);

        //Deleta o HEADER da nota fiscal.
        notaFiscalCompraRepository.deleteById(id);

        return new ResponseEntity<String>("Nota fiscal compra Removido.",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterNotaFiscalCompra/{id}")
    public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompra(@PathVariable("id") Long id) throws ExceptionMentoriaJava {

        NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

        if(notaFiscalCompra == null){
            throw new ExceptionMentoriaJava("Não encontrou Nota fiscal compra com o código: " + id);
        }

        return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorDesc/{desc}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorDesc(@PathVariable("desc") String desc) {

        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscaNotaFiscalCompraDescicao(desc.toUpperCase());

        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra, HttpStatus.OK);
    }






}
