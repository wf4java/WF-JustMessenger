package wf.spring.justmessenger.dto.person;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonSearchRqDTO {

    @NotNull
    @Size(min = 1)
    private String username;

    @Max(20)
    @Min(1)
    private int limit = 5;

}
