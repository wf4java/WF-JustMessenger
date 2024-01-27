package wf.spring.justmessenger.dto.person.status;

import lombok.*;
import org.bson.types.ObjectId;
import wf.spring.justmessenger.entity.person.Person;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonStatusRsDTO {

    private ObjectId personId;

    private Person.Status status;

}
