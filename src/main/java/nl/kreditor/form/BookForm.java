package nl.kreditor.form;


import lombok.Data;
import org.hibernate.validator.constraints.Currency;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class BookForm {
    @NotBlank(message = "A book should have a name")
    @Size(min = 2, max = 30)
    private String name;

    @NotBlank(message = "A book should have a currency code")
    @Size(min = 3, max = 3)
    private String currencyCode;
}
