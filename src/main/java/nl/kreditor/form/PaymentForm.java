package nl.kreditor.form;


import lombok.Data;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class PaymentForm {
    @Positive(message = "Contact is required")
    private int contactId;

    @Positive(message = "A positive amount is required")
    private BigDecimal amount;
}
