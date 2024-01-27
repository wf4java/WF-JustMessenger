package wf.spring.justmessenger.dto.person.status;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonStatusGetRqDTO {

    @NotNull
    private ObjectId personId;

}
