package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConsultaFreteDTO implements Serializable {

    private FromDTO from;

    private ToDTO to;

    private List<ProductsDTO> products = new ArrayList<ProductsDTO>();
}
