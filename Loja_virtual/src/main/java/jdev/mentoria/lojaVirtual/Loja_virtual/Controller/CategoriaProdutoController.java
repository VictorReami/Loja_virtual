package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.CategoriaProduto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.CategoriaProdutoDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.CategoriaProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoriaProdutoController {

    private final CategoriaProdutoRepository categoriaProdutoRepository;

    public CategoriaProdutoController(CategoriaProdutoRepository categoriaProdutoRepository) {
        this.categoriaProdutoRepository = categoriaProdutoRepository;
    }

    @ResponseBody
    @GetMapping(value = "/buscarPorDescCatgoria/{desc}")
    public ResponseEntity<List<CategoriaProduto>> buscarPorDesc(@PathVariable("desc") String desc) {

        List<CategoriaProduto> acesso = categoriaProdutoRepository.buscarCategoriaDes(desc.toUpperCase());

        return new ResponseEntity<List<CategoriaProduto>>(acesso,HttpStatus.OK);
    }

    @ResponseBody /*Poder dar um retorno da API*/
    @PostMapping(value = "/deleteCategoria") /*Mapeando a url para receber JSON*/
    public ResponseEntity<?> deleteAcesso(@RequestBody CategoriaProduto categoriaProduto) { /*Recebe o JSON e converte pra Objeto*/

        if (categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {
            return new ResponseEntity("Categoria já foi removida",HttpStatus.OK);
        }

        categoriaProdutoRepository.deleteById(categoriaProduto.getId());

        return new ResponseEntity("Categoria Removida",HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/salvarCategoria")
    public ResponseEntity<CategoriaProdutoDTO> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionMentoriaJava{

        if (categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {
            throw new ExceptionMentoriaJava("A empresa deve ser informada.");
        }

        if (categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc()) == true) {
            throw new ExceptionMentoriaJava("Não pode cadastar categoria com mesmo nome.");
        }


        CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);

        CategoriaProdutoDTO catgoriaProdutoDto = new CategoriaProdutoDTO();
        catgoriaProdutoDto.setId(categoriaSalva.getId());
        catgoriaProdutoDto.setNomeDesc(categoriaSalva.getNomeDesc());
        catgoriaProdutoDto.setEmpresa(categoriaSalva.getEmpresa().getId().toString());

        return new ResponseEntity<CategoriaProdutoDTO>(catgoriaProdutoDto, HttpStatus.OK);
    }


}
