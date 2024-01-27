package wf.spring.justmessenger.dto.person.edit;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import wf.spring.justmessenger.utils.validators.annotation.Password;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonPasswordChangeRqDTO {

    @NotNull
    @Password
    private String password;

}

