package nl.kreditor.form;


import lombok.Data;
import nl.kreditor.model.AllotmentType;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class AuthenticationForm {
    @NotBlank(message = "Please enter a valid email address")
    @Email()
    @Size(min = 4, max = 30)
    private String username; //todo: better validator for usernames

    @NotBlank(message = "Please enter a password")
    @Size(min = 8, max = 40)
    private String password;
}
