package wf.spring.justmessenger.dto.person;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.bson.types.ObjectId;
import wf.spring.justmessenger.utils.validators.annotation.CollectionElementNotNull;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonGetAllRqDTO {

    @NotNull
    @Size(min = 1, max = 100)
    @CollectionElementNotNull
    private List<ObjectId> personIds;

}
