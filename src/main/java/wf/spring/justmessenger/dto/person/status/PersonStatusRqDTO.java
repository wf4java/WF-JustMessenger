package wf.spring.justmessenger.dto.person.status;

import lombok.*;
import wf.spring.justmessenger.entity.person.Person;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonStatusRqDTO {

    private Person.Status status;


}
