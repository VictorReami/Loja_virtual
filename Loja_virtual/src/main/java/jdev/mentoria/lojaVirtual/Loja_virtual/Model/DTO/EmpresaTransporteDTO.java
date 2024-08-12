package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class EmpresaTransporteDTO implements Serializable {

    private String id;
    private String nome;
    private String valor;
    private String empresa;
    private String picture;

    public boolean dadosOK() {

        if (id != null && empresa != null && valor != null && nome != null) {
            return true;
        }

        return false;
    }
}
