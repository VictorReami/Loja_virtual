package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CriarWebHook implements Serializable {

    private final static long serialVersionUID = 1L;

    private String url;

    private List<String> eventTypes = new ArrayList<String>();
}
