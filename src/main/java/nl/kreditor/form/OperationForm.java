package nl.kreditor.form;


import lombok.Data;
import nl.kreditor.model.OperationType;
import nl.kreditor.model.PaymentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class OperationForm {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 30)
    private String name;

    private OperationType type;

    private PaymentType paymentType;

    @Positive(message = "Choose a category")
    private int categoryId;
}
