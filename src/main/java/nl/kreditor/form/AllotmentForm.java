package nl.kreditor.form;


import lombok.Data;
import nl.kreditor.model.AllotmentType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class AllotmentForm {
    @NotNull
    private AllotmentType type;

    @Positive(message = "Contact is required")
    private int contactId;

    @Positive(message = "A positive coefficient is required")
    private BigDecimal coefficient = new BigDecimal("0.00000000000000000");

    @Positive(message = "A positive expense is required")
    private BigDecimal expense = new BigDecimal("0.00");
}
