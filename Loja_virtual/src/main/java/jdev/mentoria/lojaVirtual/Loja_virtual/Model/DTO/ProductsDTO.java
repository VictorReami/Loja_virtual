package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String width;
    private String height;
    private String length;
    private String weigth;
    private String insurance_value;
    private String quantity;
}
