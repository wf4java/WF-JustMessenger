package wf.spring.justmessenger.dto.person;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonGetRqDTO {

    @NotNull
    private ObjectId personId;

}
