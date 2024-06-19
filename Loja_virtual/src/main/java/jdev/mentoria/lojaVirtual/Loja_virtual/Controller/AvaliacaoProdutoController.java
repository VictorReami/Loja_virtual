package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.AvaliacaoProduto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.AvaliacaoProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AvaliacaoProdutoController {

  private final AvaliacaoProdutoRepository avaliacaoProdutoRepository;

    public AvaliacaoProdutoController(AvaliacaoProdutoRepository avaliacaoProdutoRepository) {
        this.avaliacaoProdutoRepository = avaliacaoProdutoRepository;
    }


    @ResponseBody
    @PostMapping(value = "/salvarAvaliacaoProduto")
    public ResponseEntity<AvaliacaoProduto> salvarAvaliacaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionMentoriaJava {

        if(avaliacaoProduto.getEmpresa() == null || avaliacaoProduto.getEmpresa().getId() <= 0){
            throw new ExceptionMentoriaJava("Informar a empresa dona do registro.");
        }

        if( avaliacaoProduto.getProduto() == null || avaliacaoProduto.getProduto().getId() <= 0 ){
            throw new ExceptionMentoriaJava("A avaliação deve conter o produto associado.");
        }

        if( avaliacaoProduto.getPessoa() == null || avaliacaoProduto.getPessoa().getId() <= 0 ){
            throw new ExceptionMentoriaJava("A avaliação deve conter a pessoa ou cliente associado.");
        }

        avaliacaoProduto = avaliacaoProdutoRepository.save(avaliacaoProduto);

        return new ResponseEntity<AvaliacaoProduto>(avaliacaoProduto, HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteAvaliacaoProduto/{idAvaliacao}")
    public ResponseEntity<String> deleteAvaliacaoProduto(@PathVariable("idAvaliacao") Long idAvaliacao) {

        avaliacaoProdutoRepository.deleteById(idAvaliacao);

        return new ResponseEntity<String>("Avaliacao Removida.",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarAvalicaoProduto/{idProduto}")
    public ResponseEntity<List<AvaliacaoProduto>> buscarAvalicaoProduto(@PathVariable("idProduto") Long idProduto) throws ExceptionMentoriaJava {

        List<AvaliacaoProduto> avaliacaoProdutoList = avaliacaoProdutoRepository.avalicaoProduto(idProduto);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutoList,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarAvalicaoPessoa/{idPessoa}")
    public ResponseEntity<List<AvaliacaoProduto>> buscarAvalicaoPessoa(@PathVariable("idPessoa") Long idPessoa) throws ExceptionMentoriaJava {

        List<AvaliacaoProduto> avaliacaoProdutoList = avaliacaoProdutoRepository.avalicaoPessoa(idPessoa);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutoList,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarAvalicaoProdutoPessoa/{idProduto}/{idPessoa}")
    public ResponseEntity<List<AvaliacaoProduto>> buscarAvalicaoProdutoPessoa(@PathVariable("idProduto") Long idProduto, @PathVariable("idPessoa") Long idPessoa) throws ExceptionMentoriaJava {

        List<AvaliacaoProduto> avaliacaoProdutoList = avaliacaoProdutoRepository.avalicaoProdutoPessoa(idProduto, idPessoa);

        return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutoList,HttpStatus.OK);
    }



}
