package jdev.mentoria.lojaVirtual.Loja_virtual.Controller;

import jdev.mentoria.lojaVirtual.Loja_virtual.ExceptionMentoriaJava;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Model.StatusRastreio;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.StatusRastreioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StatusRastreioController {

    private final StatusRastreioRepository statusRastreioRepository;

    public StatusRastreioController(StatusRastreioRepository statusRastreioRepository) {
        this.statusRastreioRepository = statusRastreioRepository;
    }

    @ResponseBody
    @GetMapping("/listaRastreioVenda/{idVenda}")
    public ResponseEntity<List<StatusRastreio>> listaRastreioVenda(@PathVariable("idVenda") Long idvenda ){

        List<StatusRastreio> statusRastreioList = statusRastreioRepository.listaRastreiovenda(idvenda);

        return new ResponseEntity<List<StatusRastreio>>(statusRastreioList, HttpStatus.OK);
    }





}
