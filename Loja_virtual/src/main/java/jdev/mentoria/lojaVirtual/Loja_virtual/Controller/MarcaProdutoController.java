package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.MarcaProduto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.MarcaProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarcaProdutoController {

    private final MarcaProdutoRepository marcaProdutoRepository;

    public MarcaProdutoController(MarcaProdutoRepository marcaProdutoRepository) {
        this.marcaProdutoRepository = marcaProdutoRepository;
    }

    @GetMapping("/marcaProduto")
    public List<MarcaProduto> findAll(){
        return marcaProdutoRepository.findAll();
    }

    @ResponseBody
    @PostMapping("/salvarMarcaProduto")
    public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody MarcaProduto marcaProduto) throws ExceptionMentoriaJava {

        if(marcaProduto.getId() == null) {
            List<MarcaProduto> marcaprodutos = marcaProdutoRepository.buscarMarcaDesc(marcaProduto.getNomeDesc().toUpperCase());
            if(!marcaprodutos.isEmpty()){
                throw new ExceptionMentoriaJava("Já existe marca de produto com a descrição: " + marcaProduto.getNomeDesc());

            }
        }

        MarcaProduto marcaProdutoSalvo = this.marcaProdutoRepository.save(marcaProduto);

        return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/deleteMarcaProduto")
    public ResponseEntity<String> deleteMarcaProduto(@RequestBody MarcaProduto marcaProduto){
        marcaProdutoRepository.deleteById(marcaProduto.getId());

        return new ResponseEntity<String>("Marca Produto Removido", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteMarcaProdutoPorId/{id}")
    public ResponseEntity<String> deleteMarcaProdutoPorId(@PathVariable("id") Long id) {

        marcaProdutoRepository.deleteById(id);

        return new ResponseEntity<String>("Marca Produto Removido",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterMarcaProduto/{id}")
    public ResponseEntity<MarcaProduto> obterMarcaProduto(@PathVariable("id") Long id) throws ExceptionMentoriaJava {

        MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

        if(marcaProduto == null){
            throw new ExceptionMentoriaJava("Não encontrou marca produto com o código: " + id);
        }

        return new ResponseEntity<MarcaProduto>(marcaProduto,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarMarcaProdutoPorDesc/{desc}")
    public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoPorDesc(@PathVariable("desc") String desc) {

        List<MarcaProduto> marcaProdutos = marcaProdutoRepository.buscarMarcaDesc(desc.toUpperCase());

        return new ResponseEntity<List<MarcaProduto>>(marcaProdutos,HttpStatus.OK);
    }
}
