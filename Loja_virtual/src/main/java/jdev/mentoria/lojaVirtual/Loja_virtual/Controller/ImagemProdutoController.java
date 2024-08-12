package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO.ImagemProdutoDTO;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.ImagemProduto;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.ImagemProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ImagemProdutoController {

    private final ImagemProdutoRepository imagemProdutoRepository;

    public ImagemProdutoController(ImagemProdutoRepository imagemProdutoRepository) {
        this.imagemProdutoRepository = imagemProdutoRepository;
    }

    @ResponseBody
    @PostMapping(value = "/salvarImagemProduto")
    public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody ImagemProduto imagemProduto){

        imagemProduto = imagemProdutoRepository.save(imagemProduto);

        ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
        imagemProdutoDTO.setId(imagemProduto.getId());
        imagemProdutoDTO.setId(imagemProduto.getEmpresa().getId());
        imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
        imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
        imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());

        return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteTodaImagemProduto/{idProduto}")
    public ResponseEntity<String> deleteImagemProduto(@PathVariable("idProduto") Long idProduto ){

        imagemProdutoRepository.deleteImagem(idProduto);

        return new ResponseEntity<String>("Imagens do produto removida", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteImagemProduto")
    public ResponseEntity<String> deleteImagemProduto(@RequestBody ImagemProduto imagemProduto ){

        if(!imagemProdutoRepository.existsById(imagemProduto.getId())){
            return new ResponseEntity<String>("Imagem já foi removida ou não existe com esse id: " + imagemProduto.getId(), HttpStatus.OK);
        }

        imagemProdutoRepository.deleteById(imagemProduto.getId());

        return new ResponseEntity<String>("Imagem removida", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteImagemProdutoPorId/{id}")
    public ResponseEntity<String> deleteImagemProdutoPorId(@PathVariable("id") Long id ){

        if(!imagemProdutoRepository.existsById(id) ){
            return new ResponseEntity<String>("Imagem já foi removida ou não existe com esse id: " + id, HttpStatus.OK);
        }

        imagemProdutoRepository.deleteById(id);

        return new ResponseEntity<String>("Imagem removida", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterImagemProduto/{idProduto}")
    public ResponseEntity<List<ImagemProdutoDTO>> obterImagemProduto(@PathVariable("idProduto") Long idProduto){

        List<ImagemProdutoDTO> dtos = new ArrayList<ImagemProdutoDTO>();

        List<ImagemProduto> imagemProdutoList = imagemProdutoRepository.buscaImagemProduto(idProduto);

        for (ImagemProduto imagemProduto : imagemProdutoList) {

            ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
            imagemProdutoDTO.setId(imagemProduto.getId());
            imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
            imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
            imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
            imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());

            dtos.add(imagemProdutoDTO);
        }

        return new ResponseEntity<List<ImagemProdutoDTO>>(dtos, HttpStatus.OK);
    }




}
