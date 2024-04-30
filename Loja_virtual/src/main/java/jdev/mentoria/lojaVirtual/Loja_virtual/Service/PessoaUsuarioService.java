package jdev.mentoria.lojaVirtual.Loja_virtual.Service;

import jdev.mentoria.lojaVirtual.Loja_virtual.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class PessoaUsuarioService {
    private final UsuarioRepository usuarioRepository;

    public PessoaUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

}
