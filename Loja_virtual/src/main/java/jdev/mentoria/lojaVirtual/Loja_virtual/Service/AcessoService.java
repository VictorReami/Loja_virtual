package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Model.Acesso;
import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.AcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcessoService {

    private final AcessoRepository acessoRepository;

    public AcessoService(AcessoRepository acessoRepository) {
        this.acessoRepository = acessoRepository;
    }

    public List<Acesso> findAll(){
        return this.acessoRepository.findAll();
    }

    public Acesso save(Acesso acesso){
        return acessoRepository.save(acesso);
    }



}
