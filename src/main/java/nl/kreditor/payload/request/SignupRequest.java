package nl.kreditor.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 1, max = 70)
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    @Email
    private String username;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 80)
    private String password;
}
