package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Produto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @ResponseBody
    @PostMapping("/salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionMentoriaJava {

        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            throw new ExceptionMentoriaJava("Empresa responsável deve ser informada");
        }

        if (produto.getId() == null) {
            List<Produto> produtos  = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());

            if (!produtos.isEmpty()) {
                throw new ExceptionMentoriaJava("Já existe Produto com a descrição: " + produto.getNome());
            }
        }


        if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
            throw new ExceptionMentoriaJava("Categoria deve ser informada");
        }


        if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
            throw new ExceptionMentoriaJava("Marca deve ser informada");
        }

        Produto produtoSalvo = produtoRepository.save(produto);

        return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/deleteProduto")
    public ResponseEntity<String> deleteProduto(@RequestBody Produto produto){
        produtoRepository.deleteById(produto.getId());

        return new ResponseEntity<String>("Produto Removido", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteProdutoPorId/{id}")
    public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id) {

        produtoRepository.deleteById(id);

        return new ResponseEntity<String>("Produto Removido",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterProduto/{id}")
    public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionMentoriaJava {

        Produto produto = produtoRepository.findById(id).orElse(null);

        if(produto == null){
            throw new ExceptionMentoriaJava("Não encontrou o produto com o código: " + id);
        }

        return new ResponseEntity<Produto>(produto,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarProdutoNome/{desc}")
    public ResponseEntity<List<Produto>> buscarProdutoNome(@PathVariable("desc") String desc) {

        List<Produto> produtos = produtoRepository.buscarProdutoNome(desc.toUpperCase());

        return new ResponseEntity<List<Produto>>(produtos,HttpStatus.OK);
    }
}
