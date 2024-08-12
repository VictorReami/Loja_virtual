package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BillingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean free;

    private boolean database;
}
