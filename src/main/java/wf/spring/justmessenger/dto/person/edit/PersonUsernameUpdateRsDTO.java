package wf.spring.justmessenger.dto.person.edit;

import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonUsernameUpdateRsDTO {

    private ObjectId personId;

    private String username;

}
