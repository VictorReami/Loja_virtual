package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Service.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AcessoController {
    private final AcessoService acessoService;

    public AcessoController(AcessoService acessoService) {
        this.acessoService = acessoService;
    }

    @GetMapping("/acesso")
    public List<Acesso> findAll(){
        return this.acessoService.findAll();
    }

    @PostMapping("/salvarAcesso")
    public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso){
        Acesso acessoSalvo = this.acessoService.save(acesso);

        return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
    }
}
