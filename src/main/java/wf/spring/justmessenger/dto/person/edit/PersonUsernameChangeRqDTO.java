package wf.spring.justmessenger.dto.person.edit;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import wf.spring.justmessenger.utils.validators.annotation.Username;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonUsernameChangeRqDTO {

    @NotNull
    @Username
    private String username;

}
