package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CobrancaJunoApiDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ChargeDTO charge = new ChargeDTO();

    private BillingJunoDTO billing = new BillingJunoDTO();

}
