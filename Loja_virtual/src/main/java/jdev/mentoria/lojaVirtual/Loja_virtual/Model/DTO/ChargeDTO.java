package jdev.mentoria.lojaVirtual.Loja_virtual.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChargeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pixKey;
    private boolean pixIncludeImage = true;
    private String description;
    private List<String> references = new ArrayList<String>();
    private Float amount;
    private String dueDate;
    private Integer installments;
    private Integer maxOverdueDays;
    private BigDecimal fine;
    private BigDecimal interest;
    private List<String> paymentTypes = new ArrayList<String>();


}
