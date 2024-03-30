package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.AcessoRepository;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
public class AcessoController {
    private final AcessoService acessoService;

    private final AcessoRepository acessoRepository;

    public AcessoController(AcessoService acessoService, AcessoRepository acessoRepository) {
        this.acessoService = acessoService;
        this.acessoRepository = acessoRepository;
    }

    @GetMapping("/acesso")
    public List<Acesso> findAll(){
        return this.acessoService.findAll();
    }

    @ResponseBody
    @PostMapping("/salvarAcesso")
    public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso){
        Acesso acessoSalvo = this.acessoService.save(acesso);

        return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/deleteAcesso")
    public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso){
        this.acessoRepository.deleteById(acesso.getId());

        return new ResponseEntity("Acesso Removido", HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteAcessoPorId/{id}")
    public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) {

        acessoRepository.deleteById(id);

        return new ResponseEntity("Acesso Removido",HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/obterAcesso/{id}")
    public ResponseEntity<Acesso> obterAcesso(@PathVariable("id") Long id) {

        Acesso acesso = acessoRepository.findById(id).get();

        return new ResponseEntity<Acesso>(acesso,HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/buscarPorDesc/{desc}")
    public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) {

        List<Acesso> acesso = acessoRepository.buscarAcessoDesc(desc);

        return new ResponseEntity<List<Acesso>>(acesso,HttpStatus.OK);
    }

}
