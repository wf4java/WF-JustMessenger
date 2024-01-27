package wf.spring.justmessenger.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import wf.spring.justmessenger.utils.validators.annotation.Password;
import wf.spring.justmessenger.utils.validators.annotation.Username;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthenticationRqDTO {

    @NotNull
    @Username
    private String username;

    @NotNull
    @Password
    private String password;


}
